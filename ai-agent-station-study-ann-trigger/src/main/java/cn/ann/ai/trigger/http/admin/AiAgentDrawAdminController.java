package cn.ann.ai.trigger.http.admin;

import cn.ann.ai.api.IAiAgentDrawAdminService;
import cn.ann.ai.api.dto.AiAgentDrawConfigRequestDTO;
import cn.ann.ai.api.dto.AiAgentDrawConfigResponseDTO;
import cn.ann.ai.api.dto.AiAgentDrawConfigQueryRequestDTO;
import cn.ann.ai.api.response.Response;
import cn.ann.ai.infrastructure.dao.*;
import cn.ann.ai.infrastructure.dao.po.AiAgent;
import cn.ann.ai.infrastructure.dao.po.AiAgentDrawConfig;
import cn.ann.ai.infrastructure.dao.po.AiAgentFlowConfig;
import cn.ann.ai.infrastructure.dao.po.AiClientConfig;
import cn.ann.ai.trigger.http.admin.util.DrawConfigParser;
import cn.ann.ai.types.enums.ResponseCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * 鎷栨媺鎷? *
 * @author xiaofuge bugstack.cn @灏忓倕鍝? * 2025/9/28 07:35
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/admin/ai-agent-draw")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AiAgentDrawAdminController implements IAiAgentDrawAdminService {

    @Resource
    private IAiAgentDrawConfigDao aiAgentDrawConfigDao;
    @Resource
    private IAiClientConfigDao aiClientConfigDao;
    @Resource
    private IAiAgentDao aiAgentDao;
    @Resource
    private IAiAgentFlowConfigDao aiAgentFlowConfigDao;

    @Override
    @PostMapping("/query-list")
    public Response<List<AiAgentDrawConfigResponseDTO>> queryDrawConfigList(@RequestBody AiAgentDrawConfigQueryRequestDTO request) {
        try {
            log.info("鏌ヨ鎷栨媺鎷芥祦绋嬪浘閰嶇疆鍒楄〃璇锋眰锛歿}", request);

            List<AiAgentDrawConfig> configs;

            // 鏉′欢鏌ヨ
            if (StringUtils.hasText(request.getConfigId())) {
                AiAgentDrawConfig cfg = aiAgentDrawConfigDao.queryByConfigId(request.getConfigId());
                configs = cfg != null ? List.of(cfg) : List.of();
            } else if (StringUtils.hasText(request.getConfigName())) {
                configs = aiAgentDrawConfigDao.queryByConfigName(request.getConfigName());
            } else if (StringUtils.hasText(request.getAgentId())) {
                AiAgentDrawConfig cfg = aiAgentDrawConfigDao.queryByAgentId(request.getAgentId());
                configs = cfg != null ? List.of(cfg) : List.of();
            } else if (request.getStatus() != null) {
                if (request.getStatus() == 1) {
                    configs = aiAgentDrawConfigDao.queryEnabledConfigs();
                } else {
                    configs = aiAgentDrawConfigDao.queryAll();
                }
            } else {
                configs = aiAgentDrawConfigDao.queryAll();
            }

            // 绠€鍗曞垎椤碉紙鍐呭瓨鍒嗛〉锛?            if (request.getPageNum() != null && request.getPageSize() != null) {
                int pageNum = Math.max(1, request.getPageNum());
                int pageSize = Math.max(1, request.getPageSize());
                int start = (pageNum - 1) * pageSize;
                int end = Math.min(start + pageSize, configs.size());
                if (start < configs.size()) {
                    configs = configs.subList(start, end);
                } else {
                    configs = List.of();
                }
            }

            // PO 杞?DTO
            List<AiAgentDrawConfigResponseDTO> responseDTOs = new ArrayList<>();
            for (AiAgentDrawConfig config : configs) {
                AiAgentDrawConfigResponseDTO dto = new AiAgentDrawConfigResponseDTO();
                BeanUtils.copyProperties(config, dto);
                responseDTOs.add(dto);
            }

            return Response.<List<AiAgentDrawConfigResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("鏌ヨ鎷栨媺鎷芥祦绋嬪浘閰嶇疆鍒楄〃澶辫触", e);
            return Response.<List<AiAgentDrawConfigResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @PostMapping("/save-config")
    @Transactional(rollbackFor = Exception.class)
    public Response<String> saveDrawConfig(@RequestBody AiAgentDrawConfigRequestDTO request) {
        try {
            log.info("淇濆瓨娴佺▼鍥鹃厤缃姹傦細{}", request);

            // 鐢熸垚8浣嶆暟瀛楃殑鍞竴AgentId

            // 鍙傛暟鏍￠獙
            if (!StringUtils.hasText(request.getConfigName())) {
                return Response.<String>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info("閰嶇疆鍚嶇О涓嶈兘涓虹┖")
                        .build();
            }

            if (!StringUtils.hasText(request.getConfigData())) {
                return Response.<String>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info("閰嶇疆鏁版嵁涓嶈兘涓虹┖")
                        .build();
            }

            // 瑙ｆ瀽JSON涓殑agent淇℃伅
            String configId = request.getConfigId();
            if (!StringUtils.hasText(configId)) {
                configId = UUID.randomUUID().toString().replace("-", "");
            }

            AiAgentDrawConfig existingConfig = aiAgentDrawConfigDao.queryByConfigId(configId);
            String agentId = existingConfig != null && StringUtils.hasText(existingConfig.getAgentId())
                    ? existingConfig.getAgentId()
                    : String.format("%08d", System.currentTimeMillis() % 100000000L);
            request.setAgentId(agentId);

            String[] agentInfo = parseAgentInfoFromJson(request.getConfigData());
            String agentName = agentInfo[0];
            String description = agentInfo[1];
            String channel = agentInfo[2];
            String strategy = agentInfo[3];

            AiAgent aiAgent = AiAgent.builder()
                    .agentId(request.getAgentId())
                    .agentName(agentName)
                    .channel(channel)
                    .strategy(strategy)
                    .status(1)
                    .description(description)
                    .updateTime(LocalDateTime.now())
                    .build();
            if (aiAgentDao.queryByAgentId(agentId) == null) {
                aiAgentDao.insert(aiAgent);
            } else {
                aiAgentDao.updateByAgentId(aiAgent);
            }

            AiAgentDrawConfig drawConfig = new AiAgentDrawConfig();
            BeanUtils.copyProperties(request, drawConfig);
            drawConfig.setConfigId(configId);
            drawConfig.setVersion(1); // 榛樿鐗堟湰鍙?            drawConfig.setStatus(1); // 榛樿鍚敤鐘舵€?
            int result;
            if (existingConfig != null) {
                // 鏇存柊鐜版湁閰嶇疆
                drawConfig.setId(existingConfig.getId());
                drawConfig.setVersion(existingConfig.getVersion() + 1);
                drawConfig.setUpdateTime(LocalDateTime.now());
                result = aiAgentDrawConfigDao.updateByConfigId(drawConfig);
                log.info("鏇存柊娴佺▼鍥鹃厤缃紝configId: {}, result: {}", configId, result);
            } else {
                // 鍒涘缓鏂伴厤缃?                drawConfig.setCreateTime(LocalDateTime.now());
                drawConfig.setUpdateTime(LocalDateTime.now());
                result = aiAgentDrawConfigDao.insert(drawConfig);
                log.info("鍒涘缓娴佺▼鍥鹃厤缃紝configId: {}, result: {}", configId, result);
            }

            if (result > 0) {
                // 瑙ｆ瀽JSON閰嶇疆鏁版嵁锛岀敓鎴愬叧绯绘槧灏勫苟瀛樺偍鍒癮i_client_config琛?                try {
                    List<AiClientConfig> configRelations = DrawConfigParser.parseConfigData(request.getConfigData());
                    if (!configRelations.isEmpty()) {
                        Set<String> cleanedSources = new HashSet<>();
                        for (AiClientConfig config : configRelations) {
                            String sourceKey = config.getSourceType() + ":" + config.getSourceId();
                            if (cleanedSources.add(sourceKey)) {
                                int deleted = aiClientConfigDao.deleteBySourceTypeAndId(config.getSourceType(), config.getSourceId());
                                log.info("鍒犻櫎鏃ч厤缃叧绯?sourceType={}, sourceId={}, count={}",
                                        config.getSourceType(), config.getSourceId(), deleted);
                            }
                        }

                        // 鎵归噺鎻掑叆鏂扮殑鍏崇郴鏁版嵁
                        for (AiClientConfig config : configRelations) {
                            // 璁剧疆鎵╁睍鍙傛暟锛岃褰曟潵婧愰厤缃甀D
                            config.setExtParam("{\"configId\":\"" + configId + "\"}");
                            aiClientConfigDao.insert(config);
                            log.debug("鎻掑叆鏂扮殑閰嶇疆鍏崇郴: sourceType={}, sourceId={}, targetType={}, targetId={}",
                                    config.getSourceType(), config.getSourceId(), config.getTargetType(), config.getTargetId());
                        }
                        log.info("鎴愬姛淇濆瓨{}鏉￠厤缃叧绯绘暟鎹?, configRelations.size());
                    }
                } catch (Exception e) {
                    log.error("瑙ｆ瀽鍜屼繚瀛橀厤缃叧绯绘暟鎹け璐ワ紝configId: {}", configId, e);
                }

                // 瑙ｆ瀽JSON閰嶇疆鏁版嵁锛屾彁鍙朿lient淇℃伅骞朵繚瀛榓gent-client鍏崇郴
                try {
                    List<AiAgentFlowConfig> agentFlowConfigs = parseClientInfoFromJson(request.getConfigData(), agentId);
                    if (!agentFlowConfigs.isEmpty()) {
                        aiAgentFlowConfigDao.deleteByAgentId(agentId);
                        log.info("鍒犻櫎agentId{}鐨勬棫娴佺▼閰嶇疆鏁版嵁", agentId);

                        // 鎵归噺鎻掑叆鏂扮殑agent-client鍏崇郴鏁版嵁
                        for (AiAgentFlowConfig flowConfig : agentFlowConfigs) {
                            aiAgentFlowConfigDao.insert(flowConfig);
                        }
                        log.info("鎴愬姛淇濆瓨{}鏉gent-client鍏崇郴鏁版嵁", agentFlowConfigs.size());
                    }
                } catch (Exception e) {
                    log.error("瑙ｆ瀽鍜屼繚瀛榓gent-client鍏崇郴鏁版嵁澶辫触锛宎gentId: {}", agentId, e);
                    // 杩欓噷涓嶅奖鍝嶄富娴佺▼锛屽彧璁板綍閿欒鏃ュ織
                }

                return Response.<String>builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info(ResponseCode.SUCCESS.getInfo())
                        .data(configId)
                        .build();
            } else {
                return Response.<String>builder()
                        .code(ResponseCode.UN_ERROR.getCode())
                        .info("淇濆瓨澶辫触")
                        .build();
            }

        } catch (Exception e) {
            log.error("淇濆瓨娴佺▼鍥鹃厤缃け璐?, e);
            return Response.<String>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info("淇濆瓨澶辫触锛? + e.getMessage())
                    .build();
        }
    }

    /**
     * 瑙ｆ瀽JSON閰嶇疆鏁版嵁涓殑agent淇℃伅
     *
     * @param configData JSON閰嶇疆鏁版嵁
     * @return agent淇℃伅鏁扮粍 [agentName, channel]
     */
    private String[] parseAgentInfoFromJson(String configData) {
        String[] agentInfo = new String[]{"", "", "", ""}; // 榛樿鍊?
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(configData);
            JsonNode nodesArray = rootNode.get("nodes");

            if (nodesArray != null && nodesArray.isArray()) {
                for (JsonNode node : nodesArray) {
                    String nodeType = node.get("type").asText();

                    // 鍙鐞唗ype涓?agent"鐨勮妭鐐?                    if ("agent".equals(nodeType)) {
                        JsonNode dataNode = node.get("data");
                        if (dataNode != null) {
                            JsonNode inputsValuesNode = dataNode.get("inputsValues");
                            if (inputsValuesNode != null) {
                                log.debug("寮€濮嬭В鏋恆gent鑺傜偣鐨刬nputsValues: {}", inputsValuesNode.toString());
                                
                                // 鎻愬彇agent淇℃伅
                                String agentName = extractValueFromInputs(inputsValuesNode, "agentName");
                                String description = extractValueFromInputs(inputsValuesNode, "description");
                                String channel = extractValueFromInputs(inputsValuesNode, "channel");
                                String strategy = extractValueFromInputs(inputsValuesNode, "strategy");

                                agentInfo[0] = agentName != null ? agentName : "";
                                agentInfo[1] = description != null ? description : "";
                                agentInfo[2] = channel != null ? channel : "";
                                agentInfo[3] = strategy != null ? strategy : "";

                                log.info("瑙ｆ瀽鍒癮gent淇℃伅: agentName={}, description={}, channel={}, strategy={}", 
                                        agentName, description, channel, strategy);
                                break; // 鎵惧埌绗竴涓猘gent鑺傜偣灏遍€€鍑?                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("瑙ｆ瀽JSON閰嶇疆鏁版嵁涓殑agent淇℃伅澶辫触", e);
            // 杩斿洖榛樿鍊硷紝涓嶆姏鍑哄紓甯革紝閬垮厤褰卞搷鏁翠釜淇濆瓨娴佺▼
        }

        return agentInfo;
    }

    /**
     * 瑙ｆ瀽JSON閰嶇疆鏁版嵁涓殑client淇℃伅
     *
     * @param configData JSON閰嶇疆鏁版嵁
     * @param agentId    鏅鸿兘浣揑D
     * @return agent-client鍏崇郴閰嶇疆鍒楄〃
     */
    private List<AiAgentFlowConfig> parseClientInfoFromJson(String configData, String agentId) {
        List<AiAgentFlowConfig> agentFlowConfigs = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(configData);
            JsonNode nodesArray = rootNode.get("nodes");

            if (nodesArray != null && nodesArray.isArray()) {
                for (JsonNode node : nodesArray) {
                    String nodeType = node.get("type").asText();

                    // 鍙鐞唗ype涓?client"鐨勮妭鐐?                    if ("client".equals(nodeType)) {
                        JsonNode dataNode = node.get("data");
                        if (dataNode != null) {
                            JsonNode inputsValuesNode = dataNode.get("inputsValues");
                            if (inputsValuesNode != null) {
                                // 鎻愬彇client淇℃伅
                                String clientType = extractValueFromInputs(inputsValuesNode, "clientType");
                                String clientId = extractValueFromInputs(inputsValuesNode, "clientId");
                                String clientName = extractValueFromInputs(inputsValuesNode, "clientName");
                                Integer sequence = extractIntegerValueFromInputs(inputsValuesNode, "sequence");
                                String stepPrompt = extractValueFromInputs(inputsValuesNode, "stepPrompt");

                                // 鍒涘缓AiAgentFlowConfig瀵硅薄
                                AiAgentFlowConfig flowConfig = AiAgentFlowConfig.builder()
                                        .agentId(agentId)
                                        .clientId(clientId)
                                        .clientName(clientName)
                                        .clientType(clientType)
                                        .sequence(sequence)
                                        .stepPrompt(stepPrompt)
                                        .createTime(LocalDateTime.now())
                                        .build();

                                agentFlowConfigs.add(flowConfig);
                                log.info("瑙ｆ瀽鍒癱lient淇℃伅: clientType={}, clientName={}, sequence={}",
                                        clientType, clientName, sequence);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("瑙ｆ瀽JSON閰嶇疆鏁版嵁澶辫触", e);
            throw new RuntimeException("瑙ｆ瀽JSON閰嶇疆鏁版嵁澶辫触", e);
        }

        return agentFlowConfigs;
    }

    /**
     * 浠巌nputsValues涓彁鍙栧瓧绗︿覆鍊?     *
     * @param inputsValuesNode inputsValues鑺傜偣
     * @param fieldName        瀛楁鍚?     * @return 瀛楁鍊?     */
    private String extractValueFromInputs(JsonNode inputsValuesNode, String fieldName) {
        JsonNode fieldNode = inputsValuesNode.get(fieldName);
        log.debug("鎻愬彇瀛楁 '{}': fieldNode={}", fieldName, fieldNode != null ? fieldNode.toString() : "null");
        
        if (fieldNode != null) {
            // 澶勭悊鏁扮粍鏍煎紡锛歔{"key": "xxx", "value": "yyy"}] 鎴?[{"key": "xxx", "value": {"content": "yyy"}}]
            if (fieldNode.isArray() && !fieldNode.isEmpty()) {
                JsonNode firstItem = fieldNode.get(0);
                if (firstItem != null) {
                    JsonNode valueNode = firstItem.get("value");
                    log.debug("瀛楁 '{}' 鏁扮粍鏍煎紡锛寁alueNode={}", fieldName, valueNode != null ? valueNode.toString() : "null");
                    
                    if (valueNode != null) {
                        // 濡傛灉value鏄璞★紝灏濊瘯鑾峰彇content瀛楁
                        if (valueNode.isObject()) {
                            JsonNode contentNode = valueNode.get("content");
                            if (contentNode != null) {
                                String result = contentNode.asText();
                                log.debug("瀛楁 '{}' 浠巆ontent鑾峰彇鍊? {}", fieldName, result);
                                return result;
                            }
                        }
                        // 濡傛灉value鏄瓧绗︿覆锛岀洿鎺ヨ繑鍥?                        else if (valueNode.isTextual()) {
                            String result = valueNode.asText();
                            log.debug("瀛楁 '{}' 鐩存帴鑾峰彇瀛楃涓插€? {}", fieldName, result);
                            return result;
                        }
                        // 濡傛灉value鏄暟瀛楋紝杞崲涓哄瓧绗︿覆
                        else if (valueNode.isNumber()) {
                            String result = valueNode.asText();
                            log.debug("瀛楁 '{}' 鏁板瓧杞瓧绗︿覆鍊? {}", fieldName, result);
                            return result;
                        }
                    }
                }
            }
            // 澶勭悊鐩存帴瀛楃涓叉牸寮忥細"fieldName": "value"
            else if (fieldNode.isTextual()) {
                String result = fieldNode.asText();
                log.debug("瀛楁 '{}' 鐩存帴瀛楃涓叉牸寮忓€? {}", fieldName, result);
                return result;
            }
        }
        
        log.debug("瀛楁 '{}' 鏈壘鍒版湁鏁堝€?, fieldName);
        return null;
    }

    /**
     * 浠巌nputsValues涓彁鍙栨暣鏁板€?     *
     * @param inputsValuesNode inputsValues鑺傜偣
     * @param fieldName        瀛楁鍚?     * @return 瀛楁鍊?     */
    private Integer extractIntegerValueFromInputs(JsonNode inputsValuesNode, String fieldName) {
        JsonNode fieldNode = inputsValuesNode.get(fieldName);
        if (fieldNode != null) {
            // 澶勭悊鏁扮粍鏍煎紡锛歔{"key": "xxx", "value": 123}] 鎴?[{"key": "xxx", "value": {"content": 123}}]
            if (fieldNode.isArray() && fieldNode.size() > 0) {
                JsonNode firstItem = fieldNode.get(0);
                if (firstItem != null) {
                    JsonNode valueNode = firstItem.get("value");
                    if (valueNode != null) {
                        // 濡傛灉value鏄璞★紝灏濊瘯鑾峰彇content瀛楁
                        if (valueNode.isObject()) {
                            JsonNode contentNode = valueNode.get("content");
                            if (contentNode != null && contentNode.isNumber()) {
                                return contentNode.asInt();
                            }
                        }
                        // 濡傛灉value鏄暟瀛楋紝鐩存帴杩斿洖
                        else if (valueNode.isNumber()) {
                            return valueNode.asInt();
                        }
                        // 濡傛灉value鏄瓧绗︿覆锛屽皾璇曡浆鎹负鏁板瓧
                        else if (valueNode.isTextual()) {
                            try {
                                return Integer.parseInt(valueNode.asText());
                            } catch (NumberFormatException e) {
                                log.warn("鏃犳硶灏嗗瓧绗︿覆 '{}' 杞崲涓烘暣鏁?, valueNode.asText());
                            }
                        }
                    }
                }
            }
            // 澶勭悊鐩存帴鏁板€兼牸寮忥細"fieldName": 123
            else if (fieldNode.isNumber()) {
                return fieldNode.asInt();
            }
        }
        return null;
    }

    @Override
    @GetMapping("/get-config/{configId}")
    public Response<AiAgentDrawConfigResponseDTO> getDrawConfig(@PathVariable("configId") String configId) {
        try {
            log.info("鑾峰彇娴佺▼鍥鹃厤缃姹傦紝configId: {}", configId);

            if (!StringUtils.hasText(configId)) {
                return Response.<AiAgentDrawConfigResponseDTO>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info("閰嶇疆ID涓嶈兘涓虹┖")
                        .build();
            }

            AiAgentDrawConfig drawConfig = aiAgentDrawConfigDao.queryByConfigId(configId);

            if (drawConfig == null) {
                return Response.<AiAgentDrawConfigResponseDTO>builder()
                        .code(ResponseCode.UN_ERROR.getCode())
                        .info("閰嶇疆涓嶅瓨鍦?)
                        .build();
            }

            AiAgentDrawConfigResponseDTO responseDTO = new AiAgentDrawConfigResponseDTO();
            BeanUtils.copyProperties(drawConfig, responseDTO);

            return Response.<AiAgentDrawConfigResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTO)
                    .build();

        } catch (Exception e) {
            log.error("鑾峰彇娴佺▼鍥鹃厤缃け璐?, e);
            return Response.<AiAgentDrawConfigResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info("鑾峰彇澶辫触锛? + e.getMessage())
                    .build();
        }
    }

    @Override
    @DeleteMapping("/delete-config/{configId}")
    @Transactional(rollbackFor = Exception.class)
    public Response<String> deleteDrawConfig(@PathVariable("configId") String configId) {
        try {
            log.info("鍒犻櫎娴佺▼鍥鹃厤缃姹傦紝configId: {}", configId);

            if (!StringUtils.hasText(configId)) {
                return Response.<String>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info("閰嶇疆ID涓嶈兘涓虹┖")
                        .build();
            }

            // 1. 鍏堟煡璇㈤厤缃鎯呰幏鍙朼gentId
            AiAgentDrawConfig drawConfig = aiAgentDrawConfigDao.queryByConfigId(configId);
            if (drawConfig == null) {
                return Response.<String>builder()
                        .code(ResponseCode.UN_ERROR.getCode())
                        .info("鍒犻櫎澶辫触锛岄厤缃笉瀛樺湪")
                        .build();
            }

            String agentId = drawConfig.getAgentId();
            log.info("鍒犻櫎娴佺▼鍥鹃厤缃紝configId: {}, agentId: {}", configId, agentId);

            // 2. 鍒犻櫎鎷栨媺鎷介厤缃?            int drawConfigResult = aiAgentDrawConfigDao.deleteByConfigId(configId);
            log.info("鍒犻櫎鎷栨媺鎷介厤缃粨鏋? {}", drawConfigResult);

            // 3. 鍒犻櫎鏅鸿兘浣撻厤缃?            if (StringUtils.hasText(agentId)) {
                int agentResult = aiAgentDao.deleteByAgentId(agentId);
                log.info("鍒犻櫎鏅鸿兘浣撻厤缃粨鏋? {}", agentResult);

                // 4. 鍒犻櫎鏅鸿兘浣撴祦绋嬮厤缃?                int flowConfigResult = aiAgentFlowConfigDao.deleteByAgentId(agentId);
                log.info("鍒犻櫎鏅鸿兘浣撴祦绋嬮厤缃粨鏋? {}", flowConfigResult);
            }

            if (drawConfigResult > 0) {
                return Response.<String>builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info(ResponseCode.SUCCESS.getInfo())
                        .data("鍒犻櫎鎴愬姛")
                        .build();
            } else {
                return Response.<String>builder()
                        .code(ResponseCode.UN_ERROR.getCode())
                        .info("鍒犻櫎澶辫触锛岄厤缃笉瀛樺湪")
                        .build();
            }

        } catch (Exception e) {
            log.error("鍒犻櫎娴佺▼鍥鹃厤缃け璐?, e);
            return Response.<String>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info("鍒犻櫎澶辫触锛? + e.getMessage())
                    .build();
        }
    }
}

