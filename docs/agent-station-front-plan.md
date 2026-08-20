# 实施计划：ANN Agent Studio 前端全量重构

对应设计文档：agent-station-front-design.md  
版本：v2.1  
日期：2026-08-19  
状态：待执行（B 方案：重建应用层、保留能力层）

> 执行要求：使用 executing-plans 按任务顺序实施；每个行为变化先建立失败测试，再完成最小实现并验证通过。

**目标：** 在保留现有后端契约与 Flowgram 能力的前提下，重建 ANN Agent Studio 的前端应用层。

**架构：** 页面、布局、业务表单和视觉系统整文件重建；服务 DTO、Flowgram 引擎、插件、快捷键、画布工具和通用工具保留并定点修正。新页面统一依赖应用框架、导航配置、异步状态和管理页骨架。

**技术栈：** React 18、TypeScript、Rsbuild、Semi UI、styled-components、Flowgram、Vitest 与 Testing Library。

## 1. 实施原则

- 不删除整个 ai-agent-station-study-ann/frontend；重建应用层文件并保留能力层文件，不再次整体拷贝示例前端。
- 保留 React 18、TypeScript、Rsbuild、Semi UI、styled-components 与 Flowgram 现有技术体系。
- 所有业务数据继续来自 8091 后端现有接口，不增加后端、数据库、健康探测、API 装配或模拟统计能力。
- 保留 13 个现有页面路由、已有流程数据含义及 Flowgram 画布交互。
- 按“请求与状态基线 → 设计系统 → 全局框架 → 核心页面 → 编辑器 → 管理页面 → 验证”的依赖顺序实施。
- 每完成一个阶段先执行对应静态检查和页面回归，再进入下一阶段。

### 1.1 文件处理边界

- 整文件重建：src/pages、src/components/layout、业务表单组件、src/styles、src/app.tsx。
- 新增共享应用层：导航配置、应用框架、命令中心、异步状态、管理页骨架、登录存储与未保存保护。
- 保留并定点修正：src/services、src/editor.tsx、src/hooks/use-editor-props.tsx、src/nodes 现有节点、src/plugins、src/shortcuts、src/utils、src/typings。
- 从参考项目恢复：LLM、任务、条件和循环节点实现；恢复时保持目标项目现有类型与流程数据兼容。
- 延迟删除：旧页面或 UI 组件必须在替代实现通过自动化测试、构建和引用检查后才允许删除。
- 不处理：后端工程、数据库、日志、IDE 配置及其他与本次前端无关的工作区改动。

## 2. 阶段一：构建与数据访问基线

