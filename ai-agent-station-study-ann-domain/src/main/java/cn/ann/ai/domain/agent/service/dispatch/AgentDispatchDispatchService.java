package cn.ann.ai.domain.agent.service.dispatch;

import cn.ann.ai.domain.agent.adapter.repository.IAgentRepository;
import cn.ann.ai.domain.agent.model.entity.ExecuteCommandEntity;
import cn.ann.ai.domain.agent.model.valobj.AiAgentVO;
import cn.ann.ai.domain.agent.service.IAgentDispatchService;
import cn.ann.ai.domain.agent.service.IExecuteStrategy;
import cn.ann.ai.types.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Agent 鏈嶅姟鎺ュ彛
 *
 * @author xiaofuge bugstack.cn @灏忓倕鍝?
 * 2025/9/6 06:55
 */
@Slf4j
@Service
public class AgentDispatchDispatchService implements IAgentDispatchService {

    @Resource
    private Map<String, IExecuteStrategy> executeStrategyMap;

    @Resource
    private IAgentRepository repository;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public void dispatch(ExecuteCommandEntity requestParameter, ResponseBodyEmitter emitter) throws Exception {
        AiAgentVO aiAgentVO = repository.queryAiAgentByAgentId(requestParameter.getAiAgentId());

        String strategy = aiAgentVO.getStrategy();
        IExecuteStrategy executeStrategy = executeStrategyMap.get(strategy);
        if (null == executeStrategy) {
            throw new BizException("涓嶅瓨鍦ㄧ殑鎵ц绛栫暐绫诲瀷 strategy:" + strategy);
        }

        // 3. 寮傛鎵цAutoAgent
        threadPoolExecutor.execute(() -> {
            try {
                executeStrategy.execute(requestParameter, emitter);
            } catch (Exception e) {
                log.error("AutoAgent鎵ц寮傚父锛歿}", e.getMessage(), e);
                try {
                    emitter.send("鎵ц寮傚父锛? + e.getMessage());
                } catch (Exception ex) {
                    log.error("鍙戦€佸紓甯镐俊鎭け璐ワ細{}", ex.getMessage(), ex);
                }
            } finally {
                try {
                    emitter.complete();
                } catch (Exception e) {
                    log.error("瀹屾垚娴佸紡杈撳嚭澶辫触锛歿}", e.getMessage(), e);
                }
            }
        });

    }

}

