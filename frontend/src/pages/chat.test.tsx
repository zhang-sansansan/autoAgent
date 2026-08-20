import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { beforeEach, describe, expect, it, vi } from 'vitest';

import { ChatService } from '../services/chat-service';
import { ChatPage } from './chat';

vi.mock('../services/chat-service', async () => {
  const actual = await vi.importActual<typeof import('../services/chat-service')>('../services/chat-service');
  return { ...actual, ChatService: { queryAvailableAgents: vi.fn(), streamAutoAgent: vi.fn() } };
});

describe('ChatPage', () => {
  beforeEach(() => {
    vi.mocked(ChatService.queryAvailableAgents).mockResolvedValue([{ agentId: 'agent-1', agentName: '研究助手', description: '帮助研究' }]);
  });

  it('streams a response for the selected agent', async () => {
    vi.mocked(ChatService.streamAutoAgent).mockImplementation(async (_request, onChunk) => { onChunk('你好'); onChunk('，世界'); });
    render(<MemoryRouter><ChatPage /></MemoryRouter>);
    fireEvent.click(await screen.findByRole('button', { name: /研究助手/ }));
    fireEvent.change(screen.getByPlaceholderText('输入消息，Enter 发送'), { target: { value: '测试' } });
    fireEvent.click(screen.getByRole('button', { name: '发送' }));
    await waitFor(() => expect(screen.getByText('你好，世界')).toBeInTheDocument());
    expect(ChatService.streamAutoAgent).toHaveBeenCalledWith(expect.objectContaining({ aiAgentId: 'agent-1', message: '测试' }), expect.any(Function), expect.any(AbortSignal));
  });

  it('can start a new local session without a server-history capability', async () => {
    render(<MemoryRouter><ChatPage /></MemoryRouter>);
    expect(await screen.findByRole('button', { name: /研究助手/ })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: '新会话' })).toBeInTheDocument();
  });
});
