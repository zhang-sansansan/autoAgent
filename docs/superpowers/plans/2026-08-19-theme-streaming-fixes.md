# ANN Agent Studio Theme and Streaming Fixes Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 修复暗色主题不一致、文字对比度不足和固定 Agent 非真实流式、错误展示 SSE JSON 的问题。

**Architecture:** 前端使用一个独立 SSE 事件解析器把网络分片转换为类型化事件，聊天页面只消费 `content` 增量；后端保持固定 Agent 的中间客户端串行语义，仅将最后一个客户端切换为 Spring AI 内容流。视觉修复集中在全局 Semi UI 暗色覆盖和 Agent 编排作用域内的 Flowgram 覆盖，避免逐页补丁。

**Tech Stack:** React 18、TypeScript、Vitest、Semi UI、styled-components、Flowgram、Spring Boot 3.4、Spring AI 1.0、ResponseBodyEmitter、Reactor Flux、JUnit 4、Mockito。

---

## 文件结构

- Create: `frontend/src/services/chat-stream-parser.ts` — 只负责把 SSE 字符串缓冲解析为类型化事件。
- Create: `frontend/src/services/chat-stream-parser.test.ts` — 覆盖分片、多事件、中文、complete、error 和非法 JSON。
- Modify: `frontend/src/services/chat-service.ts` — 读取 Fetch 流并把解析后的 `content` 增量交给页面。
- Modify: `frontend/src/pages/chat.test.tsx` — 验证多个内容增量进入同一气泡。
- Create: `frontend/src/styles/theme-contract.test.ts` — 锁定固定列、分页、文字和编辑器暗色选择器。
- Modify: `frontend/src/styles/index.css` — Semi UI 全局暗色覆盖和文字对比度。
- Modify: `frontend/src/pages/agent-config.tsx` — 为 Flowgram 增加 `ann-workflow-theme` 作用域。
- Modify: `frontend/src/components/base-node/styles.tsx` — 节点卡片暗色背景、边框和文字。
- Modify: `frontend/src/components/tools/styles.tsx` — 编辑器工具栏暗色背景。
- Modify: `frontend/src/hooks/use-editor-props.tsx` — 小地图画布与节点配色。
- Modify: `ai-agent-station-study-ann-domain/src/main/java/cn/ann/ai/domain/agent/model/entity/AutoAgentExecuteResultEntity.java` — 新增 content 增量事件工厂。
- Modify: `ai-agent-station-study-ann-domain/src/main/java/cn/ann/ai/domain/agent/service/execute/fixed/FixedAgentExecuteStrategy.java` — 最终客户端真实内容流与 SSE 发送。
- Modify: `ai-agent-station-study-ann-app/pom.xml` — 保持默认跳过测试，同时允许命令行显式启用后端测试。
- Create: `ai-agent-station-study-ann-app/src/test/java/cn/ann/ai/test/domain/agent/AutoAgentExecuteResultEntityTest.java` — 验证 content 事件契约。
- Create: `ai-agent-station-study-ann-app/src/test/java/cn/ann/ai/domain/agent/service/execute/fixed/FixedAgentExecuteStrategyTest.java` — 与被测策略使用相同包，验证 content 增量后只发送一次 complete。

### Task 1: 前端 SSE 事件解析器

**Files:**
- Create: `frontend/src/services/chat-stream-parser.ts`
- Create: `frontend/src/services/chat-stream-parser.test.ts`
- Modify: `frontend/src/services/chat-service.ts`

- [ ] **Step 1: 写失败测试，定义事件解析契约**

```ts
import { describe, expect, it } from 'vitest';
import { ChatStreamParser } from './chat-stream-parser';

describe('ChatStreamParser', () => {
  it('parses split SSE JSON and emits content without protocol fields', () => {
    const parser = new ChatStreamParser();
    expect(parser.push('data: {"type":"content","content":"你')).toEqual([]);
    expect(parser.push('好","completed":false}\n\n')).toEqual([
      { type: 'content', content: '你好', completed: false },
    ]);
  });

  it('parses multiple events and keeps complete out of visible content', () => {
    const parser = new ChatStreamParser();
    expect(parser.push(
      'data: {"type":"content","content":"A","completed":false}\n\n' +
      'data: {"type":"complete","content":"执行完成","completed":true}\n\n',
    )).toEqual([
      { type: 'content', content: 'A', completed: false },
      { type: 'complete', content: '执行完成', completed: true },
    ]);
  });

  it('rejects malformed event JSON instead of displaying it', () => {
    const parser = new ChatStreamParser();
    expect(() => parser.push('data: {bad json}\n\n')).toThrow('响应协议解析失败');
  });
});
```

- [ ] **Step 2: 运行测试并确认因模块不存在而失败**

Run: `npx vitest run src/services/chat-stream-parser.test.ts`

