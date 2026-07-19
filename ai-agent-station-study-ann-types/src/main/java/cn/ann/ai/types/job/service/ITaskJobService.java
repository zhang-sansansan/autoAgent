package cn.ann.ai.types.job.service;

import cn.ann.ai.types.job.model.TaskScheduleVO;

/**
 * 浠诲姟璋冨害鏈嶅姟鎺ュ彛
 *
 * @author @灏忓倕鍝? */
public interface ITaskJobService {

    /**
     * 娣诲姞鍗曚釜浠诲姟
     * @param task 浠诲姟璋冨害閰嶇疆
     * @return 鏄惁娣诲姞鎴愬姛
     */
    boolean addTask(TaskScheduleVO task);

    /**
     * 绉婚櫎鍗曚釜浠诲姟
     * @param taskId 浠诲姟ID
     * @return 鏄惁绉婚櫎鎴愬姛
     */
    boolean removeTask(Long taskId);

    /**
     * 鍒锋柊浠诲姟璋冨害閰嶇疆
     */
    void refreshTasks();
    
    /**
     * 娓呯悊鏃犳晥浠诲姟
     */
    void cleanInvalidTasks();
    
    /**
     * 鍋滄鎵€鏈変换鍔?     */
    void stopAllTasks();
    
    /**
     * 鑾峰彇褰撳墠娲昏穬浠诲姟鏁伴噺
     * @return 娲昏穬浠诲姟鏁伴噺
     */
    int getActiveTaskCount();
    
    /**
     * 鍒濆鍖栦换鍔¤皟搴﹂厤缃?     * 鍦ㄦ湇鍔″惎鍔ㄦ椂鍔犺浇鎵€鏈夋湁鏁堢殑浠诲姟璋冨害閰嶇疆
     */
    void initializeTasks();

}
