package cn.ann.ai.api;

import cn.ann.ai.api.dto.AiClientAdvisorQueryRequestDTO;
import cn.ann.ai.api.dto.AiClientAdvisorRequestDTO;
import cn.ann.ai.api.dto.AiClientAdvisorResponseDTO;
import cn.ann.ai.api.response.Response;

import java.util.List;

/**
 * 椤鹃棶閰嶇疆绠＄悊鏈嶅姟鎺ュ彛
 *
 * @author bugstack铏礊鏍? * @description 椤鹃棶閰嶇疆绠＄悊鏈嶅姟鎺ュ彛
 */
public interface IAiClientAdvisorAdminService {

    /**
     * 鍒涘缓椤鹃棶閰嶇疆
     * @param request 椤鹃棶閰嶇疆璇锋眰瀵硅薄
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> createAiClientAdvisor(AiClientAdvisorRequestDTO request);

    /**
     * 鏍规嵁ID鏇存柊椤鹃棶閰嶇疆
     * @param request 椤鹃棶閰嶇疆璇锋眰瀵硅薄
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> updateAiClientAdvisorById(AiClientAdvisorRequestDTO request);

    /**
     * 鏍规嵁椤鹃棶ID鏇存柊椤鹃棶閰嶇疆
     * @param request 椤鹃棶閰嶇疆璇锋眰瀵硅薄
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> updateAiClientAdvisorByAdvisorId(AiClientAdvisorRequestDTO request);

    /**
     * 鏍规嵁ID鍒犻櫎椤鹃棶閰嶇疆
     * @param id 涓婚敭ID
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> deleteAiClientAdvisorById(Long id);

    /**
     * 鏍规嵁椤鹃棶ID鍒犻櫎椤鹃棶閰嶇疆
     * @param advisorId 椤鹃棶ID
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> deleteAiClientAdvisorByAdvisorId(String advisorId);

    /**
     * 鏍规嵁ID鏌ヨ椤鹃棶閰嶇疆
     * @param id 涓婚敭ID
     * @return 椤鹃棶閰嶇疆瀵硅薄
     */
    Response<AiClientAdvisorResponseDTO> queryAiClientAdvisorById(Long id);

    /**
     * 鏍规嵁椤鹃棶ID鏌ヨ椤鹃棶閰嶇疆
     * @param advisorId 椤鹃棶ID
     * @return 椤鹃棶閰嶇疆瀵硅薄
     */
    Response<AiClientAdvisorResponseDTO> queryAiClientAdvisorByAdvisorId(String advisorId);

    /**
     * 鏌ヨ鎵€鏈夊惎鐢ㄧ殑椤鹃棶閰嶇疆
     * @return 椤鹃棶閰嶇疆鍒楄〃
     */
    Response<List<AiClientAdvisorResponseDTO>> queryEnabledAiClientAdvisors();

    /**
     * 鏍规嵁鐘舵€佹煡璇㈤【闂厤缃?     * @param status 鐘舵€?     * @return 椤鹃棶閰嶇疆鍒楄〃
     */
    Response<List<AiClientAdvisorResponseDTO>> queryAiClientAdvisorsByStatus(Integer status);

    /**
     * 鏍规嵁椤鹃棶绫诲瀷鏌ヨ椤鹃棶閰嶇疆
     * @param advisorType 椤鹃棶绫诲瀷
     * @return 椤鹃棶閰嶇疆鍒楄〃
     */
    Response<List<AiClientAdvisorResponseDTO>> queryAiClientAdvisorsByType(String advisorType);

    /**
     * 鏍规嵁鏉′欢鏌ヨ椤鹃棶閰嶇疆鍒楄〃
     * @param request 鏌ヨ鏉′欢
     * @return 椤鹃棶閰嶇疆鍒楄〃
     */
    Response<List<AiClientAdvisorResponseDTO>> queryAiClientAdvisorList(AiClientAdvisorQueryRequestDTO request);

    /**
     * 鏌ヨ鎵€鏈夐【闂厤缃?     * @return 椤鹃棶閰嶇疆鍒楄〃
     */
    Response<List<AiClientAdvisorResponseDTO>> queryAllAiClientAdvisors();

}
