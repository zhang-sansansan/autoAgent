package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 绯荤粺鎻愮ず璇嶉厤缃煡璇㈣姹?DTO
 *
 * @author bugstack铏礊鏍? * @description 绯荤粺鎻愮ず璇嶉厤缃煡璇㈣姹傛暟鎹紶杈撳璞? */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientSystemPromptQueryRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 鎻愮ず璇岻D
     */
    private String promptId;

    /**
     * 鎻愮ず璇嶅悕绉帮紙妯＄硦鏌ヨ锛?     */
    private String promptName;

    /**
     * 鐘舵€?0:绂佺敤,1:鍚敤)
     */
    private Integer status;

    /**
     * 椤电爜锛堝垎椤垫煡璇級
     */
    private Integer pageNum;

    /**
     * 椤靛ぇ灏忥紙鍒嗛〉鏌ヨ锛?     */
    private Integer pageSize;

}
