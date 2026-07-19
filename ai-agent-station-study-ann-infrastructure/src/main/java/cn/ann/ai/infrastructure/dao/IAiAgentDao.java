package cn.ann.ai.infrastructure.dao;


import cn.ann.ai.infrastructure.dao.po.AiAgent;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * AI鏅鸿兘浣撻厤缃〃 DAO
 * @author bugstack铏礊鏍? * @description AI鏅鸿兘浣撻厤缃〃鏁版嵁璁块棶瀵硅薄
 */
@Mapper
public interface IAiAgentDao {

    /**
     * 鎻掑叆AI鏅鸿兘浣撻厤缃?     * @param aiAgent AI鏅鸿兘浣撻厤缃璞?     * @return 褰卞搷琛屾暟
     */
    int insert(AiAgent aiAgent);

    /**
     * 鏍规嵁ID鏇存柊AI鏅鸿兘浣撻厤缃?     * @param aiAgent AI鏅鸿兘浣撻厤缃璞?     * @return 褰卞搷琛屾暟
     */
    int updateById(AiAgent aiAgent);

    /**
     * 鏍规嵁鏅鸿兘浣揑D鏇存柊AI鏅鸿兘浣撻厤缃?     * @param aiAgent AI鏅鸿兘浣撻厤缃璞?     * @return 褰卞搷琛屾暟
     */
    int updateByAgentId(AiAgent aiAgent);

    /**
     * 鏍规嵁ID鍒犻櫎AI鏅鸿兘浣撻厤缃?     * @param id 涓婚敭ID
     * @return 褰卞搷琛屾暟
     */
    int deleteById(Long id);

    /**
     * 鏍规嵁鏅鸿兘浣揑D鍒犻櫎AI鏅鸿兘浣撻厤缃?     * @param agentId 鏅鸿兘浣揑D
     * @return 褰卞搷琛屾暟
     */
    int deleteByAgentId(String agentId);

    /**
     * 鏍规嵁ID鏌ヨAI鏅鸿兘浣撻厤缃?     * @param id 涓婚敭ID
     * @return AI鏅鸿兘浣撻厤缃璞?     */
    AiAgent queryById(Long id);

    /**
     * 鏍规嵁鏅鸿兘浣揑D鏌ヨAI鏅鸿兘浣撻厤缃?     * @param agentId 鏅鸿兘浣揑D
     * @return AI鏅鸿兘浣撻厤缃璞?     */
    AiAgent queryByAgentId(String agentId);

    /**
     * 鏌ヨ鎵€鏈夊惎鐢ㄧ殑AI鏅鸿兘浣撻厤缃?     * @return AI鏅鸿兘浣撻厤缃垪琛?     */
    List<AiAgent> queryEnabledAgents();

    /**
     * 鏍规嵁娓犻亾绫诲瀷鏌ヨAI鏅鸿兘浣撻厤缃?     * @param channel 娓犻亾绫诲瀷
     * @return AI鏅鸿兘浣撻厤缃垪琛?     */
    List<AiAgent> queryByChannel(String channel);

    /**
     * 鏌ヨ鎵€鏈堿I鏅鸿兘浣撻厤缃?     * @return AI鏅鸿兘浣撻厤缃垪琛?     */
    List<AiAgent> queryAll();

}
