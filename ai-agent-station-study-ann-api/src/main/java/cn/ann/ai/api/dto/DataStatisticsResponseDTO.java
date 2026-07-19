package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 鏁版嵁缁熻鍝嶅簲 DTO
 *
 * @author xiaofuge bugstack.cn @灏忓倕鍝? * @description 鏁版嵁缁熻鍝嶅簲鏁版嵁浼犺緭瀵硅薄
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataStatisticsResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 娲昏穬浠ｇ悊鏁伴噺
     */
    private Long activeAgentCount;

    /**
     * 瀹㈡埛绔暟閲?     */
    private Long clientCount;

    /**
     * MCP宸ュ叿鏁伴噺
     */
    private Long mcpToolCount;

    /**
     * 绯荤粺鎻愮ず璇嶆暟閲?     */
    private Long systemPromptCount;

    /**
     * 鐭ヨ瘑搴撴暟閲?     */
    private Long ragOrderCount;

    /**
     * 椤鹃棶閰嶇疆鏁伴噺
     */
    private Long advisorCount;

    /**
     * 妯″瀷閰嶇疆鏁伴噺
     */
    private Long modelCount;

    /**
     * 浠婃棩璇锋眰鏁伴噺锛堟ā鎷熸暟鎹紝瀹為檯椤圭洰涓渶瑕佷粠鏃ュ織鎴栫粺璁¤〃鑾峰彇锛?     */
    private Long todayRequestCount;

    /**
     * 鎴愬姛鐜囷紙妯℃嫙鏁版嵁锛屽疄闄呴」鐩腑闇€瑕佷粠鏃ュ織鎴栫粺璁¤〃璁＄畻锛?     */
    private Double successRate;

    /**
     * 杩愯涓换鍔℃暟閲忥紙妯℃嫙鏁版嵁锛屽疄闄呴」鐩腑闇€瑕佷粠浠诲姟璋冨害琛ㄨ幏鍙栵級
     */
    private Long runningTaskCount;
}
