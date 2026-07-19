package cn.ann.ai.trigger.http.admin;

import cn.ann.ai.api.IAiClientRagOrderAdminService;
import cn.ann.ai.api.dto.AiClientRagOrderQueryRequestDTO;
import cn.ann.ai.api.dto.AiClientRagOrderRequestDTO;
import cn.ann.ai.api.dto.AiClientRagOrderResponseDTO;
import cn.ann.ai.api.response.Response;
import cn.ann.ai.domain.agent.service.IRagService;
import cn.ann.ai.infrastructure.dao.IAiClientRagOrderDao;
import cn.ann.ai.infrastructure.dao.po.AiClientRagOrder;
import cn.ann.ai.types.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 鐭ヨ瘑搴撻厤缃鐞嗘帶鍒跺櫒
 *
 * @author bugstack铏礊鏍?
 * @description 鐭ヨ瘑搴撻厤缃鐞嗘帶鍒跺櫒
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/admin/ai-client-rag-order")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AiClientRagOrderAdminController implements IAiClientRagOrderAdminService {

    @Resource
    private IAiClientRagOrderDao aiClientRagOrderDao;

    @Resource
    private IRagService ragService;

    @Override
    @PostMapping("/create")
    public Response<Boolean> createAiClientRagOrder(@RequestBody AiClientRagOrderRequestDTO request) {
        try {
            log.info("鍒涘缓鐭ヨ瘑搴撻厤缃姹傦細{}", request);
            
            // DTO杞琍O
            AiClientRagOrder aiClientRagOrder = convertToAiClientRagOrder(request);
            aiClientRagOrder.setCreateTime(LocalDateTime.now());
            aiClientRagOrder.setUpdateTime(LocalDateTime.now());
            
            int result = aiClientRagOrderDao.insert(aiClientRagOrder);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鍒涘缓鐭ヨ瘑搴撻厤缃け璐?, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @PutMapping("/update-by-id")
    public Response<Boolean> updateAiClientRagOrderById(@RequestBody AiClientRagOrderRequestDTO request) {
        try {
            log.info("鏍规嵁ID鏇存柊鐭ヨ瘑搴撻厤缃姹傦細{}", request);
            
            if (request.getId() == null) {
                return Response.<Boolean>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info("ID涓嶈兘涓虹┖")
                        .data(false)
                        .build();
            }
            
            // DTO杞琍O
            AiClientRagOrder aiClientRagOrder = convertToAiClientRagOrder(request);
            aiClientRagOrder.setUpdateTime(LocalDateTime.now());
            
            int result = aiClientRagOrderDao.updateById(aiClientRagOrder);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁ID鏇存柊鐭ヨ瘑搴撻厤缃け璐?, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @PutMapping("/update-by-rag-id")
    public Response<Boolean> updateAiClientRagOrderByRagId(@RequestBody AiClientRagOrderRequestDTO request) {
        try {
            log.info("鏍规嵁鐭ヨ瘑搴揑D鏇存柊鐭ヨ瘑搴撻厤缃姹傦細{}", request);
            
            if (!StringUtils.hasText(request.getRagId())) {
                return Response.<Boolean>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info("鐭ヨ瘑搴揑D涓嶈兘涓虹┖")
                        .data(false)
                        .build();
            }
            
            // DTO杞琍O
            AiClientRagOrder aiClientRagOrder = convertToAiClientRagOrder(request);
            aiClientRagOrder.setUpdateTime(LocalDateTime.now());
            
            int result = aiClientRagOrderDao.updateByRagId(aiClientRagOrder);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁鐭ヨ瘑搴揑D鏇存柊鐭ヨ瘑搴撻厤缃け璐?, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @DeleteMapping("/delete-by-id/{id}")
    public Response<Boolean> deleteAiClientRagOrderById(@PathVariable("id") Long id) {
        try {
            log.info("鏍规嵁ID鍒犻櫎鐭ヨ瘑搴撻厤缃細{}", id);
            
            int result = aiClientRagOrderDao.deleteById(id);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁ID鍒犻櫎鐭ヨ瘑搴撻厤缃け璐?, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @DeleteMapping("/delete-by-rag-id/{ragId}")
    public Response<Boolean> deleteAiClientRagOrderByRagId(@PathVariable("ragId") String ragId) {
        try {
            log.info("鏍规嵁鐭ヨ瘑搴揑D鍒犻櫎鐭ヨ瘑搴撻厤缃細{}", ragId);
            
            int result = aiClientRagOrderDao.deleteByRagId(ragId);
            
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(result > 0)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁鐭ヨ瘑搴揑D鍒犻櫎鐭ヨ瘑搴撻厤缃け璐?, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-by-id/{id}")
    public Response<AiClientRagOrderResponseDTO> queryAiClientRagOrderById(@PathVariable("id") Long id) {
        try {
            log.info("鏍规嵁ID鏌ヨ鐭ヨ瘑搴撻厤缃細{}", id);
            
            AiClientRagOrder aiClientRagOrder = aiClientRagOrderDao.queryById(id);
            if (aiClientRagOrder == null) {
                return Response.<AiClientRagOrderResponseDTO>builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info(ResponseCode.SUCCESS.getInfo())
                        .data(null)
                        .build();
            }
            
            AiClientRagOrderResponseDTO responseDTO = convertToAiClientRagOrderResponseDTO(aiClientRagOrder);
            
            return Response.<AiClientRagOrderResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTO)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁ID鏌ヨ鐭ヨ瘑搴撻厤缃け璐?, e);
            return Response.<AiClientRagOrderResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-by-rag-id/{ragId}")
    public Response<AiClientRagOrderResponseDTO> queryAiClientRagOrderByRagId(@PathVariable("ragId") String ragId) {
        try {
            log.info("鏍规嵁鐭ヨ瘑搴揑D鏌ヨ鐭ヨ瘑搴撻厤缃細{}", ragId);
            
            AiClientRagOrder aiClientRagOrder = aiClientRagOrderDao.queryByRagId(ragId);
            if (aiClientRagOrder == null) {
                return Response.<AiClientRagOrderResponseDTO>builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info(ResponseCode.SUCCESS.getInfo())
                        .data(null)
                        .build();
            }
            
            AiClientRagOrderResponseDTO responseDTO = convertToAiClientRagOrderResponseDTO(aiClientRagOrder);
            
            return Response.<AiClientRagOrderResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTO)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁鐭ヨ瘑搴揑D鏌ヨ鐭ヨ瘑搴撻厤缃け璐?, e);
            return Response.<AiClientRagOrderResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-enabled")
    public Response<List<AiClientRagOrderResponseDTO>> queryEnabledAiClientRagOrders() {
        try {
            log.info("鏌ヨ鍚敤鐨勭煡璇嗗簱閰嶇疆");
            
            List<AiClientRagOrder> aiClientRagOrders = aiClientRagOrderDao.queryEnabledRagOrders();
            List<AiClientRagOrderResponseDTO> responseDTOs = aiClientRagOrders.stream()
                    .map(this::convertToAiClientRagOrderResponseDTO)
                    .collect(Collectors.toList());
            
            return Response.<List<AiClientRagOrderResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("鏌ヨ鍚敤鐨勭煡璇嗗簱閰嶇疆澶辫触", e);
            return Response.<List<AiClientRagOrderResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-by-knowledge-tag/{knowledgeTag}")
    public Response<List<AiClientRagOrderResponseDTO>> queryAiClientRagOrdersByKnowledgeTag(@PathVariable("knowledgeTag") String knowledgeTag) {
        try {
            log.info("鏍规嵁鐭ヨ瘑鏍囩鏌ヨ鐭ヨ瘑搴撻厤缃細{}", knowledgeTag);
            
            List<AiClientRagOrder> aiClientRagOrders = aiClientRagOrderDao.queryByKnowledgeTag(knowledgeTag);
            List<AiClientRagOrderResponseDTO> responseDTOs = aiClientRagOrders.stream()
                    .map(this::convertToAiClientRagOrderResponseDTO)
                    .collect(Collectors.toList());
            
            return Response.<List<AiClientRagOrderResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁鐭ヨ瘑鏍囩鏌ヨ鐭ヨ瘑搴撻厤缃け璐?, e);
            return Response.<List<AiClientRagOrderResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-by-status/{status}")
    public Response<List<AiClientRagOrderResponseDTO>> queryAiClientRagOrdersByStatus(@PathVariable("status") Integer status) {
        try {
            log.info("鏍规嵁鐘舵€佹煡璇㈢煡璇嗗簱閰嶇疆锛歿}", status);
            
            // 杩欓噷闇€瑕佹牴鎹疄闄呯殑DAO鏂规硶瀹炵幇锛屽鏋滄病鏈夊彲浠ラ€氳繃queryAll鐒跺悗杩囨护
            List<AiClientRagOrder> aiClientRagOrders = aiClientRagOrderDao.queryAll();
            List<AiClientRagOrderResponseDTO> responseDTOs = aiClientRagOrders.stream()
                    .filter(order -> order.getStatus().equals(status))
                    .map(this::convertToAiClientRagOrderResponseDTO)
                    .collect(Collectors.toList());
            
            return Response.<List<AiClientRagOrderResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("鏍规嵁鐘舵€佹煡璇㈢煡璇嗗簱閰嶇疆澶辫触", e);
            return Response.<List<AiClientRagOrderResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @PostMapping("/query-list")
    public Response<List<AiClientRagOrderResponseDTO>> queryAiClientRagOrderList(@RequestBody AiClientRagOrderQueryRequestDTO request) {
        try {
            log.info("鍒嗛〉鏌ヨ鐭ヨ瘑搴撻厤缃垪琛細{}", request);
            
            // 杩欓噷绠€鍖栧疄鐜帮紝瀹為檯椤圭洰涓彲鑳介渶瑕佸疄鐜板垎椤垫煡璇?
            List<AiClientRagOrder> aiClientRagOrders = aiClientRagOrderDao.queryAll();
            
            // 鏍规嵁鏌ヨ鏉′欢杩囨护
            List<AiClientRagOrder> filteredOrders = aiClientRagOrders.stream()
                    .filter(order -> {
                        boolean match = true;
                        if (StringUtils.hasText(request.getRagId())) {
                            match = match && order.getRagId().contains(request.getRagId());
                        }
                        if (StringUtils.hasText(request.getRagName())) {
                            match = match && order.getRagName().contains(request.getRagName());
                        }
                        if (StringUtils.hasText(request.getKnowledgeTag())) {
                            match = match && order.getKnowledgeTag().contains(request.getKnowledgeTag());
                        }
                        if (request.getStatus() != null) {
                            match = match && order.getStatus().equals(request.getStatus());
                        }
                        return match;
                    })
                    .collect(Collectors.toList());
            
            // 绠€鍗曞垎椤靛鐞?
            if (request.getPageNum() != null && request.getPageSize() != null) {
                int start = (request.getPageNum() - 1) * request.getPageSize();
                int end = Math.min(start + request.getPageSize(), filteredOrders.size());
                if (start < filteredOrders.size()) {
                    filteredOrders = filteredOrders.subList(start, end);
                } else {
                    filteredOrders.clear();
                }
            }
            
            List<AiClientRagOrderResponseDTO> responseDTOs = filteredOrders.stream()
                    .map(this::convertToAiClientRagOrderResponseDTO)
                    .collect(Collectors.toList());
            
            return Response.<List<AiClientRagOrderResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("鍒嗛〉鏌ヨ鐭ヨ瘑搴撻厤缃垪琛ㄥけ璐?, e);
            return Response.<List<AiClientRagOrderResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    @Override
    @GetMapping("/query-all")
    public Response<List<AiClientRagOrderResponseDTO>> queryAllAiClientRagOrders() {
        try {
            log.info("鏌ヨ鎵€鏈夌煡璇嗗簱閰嶇疆");
            
            List<AiClientRagOrder> aiClientRagOrders = aiClientRagOrderDao.queryAll();
            List<AiClientRagOrderResponseDTO> responseDTOs = aiClientRagOrders.stream()
                    .map(this::convertToAiClientRagOrderResponseDTO)
                    .collect(Collectors.toList());
            
            return Response.<List<AiClientRagOrderResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTOs)
                    .build();
        } catch (Exception e) {
            log.error("鏌ヨ鎵€鏈夌煡璇嗗簱閰嶇疆澶辫触", e);
            return Response.<List<AiClientRagOrderResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(null)
                    .build();
        }
    }

    /**
     * DTO杞琍O
     */
    private AiClientRagOrder convertToAiClientRagOrder(AiClientRagOrderRequestDTO requestDTO) {
        AiClientRagOrder aiClientRagOrder = new AiClientRagOrder();
        BeanUtils.copyProperties(requestDTO, aiClientRagOrder);
        return aiClientRagOrder;
    }

    @Override
    @RequestMapping(value = "file/upload", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public Response<Boolean> uploadRagFile(@RequestParam("name") String name, @RequestParam("tag") String tag, @RequestParam("files") List<MultipartFile> files) {
        try {
            log.info("涓婁紶鐭ヨ瘑搴擄紝璇锋眰 {}", name);
            ragService.storeRagFile(name, tag, files);
            Response<Boolean> response = Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(true)
                    .build();
            log.info("涓婁紶鐭ヨ瘑搴擄紝缁撴灉 {} {}", name, response);
            return response;
        } catch (Exception e) {
            log.error("涓婁紶鐭ヨ瘑搴擄紝寮傚父 {}", name, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }

    /**
     * PO杞珼TO
     */
    private AiClientRagOrderResponseDTO convertToAiClientRagOrderResponseDTO(AiClientRagOrder aiClientRagOrder) {
        AiClientRagOrderResponseDTO responseDTO = new AiClientRagOrderResponseDTO();
        BeanUtils.copyProperties(aiClientRagOrder, responseDTO);
        return responseDTO;
    }

}

