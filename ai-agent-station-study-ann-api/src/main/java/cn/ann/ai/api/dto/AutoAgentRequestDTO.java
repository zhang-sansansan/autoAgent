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
     * AI智能体ID
     */
    private String aiAgentId;

    /**
     * 用户消息
     */
    private String message;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 最大执行步数
     */
    private Integer maxStep;
}
