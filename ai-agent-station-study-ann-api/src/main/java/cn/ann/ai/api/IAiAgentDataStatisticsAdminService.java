package cn.ann.ai.api;

import cn.ann.ai.api.dto.DataStatisticsResponseDTO;
import cn.ann.ai.api.response.Response;

/**
 * 鏁版嵁缁熻
 * @author xiaofuge bugstack.cn @灏忓倕鍝?
 * 2025/10/4 10:33
 */
public interface IAiAgentDataStatisticsAdminService {

    /**
     * 鑾峰彇绯荤粺鏁版嵁缁熻
     * @return 缁熻鏁版嵁鍝嶅簲
     */
    Response<DataStatisticsResponseDTO> getDataStatistics();
}

