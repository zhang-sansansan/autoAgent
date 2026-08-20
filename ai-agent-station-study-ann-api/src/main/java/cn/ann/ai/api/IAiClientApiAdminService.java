package cn.ann.ai.api;

import cn.ann.ai.api.dto.AiClientApiQueryRequestDTO;
import cn.ann.ai.api.dto.AiClientApiRequestDTO;
import cn.ann.ai.api.dto.AiClientApiResponseDTO;
import cn.ann.ai.api.response.Response;

import java.util.List;

public interface IAiClientApiAdminService {

    Response<Boolean> createAiClientApi(AiClientApiRequestDTO request);

    Response<Boolean> updateAiClientApiById(AiClientApiRequestDTO request);

    Response<Boolean> updateAiClientApiByApiId(AiClientApiRequestDTO request);

    Response<Boolean> deleteAiClientApiById(Long id);

    Response<Boolean> deleteAiClientApiByApiId(String apiId);

    Response<AiClientApiResponseDTO> queryAiClientApiById(Long id);

    Response<AiClientApiResponseDTO> queryAiClientApiByApiId(String apiId);

    Response<List<AiClientApiResponseDTO>> queryEnabledAiClientApis();

    Response<List<AiClientApiResponseDTO>> queryAiClientApiList(AiClientApiQueryRequestDTO request);

    Response<List<AiClientApiResponseDTO>> queryAllAiClientApis();

}
