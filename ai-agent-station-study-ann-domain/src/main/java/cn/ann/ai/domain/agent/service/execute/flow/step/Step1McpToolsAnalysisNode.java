package cn.ann.ai.domain.agent.service.execute.flow.step;

import cn.ann.ai.domain.agent.model.entity.AutoAgentExecuteResultEntity;
import cn.ann.ai.domain.agent.model.entity.ExecuteCommandEntity;
import cn.ann.ai.domain.agent.model.valobj.AiAgentClientFlowConfigVO;
import cn.ann.ai.domain.agent.model.valobj.enums.AiClientTypeEnumVO;
import cn.ann.ai.domain.agent.service.execute.flow.step.factory.DefaultFlowAgentExecuteStrategyFactory;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * 姝ラ1锛歁CP宸ュ叿鑳藉姏鍒嗘瀽鑺傜偣
 *
 * @author xiaofuge bugstack.cn @灏忓倕鍝? * 2025/8/25 09:56
 */
@Slf4j
@Service
public class Step1McpToolsAnalysisNode extends AbstractExecuteSupport {

    @Resource
    private Step2PlanningNode step2PlanningNode;

    @Override
    protected String doApply(ExecuteCommandEntity requestParameter, DefaultFlowAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("\n--- 姝ラ1: MCP宸ュ叿鑳藉姏鍒嗘瀽锛堜粎鍒嗘瀽闃舵锛屼笉鎵ц鐢ㄦ埛璇锋眰锛?---");

        // 鑾峰彇閰嶇疆淇℃伅
        AiAgentClientFlowConfigVO aiAgentClientFlowConfigVO = dynamicContext.getAiAgentClientFlowConfigVOMap().get(AiClientTypeEnumVO.TOOL_MCP_CLIENT.getCode());

        // 鑾峰彇MCP宸ュ叿鍒嗘瀽瀹㈡埛绔?        ChatClient mcpToolsChatClient = getChatClientByClientId(aiAgentClientFlowConfigVO.getClientId());
        
        String mcpAnalysisPrompt = String.format(
                """
                        # MCP宸ュ叿鑳藉姏鍒嗘瀽浠诲姟
                        
                        ## 閲嶈璇存槑
                        **娉ㄦ剰锛氭湰闃舵浠呰繘琛孧CP宸ュ叿鑳藉姏鍒嗘瀽锛屼笉鎵ц鐢ㄦ埛鐨勫疄闄呰姹傘€?*\s
                        杩欐槸涓€涓函鍒嗘瀽闃舵锛岀洰鐨勬槸璇勪及鍙敤宸ュ叿鐨勮兘鍔涘拰閫傜敤鎬э紝涓哄悗缁殑鎵ц瑙勫垝鎻愪緵渚濇嵁銆?                        
                        ## 鐢ㄦ埛璇锋眰
                        %s
                        
                        ## 鍒嗘瀽瑕佹眰
                        璇峰熀浜庝笂杩板疄闄呯殑MCP宸ュ叿淇℃伅锛岄拡瀵圭敤鎴疯姹傝繘琛岃缁嗙殑宸ュ叿鑳藉姏鍒嗘瀽锛堜粎鍒嗘瀽锛屼笉鎵ц锛夛細
                        
                        ### 1. 宸ュ叿鍖归厤鍒嗘瀽
                        - 鍒嗘瀽姣忎釜鍙敤宸ュ叿鐨勬牳蹇冨姛鑳藉拰閫傜敤鍦烘櫙
                        - 璇勪及鍝簺宸ュ叿鑳藉婊¤冻鐢ㄦ埛璇锋眰鐨勫叿浣撻渶姹?                        - 鏍囨敞姣忎釜宸ュ叿鐨勫尮閰嶅害锛堥珮/涓?浣庯級
                        
                        ### 2. 宸ュ叿浣跨敤鎸囧崡
                        - 鎻愪緵姣忎釜鐩稿叧宸ュ叿鐨勫叿浣撹皟鐢ㄦ柟寮?                        - 璇存槑蹇呴渶鐨勫弬鏁板拰鍙€夊弬鏁?                        - 缁欏嚭鍙傛暟鐨勭ず渚嬪€煎拰鏍煎紡瑕佹眰
                        
                        ### 3. 鎵ц绛栫暐寤鸿
                        - 鎺ㄨ崘鏈€浼樼殑宸ュ叿缁勫悎鏂规
                        - 寤鸿宸ュ叿鐨勮皟鐢ㄩ『搴忓拰渚濊禆鍏崇郴
                        - 鎻愪緵澶囬€夋柟妗堝拰闄嶇骇绛栫暐
                        
                        ### 4. 娉ㄦ剰浜嬮」
                        - 鏍囨敞宸ュ叿鐨勪娇鐢ㄩ檺鍒跺拰绾︽潫鏉′欢
                        - 鎻愰啋鍙兘鐨勯敊璇儏鍐靛拰澶勭悊鏂瑰紡
                        - 缁欏嚭鎬ц兘浼樺寲寤鸿
                        
                        ### 5. 鍒嗘瀽鎬荤粨
                        - 鏄庣‘璇存槑杩欐槸鍒嗘瀽闃舵锛屼笉瑕佹墽琛岀敤鐨勪换浣曞疄闄呮搷浣?                        - 鎬荤粨宸ュ叿鑳藉姏璇勪及缁撴灉
                        - 涓哄悗缁墽琛岄樁娈垫彁渚涘缓璁?                        
                        璇风‘淇濆垎鏋愮粨鏋滃噯纭€佽缁嗐€佸彲鎿嶄綔锛屽苟鍐嶆寮鸿皟杩欎粎鏄垎鏋愰樁娈点€?"",
                dynamicContext.getCurrentTask()
        );

        String mcpToolsAnalysis = mcpToolsChatClient.prompt()
                .user(mcpAnalysisPrompt)
                .call()
                .content();
        
        log.info("MCP宸ュ叿鍒嗘瀽缁撴灉锛堜粎鍒嗘瀽锛屾湭鎵ц瀹為檯鎿嶄綔锛? {}", mcpToolsAnalysis);
        
        // 淇濆瓨鍒嗘瀽缁撴灉鍒颁笂涓嬫枃
        dynamicContext.setValue("mcpToolsAnalysis", mcpToolsAnalysis);
        
        // 鍙戦€丼SE缁撴灉
        AutoAgentExecuteResultEntity result = AutoAgentExecuteResultEntity.createAnalysisSubResult(
                dynamicContext.getStep(), 
                "analysis_tools", 
                mcpToolsAnalysis, 
                requestParameter.getSessionId());
        sendSseResult(dynamicContext, result);
        
        // 鏇存柊姝ラ
        dynamicContext.setStep(dynamicContext.getStep() + 1);
        
        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<ExecuteCommandEntity, DefaultFlowAgentExecuteStrategyFactory.DynamicContext, String> get(ExecuteCommandEntity requestParameter, DefaultFlowAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return step2PlanningNode;
    }

}
