package cn.ann.ai.infrastructure.dao;

import cn.ann.ai.infrastructure.dao.po.AiClient;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * AI瀹㈡埛绔厤缃〃 DAO
 * @author bugstack铏礊鏍? * @description AI瀹㈡埛绔厤缃〃鏁版嵁璁块棶瀵硅薄
 */
@Mapper
public interface IAiClientDao {

    /**
     * 鎻掑叆AI瀹㈡埛绔厤缃?     * @param aiClient AI瀹㈡埛绔厤缃璞?     * @return 褰卞搷琛屾暟
     */
    int insert(AiClient aiClient);

    /**
     * 鏍规嵁ID鏇存柊AI瀹㈡埛绔厤缃?     * @param aiClient AI瀹㈡埛绔厤缃璞?     * @return 褰卞搷琛屾暟
     */
    int updateById(AiClient aiClient);

    /**
     * 鏍规嵁瀹㈡埛绔疘D鏇存柊AI瀹㈡埛绔厤缃?     * @param aiClient AI瀹㈡埛绔厤缃璞?     * @return 褰卞搷琛屾暟
     */
    int updateByClientId(AiClient aiClient);

    /**
     * 鏍规嵁ID鍒犻櫎AI瀹㈡埛绔厤缃?     * @param id 涓婚敭ID
     * @return 褰卞搷琛屾暟
     */
    int deleteById(Long id);

    /**
     * 鏍规嵁瀹㈡埛绔疘D鍒犻櫎AI瀹㈡埛绔厤缃?     * @param clientId 瀹㈡埛绔疘D
     * @return 褰卞搷琛屾暟
     */
    int deleteByClientId(String clientId);

    /**
     * 鏍规嵁ID鏌ヨAI瀹㈡埛绔厤缃?     * @param id 涓婚敭ID
     * @return AI瀹㈡埛绔厤缃璞?     */
    AiClient queryById(Long id);

    /**
     * 鏍规嵁瀹㈡埛绔疘D鏌ヨAI瀹㈡埛绔厤缃?     * @param clientId 瀹㈡埛绔疘D
     * @return AI瀹㈡埛绔厤缃璞?     */
    AiClient queryByClientId(String clientId);

    /**
     * 鏌ヨ鎵€鏈夊惎鐢ㄧ殑AI瀹㈡埛绔厤缃?     * @return AI瀹㈡埛绔厤缃垪琛?     */
    List<AiClient> queryEnabledClients();

    /**
     * 鏍规嵁瀹㈡埛绔悕绉版煡璇I瀹㈡埛绔厤缃?     * @param clientName 瀹㈡埛绔悕绉?     * @return AI瀹㈡埛绔厤缃垪琛?     */
    List<AiClient> queryByClientName(String clientName);

    /**
     * 鏌ヨ鎵€鏈堿I瀹㈡埛绔厤缃?     * @return AI瀹㈡埛绔厤缃垪琛?     */
    List<AiClient> queryAll();

}
