package cn.ann.ai.domain.agent.service.execute.auto.step;

import cn.ann.ai.domain.agent.model.entity.AutoAgentExecuteResultEntity;
import cn.ann.ai.domain.agent.model.entity.ExecuteCommandEntity;
import cn.ann.ai.domain.agent.model.valobj.AiAgentClientFlowConfigVO;
import cn.ann.ai.domain.agent.model.valobj.enums.AiClientTypeEnumVO;
import cn.ann.ai.domain.agent.service.execute.auto.step.factory.DefaultAutoAgentExecuteStrategyFactory;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * 绮惧噯鎵ц鑺傜偣
 *
 * @author xiaofuge bugstack.cn @灏忓倕鍝? * 2025/7/27 16:42
 */
@Slf4j
@Service
public class Step2PrecisionExecutorNode extends AbstractExecuteSupport{

    @Override
    protected String doApply(ExecuteCommandEntity requestParameter, DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("\n鈿?闃舵2: 绮惧噯浠诲姟鎵ц");
        
        // 浠庡姩鎬佷笂涓嬫枃涓幏鍙栧垎鏋愮粨鏋?        String analysisResult = dynamicContext.getValue("analysisResult");
        if (analysisResult == null || analysisResult.trim().isEmpty()) {
            log.warn("鈿狅笍 鍒嗘瀽缁撴灉涓虹┖锛屼娇鐢ㄩ粯璁ゆ墽琛岀瓥鐣?);
            analysisResult = "鎵ц褰撳墠浠诲姟姝ラ";
        }

        AiAgentClientFlowConfigVO aiAgentClientFlowConfigVO = dynamicContext.getAiAgentClientFlowConfigVOMap().get(AiClientTypeEnumVO.PRECISION_EXECUTOR_CLIENT.getCode());

        String executionPrompt = String.format(aiAgentClientFlowConfigVO.getStepPrompt(), requestParameter.getMessage(), analysisResult);

        // 鑾峰彇瀵硅瘽瀹㈡埛绔?        ChatClient chatClient = getChatClientByClientId(aiAgentClientFlowConfigVO.getClientId());

        String executionResult = chatClient
                .prompt(executionPrompt)
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, requestParameter.getSessionId())
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 1024))
                .call().content();

        assert executionResult != null;
        parseExecutionResult(dynamicContext, executionResult, requestParameter.getSessionId());
        
        // 灏嗘墽琛岀粨鏋滀繚瀛樺埌鍔ㄦ€佷笂涓嬫枃涓紝渚涗笅涓€姝ヤ娇鐢?        dynamicContext.setValue("executionResult", executionResult);
        
        // 鏇存柊鎵ц鍘嗗彶
        String stepSummary = String.format("""
                === 绗?%d 姝ユ墽琛岃褰?===
                銆愬垎鏋愰樁娈点€?s
                銆愭墽琛岄樁娈点€?s
                """, dynamicContext.getStep(), analysisResult, executionResult);
        
        dynamicContext.getExecutionHistory().append(stepSummary);

        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<ExecuteCommandEntity, DefaultAutoAgentExecuteStrategyFactory.DynamicContext, String> get(ExecuteCommandEntity requestParameter, DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return getBean("step3QualitySupervisorNode");
    }
    
    /**
     * 瑙ｆ瀽鎵ц缁撴灉
     */
    private void parseExecutionResult(DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext, String executionResult, String sessionId) {
        int step = dynamicContext.getStep();
        log.info("\n鈿?=== 绗?{} 姝ユ墽琛岀粨鏋?===", step);
        
        String[] lines = executionResult.split("\n");
        String currentSection = "";
        StringBuilder sectionContent = new StringBuilder();
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            if (line.contains("鎵ц鐩爣:")) {
                // 鍙戦€佷笂涓€涓猻ection鐨勫唴瀹?                sendExecutionSubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
                currentSection = "execution_target";
                sectionContent = new StringBuilder();
                log.info("\n馃幆 鎵ц鐩爣:");
                continue;
            } else if (line.contains("鎵ц杩囩▼:")) {
                // 鍙戦€佷笂涓€涓猻ection鐨勫唴瀹?                sendExecutionSubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
                currentSection = "execution_process";
                sectionContent = new StringBuilder();
                log.info("\n馃敡 鎵ц杩囩▼:");
                continue;
            } else if (line.contains("鎵ц缁撴灉:")) {
                // 鍙戦€佷笂涓€涓猻ection鐨勫唴瀹?                sendExecutionSubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
                currentSection = "execution_result";
                sectionContent = new StringBuilder();
                log.info("\n馃搱 鎵ц缁撴灉:");
                continue;
            } else if (line.contains("璐ㄩ噺妫€鏌?")) {
                // 鍙戦€佷笂涓€涓猻ection鐨勫唴瀹?                sendExecutionSubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
                currentSection = "execution_quality";
                sectionContent = new StringBuilder();
                log.info("\n馃攳 璐ㄩ噺妫€鏌?");
                continue;
            }
            
            // 鏀堕泦褰撳墠section鐨勫唴瀹?            if (!currentSection.isEmpty()) {
                sectionContent.append(line).append("\n");
                switch (currentSection) {
                    case "execution_target":
                        log.info("   馃幆 {}", line);
                        break;
                    case "execution_process":
                        log.info("   鈿欙笍 {}", line);
                        break;
                    case "execution_result":
                        log.info("   馃搳 {}", line);
                        break;
                    case "execution_quality":
                        log.info("   鉁?{}", line);
                        break;
                    default:
                        log.info("   馃摑 {}", line);
                        break;
                }
            }
        }
        
        // 鍙戦€佹渶鍚庝竴涓猻ection鐨勫唴瀹?        sendExecutionSubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
    }
    
    /**
     * 鍙戦€佹墽琛岄樁娈电粏鍒嗙粨鏋滃埌娴佸紡杈撳嚭
     */
    private void sendExecutionSubResult(DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext, 
                                       String subType, String content, String sessionId) {
        // 鎶藉彇鐨勯€氱敤鍒ゆ柇閫昏緫
        if (!subType.isEmpty() && !content.isEmpty()) {
            AutoAgentExecuteResultEntity result = AutoAgentExecuteResultEntity.createExecutionSubResult(
                    dynamicContext.getStep(), subType, content, sessionId);
            sendSseResult(dynamicContext, result);
        }
    }
    
}

