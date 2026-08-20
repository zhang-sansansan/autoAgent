import React, { FormEvent, useState } from 'react';
import { Button, Input } from '@douyinfe/semi-ui';
import { IconEyeClosed, IconEyeOpened, IconLock, IconUser } from '@douyinfe/semi-icons';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';

import { AdminUserService } from '../services/admin-user-service';
import { theme } from '../styles/theme';
import { saveAuthenticatedUser } from '../utils/auth-storage';

const Screen = styled.main`min-height: 100vh; display: grid; place-items: center; padding: 32px; overflow: hidden; position: relative; &::before { content: ''; position: absolute; width: 620px; height: 620px; right: -180px; top: -260px; border-radius: 50%; background: rgba(139,92,246,.15); filter: blur(80px); } &::after { content: ''; position: absolute; width: 420px; height: 420px; left: -180px; bottom: -240px; border-radius: 50%; background: rgba(34,211,238,.09); filter: blur(70px); }`;
const Layout = styled.div`position: relative; z-index: 1; width: min(980px, 100%); display: grid; grid-template-columns: 1.1fr .9fr; gap: 80px; align-items: center;`;
const Intro = styled.section`h1 { margin: 0 0 18px; max-width: 560px; color: #fff; font-size: clamp(42px, 5vw, 68px); line-height: .98; letter-spacing: -.055em; } p { max-width: 500px; color: ${theme.colors.text.secondary}; font-size: 16px; line-height: 1.8; }`;
const Signal = styled.div`display: inline-flex; align-items: center; gap: 9px; margin-bottom: 28px; color: ${theme.colors.cyan}; font-size: 12px; font-weight: 700; letter-spacing: .16em; text-transform: uppercase; &::before { content: ''; width: 7px; height: 7px; border-radius: 50%; background: currentColor; box-shadow: 0 0 14px currentColor; }`;
const Card = styled.section`padding: 34px; background: rgba(18,22,36,.88); border: 1px solid ${theme.colors.border.primary}; border-radius: 22px; box-shadow: ${theme.shadows.modal}; backdrop-filter: blur(22px); h2 { margin: 0 0 8px; font-size: 24px; letter-spacing: -.03em; } > p { margin: 0 0 26px; color: ${theme.colors.text.secondary}; }`;
const Field = styled.label`display: grid; gap: 8px; margin-bottom: 17px; color: ${theme.colors.text.secondary}; font-size: 12px; font-weight: 600;`;
const Error = styled.div`margin: 4px 0 14px; color: ${theme.colors.error}; font-size: 13px;`;
const Divider = styled.div`display: flex; align-items: center; gap: 12px; margin: 18px 0; color: ${theme.colors.text.tertiary}; font-size: 11px; &::before, &::after { content: ''; flex: 1; height: 1px; background: ${theme.colors.border.secondary}; }`;

const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const submit = async (credentials: { username: string; password: string }) => {
    if (!credentials.username.trim() || !credentials.password) { setError('请输入账号和密码'); return; }
    setLoading(true); setError('');
    try {
      const valid = await AdminUserService.validateAdminUserLogin(credentials);
      if (!valid) { setError('账号或密码错误'); return; }
      saveAuthenticatedUser({ username: credentials.username });
      navigate('/dashboard', { replace: true });
    } catch {
      setError('网络连接失败，请稍后重试');
    } finally { setLoading(false); }
  };

  const onSubmit = (event: FormEvent) => { event.preventDefault(); void submit({ username, password }); };
  return <Screen><Layout><Intro><Signal>Obsidian Signal</Signal><h1>Build agents.<br/>Operate clearly.</h1><p>ANN Agent Studio 将流程编排、模型资源、工具装配与流式调试集中在一个克制、清晰的工作台中。</p></Intro><Card><h2>登录工作台</h2><p>使用平台管理员账号继续</p><form onSubmit={onSubmit}>
    <Field htmlFor="username">账号<Input id="username" prefix={<IconUser />} value={username} onChange={setUsername} placeholder="请输入账号" size="large" /></Field>
    <Field htmlFor="password">密码<Input id="password" prefix={<IconLock />} suffix={<Button aria-label={showPassword ? '隐藏密码' : '显示密码'} theme="borderless" icon={showPassword ? <IconEyeOpened /> : <IconEyeClosed />} onClick={() => setShowPassword((value) => !value)} />} type={showPassword ? 'text' : 'password'} value={password} onChange={setPassword} placeholder="请输入密码" size="large" /></Field>
    {error && <Error role="alert">{error}</Error>}
    <Button htmlType="submit" type="primary" block size="large" loading={loading}>登录</Button>
  </form><Divider>快捷入口</Divider><Button block size="large" disabled={loading} onClick={() => void submit({ username: 'admin', password: '123456' })}>使用管理员账号登录</Button></Card></Layout></Screen>;
};

export default LoginPage;
