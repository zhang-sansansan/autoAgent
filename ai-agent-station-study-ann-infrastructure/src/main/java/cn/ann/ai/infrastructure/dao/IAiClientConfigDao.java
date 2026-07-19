package cn.ann.ai.infrastructure.dao;

import cn.ann.ai.infrastructure.dao.po.AiClientConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AI瀹㈡埛绔粺涓€鍏宠仈閰嶇疆琛?DAO
 * @author bugstack铏礊鏍? * @description AI瀹㈡埛绔粺涓€鍏宠仈閰嶇疆琛ㄦ暟鎹闂璞? */
@Mapper
public interface IAiClientConfigDao {

    /**
     * 鎻掑叆AI瀹㈡埛绔厤缃?     * @param aiClientConfig AI瀹㈡埛绔厤缃璞?     * @return 褰卞搷琛屾暟
     */
    int insert(AiClientConfig aiClientConfig);

    /**
     * 鏍规嵁ID鏇存柊AI瀹㈡埛绔厤缃?     * @param aiClientConfig AI瀹㈡埛绔厤缃璞?     * @return 褰卞搷琛屾暟
     */
    int updateById(AiClientConfig aiClientConfig);

    /**
     * 鏍规嵁婧怚D鏇存柊AI瀹㈡埛绔厤缃?     * @param aiClientConfig AI瀹㈡埛绔厤缃璞?     * @return 褰卞搷琛屾暟
     */
    int updateBySourceId(AiClientConfig aiClientConfig);

    /**
     * 鏍规嵁ID鍒犻櫎AI瀹㈡埛绔厤缃?     * @param id 涓婚敭ID
     * @return 褰卞搷琛屾暟
     */
    int deleteById(Long id);

    /**
     * 鏍规嵁婧怚D鍒犻櫎AI瀹㈡埛绔厤缃?     * @param sourceId 婧怚D
     * @return 褰卞搷琛屾暟
     */
    int deleteBySourceId(String sourceId);

    int deleteBySourceTypeAndId(@Param("sourceType") String sourceType, @Param("sourceId") String sourceId);

    /**
     * 鏍规嵁ID鏌ヨAI瀹㈡埛绔厤缃?     * @param id 涓婚敭ID
     * @return AI瀹㈡埛绔厤缃璞?     */
    AiClientConfig queryById(Long id);

    /**
     * 鏍规嵁婧怚D鏌ヨAI瀹㈡埛绔厤缃?     * @param sourceId 婧怚D
     * @return AI瀹㈡埛绔厤缃璞″垪琛?     */
    List<AiClientConfig> queryBySourceId(String sourceId);

    /**
     * 鏍规嵁鐩爣ID鏌ヨAI瀹㈡埛绔厤缃?     * @param targetId 鐩爣ID
     * @return AI瀹㈡埛绔厤缃璞″垪琛?     */
    List<AiClientConfig> queryByTargetId(String targetId);

    /**
     * 鏍规嵁婧愮被鍨嬪拰婧怚D鏌ヨAI瀹㈡埛绔厤缃?     * @param sourceType 婧愮被鍨?     * @param sourceId 婧怚D
     * @return AI瀹㈡埛绔厤缃璞″垪琛?     */
    List<AiClientConfig> queryBySourceTypeAndId(@Param("sourceType") String sourceType, @Param("sourceId")String sourceId);

    /**
     * 鏍规嵁鐩爣绫诲瀷鍜岀洰鏍嘔D鏌ヨAI瀹㈡埛绔厤缃?     * @param targetType 鐩爣绫诲瀷
     * @param targetId 鐩爣ID
     * @return AI瀹㈡埛绔厤缃璞″垪琛?     */
    List<AiClientConfig> queryByTargetTypeAndId(@Param("targetType") String targetType, @Param("targetId") String targetId);

    /**
     * 鏍规嵁婧愮被鍨嬨€佹簮ID銆佺洰鏍囩被鍨嬨€佺洰鏍嘔D鏌ヨAI瀹㈡埛绔厤缃?     * @param sourceType 婧愮被鍨?     * @param sourceId 婧怚D
     * @param targetType 鐩爣绫诲瀷
     * @param targetId 鐩爣ID
     * @return AI瀹㈡埛绔厤缃璞″垪琛?     */
    List<AiClientConfig> queryByConditions(@Param("sourceType") String sourceType, 
                                          @Param("sourceId") String sourceId,
                                          @Param("targetType") String targetType, 
                                          @Param("targetId") String targetId);

    /**
     * 鏌ヨ鍚敤鐘舵€佺殑AI瀹㈡埛绔厤缃?     * @return AI瀹㈡埛绔厤缃璞″垪琛?     */
    List<AiClientConfig> queryEnabledConfigs();

    /**
     * 鏌ヨ鎵€鏈堿I瀹㈡埛绔厤缃?     * @return AI瀹㈡埛绔厤缃璞″垪琛?     */
    List<AiClientConfig> queryAll();

}

