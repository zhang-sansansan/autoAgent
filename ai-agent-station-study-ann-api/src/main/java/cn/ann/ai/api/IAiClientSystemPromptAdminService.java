package cn.ann.ai.api;

import cn.ann.ai.api.dto.AiClientSystemPromptQueryRequestDTO;
import cn.ann.ai.api.dto.AiClientSystemPromptRequestDTO;
import cn.ann.ai.api.dto.AiClientSystemPromptResponseDTO;
import cn.ann.ai.api.response.Response;

import java.util.List;

/**
 * 绯荤粺鎻愮ず璇嶉厤缃鐞嗘湇鍔℃帴鍙? *
 * @author bugstack铏礊鏍? * @description 绯荤粺鎻愮ず璇嶉厤缃鐞嗘湇鍔℃帴鍙? */
public interface IAiClientSystemPromptAdminService {

    /**
     * 鍒涘缓绯荤粺鎻愮ず璇嶉厤缃?     * @param request 绯荤粺鎻愮ず璇嶉厤缃姹傚璞?     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> createAiClientSystemPrompt(AiClientSystemPromptRequestDTO request);

    /**
     * 鏍规嵁ID鏇存柊绯荤粺鎻愮ず璇嶉厤缃?     * @param request 绯荤粺鎻愮ず璇嶉厤缃姹傚璞?     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> updateAiClientSystemPromptById(AiClientSystemPromptRequestDTO request);

    /**
     * 鏍规嵁鎻愮ず璇岻D鏇存柊绯荤粺鎻愮ず璇嶉厤缃?     * @param request 绯荤粺鎻愮ず璇嶉厤缃姹傚璞?     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> updateAiClientSystemPromptByPromptId(AiClientSystemPromptRequestDTO request);

    /**
     * 鏍规嵁ID鍒犻櫎绯荤粺鎻愮ず璇嶉厤缃?     * @param id 涓婚敭ID
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> deleteAiClientSystemPromptById(Long id);

    /**
     * 鏍规嵁鎻愮ず璇岻D鍒犻櫎绯荤粺鎻愮ず璇嶉厤缃?     * @param promptId 鎻愮ず璇岻D
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> deleteAiClientSystemPromptByPromptId(String promptId);

    /**
     * 鏍规嵁ID鏌ヨ绯荤粺鎻愮ず璇嶉厤缃?     * @param id 涓婚敭ID
     * @return 绯荤粺鎻愮ず璇嶉厤缃俊鎭?     */
    Response<AiClientSystemPromptResponseDTO> queryAiClientSystemPromptById(Long id);

    /**
     * 鏍规嵁鎻愮ず璇岻D鏌ヨ绯荤粺鎻愮ず璇嶉厤缃?     * @param promptId 鎻愮ず璇岻D
     * @return 绯荤粺鎻愮ず璇嶉厤缃俊鎭?     */
    Response<AiClientSystemPromptResponseDTO> queryAiClientSystemPromptByPromptId(String promptId);

    /**
     * 鏌ヨ鎵€鏈夌郴缁熸彁绀鸿瘝閰嶇疆
     * @return 绯荤粺鎻愮ず璇嶉厤缃垪琛?     */
    Response<List<AiClientSystemPromptResponseDTO>> queryAllAiClientSystemPrompts();

    /**
     * 鏌ヨ鍚敤鐨勭郴缁熸彁绀鸿瘝閰嶇疆
     * @return 鍚敤鐨勭郴缁熸彁绀鸿瘝閰嶇疆鍒楄〃
     */
    Response<List<AiClientSystemPromptResponseDTO>> queryEnabledAiClientSystemPrompts();

    /**
     * 鏍规嵁鎻愮ず璇嶅悕绉版煡璇㈢郴缁熸彁绀鸿瘝閰嶇疆
     * @param promptName 鎻愮ず璇嶅悕绉?     * @return 绯荤粺鎻愮ず璇嶉厤缃垪琛?     */
    Response<List<AiClientSystemPromptResponseDTO>> queryAiClientSystemPromptsByPromptName(String promptName);

    /**
     * 鏍规嵁鏉′欢鏌ヨ绯荤粺鎻愮ず璇嶉厤缃垪琛?     * @param request 鏌ヨ璇锋眰瀵硅薄
     * @return 绯荤粺鎻愮ず璇嶉厤缃垪琛?     */
    Response<List<AiClientSystemPromptResponseDTO>> queryAiClientSystemPromptList(AiClientSystemPromptQueryRequestDTO request);

}
