import React, { useEffect, useRef, useState } from 'react';
import { Button, Empty, Spin, Toast } from '@douyinfe/semi-ui';
import { IconPlus, IconSend, IconStop } from '@douyinfe/semi-icons';
import { useLocation } from 'react-router-dom';
import styled from 'styled-components';

import { AiAgentVO, ChatService } from '../services/chat-service';

interface Message { role: 'user' | 'assistant'; content: string; error?: boolean; source?: string; }

const Page = styled.section`display: flex; flex-direction: column; width: min(1480px, 100%); height: calc(100vh - 112px); min-height: 620px; margin: 0 auto;`;
const PageHeader = styled.header`display: flex; align-items: flex-end; justify-content: space-between; margin-bottom: 18px; h1 { margin: 0 0 7px; color: #f7f7ff; font-size: 26px; } p { margin: 0; color: #7f869e; font-size: 13px; }`;
const Workspace = styled.div`display: grid; grid-template-columns: 270px minmax(0,1fr); flex: 1; min-height: 0; gap: 14px;`;
const Panel = styled.div`display: flex; flex-direction: column; overflow: hidden; border: 1px solid rgba(151,160,198,.14); border-radius: 18px; background: linear-gradient(145deg, rgba(18,22,36,.97), rgba(10,13,22,.98)); box-shadow: 0 18px 52px rgba(0,0,0,.2);`;
const PanelTitle = styled.div`padding: 17px 18px; color: #f0effb; font-size: 13px; font-weight: 700; border-bottom: 1px solid rgba(151,160,198,.11); span { float: right; color: #656c84; font-size: 11px; font-weight: 500; }`;
const AgentList = styled.div`display: grid; align-content: start; gap: 7px; padding: 10px; overflow-y: auto;`;
const AgentButton = styled.button<{ $active: boolean }>`
  position: relative; display: grid; gap: 5px; padding: 13px 14px; color: #eef0fb; text-align: left; border: 1px solid ${({$active}) => $active ? 'rgba(139,92,246,.56)' : 'transparent'}; border-radius: 12px; background: ${({$active}) => $active ? 'linear-gradient(135deg, rgba(139,92,246,.19), rgba(34,211,238,.06))' : 'transparent'}; cursor: pointer;
  &:hover { background-color: rgba(255,255,255,.035); } strong { font-size: 13px; } small { overflow: hidden; color: #747b94; font-size: 11px; text-overflow: ellipsis; white-space: nowrap; }
`;
const ChatHeader = styled.div`display: flex; align-items: center; gap: 11px; padding: 14px 18px; border-bottom: 1px solid rgba(151,160,198,.11); strong { color: #f4f3ff; font-size: 13px; } small { color: #727990; }`;
const LiveDot = styled.span`width: 8px; height: 8px; border-radius: 50%; background: #34d399; box-shadow: 0 0 12px rgba(52,211,153,.7);`;
const Messages = styled.div`display: flex; flex: 1; flex-direction: column; gap: 18px; min-height: 0; padding: 24px; overflow-y: auto;`;
const Row = styled.div<{ $user: boolean }>`display: flex; justify-content: ${({$user}) => $user ? 'flex-end' : 'flex-start'};`;
const Bubble = styled.div<{ $user: boolean; $error?: boolean }>`max-width: min(720px,76%); padding: 13px 16px; color: ${({$user}) => $user ? '#fff' : '#daddE9'}; line-height: 1.7; white-space: pre-wrap; word-break: break-word; border: 1px solid ${({$error,$user}) => $error ? 'rgba(251,113,133,.28)' : $user ? 'rgba(167,139,250,.35)' : 'rgba(151,160,198,.13)'}; border-radius: ${({$user}) => $user ? '16px 4px 16px 16px' : '4px 16px 16px 16px'}; background: ${({$error,$user}) => $error ? 'rgba(64,22,34,.56)' : $user ? 'linear-gradient(135deg,#7557e8,#6351d7)' : 'rgba(24,28,45,.92)'};`;
const Composer = styled.div`display: flex; align-items: flex-end; gap: 10px; padding: 14px; border-top: 1px solid rgba(151,160,198,.11); background: rgba(7,9,16,.42); textarea { flex: 1; min-height: 44px; max-height: 140px; padding: 11px 13px; resize: vertical; color: #f3f3fb; font: inherit; line-height: 1.5; border: 1px solid rgba(151,160,198,.18); border-radius: 12px; outline: none; background: rgba(13,16,27,.9); } textarea:focus { border-color: rgba(139,92,246,.65); box-shadow: 0 0 0 3px rgba(139,92,246,.09); }`;
const Loading = styled.div`display: grid; place-items: center; min-height: 180px;`;

