package cn.ann.ai.domain.agent.service.execute.flow.step;

import cn.ann.ai.domain.agent.model.entity.AutoAgentExecuteResultEntity;
import cn.ann.ai.domain.agent.model.entity.ExecuteCommandEntity;
import cn.ann.ai.domain.agent.service.execute.flow.step.factory.DefaultFlowAgentExecuteStrategyFactory;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 姝ラ3锛氳鍒掓楠よВ鏋愯妭鐐? *
 * @author xiaofuge bugstack.cn @灏忓倕鍝? * 2025/8/25 11:00
 */
@Slf4j
@Service
public class Step3ParseStepsNode extends AbstractExecuteSupport {

    @Resource
    private Step4ExecuteStepsNode step4ExecuteStepsNode;

    @Override
    protected String doApply(ExecuteCommandEntity requestParameter, DefaultFlowAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("\n--- 姝ラ3: 瑙勫垝姝ラ瑙ｆ瀽 ---");
        
        String planningResult = dynamicContext.getValue("planningResult");
        
        if (planningResult == null || planningResult.trim().isEmpty()) {
            log.warn("瑙勫垝缁撴灉涓虹┖锛屾棤娉曡В鏋愭楠?);
            throw new RuntimeException("瑙勫垝缁撴灉涓虹┖锛屾棤娉曡В鏋愭楠?);
        }
        
        Map<String, String> stepsMap = parseExecutionSteps(planningResult);
        
        log.info("鎴愬姛瑙ｆ瀽 {} 涓墽琛屾楠?, stepsMap.size());
        
        // 淇濆瓨瑙ｆ瀽缁撴灉鍒颁笂涓嬫枃
        dynamicContext.setValue("stepsMap", stepsMap);
        
        // 鏋勫缓瑙ｆ瀽缁撴灉鎽樿
        StringBuilder parseResult = new StringBuilder();
        parseResult.append("## 姝ラ瑙ｆ瀽缁撴灉\n\n");
        parseResult.append(String.format("鎴愬姛瑙ｆ瀽 %d 涓墽琛屾楠わ細\n\n", stepsMap.size()));
        
        for (Map.Entry<String, String> entry : stepsMap.entrySet()) {
            parseResult.append(String.format("- **%s**: %s\n", 
                entry.getKey(), 
                entry.getValue().split("\n")[0])); // 鍙樉绀烘爣棰橀儴鍒?        }
        
        // 鍙戦€丼SE缁撴灉
        AutoAgentExecuteResultEntity result = AutoAgentExecuteResultEntity.createAnalysisSubResult(
                dynamicContext.getStep(), 
                "analysis_progress", 
                parseResult.toString(), 
                requestParameter.getSessionId());
        sendSseResult(dynamicContext, result);
        
        // 鏇存柊姝ラ
        dynamicContext.setStep(dynamicContext.getStep() + 1);
        
        return router(requestParameter, dynamicContext);
    }

    /**
     * 瑙ｆ瀽鎵ц姝ラ
     */
    private Map<String, String> parseExecutionSteps(String planningResult) {
        Map<String, String> stepsMap = new HashMap<>();

        if (planningResult == null || planningResult.trim().isEmpty()) {
            return stepsMap;
        }

        try {
            // 浣跨敤姝ｅ垯琛ㄨ揪寮忓尮閰嶆楠ゆ爣棰樺拰璇︾粏鍐呭
            Pattern stepPattern = Pattern.compile("### (绗琝\d+姝ワ細[^\\n]+)([\\s\\S]*?)(?=### 绗琝\d+姝ワ細|$)");
            Matcher matcher = stepPattern.matcher(planningResult);

            while (matcher.find()) {
                String stepTitle = matcher.group(1).trim();
                String stepContent = matcher.group(2).trim();

                // 鎻愬彇姝ラ缂栧彿
                Pattern numberPattern = Pattern.compile("绗?\\d+)姝ワ細");
                Matcher numberMatcher = numberPattern.matcher(stepTitle);

                if (numberMatcher.find()) {
                    String stepNumber = "绗? + numberMatcher.group(1) + "姝?;
                    String fullStepInfo = stepTitle + "\n" + stepContent;
                    stepsMap.put(stepNumber, fullStepInfo);
                    log.debug("瑙ｆ瀽姝ラ: {} -> {}", stepNumber, stepTitle);
                }
            }

            // 濡傛灉娌℃湁鍖归厤鍒拌缁嗘楠わ紝灏濊瘯鍖归厤绠€鍗曠殑姝ラ鍒楄〃
            if (stepsMap.isEmpty()) {
                Pattern simpleStepPattern = Pattern.compile("\\[ \\] (绗琝\d+姝ワ細[^\\n]+)");
                Matcher simpleMatcher = simpleStepPattern.matcher(planningResult);

                while (simpleMatcher.find()) {
                    String stepTitle = simpleMatcher.group(1).trim();
                    Pattern numberPattern = Pattern.compile("绗?\\d+)姝ワ細");
                    Matcher numberMatcher = numberPattern.matcher(stepTitle);

                    if (numberMatcher.find()) {
                        String stepNumber = "绗? + numberMatcher.group(1) + "姝?;
                        stepsMap.put(stepNumber, stepTitle);
                        log.debug("瑙ｆ瀽绠€鍗曟楠? {} -> {}", stepNumber, stepTitle);
                    }
                }
            }

            log.info("鎴愬姛瑙ｆ瀽 {} 涓墽琛屾楠?, stepsMap.size());

        } catch (Exception e) {
            log.error("瑙ｆ瀽瑙勫垝缁撴灉鏃跺彂鐢熼敊璇?, e);
        }

        return stepsMap;
    }

    @Override
    public StrategyHandler<ExecuteCommandEntity, DefaultFlowAgentExecuteStrategyFactory.DynamicContext, String> get(ExecuteCommandEntity requestParameter, DefaultFlowAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return step4ExecuteStepsNode;
    }

}