1. frontend/package.json：加入 Vitest、Testing Library 和 jsdom 测试入口，将空操作构建脚本恢复为真实 Rsbuild 构建校验，并保留开发和 lint 入口。
1.1 frontend/vitest.config.ts：新增 React 与 jsdom 测试配置，限定测试文件范围并启用统一测试初始化。
1.2 frontend/src/test/setup.ts：新增 DOM 断言、浏览器 API 清理和每次用例后的状态复位。
1.3 frontend/src/services/request-service.test.ts：先覆盖统一响应成功、业务失败、网络失败和空数据边界，并确认测试在实现前失败。
2. frontend/src/config/api.ts：保留 8091 服务配置并移除后端不存在的 API 装配端点定义。
3. frontend/src/services/request-service.ts：实现测试要求的统一响应解析、网络异常和业务异常表达，避免各页面把失败误判为空数据。
4. frontend/src/services/admin-user-service.ts：接入统一请求结果，并区分登录凭据错误与网络失败。
5. frontend/src/services/data-statistics-service.ts：移除失败时返回全零统计的降级逻辑，让仪表盘能够显示真实错误和重试状态。
6. frontend/src/services/ai-agent-service.ts：删除 API 装配相关请求，只保留现有 Agent 装配能力。
7. frontend/src/services/ai-agent-draw-service.ts：让配置列表、加载、保存和删除失败向页面透传，同时保留当前画布重试条件。
8. frontend/src/services/chat-service.ts：统一可用 Agent 查询异常，并稳固流式内容、停止生成和中断后的状态边界。
9. frontend/src/services/ai-client-service.ts：让节点客户端资源查询区分空数据与请求失败。
10. frontend/src/services/ai-client-model-service.ts：让节点模型资源查询区分空数据与请求失败，并只返回当前可用模型。
11. frontend/src/services/ai-client-advisor-service.ts：让节点 Advisor 资源查询区分空数据与请求失败，并只返回当前可用数据。
12. frontend/src/services/ai-client-system-prompt-service.ts：让节点 Prompt 资源查询区分空数据与请求失败，并只返回当前可用数据。
13. frontend/src/services/ai-client-tool-mcp-service.ts：让节点 MCP 资源查询区分空数据与请求失败，并只返回当前可用数据。
14. frontend/src/services/ai-client-admin-service.ts：统一客户端管理的查询、新增、编辑和删除结果表达。
15. frontend/src/services/ai-client-api-admin-service.ts：统一模型 API 管理的查询、新增、编辑和删除结果表达。
16. frontend/src/services/ai-client-model-admin-service.ts：统一模型管理的查询、新增、编辑、删除和关联 API 查询结果表达。
17. frontend/src/services/ai-client-advisor-admin-service.ts：统一 Advisor 管理的查询、新增、编辑和删除结果表达。
18. frontend/src/services/ai-client-system-prompt-admin-service.ts：统一系统 Prompt 管理的查询、新增、编辑和删除结果表达。
19. frontend/src/services/ai-client-tool-mcp-admin-service.ts：统一 MCP 管理的查询、新增、编辑和删除结果表达。
20. frontend/src/services/ai-client-rag-order-admin-service.ts：统一 RAG 查询、增删改和文件上传结果表达。
21. frontend/src/services/index.ts：集中导出调整后的业务服务与统一请求能力。

## 3. 阶段二：Obsidian Signal 设计系统

22. frontend/src/styles/theme.ts：重建 Obsidian Signal 色彩、文字、间距、圆角、阴影、动效、断点和层级令牌。
23. frontend/src/styles/index.css：设置深色基础背景、字体、滚动条、焦点、选区、页面过渡及 Semi UI 全局深色适配。
24. frontend/src/components/common/Card.tsx：将通用卡片改造成细描边、克制阴影和轻透明层次的深色容器。
25. frontend/src/components/common/AsyncState.tsx：新增统一骨架、空数据、错误说明和重试状态容器。
26. frontend/src/components/common/StatusTag.tsx：新增同时使用文字、图标和颜色表达启用或禁用的状态组件。
27. frontend/src/components/common/ConfirmAction.tsx：新增包含目标对象和不可恢复说明的统一高风险确认组件。
28. frontend/src/components/common/ManagementScaffold.tsx：新增统一管理页页头、仅基于现有能力的筛选区、含标识/名称/状态/创建与更新时间/操作列的表格容器和横向滚动骨架。
29. frontend/src/components/common/UnsavedConfirm.tsx：新增弹窗或抽屉关闭以及离开当前页面时的未保存内容确认能力。
30. frontend/src/components/common/NarrowViewportNotice.tsx：新增低于 1280px 时可关闭、非阻断的窗口宽度提示。
31. frontend/src/components/common/index.ts：集中导出新增的通用视觉和状态组件。

## 4. 阶段三：全局框架、导航与登录态

