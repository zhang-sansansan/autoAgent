package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 鐭ヨ瘑搴撻厤缃搷搴?DTO
 *
 * @author bugstack铏礊鏍? * @description 鐭ヨ瘑搴撻厤缃搷搴旀暟鎹紶杈撳璞? */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientRagOrderResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 涓婚敭ID
     */
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

    /**
     * 鍒涘缓鏃堕棿
     */
    private LocalDateTime createTime;

    /**
     * 鏇存柊鏃堕棿
     */
    private LocalDateTime updateTime;

}
