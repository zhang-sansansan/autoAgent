import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { Button, Input, Space, Table, Tag, Toast } from '@douyinfe/semi-ui';
import { IconBranch, IconComment, IconCopy, IconEdit, IconRefresh, IconSearch } from '@douyinfe/semi-icons';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';

import { AiAgentService } from '../services/ai-agent-service';
import { AiAgentVO, ChatService } from '../services/chat-service';

const Page = styled.section`width: min(1480px, 100%); margin: 0 auto;`;
const Header = styled.header`display: flex; align-items: flex-end; justify-content: space-between; gap: 20px; margin-bottom: 22px;`;
const Heading = styled.div`h1 { margin: 0 0 8px; color: #f7f7ff; font-size: 26px; letter-spacing: -.025em; } p { margin: 0; color: #7f869e; font-size: 13px; }`;
const Toolbar = styled.div`display: flex; align-items: center; gap: 10px; margin-bottom: 14px;`;
const TablePanel = styled.div`
  overflow: hidden;
  border: 1px solid rgba(151,160,198,.14);
  border-radius: 18px;
  background: linear-gradient(145deg, rgba(18,22,36,.96), rgba(10,13,22,.98));
  box-shadow: 0 18px 48px rgba(0,0,0,.2);
  .semi-table-container { border-radius: 18px; }
`;
const IdCell = styled.div`display: flex; align-items: center; gap: 6px; code { color: #b7abff; font-size: 12px; }`;
const Empty = styled.div`padding: 68px 20px; color: #747b94; text-align: center; strong { display: block; margin-bottom: 8px; color: #aab0c4; }`;
const Error = styled.div`display: flex; align-items: center; justify-content: center; gap: 14px; min-height: 260px; color: #a7acc0;`;

export const AgentListPage: React.FC = () => {
  const navigate = useNavigate();
  const [agents, setAgents] = useState<AiAgentVO[]>([]);
  const [query, setQuery] = useState('');
  const [loading, setLoading] = useState(true);
  const [failed, setFailed] = useState(false);
  const [armingId, setArmingId] = useState<string | null>(null);

  const load = useCallback(async () => {
    setLoading(true);
    setFailed(false);
    try { setAgents(await ChatService.queryAvailableAgents()); }
    catch { setFailed(true); }
    finally { setLoading(false); }
  }, []);

  useEffect(() => { void load(); }, [load]);

  const visibleAgents = useMemo(() => {
    const keyword = query.trim().toLowerCase();
    if (!keyword) return agents;
    return agents.filter((agent) => [agent.agentId, agent.agentName, agent.description, agent.channel, agent.strategy].some((value) => value?.toLowerCase().includes(keyword)));
  }, [agents, query]);

  const arm = async (agent: AiAgentVO) => {
    setArmingId(agent.agentId);
    try {
      const success = await AiAgentService.armoryAgent(agent.agentId);
      success ? Toast.success(`Agent「${agent.agentName || agent.agentId}」装配成功`) : Toast.error('Agent 装配失败');
    } catch { Toast.error('Agent 装配失败，请稍后重试'); }
    finally { setArmingId(null); }
  };

  const copyId = async (id: string) => {
    try { await navigator.clipboard.writeText(id); Toast.success('Agent ID 已复制'); }
    catch { Toast.error('复制失败'); }
  };

  const columns = [
    { title: 'Agent ID', dataIndex: 'agentId', width: 185, render: (id: string) => <IdCell><code>{id}</code><Button aria-label={`复制 ${id}`} theme="borderless" size="small" icon={<IconCopy />} onClick={() => void copyId(id)} /></IdCell> },
    { title: '名称', dataIndex: 'agentName', width: 170, render: (value: string) => value || '—' },
    { title: '描述', dataIndex: 'description', render: (value: string) => value || '—' },
    { title: '渠道', dataIndex: 'channel', width: 115, render: (value: string) => value || '—' },
    { title: '策略', dataIndex: 'strategy', width: 140, render: (value: string) => value || '—' },
    { title: '状态', dataIndex: 'status', width: 95, render: (status: number) => <Tag color={status === 1 ? 'green' : 'grey'}>{status === 1 ? '启用' : '禁用'}</Tag> },
    { title: '操作', width: 178, fixed: 'right' as const, render: (_: unknown, agent: AiAgentVO) => <Space><Button aria-label="装配" size="small" theme="solid" icon={<IconBranch />} loading={armingId === agent.agentId} onClick={() => void arm(agent)}>装配</Button><Button aria-label="对话" size="small" theme="borderless" icon={<IconComment />} onClick={() => navigate('/chat', { state: { agentId: agent.agentId } })}>对话</Button></Space> },
  ];

  return <Page>
    <Header><Heading><h1>Agent 列表</h1><p>查询后端当前可用的 Agent，并执行装配或进入对话调试。</p></Heading><Button theme="solid" type="primary" icon={<IconEdit />} onClick={() => navigate('/agent-config')}>进入流程编排</Button></Header>
    <Toolbar><Input prefix={<IconSearch />} showClear value={query} onChange={setQuery} placeholder="搜索 ID、名称、渠道或策略" style={{ width: 310 }} /><Button icon={<IconRefresh />} onClick={() => void load()}>刷新</Button></Toolbar>
    <TablePanel>
      {failed ? <Error><span>Agent 数据加载失败</span><Button onClick={() => void load()}>重新加载</Button></Error> : <Table columns={columns} dataSource={visibleAgents} rowKey="agentId" loading={loading} scroll={{ x: 1080 }} pagination={visibleAgents.length > 10 ? { pageSize: 10 } : false} empty={<Empty><strong>{query ? '没有匹配的 Agent' : '暂无可用 Agent'}</strong><span>{query ? '尝试调整搜索关键词。' : '请先在流程编排中创建并保存 Agent。'}</span></Empty>} />}
    </TablePanel>
  </Page>;
};
