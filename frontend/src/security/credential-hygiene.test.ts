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
