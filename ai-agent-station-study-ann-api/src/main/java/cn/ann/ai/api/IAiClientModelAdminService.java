package cn.ann.ai.api;

import cn.ann.ai.api.dto.AiClientModelQueryRequestDTO;
import cn.ann.ai.api.dto.AiClientModelRequestDTO;
import cn.ann.ai.api.dto.AiClientModelResponseDTO;
import cn.ann.ai.api.response.Response;

import java.util.List;

/**
 * AI瀹㈡埛绔ā鍨嬬鐞嗘湇鍔℃帴鍙? *
 * @author bugstack铏礊鏍? * @description AI瀹㈡埛绔ā鍨嬮厤缃鐞嗘湇鍔℃帴鍙? */
public interface IAiClientModelAdminService {

    /**
     * 鍒涘缓AI瀹㈡埛绔ā鍨嬮厤缃?     * @param request AI瀹㈡埛绔ā鍨嬮厤缃姹傚璞?     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> createAiClientModel(AiClientModelRequestDTO request);

    /**
     * 鏍规嵁ID鏇存柊AI瀹㈡埛绔ā鍨嬮厤缃?     * @param request AI瀹㈡埛绔ā鍨嬮厤缃姹傚璞?     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> updateAiClientModelById(AiClientModelRequestDTO request);

    /**
     * 鏍规嵁妯″瀷ID鏇存柊AI瀹㈡埛绔ā鍨嬮厤缃?     * @param request AI瀹㈡埛绔ā鍨嬮厤缃姹傚璞?     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> updateAiClientModelByModelId(AiClientModelRequestDTO request);

    /**
     * 鏍规嵁ID鍒犻櫎AI瀹㈡埛绔ā鍨嬮厤缃?     * @param id 涓婚敭ID
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> deleteAiClientModelById(Long id);

    /**
     * 鏍规嵁妯″瀷ID鍒犻櫎AI瀹㈡埛绔ā鍨嬮厤缃?     * @param modelId 妯″瀷ID
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> deleteAiClientModelByModelId(String modelId);

    /**
     * 鏍规嵁ID鏌ヨAI瀹㈡埛绔ā鍨嬮厤缃?     * @param id 涓婚敭ID
     * @return AI瀹㈡埛绔ā鍨嬮厤缃璞?     */
    Response<AiClientModelResponseDTO> queryAiClientModelById(Long id);

    /**
     * 鏍规嵁妯″瀷ID鏌ヨAI瀹㈡埛绔ā鍨嬮厤缃?     * @param modelId 妯″瀷ID
     * @return AI瀹㈡埛绔ā鍨嬮厤缃璞?     */
    Response<AiClientModelResponseDTO> queryAiClientModelByModelId(String modelId);

    /**
     * 鏍规嵁API閰嶇疆ID鏌ヨAI瀹㈡埛绔ā鍨嬮厤缃垪琛?     * @param apiId API閰嶇疆ID
     * @return AI瀹㈡埛绔ā鍨嬮厤缃垪琛?     */
    Response<List<AiClientModelResponseDTO>> queryAiClientModelsByApiId(String apiId);

    /**
     * 鏍规嵁妯″瀷绫诲瀷鏌ヨAI瀹㈡埛绔ā鍨嬮厤缃垪琛?     * @param modelType 妯″瀷绫诲瀷
     * @return AI瀹㈡埛绔ā鍨嬮厤缃垪琛?     */
    Response<List<AiClientModelResponseDTO>> queryAiClientModelsByModelType(String modelType);

    /**
     * 鏌ヨ鎵€鏈夊惎鐢ㄧ殑AI瀹㈡埛绔ā鍨嬮厤缃?     * @return AI瀹㈡埛绔ā鍨嬮厤缃垪琛?     */
    Response<List<AiClientModelResponseDTO>> queryEnabledAiClientModels();

    /**
     * 鏍规嵁鏉′欢鏌ヨAI瀹㈡埛绔ā鍨嬮厤缃垪琛?     * @param request 鏌ヨ鏉′欢
     * @return AI瀹㈡埛绔ā鍨嬮厤缃垪琛?     */
    Response<List<AiClientModelResponseDTO>> queryAiClientModelList(AiClientModelQueryRequestDTO request);

    /**
     * 鏌ヨ鎵€鏈堿I瀹㈡埛绔ā鍨嬮厤缃?     * @return AI瀹㈡埛绔ā鍨嬮厤缃垪琛?     */
    Response<List<AiClientModelResponseDTO>> queryAllAiClientModels();

}
