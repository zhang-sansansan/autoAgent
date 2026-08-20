import { FormMeta, FormRenderProps, ValidateTrigger } from '@flowgram.ai/free-layout-editor';
import { FlowNodeJSON } from '../../typings';
import { FormContent, FormHeader } from '../../form-components';
import { ConditionInputs } from './condition-inputs';

export const formMeta: FormMeta<FlowNodeJSON> = {
  render: ({ form: _form }: FormRenderProps<FlowNodeJSON>) => <><FormHeader /><FormContent><ConditionInputs /></FormContent></>,
  validateTrigger: ValidateTrigger.onChange,
  validate: { title: ({ value }: { value: string }) => value ? undefined : '请输入标题', 'inputsValues.conditions.*': ({ value }) => value?.value?.content ? undefined : '请输入分支条件' },
};
