package cn.ann.ai.infrastructure.dao;

import cn.ann.ai.infrastructure.dao.po.AiAgentTaskSchedule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 鏅鸿兘浣撲换鍔¤皟搴﹂厤缃〃 DAO
 * @author bugstack铏礊鏍? * @description 鏅鸿兘浣撲换鍔¤皟搴﹂厤缃〃鏁版嵁璁块棶瀵硅薄
 */
@Mapper
public interface IAiAgentTaskScheduleDao {

    /**
     * 鎻掑叆鏅鸿兘浣撲换鍔¤皟搴﹂厤缃?     * @param aiAgentTaskSchedule 鏅鸿兘浣撲换鍔¤皟搴﹂厤缃璞?     * @return 褰卞搷琛屾暟
     */
    int insert(AiAgentTaskSchedule aiAgentTaskSchedule);

    /**
     * 鏍规嵁ID鏇存柊鏅鸿兘浣撲换鍔¤皟搴﹂厤缃?     * @param aiAgentTaskSchedule 鏅鸿兘浣撲换鍔¤皟搴﹂厤缃璞?     * @return 褰卞搷琛屾暟
     */
    int updateById(AiAgentTaskSchedule aiAgentTaskSchedule);

    /**
     * 鏍规嵁ID鍒犻櫎鏅鸿兘浣撲换鍔¤皟搴﹂厤缃?     * @param id 涓婚敭ID
     * @return 褰卞搷琛屾暟
     */
    int deleteById(Long id);

    /**
     * 鏍规嵁鏅鸿兘浣揑D鍒犻櫎浠诲姟璋冨害閰嶇疆
     * @param agentId 鏅鸿兘浣揑D
     * @return 褰卞搷琛屾暟
     */
    int deleteByAgentId(Long agentId);

    /**
     * 鏍规嵁ID鏌ヨ鏅鸿兘浣撲换鍔¤皟搴﹂厤缃?     * @param id 涓婚敭ID
     * @return 鏅鸿兘浣撲换鍔¤皟搴﹂厤缃璞?     */
    AiAgentTaskSchedule queryById(Long id);

    /**
     * 鏍规嵁鏅鸿兘浣揑D鏌ヨ浠诲姟璋冨害閰嶇疆鍒楄〃
     * @param agentId 鏅鸿兘浣揑D
     * @return 鏅鸿兘浣撲换鍔¤皟搴﹂厤缃垪琛?     */
    List<AiAgentTaskSchedule> queryByAgentId(Long agentId);

    /**
     * 鏌ヨ鎵€鏈夋湁鏁堢殑浠诲姟璋冨害閰嶇疆
     * @return 鏅鸿兘浣撲换鍔¤皟搴﹂厤缃垪琛?     */
    List<AiAgentTaskSchedule> queryEnabledTasks();

    /**
     * 鏍规嵁浠诲姟鍚嶇О鏌ヨ浠诲姟璋冨害閰嶇疆
     * @param taskName 浠诲姟鍚嶇О
     * @return 鏅鸿兘浣撲换鍔¤皟搴﹂厤缃璞?     */
    AiAgentTaskSchedule queryByTaskName(String taskName);

    /**
     * 鏌ヨ鎵€鏈夋櫤鑳戒綋浠诲姟璋冨害閰嶇疆
     * @return 鏅鸿兘浣撲换鍔¤皟搴﹂厤缃垪琛?     */
    List<AiAgentTaskSchedule> queryAll();

    /**
     * 鏌ヨ鎵€鏈夋湁鏁堢殑浠诲姟璋冨害閰嶇疆
     * @return 鏅鸿兘浣撲换鍔¤皟搴﹂厤缃垪琛?     */
    List<AiAgentTaskSchedule> queryAllValidTaskSchedule();

    /**
     * 鏌ヨ鎵€鏈夋棤鏁堢殑浠诲姟璋冨害閰嶇疆ID
     * @return 鏃犳晥浠诲姟璋冨害閰嶇疆ID鍒楄〃
     */
    List<Long> queryAllInvalidTaskScheduleIds();

}