31.1 frontend/src/config/navigation.test.ts：先覆盖 13 个唯一路由、四个导航分组、关键词匹配和无直接业务动作，并确认测试在实现前失败。
31.2 frontend/src/utils/auth-storage.test.ts：先覆盖本地登录标记、用户信息、退出清理和不依赖模拟 Token，并确认测试在实现前失败。
31.3 frontend/src/components/layout/CommandCenter.test.tsx：先覆盖点击打开、页面关键词过滤、分组展示、空态和选择后导航，并确认测试在实现前失败。
32. frontend/src/config/navigation.ts：新增 13 个页面的名称、路由、导航分组和功能搜索关键词配置。
33. frontend/src/utils/auth-storage.ts：集中管理现有本地登录标记、用户信息读取和退出清理，不再模拟服务端 Token。
34. frontend/src/context/layout-context.tsx：新增侧栏展开偏好、专注模式前后布局状态和全局框架状态。
35. frontend/src/components/layout/AppShell.tsx：整文件建立统一侧栏、顶部栏、主内容区、窄屏提示和页面过渡框架，并在专注模式中只隐藏非必要框架内容。
36. frontend/src/components/layout/Sidebar.tsx：整文件重建为默认展开、可记忆偏好、四组单层导航和清晰选中状态的 Focused Sidebar，折叠时通过悬浮提示说明页面名称。
37. frontend/src/components/layout/CommandCenter.tsx：新增点击打开、仅搜索页面及功能关键词、只执行页面跳转的命令中心，且不注册键盘快捷键。
38. frontend/src/components/layout/Header.tsx：整文件重建顶部栏，展示当前页面标题、命令中心入口、用户菜单和退出登录，不展示健康状态。
39. frontend/src/components/layout/index.ts：统一导出新的应用框架、侧栏、顶部栏和命令中心。
40. frontend/src/app.tsx：整文件重建路由入口，使用统一登录守卫和应用框架组织全部 13 个路由，并处理登录页重定向与未知路由。

## 5. 阶段四：登录、仪表盘与 Agent 列表

40.1 frontend/src/pages/login.test.tsx：先覆盖普通登录、密码显隐、管理员快捷直接登录、凭据错误和网络错误，并确认测试在页面重建前失败。
40.2 frontend/src/pages/dashboard.test.tsx：先覆盖只渲染 7 项真实统计、真实零值和请求失败重试，并确认测试在页面重建前失败。
40.3 frontend/src/pages/agent-list.test.tsx：先覆盖筛选、刷新、复制、单项装配状态和空态，并确认测试在页面重建前失败。
41. frontend/src/pages/login.tsx：整文件重建全屏深色登录页，完成密码显隐、普通登录、管理员快捷直接登录及可区分的失败反馈。
42. frontend/src/pages/login.css：补齐登录页 Obsidian Signal 背景、焦点、卡片层级和推荐宽度适配。
43. frontend/src/pages/dashboard.tsx：整文件重建仪表盘，只展示 7 项真实资源数量、零值说明、骨架与错误重试，并保留三个 Agent 快捷入口。
44. frontend/src/pages/agent-list.tsx：整文件重建 Agent 列表，完成本地关键词筛选、刷新、标识复制、单行装配状态、空态和编排/对话入口。

## 6. 阶段五：恢复 12 类节点并完成编辑器深色适配

44.1 frontend/src/nodes/node-registries.test.ts：先覆盖 12 类可见节点唯一注册和批注节点保留，并确认测试在四类节点恢复前失败。
45. frontend/src/nodes/task/index.ts：从参考前端恢复任务节点注册并适配目标项目类型与深色节点表现。
46. frontend/src/nodes/llm/index.ts：从参考前端恢复 LLM 节点注册并适配目标项目类型与深色节点表现。
47. frontend/src/nodes/condition/index.ts：从参考前端恢复条件节点注册并接入条件属性表单。
48. frontend/src/nodes/condition/form-meta.tsx：从参考前端恢复条件节点的分组属性定义。
49. frontend/src/nodes/condition/condition-inputs/index.tsx：从参考前端恢复条件输入编辑能力并适配深色交互状态。
50. frontend/src/nodes/condition/condition-inputs/styles.tsx：将条件输入的边框、背景、焦点和错误状态适配 Obsidian Signal。
51. frontend/src/nodes/loop/index.ts：从参考前端恢复循环节点注册及子画布能力。
52. frontend/src/nodes/loop/loop-form-render.tsx：从参考前端恢复循环节点属性编辑并适配深色表单。
53. frontend/src/nodes/index.ts：重新注册开始、Agent、客户端、Advisor、模型、Prompt、MCP、LLM、任务、条件、循环和结束 12 类可见节点，并保留批注节点。
54. frontend/src/hooks/use-editor-props.tsx：统一画布、连线、吸附、小地图、选中和错误状态的深色配置，同时保留现有快捷键与插件。
55. frontend/src/components/base-node/styles.tsx：重构节点卡片、端口、选中、异常和可连接状态的深色视觉。
56. frontend/src/components/node-panel/index.less：重构节点面板的分类、悬浮、拖拽和滚动区域视觉。
57. frontend/src/components/tools/styles.tsx：重构缩放、小地图、自动布局、批注和交互模式工具栏视觉。
58. frontend/src/form-components/form-content/styles.tsx：重构节点属性区的深色分组容器和内容层级。
59. frontend/src/form-components/form-inputs/styles.tsx：重构节点输入控件的深色正常、焦点、禁用和错误状态。
60. frontend/src/editor.tsx：整合恢复后的节点、深色画布工具和外部流程数据变化反馈。

