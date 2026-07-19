package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 绠＄悊鍛樼敤鎴风櫥褰曡姹?DTO
 *
 * @author bugstack铏礊鏍? * @description 绠＄悊鍛樼敤鎴风櫥褰曡姹傛暟鎹紶杈撳璞? */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserLoginRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 鐢ㄦ埛鍚嶏紙鐧诲綍璐﹀彿锛?     */
    private String username;

    /**
     * 瀵嗙爜
     */
    private String password;

}
