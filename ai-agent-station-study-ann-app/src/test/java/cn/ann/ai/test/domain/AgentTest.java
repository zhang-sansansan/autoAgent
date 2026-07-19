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

        log.info("娴嬭瘯缁撴灉锛歿}", openAiApi);
    }

    @Test
    public void test_aiClientModelNode() throws Exception {
        StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> armoryStrategyHandler =
                defaultArmoryStrategyFactory.armoryStrategyHandler();
        //apply鍑芥暟浼犲弬 涓€涓槸璇锋眰鍙傛暟锛屼竴涓槸鍔ㄦ€佽儗鍖呮潵鏀炬煡璇㈢粨鏋?        String apply = armoryStrategyHandler.apply(
                ArmoryCommandEntity.builder()
                .commandType(AiAgentEnumVO.AI_CLIENT.getCode())
                .commandIdList(Arrays.asList("3001")).build(),
                new DefaultArmoryStrategyFactory.DynamicContext());
        //缁忚繃apply鍑芥暟涔嬪悗锛宮odel鐨刡ean瀵硅薄宸茬粡瀛樺叆瀹瑰櫒涓?        OpenAiChatModel openAiChatModel = (OpenAiChatModel) applicationContext.getBean(AiAgentEnumVO.AI_CLIENT_MODEL.getBeanName("2001"));

        log.info("妯″瀷鏋勫缓:{}", openAiChatModel);

        Prompt prompt = Prompt.builder()
                .messages(new UserMessage(
                        """
                                鍦?D:/妗岄潰/鏂囨。/鏂囨。鐩?鍒涘缓 txt.md 鏂囦欢
                                """))
                .build();

        ChatResponse chatResponse = openAiChatModel.call(prompt);
        log.info("娴嬭瘯缁撴灉(call):{}", JSON.toJSONString(chatResponse));
    }

    //娴嬭瘯ai瀹㈡埛绔紝鍏堝垱寤烘牴鑺傜偣锛屼箣鍚庣粰鍑鸿鍒涘缓鐨則ype鍜屽懡浠ist锛岃繖涓槸鏍硅妭鐐硅繍琛岀殑鍏ュ弬
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

        log.info("瀹㈡埛绔瀯寤?{}", chatClient);
        String context = chatClient.prompt(Prompt.builder()
                        .messages(
                                new UserMessage("鏈夊摢浜涘伐鍏峰彲浠ヤ娇鐢?)
                        )
                .build()).call().content();

        log.info("娴嬭瘯缁撴灉{}", context);
    }

}

