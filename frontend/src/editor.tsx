import { EditorRenderer, FreeLayoutEditorProvider } from '@flowgram.ai/free-layout-editor';

import '@flowgram.ai/free-layout-editor/index.css';
import './styles/index.css';
import { nodeRegistries } from './nodes';
import { initialData } from './initial-data';
import { useEditorProps } from './hooks';
import { DemoTools } from './components/tools';
import { SidebarProvider, SidebarRenderer } from './components/sidebar';
import { FlowDocumentJSON } from './typings';

export interface EditorProps {
  /** 外部传入的初始数据（用于加载已保存的流程配置） */
  data?: FlowDocumentJSON;
  /** 内容变更回调（序列化后的画布数据） */
  onDataChange?: (json: FlowDocumentJSON) => void;
}

export const Editor = ({ data, onDataChange }: EditorProps) => {
  const editorProps = useEditorProps(data || initialData, nodeRegistries, onDataChange);

  return (
    <div className="doc-free-feature-overview">
      <FreeLayoutEditorProvider {...editorProps}>
        <SidebarProvider>
          <div className="demo-container">
            <EditorRenderer className="demo-editor" />
          </div>
          <DemoTools />
          <SidebarRenderer />
        </SidebarProvider>
      </FreeLayoutEditorProvider>
    </div>
  );
};