Expected: FAIL，提示无法解析 `./chat-stream-parser`。

- [ ] **Step 3: 实现最小的类型化 SSE 缓冲解析器**

```ts
export interface AutoAgentStreamEvent {
  type: 'content' | 'analysis' | 'execution' | 'supervision' | 'summary' | 'error' | 'complete';
  content?: string;
  completed?: boolean;
  sessionId?: string;
  timestamp?: number;
  step?: number;
  subType?: string;
}

export class ChatStreamParser {
  private buffer = '';

  push(text: string): AutoAgentStreamEvent[] {
    this.buffer += text.replace(/\r\n/g, '\n');
    const frames = this.buffer.split('\n\n');
    this.buffer = frames.pop() ?? '';
    return frames.filter(Boolean).map((frame) => this.parseFrame(frame));
  }

  finish(): AutoAgentStreamEvent[] {
    if (!this.buffer.trim()) return [];
    const frame = this.buffer;
    this.buffer = '';
    return [this.parseFrame(frame)];
  }

  private parseFrame(frame: string): AutoAgentStreamEvent {
    const data = frame.split('\n')
      .filter((line) => line.startsWith('data:'))
      .map((line) => line.slice(5).trimStart())
      .join('\n');
    try {
      return JSON.parse(data) as AutoAgentStreamEvent;
    } catch {
      throw new Error('响应协议解析失败');
    }
  }
}
```

在 `chat-service.ts` 中为每个解码分片调用 `parser.push(...)`，流结束时调用 `parser.finish()`；`complete` 不触发 `onChunk`，`error` 抛出其 `content`，其他事件只传递非空 `content`。

- [ ] **Step 4: 运行解析器和聊天页面测试**

Run: `npx vitest run src/services/chat-stream-parser.test.ts src/pages/chat.test.tsx`

Expected: PASS；聊天气泡只出现 `你好，世界`，不出现 `sessionId` 或 `complete` JSON。

- [ ] **Step 5: 提交前端流解析器**

```bash
git add frontend/src/services/chat-stream-parser.ts frontend/src/services/chat-stream-parser.test.ts frontend/src/services/chat-service.ts frontend/src/pages/chat.test.tsx
git commit -m "fix(frontend): parse agent SSE content events"
```

### Task 2: 后端增量事件契约

**Files:**
- Modify: `ai-agent-station-study-ann-domain/src/main/java/cn/ann/ai/domain/agent/model/entity/AutoAgentExecuteResultEntity.java`
- Modify: `ai-agent-station-study-ann-app/pom.xml`
- Create: `ai-agent-station-study-ann-app/src/test/java/cn/ann/ai/test/domain/agent/AutoAgentExecuteResultEntityTest.java`

- [ ] **Step 1: 写失败测试定义 content 事件字段**

```java
package cn.ann.ai.test.domain.agent;

import cn.ann.ai.domain.agent.model.entity.AutoAgentExecuteResultEntity;
import org.junit.Test;

import static org.junit.Assert.*;

public class AutoAgentExecuteResultEntityTest {
    @Test
    public void shouldCreateIncompleteContentDelta() {
        AutoAgentExecuteResultEntity result =
                AutoAgentExecuteResultEntity.createContentResult("你好", "session-1");

        assertEquals("content", result.getType());
        assertEquals("你好", result.getContent());
        assertEquals(Boolean.FALSE, result.getCompleted());
        assertEquals("session-1", result.getSessionId());
        assertNotNull(result.getTimestamp());
    }
}
```

- [ ] **Step 2: 运行测试并确认工厂方法不存在**

Run: `mvn -pl ai-agent-station-study-ann-app -am -DskipTests=false -DfailIfNoTests=false -Dtest=AutoAgentExecuteResultEntityTest test`

Expected: FAIL，提示 `createContentResult` 未定义。

- [ ] **Step 3: 新增增量事件工厂**

```java
public static AutoAgentExecuteResultEntity createContentResult(String content, String sessionId) {
    return AutoAgentExecuteResultEntity.builder()
            .type("content")
            .content(content)
            .completed(false)
            .timestamp(System.currentTimeMillis())
            .sessionId(sessionId)
            .build();
}
```

同步更新实体类型注释，加入 `content(回答增量)`。

- [ ] **Step 4: 运行实体测试**

Run: `mvn -pl ai-agent-station-study-ann-app -am -DskipTests=false -DfailIfNoTests=false -Dtest=AutoAgentExecuteResultEntityTest test`

Expected: PASS。

- [ ] **Step 5: 提交事件契约**

```bash
git add ai-agent-station-study-ann-domain/src/main/java/cn/ann/ai/domain/agent/model/entity/AutoAgentExecuteResultEntity.java ai-agent-station-study-ann-app/src/test/java/cn/ann/ai/test/domain/agent/AutoAgentExecuteResultEntityTest.java
git commit -m "feat(agent): define streaming content event"
```

