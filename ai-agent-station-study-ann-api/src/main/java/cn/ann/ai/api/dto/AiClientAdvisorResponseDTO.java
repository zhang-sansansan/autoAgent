package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 椤鹃棶閰嶇疆鍝嶅簲 DTO
 *
 * @author bugstack铏礊鏍? * @description 椤鹃棶閰嶇疆鍝嶅簲鏁版嵁浼犺緭瀵硅薄
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientAdvisorResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 涓婚敭ID
     */
    private Long id;

    /**
     * 椤鹃棶ID
     */
    private String advisorId;

    /**
     * 椤鹃棶鍚嶇О
     */
    private String advisorName;

    /**
     * 椤鹃棶绫诲瀷(PromptChatMemory/RagAnswer/SimpleLoggerAdvisor绛?
     */
    private String advisorType;

    /**
     * 椤哄簭鍙?     */
    private Integer orderNum;

    /**
     * 鎵╁睍鍙傛暟閰嶇疆锛宩son 璁板綍
     */
    private String extParam;

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
