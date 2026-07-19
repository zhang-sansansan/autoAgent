package cn.ann.ai.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AI鏅鸿兘浣撻厤缃〃
 * @author bugstack铏礊鏍? * @description AI鏅鸿兘浣撻厤缃〃 PO 瀵硅薄
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiAgent {

    /**
     * 涓婚敭ID
     */
    private Long id;

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
     * 鎵ц绛栫暐(auto銆乫low)
     */
    private String strategy;
    
    /**
     * 娓犻亾绫诲瀷(agent锛宑hat_stream)
     */
    private String channel;

    /**
     * 鐘舵€?0:绂佺敤,1:鍚敤)
     */
    private Integer status;

    /**
     * 鍒涘缓鏃堕棿
     */
    private LocalDateTime createTime;

    /**
     * 鏇存柊鏃堕棿
     */
    private LocalDateTime updateTime;

}
