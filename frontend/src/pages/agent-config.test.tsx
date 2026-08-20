import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { LayoutProvider } from '../context/layout-context';
import { AiAgentDrawService } from '../services/ai-agent-draw-service';
import { AgentConfigPage } from './agent-config';

vi.mock('../editor', () => ({ Editor: ({ onDataChange }: { onDataChange: (data: unknown) => void }) => <button onClick={() => onDataChange({ nodes: [{ id: 'changed' }], edges: [] })}>测试画布</button> }));
vi.mock('../services/ai-agent-draw-service', () => ({ AiAgentDrawService: { queryDrawConfigList: vi.fn(), getDrawConfig: vi.fn(), saveConfig: vi.fn(), deleteDrawConfig: vi.fn() } }));

describe('AgentConfigPage', () => {
  beforeEach(() => { vi.mocked(AiAgentDrawService.queryDrawConfigList).mockResolvedValue([]); vi.mocked(AiAgentDrawService.saveConfig).mockResolvedValue(true); });
  const renderPage = () => render(<MemoryRouter><LayoutProvider><AgentConfigPage /></LayoutProvider></MemoryRouter>);

  it('keeps workflow persistence and focus mode controls', async () => {
    renderPage();
    expect(await screen.findByRole('button', { name: '测试画布' })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: '专注模式' })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: '保存配置' })).toBeInTheDocument();
  });

  it('saves the latest editor data instead of the initial document', async () => {
    renderPage();
    fireEvent.click(await screen.findByRole('button', { name: '测试画布' }));
    fireEvent.change(screen.getByPlaceholderText('配置名称'), { target: { value: '新流程' } });
    fireEvent.click(screen.getByRole('button', { name: '保存配置' }));
    await waitFor(() => expect(AiAgentDrawService.saveConfig).toHaveBeenCalledWith(expect.objectContaining({ configName: '新流程', configData: expect.stringContaining('changed') })));
  });
});
