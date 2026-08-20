import { nanoid } from 'nanoid';
import { Field, FieldArray } from '@flowgram.ai/free-layout-editor';
import { Button } from '@douyinfe/semi-ui';
import { IconCrossCircleStroked, IconPlus } from '@douyinfe/semi-icons';
import { FlowLiteralValueSchema, FlowRefValueSchema } from '../../../typings';
import { useIsSidebar } from '../../../hooks';
import { Feedback, FormItem } from '../../../form-components';
import { FxExpression } from '../../../form-components/fx-expression';
import { ConditionPort } from './styles';

interface ConditionValue { key: string; value: FlowLiteralValueSchema | FlowRefValueSchema; }
export function ConditionInputs() {
  const readonly = !useIsSidebar();
  return <FieldArray name="inputsValues.conditions">{({ field }) => <>{field.map((child,index) => <Field<ConditionValue> key={child.name} name={child.name}>{({ field: item, fieldState }) => <FormItem name="if" type="boolean" required labelWidth={40}><FxExpression value={item.value.value} onChange={(value) => item.onChange({ key: item.value.key, value })} icon={<Button theme="borderless" icon={<IconCrossCircleStroked />} onClick={() => field.delete(index)} />} hasError={Object.keys(fieldState?.errors || {}).length > 0} readonly={readonly} /><Feedback errors={fieldState?.errors} invalid={fieldState?.invalid} /><ConditionPort data-port-id={item.value.key} data-port-type="output" /></FormItem>}</Field>)}{!readonly && <Button theme="borderless" icon={<IconPlus />} onClick={() => field.append({ key: `if_${nanoid(6)}`, value: { type: 'expression', content: '' } })}>添加分支</Button>}</>}</FieldArray>;
}