## 7. 阶段六：流程资源选择、专注模式与未保存保护

60.1 frontend/src/pages/agent-config.test.tsx：先覆盖新建、加载、命名保存、删除、专注模式状态保持和未保存保护，并确认测试在页面重建前失败。
61. frontend/src/hooks/use-unsaved-changes-guard.ts：新增页面跳转、新建、加载其他流程及浏览器刷新关闭的未保存修改保护。
62. frontend/src/nodes/client/client-select/index.tsx：补齐客户端资源的加载、空、失败、缺失或禁用关联和前往管理页指引。
63. frontend/src/nodes/model/model-select/index.tsx：补齐模型资源的加载、空、失败、缺失或禁用关联和前往管理页指引。
64. frontend/src/nodes/advisor/advisor-select/index.tsx：补齐 Advisor 资源的加载、空、失败、缺失或禁用关联和前往管理页指引。
65. frontend/src/nodes/prompt/prompt-select/index.tsx：补齐 Prompt 资源的加载、空、失败、缺失或禁用关联和前往管理页指引。
66. frontend/src/nodes/tool_mcp/tool-mcp-select/index.tsx：补齐 MCP 资源的加载、空、失败、缺失或禁用关联和前往管理页指引。
67. frontend/src/pages/agent-config.tsx：整文件重建流程页面，完成新建、选择、加载、命名保存、删除、失败保留和未保存确认闭环；专注模式开启与退出不得改变流程内容、缩放状态或选中节点。

## 8. 阶段七：Agent 流式对话

67.1 frontend/src/pages/chat.test.tsx：先覆盖 Agent 选择、流式追加、停止、新会话、消息级失败和重试，并确认测试在页面重建前失败。
68. frontend/src/pages/chat.tsx：整文件重建 Agent 对话页，完成选择、左右消息流、输入区、新会话、流式生成、停止、超时、消息级失败与重试体验，不读取或伪造服务端会话历史。

## 9. 阶段八：统一资源管理页面

68.1 frontend/src/pages/management-pages.test.tsx：先覆盖八个模块的容器类型、字段、状态、删除确认、API Key、RAG 上传和当前账号保护，并确认测试在页面重建前失败。
69. frontend/src/components/client-create-modal.tsx：将客户端新增表单适配深色居中弹窗、字段校验、重复提交保护和未保存确认。
70. frontend/src/components/client-edit-modal.tsx：将客户端编辑表单适配深色居中弹窗、标识保护、失败保留和未保存确认。
71. frontend/src/pages/client-management.tsx：整文件重建客户端管理页，按客户端标识、名称、描述和状态完成查询、筛选、重置、增删改和完整列表状态。
72. frontend/src/components/ai-client-api-form-modal.tsx：改造为覆盖 API 标识、基础地址、Key、对话补全路径、向量嵌入路径和状态的统一右侧抽屉，完成 Key 默认隐藏、显隐切换、校验和未保存确认。
73. frontend/src/pages/ai-client-api-management.tsx：整文件重建模型 API 管理页，接入右侧抽屉、列表永久脱敏与主动复制，并彻底移除 API 加载或装配操作。
74. frontend/src/pages/client-model-management.tsx：整文件重建模型管理页，按模型标识、名称、所属 API、类型、用途和状态提供列表与居中弹窗，完成关联缺失阻止保存及完整反馈。
75. frontend/src/pages/advisor-management.tsx：整文件重建 Advisor 管理页，按标识、名称、类型、顺序、扩展参数和状态提供列表与右侧抽屉，完成格式校验及失败内容保留。
76. frontend/src/pages/client-system-prompt-management.tsx：整文件重建系统 Prompt 管理页，按标识、名称、内容、描述和状态提供列表与右侧抽屉，完成长内容独立查看、编辑及完整反馈。
77. frontend/src/pages/client-tool-mcp-management.tsx：整文件重建 MCP 管理页，按标识、名称、传输类型、传输配置、请求超时和状态提供列表与右侧抽屉，完成 JSON 校验及失败内容保留。
78. frontend/src/pages/rag-order-management.tsx：整文件重建 RAG 管理页，按标识、名称、知识标签和状态提供 CRUD 弹窗，并完善 TXT/PDF/DOC/DOCX/Markdown、10MB、上传中、失败保留、重试和成功刷新。
79. frontend/src/pages/user-management.tsx：整文件重建用户管理页，按用户标识、用户名和状态提供列表与居中弹窗，列表不显示密码，完成新增密码、编辑可选改密且不回显原密码、启禁用及当前账号保护。

