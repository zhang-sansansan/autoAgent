import React, { useCallback, useEffect, useRef, useState } from 'react';
import { Button, Input, Modal, Select, Toast } from '@douyinfe/semi-ui';
import { IconDelete, IconExpand, IconPlus, IconSave, IconShrink } from '@douyinfe/semi-icons';
import { useSearchParams } from 'react-router-dom';
import styled from 'styled-components';

import { useLayout } from '../context/layout-context';
import { Editor } from '../editor';
import { initialData } from '../initial-data';
import { AiAgentDrawConfigResponseDTO, AiAgentDrawService } from '../services/ai-agent-draw-service';
import { FlowDocumentJSON } from '../typings';

const Page = styled.section<{ $focus: boolean }>`display: flex; flex-direction: column; width: 100%; height: ${({$focus}) => $focus ? 'calc(100vh - 48px)' : 'calc(100vh - 112px)'}; min-height: 660px;`;
const Toolbar = styled.header`display: flex; align-items: center; gap: 10px; margin-bottom: 14px; padding: 14px 16px; border: 1px solid rgba(151,160,198,.14); border-radius: 16px; background: linear-gradient(135deg,rgba(19,23,38,.96),rgba(10,13,22,.98));`;
const Title = styled.div`min-width: 180px; margin-right: auto; h1 { margin: 0 0 4px; color: #f7f7ff; font-size: 19px; } p { margin: 0; color: #727990; font-size: 11px; }`;
const Canvas = styled.div`position: relative; flex: 1; min-height: 0; overflow: hidden; border: 1px solid rgba(139,92,246,.24); border-radius: 18px; background: #090b13; box-shadow: 0 20px 60px rgba(0,0,0,.28);`;
const Dirty = styled.span`padding: 5px 9px; color: #fbbf24; font-size: 11px; border: 1px solid rgba(251,191,36,.25); border-radius: 999px; background: rgba(251,191,36,.08);`;

export const AgentConfigPage: React.FC = () => {
  const [searchParams] = useSearchParams();
  const { focusMode, enterFocusMode, exitFocusMode } = useLayout();
  const [configs, setConfigs] = useState<AiAgentDrawConfigResponseDTO[]>([]);
  const [selectedId, setSelectedId] = useState<string | null>(null);
  const [name, setName] = useState('');
  const [data, setData] = useState<FlowDocumentJSON>(initialData);
  const [editorKey, setEditorKey] = useState(0);
  const [dirty, setDirty] = useState(false);
  const [saving, setSaving] = useState(false);
  const [loading, setLoading] = useState(false);
  const latestRef = useRef<FlowDocumentJSON>(initialData);

  const loadList = useCallback(async () => {
    try { setConfigs(await AiAgentDrawService.queryDrawConfigList({ pageNum: 1, pageSize: 100 })); }
    catch { Toast.error('流程列表加载失败'); }
  }, []);

  const applyDocument = (document: FlowDocumentJSON, configName = '', configId: string | null = null) => {
    setData(document); latestRef.current = document; setName(configName); setSelectedId(configId); setDirty(false); setEditorKey((value) => value + 1);
  };

  const confirmDiscard = (action: () => void) => {
    if (!dirty) { action(); return; }
    Modal.confirm({ title: '放弃未保存的修改？', content: '当前画布内容尚未保存，继续后这些修改将无法恢复。', okText: '放弃修改', cancelText: '继续编辑', okType: 'danger', onOk: action });
  };

  const loadConfig = useCallback(async (configId: string) => {
    setLoading(true);
    try {
      const config = await AiAgentDrawService.getDrawConfig(configId);
      if (!config?.configData) throw new Error('empty');
      const document = JSON.parse(config.configData) as FlowDocumentJSON;
      applyDocument(document, config.configName || '', config.configId);
      Toast.success('流程配置已加载');
    } catch { Toast.error('流程配置加载失败，当前画布已保留'); }
    finally { setLoading(false); }
  }, []);

  useEffect(() => {
    void loadList();
    const configId = searchParams.get('configId');
    if (configId) void loadConfig(configId);
  }, [loadConfig, loadList, searchParams]);

  useEffect(() => {
    const warn = (event: BeforeUnloadEvent) => { if (dirty) { event.preventDefault(); event.returnValue = ''; } };
    window.addEventListener('beforeunload', warn);
    return () => window.removeEventListener('beforeunload', warn);
  }, [dirty]);

  useEffect(() => () => exitFocusMode(), [exitFocusMode]);

  const save = async () => {
    if (!name.trim()) { Toast.warning('请输入配置名称'); return; }
    setSaving(true);
    try {
      const success = await AiAgentDrawService.saveConfig({ configId: selectedId || undefined, configName: name.trim(), configData: JSON.stringify(latestRef.current) });
      if (!success) throw new Error('failed');
      setDirty(false); Toast.success('流程配置保存成功'); await loadList();
    } catch { Toast.error('保存失败，当前画布内容已保留'); }
    finally { setSaving(false); }
  };

  const remove = () => {
    if (!selectedId) { Toast.warning('请先选择已保存的流程'); return; }
    Modal.confirm({ title: `删除流程「${name || selectedId}」？`, content: '删除后无法恢复，画布会重置为空白流程。', okText: '确认删除', cancelText: '取消', okType: 'danger', onOk: async () => {
      try { await AiAgentDrawService.deleteDrawConfig(selectedId); applyDocument(initialData); await loadList(); Toast.success('流程已删除'); }
      catch { Toast.error('删除失败，当前画布已保留'); }
    } });
  };

  return <Page $focus={focusMode}>
    <Toolbar><Title><h1>Agent 编排</h1><p>自由画布 · 12 类节点 · 配置由后端持久化</p></Title>{dirty && <Dirty>未保存</Dirty>}<Select aria-label="选择流程" placeholder="选择已有流程" value={selectedId || undefined} loading={loading} style={{ width: 180 }} onChange={(value) => confirmDiscard(() => void loadConfig(value as string))}>{configs.map((config) => <Select.Option key={config.configId} value={config.configId}>{config.configName}</Select.Option>)}</Select><Button aria-label="新建流程" icon={<IconPlus />} onClick={() => confirmDiscard(() => applyDocument(initialData))}>新建</Button><Input aria-label="配置名称" placeholder="配置名称" value={name} onChange={(value) => { setName(value); setDirty(true); }} style={{ width: 150 }} /><Button aria-label="保存配置" theme="solid" type="primary" icon={<IconSave />} loading={saving} onClick={() => void save()}>保存配置</Button><Button aria-label="删除配置" type="danger" theme="borderless" icon={<IconDelete />} onClick={remove}>删除</Button><Button aria-label={focusMode ? '退出专注' : '专注模式'} icon={focusMode ? <IconShrink /> : <IconExpand />} onClick={focusMode ? exitFocusMode : enterFocusMode}>{focusMode ? '退出专注' : '专注模式'}</Button></Toolbar>
    <Canvas className="ann-workflow-theme"><Editor key={editorKey} data={data} onDataChange={(document) => { latestRef.current = document; setDirty(true); }} /></Canvas>
  </Page>;
};
