import React, { useEffect, useState } from 'react';
import { Banner } from '@douyinfe/semi-ui';
import { Outlet, useNavigate } from 'react-router-dom';
import styled from 'styled-components';

import { LayoutProvider, useLayout } from '../../context/layout-context';
import { clearAuth } from '../../utils/auth-storage';
import { Header } from './Header';
import { Sidebar } from './Sidebar';

const Main = styled.main<{ $collapsed: boolean; $focus: boolean }>`min-height: 100vh; margin-left: ${({$collapsed,$focus}) => $focus ? '0' : $collapsed ? '76px' : '248px'}; transition: margin-left 200ms cubic-bezier(.22,1,.36,1);`;
const Page = styled.div`min-height: calc(100vh - 64px); padding: 24px;`;

const ShellContent: React.FC<React.PropsWithChildren> = ({ children }) => {
  const { collapsed, focusMode, toggleSidebar } = useLayout();
  const navigate = useNavigate();
  const [narrow, setNarrow] = useState(() => window.innerWidth < 1280);
  useEffect(() => { const update = () => setNarrow(window.innerWidth < 1280); window.addEventListener('resize', update); return () => window.removeEventListener('resize', update); }, []);
  const logout = () => { clearAuth(); navigate('/login', { replace: true }); };
  return <>{!focusMode && <Sidebar collapsed={collapsed} />}<Main $collapsed={collapsed} $focus={focusMode}>{!focusMode && <Header onToggleSidebar={toggleSidebar} onLogout={logout} />}{narrow && <Banner type="warning" description="当前窗口低于 1280px，建议扩大窗口以获得更完整的工作区；你仍可继续操作。" closeIcon={null} />}<Page className="ann-page-enter">{children || <Outlet />}</Page></Main></>;
};

export const AppShell: React.FC<React.PropsWithChildren> = ({ children }) => <LayoutProvider><ShellContent>{children}</ShellContent></LayoutProvider>;
