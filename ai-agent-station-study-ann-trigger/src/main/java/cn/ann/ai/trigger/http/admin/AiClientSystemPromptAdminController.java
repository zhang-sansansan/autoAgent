package cn.ann.ai.trigger.http.admin;

import cn.ann.ai.api.IAiClientSystemPromptAdminService;
import cn.ann.ai.api.dto.AiClientSystemPromptQueryRequestDTO;
import cn.ann.ai.api.dto.AiClientSystemPromptRequestDTO;
import cn.ann.ai.api.dto.AiClientSystemPromptResponseDTO;
import cn.ann.ai.api.response.Response;
import cn.ann.ai.infrastructure.dao.IAiClientSystemPromptDao;
import cn.ann.ai.infrastructure.dao.po.AiClientSystemPrompt;
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
 * 绯荤粺鎻愮ず璇嶉厤缃鐞嗘帶鍒跺櫒
 *
 * @author bugstack铏礊鏍? * @description 绯荤粺鎻愮ず璇嶉厤缃鐞嗘帶鍒跺櫒
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/admin/ai-client-system-prompt")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AiClientSystemPromptAdminController implements IAiClientSystemPromptAdminService {

    @Resource
    private IAiClientSystemPromptDao aiClientSystemPromptDao;

    @Override
    @PostMapping("/create")
    public Response<Boolean> createAiClientSystemPrompt(@RequestBody AiClientSystemPromptRequestDTO request) {
        try {
            log.info("鍒涘缓绯荤粺鎻愮ず璇嶉厤缃姹傦細{}", request);
            
            // DTO杞琍O
            AiClientSystemPrompt aiClientSystemPrompt = convertToAiClientSystemPrompt(request);
            aiClientSystemPrompt.setCreateTime(LocalDateTime.now());
            aiClientSystemPrompt.setUpdateTime(LocalDateTime.now());
            
            aiClientSystemPromptDao.insert(aiClientSystemPrompt);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(true)
                    .build();
        } catch (Exception e) {
            log.error("鍒涘缓绯荤粺鎻愮ず璇嶉厤缃け璐?, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @PutMapping("/update-by-id")
    public Response<Boolean> updateAiClientSystemPromptById(@RequestBody AiClientSystemPromptRequestDTO request) {
        try {
            log.info("鏍规嵁ID鏇存柊绯荤粺鎻愮ず璇嶉厤缃姹傦細{}", request);
            
            if (request.getId() == null) {
                return Response.<Boolean>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info("ID涓嶈兘涓虹┖")
                        .data(false)
                        .build();
            }
            
            // DTO杞琍O
            AiClientSystemPrompt aiClientSystemPrompt = convertToAiClientSystemPrompt(request);
            aiClientSystemPrompt.setUpdateTime(LocalDateTime.now());
            
            int result = aiClientSystemPromptDao.updateById(aiClientSystemPrompt);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁ID鏇存柊绯荤粺鎻愮ず璇嶉厤缃け璐?, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @PutMapping("/update-by-prompt-id")
    public Response<Boolean> updateAiClientSystemPromptByPromptId(@RequestBody AiClientSystemPromptRequestDTO request) {
        try {
            log.info("鏍规嵁鎻愮ず璇岻D鏇存柊绯荤粺鎻愮ず璇嶉厤缃姹傦細{}", request);
            
            if (!StringUtils.hasText(request.getPromptId())) {
                return Response.<Boolean>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info("鎻愮ず璇岻D涓嶈兘涓虹┖")
                        .data(false)
                        .build();
            }
            
            // DTO杞琍O
            AiClientSystemPrompt aiClientSystemPrompt = convertToAiClientSystemPrompt(request);
            aiClientSystemPrompt.setUpdateTime(LocalDateTime.now());
            
            int result = aiClientSystemPromptDao.updateByPromptId(aiClientSystemPrompt);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁鎻愮ず璇岻D鏇存柊绯荤粺鎻愮ず璇嶉厤缃け璐?, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @DeleteMapping("/delete-by-id/{id}")
    public Response<Boolean> deleteAiClientSystemPromptById(@PathVariable("id") Long id) {
        try {
            log.info("鏍规嵁ID鍒犻櫎绯荤粺鎻愮ず璇嶉厤缃細{}", id);
            
            int result = aiClientSystemPromptDao.deleteById(id);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁ID鍒犻櫎绯荤粺鎻愮ず璇嶉厤缃け璐?, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @DeleteMapping("/delete-by-prompt-id/{promptId}")
    public Response<Boolean> deleteAiClientSystemPromptByPromptId(@PathVariable("promptId") String promptId) {
        try {
            log.info("鏍规嵁鎻愮ず璇岻D鍒犻櫎绯荤粺鎻愮ず璇嶉厤缃細{}", promptId);
            
            int result = aiClientSystemPromptDao.deleteByPromptId(promptId);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁鎻愮ず璇岻D鍒犻櫎绯荤粺鎻愮ず璇嶉厤缃け璐?, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-by-id/{id}")
    public Response<AiClientSystemPromptResponseDTO> queryAiClientSystemPromptById(@PathVariable("id") Long id) {
        try {
            log.info("鏍规嵁ID鏌ヨ绯荤粺鎻愮ず璇嶉厤缃細{}", id);
            
            AiClientSystemPrompt aiClientSystemPrompt = aiClientSystemPromptDao.queryById(id);
            
            if (aiClientSystemPrompt == null) {
                return Response.<AiClientSystemPromptResponseDTO>builder()
                        .code(ResponseCode.UN_ERROR.getCode())
                        .info("鏁版嵁涓嶅瓨鍦?)
                        .data(null)
                        .build();
            }
            
            AiClientSystemPromptResponseDTO responseDTO = convertToAiClientSystemPromptResponseDTO(aiClientSystemPrompt);
            
            return Response.<AiClientSystemPromptResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTO)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁ID鏌ヨ绯荤粺鎻愮ず璇嶉厤缃け璐?, e);
            return Response.<AiClientSystemPromptResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-by-prompt-id/{promptId}")
    public Response<AiClientSystemPromptResponseDTO> queryAiClientSystemPromptByPromptId(@PathVariable("promptId") String promptId) {
        try {
            log.info("鏍规嵁鎻愮ず璇岻D鏌ヨ绯荤粺鎻愮ず璇嶉厤缃細{}", promptId);
            
            AiClientSystemPrompt aiClientSystemPrompt = aiClientSystemPromptDao.queryByPromptId(promptId);
            
            if (aiClientSystemPrompt == null) {
                return Response.<AiClientSystemPromptResponseDTO>builder()
                        .code(ResponseCode.UN_ERROR.getCode())
                        .info("绯荤粺鎻愮ず璇嶉厤缃笉瀛樺湪")
                        .data(null)
                        .build();
            }
            
            AiClientSystemPromptResponseDTO responseDTO = convertToAiClientSystemPromptResponseDTO(aiClientSystemPrompt);
            
            return Response.<AiClientSystemPromptResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTO)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁鎻愮ず璇岻D鏌ヨ绯荤粺鎻愮ず璇嶉厤缃け璐?, e);
            return Response.<AiClientSystemPromptResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-all")
    public Response<List<AiClientSystemPromptResponseDTO>> queryAllAiClientSystemPrompts() {
        try {
            log.info("鏌ヨ鎵€鏈夌郴缁熸彁绀鸿瘝閰嶇疆");
            
            List<AiClientSystemPrompt> aiClientSystemPrompts = aiClientSystemPromptDao.queryAll();
            
            List<AiClientSystemPromptResponseDTO> responseDTOs = aiClientSystemPrompts.stream()
                    .map(this::convertToAiClientSystemPromptResponseDTO)
                    .collect(Collectors.toList());
            
            return Response.<List<AiClientSystemPromptResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("鏌ヨ鎵€鏈夌郴缁熸彁绀鸿瘝閰嶇疆澶辫触", e);
            return Response.<List<AiClientSystemPromptResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-enabled")
    public Response<List<AiClientSystemPromptResponseDTO>> queryEnabledAiClientSystemPrompts() {
        try {
            log.info("鏌ヨ鍚敤鐨勭郴缁熸彁绀鸿瘝閰嶇疆");
            
            List<AiClientSystemPrompt> aiClientSystemPrompts = aiClientSystemPromptDao.queryEnabledPrompts();
            
            List<AiClientSystemPromptResponseDTO> responseDTOs = aiClientSystemPrompts.stream()
                    .map(this::convertToAiClientSystemPromptResponseDTO)
                    .collect(Collectors.toList());
            
            return Response.<List<AiClientSystemPromptResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("鏌ヨ鍚敤鐨勭郴缁熸彁绀鸿瘝閰嶇疆澶辫触", e);
            return Response.<List<AiClientSystemPromptResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-by-prompt-name/{promptName}")
    public Response<List<AiClientSystemPromptResponseDTO>> queryAiClientSystemPromptsByPromptName(@PathVariable("promptName") String promptName) {
        try {
            log.info("鏍规嵁鎻愮ず璇嶅悕绉版煡璇㈢郴缁熸彁绀鸿瘝閰嶇疆锛歿}", promptName);
            
            List<AiClientSystemPrompt> aiClientSystemPrompts = aiClientSystemPromptDao.queryByPromptName(promptName);
            
            List<AiClientSystemPromptResponseDTO> responseDTOs = aiClientSystemPrompts.stream()
                    .map(this::convertToAiClientSystemPromptResponseDTO)
                    .collect(Collectors.toList());
            
            return Response.<List<AiClientSystemPromptResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁鎻愮ず璇嶅悕绉版煡璇㈢郴缁熸彁绀鸿瘝閰嶇疆澶辫触", e);
            return Response.<List<AiClientSystemPromptResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @PostMapping("/query-list")
    public Response<List<AiClientSystemPromptResponseDTO>> queryAiClientSystemPromptList(@RequestBody AiClientSystemPromptQueryRequestDTO request) {
        try {
            log.info("鏍规嵁鏉′欢鏌ヨ绯荤粺鎻愮ず璇嶉厤缃垪琛細{}", request);
            
            // 鏍规嵁鏌ヨ鏉′欢鏋勫缓鏌ヨ閫昏緫
            List<AiClientSystemPrompt> aiClientSystemPrompts;
            
            if (StringUtils.hasText(request.getPromptId())) {
                // 鏍规嵁鎻愮ず璇岻D鏌ヨ
                AiClientSystemPrompt prompt = aiClientSystemPromptDao.queryByPromptId(request.getPromptId());
                aiClientSystemPrompts = prompt != null ? List.of(prompt) : List.of();
            } else if (StringUtils.hasText(request.getPromptName())) {
                // 鏍规嵁鎻愮ず璇嶅悕绉版煡璇?                aiClientSystemPrompts = aiClientSystemPromptDao.queryByPromptName(request.getPromptName());
            } else if (request.getStatus() != null) {
                // 鏍规嵁鐘舵€佹煡璇?                if (request.getStatus() == 1) {
                    aiClientSystemPrompts = aiClientSystemPromptDao.queryEnabledPrompts();
                } else {
                    // 鏌ヨ鎵€鏈夌劧鍚庤繃婊?                    aiClientSystemPrompts = aiClientSystemPromptDao.queryAll().stream()
                            .filter(prompt -> prompt.getStatus().equals(request.getStatus()))
                            .collect(Collectors.toList());
                }
            } else {
                // 鏌ヨ鎵€鏈?                aiClientSystemPrompts = aiClientSystemPromptDao.queryAll();
            }
            
            // 搴旂敤鐘舵€佽繃婊わ紙濡傛灉鏈夊叾浠栨潯浠剁殑璇濓級
            if (request.getStatus() != null && !StringUtils.hasText(request.getPromptId()) && !StringUtils.hasText(request.getPromptName())) {
                aiClientSystemPrompts = aiClientSystemPrompts.stream()
                        .filter(prompt -> prompt.getStatus().equals(request.getStatus()))
                        .collect(Collectors.toList());
            }
            
            List<AiClientSystemPromptResponseDTO> responseDTOs = aiClientSystemPrompts.stream()
                    .map(this::convertToAiClientSystemPromptResponseDTO)
                    .collect(Collectors.toList());
            
            return Response.<List<AiClientSystemPromptResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁鏉′欢鏌ヨ绯荤粺鎻愮ず璇嶉厤缃垪琛ㄥけ璐?, e);
            return Response.<List<AiClientSystemPromptResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    /**
     * DTO杞琍O瀵硅薄
     */
    private AiClientSystemPrompt convertToAiClientSystemPrompt(AiClientSystemPromptRequestDTO requestDTO) {
        AiClientSystemPrompt aiClientSystemPrompt = new AiClientSystemPrompt();
        BeanUtils.copyProperties(requestDTO, aiClientSystemPrompt);
        return aiClientSystemPrompt;
    }

    /**
     * PO杞珼TO瀵硅薄
     */
    private AiClientSystemPromptResponseDTO convertToAiClientSystemPromptResponseDTO(AiClientSystemPrompt aiClientSystemPrompt) {
        AiClientSystemPromptResponseDTO responseDTO = new AiClientSystemPromptResponseDTO();
        BeanUtils.copyProperties(aiClientSystemPrompt, responseDTO);
        return responseDTO;
    }

}

