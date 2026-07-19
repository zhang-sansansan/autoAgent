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
        log.info("Ai Agent 鏋勫缓鑺傜偣锛孴ool MCP 宸ュ叿閰嶇疆{}", JSON.toJSONString(requestParameter));

        //鍏堜粠鍔ㄦ€佽儗鍖呬腑鑾峰彇琚煡璇㈠埌鐨勬暟鎹?
        List<AiClientToolMcpVO> aiClientToolMcpList = dynamicContext.getValue(dataName());
        if (aiClientToolMcpList == null || aiClientToolMcpList.isEmpty()) {
            log.warn("娌℃湁闇€瑕佽鍒濆鍖栫殑 ai client tool mcp");
            return router(requestParameter, dynamicContext);
        }

        for (AiClientToolMcpVO mcpVO : aiClientToolMcpList) {
            // 鍒涘缓 MCP 鏈嶅姟
            McpSyncClient mcpSyncClient = createMcpSyncClient(mcpVO);

            // 娉ㄥ唽 MCP 瀵硅薄
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
        //瀹炰緥鍖杕cp瀹㈡埛绔?
        //鍏堝垽鏂被鍨?
        String transportType = aiClientToolMcpVO.getTransportType();

        switch (transportType) {
            case "sse" -> {
                AiClientToolMcpVO.TransportConfigSse transportConfigSse = aiClientToolMcpVO.getTransportConfigSse();//鑾峰彇閰嶇疆锛岃繖涓暟鎹瓨鍦ㄦ暟鎹簱涓?
                //閰嶇疆涓寘鎷瑄ri鍜宔ndpoint
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
                var init_sse = mcpSyncClient.initialize();//瀹㈡埛绔垵濮嬪寲锛屽缓绔嬭繛鎺?
//                寤虹珛杩炴帴锛欳lient 鍚?baseUri + sseEndpoint 鍙戣捣 GET 璇锋眰锛屽缓绔?SSE 闀胯繛鎺ャ€?
//
//                鍙戦€侀棶鍊欙細Client 鍙戦€?initialize JSON-RPC 娑堟伅銆?
//
//                鑾峰彇鑳藉姏锛歋erver 杩斿洖瀹冩敮鎸佺殑宸ュ叿鍒楄〃锛圱ools锛夈€佽祫婧愶紙Resources锛夌瓑銆?
//
//                杩斿洖缁撴灉锛氬彉閲?init_sse 閲屽氨鎷跨潃瀵规柟閫掕繃鏉ョ殑鈥滆兘鍔涙竻鍗曗€?

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
                        .requestTimeout(Duration.ofSeconds(aiClientToolMcpVO.getRequestTimeout())).build();//鍒涘缓mcp瀹㈡埛绔?

                var init_stdio = mcpSyncClient.initialize();
                log.info("Tool Stdio MCP Initialized {}", init_stdio);
                return mcpSyncClient;
            }
        }
        throw new RuntimeException("err! transportType " + transportType + " not exist!");
    }
}

