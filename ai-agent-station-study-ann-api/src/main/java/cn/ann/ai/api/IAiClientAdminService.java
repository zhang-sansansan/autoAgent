package cn.ann.ai.api;

import cn.ann.ai.api.dto.AiClientQueryRequestDTO;
import cn.ann.ai.api.dto.AiClientRequestDTO;
import cn.ann.ai.api.dto.AiClientResponseDTO;
import cn.ann.ai.api.response.Response;

import java.util.List;

/**
 * AI瀹㈡埛绔鐞嗘湇鍔℃帴鍙? *
 * @author bugstack铏礊鏍? * @description AI瀹㈡埛绔厤缃鐞嗘湇鍔℃帴鍙? */
public interface IAiClientAdminService {

    /**
     * 鍒涘缓AI瀹㈡埛绔厤缃?     * @param request AI瀹㈡埛绔厤缃姹傚璞?     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> createAiClient(AiClientRequestDTO request);

    /**
     * 鏍规嵁ID鏇存柊AI瀹㈡埛绔厤缃?     * @param request AI瀹㈡埛绔厤缃姹傚璞?     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> updateAiClientById(AiClientRequestDTO request);

    /**
     * 鏍规嵁瀹㈡埛绔疘D鏇存柊AI瀹㈡埛绔厤缃?     * @param request AI瀹㈡埛绔厤缃姹傚璞?     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> updateAiClientByClientId(AiClientRequestDTO request);

    /**
     * 鏍规嵁ID鍒犻櫎AI瀹㈡埛绔厤缃?     * @param id 涓婚敭ID
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> deleteAiClientById(Long id);

    /**
     * 鏍规嵁瀹㈡埛绔疘D鍒犻櫎AI瀹㈡埛绔厤缃?     * @param clientId 瀹㈡埛绔疘D
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> deleteAiClientByClientId(String clientId);

    /**
     * 鏍规嵁ID鏌ヨAI瀹㈡埛绔厤缃?     * @param id 涓婚敭ID
     * @return AI瀹㈡埛绔厤缃璞?     */
    Response<AiClientResponseDTO> queryAiClientById(Long id);

    /**
     * 鏍规嵁瀹㈡埛绔疘D鏌ヨAI瀹㈡埛绔厤缃?     * @param clientId 瀹㈡埛绔疘D
     * @return AI瀹㈡埛绔厤缃璞?     */
    Response<AiClientResponseDTO> queryAiClientByClientId(String clientId);

    /**
     * 鏌ヨ鎵€鏈夊惎鐢ㄧ殑AI瀹㈡埛绔厤缃?     * @return AI瀹㈡埛绔厤缃垪琛?     */
    Response<List<AiClientResponseDTO>> queryEnabledAiClients();

    /**
     * 鏍规嵁鏉′欢鏌ヨAI瀹㈡埛绔厤缃垪琛?     * @param request 鏌ヨ鏉′欢
     * @return AI瀹㈡埛绔厤缃垪琛?     */
    Response<List<AiClientResponseDTO>> queryAiClientList(AiClientQueryRequestDTO request);

    /**
     * 鏌ヨ鎵€鏈堿I瀹㈡埛绔厤缃?     * @return AI瀹㈡埛绔厤缃垪琛?     */
    Response<List<AiClientResponseDTO>> queryAllAiClients();

}
