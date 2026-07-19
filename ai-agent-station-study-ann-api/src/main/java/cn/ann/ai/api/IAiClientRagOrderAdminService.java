package cn.ann.ai.api;

import cn.ann.ai.api.dto.AiClientRagOrderQueryRequestDTO;
import cn.ann.ai.api.dto.AiClientRagOrderRequestDTO;
import cn.ann.ai.api.dto.AiClientRagOrderResponseDTO;
import cn.ann.ai.api.response.Response;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 鐭ヨ瘑搴撻厤缃鐞嗘湇鍔℃帴鍙?
 *
 * @author bugstack铏礊鏍?
 * @description 鐭ヨ瘑搴撻厤缃鐞嗘湇鍔℃帴鍙?
 */
public interface IAiClientRagOrderAdminService {

    /**
     * 鍒涘缓鐭ヨ瘑搴撻厤缃?
     * @param request 鐭ヨ瘑搴撻厤缃姹傚璞?
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> createAiClientRagOrder(AiClientRagOrderRequestDTO request);

    /**
     * 鏍规嵁ID鏇存柊鐭ヨ瘑搴撻厤缃?
     * @param request 鐭ヨ瘑搴撻厤缃姹傚璞?
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> updateAiClientRagOrderById(AiClientRagOrderRequestDTO request);

    /**
     * 鏍规嵁鐭ヨ瘑搴揑D鏇存柊鐭ヨ瘑搴撻厤缃?
     * @param request 鐭ヨ瘑搴撻厤缃姹傚璞?
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> updateAiClientRagOrderByRagId(AiClientRagOrderRequestDTO request);

    /**
     * 鏍规嵁ID鍒犻櫎鐭ヨ瘑搴撻厤缃?
     * @param id 涓婚敭ID
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> deleteAiClientRagOrderById(Long id);

    /**
     * 鏍规嵁鐭ヨ瘑搴揑D鍒犻櫎鐭ヨ瘑搴撻厤缃?
     * @param ragId 鐭ヨ瘑搴揑D
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> deleteAiClientRagOrderByRagId(String ragId);

    /**
     * 鏍规嵁ID鏌ヨ鐭ヨ瘑搴撻厤缃?
     * @param id 涓婚敭ID
     * @return 鐭ヨ瘑搴撻厤缃璞?
     */
    Response<AiClientRagOrderResponseDTO> queryAiClientRagOrderById(Long id);

    /**
     * 鏍规嵁鐭ヨ瘑搴揑D鏌ヨ鐭ヨ瘑搴撻厤缃?
     * @param ragId 鐭ヨ瘑搴揑D
     * @return 鐭ヨ瘑搴撻厤缃璞?
     */
    Response<AiClientRagOrderResponseDTO> queryAiClientRagOrderByRagId(String ragId);

    /**
     * 鏌ヨ鎵€鏈夊惎鐢ㄧ殑鐭ヨ瘑搴撻厤缃?
     * @return 鐭ヨ瘑搴撻厤缃垪琛?
     */
    Response<List<AiClientRagOrderResponseDTO>> queryEnabledAiClientRagOrders();

    /**
     * 鏍规嵁鐭ヨ瘑鏍囩鏌ヨ鐭ヨ瘑搴撻厤缃?
     * @param knowledgeTag 鐭ヨ瘑鏍囩
     * @return 鐭ヨ瘑搴撻厤缃垪琛?
     */
    Response<List<AiClientRagOrderResponseDTO>> queryAiClientRagOrdersByKnowledgeTag(String knowledgeTag);

    /**
     * 鏍规嵁鐘舵€佹煡璇㈢煡璇嗗簱閰嶇疆
     * @param status 鐘舵€?
     * @return 鐭ヨ瘑搴撻厤缃垪琛?
     */
    Response<List<AiClientRagOrderResponseDTO>> queryAiClientRagOrdersByStatus(Integer status);

    /**
     * 鍒嗛〉鏌ヨ鐭ヨ瘑搴撻厤缃垪琛?
     * @param request 鏌ヨ璇锋眰瀵硅薄
     * @return 鐭ヨ瘑搴撻厤缃垪琛?
     */
    Response<List<AiClientRagOrderResponseDTO>> queryAiClientRagOrderList(AiClientRagOrderQueryRequestDTO request);

    /**
     * 鏌ヨ鎵€鏈夌煡璇嗗簱閰嶇疆
     * @return 鐭ヨ瘑搴撻厤缃垪琛?
     */
    Response<List<AiClientRagOrderResponseDTO>> queryAllAiClientRagOrders();

    /**
     * 涓婁紶鐭ヨ瘑搴撴枃浠?
     * @param name 鐭ヨ瘑搴撳悕绉?
     * @param tag 鐭ヨ瘑搴撴爣绛?
     * @param files 涓婁紶鐨勬枃浠跺垪琛?
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> uploadRagFile(String name, String tag, List<MultipartFile> files);

}
