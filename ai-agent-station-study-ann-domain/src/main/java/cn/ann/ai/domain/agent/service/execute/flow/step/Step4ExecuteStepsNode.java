package cn.ann.ai.domain.agent.service.execute.flow.step;

import cn.ann.ai.domain.agent.model.entity.AutoAgentExecuteResultEntity;
import cn.ann.ai.domain.agent.model.entity.ExecuteCommandEntity;
import cn.ann.ai.domain.agent.model.valobj.AiAgentClientFlowConfigVO;
import cn.ann.ai.domain.agent.model.valobj.enums.AiClientTypeEnumVO;
import cn.ann.ai.domain.agent.service.execute.flow.step.factory.DefaultFlowAgentExecuteStrategyFactory;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 绗洓姝ワ細鎸夐『搴忔墽琛岃鍒掓楠よ妭鐐? *
 * @author xiaofuge bugstack.cn @灏忓倕鍝? * 2025/8/25 10:30
 */
@Slf4j
@Component
public class Step4ExecuteStepsNode extends AbstractExecuteSupport {

    @Override
    public String doApply(ExecuteCommandEntity request, DefaultFlowAgentExecuteStrategyFactory.DynamicContext dynamicContext) {
        log.info("寮€濮嬫墽琛岀鍥涙锛氭寜椤哄簭鎵ц瑙勫垝姝ラ");
        
        try {
            // 鑾峰彇閰嶇疆淇℃伅
            AiAgentClientFlowConfigVO aiAgentClientFlowConfigVO = dynamicContext.getAiAgentClientFlowConfigVOMap().get(AiClientTypeEnumVO.EXECUTOR_CLIENT.getCode());

            // 鑾峰彇瑙勫垝瀹㈡埛绔?            ChatClient executorChatClient = getChatClientByClientId(aiAgentClientFlowConfigVO.getClientId());

            // 浠庡姩鎬佷笂涓嬫枃鑾峰彇瑙ｆ瀽鐨勬楠?            Map<String, String> stepsMap = dynamicContext.getValue("stepsMap");
            
            if (stepsMap == null || stepsMap.isEmpty()) {
                return "姝ラ鏄犲皠涓虹┖锛屾棤娉曟墽琛?;
            }
            
            // 鎸夐『搴忔墽琛岃鍒掓楠?            executeStepsInOrder(executorChatClient, stepsMap, dynamicContext);
            
            // 鍙戦€丼SE缁撴灉
            AutoAgentExecuteResultEntity result = AutoAgentExecuteResultEntity.createExecutionResult(
                    dynamicContext.getStep(),
                    "宸插畬鎴愭墍鏈夎鍒掓楠ょ殑鎵ц",
                    request.getSessionId()
            );
            sendSseResult(dynamicContext, result);
            
            // 鍙戦€佹€荤粨缁撴灉鍒般€愭渶缁堟墽琛岀粨鏋溿€戝尯鍩?            sendSummaryResult(dynamicContext, request.getSessionId());
            
            // 鍙戦€佸畬鎴愭爣璇?            sendCompleteResult(dynamicContext, request.getSessionId());

            // 鏇存柊姝ラ
            dynamicContext.setStep(dynamicContext.getStep() + 1);
            dynamicContext.setCompleted(true);
            
            log.info("绗洓姝ユ墽琛屽畬鎴愶細鎵€鏈夎鍒掓楠ゅ凡鎵ц");

            return "鎵€鏈夎鍒掓楠ゆ墽琛屽畬鎴?;
        } catch (Exception e) {
            log.error("绗洓姝ユ墽琛屽け璐?, e);
            return "鎵ц姝ラ澶辫触: " + e.getMessage();
        }
    }

    @Override
    public StrategyHandler<ExecuteCommandEntity, DefaultFlowAgentExecuteStrategyFactory.DynamicContext, String> get(ExecuteCommandEntity request, DefaultFlowAgentExecuteStrategyFactory.DynamicContext dynamicContext) {
        return defaultStrategyHandler;
    }
    
    /**
     * 鎸夐『搴忔墽琛岃鍒掓楠?     */
    private void executeStepsInOrder(ChatClient executorChatClient, Map<String, String> stepsMap, DefaultFlowAgentExecuteStrategyFactory.DynamicContext dynamicContext) {
        if (stepsMap == null || stepsMap.isEmpty()) {
            log.warn("姝ラ鏄犲皠涓虹┖锛屾棤娉曟墽琛?);
            return;
        }

        // 鎸夋楠ょ紪鍙锋帓搴忔墽琛?        List<Integer> stepNumbers = new ArrayList<>();
        for (String stepKey : stepsMap.keySet()) {
            try {
                // 浠?绗?姝?銆?绗?姝?绛夋牸寮忎腑鎻愬彇鏁板瓧
                Pattern numberPattern = Pattern.compile("绗?\\d+)姝?);
                Matcher matcher = numberPattern.matcher(stepKey);
                if (matcher.find()) {
                    stepNumbers.add(Integer.parseInt(matcher.group(1)));
                }
            } catch (NumberFormatException e) {
                log.warn("鏃犳硶瑙ｆ瀽姝ラ缂栧彿: {}", stepKey);
            }
        }

        // 鎺掑簭姝ラ缂栧彿
        stepNumbers.sort(Integer::compareTo);

        // 鎸夐『搴忔墽琛屾瘡涓楠?        for (Integer stepNumber : stepNumbers) {
            String stepKey = "绗? + stepNumber + "姝?;
            String stepContent = null;

            // 鏌ユ壘鍖归厤鐨勬楠ゅ唴瀹?            for (Map.Entry<String, String> entry : stepsMap.entrySet()) {
                if (entry.getKey().startsWith(stepKey)) {
                    stepContent = entry.getValue();
                    break;
                }
            }

            if (stepContent != null) {
                executeStep(executorChatClient, stepNumber, stepKey, stepContent, dynamicContext);
            } else {
                log.warn("鏈壘鍒版楠ゅ唴瀹? {}", stepKey);
            }
        }
    }
    
    /**
     * 鎵ц鍗曚釜姝ラ
     */
    private void executeStep(ChatClient executorChatClient, Integer stepNumber, String stepKey, String stepContent, DefaultFlowAgentExecuteStrategyFactory.DynamicContext dynamicContext) {
        log.info("\n--- 寮€濮嬫墽琛?{} ---", stepKey);
        log.info("姝ラ鍐呭: {}", stepContent.substring(0, Math.min(200, stepContent.length())) + "...");

        try {
            // 鏇存柊鎵ц涓婁笅鏂?            dynamicContext.setValue("currentStep", stepNumber);
            dynamicContext.setValue("currentStepKey", stepKey);
            dynamicContext.setValue("currentStepContent", stepContent);

            // 浣跨敤鎵ц鍣–hatClient鏉ユ墽琛屽叿浣撴楠?            String executionResult = executorChatClient.prompt()
                    .user(buildStepExecutionPrompt(stepContent, dynamicContext))
                    .call()
                    .content();

            assert executionResult != null;
            log.info("姝ラ {} 鎵ц缁撴灉: {}", stepNumber, executionResult.substring(0, Math.min(150, executionResult.length())) + "...");

            // 淇濆瓨鎵ц缁撴灉
            dynamicContext.setValue("step" + stepNumber + "Result", executionResult);
            
            // 鍙戦€佹楠ゆ墽琛岀粨鏋滅殑SSE
            AutoAgentExecuteResultEntity stepResult = AutoAgentExecuteResultEntity.createExecutionResult(
                    stepNumber,
                    stepKey + " 鎵ц瀹屾垚: " + executionResult.substring(0, Math.min(500, executionResult.length())),
                    (String) dynamicContext.getValue("sessionId")
            );
            sendSseResult(dynamicContext, stepResult);

            // 鐭殏寤惰繜锛岄伩鍏嶈姹傝繃浜庨绻?            Thread.sleep(1000);

        } catch (Exception e) {
            log.error("鎵ц姝ラ {} 鏃跺彂鐢熼敊璇? {}", stepNumber, e.getMessage());
            dynamicContext.setValue("step" + stepNumber + "Error", e.getMessage());

            // 璁板綍閿欒浣嗙户缁墽琛屼笅涓€姝?            handleStepExecutionError(stepNumber, stepKey, e, dynamicContext);
        }

        log.info("--- 瀹屾垚鎵ц {} ---", stepKey);
    }
    
    /**
     * 澶勭悊姝ラ鎵ц閿欒
     */
    private void handleStepExecutionError(Integer stepNumber, String stepKey, Exception e, DefaultFlowAgentExecuteStrategyFactory.DynamicContext dynamicContext) {
        log.warn("姝ラ {} 鎵ц澶辫触锛屽皾璇曟仮澶嶇瓥鐣?, stepNumber);

        // 璁板綍閿欒缁熻
        Map<String, Integer> errorStats = dynamicContext.getValue("stepErrorStats");
        if (errorStats == null) {
            errorStats = new HashMap<>();
            dynamicContext.setValue("stepErrorStats", errorStats);
        }
        errorStats.put("step" + stepNumber, errorStats.getOrDefault("step" + stepNumber, 0) + 1);

        // 濡傛灉鏄綉缁滈敊璇紝鍙互灏濊瘯閲嶈瘯
        if (e.getMessage() != null && (e.getMessage().contains("timeout") || e.getMessage().contains("connection"))) {
            log.info("妫€娴嬪埌缃戠粶閿欒锛屽皢鍦ㄥ悗缁噸璇曟満鍒朵腑澶勭悊");
        }

        // 鏍囪姝ラ涓洪儴鍒嗗畬鎴愮姸鎬?        dynamicContext.setValue("step" + stepNumber + "Status", "FAILED_WITH_ERROR");
        
        // 鍙戦€侀敊璇粨鏋滅殑SSE
        try {
            AutoAgentExecuteResultEntity errorResult = AutoAgentExecuteResultEntity.createExecutionResult(
                    stepNumber,
                    stepKey + " 鎵ц澶辫触: " + e.getMessage(),
                    dynamicContext.getValue("sessionId")
            );
            sendSseResult(dynamicContext, errorResult);
        } catch (Exception sseException) {
            log.error("鍙戦€侀敊璇疭SE缁撴灉澶辫触", sseException);
        }
    }
    
    /**
     * 鏋勫缓姝ラ鎵ц鎻愮ず璇?     */
    private String buildStepExecutionPrompt(String stepContent, DefaultFlowAgentExecuteStrategyFactory.DynamicContext dynamicContext) {
        return "浣犳槸涓€涓櫤鑳芥墽琛屽姪鎵嬶紝闇€瑕佹墽琛屼互涓嬫楠?\n\n" +
                "**姝ラ鍐呭:**\n" +
                stepContent + "\n\n" +
                "**鐢ㄦ埛鍘熷璇锋眰:**\n" +
                dynamicContext.getCurrentTask() + "\n\n" +
                "**鎵ц瑕佹眰:**\n" +
                "1. 浠旂粏鍒嗘瀽姝ラ鍐呭锛岀悊瑙ｉ渶瑕佹墽琛岀殑鍏蜂綋浠诲姟\n" +
                "2. 濡傛灉娑夊強MCP宸ュ叿璋冪敤锛岃浣跨敤鐩稿簲鐨勫伐鍏穃n" +
                "3. 鎻愪緵璇︾粏鐨勬墽琛岃繃绋嬪拰缁撴灉\n" +
                "4. 濡傛灉閬囧埌闂锛岃璇存槑鍏蜂綋鐨勯敊璇俊鎭痋n" +
                "5. **閲嶈**: 鎵ц瀹屾垚鍚庯紝蹇呴』鍦ㄥ洖澶嶆湯灏炬槑纭緭鍑烘墽琛岀粨鏋滐紝鏍煎紡濡備笅:\n" +
                "   ```\n" +
                "   === 鎵ц缁撴灉 ===\n" +
                "   鐘舵€? [鎴愬姛/澶辫触]\n" +
                "   缁撴灉鎻忚堪: [鍏蜂綋鐨勬墽琛岀粨鏋滄弿杩癩\n" +
                "   杈撳嚭鏁版嵁: [濡傛灉鏈夊叿浣撶殑杈撳嚭鏁版嵁锛岃鍦ㄦ鍒楀嚭]\n" +
                "   ```\n\n" +
                "璇峰紑濮嬫墽琛岃繖涓楠わ紝骞朵弗鏍兼寜鐓ц姹傛彁渚涜缁嗙殑鎵ц鎶ュ憡鍜岀粨鏋滆緭鍑恒€?;
    }

    /**
     * 鍙戦€佹€荤粨缁撴灉鍒版祦寮忚緭鍑?     */
       private void sendSummaryResult(DefaultFlowAgentExecuteStrategyFactory.DynamicContext dynamicContext, String sessionId) {
        // 鏋勫缓鎵ц鎬荤粨鍐呭
        StringBuilder summaryContent = new StringBuilder();
        summaryContent.append("## 鎵ц姝ラ瀹屾垚鎬荤粨\n\n");
        
        // 鑾峰彇鎵ц鍘嗗彶
        StringBuilder executionHistory = dynamicContext.getExecutionHistory();
        if (executionHistory != null && executionHistory.length() > 0) {
            summaryContent.append("### 宸插畬鎴愮殑宸ヤ綔\n");
            summaryContent.append(executionHistory.toString());
            summaryContent.append("\n\n");
        }
        
        summaryContent.append("### 鎵ц鐘舵€乗n");
        summaryContent.append("鉁?鎵€鏈夎鍒掓楠ゅ凡鎴愬姛鎵ц瀹屾垚\n\n");
        
        summaryContent.append("### 鎵ц鏁堟灉璇勪及\n");
        summaryContent.append("馃搳 浠诲姟鎵ц娴佺▼椤哄埄瀹屾垚锛屽悇姝ラ鎸夎鍒掓墽琛?);
        
        AutoAgentExecuteResultEntity result = AutoAgentExecuteResultEntity.createSummaryResult(
                summaryContent.toString(), sessionId);
        sendSseResult(dynamicContext, result);
        log.info("馃搳 宸插彂閫佹€荤粨缁撴灉鍒般€愭渶缁堟墽琛岀粨鏋溿€戝尯鍩?);
    }
    
    /**
     * 鍙戦€佸畬鎴愭爣璇嗗埌娴佸紡杈撳嚭
     */
    private void sendCompleteResult(DefaultFlowAgentExecuteStrategyFactory.DynamicContext dynamicContext, String sessionId) {
        AutoAgentExecuteResultEntity result = AutoAgentExecuteResultEntity.createCompleteResult(sessionId);
        sendSseResult(dynamicContext, result);
        log.info("鉁?宸插彂閫佸畬鎴愭爣璇?);
    }
}
