import React from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { LoginPage, DashboardPage, AgentConfigPage, AgentListPage, ChatPage, ClientManagement, AiClientApiManagement, AdvisorManagement, RagOrderManagement, ClientModelManagement, ClientSystemPromptManagement, ClientToolMcpManagement, UserManagement } from './pages';
import { AppShell } from './components/layout';
import { isAuthenticated } from './utils/auth-storage';
import './styles/index.css';

// 路由保护组件
const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  return isAuthenticated() ? <>{children}</> : <Navigate to="/login" replace />;
};

// 登录重定向组件
const LoginRedirect: React.FC = () => {
  return isAuthenticated() ? <Navigate to="/dashboard" replace /> : <LoginPage />;
};

const App: React.FC = () => {
  return (
    <Router future={{ v7_startTransition: true, v7_relativeSplatPath: true }}>
      <Routes>
        <Route path="/login" element={<LoginRedirect />} />
        <Route 
          path="/dashboard" 
          element={
            <ProtectedRoute>
              <AppShell><DashboardPage /></AppShell>
            </ProtectedRoute>
          } 
        />
        <Route 
          path="/agent-config" 
          element={
            <ProtectedRoute>
              <AppShell><AgentConfigPage /></AppShell>
            </ProtectedRoute>
          } 
        />
        <Route
          path="/agent-list"
          element={
            <ProtectedRoute>
              <AppShell><AgentListPage /></AppShell>
            </ProtectedRoute>
          }
        />
        <Route
          path="/chat"
          element={
            <ProtectedRoute>
              <AppShell><ChatPage /></AppShell>
            </ProtectedRoute>
          }
        />
        <Route
          path="/client-management"
          element={
            <ProtectedRoute>
              <ClientManagement />
            </ProtectedRoute>
          } 
        />
        <Route 
          path="/ai-client-api-management" 
          element={
            <ProtectedRoute>
              <AiClientApiManagement />
            </ProtectedRoute>
          } 
        />
        <Route 
          path="/advisor-management" 
          element={
            <ProtectedRoute>
              <AdvisorManagement />
            </ProtectedRoute>
          } 
        />
        <Route 
          path="/rag-order-management" 
          element={
            <ProtectedRoute>
              <RagOrderManagement />
            </ProtectedRoute>
          } 
        />
        <Route 
          path="/client-model-management" 
          element={
            <ProtectedRoute>
              <ClientModelManagement />
            </ProtectedRoute>
          } 
        />
        <Route 
          path="/client-system-prompt-management" 
          element={
            <ProtectedRoute>
              <ClientSystemPromptManagement />
            </ProtectedRoute>
          } 
        />
        <Route
          path="/client-tool-mcp-management"
          element={
            <ProtectedRoute>
              <ClientToolMcpManagement />
            </ProtectedRoute>
          }
        />
        <Route
          path="/user-management"
          element={
            <ProtectedRoute>
              <UserManagement />
            </ProtectedRoute>
          }
        />
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </Router>
  );
};

document.body.setAttribute('theme-mode', 'dark');

const app = createRoot(document.getElementById('root')!);

app.render(<App />);
