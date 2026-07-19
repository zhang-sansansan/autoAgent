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
     * 瑁呴厤鏅鸿兘浣?
     */
    Response<Boolean> armoryAgent(ArmoryAgentRequestDTO request);

    /**
     * 鏌ヨ鍙敤鐨勬櫤鑳戒綋鍒楄〃
     */
    Response<List<AiAgentResponseDTO>> queryAvailableAgents();
}

