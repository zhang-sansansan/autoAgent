package cn.ann.ai.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AI鏅鸿兘浣撴嫋鎷夋嫿閰嶇疆鍘嗗彶琛? * @author bugstack铏礊鏍? * @description AI鏅鸿兘浣撴嫋鎷夋嫿閰嶇疆鍘嗗彶琛?PO 瀵硅薄
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiAgentDrawHistory {

    /**
     * 涓婚敭ID
     */
    private Long id;

    /**
     * 閰嶇疆ID锛堝叧鑱攁i_agent_draw_config锛?     */
    private String configId;

    /**
     * 鐗堟湰鍙?     */
    private Integer version;

    /**
     * 鍘嗗彶閰嶇疆JSON鏁版嵁
     */
    private String configData;

    /**
     * 鍙樻洿绫诲瀷锛坈reate銆乽pdate銆乨elete锛?     */
    private String changeType;

    /**
     * 鍙樻洿鎻忚堪
     */
    private String changeDesc;

    /**
     * 鍙樻洿浜?     */
    private String changeBy;

    /**
     * 鍒涘缓鏃堕棿
     */
    private LocalDateTime createTime;

}
