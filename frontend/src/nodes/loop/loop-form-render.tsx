import { FlowNodeJSON, FormRenderProps } from '@flowgram.ai/free-layout-editor';
import { SubCanvasRender } from '@flowgram.ai/free-container-plugin';
import { useIsSidebar } from '../../hooks';
import { FormContent, FormHeader, FormInputs, FormOutputs } from '../../form-components';

export const LoopFormRender = ({ form: _form }: FormRenderProps<FlowNodeJSON>) => {
  const isSidebar = useIsSidebar();
  return <><FormHeader /><FormContent><FormInputs />{!isSidebar && <SubCanvasRender />}<FormOutputs /></FormContent></>;
};