## 10. 阶段九：清理、构建与验收

80. frontend/src/components/ai-client-api-create-modal.tsx：确认新抽屉测试、构建和引用检查通过后，删除已被替代的旧新增弹窗实现。
81. frontend/src/components/ai-client-api-edit-modal.tsx：确认新抽屉测试、构建和引用检查通过后，删除已被替代的旧编辑弹窗实现。
82. frontend/src/pages/index.ts：核对并统一导出 13 个最终页面组件。
83. frontend/docs/ui-qa-checklist.md：建立登录、13 个路由、核心闭环、8 个管理模块、1280/1440/1920px、键盘焦点和控制台检查清单。
84. frontend/package.json：执行 lint 与真实构建验收，并修复所有阻断性交付的问题。
85. frontend/docs/ui-qa-checklist.md：使用 8091 后端逐项完成浏览器联调并记录通过结果，不以 Mock 数据替代失败接口。

## 11. 覆盖关系

| 设计章节 | 对应计划步骤 |
|---|---|
| 2.1 品牌与视觉语言 | 22–31、41–44、55–59 |
| 2.2 全局页面框架 | 32–40 |
| 2.3 页面导航搜索 | 32、37、38 |
| 2.4 登录态与路由访问 | 33、40、41 |
| 2.5 全局加载、反馈与异常状态 | 3–21、25–31、69–79 |
| 2.6 登录页 | 4、33、41、42 |
| 2.7 仪表盘 | 5、43 |
| 2.8 Agent 列表 | 6、8、44 |
| 2.9 Agent 编排 | 7、34、45–67 |
| 2.10 Agent 对话 | 8、68 |
| 2.11 资源管理通用体验 | 14–21、25–31、69–79 |
| 2.12 客户端管理 | 14、69–71 |
| 2.13 模型 API 管理 | 2、6、15、72、73、80、81 |
| 2.14 模型管理 | 10、16、63、74 |
| 2.15 Advisor 管理 | 11、17、64、75 |
| 2.16 系统 Prompt 管理 | 12、18、65、76 |
| 2.17 MCP 工具管理 | 13、19、66、77 |
| 2.18 RAG 订单管理 | 20、78 |
| 2.19 用户管理 | 4、79 |
| 2.20 桌面适配与可访问性 | 22–31、35–38、83–85 |
| 3.1–3.8 数据流变化 | 3–21、33、37、41、43–44、61–79 |
| 4 兼容性说明 | 1–2、40、45–60、82–85 |
| 5 不在本次范围 | 2、5–6、33、37、43、68、73、83–85 |

## 12. 阶段验收条件

- 阶段一完成后，所有真实请求能区分成功、空数据、业务失败和网络失败。
- 阶段三完成后，13 个路由共享统一框架，侧栏偏好、命令中心和本地登录边界可用。
- 阶段六完成后，12 类节点、画布能力、资源异常、专注模式和未保存保护全部可用。
- 阶段八完成后，8 个管理模块均符合指定弹窗或抽屉形态，CRUD 与特有能力保持可用。
- 最终交付前，真实构建通过，1280、1440、1920px 浏览器检查通过，主要页面无阻断性控制台错误。
