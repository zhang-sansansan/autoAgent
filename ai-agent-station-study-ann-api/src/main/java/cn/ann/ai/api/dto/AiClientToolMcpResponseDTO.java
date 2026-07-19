package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * MCP瀹㈡埛绔厤缃搷搴?DTO
 *
 * @author bugstack铏礊鏍? * @description MCP瀹㈡埛绔厤缃搷搴旀暟鎹紶杈撳璞? */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientToolMcpResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 涓婚敭ID
     */
    private Long id;

    /**
     * MCP ID
     */
    private String mcpId;

    /**
     * MCP鍚嶇О
     */
    private String mcpName;

    /**
     * 浼犺緭绫诲瀷(sse/stdio)
     */
    private String transportType;

    /**
     * 浼犺緭閰嶇疆(sse/stdio)
     */
    private String transportConfig;

    /**
     * 璇锋眰瓒呮椂鏃堕棿(鍒嗛挓)
     */
    private Integer requestTimeout;

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
