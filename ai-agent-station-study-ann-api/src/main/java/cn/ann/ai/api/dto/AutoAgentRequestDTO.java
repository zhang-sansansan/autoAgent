package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * @author zhang san
 * @description
 * @create 2026/2/3 15:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AutoAgentRequestDTO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * AI鏅鸿兘浣揑D
     */
    private String aiAgentId;

    /**
     * 鐢ㄦ埛娑堟伅
     */
    private String message;

    /**
     * 浼氳瘽ID
     */
    private String sessionId;

    /**
     * 鏈€澶ф墽琛屾鏁?
     */
    private Integer maxStep;
}

