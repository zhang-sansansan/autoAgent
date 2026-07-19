const state = {
  controller: null,
  running: false,
  sessionId: '',
  eventCount: 0,
  lastType: '',
};

const elements = {
  form: document.getElementById('agentForm'),
  agentId: document.getElementById('agentId'),
  sessionId: document.getElementById('sessionId'),
  maxStep: document.getElementById('maxStep'),
  message: document.getElementById('message'),
  submitBtn: document.getElementById('submitBtn'),
  stopBtn: document.getElementById('stopBtn'),
  clearBtn: document.getElementById('clearBtn'),
  fillExampleBtn: document.getElementById('fillExampleBtn'),
  copyBtn: document.getElementById('copyBtn'),
  apiStatus: document.getElementById('apiStatus'),
  streamMeta: document.getElementById('streamMeta'),
  completedBadge: document.getElementById('completedBadge'),
  finalSummary: document.getElementById('finalSummary'),
  rawLog: document.getElementById('rawLog'),
  analysisList: document.getElementById('analysisList'),
  executionList: document.getElementById('executionList'),
  supervisionList: document.getElementById('supervisionList'),
  summaryList: document.getElementById('summaryList'),
};

const sectionMap = {
  analysis: elements.analysisList,
  execution: elements.executionList,
  supervision: elements.supervisionList,
  summary: elements.summaryList,
  error: elements.summaryList,
  complete: elements.summaryList,
};

const titleMap = {
  analysis_status: '任务状态分析',
  analysis_history: '执行历史评估',
  analysis_strategy: '下一步策略',
  analysis_progress: '完成度评估',
  analysis_task_status: '任务状态',
  execution_target: '执行目标',
  execution_process: '执行过程',
  execution_result: '执行结果',
  execution_quality: '质量检查',
  assessment: '质量评估',
  issues: '问题识别',
  suggestions: '改进建议',
  score: '质量评分',
  pass: '是否通过',
  completed_work: '已完成内容',
  incomplete_reasons: '未完成原因',
  key_factors: '关键因素',
  efficiency_quality: '执行效率',
  evaluation: '整体评估',
  summary_overview: '最终总结',
};

function createSessionId() {
  return `demo-${Date.now()}`;
}

function nowTime() {
  return new Date().toLocaleTimeString('zh-CN', { hour12: false });
}

function setStatus(text, mode = 'idle') {
  elements.apiStatus.textContent = text;
  elements.apiStatus.className = `status-badge ${mode}`;
}

function setCompleted(text, mode = 'idle') {
  elements.completedBadge.textContent = text;
  elements.completedBadge.className = `status-badge ${mode}`;
}

function appendLog(line) {
  elements.rawLog.textContent += `${line}\n`;
  elements.rawLog.scrollTop = elements.rawLog.scrollHeight;
}

function clearList(target, placeholder) {
  target.innerHTML = '';
  target.classList.add('empty-state');
  target.textContent = placeholder;
}

function ensureListActive(target) {
  if (target.classList.contains('empty-state')) {
    target.classList.remove('empty-state');
    target.textContent = '';
  }
}

function resetBoard() {
  state.eventCount = 0;
  state.lastType = '';
  elements.streamMeta.textContent = '尚未开始';
  elements.finalSummary.textContent = '这里会展示最终总结结果，方便你向面试官强调“Agent 不仅能流式展示过程，也能沉淀最终答案”。';
  elements.rawLog.textContent = '';
  clearList(elements.analysisList, '等待分析阶段输出…');
  clearList(elements.executionList, '等待执行阶段输出…');
  clearList(elements.supervisionList, '等待监督阶段输出…');
  clearList(elements.summaryList, '等待总结阶段输出…');
  setCompleted('未完成', 'idle');
}

function escapeHtml(text = '') {
  return text
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;');
}

function addEventCard(payload) {
  const target = sectionMap[payload.type] || elements.summaryList;
  ensureListActive(target);

  const card = document.createElement('article');
  const type = payload.type || 'summary';
  card.className = `event-card ${type === 'complete' ? 'summary' : type}`;

  const title = titleMap[payload.subType] || payload.subType || payload.type || '事件';
  const stepText = payload.step ? `Step ${payload.step}` : 'Final';

  card.innerHTML = `
    <div class="event-meta">
      <span>${stepText}</span>
      <span>${nowTime()}</span>
    </div>
    <h5>${escapeHtml(title)}</h5>
    <p>${escapeHtml(payload.content || '')}</p>
  `;

  target.appendChild(card);
}

function parseSseChunk(chunk) {
  const results = [];
  const parts = chunk.split('\n\n');

  for (const part of parts) {
    const lines = part.split('\n');
    const dataLines = lines
      .map((line) => line.trim())
      .filter((line) => line.startsWith('data:'))
      .map((line) => line.slice(5).trim())
      .filter(Boolean);

    for (const line of dataLines) {
      try {
        results.push(JSON.parse(line));
      } catch {
        appendLog(`[${nowTime()}] 非 JSON 数据: ${line}`);
      }
    }
  }

  return results;
}

