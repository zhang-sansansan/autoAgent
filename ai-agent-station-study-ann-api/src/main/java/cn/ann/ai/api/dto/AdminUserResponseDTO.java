package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 绠＄悊鍛樼敤鎴峰搷搴?DTO
 *
 * @author bugstack铏礊鏍? * @description 绠＄悊鍛樼敤鎴峰搷搴旀暟鎹紶杈撳璞? */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 涓婚敭ID
     */
    private Long id;

    /**
     * 鐢ㄦ埛ID锛堝敮涓€鏍囪瘑锛?     */
    private String userId;

    /**
     * 鐢ㄦ埛鍚嶏紙鐧诲綍璐﹀彿锛?     */
    private String username;

    /**
     * 鐘舵€?0:绂佺敤,1:鍚敤,2:閿佸畾)
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
