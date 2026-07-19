package cn.ann.ai.domain.agent.service;

import cn.ann.ai.domain.agent.model.valobj.AiAgentVO;

import java.util.List;

/**
 * 瑁呴厤鎺ュ彛
 * @author xiaofuge bugstack.cn @灏忓倕鍝? * 2025/10/3 12:48
 */
public interface IArmoryService {

    List<AiAgentVO> acceptArmoryAllAvailableAgents();

    void acceptArmoryAgent(String agentId);

    List<AiAgentVO> queryAvailableAgents();

}

