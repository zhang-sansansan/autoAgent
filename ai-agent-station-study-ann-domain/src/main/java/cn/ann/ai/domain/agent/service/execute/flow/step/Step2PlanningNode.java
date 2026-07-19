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
 * 姝ラ2锛氭墽琛屾楠よ鍒掕妭鐐? *
 * @author xiaofuge bugstack.cn @灏忓倕鍝? * 2025/8/25 10:30
 */
@Slf4j
@Service
public class Step2PlanningNode extends AbstractExecuteSupport {

     @Resource
     private Step3ParseStepsNode step3ParseStepsNode;

    @Override
    protected String doApply(ExecuteCommandEntity requestParameter, DefaultFlowAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("\n--- 姝ラ2: 鎵ц姝ラ瑙勫垝 ---");

        // 鑾峰彇閰嶇疆淇℃伅
        AiAgentClientFlowConfigVO aiAgentClientFlowConfigVO = dynamicContext.getAiAgentClientFlowConfigVOMap().get(AiClientTypeEnumVO.PLANNING_CLIENT.getCode());

        // 鑾峰彇瑙勫垝瀹㈡埛绔?        ChatClient planningChatClient = getChatClientByClientId(aiAgentClientFlowConfigVO.getClientId());

        String userRequest = dynamicContext.getCurrentTask();
        String mcpToolsAnalysis = dynamicContext.getValue("mcpToolsAnalysis");
        
        String planningPrompt = buildStructuredPlanningPrompt(userRequest, mcpToolsAnalysis);
        
        String refinedPrompt = planningPrompt + "\n\n## 鈿狅笍 宸ュ叿鏄犲皠楠岃瘉鍙嶉\n" +
                "\n\n**璇锋牴鎹笂杩伴獙璇佸弽棣堥噸鏂扮敓鎴愯鍒掞紝纭繚锛?*\n" +
                "1. 鍙娇鐢ㄩ獙璇佹姤鍛婁腑鍒楀嚭鐨勬湁鏁堝伐鍏穃n" +
                "2. 宸ュ叿鍚嶇О蹇呴』瀹屽叏鍖归厤锛堝尯鍒嗗ぇ灏忓啓锛塡n" +
                "3. 姣忎釜姝ラ鏄庣‘鎸囧畾浣跨敤鐨凪CP宸ュ叿\n" +
                "4. 閬垮厤浣跨敤涓嶅瓨鍦ㄦ垨鏃犳晥鐨勫伐鍏?;

        String planningResult = planningChatClient.prompt()
                .user(refinedPrompt)
                .call()
                .content();
        
        log.info("鎵ц姝ラ瑙勫垝缁撴灉: {}", planningResult);
        
        // 淇濆瓨瑙勫垝缁撴灉鍒颁笂涓嬫枃
        dynamicContext.setValue("planningResult", planningResult);
        
        // 鍙戦€丼SE缁撴灉
        AutoAgentExecuteResultEntity result = AutoAgentExecuteResultEntity.createAnalysisSubResult(
                dynamicContext.getStep(), 
                "analysis_strategy", 
                planningResult, 
                requestParameter.getSessionId());
        sendSseResult(dynamicContext, result);
        
        // 鏇存柊姝ラ
        dynamicContext.setStep(dynamicContext.getStep() + 1);
        
        return router(requestParameter, dynamicContext);
    }

