package cn.ann.ai.trigger.http;

import cn.ann.ai.api.IAiAgentService;
import cn.ann.ai.api.dto.AiAgentResponseDTO;
import cn.ann.ai.api.dto.ArmoryAgentRequestDTO;
import cn.ann.ai.api.dto.AutoAgentRequestDTO;
import cn.ann.ai.api.response.Response;
import cn.ann.ai.domain.agent.model.entity.ExecuteCommandEntity;
import cn.ann.ai.domain.agent.model.valobj.AiAgentVO;
import cn.ann.ai.domain.agent.service.IAgentDispatchService;
import cn.ann.ai.domain.agent.service.IArmoryService;
import cn.ann.ai.domain.agent.service.armory.node.factory.DefaultArmoryStrategyFactory;
import cn.ann.ai.types.common.Constants;
import cn.ann.ai.types.enums.ResponseCode;
import com.alibaba.fastjson.JSON;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * AutoAgent 鑷姩鏅鸿兘瀵硅瘽浣?
 *
 * @author xiaofuge bugstack.cn @灏忓倕鍝?
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/agent")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class AiAgentController implements IAiAgentService {

    @Resource
    private IAgentDispatchService agentDispatchService;

    @Resource
    private IArmoryService armoryService;

    @RequestMapping(value = "auto_agent", method = RequestMethod.POST)
    public ResponseBodyEmitter autoAgent(@RequestBody AutoAgentRequestDTO request, HttpServletResponse response) {
        log.info("AutoAgent娴佸紡鎵ц璇锋眰寮€濮嬶紝璇锋眰淇℃伅锛歿}", JSON.toJSONString(request));

        try {
            // 璁剧疆SSE鍝嶅簲澶?
            response.setContentType("text/event-stream");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Connection", "keep-alive");

            // 1. 鍒涘缓娴佸紡杈撳嚭瀵硅薄
            ResponseBodyEmitter emitter = new ResponseBodyEmitter(Long.MAX_VALUE);
            if (request == null || request.getAiAgentId() == null || request.getAiAgentId().trim().isEmpty()) {
                log.warn("AutoAgent璇锋眰鍙傛暟鏃犳晥锛歛iAgentId涓虹┖");
                emitter.send("璇锋眰澶勭悊寮傚父锛歛iAgentId涓嶈兘涓虹┖");
                emitter.complete();
                return emitter;
            }

            // 2. 鏋勫缓鎵ц鍛戒护瀹炰綋
            ExecuteCommandEntity executeCommandEntity = ExecuteCommandEntity.builder()
                    .aiAgentId(request.getAiAgentId())
                    .message(request.getMessage())
                    .sessionId(request.getSessionId())
                    .maxStep(request.getMaxStep())
                    .build();

            // 3. 璋冨害澶勭悊
            agentDispatchService.dispatch(executeCommandEntity, emitter);

            return emitter;

        } catch (Exception e) {
            log.error("AutoAgent璇锋眰澶勭悊寮傚父锛歿}", e.getMessage(), e);
            ResponseBodyEmitter errorEmitter = new ResponseBodyEmitter();
            try {
                errorEmitter.send("璇锋眰澶勭悊寮傚父锛? + e.getMessage());
                errorEmitter.complete();
            } catch (Exception ex) {
                log.error("鍙戦€侀敊璇俊鎭け璐ワ細{}", ex.getMessage(), ex);
            }
            return errorEmitter;
        }
    }

    @RequestMapping(value = "armory_agent", method = RequestMethod.POST)
    @Override
    public Response<Boolean> armoryAgent(@RequestBody ArmoryAgentRequestDTO request) {
        log.info("瑁呴厤鏅鸿兘浣撹姹傚紑濮嬶紝璇锋眰淇℃伅锛歿}", JSON.toJSONString(request));

        try {
            // 鍙傛暟鏍￠獙
            if (request == null || request.getAgentId() == null || request.getAgentId().trim().isEmpty()) {
                log.warn("瑁呴厤鏅鸿兘浣撹姹傚弬鏁版棤鏁堬細agentId涓虹┖");
                return Response.<Boolean>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info("agentId涓嶈兘涓虹┖")
                        .data(false)
                        .build();
            }
            
            // 璋冪敤瑁呴厤鏈嶅姟
            armoryService.acceptArmoryAgent(request.getAgentId());
            
            log.info("瑁呴厤鏅鸿兘浣撴垚鍔燂紝agentId锛歿}", request.getAgentId());
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info("瑁呴厤鎴愬姛")
                    .data(true)
                    .build();
                    
        } catch (Exception e) {
            log.error("瑁呴厤鏅鸿兘浣撳け璐ワ紝agentId锛歿}锛岄敊璇俊鎭細{}", 
                    request != null ? request.getAgentId() : "null", e.getMessage(), e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info("瑁呴厤澶辫触锛? + e.getMessage())
                    .data(false)
                    .build();
        }
    }

    @RequestMapping(value = "query_available_agents", method = RequestMethod.GET)
    @Override
    public Response<List<AiAgentResponseDTO>> queryAvailableAgents() {
        log.info("鏌ヨ鍙敤鏅鸿兘浣撳垪琛ㄨ姹傚紑濮?);

        try {
            // 璋冪敤瑁呴厤鏈嶅姟鏌ヨ鍙敤鏅鸿兘浣?
            List<AiAgentVO> aiAgentVOList = armoryService.queryAvailableAgents();
            
            // 杞崲涓哄搷搴擠TO
            List<AiAgentResponseDTO> responseList = new ArrayList<>();
            for (AiAgentVO aiAgentVO : aiAgentVOList) {
                AiAgentResponseDTO responseDTO = AiAgentResponseDTO.builder()
                        .agentId(aiAgentVO.getAgentId())
                        .agentName(aiAgentVO.getAgentName())
                        .description(aiAgentVO.getDescription())
                        .channel(aiAgentVO.getChannel())
                        .strategy(aiAgentVO.getStrategy())
                        .status(aiAgentVO.getStatus())
                        .build();
                responseList.add(responseDTO);
            }
            
            log.info("鏌ヨ鍙敤鏅鸿兘浣撳垪琛ㄦ垚鍔燂紝鍏眥}涓櫤鑳戒綋", responseList.size());
            return Response.<List<AiAgentResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info("鏌ヨ鎴愬姛")
                    .data(responseList)
                    .build();
                    
        } catch (Exception e) {
            log.error("鏌ヨ鍙敤鏅鸿兘浣撳垪琛ㄥけ璐ワ紝閿欒淇℃伅锛歿}", e.getMessage(), e);
            return Response.<List<AiAgentResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info("鏌ヨ澶辫触锛? + e.getMessage())
                    .data(new ArrayList<>())
                    .build();
        }
    }

}

