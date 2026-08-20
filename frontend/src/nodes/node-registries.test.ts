import { describe, expect, it } from 'vitest';

import { visibleNodeRegistries } from './index';

describe('workflow node registries', () => {
  it('keeps all twelve PRD workflow node types available', () => {
    expect(visibleNodeRegistries.map((registry) => registry.type)).toEqual(expect.arrayContaining([
      'start', 'agent', 'client', 'advisor', 'model', 'prompt', 'tool_mcp', 'llm', 'task', 'condition', 'loop', 'end',
    ]));
    expect(visibleNodeRegistries).toHaveLength(12);
  });
});
