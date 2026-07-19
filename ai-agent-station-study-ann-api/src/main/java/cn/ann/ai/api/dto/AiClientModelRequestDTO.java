package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI瀹㈡埛绔ā鍨嬮厤缃姹?DTO
 *
 * @author bugstack铏礊鏍? * @description AI瀹㈡埛绔ā鍨嬮厤缃姹傛暟鎹紶杈撳璞? */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientModelRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 鑷涓婚敭ID锛堟洿鏂版椂浣跨敤锛?     */
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

}
