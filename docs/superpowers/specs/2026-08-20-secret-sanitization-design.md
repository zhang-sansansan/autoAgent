# GitHub 推送密钥脱敏设计

## 背景

GitHub Push Protection 在待推送提交中识别到 Baidu AI API Key 与 Grafana Service Account Token。命中位置分布在 Java 测试、SQL 示例和前端文档配置中。任何真实凭据都不得进入远端提交历史。

## 目标

- 保留测试与示例的原有结构和说明价值。
- Java 测试通过环境变量获取运行时凭据，不再硬编码密钥。
- SQL 与 JSON 文档使用明确、无效且可替换的占位符。
- 最终功能分支只包含一个基于 `github/main` 的脱敏提交，提交信息保持为“装配了前端ui页面的可运行版本”。
- 推送前通过自动扫描、前端测试与构建、后端目标测试验证提交内容。

## 方案

### Java 测试

涉及 Baidu 凭据的测试读取 `BAIDU_AI_API_KEY` 环境变量。缺少变量时使用清晰错误提示终止对应手工集成测试，避免静默使用无效值或回退到硬编码凭据。

### SQL 与 JSON 示例

- Baidu 凭据替换为 `YOUR_BAIDU_AI_API_KEY`。
- Grafana 凭据替换为 `YOUR_GRAFANA_SERVICE_ACCOUNT_TOKEN`。
- 保留原字段、JSON 结构和 SQL 可读性，使使用者能明确知道需要注入什么配置。

### 提交历史

脱敏完成后，以 `github/main` 为父提交重新生成单一提交。原先含凭据的本地提交不作为新提交的祖先，避免密钥仍存在于待推送历史中。随后让本地 `main` 与 `feat/runnable-frontend-ui` 同时指向新提交。

## 验证

1. 用只输出文件路径和命中数量的扫描命令检查两类原始凭据特征，禁止输出凭据内容。
2. 运行前端完整测试、TypeScript 类型检查和生产构建。
3. 运行后端 `AutoAgentExecuteResultEntityTest` 与 `FixedAgentExecuteStrategyTest`。
4. 确认最终分支相对 `github/main` 只有一个提交，提交信息正确，并且工作区干净。
5. 通过 GitHub SSH 推送；Push Protection 必须不再报告密钥命中。

## 非目标

- 不更改业务接口、页面交互或流式响应协议。
- 不删除测试、SQL 示例或 MCP 示例配置。
- 不在日志、终端输出或 PR 描述中展示任何凭据值。
