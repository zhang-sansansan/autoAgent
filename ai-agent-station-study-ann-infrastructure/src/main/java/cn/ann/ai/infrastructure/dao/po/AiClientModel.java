package cn.ann.ai.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 鑱婂ぉ妯″瀷閰嶇疆琛? * @author bugstack铏礊鏍? * @description 鑱婂ぉ妯″瀷閰嶇疆琛?PO 瀵硅薄
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientModel {

    /**
     * 鑷涓婚敭ID
     */
    private Long id;

    /**
     * 鍏ㄥ眬鍞竴妯″瀷ID
     */
    private String modelId;

    /**
     * 鍏宠仈鐨凙PI閰嶇疆ID
     */
    private String apiId;

    /**
     * 妯″瀷鍚嶇О
     */
    private String modelName;

    /**
     * 妯″瀷绫诲瀷锛歰penai銆乨eepseek銆乧laude
     */
    private String modelType;

    /**
     * 妯″瀷鐢ㄩ€?     */
    private String modelUsage;

    /**
     * 鐘舵€侊細0-绂佺敤锛?-鍚敤
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
