package cn.ann.ai.trigger.http.admin.util;

import cn.ann.ai.infrastructure.dao.po.AiClientConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 鎷栨嫿閰嶇疆瑙ｆ瀽宸ュ叿绫?
 * 鐢ㄤ簬瑙ｆ瀽鍓嶇浼犳潵鐨凧SON閰嶇疆鏁版嵁锛岀敓鎴恆i_client_config琛ㄧ殑鍏崇郴鏄犲皠
 *
 * @author xiaofuge bugstack.cn @灏忓倕鍝?
 * 2025/1/20 10:00
 */
@Slf4j
public class DrawConfigParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 瑙ｆ瀽鎷栨嫿閰嶇疆JSON鏁版嵁锛岀敓鎴怉iClientConfig鍏崇郴鍒楄〃
     *
     * @param configData JSON閰嶇疆鏁版嵁
     * @return AiClientConfig鍏崇郴鍒楄〃
     */
    public static List<AiClientConfig> parseConfigData(String configData) {
        List<AiClientConfig> configList = new ArrayList<>();
        
        try {
            JsonNode rootNode = objectMapper.readTree(configData);
            JsonNode nodesArray = rootNode.get("nodes");
            JsonNode edgesArray = rootNode.get("edges");
            
            if (nodesArray == null || edgesArray == null) {
                log.warn("閰嶇疆鏁版嵁涓己灏憂odes鎴杄dges鑺傜偣");
                return configList;
            }
            
            log.info("寮€濮嬭В鏋愰厤缃暟鎹紝鍖呭惈{}涓妭鐐癸紝{}鏉¤竟", nodesArray.size(), edgesArray.size());
            
            // 鏋勫缓鑺傜偣鏄犲皠琛紝key涓簄odeId锛寁alue涓鸿妭鐐逛俊鎭?
            Map<String, NodeInfo> nodeMap = buildNodeMap(nodesArray);
            
            // 瑙ｆ瀽杈瑰叧绯伙紝鐢熸垚閰嶇疆鍏崇郴
            parseEdges(edgesArray, nodeMap, configList);
            
            // 楠岃瘉鍜岀粺璁¤В鏋愮粨鏋?
            validateAndLogResults(nodeMap, configList, edgesArray.size());
            
            log.info("瑙ｆ瀽閰嶇疆鏁版嵁瀹屾垚锛岀敓鎴恵}鏉″叧绯?, configList.size());
            
        } catch (Exception e) {
            log.error("瑙ｆ瀽閰嶇疆鏁版嵁澶辫触", e);
        }
        
        return configList;
    }

    /**
     * 鏋勫缓鑺傜偣鏄犲皠琛?
     */
    private static Map<String, NodeInfo> buildNodeMap(JsonNode nodesArray) {
        Map<String, NodeInfo> nodeMap = new HashMap<>();
        
        log.info("寮€濮嬫瀯寤鸿妭鐐规槧灏勮〃锛屾€诲叡{}涓妭鐐?, nodesArray.size());
        
        for (JsonNode nodeJson : nodesArray) {
            String nodeId = nodeJson.get("id").asText();
            String nodeType = nodeJson.get("type").asText();
            
            NodeInfo nodeInfo = new NodeInfo();
            nodeInfo.setNodeId(nodeId);
            nodeInfo.setNodeType(nodeType);
            
            // 瑙ｆ瀽鑺傜偣鏁版嵁锛屾彁鍙栧紩鐢↖D
            JsonNode dataNode = nodeJson.get("data");
            if (dataNode != null) {
                nodeInfo.setTitle(dataNode.has("title") ? dataNode.get("title").asText() : "");
                
                // 瑙ｆ瀽inputsValues锛屾彁鍙栧叿浣撶殑閰嶇疆鍊?
                JsonNode inputsValues = dataNode.get("inputsValues");
                if (inputsValues != null) {
                    extractRefId(inputsValues, nodeInfo);
                }
            }
            
            log.debug("鏋勫缓鑺傜偣淇℃伅: {}", nodeInfo);
            nodeMap.put(nodeId, nodeInfo);
        }
        
        log.info("鑺傜偣鏄犲皠琛ㄦ瀯寤哄畬鎴愶紝鍏眥}涓妭鐐?, nodeMap.size());
        return nodeMap;
    }

    /**
     * 浠巌nputsValues涓彁鍙栧紩鐢↖D
     */
    private static void extractRefId(JsonNode inputsValues, NodeInfo nodeInfo) {
        // 鏍规嵁涓嶅悓鑺傜偣绫诲瀷鎻愬彇涓嶅悓鐨勫紩鐢↖D
        switch (nodeInfo.getNodeType()) {
            case "client":
                extractClientRefId(inputsValues, nodeInfo);
                break;
            case "agent":
                extractAgentRefId(inputsValues, nodeInfo);
                break;
            case "tool_mcp":
                extractToolMcpRefId(inputsValues, nodeInfo);
                break;
            case "model":
                extractModelRefId(inputsValues, nodeInfo);
                break;
            case "prompt":
                extractPromptRefId(inputsValues, nodeInfo);
                break;
            case "advisor":
                extractAdvisorRefId(inputsValues, nodeInfo);
                break;
            default:
                // 鍏朵粬绫诲瀷鑺傜偣鏆備笉澶勭悊
                log.debug("鏈鐞嗙殑鑺傜偣绫诲瀷: {}", nodeInfo.getNodeType());
                break;
        }
    }

    /**
     * 鎻愬彇瀹㈡埛绔紩鐢↖D
     */
    private static void extractClientRefId(JsonNode inputsValues, NodeInfo nodeInfo) {
        // 浼樺厛浣跨敤clientId瀛楁锛堢洿鎺ュ瓧绗︿覆鍊硷級
        JsonNode clientId = inputsValues.get("clientId");
        if (clientId != null && clientId.isTextual()) {
            nodeInfo.setRefId(clientId.asText());
            return;
        }
        
        // 濡傛灉娌℃湁clientId锛屽垯浣跨敤clientName鏁扮粍
        JsonNode clientName = inputsValues.get("clientName");
        if (clientName != null && clientName.isArray() && !clientName.isEmpty()) {
            JsonNode firstItem = clientName.get(0);
            if (firstItem.has("value")) {
                nodeInfo.setRefId(firstItem.get("value").asText());
            }
        }
    }

    /**
     * 鎻愬彇鏅鸿兘浣撳紩鐢↖D
     */
    private static void extractAgentRefId(JsonNode inputsValues, NodeInfo nodeInfo) {
        JsonNode agentName = inputsValues.get("agentName");
        if (agentName != null) {
            if (agentName.isArray() && agentName.size() > 0) {
                // 鏁扮粍鏍煎紡锛歔{"key": "xxx", "value": "yyy"}]
                JsonNode firstItem = agentName.get(0);
                if (firstItem.has("value")) {
                    nodeInfo.setRefId(firstItem.get("value").asText());
                }
            } else if (agentName.isTextual()) {
                // 瀛楃涓叉牸寮忥細鐩存帴浣跨敤瀛楃涓插€间綔涓哄紩鐢↖D
                nodeInfo.setRefId(agentName.asText());
            }
        }
    }

    /**
     * 鎻愬彇宸ュ叿MCP寮曠敤ID
     */
    private static void extractToolMcpRefId(JsonNode inputsValues, NodeInfo nodeInfo) {
        JsonNode toolMcpName = inputsValues.get("toolMcpName");
        if (toolMcpName != null && toolMcpName.isArray() && toolMcpName.size() > 0) {
            JsonNode firstItem = toolMcpName.get(0);
            if (firstItem.has("value")) {
                nodeInfo.setRefId(firstItem.get("value").asText());
            }
        }
    }

    /**
     * 鎻愬彇妯″瀷寮曠敤ID
     */
    private static void extractModelRefId(JsonNode inputsValues, NodeInfo nodeInfo) {
        JsonNode modelName = inputsValues.get("modelName");
        if (modelName != null && modelName.isArray() && modelName.size() > 0) {
            JsonNode firstItem = modelName.get(0);
            if (firstItem.has("value")) {
                nodeInfo.setRefId(firstItem.get("value").asText());
            }
        }
    }

    /**
     * 鎻愬彇鎻愮ず璇嶅紩鐢↖D
     */
    private static void extractPromptRefId(JsonNode inputsValues, NodeInfo nodeInfo) {
        JsonNode promptName = inputsValues.get("promptName");
        if (promptName != null) {
            if (promptName.isArray() && promptName.size() > 0) {
                // 鏁扮粍鏍煎紡锛歔{"key": "xxx", "value": "yyy"}]
                JsonNode firstItem = promptName.get(0);
                if (firstItem.has("value")) {
                    nodeInfo.setRefId(firstItem.get("value").asText());
                }
            } else if (promptName.isTextual()) {
                // 瀛楃涓叉牸寮忥細鐩存帴浣跨敤瀛楃涓插€间綔涓哄紩鐢↖D
                nodeInfo.setRefId(promptName.asText());
            }
        }
    }

    /**
     * 鎻愬彇椤鹃棶寮曠敤ID
     */
    private static void extractAdvisorRefId(JsonNode inputsValues, NodeInfo nodeInfo) {
        JsonNode advisorName = inputsValues.get("advisorName");
        if (advisorName != null && advisorName.isArray() && advisorName.size() > 0) {
            JsonNode firstItem = advisorName.get(0);
            if (firstItem.has("value")) {
                nodeInfo.setRefId(firstItem.get("value").asText());
            }
        }
    }

    /**
     * 瑙ｆ瀽杈瑰叧绯伙紝鐢熸垚閰嶇疆鍏崇郴
     */
    private static void parseEdges(JsonNode edgesArray, Map<String, NodeInfo> nodeMap, List<AiClientConfig> configList) {
        log.info("寮€濮嬭В鏋愯竟鍏崇郴锛屾€诲叡{}鏉¤竟", edgesArray.size());
        
        int processedEdges = 0;
        int skippedEdges = 0;
        int validConfigs = 0;
        
        for (JsonNode edgeJson : edgesArray) {
            processedEdges++;
            String sourceNodeId = edgeJson.get("sourceNodeID").asText();
            String targetNodeId = edgeJson.get("targetNodeID").asText();
            
            // 鑾峰彇sourcePortID锛岃繖鍖呭惈浜嗛噸瑕佺殑杩炴帴绔彛淇℃伅
            String sourcePortId = null;
            if (edgeJson.has("sourcePortID")) {
                sourcePortId = edgeJson.get("sourcePortID").asText();
            }
            
            log.debug("澶勭悊杈瑰叧绯籟{}/{}]: {} -> {}, sourcePortId: {}", 
                    processedEdges, edgesArray.size(), sourceNodeId, targetNodeId, sourcePortId);
            
            NodeInfo sourceNode = nodeMap.get(sourceNodeId);
            NodeInfo targetNode = nodeMap.get(targetNodeId);
            
            if (sourceNode == null || targetNode == null) {
                log.warn("鎵句笉鍒拌妭鐐逛俊鎭紝sourceNodeId: {}, targetNodeId: {}", sourceNodeId, targetNodeId);
                skippedEdges++;
                continue;
            }
            
            // 璺宠繃start鑺傜偣
            if ("start".equals(sourceNode.getNodeType()) || "start".equals(targetNode.getNodeType())) {
                log.debug("璺宠繃start鑺傜偣: {} -> {}", sourceNode.getNodeType(), targetNode.getNodeType());
                skippedEdges++;
                continue;
            }
            
            // 楠岃瘉鑺傜偣鏄惁鏈夋湁鏁堢殑寮曠敤ID
            if (sourceNode.getRefId() == null || sourceNode.getRefId().trim().isEmpty() ||
                targetNode.getRefId() == null || targetNode.getRefId().trim().isEmpty()) {
                log.warn("鑺傜偣缂哄皯鏈夋晥鐨勫紩鐢↖D锛岃烦杩囧叧绯? {}({}) -> {}({})", 
                        sourceNode.getNodeType(), sourceNode.getRefId(), 
                        targetNode.getNodeType(), targetNode.getRefId());
                skippedEdges++;
                continue;
            }
            
            log.info("鍒涘缓閰嶇疆鍏崇郴: {}({}) -> {}({}), sourcePortId: {}", 
                    sourceNode.getNodeType(), sourceNode.getRefId(), 
                    targetNode.getNodeType(), targetNode.getRefId(), sourcePortId);
            
            // 鐢熸垚閰嶇疆鍏崇郴锛屼紶鍏ョ鍙ｄ俊鎭?
            AiClientConfig config = createAiClientConfig(sourceNode, targetNode, sourcePortId);
            if (config != null) {
                configList.add(config);
                validConfigs++;
                log.info("鎴愬姛鍒涘缓閰嶇疆鍏崇郴[{}]: {} -> {}", validConfigs, config.getSourceType() + ":" + config.getSourceId(), config.getTargetType() + ":" + config.getTargetId());
            } else {
                skippedEdges++;
                log.warn("鍒涘缓閰嶇疆鍏崇郴澶辫触: {}({}) -> {}({})", 
                        sourceNode.getNodeType(), sourceNode.getRefId(), 
                        targetNode.getNodeType(), targetNode.getRefId());
            }
        }
        
        log.info("杈瑰叧绯昏В鏋愬畬鎴愶紝澶勭悊{}鏉¤竟锛岃烦杩噞}鏉★紝鐢熸垚{}鏉℃湁鏁堥厤缃叧绯?, 
                processedEdges, skippedEdges, validConfigs);
    }

    /**
     * 鍒涘缓AiClientConfig閰嶇疆鍏崇郴
     */
    private static AiClientConfig createAiClientConfig(NodeInfo sourceNode, NodeInfo targetNode, String sourcePortId) {
        // 纭繚涓や釜鑺傜偣閮芥湁寮曠敤ID
        if (sourceNode.getRefId() == null || targetNode.getRefId() == null) {
            log.warn("鑺傜偣缂哄皯寮曠敤ID锛宻ource: {}, target: {}", sourceNode, targetNode);
            return null;
        }
        
        // 鏋勫缓鎵╁睍鍙傛暟锛屽寘鍚鍙ｄ俊鎭拰鑺傜偣鏍囬
        String extParam = "{}";
        try {
            StringBuilder extParamBuilder = new StringBuilder("{");
            boolean hasParam = false;
            
            // 娣诲姞婧愮鍙D
            if (sourcePortId != null && !sourcePortId.trim().isEmpty()) {
                extParamBuilder.append("\"sourcePortId\":\"").append(sourcePortId).append("\"");
                hasParam = true;
            }
            
            // 娣诲姞婧愯妭鐐规爣棰?
            if (sourceNode.getTitle() != null && !sourceNode.getTitle().trim().isEmpty()) {
                if (hasParam) extParamBuilder.append(",");
                extParamBuilder.append("\"sourceTitle\":\"").append(sourceNode.getTitle()).append("\"");
                hasParam = true;
            }
            
            // 娣诲姞鐩爣鑺傜偣鏍囬
            if (targetNode.getTitle() != null && !targetNode.getTitle().trim().isEmpty()) {
                if (hasParam) extParamBuilder.append(",");
                extParamBuilder.append("\"targetTitle\":\"").append(targetNode.getTitle()).append("\"");
                hasParam = true;
            }
            
            extParamBuilder.append("}");
            extParam = extParamBuilder.toString();
            
        } catch (Exception e) {
            log.warn("鏋勫缓鎵╁睍鍙傛暟澶辫触锛屼娇鐢ㄩ粯璁ゅ€? {}", e.getMessage());
            extParam = "{}";
        }
        
        AiClientConfig config = AiClientConfig.builder()
                .sourceType(sourceNode.getNodeType())
                .sourceId(sourceNode.getRefId())
                .targetType(targetNode.getNodeType())
                .targetId(targetNode.getRefId())
                .extParam(extParam)
                .status(1)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
                
        log.debug("鍒涘缓閰嶇疆瀵硅薄: sourceType={}, sourceId={}, targetType={}, targetId={}, extParam={}", 
                config.getSourceType(), config.getSourceId(), config.getTargetType(), config.getTargetId(), config.getExtParam());
                
        return config;
    }

    /**
     * 楠岃瘉鍜岀粺璁¤В鏋愮粨鏋?
     */
    private static void validateAndLogResults(Map<String, NodeInfo> nodeMap, List<AiClientConfig> configList, int totalEdges) {
        log.info("=== 瑙ｆ瀽缁撴灉缁熻 ===");
        
        // 缁熻鑺傜偣绫诲瀷
        Map<String, Integer> nodeTypeCount = new HashMap<>();
        Map<String, Integer> nodeWithRefIdCount = new HashMap<>();
        
        for (NodeInfo node : nodeMap.values()) {
            String nodeType = node.getNodeType();
            nodeTypeCount.put(nodeType, nodeTypeCount.getOrDefault(nodeType, 0) + 1);
            
            if (node.getRefId() != null && !node.getRefId().trim().isEmpty()) {
                nodeWithRefIdCount.put(nodeType, nodeWithRefIdCount.getOrDefault(nodeType, 0) + 1);
            }
        }
        
        log.info("鑺傜偣绫诲瀷缁熻:");
        for (Map.Entry<String, Integer> entry : nodeTypeCount.entrySet()) {
            int withRefId = nodeWithRefIdCount.getOrDefault(entry.getKey(), 0);
            log.info("  {}: {}涓妭鐐癸紝{}涓湁寮曠敤ID", entry.getKey(), entry.getValue(), withRefId);
        }
        
        // 缁熻閰嶇疆鍏崇郴绫诲瀷
        Map<String, Integer> relationTypeCount = new HashMap<>();
        for (AiClientConfig config : configList) {
            String relationType = config.getSourceType() + " -> " + config.getTargetType();
            relationTypeCount.put(relationType, relationTypeCount.getOrDefault(relationType, 0) + 1);
        }
        
        log.info("閰嶇疆鍏崇郴缁熻:");
        for (Map.Entry<String, Integer> entry : relationTypeCount.entrySet()) {
            log.info("  {}: {}鏉″叧绯?, entry.getKey(), entry.getValue());
        }
        
        log.info("鎬昏: {}涓妭鐐癸紝{}鏉¤竟锛寋}鏉℃湁鏁堥厤缃叧绯?, 
                nodeMap.size(), totalEdges, configList.size());
        log.info("=== 缁熻缁撴潫 ===");
    }

    /**
     * 鑺傜偣淇℃伅鍐呴儴绫?
     */
    private static class NodeInfo {
        private String nodeId;
        private String nodeType;
        private String title;
        private String refId;

        public String getNodeId() {
            return nodeId;
        }

        public void setNodeId(String nodeId) {
            this.nodeId = nodeId;
        }

        public String getNodeType() {
            return nodeType;
        }

        public void setNodeType(String nodeType) {
            this.nodeType = nodeType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getRefId() {
            return refId;
        }

        public void setRefId(String refId) {
            this.refId = refId;
        }

        @Override
        public String toString() {
            return "NodeInfo{" +
                    "nodeId='" + nodeId + '\'' +
                    ", nodeType='" + nodeType + '\'' +
                    ", title='" + title + '\'' +
                    ", refId='" + refId + '\'' +
                    '}';
        }
    }
}
