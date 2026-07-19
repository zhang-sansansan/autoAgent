package cn.ann.ai.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AI瀹㈡埛绔疉PI閰嶇疆琛? * @author bugstack铏礊鏍? * @description AI瀹㈡埛绔疉PI閰嶇疆琛?PO 瀵硅薄
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientApi {

    /**
     * 涓婚敭ID
     */
    private Long id;

    /**
     * API ID
     */
    private String apiId;

    /**
     * 鍩虹URL
     */
    private String baseUrl;

    /**
     * API瀵嗛挜
     */
    private String apiKey;

    /**
     * 瀵硅瘽琛ュ叏璺緞
     */
    private String completionsPath;

    /**
     * 宓屽叆鍚戦噺璺緞
     */
    private String embeddingsPath;

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
