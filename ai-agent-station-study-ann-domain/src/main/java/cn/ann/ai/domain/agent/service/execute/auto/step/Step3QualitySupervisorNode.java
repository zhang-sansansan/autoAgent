package cn.ann.ai.domain.agent.service.execute.auto.step;

import cn.ann.ai.domain.agent.model.entity.AutoAgentExecuteResultEntity;
import cn.ann.ai.domain.agent.model.entity.ExecuteCommandEntity;
import cn.ann.ai.domain.agent.model.valobj.AiAgentClientFlowConfigVO;
import cn.ann.ai.domain.agent.model.valobj.enums.AiClientTypeEnumVO;
import cn.ann.ai.domain.agent.service.execute.auto.step.AbstractExecuteSupport;
import cn.ann.ai.domain.agent.service.execute.auto.step.factory.DefaultAutoAgentExecuteStrategyFactory;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * 璐ㄩ噺鐩戠潱鑺傜偣
 *
 * @author xiaofuge bugstack.cn @灏忓倕鍝? * 2025/7/27 16:43
 */
@Slf4j
@Service
public class Step3QualitySupervisorNode extends AbstractExecuteSupport {

    @Override
    protected String doApply(ExecuteCommandEntity requestParameter, DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        // 绗笁闃舵锛氳川閲忕洃鐫?        log.info("\n馃攳 闃舵3: 璐ㄩ噺鐩戠潱妫€鏌?);
        
        // 浠庡姩鎬佷笂涓嬫枃涓幏鍙栨墽琛岀粨鏋?        String executionResult = dynamicContext.getValue("executionResult");
        if (executionResult == null || executionResult.trim().isEmpty()) {
            log.warn("鈿狅笍 鎵ц缁撴灉涓虹┖锛岃烦杩囪川閲忕洃鐫?);
            return "璐ㄩ噺鐩戠潱璺宠繃";
        }

        AiAgentClientFlowConfigVO aiAgentClientFlowConfigVO = dynamicContext.getAiAgentClientFlowConfigVOMap().get(AiClientTypeEnumVO.QUALITY_SUPERVISOR_CLIENT.getCode());
        
        String supervisionPrompt = String.format(aiAgentClientFlowConfigVO.getStepPrompt(), requestParameter.getMessage(), executionResult);

        // 鑾峰彇瀵硅瘽瀹㈡埛绔?        ChatClient chatClient = getChatClientByClientId(aiAgentClientFlowConfigVO.getClientId());

        String supervisionResult = chatClient
                .prompt(supervisionPrompt)
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, requestParameter.getSessionId())
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 1024))
                .call().content();

        assert supervisionResult != null;
        parseSupervisionResult(dynamicContext, supervisionResult, requestParameter.getSessionId());
        
        // 灏嗙洃鐫ｇ粨鏋滀繚瀛樺埌鍔ㄦ€佷笂涓嬫枃涓?        dynamicContext.setValue("supervisionResult", supervisionResult);
        
        // 鏍规嵁鐩戠潱缁撴灉鍐冲畾鏄惁闇€瑕侀噸鏂版墽琛?        if (supervisionResult.contains("鏄惁閫氳繃: FAIL")) {
            log.info("鉂?璐ㄩ噺妫€鏌ユ湭閫氳繃锛岄渶瑕侀噸鏂版墽琛?);
            dynamicContext.setCurrentTask("鏍规嵁璐ㄩ噺鐩戠潱鐨勫缓璁噸鏂版墽琛屼换鍔?);
        } else if (supervisionResult.contains("鏄惁閫氳繃: OPTIMIZE")) {
            log.info("馃敡 璐ㄩ噺妫€鏌ュ缓璁紭鍖栵紝缁х画鏀硅繘");
            dynamicContext.setCurrentTask("鏍规嵁璐ㄩ噺鐩戠潱鐨勫缓璁紭鍖栨墽琛岀粨鏋?);
        } else {
            log.info("鉁?璐ㄩ噺妫€鏌ラ€氳繃");
            dynamicContext.setCompleted(true);
        }
        
        // 鏇存柊鎵ц鍘嗗彶
        String stepSummary = String.format("""
                === 绗?%d 姝ュ畬鏁磋褰?===
                銆愬垎鏋愰樁娈点€?s
                銆愭墽琛岄樁娈点€?s
                銆愮洃鐫ｉ樁娈点€?s
                """, dynamicContext.getStep(), 
                dynamicContext.getValue("analysisResult"), 
                executionResult, 
                supervisionResult);
        
        dynamicContext.getExecutionHistory().append(stepSummary);
        
        // 澧炲姞姝ラ璁℃暟
        dynamicContext.setStep(dynamicContext.getStep() + 1);
        
        // 濡傛灉浠诲姟宸插畬鎴愭垨杈惧埌鏈€澶ф鏁帮紝杩涘叆鎬荤粨闃舵
        if (dynamicContext.isCompleted() || dynamicContext.getStep() > dynamicContext.getMaxStep()) {
            return router(requestParameter, dynamicContext);
        }
        
        // 鍚﹀垯缁х画涓嬩竴杞墽琛岋紝杩斿洖鍒癝tep1AnalyzerNode
        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<ExecuteCommandEntity, DefaultAutoAgentExecuteStrategyFactory.DynamicContext, String> get(ExecuteCommandEntity requestParameter, DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        // 濡傛灉浠诲姟宸插畬鎴愭垨杈惧埌鏈€澶ф鏁帮紝杩涘叆鎬荤粨闃舵
        if (dynamicContext.isCompleted() || dynamicContext.getStep() > dynamicContext.getMaxStep()) {
            return getBean("step4LogExecutionSummaryNode");
        }
        
        // 鍚﹀垯杩斿洖鍒癝tep1AnalyzerNode杩涜涓嬩竴杞垎鏋?        return getBean("step1AnalyzerNode");
    }
    
    /**
     * 瑙ｆ瀽鐩戠潱缁撴灉
     */
    private void parseSupervisionResult(DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext, String supervisionResult, String sessionId) {
        int step = dynamicContext.getStep();
        log.info("\n馃攳 === 绗?{} 姝ョ洃鐫ｇ粨鏋?===", step);
        
        String[] lines = supervisionResult.split("\n");
        String currentSection = "";
        StringBuilder sectionContent = new StringBuilder();
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            if (line.contains("璐ㄩ噺璇勪及:")) {
                // 鍙戦€佸墠涓€涓儴鍒嗙殑鍐呭
                sendSupervisionSubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
                currentSection = "assessment";
                sectionContent.setLength(0);
                log.info("\n馃搳 璐ㄩ噺璇勪及:");
                continue;
            } else if (line.contains("闂璇嗗埆:")) {
                // 鍙戦€佸墠涓€涓儴鍒嗙殑鍐呭
                sendSupervisionSubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
                currentSection = "issues";
                sectionContent.setLength(0);
                log.info("\n鈿狅笍 闂璇嗗埆:");
                continue;
            } else if (line.contains("鏀硅繘寤鸿:")) {
                // 鍙戦€佸墠涓€涓儴鍒嗙殑鍐呭
                sendSupervisionSubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
                currentSection = "suggestions";
                sectionContent.setLength(0);
                log.info("\n馃挕 鏀硅繘寤鸿:");
                continue;
            } else if (line.contains("璐ㄩ噺璇勫垎:")) {
                // 鍙戦€佸墠涓€涓儴鍒嗙殑鍐呭
                sendSupervisionSubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
                currentSection = "score";
                sectionContent.setLength(0);
                String score = line.substring(line.indexOf(":") + 1).trim();
                log.info("\n馃搳 璐ㄩ噺璇勫垎: {}", score);
                sectionContent.append(score);
                continue;
            } else if (line.contains("鏄惁閫氳繃:")) {
                // 鍙戦€佸墠涓€涓儴鍒嗙殑鍐呭
                sendSupervisionSubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
                currentSection = "pass";
                sectionContent.setLength(0);
                String status = line.substring(line.indexOf(":") + 1).trim();
                if (status.equals("PASS")) {
                    log.info("\n鉁?妫€鏌ョ粨鏋? 閫氳繃");
                } else if (status.equals("FAIL")) {
                    log.info("\n鉂?妫€鏌ョ粨鏋? 鏈€氳繃");
                } else {
                    log.info("\n馃敡 妫€鏌ョ粨鏋? 闇€瑕佷紭鍖?);
                }
                sectionContent.append(status);
                continue;
            }
            
            // 鏀堕泦褰撳墠閮ㄥ垎鐨勫唴瀹?            if (!currentSection.isEmpty()) {
                if (!sectionContent.isEmpty()) {
                    sectionContent.append("\n");
                }
                sectionContent.append(line);
            }
            
            switch (currentSection) {
                case "assessment":
                    log.info("   馃搵 {}", line);
                    break;
                case "issues":
                    log.info("   鈿狅笍 {}", line);
                    break;
                case "suggestions":
                    log.info("   馃挕 {}", line);
                    break;
                default:
                    log.info("   馃摑 {}", line);
                    break;
            }
        }
        
        // 鍙戦€佹渶鍚庝竴涓儴鍒嗙殑鍐呭
        sendSupervisionSubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
        
        // 鍙戦€佸畬鏁寸殑鐩戠潱缁撴灉
        sendSupervisionResult(dynamicContext, supervisionResult, sessionId);
    }
    
    /**
     * 鍙戦€佺洃鐫ｇ粨鏋滃埌娴佸紡杈撳嚭
     */
    private void sendSupervisionResult(DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext, 
                                     String supervisionResult, String sessionId) {
        AutoAgentExecuteResultEntity result = AutoAgentExecuteResultEntity.createSupervisionResult(
                dynamicContext.getStep(), supervisionResult, sessionId);
        sendSseResult(dynamicContext, result);
    }
    
    /**
     * 鍙戦€佺洃鐫ｅ瓙缁撴灉鍒版祦寮忚緭鍑猴紙缁嗙矑搴︽爣璇嗭級
     */
    private void sendSupervisionSubResult(DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext,
                                        String section, String content, String sessionId) {
        // 鎶藉彇鐨勯€氱敤鍒ゆ柇閫昏緫
        if (!content.isEmpty() && !section.isEmpty()) {
            AutoAgentExecuteResultEntity result = AutoAgentExecuteResultEntity.createSupervisionSubResult(
                    dynamicContext.getStep(), section, content, sessionId);
            sendSseResult(dynamicContext, result);
        }
    }

}

