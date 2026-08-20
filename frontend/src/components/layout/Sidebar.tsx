import React, { useEffect } from 'react';
import { Tooltip } from '@douyinfe/semi-ui';
import { IconApps, IconBranch, IconFolder, IconHome, IconSetting } from '@douyinfe/semi-icons';
import { useLocation, useNavigate } from 'react-router-dom';
import styled from 'styled-components';

import { NAV_GROUPS, PageEntry } from '../../config/navigation';
import { theme } from '../../styles/theme';

interface SidebarProps { selectedKey?: string; onSelect?: (key: string) => void; collapsed?: boolean; }
const icons = { workspace: <IconHome />, agent: <IconBranch />, resources: <IconFolder />, system: <IconSetting /> };
const Shell = styled.aside<{ $collapsed: boolean }>`position: fixed; inset: 0 auto 0 0; z-index: 30; width: ${({$collapsed}) => $collapsed ? '76px' : '248px'}; display: flex; flex-direction: column; background: rgba(9, 12, 20, .96); border-right: 1px solid ${theme.colors.border.primary}; transition: width 200ms ${theme.animation.easing.cubic}; backdrop-filter: blur(18px);`;
const Brand = styled.div<{ $collapsed: boolean }>`height: 72px; padding: 0 18px; display: flex; align-items: center; gap: 12px; border-bottom: 1px solid ${theme.colors.border.secondary}; overflow: hidden; strong { white-space: nowrap; font-size: 15px; letter-spacing: -.2px; } span { display: ${({$collapsed}) => $collapsed ? 'none' : 'block'}; color: ${theme.colors.text.tertiary}; font-size: 11px; }`;
const Mark = styled.div`width: 38px; height: 38px; flex: 0 0 38px; display: grid; place-items: center; color: white; border-radius: 12px; background: ${theme.colors.gradient.primary}; box-shadow: 0 0 24px rgba(139,92,246,.28);`;
const Scroll = styled.nav`flex: 1; overflow: auto; padding: 14px 10px;`;
const Label = styled.div<{ $collapsed: boolean }>`height: 28px; padding: 8px 10px 4px; color: ${theme.colors.text.tertiary}; font-size: 10px; font-weight: 700; letter-spacing: .12em; text-transform: uppercase; overflow: hidden; opacity: ${({$collapsed}) => $collapsed ? 0 : 1};`;
const Item = styled.button<{ $active: boolean; $collapsed: boolean }>`position: relative; width: 100%; min-height: 42px; margin: 2px 0; padding: 0 ${({$collapsed}) => $collapsed ? '0' : '12px'}; display: flex; align-items: center; justify-content: ${({$collapsed}) => $collapsed ? 'center' : 'flex-start'}; gap: 11px; color: ${({$active}) => $active ? theme.colors.text.primary : theme.colors.text.secondary}; background: ${({$active}) => $active ? 'rgba(139,92,246,.12)' : 'transparent'}; border: 1px solid ${({$active}) => $active ? 'rgba(139,92,246,.3)' : 'transparent'}; border-radius: 10px; cursor: pointer; text-align: left; transition: 140ms ease; &:hover { color: ${theme.colors.text.primary}; background: rgba(255,255,255,.045); } &::before { content: ''; position: absolute; left: -10px; width: 2px; height: ${({$active}) => $active ? '22px' : '0'}; background: ${theme.colors.primary}; box-shadow: 0 0 12px ${theme.colors.primary}; } span { display: ${({$collapsed}) => $collapsed ? 'none' : 'inline'}; white-space: nowrap; }`;

export const Sidebar: React.FC<SidebarProps> = ({ selectedKey, onSelect, collapsed = false }) => {
  const navigate = useNavigate();
  const location = useLocation();
  useEffect(() => { localStorage.setItem('ann-agent-studio:sidebar-collapsed', String(collapsed)); }, [collapsed]);
  const go = (page: PageEntry) => { navigate(page.path); onSelect?.(page.path.slice(1)); };
  return <Shell $collapsed={collapsed}>
    <Brand $collapsed={collapsed}><Mark><IconApps /></Mark><div><strong>ANN Agent Studio</strong><span>Agent operations workspace</span></div></Brand>
    <Scroll>{NAV_GROUPS.map((group) => <div key={group.id}>
      <Label $collapsed={collapsed}>{group.label}</Label>
      {group.pages.map((page) => {
        const active = location.pathname === page.path || selectedKey === page.path.slice(1);
        const node = <Item key={page.path} type="button" $active={active} $collapsed={collapsed} onClick={() => go(page)}>{icons[group.id]}<span>{page.name}</span></Item>;
        return collapsed ? <Tooltip key={page.path} content={page.name} position="right">{node}</Tooltip> : node;
      })}
    </div>)}</Scroll>
  </Shell>;
};
