package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI鏅鸿兘浣撴嫋鎷夋嫿閰嶇疆鏌ヨ璇锋眰 DTO
 *
 * @author xiaofuge bugstack.cn @灏忓倕鍝? * 2025/10/02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiAgentDrawConfigQueryRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 閰嶇疆ID锛堝敮涓€鏍囪瘑锛?     */
    private String configId;

    /**
     * 閰嶇疆鍚嶇О锛堟ā绯婃煡璇級
     */
    private String configName;

    /**
     * 鍏宠仈鐨勬櫤鑳戒綋ID
     */
    private String agentId;

    /**
     * 鐘舵€?0:绂佺敤,1:鍚敤)
     */
    private Integer status;

    /**
     * 椤电爜锛堜粠1寮€濮嬶級
     */
    private Integer pageNum;

    /**
     * 姣忛〉澶у皬
     */
    private Integer pageSize;
}
