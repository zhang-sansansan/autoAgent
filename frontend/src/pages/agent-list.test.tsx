import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { beforeEach, describe, expect, it, vi } from 'vitest';

import { AiAgentService } from '../services/ai-agent-service';
import { ChatService } from '../services/chat-service';
import { AgentListPage } from './agent-list';

vi.mock('../services/chat-service', async () => {
  const actual = await vi.importActual<typeof import('../services/chat-service')>('../services/chat-service');
  return { ...actual, ChatService: { queryAvailableAgents: vi.fn() } };
});
vi.mock('../services/ai-agent-service', () => ({ AiAgentService: { armoryAgent: vi.fn() } }));

describe('AgentListPage', () => {
  beforeEach(() => {
    vi.mocked(ChatService.queryAvailableAgents).mockResolvedValue([
      { agentId: 'agent-001', agentName: '订单助手', description: '处理订单', channel: 'web', strategy: 'flow', status: 1 },
    ]);
  });

  it('renders backend agent fields and supported actions', async () => {
    render(<MemoryRouter><AgentListPage /></MemoryRouter>);
    expect(await screen.findByText('订单助手')).toBeInTheDocument();
    ['agent-001', '处理订单', 'web', 'flow', '启用'].forEach((value) => expect(screen.getByText(value)).toBeInTheDocument());
    expect(screen.getByRole('button', { name: '装配' })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: '对话' })).toBeInTheDocument();
  });

  it('arms an agent through the existing backend endpoint', async () => {
    vi.mocked(AiAgentService.armoryAgent).mockResolvedValue(true);
    render(<MemoryRouter><AgentListPage /></MemoryRouter>);
    fireEvent.click(await screen.findByRole('button', { name: '装配' }));
    await waitFor(() => expect(AiAgentService.armoryAgent).toHaveBeenCalledWith('agent-001'));
  });
});
