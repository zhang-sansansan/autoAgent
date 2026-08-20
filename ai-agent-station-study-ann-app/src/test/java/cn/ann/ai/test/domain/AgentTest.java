package cn.ann.ai.test.domain;

import cn.ann.ai.domain.agent.model.entity.ArmoryCommandEntity;
import cn.ann.ai.domain.agent.model.valobj.AiAgentEnumVO;
import cn.ann.ai.domain.agent.service.armory.node.factory.DefaultArmoryStrategyFactory;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AgentTest {

    @Resource
    private DefaultArmoryStrategyFactory defaultArmoryStrategyFactory;

    @Resource
    private ApplicationContext applicationContext;

    @Test
    public void test_aiClientApiNode() throws Exception {
        StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> armoryStrategyHandler =
                defaultArmoryStrategyFactory.armoryStrategyHandler();//RootNode

        String apply = armoryStrategyHandler.apply(
                ArmoryCommandEntity.builder()
                        .commandType(AiAgentEnumVO.AI_CLIENT.getCode())
                        .commandIdList(Arrays.asList("48376249"))
                        .build(),
                new DefaultArmoryStrategyFactory.DynamicContext());

        OpenAiApi openAiApi = (OpenAiApi) applicationContext.getBean(AiAgentEnumVO.AI_CLIENT_API.getBeanName("1001"));

        log.info("测试结果：{}", openAiApi);
    }

    @Test
    public void test_aiClientModelNode() throws Exception {
        StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> armoryStrategyHandler =
                defaultArmoryStrategyFactory.armoryStrategyHandler();
        //apply函数传参 一个是请求参数，一个是动态背包来放查询结果
        String apply = armoryStrategyHandler.apply(
                ArmoryCommandEntity.builder()
                .commandType(AiAgentEnumVO.AI_CLIENT.getCode())
                .commandIdList(Arrays.asList("3001")).build(),
                new DefaultArmoryStrategyFactory.DynamicContext());
        //经过apply函数之后，model的bean对象已经存入容器中
        OpenAiChatModel openAiChatModel = (OpenAiChatModel) applicationContext.getBean(AiAgentEnumVO.AI_CLIENT_MODEL.getBeanName("2001"));

        log.info("模型构建:{}", openAiChatModel);

        Prompt prompt = Prompt.builder()
                .messages(new UserMessage(
                        """
                                在 D:/桌面/文档/文档盘 创建 txt.md 文件
                                """))
                .build();

        ChatResponse chatResponse = openAiChatModel.call(prompt);
        log.info("测试结果(call):{}", JSON.toJSONString(chatResponse));
    }

    //测试ai客户端，先创建根节点，之后给出要创建的type和命令list，这个是根节点运行的入参
    @Test
    public void aiClient_test() throws Exception {
        StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> armoryStrategyHandler =
                defaultArmoryStrategyFactory.armoryStrategyHandler();

        String apply = armoryStrategyHandler.apply(
                ArmoryCommandEntity.builder()
                        .commandType(AiAgentEnumVO.AI_CLIENT.getCode())
                        .commandIdList(Arrays.asList("3001"))
                        .build(),
                new DefaultArmoryStrategyFactory.DynamicContext());

        ChatClient chatClient = (ChatClient)applicationContext.getBean(AiAgentEnumVO.AI_CLIENT.getBeanName("3001"));

        log.info("客户端构建:{}", chatClient);
        String context = chatClient.prompt(Prompt.builder()
                        .messages(
                                new UserMessage("有哪些工具可以使用")
                        )
                .build()).call().content();

        log.info("测试结果{}", context);
    }

}
