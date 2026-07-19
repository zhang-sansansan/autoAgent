package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 绯荤粺鎻愮ず璇嶉厤缃搷搴?DTO
 *
 * @author bugstack铏礊鏍? * @description 绯荤粺鎻愮ず璇嶉厤缃搷搴旀暟鎹紶杈撳璞? */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientSystemPromptResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 涓婚敭ID
     */
    private Long id;

    /**
     * 鎻愮ず璇岻D
     */
    private String promptId;

    /**
     * 鎻愮ず璇嶅悕绉?     */
    private String promptName;

    /**
     * 鎻愮ず璇嶅唴瀹?     */
    private String promptContent;

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
