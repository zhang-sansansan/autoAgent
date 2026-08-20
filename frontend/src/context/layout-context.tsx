import React, { createContext, useCallback, useContext, useMemo, useState } from 'react';

const SIDEBAR_KEY = 'ann-agent-studio:sidebar-collapsed';

interface LayoutState {
  collapsed: boolean;
  focusMode: boolean;
  toggleSidebar: () => void;
  enterFocusMode: () => void;
  exitFocusMode: () => void;
}

const LayoutContext = createContext<LayoutState | null>(null);

export const LayoutProvider: React.FC<React.PropsWithChildren> = ({ children }) => {
  const [collapsed, setCollapsed] = useState(() => localStorage.getItem(SIDEBAR_KEY) === 'true');
  const [focusMode, setFocusMode] = useState(false);
  const toggleSidebar = useCallback(() => setCollapsed((current) => {
    const next = !current;
    localStorage.setItem(SIDEBAR_KEY, String(next));
    return next;
  }), []);
  const enterFocusMode = useCallback(() => setFocusMode(true), []);
  const exitFocusMode = useCallback(() => setFocusMode(false), []);

  const value = useMemo<LayoutState>(() => ({
    collapsed,
    focusMode,
    toggleSidebar,
    enterFocusMode,
    exitFocusMode,
  }), [collapsed, enterFocusMode, exitFocusMode, focusMode, toggleSidebar]);

  return <LayoutContext.Provider value={value}>{children}</LayoutContext.Provider>;
};

export const useLayout = (): LayoutState => {
  const value = useContext(LayoutContext);
  if (!value) throw new Error('useLayout must be used inside LayoutProvider');
  return value;
};
