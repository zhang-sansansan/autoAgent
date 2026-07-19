package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 鐭ヨ瘑搴撻厤缃姹?DTO
 *
 * @author bugstack铏礊鏍? * @description 鐭ヨ瘑搴撻厤缃姹傛暟鎹紶杈撳璞? */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientRagOrderRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 涓婚敭ID锛堟洿鏂版椂浣跨敤锛?     */
    private Long id;

    /**
     * 鐭ヨ瘑搴揑D
     */
    private String ragId;

    /**
     * 鐭ヨ瘑搴撳悕绉?     */
    private String ragName;

    /**
     * 鐭ヨ瘑鏍囩
     */
    private String knowledgeTag;

    /**
     * 鐘舵€?0:绂佺敤,1:鍚敤)
     */
    private Integer status;

}
