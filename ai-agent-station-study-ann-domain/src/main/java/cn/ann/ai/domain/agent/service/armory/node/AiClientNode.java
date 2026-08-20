package cn.ann.ai.domain.agent.service.armory.node;

import cn.ann.ai.domain.agent.model.entity.ArmoryCommandEntity;
import cn.ann.ai.domain.agent.model.valobj.AiAgentEnumVO;
import cn.ann.ai.domain.agent.model.valobj.AiClientSystemPromptVO;
import cn.ann.ai.domain.agent.model.valobj.AiClientVO;
import cn.ann.ai.domain.agent.service.armory.node.factory.DefaultArmoryStrategyFactory;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.alibaba.fastjson.JSON;
import io.modelcontextprotocol.client.McpSyncClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhang san
 * @description
 * @create 2026/1/29 9:17
 */
@Service
@Slf4j
public class AiClientNode extends AbstractArmorySupport {
    @Override
    protected String doApply(ArmoryCommandEntity requestParameter, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("Ai Agent 构建节点，客户端{}", JSON.toJSONString(requestParameter));

        //和智能体客户端关联的配置在值对象中都有存储
        //依次配置智能体客户端需要的一些功能  流程整体一致  获取bean然后添加
        //包括：模型、预设词、工具：mcp、顾问
        List<AiClientVO> aiClientList = dynamicContext.getValue(dataName());

        if(aiClientList == null || aiClientList.isEmpty()){
            return router(requestParameter,dynamicContext);
        }

        //预设词没有用节点存入bean  所以从动态背包中拿出来  用map存储
        Map<String, AiClientSystemPromptVO> systemPromptMap = dynamicContext.getValue(AiAgentEnumVO.AI_CLIENT_SYSTEM_PROMPT.getDataName());

        //一个clientvo中有对应的promptid的值对象，可以从map中将prompt取出来
        for(AiClientVO aiClientVO : aiClientList){
            //1、预设话术  获取方式  client值对象里面有对应的id，动态背包里有存储的对应的map，所以便利id然后从map中拿之后拼接到defaultSystem上
            StringBuilder defaultSystem = new StringBuilder("Ai 智能体 \r\n");
            List<String> promptIdList = aiClientVO.getPromptIdList();
            for(String promptId : promptIdList){
                AiClientSystemPromptVO aiClientSystemPromptVO = systemPromptMap.get(promptId);
                defaultSystem.append(aiClientSystemPromptVO.getPromptContent());
            }

            //2、对话的模型
            OpenAiChatModel chatModel = getBean(AiAgentEnumVO.AI_CLIENT_MODEL.getBeanName(aiClientVO.getModelId()));

            //3、MCP服务
            List<McpSyncClient> mcpSyncClients = new ArrayList<>();
            List<String> mcpBeanNameList = aiClientVO.getMcpBeanNameList();
            for(String mcpBeanName : mcpBeanNameList ){
                mcpSyncClients.add(getBean(mcpBeanName));
            }

            //4、advisor顾问角色
            List<Advisor>advisors = new ArrayList<>();
            List<String> advisorBeanNameList = aiClientVO.getAdvisorBeanNameList();
            for(String advisorBeanName : advisorBeanNameList ){
                advisors.add(getBean(advisorBeanName));
            }

            //将动态集合转为数组之后，构建客户端
            Advisor[] advisorArray = advisors.toArray(new Advisor[]{});//正常toArray不会明确返回的类型，使用泛型之后会返回类型

            //5、构建对话客户端
            ChatClient chatClient = ChatClient.builder(chatModel)
                    .defaultSystem(defaultSystem.toString())
                    .defaultToolCallbacks(new SyncMcpToolCallbackProvider(mcpSyncClients.toArray(new McpSyncClient[]{})))
                    .defaultAdvisors(advisorArray)
                    .build();

            registerBean(beanName(aiClientVO.getClientId()),ChatClient.class,chatClient);
        }

        return router(requestParameter,dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> get(ArmoryCommandEntity requestParameter, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return defaultStrategyHandler;
    }

    public String dataName(){
        return AiAgentEnumVO.AI_CLIENT.getDataName();
    }

    public String beanName(String beanId){
        return AiAgentEnumVO.AI_CLIENT.getBeanName(beanId);
    }
}
