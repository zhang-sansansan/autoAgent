# GitHub Push Protection 密钥脱敏实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 清除待推送提交中的 Baidu 与 Grafana 凭据，同时保留测试和示例结构，并以单一安全提交完成推送与 PR。

**Architecture:** 在前端测试集中加入仓库级凭据卫生测试，只报告文件路径而不回显密钥。Java 手工集成测试从环境变量读取完整 Baidu 凭据，SQL 与 JSON 示例使用明确的无效占位符；最后基于 `github/main` 重建单一提交，确保旧的含密钥提交不在可达历史中。

**Tech Stack:** Vitest、TypeScript、Java、Maven、Git、GitHub SSH

---

### Task 1: 建立凭据扫描回归测试

**Files:**
- Create: `frontend/src/security/credential-hygiene.test.ts`
- Test: `frontend/src/security/credential-hygiene.test.ts`

- [ ] **Step 1: 写入失败测试**

创建测试，读取 GitHub 报告的五个仓库文件，扫描完整 Baidu 凭据格式与 Grafana Service Account Token 格式。发现命中时只抛出文件路径：

```ts
import { readFileSync } from 'node:fs';
import path from 'node:path';
import { describe, expect, it } from 'vitest';

const repositoryRoot = path.resolve(process.cwd(), '..');
const credentialFiles = [
  'ai-agent-station-study-ann-app/src/test/java/cn/ann/ai/test/spring/ai/AiSearchMCPTest.java',
  'ai-agent-station-study-ann-app/src/test/java/cn/ann/ai/test/spring/ai/AutoAgentTest.java',
  'docs/dev-ops/mysql/sql/ai-agent-station-study.sql',
  'frontend/docs/ai-agent-station-study.sql',
  'frontend/docs/mcp.json',
];
const forbiddenCredentialPatterns = [
  /bce-v3\/ALTAK-[A-Za-z0-9_-]+\/[A-Fa-f0-9]+/,
  /glsa_[A-Za-z0-9_-]+/,
];

describe('repository credential hygiene', () => {
  it.each(credentialFiles)('contains no hard-coded credential in %s', (relativePath) => {
    const content = readFileSync(path.join(repositoryRoot, relativePath), 'utf8');
    const hasCredential = forbiddenCredentialPatterns.some((pattern) => pattern.test(content));

    expect(hasCredential, `hard-coded credential detected in ${relativePath}`).toBe(false);
  });
});
```

- [ ] **Step 2: 运行测试并确认 RED**

Run: `npm test -- --run src/security/credential-hygiene.test.ts`

Expected: FAIL；失败消息仅列出含凭据的文件路径，不显示凭据内容。

### Task 2: 运行时凭据改为环境变量

**Files:**
- Modify: `ai-agent-station-study-ann-app/src/test/java/cn/ann/ai/test/spring/ai/AiSearchMCPTest.java`
- Modify: `ai-agent-station-study-ann-app/src/test/java/cn/ann/ai/test/spring/ai/AutoAgentTest.java`
- Test: `frontend/src/security/credential-hygiene.test.ts`

- [ ] **Step 1: 为每个测试类增加环境变量校验方法**

在类内加入同名私有方法，缺少配置时提供明确错误：

```java
private static String requireEnvironment(String name) {
    String value = System.getenv(name);
    if (value == null || value.isBlank()) {
        throw new IllegalStateException("Missing required environment variable: " + name);
    }
    return value;
}
```

- [ ] **Step 2: 用环境变量拼接 SSE endpoint**

两个测试的客户端配置统一改为：

```java
.sseEndpoint("sse?api_key=" + requireEnvironment("BAIDU_AI_API_KEY"))
```

`BAIDU_AI_API_KEY` 保存原先 endpoint 中 `api_key=` 后的完整值，包括资源标识。

- [ ] **Step 3: 编译目标测试类**

Run: `mvn -pl ai-agent-station-study-ann-app -am -DskipTests test-compile`

Expected: `BUILD SUCCESS`。

### Task 3: 脱敏 SQL 与 JSON 示例

**Files:**
- Modify: `docs/dev-ops/mysql/sql/ai-agent-station-study.sql`
- Modify: `frontend/docs/ai-agent-station-study.sql`
- Modify: `frontend/docs/mcp.json`
- Test: `frontend/src/security/credential-hygiene.test.ts`

- [ ] **Step 1: 替换 Baidu 凭据**

两个 SQL 文件与 `frontend/docs/mcp.json` 中的 Baidu endpoint 保留前缀，并将完整凭据替换为：

```text
sse?api_key=Bearer+YOUR_BAIDU_AI_API_KEY
```

- [ ] **Step 2: 替换 Grafana 凭据**

两个 SQL 文件与 `frontend/docs/mcp.json` 中 `GRAFANA_API_KEY` 的值改为：

```text
YOUR_GRAFANA_SERVICE_ACCOUNT_TOKEN
```

- [ ] **Step 3: 运行扫描测试并确认 GREEN**

Run: `npm test -- --run src/security/credential-hygiene.test.ts`

Expected: 5 个参数化用例全部 PASS。

- [ ] **Step 4: 验证 JSON 语法**

Run: `node -e "JSON.parse(require('fs').readFileSync('docs/mcp.json','utf8')); console.log('JSON_OK')"`

Working directory: `frontend`

Expected: `JSON_OK`。

### Task 4: 完整验证并重建安全提交

**Files:**
- Modify: `docs/superpowers/specs/2026-08-20-secret-sanitization-design.md`
- Create: `docs/superpowers/plans/2026-08-20-secret-sanitization.md`
- Include: Task 1-3 的全部代码与文档变更

- [ ] **Step 1: 运行前端验证**

Run: `npm test -- --run`

Expected: 全部测试通过，包含新的 5 个凭据卫生用例。

Run: `npx tsc --noEmit`

Expected: exit code 0。

Run: `npm run build`

Expected: exit code 0。

- [ ] **Step 2: 运行后端目标测试**

Run: `mvn -pl ai-agent-station-study-ann-app -am "-DskipTests=false" "-Dsurefire.failIfNoSpecifiedTests=false" "-Dtest=AutoAgentExecuteResultEntityTest,FixedAgentExecuteStrategyTest" test`

Expected: 2 tests，0 failures，`BUILD SUCCESS`。

- [ ] **Step 3: 重建单一提交**

将当前已验证工作树相对 `github/main` 重新暂存并创建唯一提交：

```text
装配了前端ui页面的可运行版本
```

确认 `main` 与 `feat/runnable-frontend-ui` 都指向新提交，且新提交的父提交是 `github/main`；旧的含凭据提交不在新分支祖先中。

- [ ] **Step 4: 推送并创建 PR**

通过已认证的 GitHub SSH 推送 `feat/runnable-frontend-ui`，然后创建 base 为 `main`、head 为 `feat/runnable-frontend-ui` 的 Pull Request。

Expected: Push Protection 不再报错；PR 页面显示单一安全提交。
