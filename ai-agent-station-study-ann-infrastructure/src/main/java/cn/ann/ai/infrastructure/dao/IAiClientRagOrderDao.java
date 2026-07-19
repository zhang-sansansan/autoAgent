package cn.ann.ai.infrastructure.dao;

import cn.ann.ai.infrastructure.dao.po.AiClientRagOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 鐭ヨ瘑搴撻厤缃〃 DAO
 * @author bugstack铏礊鏍? * @description 鐭ヨ瘑搴撻厤缃〃鏁版嵁璁块棶瀵硅薄
 */
@Mapper
public interface IAiClientRagOrderDao {

    /**
     * 鎻掑叆鐭ヨ瘑搴撻厤缃?     * @param aiClientRagOrder 鐭ヨ瘑搴撻厤缃璞?     * @return 褰卞搷琛屾暟
     */
    int insert(AiClientRagOrder aiClientRagOrder);

    /**
     * 鏍规嵁ID鏇存柊鐭ヨ瘑搴撻厤缃?     * @param aiClientRagOrder 鐭ヨ瘑搴撻厤缃璞?     * @return 褰卞搷琛屾暟
     */
    int updateById(AiClientRagOrder aiClientRagOrder);

    /**
     * 鏍规嵁鐭ヨ瘑搴揑D鏇存柊鐭ヨ瘑搴撻厤缃?     * @param aiClientRagOrder 鐭ヨ瘑搴撻厤缃璞?     * @return 褰卞搷琛屾暟
     */
    int updateByRagId(AiClientRagOrder aiClientRagOrder);

    /**
     * 鏍规嵁ID鍒犻櫎鐭ヨ瘑搴撻厤缃?     * @param id 涓婚敭ID
     * @return 褰卞搷琛屾暟
     */
    int deleteById(Long id);

    /**
     * 鏍规嵁鐭ヨ瘑搴揑D鍒犻櫎鐭ヨ瘑搴撻厤缃?     * @param ragId 鐭ヨ瘑搴揑D
     * @return 褰卞搷琛屾暟
     */
    int deleteByRagId(String ragId);

    /**
     * 鏍规嵁ID鏌ヨ鐭ヨ瘑搴撻厤缃?     * @param id 涓婚敭ID
     * @return 鐭ヨ瘑搴撻厤缃璞?     */
    AiClientRagOrder queryById(Long id);

    /**
     * 鏍规嵁鐭ヨ瘑搴揑D鏌ヨ鐭ヨ瘑搴撻厤缃?     * @param ragId 鐭ヨ瘑搴揑D
     * @return 鐭ヨ瘑搴撻厤缃璞?     */
    AiClientRagOrder queryByRagId(String ragId);

    /**
     * 鏌ヨ鍚敤鐨勭煡璇嗗簱閰嶇疆
     * @return 鐭ヨ瘑搴撻厤缃垪琛?     */
    List<AiClientRagOrder> queryEnabledRagOrders();

    /**
     * 鏍规嵁鐭ヨ瘑鏍囩鏌ヨ鐭ヨ瘑搴撻厤缃?     * @param knowledgeTag 鐭ヨ瘑鏍囩
     * @return 鐭ヨ瘑搴撻厤缃垪琛?     */
    List<AiClientRagOrder> queryByKnowledgeTag(String knowledgeTag);

    /**
     * 鏌ヨ鎵€鏈夌煡璇嗗簱閰嶇疆
     * @return 鐭ヨ瘑搴撻厤缃垪琛?     */
    List<AiClientRagOrder> queryAll();

}