    /**
     * 鏋勫缓缁撴瀯鍖栫殑瑙勫垝鎻愮ず璇?     */
    private String buildStructuredPlanningPrompt(String userRequest, String mcpToolsAnalysis) {
        StringBuilder prompt = new StringBuilder();

        // 1. 浠诲姟鍒嗘瀽閮ㄥ垎 - 閫氱敤鍖栫敤鎴烽渶姹傚垎鏋?        prompt.append("# 鏅鸿兘鎵ц璁″垝鐢熸垚\n\n");
        prompt.append("## 馃搵 鐢ㄦ埛闇€姹傚垎鏋怽n");
        prompt.append("**瀹屾暣鐢ㄦ埛璇锋眰锛?*\n");
        prompt.append("```\n");
        prompt.append(userRequest);
        prompt.append("\n```\n\n");
        prompt.append("**鈿狅笍 閲嶈鎻愰啋锛?* 鍦ㄧ敓鎴愭墽琛岃鍒掓椂锛屽繀椤诲畬鏁翠繚鐣欏拰浼犻€掔敤鎴疯姹備腑鐨勬墍鏈夎缁嗕俊鎭紝鍖呮嫭浣嗕笉闄愪簬锛歕n");
        prompt.append("- 浠诲姟鐨勫叿浣撶洰鏍囧拰鏈熸湜缁撴灉\n");
        prompt.append("- 娑夊強鐨勬暟鎹€佸弬鏁般€侀厤缃瓑璇︾粏淇℃伅\n");
        prompt.append("- 鐗瑰畾鐨勪笟鍔¤鍒欍€佺害鏉熸潯浠舵垨瑕佹眰\n");
        prompt.append("- 杈撳嚭鏍煎紡銆佽川閲忔爣鍑嗘垨楠屾敹鏉′欢\n");
        prompt.append("- 鏃堕棿瑕佹眰銆佷紭鍏堢骇鎴栧叾浠栨墽琛岀害鏉焅n\n");

        // 2. 宸ュ叿鑳藉姏鍒嗘瀽
        prompt.append("## 馃敡 MCP宸ュ叿鑳藉姏鍒嗘瀽缁撴灉\n");
        prompt.append(mcpToolsAnalysis).append("\n\n");

        // 3. 宸ュ叿鏄犲皠楠岃瘉 - 浣跨敤鍔ㄦ€佽幏鍙栫殑宸ュ叿淇℃伅
        prompt.append("## 鉁?宸ュ叿鏄犲皠楠岃瘉瑕佹眰\n");
        prompt.append("**閲嶈鎻愰啋锛?* 鍦ㄧ敓鎴愭墽琛屾楠ゆ椂锛屽繀椤讳弗鏍奸伒寰互涓嬪伐鍏锋槧灏勮鍒欙細\n\n");

        // 鍔ㄦ€佽幏鍙栧疄闄呯殑MCP宸ュ叿淇℃伅
        String actualToolsInfo = getActualMcpToolsInfo();
        prompt.append("### 鍙敤宸ュ叿娓呭崟\n");
        prompt.append(actualToolsInfo).append("\n");

        prompt.append("### 宸ュ叿閫夋嫨鍘熷垯\n");
        prompt.append("- **绮剧‘鍖归厤**: 姣忎釜姝ラ蹇呴』浣跨敤涓婅堪宸ュ叿娓呭崟涓殑纭垏鍑芥暟鍚嶇О\n");
        prompt.append("- **鍔熻兘瀵瑰簲**: 鏍规嵁MCP宸ュ叿鍒嗘瀽缁撴灉涓殑鍖归厤搴﹂€夋嫨鏈€閫傚悎鐨勫伐鍏穃n");
        prompt.append("- **鍙傛暟瀹屾暣**: 纭繚姣忎釜宸ュ叿璋冪敤閮藉寘鍚繀闇€鐨勫弬鏁拌鏄嶾n");
        prompt.append("- **渚濊禆鍏崇郴**: 鑰冭檻宸ュ叿闂寸殑鏁版嵁娴佽浆鍜屼緷璧栧叧绯籠n\n");

        // 4. 鎵ц璁″垝瑕佹眰
        prompt.append("## 馃摑 鎵ц璁″垝瑕佹眰\n");
        prompt.append("璇峰熀浜庝笂杩扮敤鎴疯缁嗛渶姹傘€丮CP宸ュ叿鍒嗘瀽缁撴灉鍜屽伐鍏锋槧灏勯獙璇佽姹傦紝鐢熸垚绮剧‘鐨勬墽琛岃鍒掞細\n\n");
        prompt.append("### 鏍稿績瑕佹眰\n");
        prompt.append("1. **瀹屾暣淇濈暀鐢ㄦ埛闇€姹?*: 蹇呴』灏嗙敤鎴疯姹備腑鐨勬墍鏈夎缁嗕俊鎭畬鏁翠紶閫掑埌姣忎釜鎵ц姝ラ涓璡n");
        prompt.append("2. **涓ユ牸閬靛惊MCP鍒嗘瀽缁撴灉**: 蹇呴』鏍规嵁宸ュ叿鑳藉姏鍒嗘瀽涓殑鍖归厤搴﹀拰鎺ㄨ崘鏂规鍒跺畾姝ラ\n");
        prompt.append("3. **绮剧‘宸ュ叿鏄犲皠**: 姣忎釜姝ラ蹇呴』浣跨敤纭垏鐨勫嚱鏁板悕绉帮紝涓嶅厑璁镐娇鐢ㄦā绯婃垨閿欒鐨勫伐鍏峰悕\n");
        prompt.append("4. **鍙傛暟瀹屾暣鎬?*: 鎵€鏈夊伐鍏疯皟鐢ㄥ繀椤诲寘鍚敤鎴峰師濮嬮渶姹備腑鐨勫畬鏁村弬鏁颁俊鎭痋n");
        prompt.append("5. **渚濊禆鍏崇郴鏄庣‘**: 鍩轰簬MCP鍒嗘瀽缁撴灉涓殑鎵ц绛栫暐寤鸿瀹夋帓姝ラ椤哄簭\n");
        prompt.append("6. **鍚堢悊绮掑害**: 閬垮厤杩囧害缁嗗垎锛屾瘡涓楠ゅ簲璇ユ槸瀹屾暣涓旂嫭绔嬬殑鍔熻兘鍗曞厓\n\n");

        // 4. 鏍煎紡瑙勮寖 - 閫氱敤鍖栦换鍔℃牸寮?        prompt.append("### 鏍煎紡瑙勮寖\n");
        prompt.append("璇蜂娇鐢ㄤ互涓婱arkdown鏍煎紡鐢熸垚3-5涓墽琛屾楠わ細\n");
        prompt.append("```markdown\n");
        prompt.append("# 鎵ц姝ラ瑙勫垝\n\n");
        prompt.append("[ ] 绗?姝ワ細[姝ラ鎻忚堪]\n");
        prompt.append("[ ] 绗?姝ワ細[姝ラ鎻忚堪]\n");
        prompt.append("[ ] 绗?姝ワ細[姝ラ鎻忚堪]\n");
        prompt.append("...\n\n");
        prompt.append("## 姝ラ璇︽儏\n\n");
        prompt.append("### 绗?姝ワ細[姝ラ鎻忚堪]\n");
        prompt.append("- **浼樺厛绾?*: [HIGH/MEDIUM/LOW]\n");
        prompt.append("- **棰勪及鏃堕暱**: [鍒嗛挓鏁癩鍒嗛挓\n");
        prompt.append("- **浣跨敤宸ュ叿**: [蹇呴』浣跨敤纭垏鐨勫嚱鏁板悕绉癩\n");
        prompt.append("- **宸ュ叿鍖归厤搴?*: [寮曠敤MCP鍒嗘瀽缁撴灉涓殑鍖归厤搴﹁瘎浼癩\n");
        prompt.append("- **渚濊禆姝ラ**: [鍓嶇疆姝ラ搴忓彿锛屽鏃犱緷璧栧垯濉啓'鏃?]\n");
        prompt.append("- **鎵ц鏂规硶**: [鍩轰簬MCP鍒嗘瀽缁撴灉鐨勫叿浣撴墽琛岀瓥鐣ワ紝鍖呭惈宸ュ叿璋冪敤鍙傛暟]\n");
        prompt.append("- **宸ュ叿鍙傛暟**: [璇︾粏鐨勫弬鏁拌鏄庡拰绀轰緥鍊硷紝蹇呴』鍖呭惈鐢ㄦ埛鍘熷闇€姹備腑鐨勬墍鏈夌浉鍏充俊鎭痌\n");
        prompt.append("- **闇€姹備紶閫?*: [鏄庣‘璇存槑濡備綍灏嗙敤鎴风殑璇︾粏瑕佹眰浼犻€掑埌姝ゆ楠や腑]\n");
        prompt.append("- **棰勬湡杈撳嚭**: [鏈熸湜鐨勬渶缁堢粨鏋淽\n");
        prompt.append("- **鎴愬姛鏍囧噯**: [鍒ゆ柇浠诲姟瀹屾垚鐨勬爣鍑哴\n");
        prompt.append("- **MCP鍒嗘瀽渚濇嵁**: [寮曠敤鍏蜂綋鐨凪CP宸ュ叿鍒嗘瀽缁撹]\n\n");
        prompt.append("```\n\n");

        // 5. 鍔ㄦ€佽鍒掓寚瀵煎師鍒?        prompt.append("### 瑙勫垝鎸囧鍘熷垯\n");
        prompt.append("璇锋牴鎹敤鎴疯缁嗚姹傚拰鍙敤宸ュ叿鑳藉姏锛屽姩鎬佺敓鎴愬悎閫傜殑鎵ц姝ラ锛歕n");
        prompt.append("- **闇€姹傚畬鏁存€у師鍒?*: 纭繚鐢ㄦ埛璇锋眰涓殑鎵€鏈夎缁嗕俊鎭兘琚畬鏁翠繚鐣欏拰浼犻€抃n");
        prompt.append("- **姝ラ鍒嗙鍘熷垯**: 姣忎釜姝ラ搴旇涓撴敞浜庡崟涓€鍔熻兘锛岄伩鍏嶆贩鍚堜笉鍚岀被鍨嬬殑鎿嶄綔\n");
        prompt.append("- **宸ュ叿鏄犲皠鍘熷垯**: 姣忎釜姝ラ搴旀槑纭娇鐢ㄥ摢涓叿浣撶殑MCP宸ュ叿\n");
        prompt.append("- **鍙傛暟浼犻€掑師鍒?*: 纭繚鐢ㄦ埛鐨勮缁嗚姹傝兘澶熷噯纭紶閫掑埌宸ュ叿鍙傛暟涓璡n");
        prompt.append("- **渚濊禆鍏崇郴鍘熷垯**: 鍚堢悊瀹夋帓姝ラ椤哄簭锛岀‘淇濆墠缃潯浠跺緱鍒版弧瓒砛n");
        prompt.append("- **缁撴灉杈撳嚭鍘熷垯**: 姣忎釜姝ラ閮藉簲鏈夋槑纭殑杈撳嚭缁撴灉鍜屾垚鍔熸爣鍑哱n\n");

        // 6. 姝ラ绫诲瀷鎸囧
        prompt.append("### 姝ラ绫诲瀷鎸囧\n");
        prompt.append("鏍规嵁鍙敤宸ュ叿鍜岀敤鎴烽渶姹傦紝甯歌鐨勬楠ょ被鍨嬪寘鎷細\n");
        prompt.append("- **鏁版嵁鑾峰彇姝ラ**: 浣跨敤鎼滅储銆佹煡璇㈢瓑宸ュ叿鑾峰彇鎵€闇€淇℃伅\n");
        prompt.append("- **鏁版嵁澶勭悊姝ラ**: 瀵硅幏鍙栫殑淇℃伅杩涜鍒嗘瀽銆佹暣鐞嗗拰鍔犲伐\n");
        prompt.append("- **鍐呭鐢熸垚姝ラ**: 鍩轰簬澶勭悊鍚庣殑鏁版嵁鐢熸垚鐩爣鍐呭\n");
        prompt.append("- **缁撴灉杈撳嚭姝ラ**: 灏嗙敓鎴愮殑鍐呭鍙戝竷銆佷繚瀛樻垨浼犻€掔粰鐢ㄦ埛\n");
        prompt.append("- **閫氱煡鍙嶉姝ラ**: 鍚戠敤鎴锋垨鐩稿叧鏂瑰彂閫佹墽琛岀粨鏋滈€氱煡\n\n");

        // 7. 鎵ц瑕佹眰
        prompt.append("### 鎵ц瑕佹眰\n");
        prompt.append("1. **姝ラ缂栧彿**: 浣跨敤绗?姝ャ€佺2姝ャ€佺3姝?..鏍煎紡\n");
        prompt.append("2. **Markdown鏍煎紡**: 涓ユ牸鎸夌収涓婅堪Markdown鏍煎紡杈撳嚭\n");
        prompt.append("3. **姝ラ鎻忚堪**: 姣忎釜姝ラ鎻忚堪瑕佹竻鏅般€佸叿浣撱€佸彲鎵ц\n");
        prompt.append("4. **浼樺厛绾?*: 鏍规嵁姝ラ閲嶈鎬у拰绱ф€ョ▼搴﹁瀹歕n");
        prompt.append("5. **鏃堕暱浼扮畻**: 鍩轰簬姝ラ澶嶆潅搴﹀悎鐞嗕及绠梊n");
        prompt.append("6. **宸ュ叿閫夋嫨**: 浠庡彲鐢ㄥ伐鍏蜂腑閫夋嫨鏈€閫傚悎鐨勶紝蹇呴』浣跨敤瀹屾暣鐨勫嚱鏁板悕绉癨n");
        prompt.append("7. **渚濊禆鍏崇郴**: 鏄庣‘姝ラ闂寸殑鍏堝悗椤哄簭\n");
        prompt.append("8. **鎵ц缁嗚妭**: 鎻愪緵鍏蜂綋鍙搷浣滅殑鏂规硶锛屽寘鍚缁嗙殑鍙傛暟璇存槑鍜岀敤鎴烽渶姹備紶閫抃n");
        prompt.append("9. **闇€姹備紶閫?*: 纭繚鐢ㄦ埛鐨勬墍鏈夎缁嗚姹傞兘鑳藉噯纭紶閫掑埌鐩稿簲鐨勬墽琛屾楠や腑\n");
        prompt.append("10. **鍔熻兘鐙珛**: 纭繚姣忎釜姝ラ鍔熻兘鐙珛锛岄伩鍏嶆贩鍚堜笉鍚岀被鍨嬬殑鎿嶄綔\n");
        prompt.append("11. **宸ュ叿鏄犲皠**: 姣忎釜姝ラ蹇呴』鏄庣‘鎸囧畾浣跨敤鐨凪CP宸ュ叿鍑芥暟鍚嶇О\n");
        prompt.append("12. **璐ㄩ噺鏍囧噯**: 璁惧畾鏄庣‘鐨勫畬鎴愭爣鍑哱n\n");

        // 7. 姝ラ绫诲瀷鎸囧
        prompt.append("### 甯歌姝ラ绫诲瀷鎸囧\n");
        prompt.append("- **淇℃伅鑾峰彇姝ラ**: 浣跨敤鎼滅储宸ュ叿锛屽叧娉ㄥ叧閿瘝閫夋嫨鍜岀粨鏋滅瓫閫塡n");
        prompt.append("- **鍐呭澶勭悊姝ラ**: 鍩轰簬鑾峰彇鐨勪俊鎭繘琛屽垎鏋愩€佹暣鐞嗗拰鍒涗綔\n");
        prompt.append("- **缁撴灉杈撳嚭姝ラ**: 浣跨敤鐩稿簲骞冲彴宸ュ叿鍙戝竷鎴栦繚瀛樺鐞嗙粨鏋淺n");
        prompt.append("- **閫氱煡鍙嶉姝ラ**: 浣跨敤閫氫俊宸ュ叿杩涜鐘舵€侀€氱煡鎴栫粨鏋滃弽棣圽n");
        prompt.append("- **鏁版嵁澶勭悊姝ラ**: 瀵硅幏鍙栫殑淇℃伅杩涜鍒嗘瀽銆佽浆鎹㈠拰澶勭悊\n\n");

        // 8. 璐ㄩ噺妫€鏌?        prompt.append("### 璐ㄩ噺妫€鏌ユ竻鍗昞n");
        prompt.append("鐢熸垚璁″垝鍚庤纭锛歕n");
        prompt.append("- [ ] 姣忎釜姝ラ閮芥湁鏄庣‘鐨勫簭鍙峰拰鎻忚堪\n");
        prompt.append("- [ ] 浣跨敤浜嗘纭殑Markdown鏍煎紡\n");
        prompt.append("- [ ] 姝ラ鎻忚堪娓呮櫚鍏蜂綋\n");
        prompt.append("- [ ] 浼樺厛绾ц缃悎鐞哱n");
        prompt.append("- [ ] 鏃堕暱浼扮畻鐜板疄鍙\n");
        prompt.append("- [ ] 宸ュ叿閫夋嫨鎭板綋\n");
        prompt.append("- [ ] 渚濊禆鍏崇郴娓呮櫚\n");
        prompt.append("- [ ] 鎵ц鏂规硶鍏蜂綋鍙搷浣淺n");
        prompt.append("- [ ] 鎴愬姛鏍囧噯鏄庣‘鍙　閲廫n\n");

        prompt.append("鐜板湪璇峰紑濮嬬敓鎴怣arkdown鏍煎紡鐨勬墽琛屾楠よ鍒掞細\n");

        return prompt.toString();
    }

