package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI鏅鸿兘浣撴嫋鎷夋嫿閰嶇疆璇锋眰DTO
 *
 * @author xiaofuge bugstack.cn @灏忓倕鍝? * 2025/1/20 10:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiAgentDrawConfigRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
     * 鍏宠仈鐨勬櫤鑳戒綋ID
     */
    private String agentId;

    /**
     * 瀹屾暣鐨勬嫋鎷夋嫿閰嶇疆JSON鏁版嵁锛堝寘鍚玭odes鍜宔dges锛?     */
    private String configData;

    /**
     * 鍒涘缓浜?     */
    private String createBy;

    /**
     * 鏇存柊浜?     */
    private String updateBy;

}
