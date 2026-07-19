package cn.ann.ai.domain.agent.service;

import cn.ann.ai.domain.agent.model.valobj.AiAgentTaskScheduleVO;

import java.util.List;

/**
 * 鏅鸿兘浣撴墽琛屼换鍔?
 * @author xiaofuge bugstack.cn @灏忓倕鍝?
 * 2025/9/13 16:08
 */
public interface ITaskService {

    List<AiAgentTaskScheduleVO> queryAllValidTaskSchedule();

    List<Long> queryAllInvalidTaskScheduleIds();

}