    /**
     * 鑾峰彇瀹為檯鐨凪CP宸ュ叿淇℃伅
     */
    private String getActualMcpToolsInfo() {
        StringBuilder toolsInfo = new StringBuilder();
        toolsInfo.append("# 褰撳墠娉ㄥ唽鐨凪CP宸ュ叿鍒楄〃\n\n");

        try {
            // 鑾峰彇鐧惧害鎼滅储宸ュ叿淇℃伅
            toolsInfo.append("## 1. 鐧惧害鎼滅储宸ュ叿 (BaiduSearch)\n");
            toolsInfo.append("- **鏈嶅姟绔偣**: http://localhost:8080/mcp/baidu-search\n");
            toolsInfo.append("- **鏍稿績鍔熻兘**: 閫氳繃鐧惧害鎼滅储寮曟搸妫€绱㈡妧鏈祫鏂欏拰淇℃伅\n");
            toolsInfo.append("- **涓昏宸ュ叿鍑芥暟**: search\n");
            toolsInfo.append("- **鍙傛暟瑕佹眰**: query(鎼滅储鍏抽敭璇?\n");
            toolsInfo.append("- **閫傜敤鍦烘櫙**: 鎶€鏈祫鏂欐悳绱€佷俊鎭敹闆嗐€佺煡璇嗚幏鍙朶n\n");

            // 鑾峰彇CSDN宸ュ叿淇℃伅
            toolsInfo.append("## 2. CSDN鍙戝竷宸ュ叿 (CsdnPublish)\n");
            toolsInfo.append("- **鏈嶅姟绔偣**: http://localhost:8080/mcp/csdn\n");
            toolsInfo.append("- **鏍稿績鍔熻兘**: 鍙戝竷鎶€鏈枃绔犲埌CSDN骞冲彴\n");
            toolsInfo.append("- **涓昏宸ュ叿鍑芥暟**: publish_article\n");
            toolsInfo.append("- **鍙傛暟瑕佹眰**: title(鏂囩珷鏍囬), content(鏂囩珷鍐呭), tags(鏍囩)\n");
            toolsInfo.append("- **閫傜敤鍦烘櫙**: 鎶€鏈枃绔犲彂甯冦€佺煡璇嗗垎浜€佸唴瀹瑰垱浣淺n\n");

            // 鑾峰彇寰俊宸ュ叿淇℃伅
            toolsInfo.append("## 3. 寰俊閫氱煡宸ュ叿 (WeixinNotify)\n");
            toolsInfo.append("- **鏈嶅姟绔偣**: http://localhost:8080/mcp/weixin\n");
            toolsInfo.append("- **鏍稿績鍔熻兘**: 鍙戦€佸井淇￠€氱煡娑堟伅\n");
            toolsInfo.append("- **涓昏宸ュ叿鍑芥暟**: send_message\n");
            toolsInfo.append("- **鍙傛暟瑕佹眰**: message(娑堟伅鍐呭), recipient(鎺ユ敹鑰?\n");
            toolsInfo.append("- **閫傜敤鍦烘櫙**: 鐘舵€侀€氱煡銆佺粨鏋滃弽棣堛€佷换鍔℃彁閱抃n\n");

        } catch (Exception e) {
            log.warn("鑾峰彇MCP宸ュ叿淇℃伅鏃跺彂鐢熼敊璇? {}", e.getMessage());
            toolsInfo.append("## 宸ュ叿淇℃伅鑾峰彇澶辫触\n");
            toolsInfo.append("璇锋鏌CP鏈嶅姟杩炴帴鐘舵€乗n\n");
        }

        return toolsInfo.toString();
    }

    @Override
    public StrategyHandler<ExecuteCommandEntity, DefaultFlowAgentExecuteStrategyFactory.DynamicContext, String> get(ExecuteCommandEntity requestParameter, DefaultFlowAgentExecuteStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return step3ParseStepsNode;
    }

}
