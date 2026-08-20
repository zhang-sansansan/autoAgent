package cn.ann.ai.domain.agent.service.armory.node;

import cn.ann.ai.domain.agent.model.entity.ArmoryCommandEntity;
import cn.ann.ai.domain.agent.model.valobj.AiAgentEnumVO;
import cn.ann.ai.domain.agent.model.valobj.AiClientToolMcpVO;
import cn.ann.ai.domain.agent.service.armory.node.factory.DefaultArmoryStrategyFactory;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.alibaba.fastjson.JSON;
import com.drew.lang.StringUtil;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * @author zhang san
 * @description
 * @create 2026/1/17 14:00
 */
@Slf4j
@Service
public class AiClientToolMcpNode extends AbstractArmorySupport {
    @Resource
    private AiClientModelNode aiClientModelNode;
    @Override
    protected String doApply(ArmoryCommandEntity requestParameter, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("Ai Agent 构建节点，Tool MCP 工具配置{}", JSON.toJSONString(requestParameter));

        //先从动态背包中获取被查询到的数据
        List<AiClientToolMcpVO> aiClientToolMcpList = dynamicContext.getValue(dataName());
        if (aiClientToolMcpList == null || aiClientToolMcpList.isEmpty()) {
            log.warn("没有需要被初始化的 ai client tool mcp");
            return router(requestParameter, dynamicContext);
        }

        for (AiClientToolMcpVO mcpVO : aiClientToolMcpList) {
            // 创建 MCP 服务
            McpSyncClient mcpSyncClient = createMcpSyncClient(mcpVO);

            // 注册 MCP 对象
            registerBean(beanName(mcpVO.getMcpId()), McpSyncClient.class, mcpSyncClient);
        }
        return router(requestParameter, dynamicContext);
    }

    @Override
    protected String beanName(String beanId) {
        return AiAgentEnumVO.AI_CLIENT_TOOL_MCP.getBeanName(beanId);
    }

    @Override
    protected String dataName() {
        return AiAgentEnumVO.AI_CLIENT_TOOL_MCP.getDataName();
    }
    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> get(ArmoryCommandEntity requestParameter, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return aiClientModelNode;
    }

    private McpSyncClient createMcpSyncClient(AiClientToolMcpVO aiClientToolMcpVO) {
        //实例化mcp客户端
        //先判断类型
        String transportType = aiClientToolMcpVO.getTransportType();

        switch (transportType) {
            case "sse" -> {
                AiClientToolMcpVO.TransportConfigSse transportConfigSse = aiClientToolMcpVO.getTransportConfigSse();//获取配置，这个数据存在数据库中
                //配置中包括uri和endpoint
                String originalBaseUri = transportConfigSse.getBaseUri();
                String baseUri;
                String sseEndpoint;

                int index = originalBaseUri.indexOf("sse");
                if (index != -1) {
                    baseUri = originalBaseUri.substring(0, index-1);
                    sseEndpoint = originalBaseUri.substring(index-1);
                }
                else {
                    baseUri = originalBaseUri;
                    sseEndpoint = transportConfigSse.getSseEndpoint();
                }

                sseEndpoint = StringUtils.isBlank(sseEndpoint) ? "/sse" : sseEndpoint;

                HttpClientSseClientTransport sseClientTransport = HttpClientSseClientTransport.builder(baseUri)
                        .sseEndpoint(sseEndpoint)
                        .build();

                McpSyncClient mcpSyncClient = McpClient.sync(sseClientTransport).requestTimeout(Duration.ofMinutes(aiClientToolMcpVO.getRequestTimeout())).build();
                var init_sse = mcpSyncClient.initialize();//客户端初始化，建立连接
//                建立连接：Client 向 baseUri + sseEndpoint 发起 GET 请求，建立 SSE 长连接。
//
//                发送问候：Client 发送 initialize JSON-RPC 消息。
//
//                获取能力：Server 返回它支持的工具列表（Tools）、资源（Resources）等。
//
//                返回结果：变量 init_sse 里就拿着对方递过来的“能力清单”

                log.info("Tool SSE MCP Initialized {}", init_sse);
                return mcpSyncClient;
            }
            case "stdio" -> {
                AiClientToolMcpVO.TransportConfigStdio transportConfigStdio = aiClientToolMcpVO.getTransportConfigStdio();
                Map<String, AiClientToolMcpVO.TransportConfigStdio.Stdio> stdioMap = transportConfigStdio.getStdio();
                AiClientToolMcpVO.TransportConfigStdio.Stdio stdio = stdioMap.get(aiClientToolMcpVO.getMcpName());

                ServerParameters stdioParameters = ServerParameters.builder(stdio.getCommand())
                        .args(stdio.getArgs())
                        .env(stdio.getEnv())
                        .build();

                McpSyncClient mcpSyncClient = McpClient.sync(new StdioClientTransport(stdioParameters))
                        .requestTimeout(Duration.ofSeconds(aiClientToolMcpVO.getRequestTimeout())).build();//创建mcp客户端

                var init_stdio = mcpSyncClient.initialize();
                log.info("Tool Stdio MCP Initialized {}", init_stdio);
                return mcpSyncClient;
            }
        }
        throw new RuntimeException("err! transportType " + transportType + " not exist!");
    }
}
