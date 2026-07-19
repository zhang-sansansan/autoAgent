package cn.ann.ai.infrastructure.dao;

import cn.ann.ai.infrastructure.dao.po.AiAgentFlowConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 鏅鸿兘浣?瀹㈡埛绔叧鑱旇〃 DAO
 * @author bugstack铏礊鏍? * @description 鏅鸿兘浣?瀹㈡埛绔叧鑱旇〃鏁版嵁璁块棶瀵硅薄
 */
@Mapper
public interface IAiAgentFlowConfigDao {

    /**
     * 鎻掑叆鏅鸿兘浣?瀹㈡埛绔叧鑱旈厤缃?     * @param aiAgentFlowConfig 鏅鸿兘浣?瀹㈡埛绔叧鑱旈厤缃璞?     * @return 褰卞搷琛屾暟
     */
    int insert(AiAgentFlowConfig aiAgentFlowConfig);

    /**
     * 鏍规嵁ID鏇存柊鏅鸿兘浣?瀹㈡埛绔叧鑱旈厤缃?     * @param aiAgentFlowConfig 鏅鸿兘浣?瀹㈡埛绔叧鑱旈厤缃璞?     * @return 褰卞搷琛屾暟
     */
    int updateById(AiAgentFlowConfig aiAgentFlowConfig);

    /**
     * 鏍规嵁ID鍒犻櫎鏅鸿兘浣?瀹㈡埛绔叧鑱旈厤缃?     * @param id 涓婚敭ID
     * @return 褰卞搷琛屾暟
     */
    int deleteById(String id);

    /**
     * 鏍规嵁鏅鸿兘浣揑D鍒犻櫎鍏宠仈閰嶇疆
     * @param agentId 鏅鸿兘浣揑D
     * @return 褰卞搷琛屾暟
     */
    int deleteByAgentId(String agentId);

    /**
     * 鏍规嵁ID鏌ヨ鏅鸿兘浣?瀹㈡埛绔叧鑱旈厤缃?     * @param id 涓婚敭ID
     * @return 鏅鸿兘浣?瀹㈡埛绔叧鑱旈厤缃璞?     */
    AiAgentFlowConfig queryById(String id);

    /**
     * 鏍规嵁鏅鸿兘浣揑D鏌ヨ鍏宠仈閰嶇疆鍒楄〃
     * @param agentId 鏅鸿兘浣揑D
     * @return 鏅鸿兘浣?瀹㈡埛绔叧鑱旈厤缃垪琛?     */
    List<AiAgentFlowConfig> queryByAgentId(String agentId);

    /**
     * 鏍规嵁瀹㈡埛绔疘D鏌ヨ鍏宠仈閰嶇疆鍒楄〃
     * @param clientId 瀹㈡埛绔疘D
     * @return 鏅鸿兘浣?瀹㈡埛绔叧鑱旈厤缃垪琛?     */
    List<AiAgentFlowConfig> queryByClientId(String clientId);

    /**
     * 鏍规嵁鏅鸿兘浣揑D鍜屽鎴风ID鏌ヨ鍏宠仈閰嶇疆
     * @param agentId 鏅鸿兘浣揑D
     * @param clientId 瀹㈡埛绔疘D
     * @return 鏅鸿兘浣?瀹㈡埛绔叧鑱旈厤缃璞?     */
    AiAgentFlowConfig queryByAgentIdAndClientId(String agentId, String clientId);

    /**
     * 鏌ヨ鎵€鏈夋櫤鑳戒綋-瀹㈡埛绔叧鑱旈厤缃?     * @return 鏅鸿兘浣?瀹㈡埛绔叧鑱旈厤缃垪琛?     */
    List<AiAgentFlowConfig> queryAll();

}
