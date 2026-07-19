package cn.ann.ai.config;

import cn.ann.ai.domain.agent.model.entity.ArmoryCommandEntity;
import cn.ann.ai.domain.agent.model.valobj.AiAgentVO;
import cn.ann.ai.domain.agent.model.valobj.enums.AiAgentEnumVO;
import cn.ann.ai.domain.agent.service.IArmoryService;
import cn.ann.ai.domain.agent.service.armory.node.factory.DefaultArmoryStrategyFactory;
import cn.ann.ai.infrastructure.dao.IAiAgentDao;
import cn.ann.ai.infrastructure.dao.IAiAgentFlowConfigDao;
import cn.ann.ai.types.common.Constants;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AI Agent 鑷姩瑁呴厤閰嶇疆绫?
 * 鍦⊿pring Boot搴旂敤鍚姩瀹屾垚鍚庯紝鏍规嵁閰嶇疆鑷姩瑁呴厤AI瀹㈡埛绔?
 *
 * @author xiaofuge bugstack.cn @灏忓倕鍝?
 * 2025/1/15 10:00
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(AiAgentAutoConfigProperties.class)
@ConditionalOnProperty(prefix = "spring.ai.agent.auto-config", name = "enabled", havingValue = "true")
public class AiAgentAutoConfiguration implements ApplicationListener<ApplicationReadyEvent> {

    @Resource
    private AiAgentAutoConfigProperties aiAgentAutoConfigProperties;

    @Resource
    private DefaultArmoryStrategyFactory defaultArmoryStrategyFactory;

    @Resource
    private IArmoryService armoryService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            log.info("AI Agent 鑷姩瑁呴厤寮€濮嬶紝閰嶇疆: {}", aiAgentAutoConfigProperties);
            
            // 妫€鏌ラ厤缃槸鍚︽湁鏁?
            if (!aiAgentAutoConfigProperties.isEnabled()) {
                log.info("AI Agent 鑷姩瑁呴厤鏈惎鐢?);
                return;
            }

            List<AiAgentVO> aiAgentVOS = armoryService.acceptArmoryAllAvailableAgents();

            log.info("AI Agent 鑷姩瑁呴厤瀹屾垚 {}", JSON.toJSONString(aiAgentVOS));
        } catch (Exception e) {
            log.error("AI Agent 鑷姩瑁呴厤澶辫触", e);
        }
    }

}
