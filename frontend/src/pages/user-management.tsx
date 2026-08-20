import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Layout,
  Table,
  Button,
  Input,
  Space,
  Typography,
  Toast,
  Tag,
  Popconfirm,
  Card,
  Modal,
  Form,
} from '@douyinfe/semi-ui';
import { IconSearch, IconPlus, IconEdit, IconDelete, IconRefresh } from '@douyinfe/semi-icons';
import styled from 'styled-components';
import { theme } from '../styles/theme';
import { Sidebar, Header } from '../components/layout';
import { clearAuth } from '../utils/auth-storage';
import {
  AdminUserService,
  AdminUserResponseDTO,
  AdminUserRequestDTO,
} from '../services/admin-user-service';

const { Content } = Layout;
const { Title } = Typography;

const UserManagementLayout = styled(Layout)`
  min-height: 100vh;
  background: ${theme.colors.bg.secondary};
`;

const MainContent = styled.div<{ $collapsed: boolean }>`
  display: flex;
  flex: 1;
  margin-left: ${(props) => (props.$collapsed ? '76px' : '248px')};
  transition: margin-left ${theme.animation.duration.normal} ${theme.animation.easing.cubic};
`;

const ContentArea = styled(Content)`
  flex: 1;
  padding: ${theme.spacing.lg};
  background: ${theme.colors.bg.secondary};
  overflow-y: auto;
`;

const SearchSection = styled(Card)`
  margin: ${theme.spacing.lg};

  .semi-card-body {
    padding: ${theme.spacing.lg};
  }
`;

const SearchRow = styled.div`
  display: flex;
  align-items: center;
  gap: ${theme.spacing.base};
  flex-wrap: wrap;
`;

const TableContainer = styled.div`
  margin: 0 ${theme.spacing.lg} ${theme.spacing.lg};
`;

interface UserFormValues {
  username: string;
  password?: string;
  status: boolean;
}

