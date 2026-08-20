import React, { useCallback, useEffect, useState } from 'react';
import { Button, Skeleton } from '@douyinfe/semi-ui';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';

import {
  DataStatisticsResponseDTO,
  DataStatisticsService,
} from '../services/data-statistics-service';

const Page = styled.section`
  width: min(1480px, 100%);
  margin: 0 auto;
`;

const Hero = styled.div`
  position: relative;
  overflow: hidden;
  min-height: 214px;
  padding: 34px 38px;
  border: 1px solid rgba(139, 92, 246, 0.24);
  border-radius: 24px;
  background:
    radial-gradient(circle at 84% 24%, rgba(34, 211, 238, 0.16), transparent 28%),
    radial-gradient(circle at 72% 118%, rgba(139, 92, 246, 0.28), transparent 40%),
    linear-gradient(135deg, rgba(19, 23, 39, 0.96), rgba(10, 12, 22, 0.98));
  box-shadow: 0 24px 80px rgba(0, 0, 0, 0.28);

  &::after {
    content: '';
    position: absolute;
    inset: 0;
    pointer-events: none;
    opacity: 0.28;
    background-image: linear-gradient(rgba(255,255,255,.04) 1px, transparent 1px), linear-gradient(90deg, rgba(255,255,255,.04) 1px, transparent 1px);
    background-size: 36px 36px;
    mask-image: linear-gradient(90deg, transparent 35%, #000);
  }
`;

const Eyebrow = styled.div`
  display: inline-flex;
  align-items: center;
  gap: 8px;
  color: #79e7f6;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: .14em;
  text-transform: uppercase;

  &::before { content: ''; width: 7px; height: 7px; border-radius: 50%; background: #34d399; box-shadow: 0 0 16px #34d399; }
`;

const Title = styled.h1`
  position: relative;
  z-index: 1;
  margin: 22px 0 10px;
  color: #f8f8ff;
  font-size: clamp(28px, 3vw, 42px);
  line-height: 1.15;
  letter-spacing: -.035em;
`;

const Subtitle = styled.p`
  position: relative;
  z-index: 1;
  width: min(650px, 100%);
  margin: 0;
  color: #a7acc0;
  font-size: 15px;
  line-height: 1.75;
`;

const SectionHeader = styled.div`
  display: flex;
  align-items: end;
  justify-content: space-between;
  margin: 30px 2px 16px;
`;

const SectionTitle = styled.h2`
  margin: 0;
  color: #f7f7ff;
  font-size: 18px;
  letter-spacing: -.01em;
`;

const SectionHint = styled.span`color: #747b94; font-size: 12px;`;

const MetricGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 14px;

  @media (max-width: 1180px) { grid-template-columns: repeat(2, minmax(0, 1fr)); }
`;

const MetricCard = styled.article<{ $accent: string }>`
  position: relative;
  overflow: hidden;
  min-height: 138px;
  padding: 20px;
  border: 1px solid rgba(151, 160, 198, 0.14);
  border-radius: 17px;
  background: linear-gradient(145deg, rgba(19, 23, 37, .94), rgba(11, 14, 24, .96));
  transition: transform 180ms ease, border-color 180ms ease;

  &::before { content: ''; position: absolute; inset: 0 auto auto 0; width: 100%; height: 2px; background: ${({ $accent }) => $accent}; opacity: .82; }
  &::after { content: ''; position: absolute; right: -40px; top: -48px; width: 116px; height: 116px; border-radius: 50%; background: ${({ $accent }) => $accent}; opacity: .07; filter: blur(2px); }
  &:hover { transform: translateY(-3px); border-color: rgba(139, 92, 246, .34); }
`;

const MetricLabel = styled.div`color: #8e95ad; font-size: 13px;`;
const MetricValue = styled.div`margin-top: 20px; color: #f9f9ff; font-size: 34px; font-weight: 700; letter-spacing: -.04em;`;

const ActionGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 14px;
  @media (max-width: 980px) { grid-template-columns: 1fr; }
`;

const ActionCard = styled.button`
  display: flex;
  align-items: center;
  gap: 15px;
  width: 100%;
  padding: 19px;
  color: inherit;
  text-align: left;
  border: 1px solid rgba(151, 160, 198, .14);
  border-radius: 16px;
  background: rgba(16, 20, 33, .84);
  cursor: pointer;
  transition: background 180ms ease, border-color 180ms ease, transform 180ms ease;
  &:hover { transform: translateY(-2px); border-color: rgba(34, 211, 238, .3); background: rgba(21, 25, 42, .96); }
