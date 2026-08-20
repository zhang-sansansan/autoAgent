# ANN Agent Studio 暗色主题与流式对话修复设计

## 背景

当前前端已经完成主要页面重建，但仍存在四类可复现问题：

1. Semi UI 表格固定操作列和分页仍使用默认浅色背景，与暗色主题冲突。
2. Flowgram 流程画布、节点和节点表单仍主要使用浅色样式。
3. 管理页标题、搜索框内容及 placeholder 对比度不足。
4. 对话端把 SSE 事件 JSON 原样拼接到气泡；固定 Agent 后端使用阻塞式 `.call().content()`，并不是真正的内容流。

后端流式修改前的完整仓库基线为提交 `a6e4cdc`。

## 目标

- 表格操作列、分页、流程画布、节点表单与现有 Obsidian 暗色主题一致。
- 页面标题、正文、辅助文字和 placeholder 具有清晰的视觉层级。
- 固定 Agent 最终客户端的 LLM 内容以增量 SSE 事件发送。
- 前端只展示事件的 `content`，不展示 JSON 协议字段。
- 保持多客户端固定 Agent 的串行上下文传递语义。
- 不新增后端未提供的业务能力，不改变现有请求地址和请求 DTO。

## 非目标

- 不重构 Flowgram 编辑器架构。
- 不更换 Semi UI 或 styled-components。
- 不把固定 Agent 的中间客户端结果展示给用户。
- 不新增会话历史持久化。
- 不修改 Auto Agent 和 Flow Agent 的业务执行步骤。

## 方案

### 1. 全局暗色主题覆盖

以 `frontend/src/styles/index.css` 为主题覆盖入口，统一 Semi UI 的以下状态：

- 表格表头、普通单元格、固定列单元格、固定列遮罩和 hover 状态。
- 分页容器、页码按钮、选中页、禁用页以及跳页输入框。
- Input、Textarea、Select 的输入文字、placeholder、图标和禁用状态。
- 页面级标题使用 `#f7f7ff`，正文使用 `#dfe2f2`，辅助文字使用不低于 `#8f96ad` 的颜色。

固定操作列使用与表格主体一致的深色背景，不采用纯黑或透明叠加导致的色差。分页按钮使用三级背景，当前页使用紫色强调色。

### 2. 流程编辑器暗色作用域

在 Agent 编排页面外层增加稳定的主题作用域类名，暗色覆盖限制在该作用域内，避免影响其他 Semi UI 页面。

- 画布：`#0a0d15`，保留低对比点阵辅助定位。
- 节点卡片：`#121624`，选中时使用紫色描边与轻微辉光。
- 节点标题与标签：主文字色；说明与 placeholder：次级文字色。
- 节点 Input、Textarea、Select：`#0c0f19`，边框使用半透明灰紫色。
- 工具栏和小地图：与卡片背景一致，去除默认白底。
- 连线、端口和主要操作继续使用现有蓝紫强调色。

### 3. SSE 协议解析

前端 `ChatService` 按 SSE 空行事件边界处理响应，而不是把每一行当显示文本：

1. `TextDecoder` 以流模式解码网络分片。
2. 缓冲区可以容纳被拆开的 UTF-8 字符、JSON 和 SSE 事件。
3. 一个读取分片包含多个事件时逐个解析。
4. 去掉 `data:` 前缀后解析为 `AutoAgentStreamEvent`。
5. `type=complete` 只结束状态，不传递给消息气泡。
6. 其他事件仅把非空 `content` 传给 UI。
7. `type=error` 转换为可展示的请求错误。
8. 非法 JSON 不作为聊天内容展示，而是明确报出协议解析错误。

页面仍采用单个 assistant 气泡，通过每个 `content` 增量更新 React 状态，因此用户可以看到内容逐步增长。

### 4. 固定 Agent 真流式输出

`FixedAgentExecuteStrategy` 保持当前客户端串行执行：

- 非最终客户端继续使用 `.call().content()`，结果作为下一个客户端的上下文。
- 最终客户端改用 `.stream().content()`。
- 每收到一个非空内容增量，立即创建 `content` 类型、`completed=false` 的结果实体并发送 SSE。
- 同时累积完整内容，供日志与现有执行语义使用。
- 流完成后仅发送一次 `complete` 事件，不再发送重复的完整 summary。
- 发送失败或模型流异常时发送 `error` 事件并结束 emitter。

在 `AutoAgentExecuteResultEntity` 中新增专用的增量内容工厂方法，使事件含义明确，避免把每个 token 错标为已完成的 summary。

## 数据流

```text
用户输入
  -> POST /api/v1/agent/auto_agent
  -> 固定 Agent 中间客户端串行 call
  -> 最终客户端 stream().content()
  -> SSE: { type: "content", content: "增量", completed: false }
  -> 前端 SSE 解析器
  -> onChunk(event.content)
  -> assistant 气泡增量更新
  -> SSE: { type: "complete", completed: true }
  -> 结束生成状态
```

## 错误处理

- HTTP 非 2xx：沿用请求失败处理。
- 响应体为空：提示浏览器不支持或服务未返回流。
- SSE JSON 非法：显示“响应协议解析失败”，不泄露原始协议文本到气泡。
- 后端模型流异常：发送 `error` 事件，记录服务端日志并完成 emitter。
- 用户停止：AbortController 中止 fetch，保留已经显示的内容。

## 测试与验收

### 自动化测试

- 前端 SSE 解析测试：
  - 单事件被拆为多个网络分片。
  - 单分片包含多个事件。
  - 中文 UTF-8 字符跨分片。
  - 只输出 `content`，忽略 `complete`。
  - 错误事件和非法 JSON。
- 对话页面测试：多个内容增量最终合并为同一 assistant 气泡。
- 主题契约测试：关键的固定列、分页、编辑器作用域和高对比文字选择器存在。
- 后端测试：增量事件实体字段正确；固定策略先发送若干 content 事件，最后发送一次 complete。

### 浏览器验收

- Agent 列表和所有管理表格的操作列无白底。
- 数据超过一页时，分页区域和页码按钮保持暗色主题。
- Agent 编排画布、节点、节点字段、工具栏和小地图均为暗色。
- 管理页标题、搜索输入内容与 placeholder 清晰可读。
- 对话气泡逐段增长，只显示自然语言 content。
- 浏览器控制台无新增 error，页面无横向主题断层。

## 修改边界

预计修改：

- `frontend/src/styles/index.css`
- `frontend/src/pages/agent-config.tsx` 或编辑器外层主题容器
- Flowgram 节点基础样式和必要的工具栏样式
- `frontend/src/services/chat-service.ts`
- 对应前端测试
- `AutoAgentExecuteResultEntity.java`
- `FixedAgentExecuteStrategy.java`
- 对应后端测试

不纳入提交：运行日志、IDE 配置、现有无关测试文件及其他用户改动。
