package cn.ann.ai.infrastructure.dao;

import cn.ann.ai.infrastructure.dao.po.AiAgentDrawConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * AI鏅鸿兘浣撴嫋鎷夋嫿閰嶇疆涓昏〃 DAO
 * @author bugstack铏礊鏍?
 * @description AI鏅鸿兘浣撴嫋鎷夋嫿閰嶇疆涓昏〃鏁版嵁璁块棶瀵硅薄
 */
@Mapper
public interface IAiAgentDrawConfigDao {

    /**
     * 鎻掑叆鎷栨媺鎷介厤缃?
     * @param aiAgentDrawConfig 鎷栨媺鎷介厤缃璞?
     * @return 褰卞搷琛屾暟
     */
    int insert(AiAgentDrawConfig aiAgentDrawConfig);

    /**
     * 鏍规嵁ID鏇存柊鎷栨媺鎷介厤缃?
     * @param aiAgentDrawConfig 鎷栨媺鎷介厤缃璞?
     * @return 褰卞搷琛屾暟
     */
    int updateById(AiAgentDrawConfig aiAgentDrawConfig);

    /**
     * 鏍规嵁閰嶇疆ID鏇存柊鎷栨媺鎷介厤缃?
     * @param aiAgentDrawConfig 鎷栨媺鎷介厤缃璞?
     * @return 褰卞搷琛屾暟
     */
    int updateByConfigId(AiAgentDrawConfig aiAgentDrawConfig);

    /**
     * 鏍规嵁ID鍒犻櫎鎷栨媺鎷介厤缃?
     * @param id 涓婚敭ID
     * @return 褰卞搷琛屾暟
     */
    int deleteById(Long id);

    /**
     * 鏍规嵁閰嶇疆ID鍒犻櫎鎷栨媺鎷介厤缃?
     * @param configId 閰嶇疆ID
     * @return 褰卞搷琛屾暟
     */
    int deleteByConfigId(String configId);

    /**
     * 鏍规嵁ID鏌ヨ鎷栨媺鎷介厤缃?
     * @param id 涓婚敭ID
     * @return 鎷栨媺鎷介厤缃璞?
     */
    AiAgentDrawConfig queryById(Long id);

    /**
     * 鏍规嵁閰嶇疆ID鏌ヨ鎷栨媺鎷介厤缃?
     * @param configId 閰嶇疆ID
     * @return 鎷栨媺鎷介厤缃璞?
     */
    AiAgentDrawConfig queryByConfigId(String configId);

    /**
     * 鏍规嵁鏅鸿兘浣揑D鏌ヨ鎷栨媺鎷介厤缃?
     * @param agentId 鏅鸿兘浣揑D
     * @return 鎷栨媺鎷介厤缃璞?
     */
    AiAgentDrawConfig queryByAgentId(String agentId);

    /**
     * 鏌ヨ鍚敤鐘舵€佺殑鎷栨媺鎷介厤缃垪琛?
     * @return 鎷栨媺鎷介厤缃垪琛?
     */
    List<AiAgentDrawConfig> queryEnabledConfigs();

    /**
     * 鏍规嵁閰嶇疆鍚嶇О妯＄硦鏌ヨ鎷栨媺鎷介厤缃垪琛?
     * @param configName 閰嶇疆鍚嶇О
     * @return 鎷栨媺鎷介厤缃垪琛?
     */
    List<AiAgentDrawConfig> queryByConfigName(String configName);

    /**
     * 鏌ヨ鎵€鏈夋嫋鎷夋嫿閰嶇疆
     * @return 鎷栨媺鎷介厤缃垪琛?
     */
    List<AiAgentDrawConfig> queryAll();

}
