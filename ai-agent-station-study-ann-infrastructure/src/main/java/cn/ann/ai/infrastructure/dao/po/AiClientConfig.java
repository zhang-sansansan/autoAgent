package cn.ann.ai.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AI瀹㈡埛绔粺涓€鍏宠仈閰嶇疆琛? * @author bugstack铏礊鏍? * @description AI瀹㈡埛绔粺涓€鍏宠仈閰嶇疆琛?PO 瀵硅薄
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientConfig {

    /**
     * 涓婚敭ID
     */
    private Long id;

    /**
     * 婧愮被鍨嬶紙model銆乧lient锛?     */
    private String sourceType;

    /**
     * 婧怚D锛堝 chatModelId銆乧hatClientId 绛夛級
     */
    private String sourceId;

    /**
     * 鐩爣绫诲瀷锛坢odel銆乧lient锛?     */
    private String targetType;

    /**
     * 鐩爣ID锛堝 openAiApiId銆乧hatModelId銆乻ystemPromptId銆乤dvisorId 绛夛級
     */
    private String targetId;

    /**
     * 鎵╁睍鍙傛暟锛圝SON鏍煎紡锛?     */
    private String extParam;

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
