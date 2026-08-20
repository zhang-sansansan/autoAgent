import { render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { describe, expect, it, vi } from 'vitest';

import { DataStatisticsService } from '../services/data-statistics-service';
import { DashboardPage } from './dashboard';

vi.mock('../services/data-statistics-service', async () => {
  const actual = await vi.importActual<typeof import('../services/data-statistics-service')>('../services/data-statistics-service');
  return { ...actual, DataStatisticsService: { getDataStatistics: vi.fn() } };
});

describe('DashboardPage', () => {
  it('shows only the seven real resource totals', async () => {
    vi.mocked(DataStatisticsService.getDataStatistics).mockResolvedValue({ activeAgentCount: 1, clientCount: 2, mcpToolCount: 3, systemPromptCount: 4, ragOrderCount: 5, advisorCount: 6, modelCount: 7, todayRequestCount: 0, successRate: 95.5, runningTaskCount: 0 });
    render(<MemoryRouter><DashboardPage /></MemoryRouter>);
    await waitFor(() => expect(screen.getByText('7')).toBeInTheDocument());
    ['Agent 总数','客户端','MCP 工具','系统 Prompt','RAG 订单','Advisor','模型'].forEach((label) => expect(screen.getByText(label)).toBeInTheDocument());
    expect(screen.queryByText('今日请求')).not.toBeInTheDocument();
    expect(screen.queryByText('成功率')).not.toBeInTheDocument();
    expect(screen.queryByText('运行中任务')).not.toBeInTheDocument();
  });
});
