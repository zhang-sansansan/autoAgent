package cn.ann.ai.api;

import cn.ann.ai.api.dto.AiClientToolMcpQueryRequestDTO;
import cn.ann.ai.api.dto.AiClientToolMcpRequestDTO;
import cn.ann.ai.api.dto.AiClientToolMcpResponseDTO;
import cn.ann.ai.api.response.Response;

import java.util.List;

/**
 * MCP瀹㈡埛绔厤缃鐞嗘湇鍔℃帴鍙? *
 * @author bugstack铏礊鏍? * @description MCP瀹㈡埛绔厤缃鐞嗘湇鍔℃帴鍙? */
public interface IAiClientToolMcpAdminService {

    /**
     * 鍒涘缓MCP瀹㈡埛绔厤缃?     * @param request MCP瀹㈡埛绔厤缃姹傚璞?     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> createAiClientToolMcp(AiClientToolMcpRequestDTO request);

    /**
     * 鏍规嵁ID鏇存柊MCP瀹㈡埛绔厤缃?     * @param request MCP瀹㈡埛绔厤缃姹傚璞?     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> updateAiClientToolMcpById(AiClientToolMcpRequestDTO request);

    /**
     * 鏍规嵁MCP ID鏇存柊MCP瀹㈡埛绔厤缃?     * @param request MCP瀹㈡埛绔厤缃姹傚璞?     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> updateAiClientToolMcpByMcpId(AiClientToolMcpRequestDTO request);

    /**
     * 鏍规嵁ID鍒犻櫎MCP瀹㈡埛绔厤缃?     * @param id 涓婚敭ID
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> deleteAiClientToolMcpById(Long id);

    /**
     * 鏍规嵁MCP ID鍒犻櫎MCP瀹㈡埛绔厤缃?     * @param mcpId MCP ID
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> deleteAiClientToolMcpByMcpId(String mcpId);

    /**
     * 鏍规嵁ID鏌ヨMCP瀹㈡埛绔厤缃?     * @param id 涓婚敭ID
     * @return MCP瀹㈡埛绔厤缃璞?     */
    Response<AiClientToolMcpResponseDTO> queryAiClientToolMcpById(Long id);

    /**
     * 鏍规嵁MCP ID鏌ヨMCP瀹㈡埛绔厤缃?     * @param mcpId MCP ID
     * @return MCP瀹㈡埛绔厤缃璞?     */
    Response<AiClientToolMcpResponseDTO> queryAiClientToolMcpByMcpId(String mcpId);

    /**
     * 鏌ヨ鎵€鏈塎CP瀹㈡埛绔厤缃?     * @return MCP瀹㈡埛绔厤缃垪琛?     */
    Response<List<AiClientToolMcpResponseDTO>> queryAllAiClientToolMcps();

    /**
     * 鏍规嵁鐘舵€佹煡璇CP瀹㈡埛绔厤缃?     * @param status 鐘舵€?     * @return MCP瀹㈡埛绔厤缃垪琛?     */
    Response<List<AiClientToolMcpResponseDTO>> queryAiClientToolMcpsByStatus(Integer status);

    /**
     * 鏍规嵁浼犺緭绫诲瀷鏌ヨMCP瀹㈡埛绔厤缃?     * @param transportType 浼犺緭绫诲瀷
     * @return MCP瀹㈡埛绔厤缃垪琛?     */
    Response<List<AiClientToolMcpResponseDTO>> queryAiClientToolMcpsByTransportType(String transportType);

    /**
     * 鏌ヨ鍚敤鐨凪CP瀹㈡埛绔厤缃?     * @return MCP瀹㈡埛绔厤缃垪琛?     */
    Response<List<AiClientToolMcpResponseDTO>> queryEnabledAiClientToolMcps();

    /**
     * 鏍规嵁鏌ヨ鏉′欢鏌ヨMCP瀹㈡埛绔厤缃垪琛?     * @param request 鏌ヨ璇锋眰瀵硅薄
     * @return MCP瀹㈡埛绔厤缃垪琛?     */
    Response<List<AiClientToolMcpResponseDTO>> queryAiClientToolMcpList(AiClientToolMcpQueryRequestDTO request);

}
