package cn.ann.ai.api;

import cn.ann.ai.api.dto.AiAgentResponseDTO;
import cn.ann.ai.api.dto.ArmoryAgentRequestDTO;
import cn.ann.ai.api.dto.AutoAgentRequestDTO;
import cn.ann.ai.api.response.Response;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

public interface IAiAgentService {

    ResponseBodyEmitter autoAgent(AutoAgentRequestDTO request, HttpServletResponse response);

    /**
     * 装配智能体
     */
    Response<Boolean> armoryAgent(ArmoryAgentRequestDTO request);

    /**
     * 查询可用的智能体列表
     */
    Response<List<AiAgentResponseDTO>> queryAvailableAgents();
}
