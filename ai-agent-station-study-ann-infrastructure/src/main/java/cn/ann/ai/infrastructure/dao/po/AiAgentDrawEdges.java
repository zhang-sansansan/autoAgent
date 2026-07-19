package cn.ann.ai.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AI鏅鸿兘浣撴嫋鎷夋嫿閰嶇疆杩炵嚎琛? * @author bugstack铏礊鏍? * @description AI鏅鸿兘浣撴嫋鎷夋嫿閰嶇疆杩炵嚎琛?PO 瀵硅薄
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiAgentDrawEdges {

    /**
     * 涓婚敭ID
     */
    private Long id;

    /**
     * 閰嶇疆ID锛堝叧鑱攁i_agent_draw_config锛?     */
    private String configId;

    /**
     * 杩炵嚎ID锛堣嚜鍔ㄧ敓鎴愮殑鍞竴鏍囪瘑锛?     */
    private String edgeId;

    /**
     * 婧愯妭鐐笽D
     */
    private String sourceNodeId;

    /**
     * 鐩爣鑺傜偣ID
     */
    private String targetNodeId;

    /**
     * 婧愮鍙D锛堝彲閫夛級
     */
    private String sourcePortId;

    /**
     * 鐩爣绔彛ID锛堝彲閫夛級
     */
    private String targetPortId;

    /**
     * 杩炵嚎绫诲瀷锛坉efault銆乧onditional绛夛級
     */
    private String edgeType;

    /**
     * 杩炵嚎鏁版嵁锛圝SON鏍煎紡锛屾墿灞曚俊鎭級
     */
    private String edgeData;

    /**
     * 杩炵嚎搴忓彿锛堢敤浜庢帓搴忥級
     */
    private Integer sequence;

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
