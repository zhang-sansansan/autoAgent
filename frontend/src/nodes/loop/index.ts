import { nanoid } from 'nanoid';
import { FlowNodeTransformData, PositionSchema, WorkflowNodeEntity } from '@flowgram.ai/free-layout-editor';
import { defaultFormMeta } from '../default-form-meta';
import { FlowNodeRegistry } from '../../typings';
import iconLoop from '../../assets/icon-loop.jpg';
import { LoopFormRender } from './loop-form-render';
import { WorkflowNodeType } from '../constants';

let index = 0;
export const LoopNodeRegistry: FlowNodeRegistry = {
  type: WorkflowNodeType.Loop,
  info: { icon: iconLoop, description: '设置迭代次数，并在子画布内重复执行一组节点。' },
  meta: { isContainer: true, size: { width: 560, height: 400 }, padding: () => ({ top: 125, bottom: 100, left: 100, right: 100 }), selectable(node: WorkflowNodeEntity, mousePos?: PositionSchema) { if (!mousePos) return true; return !node.getData<FlowNodeTransformData>(FlowNodeTransformData).bounds.contains(mousePos.x, mousePos.y); }, expandable: false },
  onAdd() { return { id: `loop_${nanoid(5)}`, type: 'loop', data: { title: `Loop_${++index}`, inputsValues: { loopTimes: 2 }, inputs: { type: 'object', required: ['loopTimes'], properties: { loopTimes: { type: 'number' } } }, outputs: { type: 'object', properties: { result: { type: 'string' } } } } }; },
  formMeta: { ...defaultFormMeta, render: LoopFormRender },
  onCreate() {},
};
