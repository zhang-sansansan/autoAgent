package cn.ann.ai.domain.agent.service.execute.auto.step;

import cn.ann.ai.domain.agent.model.entity.ExecuteCommandEntity;
import cn.ann.ai.domain.agent.model.valobj.AiAgentClientFlowConfigVO;
import cn.ann.ai.domain.agent.service.execute.auto.step.factory.DefaultAutoAgentExecuteStrategyFactory;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 鎵ц鏍硅妭鐐? *
 * @author xiaofuge bugstack.cn @灏忓倕鍝? * 2025/7/27 16:33
 */
@Slf4j
@Service("executeRootNode")
public class RootNode extends AbstractExecuteSupport {

    @Resource
    private Step1AnalyzerNode step1AnalyzerNode;

    @Override
    protected String doApply(ExecuteCommandEntity requestParameter, DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("=== 鍔ㄦ€佸杞墽琛屾祴璇曞紑濮?====");
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
    public StrategyHandler<ExecuteCommandEntity, DefaultAutoAgentExecuteStrategyFactory.DynamicContext, String> get(ExecuteCommandEntity requestParameter, DefaultAutoAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return step1AnalyzerNode;
    }

}

