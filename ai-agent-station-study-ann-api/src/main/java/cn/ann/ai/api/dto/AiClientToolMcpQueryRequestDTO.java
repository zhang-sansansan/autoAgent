package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * MCP瀹㈡埛绔厤缃煡璇㈣姹?DTO
 *
 * @author bugstack铏礊鏍? * @description MCP瀹㈡埛绔厤缃煡璇㈣姹傛暟鎹紶杈撳璞? */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientToolMcpQueryRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * MCP ID
     */
    private String mcpId;

    /**
     * MCP鍚嶇О锛堟ā绯婃煡璇級
     */
    private String mcpName;

    /**
     * 浼犺緭绫诲瀷(sse/stdio)
     */
    private String transportType;

    /**
     * 鐘舵€?0:绂佺敤,1:鍚敤)
     */
    private Integer status;

    /**
     * 椤电爜锛堝垎椤垫煡璇級
     */
    private Integer pageNum;

    /**
     * 姣忛〉澶у皬锛堝垎椤垫煡璇級
     */
    private Integer pageSize;

}
