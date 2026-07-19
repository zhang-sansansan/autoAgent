package cn.ann.ai.domain.agent.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiAgentVO {
    /**
     * 鏅鸿兘浣揑D
     */
    private String agentId;

    /**
     * 鏅鸿兘浣撳悕绉?
     */
    private String agentName;

    /**
     * 鎻忚堪
     */
    private String description;

    /**
     * 娓犻亾绫诲瀷(agent锛宑hat_stream)
     */
    private String channel;

    /**
     * 鎵ц绛栫暐(auto銆乫low)
     */
    private String strategy;

    /**
     * 鐘舵€?0:绂佺敤,1:鍚敤)
     */
    private Integer status;
}

