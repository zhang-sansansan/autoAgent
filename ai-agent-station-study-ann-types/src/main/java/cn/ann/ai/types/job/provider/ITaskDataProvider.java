package cn.ann.ai.types.job.provider;

import cn.ann.ai.types.job.model.TaskScheduleVO;

import java.util.List;

/**
 * 浠诲姟鏁版嵁鎻愪緵鑰呮帴鍙ｏ紝鐢ㄦ埛闇€瑕佸疄鐜版鎺ュ彛鏉ユ彁渚涗换鍔¤皟搴︽暟鎹? *
 * @author @灏忓倕鍝? */
public interface ITaskDataProvider {

    /**
     * 鏌ヨ鎵€鏈夋湁鏁堢殑浠诲姟璋冨害閰嶇疆
     * @return 浠诲姟璋冨害閰嶇疆鍒楄〃
     */
    List<TaskScheduleVO> queryAllValidTaskSchedule();
    
    /**
     * 鏌ヨ鎵€鏈夋棤鏁堢殑浠诲姟ID
     * @return 鏃犳晥浠诲姟ID鍒楄〃
     */
    List<Long> queryAllInvalidTaskScheduleIds();

}
