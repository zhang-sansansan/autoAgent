package cn.ann.ai.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AI鏅鸿兘浣撴嫋鎷夋嫿閰嶇疆鍏崇郴琛? * @author bugstack铏礊鏍? * @description AI鏅鸿兘浣撴嫋鎷夋嫿閰嶇疆鍏崇郴琛?PO 瀵硅薄
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiAgentDrawRelations {

    /**
     * 涓婚敭ID
     */
    private Long id;

    /**
     * 閰嶇疆ID锛堝叧鑱攁i_agent_draw_config锛?     */
    private String configId;

    /**
     * 婧愯妭鐐笽D
     */
    private String sourceNodeId;

    /**
     * 婧愮被鍨嬶紙model銆乧lient銆乤gent銆乸rompt銆乤dvisor銆乼ool_mcp锛?     */
    private String sourceType;

    /**
     * 婧愬紩鐢↖D锛堝疄闄呯殑璧勬簮ID锛?     */
    private String sourceRefId;

    /**
     * 鐩爣鑺傜偣ID
     */
    private String targetNodeId;

    /**
     * 鐩爣绫诲瀷锛坢odel銆乧lient銆乤gent銆乸rompt銆乤dvisor銆乼ool_mcp锛?     */
    private String targetType;

    /**
     * 鐩爣寮曠敤ID锛堝疄闄呯殑璧勬簮ID锛?     */
    private String targetRefId;

    /**
     * 鍏崇郴绫诲瀷锛坉efault銆乧onditional銆乴oop绛夛級
     */
    private String relationType;

    /**
     * 鎵╁睍鍙傛暟锛圝SON鏍煎紡锛?     */
    private String extParam;

    /**
     * 鍏崇郴搴忓彿锛堢敤浜庢帓搴忥級
     */
    private Integer sequence;

    /**
     * 鍚屾鐘舵€?0:鏈悓姝?1:宸插悓姝ュ埌ai_client_config)
     */
    private Integer syncStatus;

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
