import { describe, expect, it } from 'vitest';

import { NAV_GROUPS, PAGE_ENTRIES, searchPages } from './navigation';

describe('navigation configuration', () => {
  it('contains the 13 unique product routes in four groups', () => {
    expect(PAGE_ENTRIES).toHaveLength(13);
    expect(new Set(PAGE_ENTRIES.map((page) => page.path)).size).toBe(13);
    expect(NAV_GROUPS.map((group) => group.label)).toEqual([
      '工作台',
      'Agent',
      '资源管理',
      '系统管理',
    ]);
  });

  it('matches Chinese names and technical keywords', () => {
    expect(searchPages('编排').map((page) => page.path)).toContain('/agent-config');
    expect(searchPages('MCP').map((page) => page.path)).toContain(
      '/client-tool-mcp-management'
    );
  });

  it('returns no business actions or mock results', () => {
    expect(searchPages('删除')).toEqual([]);
    expect(searchPages('不存在的模型')).toEqual([]);
  });
});
