package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 绠＄悊鍛樼敤鎴疯姹?DTO
 *
 * @author bugstack铏礊鏍? * @description 绠＄悊鍛樼敤鎴疯姹傛暟鎹紶杈撳璞? */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 涓婚敭ID锛堟洿鏂版椂浣跨敤锛?     */
    private Long id;

    /**
     * 鐢ㄦ埛ID锛堝敮涓€鏍囪瘑锛?     */
    private String userId;

    /**
     * 鐢ㄦ埛鍚嶏紙鐧诲綍璐﹀彿锛?     */
    private String username;

    /**
     * 瀵嗙爜锛堝姞瀵嗗瓨鍌級
     */
    private String password;

    /**
     * 鐘舵€?0:绂佺敤,1:鍚敤,2:閿佸畾)
     */
    private Integer status;

}
