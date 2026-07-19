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
 * 鎵ц鎬荤粨鑺傜偣
 *
 * @author xiaofuge bugstack.cn @灏忓倕鍝? * 2025/7/27 16:45
 */
@Slf4j
@Service
public class Step4LogExecutionSummaryNode extends AbstractExecuteSupport {

    @Override
    protected String doApply(ExecuteCommandEntity requestParameter, DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("\n馃搳 === 鎵ц绗?{} 姝?===", dynamicContext.getStep());

        // 绗洓闃舵锛氭墽琛屾€荤粨
        log.info("\n馃搳 闃舵4: 鎵ц鎬荤粨鍒嗘瀽");
        
        // 璁板綍鎵ц鎬荤粨
        logExecutionSummary(dynamicContext.getMaxStep(), dynamicContext.getExecutionHistory(), dynamicContext.isCompleted());
        
        // 鐢熸垚鏈€缁堟€荤粨鎶ュ憡锛堟棤璁轰换鍔℃槸鍚﹀畬鎴愰兘闇€瑕佺敓鎴愶級
        generateFinalReport(requestParameter, dynamicContext);
        
        log.info("\n馃弫 === 鍔ㄦ€佸杞墽琛岀粨鏉?====");
        
        return "ai agent execution summary completed!";
    }

    @Override
    public StrategyHandler<ExecuteCommandEntity, DefaultAutoAgentExecuteStrategyFactory.DynamicContext, String> get(ExecuteCommandEntity requestParameter, DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        // 鎬荤粨鑺傜偣鏄渶鍚庝竴涓妭鐐癸紝杩斿洖null琛ㄧず鎵ц缁撴潫
        return defaultStrategyHandler;
    }
    
