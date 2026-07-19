package cn.ann.ai.domain.agent.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 鏅鸿兘浣撲换鍔?
 * @author xiaofuge bugstack.cn @灏忓倕鍝?
 * 2025/9/13 16:08
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiAgentTaskScheduleVO {

    /**
     * 涓婚敭ID
     */
    private Long id;

    /**
     * 鏅鸿兘浣揑D
     */
    private String agentId;

    /**
     * 浠诲姟鎻忚堪
     */
    private String description;

    /**
     * 鏃堕棿琛ㄨ揪寮?濡? 0/3 * * * * *)
     */
    private String cronExpression;

    /**
     * 浠诲姟鍏ュ弬閰嶇疆(JSON鏍煎紡)
     */
    private String taskParam;

}