function handleEvent(payload) {
  state.eventCount += 1;
  state.lastType = payload.type || state.lastType;
  elements.streamMeta.textContent = `Session ${state.sessionId} · 已接收 ${state.eventCount} 条事件 · 当前阶段 ${payload.type || '-'}`;

  appendLog(`[${nowTime()}] ${JSON.stringify(payload, null, 2)}`);

  if (payload.type === 'summary' && payload.completed && payload.content) {
    elements.finalSummary.textContent = payload.content;
    setCompleted('已生成', 'success');
  }

  if (payload.type === 'complete') {
    setStatus('执行完成', 'success');
    setCompleted('已完成', 'success');
  }

  if (payload.type === 'error') {
    setStatus('执行异常', 'error');
    setCompleted('失败', 'error');
  }

  addEventCard(payload);
}

async function runDemo(requestBody) {
  state.controller = new AbortController();
  state.running = true;
  state.sessionId = requestBody.sessionId;
  setStatus('连接中', 'running');
  setCompleted('运行中', 'running');
  elements.streamMeta.textContent = `Session ${state.sessionId} · 请求已发出`; 

  try {
    const response = await fetch('/api/v1/agent/auto_agent', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'text/event-stream',
      },
      body: JSON.stringify(requestBody),
      signal: state.controller.signal,
    });

    if (!response.ok || !response.body) {
      throw new Error(`请求失败：${response.status}`);
    }

    setStatus('流式输出中', 'running');
    const reader = response.body.getReader();
    const decoder = new TextDecoder('utf-8');
    let buffer = '';

    while (true) {
      const { done, value } = await reader.read();
      if (done) break;
      buffer += decoder.decode(value, { stream: true });

      const events = buffer.split('\n\n');
      buffer = events.pop() || '';

      for (const eventChunk of events) {
        const parsed = parseSseChunk(eventChunk + '\n\n');
        parsed.forEach(handleEvent);
      }
    }

    if (buffer.trim()) {
      parseSseChunk(buffer).forEach(handleEvent);
    }

    if (state.lastType !== 'complete' && state.lastType !== 'error') {
      setStatus('流结束', 'success');
    }
  } catch (error) {
    if (error.name === 'AbortError') {
      appendLog(`[${nowTime()}] 已手动终止请求`);
      setStatus('已终止', 'idle');
      setCompleted('已终止', 'idle');
    } else {
      appendLog(`[${nowTime()}] 请求异常: ${error.message}`);
      elements.finalSummary.textContent = `请求异常：${error.message}\n\n请确认：\n1. 后端服务已启动；\n2. 数据库与模型配置可用；\n3. Agent ID 在数据库中存在。`;
      setStatus('请求失败', 'error');
      setCompleted('失败', 'error');
    }
  } finally {
    state.running = false;
    state.controller = null;
  }
}

function collectFormData() {
  const sessionId = elements.sessionId.value.trim() || createSessionId();
  elements.sessionId.value = sessionId;

  return {
    aiAgentId: elements.agentId.value.trim(),
    sessionId,
    maxStep: Number(elements.maxStep.value || 3),
    message: elements.message.value.trim(),
  };
}

function fillExample() {
  elements.agentId.value = '1';
  elements.maxStep.value = '3';
  elements.sessionId.value = createSessionId();
  elements.message.value = '请基于 Spring AI、RAG 和 MCP，帮我生成一段适合面试时展示的项目亮点说明，并总结这个 Agent 平台的核心价值。';
}

elements.form.addEventListener('submit', async (event) => {
  event.preventDefault();

  if (state.running) {
    appendLog(`[${nowTime()}] 当前已有任务在执行，请先终止后再发起新的请求`);
    return;
  }

  const payload = collectFormData();
  if (!payload.aiAgentId || !payload.message) {
    setStatus('请补全参数', 'error');
    appendLog(`[${nowTime()}] 参数缺失：Agent ID 或任务描述为空`);
    return;
  }

  resetBoard();
  appendLog(`[${nowTime()}] 发起请求 -> ${JSON.stringify(payload)}`);
  await runDemo(payload);
});

elements.stopBtn.addEventListener('click', () => {
  if (state.controller) {
    state.controller.abort();
  }
});

elements.clearBtn.addEventListener('click', () => {
  if (state.controller) {
    state.controller.abort();
  }
  resetBoard();
  setStatus('等待请求', 'idle');
});

elements.fillExampleBtn.addEventListener('click', fillExample);

elements.copyBtn.addEventListener('click', async () => {
  try {
    await navigator.clipboard.writeText(elements.rawLog.textContent || '');
    appendLog(`[${nowTime()}] 已复制日志到剪贴板`);
  } catch (error) {
    appendLog(`[${nowTime()}] 复制失败: ${error.message}`);
  }
});

fillExample();
setStatus('等待请求', 'idle');
resetBoard();
