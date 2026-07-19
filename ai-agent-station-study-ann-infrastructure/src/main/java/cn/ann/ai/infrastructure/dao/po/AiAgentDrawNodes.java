package cn.ann.ai.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * AI鏅鸿兘浣撴嫋鎷夋嫿閰嶇疆鑺傜偣琛? * @author bugstack铏礊鏍? * @description AI鏅鸿兘浣撴嫋鎷夋嫿閰嶇疆鑺傜偣琛?PO 瀵硅薄
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiAgentDrawNodes {

    /**
     * 涓婚敭ID
     */
    private Long id;

    /**
     * 閰嶇疆ID锛堝叧鑱攁i_agent_draw_config锛?     */
    private String configId;

    /**
     * 鑺傜偣ID锛堝湪閰嶇疆涓殑鍞竴鏍囪瘑锛?     */
    private String nodeId;

    /**
     * 鑺傜偣绫诲瀷锛坰tart銆乧lient銆乤gent銆乼ask銆乤dvisor銆乸rompt銆乵odel銆乼ool-mcp銆乪nd绛夛級
     */
    private String nodeType;

    /**
     * 鑺傜偣鏍囬
     */
    private String nodeTitle;

    /**
     * X鍧愭爣浣嶇疆
     */
    private BigDecimal positionX;

    /**
     * Y鍧愭爣浣嶇疆
     */
    private BigDecimal positionY;

    /**
     * 鑺傜偣鏁版嵁锛圝SON鏍煎紡锛屽寘鍚玦nputs銆乷utputs銆乮nputsValues绛夛級
     */
    private String nodeData;

    /**
     * 寮曠敤鐨勫疄闄呰祫婧怚D锛堝agent_id銆乧lient_id绛夛級
     */
    private String refId;

    /**
     * 寮曠敤鐨勮祫婧愮被鍨嬶紙agent銆乧lient銆乵odel銆乸rompt銆乤dvisor銆乼ool_mcp锛?     */
    private String refType;

    /**
     * 鑺傜偣搴忓彿锛堢敤浜庢帓搴忥級
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
