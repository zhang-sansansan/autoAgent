package cn.ann.ai.trigger.http.admin;

import cn.ann.ai.api.IAiClientToolMcpAdminService;
import cn.ann.ai.api.dto.AiClientToolMcpQueryRequestDTO;
import cn.ann.ai.api.dto.AiClientToolMcpRequestDTO;
import cn.ann.ai.api.dto.AiClientToolMcpResponseDTO;
import cn.ann.ai.api.response.Response;
import cn.ann.ai.infrastructure.dao.IAiClientToolMcpDao;
import cn.ann.ai.infrastructure.dao.po.AiClientToolMcp;
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
 * MCP瀹㈡埛绔厤缃鐞嗘帶鍒跺櫒
 *
 * @author bugstack铏礊鏍? * @description MCP瀹㈡埛绔厤缃鐞嗘帶鍒跺櫒
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/admin/ai-client-tool-mcp")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AiClientToolMcpAdminController implements IAiClientToolMcpAdminService {

    @Resource
    private IAiClientToolMcpDao aiClientToolMcpDao;

    @Override
    @PostMapping("/create")
    public Response<Boolean> createAiClientToolMcp(@RequestBody AiClientToolMcpRequestDTO request) {
        try {
            log.info("鍒涘缓MCP瀹㈡埛绔厤缃姹傦細{}", request);
            
            // DTO杞琍O
            AiClientToolMcp aiClientToolMcp = convertToAiClientToolMcp(request);
            aiClientToolMcp.setCreateTime(LocalDateTime.now());
            aiClientToolMcp.setUpdateTime(LocalDateTime.now());
            
            int result = aiClientToolMcpDao.insert(aiClientToolMcp);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鍒涘缓MCP瀹㈡埛绔厤缃け璐?, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @PutMapping("/update-by-id")
    public Response<Boolean> updateAiClientToolMcpById(@RequestBody AiClientToolMcpRequestDTO request) {
        try {
            log.info("鏍规嵁ID鏇存柊MCP瀹㈡埛绔厤缃姹傦細{}", request);
            
            if (request.getId() == null) {
                return Response.<Boolean>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info("ID涓嶈兘涓虹┖")
                        .data(false)
                        .build();
            }
            
            // DTO杞琍O
            AiClientToolMcp aiClientToolMcp = convertToAiClientToolMcp(request);
            aiClientToolMcp.setUpdateTime(LocalDateTime.now());
            
            int result = aiClientToolMcpDao.updateById(aiClientToolMcp);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁ID鏇存柊MCP瀹㈡埛绔厤缃け璐?, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @PutMapping("/update-by-mcp-id")
    public Response<Boolean> updateAiClientToolMcpByMcpId(@RequestBody AiClientToolMcpRequestDTO request) {
        try {
            log.info("鏍规嵁MCP ID鏇存柊MCP瀹㈡埛绔厤缃姹傦細{}", request);
            
            if (!StringUtils.hasText(request.getMcpId())) {
                return Response.<Boolean>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info("MCP ID涓嶈兘涓虹┖")
                        .data(false)
                        .build();
            }
            
            // DTO杞琍O
            AiClientToolMcp aiClientToolMcp = convertToAiClientToolMcp(request);
            aiClientToolMcp.setUpdateTime(LocalDateTime.now());
            
            int result = aiClientToolMcpDao.updateByMcpId(aiClientToolMcp);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁MCP ID鏇存柊MCP瀹㈡埛绔厤缃け璐?, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @DeleteMapping("/delete-by-id/{id}")
    public Response<Boolean> deleteAiClientToolMcpById(@PathVariable("id") Long id) {
        try {
            log.info("鏍规嵁ID鍒犻櫎MCP瀹㈡埛绔厤缃細{}", id);
            
            int result = aiClientToolMcpDao.deleteById(id);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁ID鍒犻櫎MCP瀹㈡埛绔厤缃け璐?, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @DeleteMapping("/delete-by-mcp-id/{mcpId}")
    public Response<Boolean> deleteAiClientToolMcpByMcpId(@PathVariable("mcpId") String mcpId) {
        try {
            log.info("鏍规嵁MCP ID鍒犻櫎MCP瀹㈡埛绔厤缃細{}", mcpId);
            
            int result = aiClientToolMcpDao.deleteByMcpId(mcpId);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁MCP ID鍒犻櫎MCP瀹㈡埛绔厤缃け璐?, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-by-id/{id}")
    public Response<AiClientToolMcpResponseDTO> queryAiClientToolMcpById(@PathVariable("id") Long id) {
        try {
            log.info("鏍规嵁ID鏌ヨMCP瀹㈡埛绔厤缃細{}", id);
            
            AiClientToolMcp aiClientToolMcp = aiClientToolMcpDao.queryById(id);
            
            if (aiClientToolMcp == null) {
                return Response.<AiClientToolMcpResponseDTO>builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info(ResponseCode.SUCCESS.getInfo())
                        .data(null)
                        .build();
            }
            
            AiClientToolMcpResponseDTO responseDTO = convertToAiClientToolMcpResponseDTO(aiClientToolMcp);
            
            return Response.<AiClientToolMcpResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTO)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁ID鏌ヨMCP瀹㈡埛绔厤缃け璐?, e);
            return Response.<AiClientToolMcpResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-by-mcp-id/{mcpId}")
    public Response<AiClientToolMcpResponseDTO> queryAiClientToolMcpByMcpId(@PathVariable("mcpId") String mcpId) {
        try {
            log.info("鏍规嵁MCP ID鏌ヨMCP瀹㈡埛绔厤缃細{}", mcpId);
            
            AiClientToolMcp aiClientToolMcp = aiClientToolMcpDao.queryByMcpId(mcpId);
            
            if (aiClientToolMcp == null) {
                return Response.<AiClientToolMcpResponseDTO>builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info(ResponseCode.SUCCESS.getInfo())
                        .data(null)
                        .build();
            }
            
            AiClientToolMcpResponseDTO responseDTO = convertToAiClientToolMcpResponseDTO(aiClientToolMcp);
            
            return Response.<AiClientToolMcpResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTO)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁MCP ID鏌ヨMCP瀹㈡埛绔厤缃け璐?, e);
            return Response.<AiClientToolMcpResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-all")
    public Response<List<AiClientToolMcpResponseDTO>> queryAllAiClientToolMcps() {
        try {
            log.info("鏌ヨ鎵€鏈塎CP瀹㈡埛绔厤缃?);
            
            List<AiClientToolMcp> aiClientToolMcps = aiClientToolMcpDao.queryAll();
            
            List<AiClientToolMcpResponseDTO> responseDTOs = aiClientToolMcps.stream()
                    .map(this::convertToAiClientToolMcpResponseDTO)
                    .collect(Collectors.toList());
            
            return Response.<List<AiClientToolMcpResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("鏌ヨ鎵€鏈塎CP瀹㈡埛绔厤缃け璐?, e);
            return Response.<List<AiClientToolMcpResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-by-status/{status}")
    public Response<List<AiClientToolMcpResponseDTO>> queryAiClientToolMcpsByStatus(@PathVariable("status") Integer status) {
        try {
            log.info("鏍规嵁鐘舵€佹煡璇CP瀹㈡埛绔厤缃細{}", status);
            
            List<AiClientToolMcp> aiClientToolMcps = aiClientToolMcpDao.queryByStatus(status);
            
            List<AiClientToolMcpResponseDTO> responseDTOs = aiClientToolMcps.stream()
                    .map(this::convertToAiClientToolMcpResponseDTO)
                    .collect(Collectors.toList());
            
            return Response.<List<AiClientToolMcpResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁鐘舵€佹煡璇CP瀹㈡埛绔厤缃け璐?, e);
            return Response.<List<AiClientToolMcpResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-by-transport-type/{transportType}")
    public Response<List<AiClientToolMcpResponseDTO>> queryAiClientToolMcpsByTransportType(@PathVariable("transportType") String transportType) {
        try {
            log.info("鏍规嵁浼犺緭绫诲瀷鏌ヨMCP瀹㈡埛绔厤缃細{}", transportType);
            
            List<AiClientToolMcp> aiClientToolMcps = aiClientToolMcpDao.queryByTransportType(transportType);
            
            List<AiClientToolMcpResponseDTO> responseDTOs = aiClientToolMcps.stream()
                    .map(this::convertToAiClientToolMcpResponseDTO)
                    .collect(Collectors.toList());
            
            return Response.<List<AiClientToolMcpResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁浼犺緭绫诲瀷鏌ヨMCP瀹㈡埛绔厤缃け璐?, e);
            return Response.<List<AiClientToolMcpResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-enabled")
    public Response<List<AiClientToolMcpResponseDTO>> queryEnabledAiClientToolMcps() {
        try {
            log.info("鏌ヨ鍚敤鐨凪CP瀹㈡埛绔厤缃?);
            
            List<AiClientToolMcp> aiClientToolMcps = aiClientToolMcpDao.queryEnabledMcps();
            
            List<AiClientToolMcpResponseDTO> responseDTOs = aiClientToolMcps.stream()
                    .map(this::convertToAiClientToolMcpResponseDTO)
                    .collect(Collectors.toList());
            
            return Response.<List<AiClientToolMcpResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("鏌ヨ鍚敤鐨凪CP瀹㈡埛绔厤缃け璐?, e);
            return Response.<List<AiClientToolMcpResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @PostMapping("/query-list")
    public Response<List<AiClientToolMcpResponseDTO>> queryAiClientToolMcpList(@RequestBody AiClientToolMcpQueryRequestDTO request) {
        try {
            log.info("鏍规嵁鏌ヨ鏉′欢鏌ヨMCP瀹㈡埛绔厤缃垪琛細{}", request);
            
            // 鏍规嵁鏌ヨ鏉′欢璋冪敤涓嶅悓鐨凞AO鏂规硶
            List<AiClientToolMcp> aiClientToolMcps;
            
            if (StringUtils.hasText(request.getMcpId())) {
                // 鏍规嵁MCP ID鏌ヨ
                AiClientToolMcp single = aiClientToolMcpDao.queryByMcpId(request.getMcpId());
                aiClientToolMcps = single != null ? List.of(single) : List.of();
            } else if (request.getStatus() != null) {
                // 鏍规嵁鐘舵€佹煡璇?                aiClientToolMcps = aiClientToolMcpDao.queryByStatus(request.getStatus());
            } else if (StringUtils.hasText(request.getTransportType())) {
                // 鏍规嵁浼犺緭绫诲瀷鏌ヨ
                aiClientToolMcps = aiClientToolMcpDao.queryByTransportType(request.getTransportType());
            } else {
                // 鏌ヨ鎵€鏈?                aiClientToolMcps = aiClientToolMcpDao.queryAll();
            }
            
            // 濡傛灉鏈塎CP鍚嶇О鏉′欢锛岃繘琛岃繃婊?            if (StringUtils.hasText(request.getMcpName())) {
                aiClientToolMcps = aiClientToolMcps.stream()
                        .filter(mcp -> mcp.getMcpName() != null && 
                                      mcp.getMcpName().contains(request.getMcpName()))
                        .collect(Collectors.toList());
            }
            
            List<AiClientToolMcpResponseDTO> responseDTOs = aiClientToolMcps.stream()
                    .map(this::convertToAiClientToolMcpResponseDTO)
                    .collect(Collectors.toList());
            
            return Response.<List<AiClientToolMcpResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁鏌ヨ鏉′欢鏌ヨMCP瀹㈡埛绔厤缃垪琛ㄥけ璐?, e);
            return Response.<List<AiClientToolMcpResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    /**
     * DTO杞琍O瀵硅薄
     * @param requestDTO 璇锋眰DTO
     * @return PO瀵硅薄
     */
    private AiClientToolMcp convertToAiClientToolMcp(AiClientToolMcpRequestDTO requestDTO) {
        AiClientToolMcp aiClientToolMcp = new AiClientToolMcp();
        BeanUtils.copyProperties(requestDTO, aiClientToolMcp);
        return aiClientToolMcp;
    }

    /**
     * PO杞搷搴擠TO瀵硅薄
     * @param aiClientToolMcp PO瀵硅薄
     * @return 鍝嶅簲DTO
     */
    private AiClientToolMcpResponseDTO convertToAiClientToolMcpResponseDTO(AiClientToolMcp aiClientToolMcp) {
        AiClientToolMcpResponseDTO responseDTO = new AiClientToolMcpResponseDTO();
        BeanUtils.copyProperties(aiClientToolMcp, responseDTO);
        return responseDTO;
    }

}

