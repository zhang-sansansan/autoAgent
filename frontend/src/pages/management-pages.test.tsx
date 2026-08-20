import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { afterEach, describe, expect, it, vi } from 'vitest';

import { AdvisorManagement } from './advisor-management';
import { AiClientApiManagement } from './ai-client-api-management';
import { ClientManagement } from './client-management';
import { ClientModelManagement } from './client-model-management';
import { ClientSystemPromptManagement } from './client-system-prompt-management';
import { ClientToolMcpManagement } from './client-tool-mcp-management';
import { RagOrderManagement } from './rag-order-management';
import { UserManagement } from './user-management';

describe('management page smoke coverage', () => {
  afterEach(() => vi.unstubAllGlobals());

  it.each([
    { Page: ClientManagement, title: '客户端管理' },
    { Page: AiClientApiManagement, title: '模型API管理' },
    { Page: ClientModelManagement, title: 'AI模型配置管理' },
    { Page: AdvisorManagement, title: '顾问配置管理' },
    { Page: ClientSystemPromptManagement, title: '系统提示词管理' },
    { Page: ClientToolMcpManagement, title: 'MCP客户端工具管理' },
    { Page: RagOrderManagement, title: '知识库配置管理' },
    { Page: UserManagement, title: '用户管理' },
  ])('renders $title without relying on mock business rows', async ({ Page, title }) => {
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue(new Response(JSON.stringify({ code: '0000', info: 'ok', data: [] }), { status: 200 })));
    render(<MemoryRouter><Page /></MemoryRouter>);
    expect(await screen.findByRole('heading', { name: title })).toBeInTheDocument();
  });
});
