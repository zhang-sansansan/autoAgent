package cn.ann.ai.trigger.http.admin;

import cn.ann.ai.api.IAiAgentDataStatisticsAdminService;
import cn.ann.ai.api.dto.DataStatisticsResponseDTO;
import cn.ann.ai.api.response.Response;
import cn.ann.ai.infrastructure.dao.*;
import cn.ann.ai.types.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 鏁版嵁缁熻
 *
 * @author xiaofuge bugstack.cn @灏忓倕鍝?
 * 2025/10/4 10:33
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/admin/data/statistics")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AiAgentDataStatisticsAdminController implements IAiAgentDataStatisticsAdminService {

    @Resource
    private IAiAgentDao aiAgentDao;
    @Resource
    private IAiAgentDrawConfigDao aiAgentDrawConfigDao;
    @Resource
    private IAiAgentFlowConfigDao aiAgentFlowConfigDao;
    @Resource
    private IAiAgentTaskScheduleDao aiAgentTaskScheduleDao;
    @Resource
    private IAiClientAdvisorDao aiClientAdvisorDao;
    @Resource
    private IAiClientApiDao aiClientApiDao;
    @Resource
    private IAiClientConfigDao aiClientConfigDao;
    @Resource
    private IAiClientDao aiClientDao;
    @Resource
    private IAiClientModelDao aiClientModelDao;
    @Resource
    private IAiClientRagOrderDao aiClientRagOrderDao;
    @Resource
    private IAiClientSystemPromptDao aiClientSystemPromptDao;
    @Resource
    private IAiClientToolMcpDao aiClientToolMcpDao;

    @Override
    @GetMapping("/get-data-statistics")
    public Response<DataStatisticsResponseDTO> getDataStatistics() {
        try {
            log.info("寮€濮嬭幏鍙栫郴缁熸暟鎹粺璁?);
            
            // 缁熻鍚勭被鏁版嵁鏁伴噺
            long agentCount = (long) aiAgentDao.queryAll().size();
            long clientCount = (long) aiClientDao.queryAll().size();
            long mcpToolCount = (long) aiClientToolMcpDao.queryAll().size();
            long systemPromptCount = (long) aiClientSystemPromptDao.queryAll().size();
            long ragOrderCount = (long) aiClientRagOrderDao.queryAll().size();
            long advisorCount = (long) aiClientAdvisorDao.queryAll().size();
            long modelCount = (long) aiClientModelDao.queryAll().size();
            
            // 鏋勫缓鍝嶅簲鏁版嵁
            DataStatisticsResponseDTO responseDTO = DataStatisticsResponseDTO.builder()
                    .activeAgentCount(agentCount)
                    .clientCount(clientCount)
                    .mcpToolCount(mcpToolCount)
                    .systemPromptCount(systemPromptCount)
                    .ragOrderCount(ragOrderCount)
                    .advisorCount(advisorCount)
                    .modelCount(modelCount)
                    .todayRequestCount(0L) // 鏆傛椂璁句负0锛屽悗缁彲浠ユ坊鍔犺姹傜粺璁″姛鑳?
                    .successRate(95.5) // 鏆傛椂璁句负鍥哄畾鍊硷紝鍚庣画鍙互娣诲姞鎴愬姛鐜囩粺璁″姛鑳?
                    .runningTaskCount(0L) // 鏆傛椂璁句负0锛屽悗缁彲浠ユ坊鍔犱换鍔＄粺璁″姛鑳?
                    .build();
            
            log.info("绯荤粺鏁版嵁缁熻鑾峰彇鎴愬姛锛氭櫤鑳戒綋鏁伴噺={}, 瀹㈡埛绔暟閲?{}, MCP宸ュ叿鏁伴噺={}, 绯荤粺鎻愮ず鏁伴噺={}, 鐭ヨ瘑搴撴暟閲?{}, 椤鹃棶鏁伴噺={}, 妯″瀷鏁伴噺={}", 
                    agentCount, clientCount, mcpToolCount, systemPromptCount, ragOrderCount, advisorCount, modelCount);
            
            return Response.<DataStatisticsResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTO)
                    .build();
                    
        } catch (Exception e) {
            log.error("鑾峰彇绯荤粺鏁版嵁缁熻澶辫触", e);
            return Response.<DataStatisticsResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

}

