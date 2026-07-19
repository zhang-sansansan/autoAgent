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
 * 浠诲姟鍒嗘瀽鑺傜偣
 *
 * @author xiaofuge bugstack.cn @灏忓倕鍝? * 2025/7/27 16:36
 */
@Slf4j
@Service
public class Step1AnalyzerNode extends AbstractExecuteSupport {

    @Override
    protected String doApply(ExecuteCommandEntity requestParameter, DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("\n馃幆 === 鎵ц绗?{} 姝?===", dynamicContext.getStep());

        // 鑾峰彇閰嶇疆淇℃伅
        AiAgentClientFlowConfigVO aiAgentClientFlowConfigVO = dynamicContext.getAiAgentClientFlowConfigVOMap().get(AiClientTypeEnumVO.TASK_ANALYZER_CLIENT.getCode());

        // 绗竴闃舵锛氫换鍔″垎鏋?        log.info("\n馃搳 闃舵1: 浠诲姟鐘舵€佸垎鏋?);
        String analysisPrompt = String.format(aiAgentClientFlowConfigVO.getStepPrompt(),
                requestParameter.getMessage(),
                dynamicContext.getStep(),
                dynamicContext.getMaxStep(),
                !dynamicContext.getExecutionHistory().isEmpty() ? dynamicContext.getExecutionHistory().toString() : "[棣栨鎵ц]",
                dynamicContext.getCurrentTask()
        );

        ChatClient chatClient = getChatClientByClientId(aiAgentClientFlowConfigVO.getClientId());

        String analysisResult = chatClient
                .prompt(analysisPrompt)
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, requestParameter.getSessionId())
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 1024))
                .call().content();

        assert analysisResult != null;
        parseAnalysisResult(dynamicContext, analysisResult, requestParameter.getSessionId());
        
        // 灏嗗垎鏋愮粨鏋滀繚瀛樺埌鍔ㄦ€佷笂涓嬫枃涓紝渚涗笅涓€姝ヤ娇鐢?        dynamicContext.setValue("analysisResult", analysisResult);

        // 妫€鏌ユ槸鍚﹀凡瀹屾垚
        if (analysisResult.contains("浠诲姟鐘舵€? COMPLETED") ||
                analysisResult.contains("瀹屾垚搴﹁瘎浼? 100%")) {
            dynamicContext.setCompleted(true);
            log.info("鉁?浠诲姟鍒嗘瀽鏄剧ず宸插畬鎴愶紒");
            return router(requestParameter, dynamicContext);
        }

        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<ExecuteCommandEntity, DefaultAutoAgentExecuteStrategyFactory.DynamicContext, String> get(ExecuteCommandEntity requestParameter, DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        // 濡傛灉浠诲姟宸插畬鎴愭垨杈惧埌鏈€澶ф鏁帮紝杩涘叆鎬荤粨闃舵
        if (dynamicContext.isCompleted() || dynamicContext.getStep() > dynamicContext.getMaxStep()) {
            return getBean("step4LogExecutionSummaryNode");
        }
        
        // 鍚﹀垯缁х画鎵ц涓嬩竴姝?        return getBean("step2PrecisionExecutorNode");
    }

    private void parseAnalysisResult(DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext, String analysisResult, String sessionId) {
        int step = dynamicContext.getStep();
        log.info("\n馃搳 === 绗?{} 姝ュ垎鏋愮粨鏋?===", step);
        
        String[] lines = analysisResult.split("\n");
        String currentSection = "";
        StringBuilder sectionContent = new StringBuilder();

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.contains("浠诲姟鐘舵€佸垎鏋?")) {
                // 鍙戦€佷笂涓€涓猻ection鐨勫唴瀹?                sendAnalysisSubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
                currentSection = "analysis_status";
                sectionContent = new StringBuilder();
                log.info("\n馃幆 浠诲姟鐘舵€佸垎鏋?");
                continue;
            } else if (line.contains("鎵ц鍘嗗彶璇勪及:")) {
                // 鍙戦€佷笂涓€涓猻ection鐨勫唴瀹?                sendAnalysisSubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
                currentSection = "analysis_history";
                sectionContent = new StringBuilder();
                log.info("\n馃搱 鎵ц鍘嗗彶璇勪及:");
                continue;
            } else if (line.contains("涓嬩竴姝ョ瓥鐣?")) {
                // 鍙戦€佷笂涓€涓猻ection鐨勫唴瀹?                sendAnalysisSubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
                currentSection = "analysis_strategy";
                sectionContent = new StringBuilder();
                log.info("\n馃殌 涓嬩竴姝ョ瓥鐣?");
                continue;
            } else if (line.contains("瀹屾垚搴﹁瘎浼?")) {
                // 鍙戦€佷笂涓€涓猻ection鐨勫唴瀹?                sendAnalysisSubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
                currentSection = "analysis_progress";
                sectionContent = new StringBuilder();
                String progress = line.substring(line.indexOf(":") + 1).trim();
                log.info("\n馃搳 瀹屾垚搴﹁瘎浼? {}", progress);
                sectionContent.append(line).append("\n");
                continue;
            } else if (line.contains("浠诲姟鐘舵€?")) {
                // 鍙戦€佷笂涓€涓猻ection鐨勫唴瀹?                sendAnalysisSubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
                currentSection = "analysis_task_status";
                sectionContent = new StringBuilder();
                String status = line.substring(line.indexOf(":") + 1).trim();
                if (status.equals("COMPLETED")) {
                    log.info("\n鉁?浠诲姟鐘舵€? 宸插畬鎴?);
                } else {
                    log.info("\n馃攧 浠诲姟鐘舵€? 缁х画鎵ц");
                }
                sectionContent.append(line).append("\n");
                continue;
            }

            // 鏀堕泦褰撳墠section鐨勫唴瀹?            if (!currentSection.isEmpty()) {
                sectionContent.append(line).append("\n");
                switch (currentSection) {
                    case "analysis_status":
                        log.info("   馃搵 {}", line);
                        break;
                    case "analysis_history":
                        log.info("   馃搳 {}", line);
                        break;
                    case "analysis_strategy":
                        log.info("   馃幆 {}", line);
                        break;
                    default:
                        log.info("   馃摑 {}", line);
                        break;
                }
            }
        }
        
        // 鍙戦€佹渶鍚庝竴涓猻ection鐨勫唴瀹?        sendAnalysisSubResult(dynamicContext, currentSection, sectionContent.toString(), sessionId);
    }

    /**
     * 鍙戦€佸垎鏋愰樁娈电粏鍒嗙粨鏋滃埌娴佸紡杈撳嚭
     */
    private void sendAnalysisSubResult(DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext, 
                                      String subType, String content, String sessionId) {
        if (!subType.isEmpty() && !content.isEmpty()) {
            AutoAgentExecuteResultEntity result = AutoAgentExecuteResultEntity.createAnalysisSubResult(
                    dynamicContext.getStep(), subType, content, sessionId);
            sendSseResult(dynamicContext, result);
        }
    }

}

