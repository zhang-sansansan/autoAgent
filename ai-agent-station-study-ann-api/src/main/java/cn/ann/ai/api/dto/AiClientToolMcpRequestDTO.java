package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * MCP瀹㈡埛绔厤缃姹?DTO
 *
 * @author bugstack铏礊鏍? * @description MCP瀹㈡埛绔厤缃姹傛暟鎹紶杈撳璞? */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientToolMcpRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 涓婚敭ID锛堟洿鏂版椂浣跨敤锛?     */
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

}
