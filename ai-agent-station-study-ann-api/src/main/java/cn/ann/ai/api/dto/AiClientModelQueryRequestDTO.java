package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI瀹㈡埛绔ā鍨嬮厤缃煡璇㈣姹?DTO
 *
 * @author bugstack铏礊鏍? * @description AI瀹㈡埛绔ā鍨嬮厤缃煡璇㈣姹傛暟鎹紶杈撳璞? */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientModelQueryRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 妯″瀷ID
     */
    private String modelId;

    /**
     * API閰嶇疆ID
     */
    private String apiId;

    /**
     * 妯″瀷绫诲瀷锛歰penai銆乨eepseek銆乧laude
     */
    private String modelType;

    /**
     * 鐘舵€侊細0-绂佺敤锛?-鍚敤
     */
    private Integer status;

}
