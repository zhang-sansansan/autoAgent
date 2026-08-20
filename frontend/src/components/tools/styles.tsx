import styled from 'styled-components';

import { IconMinimap } from '../../assets/icon-minimap';

export const ToolContainer = styled.div`
  position: absolute;
  bottom: 16px;
  display: flex;
  justify-content: left;
  min-width: 360px;
  pointer-events: none;
  gap: 8px;

  z-index: 99;
`;

export const ToolSection = styled.div`
  display: flex;
  align-items: center;
  color: #e8eaf6;
  background: rgba(18, 22, 36, .94);
  border: 1px solid rgba(151, 160, 198, .2);
  border-radius: 10px;
  box-shadow: 0 14px 38px rgba(0, 0, 0, .34);
  column-gap: 2px;
  height: 40px;
  padding: 0 4px;
  pointer-events: auto;
`;

export const SelectZoom = styled.span`
  padding: 4px;
  border-radius: 8px;
  color: #a7aec4;
  border: 1px solid rgba(151, 160, 198, .2);
  font-size: 12px;
  width: 50px;
  cursor: pointer;
`;

export const MinimapContainer = styled.div`
  position: absolute;
  bottom: 60px;
  width: 198px;
`;

export const UIIconMinimap = styled(IconMinimap)<{ visible: boolean }>`
  color: ${(props) => (props.visible ? '#8b5cf6' : '#a7aec4')};
`;
