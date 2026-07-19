package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 绠＄悊鍛樼敤鎴锋煡璇㈣姹?DTO
 *
 * @author bugstack铏礊鏍? * @description 绠＄悊鍛樼敤鎴锋煡璇㈣姹傛暟鎹紶杈撳璞? */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserQueryRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 鐢ㄦ埛ID
     */
    private String userId;

    /**
     * 鐢ㄦ埛鍚嶏紙妯＄硦鏌ヨ锛?     */
    private String username;

    /**
     * 鐘舵€?0:绂佺敤,1:鍚敤,2:閿佸畾)
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