`;

const ActionIcon = styled.span`
  display: grid;
  place-items: center;
  flex: 0 0 42px;
  height: 42px;
  color: #d8d3ff;
  font-weight: 800;
  border-radius: 12px;
  background: linear-gradient(135deg, rgba(139,92,246,.24), rgba(34,211,238,.13));
`;
const ActionCopy = styled.span`display: grid; gap: 4px; strong { color: #f4f3ff; font-size: 14px; } small { color: #747b94; font-size: 12px; }`;
const Arrow = styled.span`margin-left: auto; color: #727993; font-size: 18px;`;

const ErrorBox = styled.div`
  display: grid;
  place-items: center;
  gap: 12px;
  min-height: 240px;
  margin-top: 16px;
  color: #a7acc0;
  border: 1px dashed rgba(251, 113, 133, .3);
  border-radius: 18px;
  background: rgba(25, 16, 25, .44);
`;

const metrics: Array<{ label: string; key: keyof DataStatisticsResponseDTO; accent: string }> = [
  { label: 'Agent 总数', key: 'activeAgentCount', accent: '#8b5cf6' },
  { label: '客户端', key: 'clientCount', accent: '#22d3ee' },
  { label: 'MCP 工具', key: 'mcpToolCount', accent: '#34d399' },
  { label: '系统 Prompt', key: 'systemPromptCount', accent: '#fbbf24' },
  { label: 'RAG 订单', key: 'ragOrderCount', accent: '#fb7185' },
  { label: 'Advisor', key: 'advisorCount', accent: '#38bdf8' },
  { label: '模型', key: 'modelCount', accent: '#a78bfa' },
];

const actions = [
  { mark: 'AG', title: 'Agent 管理', description: '查看与维护已发布 Agent', path: '/agent-list' },
  { mark: 'FX', title: '创建工作流', description: '进入可视化 Agent 编排器', path: '/agent-config' },
  { mark: 'AI', title: '对话调试', description: '验证 Agent 的回复与表现', path: '/chat' },
];

export const DashboardPage: React.FC = () => {
  const navigate = useNavigate();
  const [data, setData] = useState<DataStatisticsResponseDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);

  const load = useCallback(async () => {
    setLoading(true);
    setError(false);
    try {
      setData(await DataStatisticsService.getDataStatistics());
    } catch {
      setError(true);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => { void load(); }, [load]);

  return (
    <Page>
      <Hero>
        <Eyebrow>Workspace online</Eyebrow>
        <Title>构建、连接并运行你的 AI Agent</Title>
        <Subtitle>从一个清晰的工作台进入编排、资源配置与对话调试。所有统计均来自当前后端数据，不使用演示数据填充。</Subtitle>
      </Hero>

      <SectionHeader><SectionTitle>资源概览</SectionTitle><SectionHint>实时读取系统资源总量</SectionHint></SectionHeader>
      {error ? (
        <ErrorBox><span>统计数据加载失败</span><Button theme="solid" type="primary" onClick={() => void load()}>重新加载</Button></ErrorBox>
      ) : (
        <MetricGrid aria-busy={loading}>
          {metrics.map((metric) => (
            <MetricCard key={metric.key} $accent={metric.accent}>
              <MetricLabel>{metric.label}</MetricLabel>
              {loading ? <Skeleton.Title style={{ width: 72, marginTop: 22 }} /> : <MetricValue>{data?.[metric.key] ?? 0}</MetricValue>}
            </MetricCard>
          ))}
        </MetricGrid>
      )}

      <SectionHeader><SectionTitle>快速开始</SectionTitle><SectionHint>继续你的核心工作流</SectionHint></SectionHeader>
      <ActionGrid>
        {actions.map((action) => <ActionCard key={action.path} type="button" onClick={() => navigate(action.path)}><ActionIcon>{action.mark}</ActionIcon><ActionCopy><strong>{action.title}</strong><small>{action.description}</small></ActionCopy><Arrow>→</Arrow></ActionCard>)}
      </ActionGrid>
    </Page>
  );
};
