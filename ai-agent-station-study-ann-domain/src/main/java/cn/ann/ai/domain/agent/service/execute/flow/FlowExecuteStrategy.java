package cn.ann.ai.domain.agent.service.execute.flow;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import com.alibaba.fastjson.JSON;

import cn.ann.ai.domain.agent.model.entity.AutoAgentExecuteResultEntity;
import cn.ann.ai.domain.agent.model.entity.ExecuteCommandEntity;
import cn.ann.ai.domain.agent.service.IExecuteStrategy;
import cn.ann.ai.domain.agent.service.execute.flow.step.factory.DefaultFlowAgentExecuteStrategyFactory;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaofuge bugstack.cn @小傅哥
 * 2025/8/5 09:56
 */
@Slf4j
@Service("flowAgentExecuteStrategy")
public class FlowExecuteStrategy implements IExecuteStrategy{

    @Resource
    DefaultFlowAgentExecuteStrategyFactory defaultFlowAgentExecuteStrategyFactory;

    @Override
    public void execute(ExecuteCommandEntity executeCommandEntity, ResponseBodyEmitter emitter) throws Exception {
        StrategyHandler<ExecuteCommandEntity, DefaultFlowAgentExecuteStrategyFactory.DynamicContext, String> executeHandler = defaultFlowAgentExecuteStrategyFactory.armoryStrategyHandler();
        DefaultFlowAgentExecuteStrategyFactory.DynamicContext  dynamicContext = new DefaultFlowAgentExecuteStrategyFactory.DynamicContext();
        dynamicContext.setMaxStep(executeCommandEntity.getMaxStep() != null ? executeCommandEntity.getMaxStep() : 4);
        dynamicContext.setExecutionHistory(new StringBuilder());
        dynamicContext.setCurrentTask(executeCommandEntity.getMessage());
        dynamicContext.setValue("emitter", emitter);

        String apply = executeHandler.apply(executeCommandEntity, dynamicContext);
        log.info("流程执行结果:{}", apply);
        
        // 发送完成标识
        try {
            AutoAgentExecuteResultEntity completeResult = AutoAgentExecuteResultEntity.createCompleteResult(executeCommandEntity.getSessionId());
            // 发送SSE格式的数据
            String sseData = "data: " + JSON.toJSONString(completeResult) + "\n\n";
            emitter.send(sseData);
        } catch (Exception e) {
            log.error("发送完成标识失败：{}", e.getMessage(), e);
        }
    }


}
