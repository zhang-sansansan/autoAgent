package cn.ann.ai.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 绠＄悊鍛樼敤鎴疯〃
 * @author bugstack铏礊鏍?
 * @description 绠＄悊鍛樼敤鎴疯〃 PO 瀵硅薄
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUser {

    /**
     * 涓婚敭ID
     */
    private Long id;

    /**
     * 鐢ㄦ埛ID锛堝敮涓€鏍囪瘑锛?
     */
    private String userId;

    /**
     * 鐢ㄦ埛鍚嶏紙鐧诲綍璐﹀彿锛?
     */
    private String username;

    /**
     * 瀵嗙爜锛堝姞瀵嗗瓨鍌級
     */
    private String password;

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
