package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI鏅鸿兘浣撳搷搴?DTO
 *
 * @author xiaofuge bugstack.cn @灏忓倕鍝? * @description AI鏅鸿兘浣撳搷搴旀暟鎹紶杈撳璞? */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiAgentResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 鏅鸿兘浣揑D
     */
    private String agentId;

    /**
     * 鏅鸿兘浣撳悕绉?     */
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
