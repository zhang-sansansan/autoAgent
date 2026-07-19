package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * AI瀹㈡埛绔ā鍨嬮厤缃搷搴?DTO
 *
 * @author bugstack铏礊鏍? * @description AI瀹㈡埛绔ā鍨嬮厤缃搷搴旀暟鎹紶杈撳璞? */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientModelResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