export const UserManagement: React.FC = () => {
  const navigate = useNavigate();
  const [collapsed, setCollapsed] = useState(() => localStorage.getItem('ann-agent-studio:sidebar-collapsed') === 'true');
  const [loading, setLoading] = useState(false);
  const [dataSource, setDataSource] = useState<AdminUserResponseDTO[]>([]);
  const [searchText, setSearchText] = useState('');
  const [modalVisible, setModalVisible] = useState(false);
  const [editRecord, setEditRecord] = useState<AdminUserResponseDTO | null>(null);
  const [saving, setSaving] = useState(false);

  const currentUser = JSON.parse(localStorage.getItem('userInfo') || '{}');

  const handleLogout = () => {
    clearAuth();
    navigate('/login');
  };

  const handleNavigation = (path: string) => {
    if (path === 'dashboard') navigate('/dashboard');
    else if (path === 'agent-list') navigate('/agent-list');
    else if (path === 'agent-config') navigate('/agent-config');
    else if (path === 'chat') navigate('/chat');
    else if (path === 'client-management') navigate('/client-management');
    else if (path === 'ai-client-api-management') navigate('/ai-client-api-management');
    else if (path === 'advisor-management') navigate('/advisor-management');
    else if (path === 'rag-order-management') navigate('/rag-order-management');
    else if (path === 'client-model-management') navigate('/client-model-management');
    else if (path === 'client-system-prompt-management') navigate('/client-system-prompt-management');
    else if (path === 'client-tool-mcp-management') navigate('/client-tool-mcp-management');
    else navigate(path);
  };

  const fetchUserList = async () => {
    setLoading(true);
    try {
      const result = await AdminUserService.queryUserList({
        username: searchText || undefined,
        pageNum: 1,
        pageSize: 100,
      });
      setDataSource(result);
    } catch (error) {
      console.error('获取用户列表失败:', error);
      Toast.error('获取用户列表失败，请检查网络连接');
      setDataSource([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUserList();
  }, []);

  const openCreate = () => {
    setEditRecord(null);
    setModalVisible(true);
  };

  const openEdit = (record: AdminUserResponseDTO) => {
    setEditRecord(record);
    setModalVisible(true);
  };

  const handleSubmit = async (values: UserFormValues) => {
    setSaving(true);
    try {
      const payload: AdminUserRequestDTO = {
        id: editRecord?.id,
        userId: editRecord?.userId,
        username: values.username,
        password: values.password || undefined,
        status: values.status ? 1 : 0,
      };
      if (editRecord) {
        await AdminUserService.updateUser(payload);
        Toast.success('更新成功');
      } else {
        if (!values.password) {
          Toast.error('新增用户必须填写密码');
          return;
        }
        await AdminUserService.createUser(payload);
        Toast.success('新增成功');
      }
      setModalVisible(false);
      fetchUserList();
    } catch (error) {
      console.error('保存用户失败:', error);
      Toast.error('保存失败，请检查网络连接');
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (record: AdminUserResponseDTO) => {
    // 删除当前登录账号时阻止
    if (record.username === currentUser.username) {
      Toast.error('不能删除当前登录账号');
      return;
    }
    try {
      await AdminUserService.deleteUserById(record.id);
      Toast.success('删除成功');
      fetchUserList();
    } catch (error) {
      console.error('删除用户失败:', error);
      Toast.error('删除失败，请检查网络连接');
    }
  };

  const columns = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
      width: 80,
    },
    {
      title: '用户ID',
      dataIndex: 'userId',
      key: 'userId',
      width: 150,
    },
    {
      title: '用户名',
      dataIndex: 'username',
      key: 'username',
      width: 180,
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      width: 100,
      render: (status: number) => (
        <Tag color={status === 1 ? 'green' : 'red'}>{status === 1 ? '启用' : '禁用'}</Tag>
      ),
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      key: 'createTime',
      width: 180,
      render: (time: string) => (time ? new Date(time).toLocaleString() : '-'),
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      key: 'updateTime',
      width: 180,
      render: (time: string) => (time ? new Date(time).toLocaleString() : '-'),
    },
    {
      title: '操作',
      key: 'action',
      width: 150,
      fixed: 'right' as const,
      render: (_: any, record: AdminUserResponseDTO) => (
        <Space>
          <Button
            theme="borderless"
            type="primary"
            icon={<IconEdit />}
            size="small"
            onClick={() => openEdit(record)}
          >
            编辑
          </Button>
          <Popconfirm
            title="确定要删除这个用户吗？"
            content="删除后无法恢复，请谨慎操作"
            onConfirm={() => handleDelete(record)}
            okText="确定"
            cancelText="取消"
          >
            <Button theme="borderless" type="danger" icon={<IconDelete />} size="small">
              删除
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <UserManagementLayout>
      <Sidebar
        selectedKey="user-management"
        onSelect={handleNavigation}
        collapsed={collapsed}
      />
      <MainContent $collapsed={collapsed}>
        <ContentArea>
          <Header
            onToggleSidebar={() => setCollapsed(!collapsed)}
            onLogout={handleLogout}
            collapsed={collapsed}
          />

          <Card style={{ marginBottom: theme.spacing.lg }}>
            <Title heading={4} style={{ margin: 0 }}>
              用户管理
            </Title>
          </Card>

          <SearchSection>
            <SearchRow>
              <Input
                placeholder="请输入用户名"
                value={searchText}
                onChange={setSearchText}
                style={{ width: 200 }}
                onEnterPress={() => {
                  fetchUserList();
                }}
              />
              <Button type="primary" icon={<IconSearch />} onClick={fetchUserList}>
                搜索
              </Button>
              <Button
                icon={<IconRefresh />}
                onClick={() => {
                  setSearchText('');
                  setTimeout(fetchUserList, 0);
                }}
              >
                重置
              </Button>
              <Button type="primary" theme="solid" icon={<IconPlus />} onClick={openCreate}>
                新增用户
              </Button>
            </SearchRow>
          </SearchSection>

          <TableContainer>
            <Card>
              <Table
                columns={columns}
                dataSource={dataSource}
                loading={loading}
                rowKey="id"
                scroll={{ x: 1000 }}
                empty={
                  <div style={{ padding: '40px', textAlign: 'center' }}>
                    <Typography.Text type="tertiary">暂无数据</Typography.Text>
                  </div>
                }
              />
            </Card>
          </TableContainer>

          <Modal
            title={editRecord ? '编辑用户' : '新增用户'}
            visible={modalVisible}
            onCancel={() => setModalVisible(false)}
            footer={null}
            width={420}
          >
            <Form
              initValues={{
                username: editRecord?.username || '',
                status: editRecord ? editRecord.status === 1 : true,
              }}
              onSubmit={handleSubmit}
            >
              <Form.Input
                field="username"
                label="用户名"
                placeholder="请输入用户名"
                rules={[{ required: true, message: '请输入用户名' }]}
              />
              <Form.Input
                field="password"
                label={editRecord ? '密码（留空不修改）' : '密码'}
                type="password"
                placeholder={editRecord ? '留空表示不修改密码' : '请输入密码'}
                rules={editRecord ? [] : [{ required: true, message: '请输入密码' }]}
              />
              <Form.Switch field="status" label="状态" />
              <div style={{ display: 'flex', justifyContent: 'flex-end', gap: 8, marginTop: 24 }}>
                <Button onClick={() => setModalVisible(false)}>取消</Button>
                <Button type="primary" htmlType="submit" loading={saving}>
                  保存
                </Button>
              </div>
            </Form>
          </Modal>
        </ContentArea>
      </MainContent>
    </UserManagementLayout>
  );
};
