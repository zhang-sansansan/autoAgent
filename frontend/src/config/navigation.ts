export type NavigationGroupId = 'workspace' | 'agent' | 'resources' | 'system';

export interface PageEntry {
  path: string;
  name: string;
  groupId?: NavigationGroupId;
  groupLabel: string;
  keywords: string[];
  navVisible: boolean;
}

export interface NavigationGroup {
  id: NavigationGroupId;
  label: string;
  pages: PageEntry[];
}

export const PAGE_ENTRIES: PageEntry[] = [
  { path: '/login', name: '登录', groupLabel: '登录', keywords: ['账号', '管理员'], navVisible: false },
  { path: '/dashboard', name: '仪表盘', groupId: 'workspace', groupLabel: '工作台', keywords: ['概览', '统计', '资源'], navVisible: true },
  { path: '/agent-list', name: 'Agent 列表', groupId: 'agent', groupLabel: 'Agent', keywords: ['智能体', '装配', '查询'], navVisible: true },
  { path: '/agent-config', name: 'Agent 编排', groupId: 'agent', groupLabel: 'Agent', keywords: ['流程', '画布', '节点', '编排'], navVisible: true },
  { path: '/chat', name: 'Agent 对话', groupId: 'agent', groupLabel: 'Agent', keywords: ['调试', '聊天', '流式'], navVisible: true },
  { path: '/client-management', name: '客户端管理', groupId: 'resources', groupLabel: '资源管理', keywords: ['Client', '客户端'], navVisible: true },
  { path: '/ai-client-api-management', name: '模型 API 管理', groupId: 'resources', groupLabel: '资源管理', keywords: ['API', 'Key', '接口'], navVisible: true },
  { path: '/client-model-management', name: '模型管理', groupId: 'resources', groupLabel: '资源管理', keywords: ['Model', '模型'], navVisible: true },
  { path: '/advisor-management', name: 'Advisor 管理', groupId: 'resources', groupLabel: '资源管理', keywords: ['Advisor', '顾问'], navVisible: true },
  { path: '/client-system-prompt-management', name: '系统 Prompt 管理', groupId: 'resources', groupLabel: '资源管理', keywords: ['Prompt', '提示词'], navVisible: true },
  { path: '/client-tool-mcp-management', name: 'MCP 工具管理', groupId: 'resources', groupLabel: '资源管理', keywords: ['MCP', '工具', '传输'], navVisible: true },
  { path: '/rag-order-management', name: 'RAG 订单管理', groupId: 'resources', groupLabel: '资源管理', keywords: ['RAG', '知识库', '上传'], navVisible: true },
  { path: '/user-management', name: '用户管理', groupId: 'system', groupLabel: '系统管理', keywords: ['用户', '账号'], navVisible: true },
];

const GROUP_DEFINITIONS: Array<{ id: NavigationGroupId; label: string }> = [
  { id: 'workspace', label: '工作台' },
  { id: 'agent', label: 'Agent' },
  { id: 'resources', label: '资源管理' },
  { id: 'system', label: '系统管理' },
];

export const NAV_GROUPS: NavigationGroup[] = GROUP_DEFINITIONS.map((group) => ({
  ...group,
  pages: PAGE_ENTRIES.filter((page) => page.navVisible && page.groupId === group.id),
}));

export const searchPages = (query: string): PageEntry[] => {
  const normalized = query.trim().toLocaleLowerCase();
  if (!normalized) return PAGE_ENTRIES.filter((page) => page.navVisible);

  return PAGE_ENTRIES.filter((page) => {
    const searchable = [page.name, page.groupLabel, ...page.keywords]
      .join(' ')
      .toLocaleLowerCase();
    return searchable.includes(normalized);
  });
};

export const getPageByPath = (path: string): PageEntry | undefined =>
  PAGE_ENTRIES.find((page) => page.path === path);