    /**
     * 璁板綍鎵ц鎬荤粨
     */
    private void logExecutionSummary(int maxSteps, StringBuilder executionHistory, boolean isCompleted) {
        log.info("\n馃搳 === 鍔ㄦ€佸杞墽琛屾€荤粨 ====");
        
        int actualSteps = Math.min(maxSteps, executionHistory.toString().split("=== 绗?).length - 1);
        log.info("馃搱 鎬绘墽琛屾鏁? {} 姝?, actualSteps);
        
        if (isCompleted) {
            log.info("鉁?浠诲姟瀹屾垚鐘舵€? 宸插畬鎴?);
        } else {
            log.info("鈴革笍 浠诲姟瀹屾垚鐘舵€? 鏈畬鎴愶紙杈惧埌鏈€澶ф鏁伴檺鍒讹級");
        }
        
        // 璁＄畻鎵ц鏁堢巼
        double efficiency = isCompleted ? 100.0 : (double) actualSteps / maxSteps * 100;
        log.info("馃搳 鎵ц鏁堢巼: {}%", efficiency);
    }
    
    /**
     * 鐢熸垚鏈€缁堟€荤粨鎶ュ憡
     */
    private void generateFinalReport(ExecuteCommandEntity requestParameter, DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext) {
        try {
            boolean isCompleted = dynamicContext.isCompleted();
            log.info("\n--- 鐢熸垚{}浠诲姟鐨勬渶缁堢瓟妗?---", isCompleted ? "宸插畬鎴? : "鏈畬鎴?);

            AiAgentClientFlowConfigVO aiAgentClientFlowConfigVO = dynamicContext.getAiAgentClientFlowConfigVOMap().get(AiClientTypeEnumVO.RESPONSE_ASSISTANT.getCode());

            String summaryPrompt = getSummaryPrompt(aiAgentClientFlowConfigVO, requestParameter, dynamicContext, isCompleted);

            // 鑾峰彇瀵硅瘽瀹㈡埛绔?- 浣跨敤浠诲姟鍒嗘瀽瀹㈡埛绔繘琛屾€荤粨
            ChatClient chatClient = getChatClientByClientId(aiAgentClientFlowConfigVO.getClientId());
            
            String summaryResult = chatClient
                    .prompt(summaryPrompt)
                    .advisors(a -> a
                            .param(CHAT_MEMORY_CONVERSATION_ID_KEY, requestParameter.getSessionId() + "-summary")
                            .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 50))
                    .call().content();

            assert summaryResult != null;
            logFinalReport(dynamicContext, summaryResult, requestParameter.getSessionId());
            
            // 灏嗘€荤粨缁撴灉淇濆瓨鍒板姩鎬佷笂涓嬫枃涓?            dynamicContext.setValue("finalSummary", summaryResult);
            
        } catch (Exception e) {
            log.error("鐢熸垚鏈€缁堟€荤粨鎶ュ憡鏃跺嚭鐜板紓甯? {}", e.getMessage(), e);
        }
    }

    private static String getSummaryPrompt(AiAgentClientFlowConfigVO aiAgentClientFlowConfigVO, ExecuteCommandEntity requestParameter, DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext, boolean isCompleted) {
        String summaryPrompt;
        if (isCompleted) {
            summaryPrompt = String.format(aiAgentClientFlowConfigVO.getStepPrompt(),
                    requestParameter.getMessage(),
                    dynamicContext.getExecutionHistory().toString());
        } else {
            summaryPrompt = String.format("""
                    铏界劧浠诲姟鏈畬鍏ㄦ墽琛屽畬鎴愶紝浣嗚鍩轰簬宸叉湁鐨勬墽琛岃繃绋嬶紝灏藉姏鍥炵瓟鐢ㄦ埛鐨勫師濮嬮棶棰橈細
                    
                    **鐢ㄦ埛鍘熷闂:** %s
                    
                    **宸叉墽琛岀殑杩囩▼鍜岃幏寰楃殑淇℃伅:**
                    %s
                    
                    **瑕佹眰:**
                    1. 鍩轰簬宸叉湁淇℃伅锛屽敖鍔涘洖绛旂敤鎴风殑鍘熷闂
                    2. 濡傛灉淇℃伅涓嶈冻锛岃鏄庡摢浜涢儴鍒嗘棤娉曞畬鎴愬苟缁欏嚭鍘熷洜
                    3. 鎻愪緵宸茶兘纭畾鐨勯儴鍒嗙瓟妗?                    4. 缁欏嚭瀹屾垚鍓╀綑閮ㄥ垎鐨勫叿浣撳缓璁?                    5. 浠D璇硶鐨勮〃鏍煎舰寮忥紝浼樺寲灞曠ず缁撴灉鏁版嵁
                    
                    璇峰熀浜庣幇鏈変俊鎭粰鍑虹敤鎴烽棶棰樼殑绛旀锛?                    """,
                    requestParameter.getMessage(),
                    dynamicContext.getExecutionHistory().toString());
        }
        return summaryPrompt;
    }

    /**
     * 杈撳嚭鏈€缁堟€荤粨鎶ュ憡
     */
    private void logFinalReport(DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext, String summaryResult, String sessionId) {
        boolean isCompleted = dynamicContext.isCompleted();
        log.info("\n馃搵 === {}浠诲姟鏈€缁堟€荤粨鎶ュ憡 ===", isCompleted ? "宸插畬鎴? : "鏈畬鎴?);

        String[] lines = summaryResult.split("\n");
        String currentSection = "summary_overview";
        StringBuilder sectionContent = new StringBuilder();
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            // 妫€娴嬫槸鍚﹀紑濮嬫柊鐨勬€荤粨閮ㄥ垎
            String newSection = detectSummarySection(line);
            if (newSection != null && !newSection.equals(currentSection)) {
                // 鍙戦€佸墠涓€涓儴鍒嗙殑鍐呭
                if (!sectionContent.isEmpty()) {
                    sendSummarySubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
                }
                currentSection = newSection;
                sectionContent.setLength(0);
            }
            
            // 鏀堕泦褰撳墠閮ㄥ垎鐨勫唴瀹?            if (!sectionContent.isEmpty()) {
                sectionContent.append("\n");
            }
            sectionContent.append(line);
            
            // 鏍规嵁鍐呭绫诲瀷娣诲姞涓嶅悓鍥炬爣
            if (line.contains("宸插畬鎴?) || line.contains("瀹屾垚鐨勫伐浣?)) {
                log.info("鉁?{}", line);
            } else if (line.contains("鏈畬鎴?) || line.contains("鍘熷洜")) {
                log.info("鉂?{}", line);
            } else if (line.contains("寤鸿") || line.contains("鎺ㄨ崘")) {
                log.info("馃挕 {}", line);
            } else if (line.contains("璇勪及") || line.contains("鏁堟灉")) {
                log.info("馃搳 {}", line);
            } else {
                log.info("馃摑 {}", line);
            }
        }
        
        // 鍙戦€佹渶鍚庝竴涓儴鍒嗙殑鍐呭
        if (!sectionContent.isEmpty()) {
            sendSummarySubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
        }
        
        // 鍙戦€佸畬鏁寸殑鎬荤粨缁撴灉
        sendSummaryResult(dynamicContext, summaryResult, sessionId);
        
        // 鍙戦€佸畬鎴愭爣璇?        sendCompleteResult(dynamicContext, sessionId);
    }
    
    /**
     * 鍙戦€佹€荤粨缁撴灉鍒版祦寮忚緭鍑?     */
    private void sendSummaryResult(DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext, 
                                 String summaryResult, String sessionId) {
        AutoAgentExecuteResultEntity result = AutoAgentExecuteResultEntity.createSummaryResult(
                 summaryResult, sessionId);
        sendSseResult(dynamicContext, result);
    }
    
    /**
     * 鍙戦€佹€荤粨闃舵缁嗗垎缁撴灉鍒版祦寮忚緭鍑?     */
    private void sendSummarySubResult(DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext, 
                                     String subType, String content, String sessionId) {
        AutoAgentExecuteResultEntity result = AutoAgentExecuteResultEntity.createSummarySubResult(
                subType, content, sessionId);
        sendSseResult(dynamicContext, result);
    }
    
    /**
     * 鍙戦€佸畬鎴愭爣璇嗗埌娴佸紡杈撳嚭
     */
    private void sendCompleteResult(DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext, String sessionId) {
        AutoAgentExecuteResultEntity result = AutoAgentExecuteResultEntity.createCompleteResult(sessionId);
        sendSseResult(dynamicContext, result);
        log.info("鉁?宸插彂閫佸畬鎴愭爣璇?);
    }
    
    /**
     * 妫€娴嬫€荤粨閮ㄥ垎鏍囪瘑
     */
    private String detectSummarySection(String content) {
        if (content.contains("宸插畬鎴愮殑宸ヤ綔") || content.contains("瀹屾垚鐨勫伐浣?) || content.contains("宸ヤ綔鍐呭鍜屾垚鏋?)) {
            return "completed_work";
        } else if (content.contains("鏈畬鎴愮殑鍘熷洜") || content.contains("鏈畬鎴愬師鍥?)) {
            return "incomplete_reasons";
        } else if (content.contains("鍏抽敭鍥犵礌") || content.contains("瀹屾垚鐨勫叧閿洜绱?)) {
            return "key_factors";
        } else if (content.contains("鎵ц鏁堢巼") || content.contains("鎵ц鏁堢巼鍜岃川閲?)) {
            return "efficiency_quality";
        } else if (content.contains("瀹屾垚鍓╀綑浠诲姟鐨勫缓璁?) || content.contains("寤鸿") || content.contains("浼樺寲寤鸿") || content.contains("缁忛獙鎬荤粨")) {
            return "suggestions";
        } else if (content.contains("鏁翠綋鎵ц鏁堟灉") || content.contains("璇勪及")) {
            return "evaluation";
        }
        return null;
    }

}

