package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 椤鹃棶閰嶇疆鏌ヨ璇锋眰 DTO
 *
 * @author bugstack铏礊鏍? * @description 椤鹃棶閰嶇疆鏌ヨ璇锋眰鏁版嵁浼犺緭瀵硅薄
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientAdvisorQueryRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 椤鹃棶ID
     */
    private String advisorId;

    /**
     * 椤鹃棶鍚嶇О锛堟ā绯婃煡璇級
     */
    private String advisorName;

    /**
     * 椤鹃棶绫诲瀷
     */
    private String advisorType;

    /**
     * 鐘舵€?0:绂佺敤,1:鍚敤)
     */
    private Integer status;

    /**
     * 椤电爜锛堜粠1寮€濮嬶級
     */
    private Integer pageNum;

    /**
     * 姣忛〉澶у皬
     */
    private Integer pageSize;

}
