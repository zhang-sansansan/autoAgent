package cn.ann.ai.trigger.http.admin;

import cn.ann.ai.api.IAiClientAdvisorAdminService;
import cn.ann.ai.api.dto.AiClientAdvisorQueryRequestDTO;
import cn.ann.ai.api.dto.AiClientAdvisorRequestDTO;
import cn.ann.ai.api.dto.AiClientAdvisorResponseDTO;
import cn.ann.ai.api.response.Response;
import cn.ann.ai.infrastructure.dao.IAiClientAdvisorDao;
import cn.ann.ai.infrastructure.dao.po.AiClientAdvisor;
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
 * 椤鹃棶閰嶇疆绠＄悊鎺у埗鍣? *
 * @author bugstack铏礊鏍? * @description 椤鹃棶閰嶇疆绠＄悊鎺у埗鍣? */
@Slf4j
@RestController
@RequestMapping("/api/v1/admin/ai-client-advisor")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AiClientAdvisorAdminController implements IAiClientAdvisorAdminService {

    @Resource
    private IAiClientAdvisorDao aiClientAdvisorDao;

    @Override
    @PostMapping("/create")
    public Response<Boolean> createAiClientAdvisor(@RequestBody AiClientAdvisorRequestDTO request) {
        try {
            log.info("鍒涘缓椤鹃棶閰嶇疆璇锋眰锛歿}", request);
            
            // DTO杞琍O
            AiClientAdvisor aiClientAdvisor = convertToAiClientAdvisor(request);
            aiClientAdvisor.setCreateTime(LocalDateTime.now());
            aiClientAdvisor.setUpdateTime(LocalDateTime.now());
            
            int result = aiClientAdvisorDao.insert(aiClientAdvisor);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鍒涘缓椤鹃棶閰嶇疆澶辫触", e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @PutMapping("/update-by-id")
    public Response<Boolean> updateAiClientAdvisorById(@RequestBody AiClientAdvisorRequestDTO request) {
        try {
            log.info("鏍规嵁ID鏇存柊椤鹃棶閰嶇疆璇锋眰锛歿}", request);
            
            if (request.getId() == null) {
                return Response.<Boolean>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info("ID涓嶈兘涓虹┖")
                        .data(false)
                        .build();
            }
            
            // DTO杞琍O
            AiClientAdvisor aiClientAdvisor = convertToAiClientAdvisor(request);
            aiClientAdvisor.setUpdateTime(LocalDateTime.now());
            
            int result = aiClientAdvisorDao.updateById(aiClientAdvisor);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁ID鏇存柊椤鹃棶閰嶇疆澶辫触", e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @PutMapping("/update-by-advisor-id")
    public Response<Boolean> updateAiClientAdvisorByAdvisorId(@RequestBody AiClientAdvisorRequestDTO request) {
        try {
            log.info("鏍规嵁椤鹃棶ID鏇存柊椤鹃棶閰嶇疆璇锋眰锛歿}", request);
            
            if (!StringUtils.hasText(request.getAdvisorId())) {
                return Response.<Boolean>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info("椤鹃棶ID涓嶈兘涓虹┖")
                        .data(false)
                        .build();
            }
            
            // DTO杞琍O
            AiClientAdvisor aiClientAdvisor = convertToAiClientAdvisor(request);
            aiClientAdvisor.setUpdateTime(LocalDateTime.now());
            
            int result = aiClientAdvisorDao.updateByAdvisorId(aiClientAdvisor);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁椤鹃棶ID鏇存柊椤鹃棶閰嶇疆澶辫触", e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @DeleteMapping("/delete-by-id/{id}")
    public Response<Boolean> deleteAiClientAdvisorById(@PathVariable("id") Long id) {
        try {
            log.info("鏍规嵁ID鍒犻櫎椤鹃棶閰嶇疆璇锋眰锛歿}", id);
            
            int result = aiClientAdvisorDao.deleteById(id);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁ID鍒犻櫎椤鹃棶閰嶇疆澶辫触", e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @DeleteMapping("/delete-by-advisor-id/{advisorId}")
    public Response<Boolean> deleteAiClientAdvisorByAdvisorId(@PathVariable("advisorId") String advisorId) {
        try {
            log.info("鏍规嵁椤鹃棶ID鍒犻櫎椤鹃棶閰嶇疆璇锋眰锛歿}", advisorId);
            
            int result = aiClientAdvisorDao.deleteByAdvisorId(advisorId);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁椤鹃棶ID鍒犻櫎椤鹃棶閰嶇疆澶辫触", e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-by-id/{id}")
    public Response<AiClientAdvisorResponseDTO> queryAiClientAdvisorById(@PathVariable("id") Long id) {
        try {
            log.info("鏍规嵁ID鏌ヨ椤鹃棶閰嶇疆璇锋眰锛歿}", id);
            
            AiClientAdvisor aiClientAdvisor = aiClientAdvisorDao.queryById(id);
            
            if (aiClientAdvisor == null) {
                return Response.<AiClientAdvisorResponseDTO>builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info("鏈壘鍒板搴旂殑椤鹃棶閰嶇疆")
                        .data(null)
                        .build();
            }
            
            AiClientAdvisorResponseDTO responseDTO = convertToAiClientAdvisorResponseDTO(aiClientAdvisor);
            
            return Response.<AiClientAdvisorResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTO)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁ID鏌ヨ椤鹃棶閰嶇疆澶辫触", e);
            return Response.<AiClientAdvisorResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-by-advisor-id/{advisorId}")
    public Response<AiClientAdvisorResponseDTO> queryAiClientAdvisorByAdvisorId(@PathVariable("advisorId") String advisorId) {
        try {
            log.info("鏍规嵁椤鹃棶ID鏌ヨ椤鹃棶閰嶇疆璇锋眰锛歿}", advisorId);
            
            AiClientAdvisor aiClientAdvisor = aiClientAdvisorDao.queryByAdvisorId(advisorId);
            
            if (aiClientAdvisor == null) {
                return Response.<AiClientAdvisorResponseDTO>builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info("鏈壘鍒板搴旂殑椤鹃棶閰嶇疆")
                        .data(null)
                        .build();
            }
            
            AiClientAdvisorResponseDTO responseDTO = convertToAiClientAdvisorResponseDTO(aiClientAdvisor);
            
            return Response.<AiClientAdvisorResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTO)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁椤鹃棶ID鏌ヨ椤鹃棶閰嶇疆澶辫触", e);
            return Response.<AiClientAdvisorResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-enabled")
    public Response<List<AiClientAdvisorResponseDTO>> queryEnabledAiClientAdvisors() {
        try {
            log.info("鏌ヨ鎵€鏈夊惎鐢ㄧ殑椤鹃棶閰嶇疆");
            
            List<AiClientAdvisor> aiClientAdvisors = aiClientAdvisorDao.queryByStatus(1);
            
            List<AiClientAdvisorResponseDTO> responseDTOs = aiClientAdvisors.stream()
                    .map(this::convertToAiClientAdvisorResponseDTO)
                    .collect(Collectors.toList());
            
            return Response.<List<AiClientAdvisorResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("鏌ヨ鎵€鏈夊惎鐢ㄧ殑椤鹃棶閰嶇疆澶辫触", e);
            return Response.<List<AiClientAdvisorResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-by-status/{status}")
    public Response<List<AiClientAdvisorResponseDTO>> queryAiClientAdvisorsByStatus(@PathVariable("status") Integer status) {
        try {
            log.info("鏍规嵁鐘舵€佹煡璇㈤【闂厤缃姹傦細{}", status);
            
            List<AiClientAdvisor> aiClientAdvisors = aiClientAdvisorDao.queryByStatus(status);
            
            List<AiClientAdvisorResponseDTO> responseDTOs = aiClientAdvisors.stream()
                    .map(this::convertToAiClientAdvisorResponseDTO)
                    .collect(Collectors.toList());
            
            return Response.<List<AiClientAdvisorResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁鐘舵€佹煡璇㈤【闂厤缃け璐?, e);
            return Response.<List<AiClientAdvisorResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-by-type/{advisorType}")
    public Response<List<AiClientAdvisorResponseDTO>> queryAiClientAdvisorsByType(@PathVariable("advisorType") String advisorType) {
        try {
            log.info("鏍规嵁椤鹃棶绫诲瀷鏌ヨ椤鹃棶閰嶇疆璇锋眰锛歿}", advisorType);
            
            List<AiClientAdvisor> aiClientAdvisors = aiClientAdvisorDao.queryByAdvisorType(advisorType);
            
            List<AiClientAdvisorResponseDTO> responseDTOs = aiClientAdvisors.stream()
                    .map(this::convertToAiClientAdvisorResponseDTO)
                    .collect(Collectors.toList());
            
            return Response.<List<AiClientAdvisorResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁椤鹃棶绫诲瀷鏌ヨ椤鹃棶閰嶇疆澶辫触", e);
            return Response.<List<AiClientAdvisorResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @PostMapping("/query-list")
    public Response<List<AiClientAdvisorResponseDTO>> queryAiClientAdvisorList(@RequestBody AiClientAdvisorQueryRequestDTO request) {
        try {
            log.info("鏍规嵁鏉′欢鏌ヨ椤鹃棶閰嶇疆鍒楄〃璇锋眰锛歿}", request);
            
            // 鏍规嵁鏌ヨ鏉′欢鑾峰彇鏁版嵁
            List<AiClientAdvisor> aiClientAdvisors;
            
            if (StringUtils.hasText(request.getAdvisorId())) {
                // 濡傛灉鏈夐【闂甀D锛岀洿鎺ユ煡璇?                AiClientAdvisor advisor = aiClientAdvisorDao.queryByAdvisorId(request.getAdvisorId());
                aiClientAdvisors = advisor != null ? List.of(advisor) : List.of();
            } else if (StringUtils.hasText(request.getAdvisorType())) {
                // 濡傛灉鏈夐【闂被鍨嬶紝鎸夌被鍨嬫煡璇?                aiClientAdvisors = aiClientAdvisorDao.queryByAdvisorType(request.getAdvisorType());
            } else if (request.getStatus() != null) {
                // 濡傛灉鏈夌姸鎬侊紝鎸夌姸鎬佹煡璇?                aiClientAdvisors = aiClientAdvisorDao.queryByStatus(request.getStatus());
            } else {
                // 鍚﹀垯鏌ヨ鎵€鏈?                aiClientAdvisors = aiClientAdvisorDao.queryAll();
            }
            
            // 杩囨护鏉′欢
            List<AiClientAdvisor> filteredAdvisors = aiClientAdvisors.stream()
                    .filter(advisor -> {
                        // 椤鹃棶鍚嶇О妯＄硦鏌ヨ
                        if (StringUtils.hasText(request.getAdvisorName()) && 
                            !advisor.getAdvisorName().contains(request.getAdvisorName())) {
                            return false;
                        }
                        // 鐘舵€佽繃婊?                        if (request.getStatus() != null && !request.getStatus().equals(advisor.getStatus())) {
                            return false;
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
            
            // 鍒嗛〉澶勭悊锛堢畝鍗曞疄鐜帮級
            if (request.getPageNum() != null && request.getPageSize() != null) {
                int pageNum = Math.max(1, request.getPageNum());
                int pageSize = Math.max(1, request.getPageSize());
                int startIndex = (pageNum - 1) * pageSize;
                int endIndex = Math.min(startIndex + pageSize, filteredAdvisors.size());
                
                if (startIndex < filteredAdvisors.size()) {
                    filteredAdvisors = filteredAdvisors.subList(startIndex, endIndex);
                } else {
                    filteredAdvisors = List.of();
                }
            }
            
            List<AiClientAdvisorResponseDTO> responseDTOs = filteredAdvisors.stream()
                    .map(this::convertToAiClientAdvisorResponseDTO)
                    .collect(Collectors.toList());
            
            return Response.<List<AiClientAdvisorResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁鏉′欢鏌ヨ椤鹃棶閰嶇疆鍒楄〃澶辫触", e);
            return Response.<List<AiClientAdvisorResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-all")
    public Response<List<AiClientAdvisorResponseDTO>> queryAllAiClientAdvisors() {
        try {
            log.info("鏌ヨ鎵€鏈夐【闂厤缃?);
            
            List<AiClientAdvisor> aiClientAdvisors = aiClientAdvisorDao.queryAll();
            
            List<AiClientAdvisorResponseDTO> responseDTOs = aiClientAdvisors.stream()
                    .map(this::convertToAiClientAdvisorResponseDTO)
                    .collect(Collectors.toList());
            
            return Response.<List<AiClientAdvisorResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("鏌ヨ鎵€鏈夐【闂厤缃け璐?, e);
            return Response.<List<AiClientAdvisorResponseDTO>>builder()
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
    private AiClientAdvisor convertToAiClientAdvisor(AiClientAdvisorRequestDTO requestDTO) {
        AiClientAdvisor aiClientAdvisor = new AiClientAdvisor();
        BeanUtils.copyProperties(requestDTO, aiClientAdvisor);
        return aiClientAdvisor;
    }

    /**
     * PO杞搷搴擠TO瀵硅薄
     * @param aiClientAdvisor PO瀵硅薄
     * @return 鍝嶅簲DTO
     */
    private AiClientAdvisorResponseDTO convertToAiClientAdvisorResponseDTO(AiClientAdvisor aiClientAdvisor) {
        AiClientAdvisorResponseDTO responseDTO = new AiClientAdvisorResponseDTO();
        BeanUtils.copyProperties(aiClientAdvisor, responseDTO);
        return responseDTO;
    }

}

