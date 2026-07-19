package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * AI瀹㈡埛绔厤缃搷搴?DTO
 *
 * @author bugstack铏礊鏍? * @description AI瀹㈡埛绔厤缃搷搴旀暟鎹紶杈撳璞? */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 涓婚敭ID
     */
    private Long id;

    /**
     * 瀹㈡埛绔疘D
     */
    private String clientId;

    /**
     * 瀹㈡埛绔悕绉?     */
    private String clientName;

    /**
     * 鎻忚堪
     */
    private String description;

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