const sessionId = () => `session-${Date.now()}-${Math.random().toString(36).slice(2,8)}`;

export const ChatPage: React.FC = () => {
  const location = useLocation();
  const initialAgentId = (location.state as { agentId?: string } | null)?.agentId;
  const [agents, setAgents] = useState<AiAgentVO[]>([]);
  const [selected, setSelected] = useState<AiAgentVO | null>(null);
  const [messages, setMessages] = useState<Message[]>([]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(true);
  const [streaming, setStreaming] = useState(false);
  const abortRef = useRef<AbortController | null>(null);
  const sessionRef = useRef(sessionId());
  const messageRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    ChatService.queryAvailableAgents().then((list) => {
      setAgents(list);
      if (initialAgentId) setSelected(list.find((item) => item.agentId === initialAgentId) || null);
    }).catch(() => Toast.error('Agent 列表加载失败')).finally(() => setLoading(false));
  }, [initialAgentId]);

  useEffect(() => { if (messageRef.current) messageRef.current.scrollTop = messageRef.current.scrollHeight; }, [messages]);
  useEffect(() => () => abortRef.current?.abort(), []);

  const newSession = () => { abortRef.current?.abort(); abortRef.current = null; sessionRef.current = sessionId(); setMessages([]); setStreaming(false); };

  const send = async () => {
    const content = input.trim();
    if (!content || streaming) return;
    if (!selected) { Toast.warning('请先选择 Agent'); return; }
    setInput(''); setStreaming(true);
    setMessages((items) => [...items, { role: 'user', content }, { role: 'assistant', content: '', source: content }]);
    const controller = new AbortController(); abortRef.current = controller;
    try {
      await ChatService.streamAutoAgent({ aiAgentId: selected.agentId, message: content, sessionId: sessionRef.current, maxStep: 10 }, (chunk) => {
        setMessages((items) => items.map((item, index) => index === items.length - 1 ? { ...item, content: item.content + chunk } : item));
      }, controller.signal);
    } catch (cause) {
      const stopped = (cause as Error).name === 'AbortError';
      setMessages((items) => items.map((item, index) => index === items.length - 1 ? { ...item, content: stopped ? (item.content || '已停止生成') : '请求失败，请重试', error: true } : item));
    } finally { setStreaming(false); abortRef.current = null; }
  };

  const retry = (message: Message) => { setInput(message.source || ''); setMessages((items) => items.slice(0,-1)); };

  return <Page>
    <PageHeader><div><h1>Agent 对话</h1><p>选择已装配 Agent，通过后端流式响应验证实际执行效果。</p></div><Button aria-label="新会话" icon={<IconPlus />} onClick={newSession}>新会话</Button></PageHeader>
    <Workspace>
      <Panel><PanelTitle>可用 Agent <span>{agents.length}</span></PanelTitle>{loading ? <Loading><Spin /></Loading> : agents.length ? <AgentList>{agents.map((agent) => <AgentButton type="button" key={agent.agentId} $active={selected?.agentId === agent.agentId} onClick={() => setSelected(agent)}><strong>{agent.agentName || agent.agentId}</strong><small>{agent.description || agent.agentId}</small></AgentButton>)}</AgentList> : <Empty title="暂无可用 Agent" description="请先完成 Agent 装配" style={{ padding: 28 }} />}</Panel>
      <Panel><ChatHeader><LiveDot /><div><strong>{selected?.agentName || selected?.agentId || '尚未选择 Agent'}</strong><br/><small>{selected ? '流式调试通道已就绪' : '从左侧选择一个 Agent'}</small></div></ChatHeader><Messages ref={messageRef}>{messages.length ? messages.map((message,index) => <Row key={index} $user={message.role === 'user'}><Bubble $user={message.role === 'user'} $error={message.error}>{message.content || '思考中…'}{message.error && <Button size="small" theme="borderless" onClick={() => retry(message)}>重试</Button>}</Bubble></Row>) : <Empty title="开始一次真实对话" description="本页面不会伪造回复，也不会持久化本地会话历史。" style={{ margin: 'auto' }} />}</Messages><Composer><textarea placeholder="输入消息，Enter 发送" value={input} disabled={streaming} maxLength={2000} onChange={(event) => setInput(event.target.value)} onKeyDown={(event) => { if (event.key === 'Enter' && !event.shiftKey) { event.preventDefault(); void send(); } }} />{streaming ? <Button aria-label="停止" theme="solid" type="danger" icon={<IconStop />} onClick={() => abortRef.current?.abort()}>停止</Button> : <Button aria-label="发送" theme="solid" type="primary" icon={<IconSend />} onClick={() => void send()}>发送</Button>}</Composer></Panel>
    </Workspace>
  </Page>;
};
