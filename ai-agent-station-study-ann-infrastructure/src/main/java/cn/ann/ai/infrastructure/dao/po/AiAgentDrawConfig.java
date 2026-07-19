package cn.ann.ai.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AI鏅鸿兘浣撴嫋鎷夋嫿閰嶇疆涓昏〃
 * @author bugstack铏礊鏍? * @description AI鏅鸿兘浣撴嫋鎷夋嫿閰嶇疆涓昏〃 PO 瀵硅薄
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiAgentDrawConfig {

    /**
     * 涓婚敭ID
     */
    private Long id;

    /**
     * 閰嶇疆ID锛堝敮涓€鏍囪瘑锛?     */
    private String configId;

    /**
     * 閰嶇疆鍚嶇О
     */
    private String configName;

    /**
     * 閰嶇疆鎻忚堪
     */
    private String description;

    /**
     * 鍏宠仈鐨勬櫤鑳戒綋ID锛堟潵鑷猘i_agent琛級
     */
    private String agentId;

    /**
     * 瀹屾暣鐨勬嫋鎷夋嫿閰嶇疆JSON鏁版嵁锛堝寘鍚玭odes鍜宔dges锛?     */
    private String configData;

    /**
     * 閰嶇疆鐗堟湰鍙?     */
    private Integer version;

    /**
     * 鐘舵€?0:绂佺敤,1:鍚敤)
     */
    private Integer status;

    /**
     * 鍒涘缓浜?     */
    private String createBy;

    /**
     * 鏇存柊浜?     */
    private String updateBy;

    /**
     * 鍒涘缓鏃堕棿
     */
    private LocalDateTime createTime;

    /**
     * 鏇存柊鏃堕棿
     */
    private LocalDateTime updateTime;

}
