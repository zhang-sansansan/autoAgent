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
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * 固定执行策略
 *
 * @author xiaofuge bugstack.cn @小傅哥
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
        try {
            List<AiAgentClientFlowConfigVO> aiAgentClientList =
                    repository.queryAiAgentClientsByAgentId(requestParameter.getAiAgentId());
            String content = "";

            if (aiAgentClientList == null || aiAgentClientList.isEmpty()) {
                sendCompleteResult(emitter, requestParameter.getSessionId());
                return;
            }

            for (int index = 0; index < aiAgentClientList.size(); index++) {
                AiAgentClientFlowConfigVO config = aiAgentClientList.get(index);
                ChatClient chatClient = getChatClientByClientId(config.getClientId());
                ChatClient.ChatClientRequestSpec prompt = chatClient
                        .prompt(requestParameter.getMessage() + "，" + content)
                        .system(s -> s.param("current_date", LocalDate.now().toString()))
                        .advisors(a -> a
                                .param(CHAT_MEMORY_CONVERSATION_ID_KEY, requestParameter.getSessionId())
                                .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100));

                boolean finalClient = index == aiAgentClientList.size() - 1;
                if (finalClient) {
                    content = streamFinalContent(
                            prompt.stream().content(), emitter, requestParameter.getSessionId());
                } else {
                    String intermediateContent = prompt.call().content();
                    content = intermediateContent == null ? "" : intermediateContent;
                }
                log.info("智能体对话进行，客户端ID {}", config.getClientId());
            }

            log.info("智能体对话请求，结果 {} {}", requestParameter.getAiAgentId(), content);
        } catch (Exception e) {
            try {
                sendEvent(emitter, AutoAgentExecuteResultEntity.createErrorResult(
                        "执行异常：" + e.getMessage(), requestParameter.getSessionId()));
            } catch (Exception sendException) {
                log.error("发送错误事件失败：{}", sendException.getMessage(), sendException);
            }
            throw e;
        }
    }

    private ChatClient getChatClientByClientId(String clientId) {
        return getBean(AiAgentEnumVO.AI_CLIENT.getBeanName(clientId));
    }

    private <T> T getBean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }
    
    String streamFinalContent(Flux<String> contentFlux, ResponseBodyEmitter emitter, String sessionId)
            throws Exception {
        StringBuilder content = new StringBuilder();
        for (String delta : contentFlux.toIterable()) {
            if (delta == null || delta.isEmpty()) {
                continue;
            }
            content.append(delta);
            sendEvent(emitter, AutoAgentExecuteResultEntity.createContentResult(delta, sessionId));
        }
        sendCompleteResult(emitter, sessionId);
        return content.toString();
    }

    /**
     * 发送完成标识到流式输出
     */
    private void sendCompleteResult(ResponseBodyEmitter emitter, String sessionId) throws Exception {
        sendEvent(emitter, AutoAgentExecuteResultEntity.createCompleteResult(sessionId));
        log.info("✅ 已发送完成标识");
    }

    private void sendEvent(ResponseBodyEmitter emitter, AutoAgentExecuteResultEntity result) throws Exception {
        emitter.send("data: " + JSON.toJSONString(result) + "\n\n");
    }

}
