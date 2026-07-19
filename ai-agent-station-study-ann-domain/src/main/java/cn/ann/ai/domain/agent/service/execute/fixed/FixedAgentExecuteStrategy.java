package cn.ann.ai.domain.agent.service.execute.fixed;

import cn.ann.ai.domain.agent.adapter.repository.IAgentRepository;
import cn.ann.ai.domain.agent.model.entity.AutoAgentExecuteResultEntity;
import cn.ann.ai.domain.agent.model.entity.ExecuteCommandEntity;
import cn.ann.ai.domain.agent.model.valobj.AiAgentClientFlowConfigVO;
import cn.ann.ai.domain.agent.model.valobj.enums.AiAgentEnumVO;
import cn.ann.ai.domain.agent.service.IExecuteStrategy;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * 鍥哄畾鎵ц绛栫暐
 *
 * @author xiaofuge bugstack.cn @灏忓倕鍝?
 * 2025/9/13 15:14
 */
@Slf4j
@Service("fixedAgentExecuteStrategy")
public class FixedAgentExecuteStrategy implements IExecuteStrategy {

    @Resource
    private IAgentRepository repository;

    @Resource
    protected ApplicationContext applicationContext;

    public static final String CHAT_MEMORY_CONVERSATION_ID_KEY = "chat_memory_conversation_id";
    public static final String CHAT_MEMORY_RETRIEVE_SIZE_KEY = "chat_memory_response_size";

    @Override
    public void execute(ExecuteCommandEntity requestParameter, ResponseBodyEmitter emitter) throws Exception {
        // 1. 鑾峰彇閰嶇疆瀹㈡埛绔?
        List<AiAgentClientFlowConfigVO> aiAgentClientList = repository.queryAiAgentClientsByAgentId(requestParameter.getAiAgentId());

        // 2. 寰幆鎵ц瀹㈡埛绔?
        String content = "";

        for (AiAgentClientFlowConfigVO config : aiAgentClientList) {
            ChatClient chatClient = getChatClientByClientId(config.getClientId());

            content = chatClient.prompt(requestParameter.getMessage() + "锛? + content)
                    .system(s -> s.param("current_date", LocalDate.now().toString()))
                    .advisors(a -> a
                            .param(CHAT_MEMORY_CONVERSATION_ID_KEY, requestParameter.getSessionId())
                            .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                    .call().content();

            log.info("鏅鸿兘浣撳璇濊繘琛岋紝瀹㈡埛绔疘D {}", requestParameter.getAiAgentId());
        }

        log.info("鏅鸿兘浣撳璇濊姹傦紝缁撴灉 {} {}", requestParameter.getAiAgentId(), content);
        
        // 鍙戦€佹渶缁堢粨鏋滈€氱煡锛堢‘淇?content 涓嶄负绌猴級
        if (content != null && !content.trim().isEmpty()) {
            sendFinalResult(emitter, content, requestParameter.getSessionId());
        }
        
        // 鍙戦€佸畬鎴愭爣璇?
        sendCompleteResult(emitter, requestParameter.getSessionId());
    }

    private ChatClient getChatClientByClientId(String clientId) {
        return getBean(AiAgentEnumVO.AI_CLIENT.getBeanName(clientId));
    }

    private <T> T getBean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }
    
    /**
     * 鍙戦€佹渶缁堢粨鏋滃埌娴佸紡杈撳嚭
     */
    private void sendFinalResult(ResponseBodyEmitter emitter, String content, String sessionId) {
        try {
            AutoAgentExecuteResultEntity result = AutoAgentExecuteResultEntity.createSummaryResult(content, sessionId);
            String sseData = "data: " + JSON.toJSONString(result) + "\n\n";
            emitter.send(sseData);
            log.info("鉁?宸插彂閫佹渶缁堢粨鏋?);
        } catch (Exception e) {
            log.error("鍙戦€佹渶缁堢粨鏋滃け璐ワ細{}", e.getMessage(), e);
        }
    }
    
    /**
     * 鍙戦€佸畬鎴愭爣璇嗗埌娴佸紡杈撳嚭
     */
    private void sendCompleteResult(ResponseBodyEmitter emitter, String sessionId) {
        try {
            AutoAgentExecuteResultEntity result = AutoAgentExecuteResultEntity.createCompleteResult(sessionId);
            String sseData = "data: " + JSON.toJSONString(result) + "\n\n";
            emitter.send(sseData);
            log.info("鉁?宸插彂閫佸畬鎴愭爣璇?);
        } catch (Exception e) {
            log.error("鍙戦€佸畬鎴愭爣璇嗗け璐ワ細{}", e.getMessage(), e);
        }
    }

}

