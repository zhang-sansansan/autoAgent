import React, { useState } from 'react';
import { Avatar, Button, Dropdown } from '@douyinfe/semi-ui';
import { IconExit, IconMenu, IconSearch } from '@douyinfe/semi-icons';
import { useLocation } from 'react-router-dom';
import styled from 'styled-components';

import { getPageByPath } from '../../config/navigation';
import { getCurrentUser } from '../../utils/auth-storage';
import { theme } from '../../styles/theme';
import { CommandCenter } from './CommandCenter';

interface HeaderProps { onToggleSidebar?: () => void; onLogout?: () => void; collapsed?: boolean; }
const Bar = styled.header`height: 64px; padding: 0 24px; display: flex; align-items: center; justify-content: space-between; background: rgba(7,9,16,.78); border-bottom: 1px solid ${theme.colors.border.secondary}; backdrop-filter: blur(16px);`;
const Side = styled.div`display: flex; align-items: center; gap: 12px;`;
const Title = styled.div`font-size: 15px; font-weight: 650; color: ${theme.colors.text.primary};`;
const Search = styled(Button)`min-width: 190px; justify-content: flex-start !important; color: ${theme.colors.text.secondary} !important; background: ${theme.colors.bg.tertiary} !important; border: 1px solid ${theme.colors.border.primary} !important;`;

export const Header: React.FC<HeaderProps> = ({ onToggleSidebar, onLogout }) => {
  const [searchOpen, setSearchOpen] = useState(false);
  const location = useLocation();
  const user = getCurrentUser();
  const page = getPageByPath(location.pathname);
  const menu = [{ node: 'item' as const, name: '退出登录', icon: <IconExit />, onClick: onLogout }];
  return <Bar><Side><Button theme="borderless" icon={<IconMenu />} onClick={onToggleSidebar} aria-label="切换侧栏"/><Title>{page?.name || 'ANN Agent Studio'}</Title></Side><Side>
    <Search icon={<IconSearch />} onClick={() => setSearchOpen(true)}>搜索页面或功能</Search>
    <Dropdown trigger="click" position="bottomRight" menu={menu}><div style={{display:'flex',alignItems:'center',gap:8,cursor:'pointer'}}><span>{user?.username || '用户'}</span><Avatar size="small" color="purple">{user?.username?.[0]?.toUpperCase() || 'U'}</Avatar></div></Dropdown>
    <CommandCenter open={searchOpen} onClose={() => setSearchOpen(false)} />
  </Side></Bar>;
};
