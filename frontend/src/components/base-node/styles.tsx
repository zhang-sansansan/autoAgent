import styled from 'styled-components';
import { IconInfoCircle } from '@douyinfe/semi-icons';

export const NodeWrapperStyle = styled.div`
  align-items: flex-start;
  color: #f7f7ff;
  background: linear-gradient(145deg, rgba(24, 29, 47, .98), rgba(14, 17, 29, .99));
  border: 1px solid rgba(151, 160, 198, .2);
  border-radius: 12px;
  box-shadow: 0 14px 38px rgba(0, 0, 0, .32), 0 0 0 1px rgba(139, 92, 246, .04);
  display: flex;
  flex-direction: column;
  justify-content: center;
  position: relative;
  min-width: 360px;
  width: 100%;
  height: auto;

  &.selected {
    border-color: #8b5cf6;
    box-shadow: 0 18px 48px rgba(0, 0, 0, .38), 0 0 0 2px rgba(139, 92, 246, .17);
  }
`;

export const ErrorIcon = () => (
  <IconInfoCircle
    style={{
      position: 'absolute',
      color: 'red',
      left: -6,
      top: -6,
      zIndex: 1,
      background: '#171b2b',
      borderRadius: 8,
    }}
  />
);
