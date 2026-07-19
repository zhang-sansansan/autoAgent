package cn.ann.ai.infrastructure.dao;

import cn.ann.ai.infrastructure.dao.po.AiClientToolMcp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * MCP瀹㈡埛绔厤缃〃 DAO
 * @author bugstack铏礊鏍? * @description MCP瀹㈡埛绔厤缃〃鏁版嵁璁块棶瀵硅薄
 */
@Mapper
public interface IAiClientToolMcpDao {

    /**
     * 鎻掑叆MCP瀹㈡埛绔厤缃?     * @param aiClientToolMcp MCP瀹㈡埛绔厤缃璞?     * @return 褰卞搷琛屾暟
     */
    int insert(AiClientToolMcp aiClientToolMcp);

    /**
     * 鏍规嵁ID鏇存柊MCP瀹㈡埛绔厤缃?     * @param aiClientToolMcp MCP瀹㈡埛绔厤缃璞?     * @return 褰卞搷琛屾暟
     */
    int updateById(AiClientToolMcp aiClientToolMcp);

    /**
     * 鏍规嵁MCP ID鏇存柊MCP瀹㈡埛绔厤缃?     * @param aiClientToolMcp MCP瀹㈡埛绔厤缃璞?     * @return 褰卞搷琛屾暟
     */
    int updateByMcpId(AiClientToolMcp aiClientToolMcp);

    /**
     * 鏍规嵁ID鍒犻櫎MCP瀹㈡埛绔厤缃?     * @param id 涓婚敭ID
     * @return 褰卞搷琛屾暟
     */
    int deleteById(Long id);

    /**
     * 鏍规嵁MCP ID鍒犻櫎MCP瀹㈡埛绔厤缃?     * @param mcpId MCP ID
     * @return 褰卞搷琛屾暟
     */
    int deleteByMcpId(String mcpId);

    /**
     * 鏍规嵁ID鏌ヨMCP瀹㈡埛绔厤缃?     * @param id 涓婚敭ID
     * @return MCP瀹㈡埛绔厤缃璞?     */
    AiClientToolMcp queryById(Long id);

    /**
     * 鏍规嵁MCP ID鏌ヨMCP瀹㈡埛绔厤缃?     * @param mcpId MCP ID
     * @return MCP瀹㈡埛绔厤缃璞?     */
    AiClientToolMcp queryByMcpId(String mcpId);

    /**
     * 鏌ヨ鎵€鏈塎CP瀹㈡埛绔厤缃?     * @return MCP瀹㈡埛绔厤缃垪琛?     */
    List<AiClientToolMcp> queryAll();

    /**
     * 鏍规嵁鐘舵€佹煡璇CP瀹㈡埛绔厤缃?     * @param status 鐘舵€?     * @return MCP瀹㈡埛绔厤缃垪琛?     */
    List<AiClientToolMcp> queryByStatus(Integer status);

    /**
     * 鏍规嵁浼犺緭绫诲瀷鏌ヨMCP瀹㈡埛绔厤缃?     * @param transportType 浼犺緭绫诲瀷
     * @return MCP瀹㈡埛绔厤缃垪琛?     */
    List<AiClientToolMcp> queryByTransportType(String transportType);

    /**
     * 鏌ヨ鍚敤鐨凪CP瀹㈡埛绔厤缃?     * @return MCP瀹㈡埛绔厤缃垪琛?     */
    List<AiClientToolMcp> queryEnabledMcps();

}
