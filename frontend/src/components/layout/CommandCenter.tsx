import React, { useMemo, useState } from 'react';
import { IconSearch } from '@douyinfe/semi-icons';
import { Input, Modal, Typography } from '@douyinfe/semi-ui';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';

import { searchPages } from '../../config/navigation';
import { theme } from '../../styles/theme';

const Results = styled.div`display: grid; gap: 8px; margin-top: 16px; max-height: 420px; overflow: auto;`;
const Result = styled.button`
  width: 100%; padding: 13px 14px; display: flex; align-items: center; justify-content: space-between;
  color: ${theme.colors.text.primary}; background: ${theme.colors.bg.tertiary};
  border: 1px solid ${theme.colors.border.secondary}; border-radius: ${theme.borderRadius.base}; cursor: pointer;
  &:hover { border-color: ${theme.colors.border.tertiary}; background: ${theme.colors.bg.elevated}; transform: translateY(-1px); }
`;
const Empty = styled.div`padding: 42px 16px; color: ${theme.colors.text.secondary}; text-align: center;`;

interface CommandCenterProps { open: boolean; onClose: () => void; }

export const CommandCenter: React.FC<CommandCenterProps> = ({ open, onClose }) => {
  const navigate = useNavigate();
  const [query, setQuery] = useState('');
  const pages = useMemo(() => searchPages(query), [query]);

  const close = () => { setQuery(''); onClose(); };

  return (
    <Modal title="页面导航" visible={open} onCancel={close} footer={null} width={620} centered>
      <Input
        autoFocus
        prefix={<IconSearch />}
        placeholder="搜索页面或功能"
        value={query}
        onChange={setQuery}
        size="large"
      />
      <Results>
        {pages.length ? pages.map((page) => (
          <Result key={page.path} type="button" onClick={() => { navigate(page.path); close(); }}>
            <span>{page.name}</span>
            <Typography.Text type="tertiary">{page.groupLabel}</Typography.Text>
          </Result>
        )) : <Empty>没有匹配的页面</Empty>}
      </Results>
    </Modal>
  );
};
