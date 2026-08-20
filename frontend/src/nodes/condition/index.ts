import { nanoid } from 'nanoid';
import { FlowNodeRegistry } from '../../typings';
import iconCondition from '../../assets/icon-condition.svg';
import { WorkflowNodeType } from '../constants';
import { formMeta } from './form-meta';

export const ConditionNodeRegistry: FlowNodeRegistry = {
  type: WorkflowNodeType.Condition,
  info: { icon: iconCondition, description: '连接多个下游分支，并按条件选择要执行的路径。' },
  meta: { defaultPorts: [{ type: 'input' }], useDynamicPort: true, expandable: false },
  formMeta,
  onAdd() { return { id: `condition_${nanoid(5)}`, type: 'condition', data: { title: 'Condition', inputsValues: { conditions: [{ key: `if_${nanoid(5)}`, value: { type: 'expression', content: '' } }, { key: `if_${nanoid(5)}`, value: { type: 'expression', content: '' } }] }, inputs: { type: 'object', properties: { conditions: { type: 'array', items: { type: 'object', properties: { key: { type: 'string' }, value: { type: 'string' } } } } } } } }; },
};
