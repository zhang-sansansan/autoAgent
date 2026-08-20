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
    //测试一下自动检测任务的agent
    // 流程
    //1、先通过armory分支来从数据库装配需要的客户端
    //2、根据装配的客户端构建整个agent执行流程

    //先创建两个工厂类
    @Resource
    private DefaultAutoAgentExecuteStrategyFactory defaultAutoAgentExecuteStrategyFactory;
    @Resource
    private DefaultArmoryStrategyFactory defaultArmoryStrategyFactory;
    @Resource
    private ApplicationContext applicationContext;

    @Before//构建客户端
    public void init() throws Exception {
        //返回根节点
        StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> armoryStrategyHandler = defaultArmoryStrategyFactory.armoryStrategyHandler();
        //调用apply方法后，装配命令对应类型id的装配类已经构建完毕并放入spring容器中
        String apply = armoryStrategyHandler.apply(
                ArmoryCommandEntity.builder()
                        .commandType(AiAgentEnumVO.AI_CLIENT.getCode())
                        .commandIdList(Arrays.asList("3101", "3102", "3103"))
                        .build(),
                new DefaultArmoryStrategyFactory.DynamicContext());

        ChatClient chatClient = (ChatClient) applicationContext.getBean(AiAgentEnumVO.AI_CLIENT.getBeanName("3101"));
        log.info("客户端构建:{}", chatClient);
    }


    @Test
    public void autoAgent() throws Exception {
        StrategyHandler<ExecuteCommandEntity, DefaultAutoAgentExecuteStrategyFactory.DynamicContext, String> executeHandler = defaultAutoAgentExecuteStrategyFactory.armoryStrategyHandler();
        //配置入参
        ExecuteCommandEntity executeCommandEntity = new ExecuteCommandEntity();
        executeCommandEntity.setAiAgentId("3");
        executeCommandEntity.setMessage("搜索小傅哥，技术项目列表。编写成一份文档，说明不同项目的学习目标，以及不同阶段的伙伴应该学习哪个项目。");
        executeCommandEntity.setSessionId("session-id-" + System.currentTimeMillis());
        executeCommandEntity.setMaxStep(3);
        String apply = executeHandler.apply(executeCommandEntity, new DefaultAutoAgentExecuteStrategyFactory.DynamicContext());
        log.info("测试结果:{}", apply);
    }
}
