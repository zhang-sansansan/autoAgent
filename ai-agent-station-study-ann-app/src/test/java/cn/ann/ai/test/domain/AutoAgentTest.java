package cn.ann.ai.test.domain;

import cn.ann.ai.domain.agent.model.entity.ArmoryCommandEntity;
import cn.ann.ai.domain.agent.model.entity.ExecuteCommandEntity;
import cn.ann.ai.domain.agent.model.valobj.AiAgentEnumVO;
import cn.ann.ai.domain.agent.service.armory.node.factory.DefaultArmoryStrategyFactory;
import cn.ann.ai.domain.agent.service.execute.auto.step.factory.DefaultAutoAgentExecuteStrategyFactory;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

/**
 * @author zhang san
 * @description
 * @create 2026/2/1 15:17
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AutoAgentTest {
    //娴嬭瘯涓€涓嬭嚜鍔ㄦ娴嬩换鍔＄殑agent
    // 娴佺▼
    //1銆佸厛閫氳繃armory鍒嗘敮鏉ヤ粠鏁版嵁搴撹閰嶉渶瑕佺殑瀹㈡埛绔?
    //2銆佹牴鎹閰嶇殑瀹㈡埛绔瀯寤烘暣涓猘gent鎵ц娴佺▼

    //鍏堝垱寤轰袱涓伐鍘傜被
    @Resource
    private DefaultAutoAgentExecuteStrategyFactory defaultAutoAgentExecuteStrategyFactory;
    @Resource
    private DefaultArmoryStrategyFactory defaultArmoryStrategyFactory;
    @Resource
    private ApplicationContext applicationContext;

    @Before//鏋勫缓瀹㈡埛绔?
    public void init() throws Exception {
        //杩斿洖鏍硅妭鐐?
        StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> armoryStrategyHandler = defaultArmoryStrategyFactory.armoryStrategyHandler();
        //璋冪敤apply鏂规硶鍚庯紝瑁呴厤鍛戒护瀵瑰簲绫诲瀷id鐨勮閰嶇被宸茬粡鏋勫缓瀹屾瘯骞舵斁鍏pring瀹瑰櫒涓?
        String apply = armoryStrategyHandler.apply(
                ArmoryCommandEntity.builder()
                        .commandType(AiAgentEnumVO.AI_CLIENT.getCode())
                        .commandIdList(Arrays.asList("3101", "3102", "3103"))
                        .build(),
                new DefaultArmoryStrategyFactory.DynamicContext());

        ChatClient chatClient = (ChatClient) applicationContext.getBean(AiAgentEnumVO.AI_CLIENT.getBeanName("3101"));
        log.info("瀹㈡埛绔瀯寤?{}", chatClient);
    }


    @Test
    public void autoAgent() throws Exception {
        StrategyHandler<ExecuteCommandEntity, DefaultAutoAgentExecuteStrategyFactory.DynamicContext, String> executeHandler = defaultAutoAgentExecuteStrategyFactory.armoryStrategyHandler();
        //閰嶇疆鍏ュ弬
        ExecuteCommandEntity executeCommandEntity = new ExecuteCommandEntity();
        executeCommandEntity.setAiAgentId("3");
        executeCommandEntity.setMessage("鎼滅储灏忓倕鍝ワ紝鎶€鏈」鐩垪琛ㄣ€傜紪鍐欐垚涓€浠芥枃妗ｏ紝璇存槑涓嶅悓椤圭洰鐨勫涔犵洰鏍囷紝浠ュ強涓嶅悓闃舵鐨勪紮浼村簲璇ュ涔犲摢涓」鐩€?);
        executeCommandEntity.setSessionId("session-id-" + System.currentTimeMillis());
        executeCommandEntity.setMaxStep(3);
        String apply = executeHandler.apply(executeCommandEntity, new DefaultAutoAgentExecuteStrategyFactory.DynamicContext());
        log.info("娴嬭瘯缁撴灉:{}", apply);
    }
}