### Task 3: 固定 Agent 最终客户端真实流式输出

**Files:**
- Modify: `ai-agent-station-study-ann-domain/src/main/java/cn/ann/ai/domain/agent/service/execute/fixed/FixedAgentExecuteStrategy.java`
- Create: `ai-agent-station-study-ann-app/src/test/java/cn/ann/ai/domain/agent/service/execute/fixed/FixedAgentExecuteStrategyTest.java`

- [ ] **Step 1: 写失败测试定义 SSE 发送顺序**

```java
package cn.ann.ai.domain.agent.service.execute.fixed;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FixedAgentExecuteStrategyTest {
    @Test
    public void shouldSendContentDeltasThenOneCompleteEvent() throws Exception {
        ResponseBodyEmitter emitter = mock(ResponseBodyEmitter.class);
        FixedAgentExecuteStrategy strategy = new FixedAgentExecuteStrategy();

        String content = strategy.streamFinalContent(
                Flux.just("你", "", "好"), emitter, "session-1");

        assertEquals("你好", content);
        ArgumentCaptor<Object> payload = ArgumentCaptor.forClass(Object.class);
        verify(emitter, times(3)).send(payload.capture());
        List<Object> values = payload.getAllValues();
        assertTrue(values.get(0).toString().contains("\\\"type\\\":\\\"content\\\""));
        assertTrue(values.get(1).toString().contains("\\\"type\\\":\\\"content\\\""));
        assertTrue(values.get(2).toString().contains("\\\"type\\\":\\\"complete\\\""));
    }
}
```

- [ ] **Step 2: 运行测试并确认流方法不存在**

Run: `mvn -pl ai-agent-station-study-ann-app -am -DskipTests=false -DfailIfNoTests=false -Dtest=FixedAgentExecuteStrategyTest test`

Expected: FAIL，提示 `streamFinalContent` 未定义。

- [ ] **Step 3: 实现最终客户端流与 SSE 发送**

在 `execute` 中按索引区分中间客户端和最终客户端：

```java
for (int index = 0; index < aiAgentClientList.size(); index++) {
    AiAgentClientFlowConfigVO config = aiAgentClientList.get(index);
    ChatClient chatClient = getChatClientByClientId(config.getClientId());
    ChatClient.ChatClientRequestSpec prompt = chatClient
            .prompt(requestParameter.getMessage() + "，" + content)
            .system(s -> s.param("current_date", LocalDate.now().toString()))
            .advisors(a -> a
                    .param(CHAT_MEMORY_CONVERSATION_ID_KEY, requestParameter.getSessionId())
                    .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100));
    boolean finalClient = index == aiAgentClientList.size() - 1;
    content = finalClient
            ? streamFinalContent(prompt.stream().content(), emitter, requestParameter.getSessionId())
            : prompt.call().content();
}
```

新增可测试方法：过滤空增量、立即发送 `createContentResult`、累积完整结果，流结束后发送一次 `createCompleteResult`。如果客户端列表为空，也发送一次 complete；异常时发送 `createErrorResult` 后完成 emitter，并将异常继续交给现有调度错误处理。

- [ ] **Step 4: 运行后端定向测试和编译**

Run: `mvn -pl ai-agent-station-study-ann-app -am -DskipTests=false -DfailIfNoTests=false -Dtest=AutoAgentExecuteResultEntityTest,FixedAgentExecuteStrategyTest test`

Expected: 两个测试类 PASS，所有模块编译成功。

- [ ] **Step 5: 提交固定 Agent 流式实现**

```bash
git add ai-agent-station-study-ann-domain/src/main/java/cn/ann/ai/domain/agent/service/execute/fixed/FixedAgentExecuteStrategy.java ai-agent-station-study-ann-app/src/test/java/cn/ann/ai/domain/agent/service/execute/fixed/FixedAgentExecuteStrategyTest.java
git commit -m "fix(agent): stream final client content"
```

### Task 4: 暗色主题和文字对比度

**Files:**
- Create: `frontend/src/styles/theme-contract.test.ts`
- Modify: `frontend/src/styles/index.css`
- Modify: `frontend/src/pages/agent-config.tsx`
- Modify: `frontend/src/components/base-node/styles.tsx`
- Modify: `frontend/src/components/tools/styles.tsx`
- Modify: `frontend/src/hooks/use-editor-props.tsx`

- [ ] **Step 1: 写失败的主题契约测试**

