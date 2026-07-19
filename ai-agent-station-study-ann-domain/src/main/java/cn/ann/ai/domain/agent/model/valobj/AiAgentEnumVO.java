package cn.ann.ai.domain.agent.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Agent 閫氱敤鏋氫妇
 *
 * @author xiaofuge bugstack.cn @灏忓倕鍝? * 2025/6/27 16:52
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AiAgentEnumVO {

    AI_CLIENT_API("瀵硅瘽API", "api", "ai_client_api_", "ai_client_api_data_list", "aiClientApiLoadDataStrategy"),
    AI_CLIENT_MODEL("瀵硅瘽妯″瀷", "model", "ai_client_model_", "ai_client_model_data_list", "aiClientModelLoadDataStrategy"),
    AI_CLIENT_SYSTEM_PROMPT("鎻愮ず璇?, "prompt", "ai_client_system_prompt_", "ai_client_system_prompt_data_list", "aiClientSystemPromptLoadDataStrategy"),
    AI_CLIENT_TOOL_MCP("mcp宸ュ叿", "tool_mcp", "ai_client_tool_mcp_", "ai_client_tool_mcp_data_list", "aiClientToolMCPLoadDataStrategy"),
    AI_CLIENT_ADVISOR("椤鹃棶瑙掕壊", "advisor", "ai_client_advisor_", "ai_client_advisor_data_list", "aiClientAdvisorLoadDataStrategy"),
    AI_CLIENT("瀹㈡埛绔?, "client", "ai_client_", "ai_client_data_list", "aiClientLoadDataStrategy"),
    ;

    /**
     * 鍚嶇О
     */
    private String name;

    /**
     * code
     */
    private String code;

    /**
     * Bean 瀵硅薄鍚嶇О鏍囩
     */
    private String beanNameTag;

    /**
     * 鏁版嵁鍚嶇О
     */
    private String dataName;

    /**
     * 瑁呴厤鏁版嵁绛栫暐
     */
    private String loadDataStrategy;

    /**
     * 鏍规嵁code鑾峰彇瀵瑰簲鐨勬灇涓?     *
     * @param code 鏋氫妇code鍊?     * @return 瀵瑰簲鐨勬灇涓撅紝濡傛灉鏈壘鍒板垯杩斿洖null
     */
    public static AiAgentEnumVO getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (AiAgentEnumVO enumVO : AiAgentEnumVO.values()) {
            if (code.equals(enumVO.getCode())) {
                return enumVO;
            }
        }
        throw new RuntimeException("code value " + code + " not exist!");
    }

    /**
     * 鑾峰彇Bean鍚嶇О
     *
     * @param id 浼犲叆鐨勫弬鏁?     * @return beanNameTag + id 鎷兼帴鐨凚ean鍚嶇О
     */
    public String getBeanName(String id) {
        return this.beanNameTag + id;
    }

}

