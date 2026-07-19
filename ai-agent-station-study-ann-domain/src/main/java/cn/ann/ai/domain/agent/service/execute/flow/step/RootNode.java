package cn.ann.ai.domain.agent.service.execute.flow.step;

import cn.ann.ai.domain.agent.model.entity.ExecuteCommandEntity;
import cn.ann.ai.domain.agent.model.valobj.AiAgentClientFlowConfigVO;
import cn.ann.ai.domain.agent.service.execute.flow.step.factory.DefaultFlowAgentExecuteStrategyFactory;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 娴佺▼鎵ц鏍硅妭鐐? * @author xiaofuge bugstack.cn @灏忓倕鍝? * 2025/8/24 14:35
 */
@Slf4j
@Service("flowRootNode")
public class RootNode extends AbstractExecuteSupport {

    @Resource
    private Step1McpToolsAnalysisNode step1McpToolsAnalysisNode;

    @Override
    protected String doApply(ExecuteCommandEntity requestParameter, DefaultFlowAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("=== 娴佺▼鎵ц寮€濮?====");
        log.info("鐢ㄦ埛杈撳叆: {}", requestParameter.getMessage());
        log.info("鏈€澶ф墽琛屾鏁? {}", requestParameter.getMaxStep());
        log.info("浼氳瘽ID: {}", requestParameter.getSessionId());

        Map<String, AiAgentClientFlowConfigVO> aiAgentClientFlowConfigVOMap = repository.queryAiAgentClientFlowConfig(requestParameter.getAiAgentId());

        // 瀹㈡埛绔璇濈粍
        dynamicContext.setAiAgentClientFlowConfigVOMap(aiAgentClientFlowConfigVOMap);
        // 涓婁笅鏂囦俊鎭?        dynamicContext.setExecutionHistory(new StringBuilder());
        // 褰撳墠浠诲姟淇℃伅
        dynamicContext.setCurrentTask(requestParameter.getMessage());
        // 鏈€澶т换鍔℃楠?        dynamicContext.setMaxStep(requestParameter.getMaxStep());

        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<ExecuteCommandEntity, DefaultFlowAgentExecuteStrategyFactory.DynamicContext, String> get(ExecuteCommandEntity requestParameter, DefaultFlowAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return step1McpToolsAnalysisNode;
    }

}

