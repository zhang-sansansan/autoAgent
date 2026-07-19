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
        log.info("Ai Agent 鏋勫缓鑺傜偣锛屽鎴风{}", JSON.toJSONString(requestParameter));

        //鍜屾櫤鑳戒綋瀹㈡埛绔叧鑱旂殑閰嶇疆鍦ㄥ€煎璞′腑閮芥湁瀛樺偍
        //渚濇閰嶇疆鏅鸿兘浣撳鎴风闇€瑕佺殑涓€浜涘姛鑳? 娴佺▼鏁翠綋涓€鑷? 鑾峰彇bean鐒跺悗娣诲姞
        //鍖呮嫭锛氭ā鍨嬨€侀璁捐瘝銆佸伐鍏凤細mcp銆侀【闂?
        List<AiClientVO> aiClientList = dynamicContext.getValue(dataName());

        if(aiClientList == null || aiClientList.isEmpty()){
            return router(requestParameter,dynamicContext);
        }

        //棰勮璇嶆病鏈夌敤鑺傜偣瀛樺叆bean  鎵€浠ヤ粠鍔ㄦ€佽儗鍖呬腑鎷垮嚭鏉? 鐢╩ap瀛樺偍
        Map<String, AiClientSystemPromptVO> systemPromptMap = dynamicContext.getValue(AiAgentEnumVO.AI_CLIENT_SYSTEM_PROMPT.getDataName());

        //涓€涓猚lientvo涓湁瀵瑰簲鐨刾romptid鐨勫€煎璞★紝鍙互浠巑ap涓皢prompt鍙栧嚭鏉?
        for(AiClientVO aiClientVO : aiClientList){
            //1銆侀璁捐瘽鏈? 鑾峰彇鏂瑰紡  client鍊煎璞￠噷闈㈡湁瀵瑰簲鐨刬d锛屽姩鎬佽儗鍖呴噷鏈夊瓨鍌ㄧ殑瀵瑰簲鐨刴ap锛屾墍浠ヤ究鍒﹊d鐒跺悗浠巑ap涓嬁涔嬪悗鎷兼帴鍒癲efaultSystem涓?
            StringBuilder defaultSystem = new StringBuilder("Ai 鏅鸿兘浣?\r\n");
            List<String> promptIdList = aiClientVO.getPromptIdList();
            for(String promptId : promptIdList){
                AiClientSystemPromptVO aiClientSystemPromptVO = systemPromptMap.get(promptId);
                defaultSystem.append(aiClientSystemPromptVO.getPromptContent());
            }

            //2銆佸璇濈殑妯″瀷
            OpenAiChatModel chatModel = getBean(AiAgentEnumVO.AI_CLIENT_MODEL.getBeanName(aiClientVO.getModelId()));

            //3銆丮CP鏈嶅姟
            List<McpSyncClient> mcpSyncClients = new ArrayList<>();
            List<String> mcpBeanNameList = aiClientVO.getMcpBeanNameList();
            for(String mcpBeanName : mcpBeanNameList ){
                mcpSyncClients.add(getBean(mcpBeanName));
            }

            //4銆乤dvisor椤鹃棶瑙掕壊
            List<Advisor>advisors = new ArrayList<>();
            List<String> advisorBeanNameList = aiClientVO.getAdvisorBeanNameList();
            for(String advisorBeanName : advisorBeanNameList ){
                advisors.add(getBean(advisorBeanName));
            }

            //灏嗗姩鎬侀泦鍚堣浆涓烘暟缁勪箣鍚庯紝鏋勫缓瀹㈡埛绔?
            Advisor[] advisorArray = advisors.toArray(new Advisor[]{});//姝ｅ父toArray涓嶄細鏄庣‘杩斿洖鐨勭被鍨嬶紝浣跨敤娉涘瀷涔嬪悗浼氳繑鍥炵被鍨?

            //5銆佹瀯寤哄璇濆鎴风
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

