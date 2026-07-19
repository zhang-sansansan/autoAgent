package cn.ann.ai.trigger.http.admin;

import cn.ann.ai.api.IAiClientApiAdminService;
import cn.ann.ai.api.dto.AiClientApiQueryRequestDTO;
import cn.ann.ai.api.dto.AiClientApiRequestDTO;
import cn.ann.ai.api.dto.AiClientApiResponseDTO;
import cn.ann.ai.api.response.Response;
import cn.ann.ai.infrastructure.dao.IAiClientApiDao;
import cn.ann.ai.infrastructure.dao.po.AiClientApi;
import cn.ann.ai.types.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/ai-client-api")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AiClientApiAdminController implements IAiClientApiAdminService {

    @Resource
    private IAiClientApiDao aiClientApiDao;

    @Override
    @PostMapping("/create")
    public Response<Boolean> createAiClientApi(@RequestBody AiClientApiRequestDTO request) {
        try {
            log.info("create ai client api request: {}", request);

            AiClientApi aiClientApi = convertToAiClientApi(request);
            aiClientApi.setCreateTime(LocalDateTime.now());
            aiClientApi.setUpdateTime(LocalDateTime.now());

            int result = aiClientApiDao.insert(aiClientApi);

            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("create ai client api failed", e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @PutMapping("/update-by-id")
    public Response<Boolean> updateAiClientApiById(@RequestBody AiClientApiRequestDTO request) {
        try {
            log.info("update ai client api by id request: {}", request);

            if (request.getId() == null) {
                return Response.<Boolean>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info("id cannot be empty")
                        .data(false)
                        .build();
            }

            AiClientApi aiClientApi = convertToAiClientApi(request);
            aiClientApi.setUpdateTime(LocalDateTime.now());

            int result = aiClientApiDao.updateById(aiClientApi);

            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("update ai client api by id failed", e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @PutMapping("/update-by-api-id")
    public Response<Boolean> updateAiClientApiByApiId(@RequestBody AiClientApiRequestDTO request) {
        try {
            log.info("update ai client api by api id request: {}", request);

            if (!StringUtils.hasText(request.getApiId())) {
                return Response.<Boolean>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info("apiId cannot be empty")
                        .data(false)
                        .build();
            }

            AiClientApi aiClientApi = convertToAiClientApi(request);
            aiClientApi.setUpdateTime(LocalDateTime.now());

            int result = aiClientApiDao.updateByApiId(aiClientApi);

            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("update ai client api by api id failed", e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @DeleteMapping("/delete-by-id/{id}")
    public Response<Boolean> deleteAiClientApiById(@PathVariable("id") Long id) {
        try {
            log.info("delete ai client api by id request: {}", id);

            int result = aiClientApiDao.deleteById(id);

            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("delete ai client api by id failed", e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @DeleteMapping("/delete-by-api-id/{apiId}")
    public Response<Boolean> deleteAiClientApiByApiId(@PathVariable("apiId") String apiId) {
        try {
            log.info("delete ai client api by api id request: {}", apiId);

            int result = aiClientApiDao.deleteByApiId(apiId);

            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("delete ai client api by api id failed", e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-by-id/{id}")
    public Response<AiClientApiResponseDTO> queryAiClientApiById(@PathVariable("id") Long id) {
        try {
            log.info("query ai client api by id request: {}", id);

            AiClientApi aiClientApi = aiClientApiDao.queryById(id);
            AiClientApiResponseDTO responseDTO = aiClientApi == null ? null : convertToAiClientApiResponseDTO(aiClientApi);

            return Response.<AiClientApiResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTO)
                    .build();
        } catch (Exception e) {
            log.error("query ai client api by id failed", e);
            return Response.<AiClientApiResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-by-api-id/{apiId}")
    public Response<AiClientApiResponseDTO> queryAiClientApiByApiId(@PathVariable("apiId") String apiId) {
        try {
            log.info("query ai client api by api id request: {}", apiId);

            AiClientApi aiClientApi = aiClientApiDao.queryByApiId(apiId);
            AiClientApiResponseDTO responseDTO = aiClientApi == null ? null : convertToAiClientApiResponseDTO(aiClientApi);

            return Response.<AiClientApiResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTO)
                    .build();
        } catch (Exception e) {
            log.error("query ai client api by api id failed", e);
            return Response.<AiClientApiResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-enabled")
    public Response<List<AiClientApiResponseDTO>> queryEnabledAiClientApis() {
        try {
            log.info("query enabled ai client apis request");

            List<AiClientApi> aiClientApis = aiClientApiDao.queryEnabledApis();
            List<AiClientApiResponseDTO> responseDTOs = convertToAiClientApiResponseDTOList(aiClientApis);

            return Response.<List<AiClientApiResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("query enabled ai client apis failed", e);
            return Response.<List<AiClientApiResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @PostMapping("/query-list")
    public Response<List<AiClientApiResponseDTO>> queryAiClientApiList(@RequestBody(required = false) AiClientApiQueryRequestDTO request) {
        try {
            log.info("query ai client api list request: {}", request);

            AiClientApiQueryRequestDTO queryRequest = request == null ? new AiClientApiQueryRequestDTO() : request;
            List<AiClientApi> aiClientApis;

            if (StringUtils.hasText(queryRequest.getApiId())) {
                AiClientApi aiClientApi = aiClientApiDao.queryByApiId(queryRequest.getApiId());
                aiClientApis = aiClientApi != null ? List.of(aiClientApi) : List.of();
            } else if (Integer.valueOf(1).equals(queryRequest.getStatus())) {
                aiClientApis = aiClientApiDao.queryEnabledApis();
            } else {
                aiClientApis = aiClientApiDao.queryAll();
            }

            if (queryRequest.getStatus() != null) {
                aiClientApis = aiClientApis.stream()
                        .filter(api -> queryRequest.getStatus().equals(api.getStatus()))
                        .collect(Collectors.toList());
            }

            if (StringUtils.hasText(queryRequest.getBaseUrl())) {
                aiClientApis = aiClientApis.stream()
                        .filter(api -> api.getBaseUrl() != null && api.getBaseUrl().contains(queryRequest.getBaseUrl()))
                        .collect(Collectors.toList());
            }

            List<AiClientApiResponseDTO> responseDTOs = convertToAiClientApiResponseDTOList(aiClientApis);

            return Response.<List<AiClientApiResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("query ai client api list failed", e);
            return Response.<List<AiClientApiResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-all")
    public Response<List<AiClientApiResponseDTO>> queryAllAiClientApis() {
        try {
            log.info("query all ai client apis request");

            List<AiClientApi> aiClientApis = aiClientApiDao.queryAll();
            List<AiClientApiResponseDTO> responseDTOs = convertToAiClientApiResponseDTOList(aiClientApis);

            return Response.<List<AiClientApiResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("query all ai client apis failed", e);
            return Response.<List<AiClientApiResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    private AiClientApi convertToAiClientApi(AiClientApiRequestDTO requestDTO) {
        AiClientApi aiClientApi = new AiClientApi();
        BeanUtils.copyProperties(requestDTO, aiClientApi);
        return aiClientApi;
    }

    private AiClientApiResponseDTO convertToAiClientApiResponseDTO(AiClientApi aiClientApi) {
        AiClientApiResponseDTO responseDTO = new AiClientApiResponseDTO();
        BeanUtils.copyProperties(aiClientApi, responseDTO);
        return responseDTO;
    }

    private List<AiClientApiResponseDTO> convertToAiClientApiResponseDTOList(List<AiClientApi> aiClientApis) {
        return aiClientApis.stream()
                .map(this::convertToAiClientApiResponseDTO)
                .collect(Collectors.toList());
    }

}

