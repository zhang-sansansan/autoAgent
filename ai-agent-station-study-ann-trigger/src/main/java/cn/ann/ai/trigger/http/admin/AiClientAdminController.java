package cn.ann.ai.trigger.http.admin;

import cn.ann.ai.api.IAiClientAdminService;
import cn.ann.ai.api.dto.AiClientQueryRequestDTO;
import cn.ann.ai.api.dto.AiClientRequestDTO;
import cn.ann.ai.api.dto.AiClientResponseDTO;
import cn.ann.ai.api.response.Response;
import cn.ann.ai.infrastructure.dao.IAiClientDao;
import cn.ann.ai.infrastructure.dao.po.AiClient;
import cn.ann.ai.types.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AI瀹㈡埛绔鐞嗘帶鍒跺櫒
 *
 * @author bugstack铏礊鏍? * @description AI瀹㈡埛绔厤缃鐞嗘帶鍒跺櫒
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/admin/ai-client")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AiClientAdminController implements IAiClientAdminService {

    @Resource
    private IAiClientDao aiClientDao;

    @Override
    @PostMapping("/create")
    public Response<Boolean> createAiClient(@RequestBody AiClientRequestDTO request) {
        try {
            log.info("鍒涘缓AI瀹㈡埛绔厤缃姹傦細{}", request);
            
            // DTO杞琍O
            AiClient aiClient = convertToAiClient(request);
            aiClient.setCreateTime(LocalDateTime.now());
            aiClient.setUpdateTime(LocalDateTime.now());
            
            int result = aiClientDao.insert(aiClient);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鍒涘缓AI瀹㈡埛绔厤缃け璐?, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @PutMapping("/update-by-id")
    public Response<Boolean> updateAiClientById(@RequestBody AiClientRequestDTO request) {
        try {
            log.info("鏍规嵁ID鏇存柊AI瀹㈡埛绔厤缃姹傦細{}", request);
            
            if (request.getId() == null) {
                return Response.<Boolean>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info("ID涓嶈兘涓虹┖")
                        .data(false)
                        .build();
            }
            
            // DTO杞琍O
            AiClient aiClient = convertToAiClient(request);
            aiClient.setUpdateTime(LocalDateTime.now());
            
            int result = aiClientDao.updateById(aiClient);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁ID鏇存柊AI瀹㈡埛绔厤缃け璐?, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @PutMapping("/update-by-client-id")
    public Response<Boolean> updateAiClientByClientId(@RequestBody AiClientRequestDTO request) {
        try {
            log.info("鏍规嵁瀹㈡埛绔疘D鏇存柊AI瀹㈡埛绔厤缃姹傦細{}", request);
            
            if (!StringUtils.hasText(request.getClientId())) {
                return Response.<Boolean>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info("瀹㈡埛绔疘D涓嶈兘涓虹┖")
                        .data(false)
                        .build();
            }
            
            // DTO杞琍O
            AiClient aiClient = convertToAiClient(request);
            aiClient.setUpdateTime(LocalDateTime.now());
            
            int result = aiClientDao.updateByClientId(aiClient);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁瀹㈡埛绔疘D鏇存柊AI瀹㈡埛绔厤缃け璐?, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @DeleteMapping("/delete-by-id/{id}")
    public Response<Boolean> deleteAiClientById(@PathVariable("id") Long id) {
        try {
            log.info("鏍规嵁ID鍒犻櫎AI瀹㈡埛绔厤缃姹傦細{}", id);
            
            int result = aiClientDao.deleteById(id);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁ID鍒犻櫎AI瀹㈡埛绔厤缃け璐?, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @DeleteMapping("/delete-by-client-id/{clientId}")
    public Response<Boolean> deleteAiClientByClientId(@PathVariable("clientId") String clientId) {
        try {
            log.info("鏍规嵁瀹㈡埛绔疘D鍒犻櫎AI瀹㈡埛绔厤缃姹傦細{}", clientId);
            
            int result = aiClientDao.deleteByClientId(clientId);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁瀹㈡埛绔疘D鍒犻櫎AI瀹㈡埛绔厤缃け璐?, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-by-id/{id}")
    public Response<AiClientResponseDTO> queryAiClientById(@PathVariable("id") Long id) {
        try {
            log.info("鏍规嵁ID鏌ヨAI瀹㈡埛绔厤缃姹傦細{}", id);
            
            AiClient aiClient = aiClientDao.queryById(id);
            
            if (aiClient == null) {
                return Response.<AiClientResponseDTO>builder()
                        .code(ResponseCode.UN_ERROR.getCode())
                        .info("鏈壘鍒板搴旂殑AI瀹㈡埛绔厤缃?)
                        .data(null)
                        .build();
            }
            
            // PO杞珼TO
            AiClientResponseDTO responseDTO = convertToAiClientResponseDTO(aiClient);
            
            return Response.<AiClientResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTO)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁ID鏌ヨAI瀹㈡埛绔厤缃け璐?, e);
            return Response.<AiClientResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-by-client-id/{clientId}")
    public Response<AiClientResponseDTO> queryAiClientByClientId(@PathVariable("clientId") String clientId) {
        try {
            log.info("鏍规嵁瀹㈡埛绔疘D鏌ヨAI瀹㈡埛绔厤缃姹傦細{}", clientId);
            
            AiClient aiClient = aiClientDao.queryByClientId(clientId);
            
            if (aiClient == null) {
                return Response.<AiClientResponseDTO>builder()
                        .code(ResponseCode.UN_ERROR.getCode())
                        .info("鏈壘鍒板搴旂殑AI瀹㈡埛绔厤缃?)
                        .data(null)
                        .build();
            }
            
            // PO杞珼TO
            AiClientResponseDTO responseDTO = convertToAiClientResponseDTO(aiClient);
            
            return Response.<AiClientResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTO)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁瀹㈡埛绔疘D鏌ヨAI瀹㈡埛绔厤缃け璐?, e);
            return Response.<AiClientResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-enabled")
    public Response<List<AiClientResponseDTO>> queryEnabledAiClients() {
        try {
            log.info("鏌ヨ鎵€鏈夊惎鐢ㄧ殑AI瀹㈡埛绔厤缃?);
            
            List<AiClient> aiClients = aiClientDao.queryEnabledClients();
            
            List<AiClientResponseDTO> responseDTOs = aiClients.stream()
                    .map(this::convertToAiClientResponseDTO)
                    .collect(Collectors.toList());
            
            return Response.<List<AiClientResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("鏌ヨ鎵€鏈夊惎鐢ㄧ殑AI瀹㈡埛绔厤缃け璐?, e);
            return Response.<List<AiClientResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @PostMapping("/query-list")
    public Response<List<AiClientResponseDTO>> queryAiClientList(@RequestBody AiClientQueryRequestDTO request) {
        try {
            log.info("鏍规嵁鏉′欢鏌ヨAI瀹㈡埛绔厤缃垪琛ㄨ姹傦細{}", request);
            
            List<AiClient> aiClients;
            
            // 鏍规嵁涓嶅悓鏉′欢鏌ヨ
            if (StringUtils.hasText(request.getClientId())) {
                AiClient aiClient = aiClientDao.queryByClientId(request.getClientId());
                aiClients = aiClient != null ? List.of(aiClient) : List.of();
            } else if (StringUtils.hasText(request.getClientName())) {
                aiClients = aiClientDao.queryByClientName(request.getClientName());
            } else {
                aiClients = aiClientDao.queryAll();
            }
            
            // 鐘舵€佽繃婊?            if (request.getStatus() != null) {
                aiClients = aiClients.stream()
                        .filter(client -> request.getStatus().equals(client.getStatus()))
                        .collect(Collectors.toList());
            }
            
            // 鍒嗛〉澶勭悊锛堢畝鍗曞疄鐜帮級
            if (request.getPageNum() != null && request.getPageSize() != null) {
                int start = (request.getPageNum() - 1) * request.getPageSize();
                int end = Math.min(start + request.getPageSize(), aiClients.size());
                if (start < aiClients.size()) {
                    aiClients = aiClients.subList(start, end);
                } else {
                    aiClients = List.of();
                }
            }
            
            List<AiClientResponseDTO> responseDTOs = aiClients.stream()
                    .map(this::convertToAiClientResponseDTO)
                    .collect(Collectors.toList());
            
            return Response.<List<AiClientResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁鏉′欢鏌ヨAI瀹㈡埛绔厤缃垪琛ㄥけ璐?, e);
            return Response.<List<AiClientResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-all")
    public Response<List<AiClientResponseDTO>> queryAllAiClients() {
        try {
            log.info("鏌ヨ鎵€鏈堿I瀹㈡埛绔厤缃?);
            
            List<AiClient> aiClients = aiClientDao.queryAll();
            
            List<AiClientResponseDTO> responseDTOs = aiClients.stream()
                    .map(this::convertToAiClientResponseDTO)
                    .collect(Collectors.toList());
            
            return Response.<List<AiClientResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("鏌ヨ鎵€鏈堿I瀹㈡埛绔厤缃け璐?, e);
            return Response.<List<AiClientResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    /**
     * DTO杞琍O瀵硅薄
     */
    private AiClient convertToAiClient(AiClientRequestDTO requestDTO) {
        AiClient aiClient = new AiClient();
        BeanUtils.copyProperties(requestDTO, aiClient);
        return aiClient;
    }

    /**
     * PO杞珼TO瀵硅薄
     */
    private AiClientResponseDTO convertToAiClientResponseDTO(AiClient aiClient) {
        AiClientResponseDTO responseDTO = new AiClientResponseDTO();
        BeanUtils.copyProperties(aiClient, responseDTO);
        return responseDTO;
    }

}