```ts
import { readFileSync } from 'node:fs';
import { describe, expect, it } from 'vitest';

const css = readFileSync(new URL('./index.css', import.meta.url), 'utf8');

describe('dark theme contract', () => {
  it('covers fixed table cells and pagination', () => {
    expect(css).toContain('.semi-table-row-cell-fixed-right');
    expect(css).toContain('.semi-page-item');
    expect(css).toContain('.semi-page-item-active');
  });

  it('keeps text and workflow surfaces readable', () => {
    expect(css).toContain('.semi-input::placeholder');
    expect(css).toContain('.ann-workflow-theme');
    expect(css).toContain('--ann-text-primary: #f7f7ff');
  });
});
```

- [ ] **Step 2: 运行测试并确认关键选择器缺失**

Run: `npx vitest run src/styles/theme-contract.test.ts`

Expected: FAIL，缺少固定列、分页或 `ann-workflow-theme` 断言内容。

- [ ] **Step 3: 实现统一暗色覆盖**

在 `index.css` 定义主题变量，并为 Semi UI 固定列和分页增加明确背景：

```css
:root {
  --ann-bg-canvas: #070910;
  --ann-bg-surface: #0c0f19;
  --ann-bg-elevated: #121624;
  --ann-text-primary: #f7f7ff;
  --ann-text-secondary: #a7aec4;
  --ann-text-placeholder: #7f879f;
}

.semi-table-row-cell-fixed-left,
.semi-table-row-cell-fixed-right,
.semi-table-thead > .semi-table-row > .semi-table-row-head.semi-table-row-cell-fixed-left,
.semi-table-thead > .semi-table-row > .semi-table-row-head.semi-table-row-cell-fixed-right {
  background: var(--ann-bg-surface) !important;
}

.semi-page-item,
.semi-page-prev,
.semi-page-next {
  color: var(--ann-text-secondary) !important;
  background: var(--ann-bg-elevated) !important;
  border-color: rgba(151, 160, 198, .18) !important;
}

.semi-page-item-active {
  color: #fff !important;
  background: #7557e8 !important;
}
```

补充输入内容、placeholder、页面 `h1/h2`、Select 文本和固定列 hover 状态。为 AgentConfig 编辑器容器添加 `className="ann-workflow-theme"`，覆盖画布、点阵、节点输入、工具栏和小地图；同时把基础节点和工具栏 styled-components 的白色常量替换为 `#121624/#0c0f19`。

- [ ] **Step 4: 运行主题测试、类型检查和页面测试**

Run: `npx vitest run src/styles/theme-contract.test.ts src/pages/agent-config.test.tsx src/pages/agent-list.test.tsx src/pages/management-pages.test.tsx`

Expected: PASS。

Run: `npx tsc --noEmit`

Expected: exit 0。

- [ ] **Step 5: 提交暗色主题修复**

```bash
git add frontend/src/styles/theme-contract.test.ts frontend/src/styles/index.css frontend/src/pages/agent-config.tsx frontend/src/components/base-node/styles.tsx frontend/src/components/tools/styles.tsx frontend/src/hooks/use-editor-props.tsx
git commit -m "fix(frontend): complete dark theme coverage"
```

### Task 5: 全量验证和浏览器验收

**Files:**
- Modify only if verification exposes an in-scope defect.

- [ ] **Step 1: 运行前端全量质量检查**

Run: `npm test -- --run`

Expected: 所有 Vitest 测试通过。

Run: `npx eslint ./src --no-cache --quiet`

Expected: exit 0。

Run: `npx tsc --noEmit`

Expected: exit 0。

Run: `npm run build`

Expected: Rsbuild 生产构建成功。

- [ ] **Step 2: 运行后端定向测试和编译**

Run: `mvn -pl ai-agent-station-study-ann-app -am -DskipTests=false -DfailIfNoTests=false -Dtest=AutoAgentExecuteResultEntityTest,FixedAgentExecuteStrategyTest test`

Expected: 测试通过且 Reactor/Spring AI 类型编译成功。

- [ ] **Step 3: 使用浏览器检查视觉状态**

在用户现有登录会话中检查：

- Agent 列表固定操作列 computed background 不为白色。
- 管理页分页按钮、当前页和跳页输入为暗色。
- 模型 API 管理标题和搜索框 placeholder 清晰可读。
- Agent 编排画布、节点、输入框、工具栏和小地图为暗色主题。
- 1280、1440、1920 宽度无新增横向主题断层。
- 控制台没有新增 error。

- [ ] **Step 4: 使用真实后端检查流式对话**

启动修改后的后端与前端，选择已装配固定 Agent，发送一条短消息；观察 assistant 气泡在请求结束前至少更新两次，最终文本中不含 `{\"completed\"`、`sessionId` 或 `type` 协议字段。

- [ ] **Step 5: 核对最终变更边界**

Run: `git status --short`

Expected: 只保留本计划文件、范围内源码/测试以及用户原有的日志、IDE、`.claude/.superpowers` 和 `PrintABC.java` 变更；不得暂存或提交这些无关文件。
