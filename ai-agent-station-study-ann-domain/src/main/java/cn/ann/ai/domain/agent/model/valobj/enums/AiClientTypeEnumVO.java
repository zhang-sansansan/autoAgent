package cn.ann.ai.domain.agent.model.valobj.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author xiaofuge bugstack.cn @灏忓倕鍝?
 * 2025/7/27 17:25
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AiClientTypeEnumVO {

    DEFAULT("DEFAULT", "閫氱敤鐨?),
    TASK_ANALYZER_CLIENT("TASK_ANALYZER_CLIENT", "浠诲姟鍒嗘瀽鍜岀姸鎬佸垽鏂?),
    PRECISION_EXECUTOR_CLIENT("PRECISION_EXECUTOR_CLIENT", "鍏蜂綋浠诲姟鎵ц"),
    QUALITY_SUPERVISOR_CLIENT("QUALITY_SUPERVISOR_CLIENT", "璐ㄩ噺妫€鏌ュ拰浼樺寲"),
    RESPONSE_ASSISTANT("RESPONSE_ASSISTANT", "鏅鸿兘鍝嶅簲鍔╂墜"),

    TOOL_MCP_CLIENT("TOOL_MCP_CLIENT", "宸ュ叿鍒嗘瀽"),
    PLANNING_CLIENT("PLANNING_CLIENT","浠诲姟瑙勫垝"),
    EXECUTOR_CLIENT("EXECUTOR_CLIENT", "浠诲姟鎵ц")

    ;

    private String code;
    private String info;

}

