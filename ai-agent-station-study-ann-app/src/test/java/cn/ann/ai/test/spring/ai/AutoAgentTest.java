package cn.ann.ai.test.spring.ai;

import cn.ann.ai.test.spring.ai.advisors.RagAnswerAdvisor;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDate;

/**
 * @author zhang san
 * @description
 * @create 2026/2/1 9:37
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class AutoAgentTest {
    private ChatModel chatModel;
    private ChatClient planningChatClient;
    private ChatClient executorChatClient;
    private ChatClient reactChatClient;

    @Resource
    private PgVectorStore vectorStore;

    public static final String CHAT_MEMORY_CONVERSATION_ID_KEY = "chat_memory_conversation_id";
    public static final String CHAT_MEMORY_RETRIEVE_SIZE_KEY = "chat_memory_response_size";

    @Before//琚娉ㄨВ鏍囪鐨勬柟娉曚細鍦ㄨ繖涓被鐨勬瘡涓柟娉曟墽琛屽墠鎵ц涓€娆?
    public void init() {
        //鍒濆鍖朅pi
        OpenAiApi openAiApi = OpenAiApi.builder()
                .baseUrl("https://apis.itedus.cn")
                .apiKey("${OPENAI_API_KEY}互鑱旂郴灏忓倕鍝ョ敵璇?)
                .completionsPath("v1/chat/completions")
                .embeddingsPath("v1/embeddings")
                .build();
        //鍒濆鍖杕odel
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4.1")
                        .toolCallbacks(new SyncMcpToolCallbackProvider(sseMcpClient(), stdioMcpClient()).getToolCallbacks())
                        .build())
                .build();
        // 鍒濆鍖朼gent瀹㈡埛绔?Planning Agent ChatClient - 璐熻矗浠诲姟瑙勫垝
        planningChatClient = ChatClient.builder(chatModel)
                .defaultSystem("""
                        # 瑙掕壊
                        浣犳槸涓€涓櫤鑳戒换鍔¤鍒掑姪鎵嬶紝鍚嶅彨 AutoAgent Planning銆?
                        
                        # 璇存槑
                        浣犳槸浠诲姟瑙勫垝鍔╂墜锛屾牴鎹敤鎴烽渶姹傦紝鎷嗚В浠诲姟鍒楄〃锛屽埗瀹氭墽琛岃鍒掋€傛瘡娆℃墽琛屽墠锛屽繀椤诲厛杈撳嚭鏈疆鎬濊€冭繃绋嬶紝鍐嶇敓鎴愬叿浣撶殑浠诲姟鍒楄〃銆?
                        
                        # 鎶€鑳?
                        - 鎿呴暱灏嗙敤鎴蜂换鍔℃媶瑙ｄ负鍏蜂綋銆佺嫭绔嬬殑浠诲姟鍒楄〃
                        - 瀵圭畝鍗曚换鍔★紝閬垮厤杩囧害鎷嗚В
                        - 瀵瑰鏉備换鍔★紝鍚堢悊鎷嗚В涓哄涓湁閫昏緫鍏宠仈鐨勫瓙浠诲姟
                        
                        # 澶勭悊闇€姹?
                        ## 鎷嗚В浠诲姟
                        - 娣卞害鎺ㄧ悊鍒嗘瀽鐢ㄦ埛杈撳叆锛岃瘑鍒牳蹇冮渶姹傚強娼滃湪鎸戞垬
                        - 灏嗗鏉傞棶棰樺垎瑙ｄ负鍙鐞嗐€佸彲鎵ц銆佺嫭绔嬩笖娓呮櫚鐨勫瓙浠诲姟
                        - 浠诲姟鎸夐『搴忔垨鍥犳灉閫昏緫缁勭粐锛屼笂涓嬩换鍔￠€昏緫杩炶疮
                        - 鎷嗚В鏈€澶氫笉瓒呰繃5涓换鍔?
                        
                        ## 杈撳嚭鏍煎紡
                        璇锋寜浠ヤ笅鏍煎紡杈撳嚭浠诲姟璁″垝锛?
                        
                        **浠诲姟瑙勫垝锛?*
                        1. [浠诲姟1鎻忚堪]
                        2. [浠诲姟2鎻忚堪]
                        3. [浠诲姟3鎻忚堪]
                        ...
                        
                        **鎵ц绛栫暐锛?*
                        [鏁翠綋鎵ц绛栫暐璇存槑]
                        
                        浠婂ぉ鏄?{current_date}銆?
                        """)
                .defaultAdvisors(
                        //璁板繂鍖?
                        PromptChatMemoryAdvisor.builder(
                                MessageWindowChatMemory.builder()
                                        .maxMessages(100)
                                        .build()
                        ).build(),
                        //杈撳叆杈撳嚭鐨勬棩蹇?
                        SimpleLoggerAdvisor.builder().build()
                )
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4.1")
                        .maxTokens(2000)
                        .build())
                .build();

        // 鍒濆鍖?Executor Agent ChatClient - 璐熻矗浠诲姟鎵ц
        executorChatClient = ChatClient.builder(chatModel)
                .defaultSystem("""
                       # 瑙掕壊
                        浣犳槸涓€涓櫤鑳戒换鍔¤鍒掑姪鎵嬶紝鍚嶅彨 AutoAgent Planning銆?
                        
                        # 璇存槑
                        浣犳槸浠诲姟瑙勫垝鍔╂墜锛屾牴鎹敤鎴烽渶姹傦紝鎷嗚В浠诲姟鍒楄〃锛屽埗瀹氭墽琛岃鍒掋€傛瘡娆℃墽琛屽墠锛屽繀椤诲厛杈撳嚭鏈疆鎬濊€冭繃绋嬶紝鍐嶇敓鎴愬叿浣撶殑浠诲姟鍒楄〃銆?
                        
                        # 鎶€鑳?
                        - 鎿呴暱灏嗙敤鎴蜂换鍔℃媶瑙ｄ负鍏蜂綋銆佺嫭绔嬬殑浠诲姟鍒楄〃
                        - 瀵圭畝鍗曚换鍔★紝閬垮厤杩囧害鎷嗚В
                        - 瀵瑰鏉備换鍔★紝鍚堢悊鎷嗚В涓哄涓湁閫昏緫鍏宠仈鐨勫瓙浠诲姟
                        
                        # 澶勭悊闇€姹?
                        ## 鎷嗚В浠诲姟
                        - 娣卞害鎺ㄧ悊鍒嗘瀽鐢ㄦ埛杈撳叆锛岃瘑鍒牳蹇冮渶姹傚強娼滃湪鎸戞垬
                        - 灏嗗鏉傞棶棰樺垎瑙ｄ负鍙鐞嗐€佸彲鎵ц銆佺嫭绔嬩笖娓呮櫚鐨勫瓙浠诲姟
                        - 浠诲姟鎸夐『搴忔垨鍥犳灉閫昏緫缁勭粐锛屼笂涓嬩换鍔￠€昏緫杩炶疮
                        - 鎷嗚В鏈€澶氫笉瓒呰繃5涓换鍔?
                        
                        ## 杈撳嚭鏍煎紡
                        璇锋寜浠ヤ笅鏍煎紡杈撳嚭浠诲姟璁″垝锛?
                        
                        **浠诲姟瑙勫垝锛?*
                        1. [浠诲姟1鎻忚堪]
                        2. [浠诲姟2鎻忚堪]
                        3. [浠诲姟3鎻忚堪]
                        ...
                        
                        **鎵ц绛栫暐锛?*
                        [鏁翠綋鎵ц绛栫暐璇存槑]
                        
                        浠婂ぉ鏄?{current_date}銆?
                        """
                )
                .defaultAdvisors(
                        PromptChatMemoryAdvisor.builder(
                                MessageWindowChatMemory.builder().maxMessages(100).build()
                        ).build(),
                        SimpleLoggerAdvisor.builder().build(),
                        new RagAnswerAdvisor(vectorStore, SearchRequest.builder()
                                .topK(5)
                                .filterExpression("knowledge == 'article-prompt-words'")
                                .build())
                )
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4.1")
                        .maxTokens(4000)
                        .build())
                .build();

        // 鍒濆鍖?React Agent ChatClient - 璐熻矗鍝嶅簲寮忓鐞?
        reactChatClient = ChatClient.builder(chatModel)
                .defaultSystem("""
                        # 瑙掕壊
                        浣犳槸涓€涓櫤鑳藉搷搴斿姪鎵嬶紝鍚嶅彨 AutoAgent React銆?
                        
                        # 璇存槑
                        浣犺礋璐ｅ鐢ㄦ埛鐨勫嵆鏃堕棶棰樿繘琛屽揩閫熷搷搴斿拰澶勭悊锛岄€傜敤浜庣畝鍗曠殑鏌ヨ鍜屼氦浜掋€?
                        
                        # 澶勭悊鏂瑰紡
                        - 瀵逛簬绠€鍗曢棶棰橈紝鐩存帴缁欏嚭绛旀
                        - 瀵逛簬闇€瑕佸伐鍏风殑闂锛岃皟鐢ㄧ浉搴斿伐鍏疯幏鍙栦俊鎭?
                        - 淇濇寔鍝嶅簲鐨勫強鏃舵€у拰鍑嗙‘鎬?
                        
                        浠婂ぉ鏄?{current_date}銆?
                        """)
                .defaultAdvisors(
                        PromptChatMemoryAdvisor.builder(
                                MessageWindowChatMemory.builder().maxMessages(10).build()
                        ).build(),
                        SimpleLoggerAdvisor.builder().build()
                )
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4.1-mini")
                        .maxTokens(1500)
                        .build())
                .build();
    }

    @Test
    public void test_planning_agent(){
        String userRequest = "甯垜鍒嗘瀽涓€涓嬪綋鍓岮I鎶€鏈彂灞曡秼鍔匡紝骞剁敓鎴愪竴浠借缁嗙殑鎶€鏈姤鍛?;
        log.info("=== Planning Agent 娴嬭瘯寮€濮?===");
        log.info("鐢ㄦ埛闇€姹? {}", userRequest);

        String content = planningChatClient
                .prompt(userRequest)//缁欏鎴风杈撳叆鐢ㄦ埛璇锋眰
                .system(s -> s.param("current_date", LocalDate.now().toString()))//濉厖浜鸿鐨勫崰浣嶇
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, "planning-session-001")//璁板綍鏈浼氳瘽id锛屼究浜庤蹇嗗洖婧?
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 50))//璁剧疆璁板繂鍥炴函鐨勬潯鏁?
                .call().content();

        log.info("瑙勫垝缁撴灉: {}", content);
        log.info("=== Planning Agent 娴嬭瘯缁撴潫 ===");

    }

    /**
     * 娴嬭瘯 Executor Agent - 浠诲姟鎵ц鍔熻兘
     */
    @Test
    public void test_executor_agent() {
        String taskDescription = "鎼滅储AI鎶€鏈彂灞曠殑鏈€鏂颁俊鎭紝骞舵暣鐞嗘垚缁撴瀯鍖栫殑鏁版嵁";

        log.info("=== Executor Agent 娴嬭瘯寮€濮?===");
        log.info("鎵ц浠诲姟: {}", taskDescription);

        String executionResult = executorChatClient
                .prompt(taskDescription)
                .system(s -> s.param("current_date", LocalDate.now().toString()))
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, "executor-session-001")
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .call().content();

        log.info("鎵ц缁撴灉: {}", executionResult);
        log.info("=== Executor Agent 娴嬭瘯缁撴潫 ===");
    }


    /**
     * 娴嬭瘯 React Agent - 鍝嶅簲寮忓鐞嗗姛鑳? 瀵瑰簲绗竴绉?
     */
    //璋冪敤澶氫釜agent锛屾瘡涓猘gent鏍规嵁杈撳叆鐨勫懡浠よ繑鍥炵粨鏋滐紝閾惧紡锛屽氨鏄皢涓婁竴涓祦绋嬬殑agent鐨勮繑鍥炵粨鏋滅殑String浣滀负褰撳墠agent鐨勫懡浠ゅ叆鍙備竴閮ㄥ垎
    @Test
    public void test_react_agent() {
        String userRequest = "甯垜鍒涘缓涓€涓叧浜嶴pring AI妗嗘灦鐨勬妧鏈枃妗ｏ紝鍖呮嫭鏍稿績姒傚康銆佷娇鐢ㄧず渚嬪拰鏈€浣冲疄璺?;

        log.info("=== 瀹屾暣 AutoAgent 宸ヤ綔娴佺▼娴嬭瘯寮€濮?===");
        log.info("鐢ㄦ埛璇锋眰: {}", userRequest);

        // 绗竴姝ワ細浠诲姟瑙勫垝 (Planning)
        log.info("--- 姝ラ1: 浠诲姟瑙勫垝 ---");
        String planningResult = planningChatClient
                .prompt("璇蜂负浠ヤ笅鐢ㄦ埛闇€姹傚埗瀹氳缁嗙殑鎵ц璁″垝锛? + userRequest)
                .system(s -> s.param("current_date", LocalDate.now().toString()))
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, "workflow-planning-001")
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 50))
                .call().content();

        log.info("瑙勫垝缁撴灉: {}", planningResult);

        // 绗簩姝ワ細浠诲姟鎵ц (Execution)
        log.info("--- 姝ラ2: 浠诲姟鎵ц ---");
        String executionContext = String.format("""
                鏍规嵁浠ヤ笅浠诲姟瑙勫垝锛岃閫愭鎵ц姣忎釜浠诲姟锛?
                
                浠诲姟瑙勫垝锛?
                %s
                
                鍘熷鐢ㄦ埛闇€姹傦細%s
                
                璇峰紑濮嬫墽琛岀涓€涓换鍔°€?
                """, planningResult, userRequest);

        String executionResult = executorChatClient
                .prompt(executionContext)
                .system(s -> s.param("current_date", LocalDate.now().toString()))
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, "workflow-execution-001")
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .call().content();

        log.info("鎵ц缁撴灉: {}", executionResult);

        // 绗笁姝ワ細缁撴灉鎬荤粨鍜岄獙璇?
        log.info("--- 姝ラ3: 缁撴灉鎬荤粨 ---");
        String summaryContext = String.format("""
                璇峰浠ヤ笅鎵ц缁撴灉杩涜鎬荤粨锛屽苟楠岃瘉鏄惁婊¤冻鐢ㄦ埛鐨勫師濮嬮渶姹傦細
                
                鍘熷闇€姹傦細%s
                
                鎵ц缁撴灉锛?s
                
                璇锋彁渚涙渶缁堢殑鎬荤粨鎶ュ憡銆?
                """, userRequest, executionResult);

        String summaryResult = reactChatClient
                .prompt(summaryContext)
                .system(s -> s.param("current_date", LocalDate.now().toString()))
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, "workflow-summary-001")
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 50))
                .call().content();

        log.info("鎬荤粨鎶ュ憡: {}", summaryResult);
        log.info("=== 瀹屾暣 AutoAgent 宸ヤ綔娴佺▼娴嬭瘯缁撴潫 ===");
    }

    /**
     * 娴嬭瘯澶氳疆瀵硅瘽 - 妯℃嫙鎸佺画鐨勭敤鎴蜂氦浜?  绗簩绉?
     */
    @Test
    public void test_multi_turn_conversation() {
        String conversationId = "multi-turn-001";

        log.info("=== 澶氳疆瀵硅瘽娴嬭瘯寮€濮?===");

        // 绗竴杞璇?
        String firstQuery = "璇蜂粙缁嶄竴涓婼pring AI妗嗘灦";
        log.info("绗竴杞敤鎴疯緭鍏? {}", firstQuery);

        String firstResponse = reactChatClient
                .prompt(firstQuery)
                .system(s -> s.param("current_date", LocalDate.now().toString()))
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 20))
                .call().content();

        log.info("绗竴杞瓵I鍝嶅簲: {}", firstResponse);

        // 绗簩杞璇?
        String secondQuery = "瀹冩湁鍝簺鏍稿績缁勪欢锛?;
        log.info("绗簩杞敤鎴疯緭鍏? {}", secondQuery);

        String secondResponse = reactChatClient
                .prompt(secondQuery)
                .system(s -> s.param("current_date", LocalDate.now().toString()))
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 20))
                .call().content();

        log.info("绗簩杞瓵I鍝嶅簲: {}", secondResponse);

        // 绗笁杞璇?
        String thirdQuery = "鑳界粰鎴戜竴涓叿浣撶殑浣跨敤绀轰緥鍚楋紵";
        log.info("绗笁杞敤鎴疯緭鍏? {}", thirdQuery);

        String thirdResponse = reactChatClient
                .prompt(thirdQuery)
                .system(s -> s.param("current_date", LocalDate.now().toString()))
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 20))
                .call().content();

        log.info("绗笁杞瓵I鍝嶅簲: {}", thirdResponse);
        log.info("=== 澶氳疆瀵硅瘽娴嬭瘯缁撴潫 ===");
    }


    /**
     * 鍔ㄦ€佸杞墽琛屾祴璇?- 妯℃嫙 PlanningAgent 鍜?ExecutorAgent 鐨勫畬鏁村姩鎬佹墽琛屾祦绋?
     *
     * 鎵ц鐗圭偣锛?
     * 1. 鍔ㄦ€佸垎鏋愮敤鎴疯緭鍏ワ紝鑷富鍐冲畾鎵ц绛栫暐
     * 2. 鏍规嵁姣忚疆鎵ц缁撴灉锛屾櫤鑳藉垽鏂笅涓€姝ヨ鍔?
     * 3. 鏀寔鍙厤缃殑鏈€澶ф墽琛屾鏁?
     * 4. 鍏峰浠诲姟瀹屾垚鍒ゆ柇鍜屾彁鍓嶇粓姝㈡満鍒?
     * 5. 妯℃嫙鐪熷疄鐨?Agent 鎬濊€?琛屽姩-瑙傚療寰幆
     */
    @Test
    public void test_dynamic_multi_step_execution() {
        // 閰嶇疆鍙傛暟
        int maxSteps = 4; // 鏈€澶ф墽琛屾鏁?
        String userInput = "鎼滅储灏忓倕鍝ワ紝鎶€鏈」鐩垪琛ㄣ€傜紪鍐欐垚涓€浠芥枃妗ｏ紝璇存槑涓嶅悓椤圭洰鐨勫涔犵洰鏍囷紝浠ュ強涓嶅悓闃舵鐨勪紮浼村簲璇ュ涔犲摢涓」鐩€?;
        userInput = "鎼滅储 springboot 鐩稿叧鐭ヨ瘑锛岀敓鎴?涓富瑕佸唴瀹圭珷鑺傘€傛瘡涓珷鑺傝鍖呮嫭璇剧▼鍐呭鍜岄厤濂楃ず渚嬩唬鐮併€傚苟鍙戝搴旂珷鑺傚垱寤哄md鏂囨。锛屾柟渚垮皬鐧戒紮浼村涔犮€?;
        String sessionId = "dynamic-execution-" + System.currentTimeMillis();

        log.info("=== 鍔ㄦ€佸杞墽琛屾祴璇曞紑濮?====");
        log.info("鐢ㄦ埛杈撳叆: {}", userInput);
        log.info("鏈€澶ф墽琛屾鏁? {}", maxSteps);
        log.info("浼氳瘽ID: {}", sessionId);

        // 鍒濆鍖栨墽琛屼笂涓嬫枃
        StringBuilder executionHistory = new StringBuilder();
        String currentTask = userInput;
        boolean isCompleted = false;

        // 鍒濆鍖栦换鍔″垎鏋愬櫒 ChatClient - 璐熻矗浠诲姟鍒嗘瀽鍜岀姸鎬佸垽鏂?
        ChatClient taskAnalyzerClient = ChatClient.builder(chatModel)
                .defaultSystem("""
                        # 瑙掕壊
                        浣犳槸涓€涓笓涓氱殑浠诲姟鍒嗘瀽甯堬紝鍚嶅彨 AutoAgent Task Analyzer銆?
                        
                        # 鏍稿績鑱岃矗
                        浣犺礋璐ｅ垎鏋愪换鍔＄殑褰撳墠鐘舵€併€佹墽琛屽巻鍙插拰涓嬩竴姝ヨ鍔ㄨ鍒掞細
                        1. **鐘舵€佸垎鏋?*: 娣卞害鍒嗘瀽褰撳墠浠诲姟瀹屾垚鎯呭喌鍜屾墽琛屽巻鍙?
                        2. **杩涘害璇勪及**: 璇勪及浠诲姟瀹屾垚杩涘害鍜岃川閲?
                        3. **绛栫暐鍒跺畾**: 鍒跺畾涓嬩竴姝ユ渶浼樻墽琛岀瓥鐣?
                        4. **瀹屾垚鍒ゆ柇**: 鍑嗙‘鍒ゆ柇浠诲姟鏄惁宸插畬鎴?
                        
                        # 鍒嗘瀽鍘熷垯
                        - **鍏ㄩ潰鎬?*: 缁煎悎鑰冭檻鎵€鏈夋墽琛屽巻鍙插拰褰撳墠鐘舵€?
                        - **鍑嗙‘鎬?*: 鍑嗙‘璇勪及浠诲姟瀹屾垚搴﹀拰璐ㄩ噺
                        - **鍓嶇灮鎬?*: 棰勬祴鍙兘鐨勯棶棰樺拰鏈€浼樿矾寰?
                        - **鏁堢巼鎬?*: 浼樺寲鎵ц璺緞锛岄伩鍏嶉噸澶嶅伐浣?
                        
                        # 杈撳嚭鏍煎紡
                        **浠诲姟鐘舵€佸垎鏋?**
                        [褰撳墠浠诲姟瀹屾垚鎯呭喌鐨勮缁嗗垎鏋怾
                        
                        **鎵ц鍘嗗彶璇勪及:**
                        [瀵瑰凡瀹屾垚宸ヤ綔鐨勮川閲忓拰鏁堟灉璇勪及]
                        
                        **涓嬩竴姝ョ瓥鐣?**
                        [鍏蜂綋鐨勪笅涓€姝ユ墽琛岃鍒掑拰绛栫暐]
                        
                        **瀹屾垚搴﹁瘎浼?** [0-100]%
                        **浠诲姟鐘舵€?** [CONTINUE/COMPLETED]
                        """)
                .defaultAdvisors(
                        PromptChatMemoryAdvisor.builder(
                                MessageWindowChatMemory.builder()
                                        .maxMessages(100)
                                        .build()
                        ).build(),
                        SimpleLoggerAdvisor.builder().build())
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4.1")
                        .maxTokens(2000)
                        .temperature(0.3)
                        .build())
                .build();


        // 鍒濆鍖栫簿鍑嗘墽琛屽櫒 ChatClient - 璐熻矗鍏蜂綋浠诲姟鎵ц
        ChatClient precisionExecutorClient = ChatClient.builder(chatModel)
                .defaultSystem("""
                        # 瑙掕壊
                        浣犳槸涓€涓簿鍑嗕换鍔℃墽琛屽櫒锛屽悕鍙?AutoAgent Precision Executor銆?
                        
                        # 鏍稿績鑳藉姏
                        浣犱笓娉ㄤ簬绮惧噯鎵ц鍏蜂綋鐨勪换鍔℃楠わ細
                        1. **绮惧噯鎵ц**: 涓ユ牸鎸夌収鍒嗘瀽甯堢殑绛栫暐鎵ц浠诲姟
                        2. **宸ュ叿浣跨敤**: 鐔熺粌浣跨敤鍚勭宸ュ叿瀹屾垚澶嶆潅鎿嶄綔
                        3. **璐ㄩ噺鎺у埗**: 纭繚姣忎竴姝ユ墽琛岀殑鍑嗙‘鎬у拰瀹屾暣鎬?
                        4. **缁撴灉璁板綍**: 璇︾粏璁板綍鎵ц杩囩▼鍜岀粨鏋?
                        
                        # 鎵ц鍘熷垯
                        - **涓撴敞鎬?*: 涓撴敞浜庡綋鍓嶅垎閰嶇殑鍏蜂綋浠诲姟
                        - **绮惧噯鎬?*: 纭繚鎵ц缁撴灉鐨勫噯纭€у拰璐ㄩ噺
                        - **瀹屾暣鎬?*: 瀹屾暣鎵ц鎵€鏈夊繀瑕佺殑姝ラ
                        - **鍙拷婧€?*: 璇︾粏璁板綍鎵ц杩囩▼渚夸簬鍚庣画鍒嗘瀽
                        
                        # 杈撳嚭鏍煎紡
                        **鎵ц鐩爣:**
                        [鏈疆瑕佹墽琛岀殑鍏蜂綋鐩爣]
                        
                        **鎵ц杩囩▼:**
                        [璇︾粏鐨勬墽琛屾楠ゅ拰浣跨敤鐨勫伐鍏穄
                        
                        **鎵ц缁撴灉:**
                        [鎵ц鐨勫叿浣撶粨鏋滃拰鑾峰緱鐨勪俊鎭痌
                        
                        **璐ㄩ噺妫€鏌?**
                        [瀵规墽琛岀粨鏋滅殑璐ㄩ噺璇勪及]
                        """)
                .defaultToolCallbacks(new SyncMcpToolCallbackProvider(stdioMcpClient(), sseMcpClient()).getToolCallbacks())
                .defaultAdvisors(
                        PromptChatMemoryAdvisor.builder(
                                MessageWindowChatMemory.builder()
                                        .maxMessages(150)
                                        .build()
                        ).build(),
                        new RagAnswerAdvisor(vectorStore, SearchRequest.builder()
                                .topK(8)
                                .filterExpression("knowledge == 'article-prompt-words'")
                                .build()),
                        SimpleLoggerAdvisor.builder().build())
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4.1")
                        .maxTokens(4000)
                        .temperature(0.5)
                        .build())
                .build();

        // 鍒濆鍖栬川閲忕洃鐫ｅ櫒 ChatClient - 璐熻矗璐ㄩ噺妫€鏌ュ拰浼樺寲
        ChatClient qualitySupervisorClient = ChatClient.builder(chatModel)
                .defaultSystem("""
                        # 瑙掕壊
                        浣犳槸涓€涓笓涓氱殑璐ㄩ噺鐩戠潱鍛橈紝鍚嶅彨 AutoAgent Quality Supervisor銆?
                        
                        # 鏍稿績鑱岃矗
                        浣犺礋璐ｇ洃鐫ｅ拰璇勪及鎵ц璐ㄩ噺锛?
                        1. **璐ㄩ噺璇勪及**: 璇勪及鎵ц缁撴灉鐨勫噯纭€у拰瀹屾暣鎬?
                        2. **闂璇嗗埆**: 璇嗗埆鎵ц杩囩▼涓殑闂鍜屼笉瓒?
                        3. **鏀硅繘寤鸿**: 鎻愪緵鍏蜂綋鐨勬敼杩涘缓璁拰浼樺寲鏂规
                        4. **鏍囧噯鍒跺畾**: 鍒跺畾璐ㄩ噺鏍囧噯鍜岃瘎浼版寚鏍?
                        
                        # 璇勪及鏍囧噯
                        - **鍑嗙‘鎬?*: 缁撴灉鏄惁鍑嗙‘鏃犺
                        - **瀹屾暣鎬?*: 鏄惁閬楁紡閲嶈淇℃伅
                        - **鐩稿叧鎬?*: 鏄惁绗﹀悎鐢ㄦ埛闇€姹?
                        - **鍙敤鎬?*: 缁撴灉鏄惁瀹炵敤鏈夋晥
                        
                        # 杈撳嚭鏍煎紡
                        **璐ㄩ噺璇勪及:**
                        [瀵规墽琛岀粨鏋滅殑璇︾粏璐ㄩ噺璇勪及]
                        
                        **闂璇嗗埆:**
                        [鍙戠幇鐨勯棶棰樺拰涓嶈冻涔嬪]
                        
                        **鏀硅繘寤鸿:**
                        [鍏蜂綋鐨勬敼杩涘缓璁拰浼樺寲鏂规]
                        
                        **璐ㄩ噺璇勫垎:** [0-100]鍒?
                        **鏄惁閫氳繃:** [PASS/FAIL/OPTIMIZE]
                        """)
                .defaultAdvisors(
                        PromptChatMemoryAdvisor.builder(
                                MessageWindowChatMemory.builder()
                                        .maxMessages(80)
                                        .build()
                        ).build(),
                        SimpleLoggerAdvisor.builder().build())
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4.1")
                        .maxTokens(2500)
                        .temperature(0.2)
                        .build())
                .build();

        // 寮€濮嬬簿鍑嗗杞墽琛?
        for (int step = 1; step <= maxSteps && !isCompleted; step++) {
            log.info("\n馃幆 === 鎵ц绗?{} 姝?===", step);

            try {
                // 绗竴闃舵锛氫换鍔″垎鏋?
                log.info("\n馃搳 闃舵1: 浠诲姟鐘舵€佸垎鏋?);
                String analysisPrompt = String.format("""
                        **鍘熷鐢ㄦ埛闇€姹?** %s
                        
                        **褰撳墠鎵ц姝ラ:** 绗?%d 姝?(鏈€澶?%d 姝?
                        
                        **鍘嗗彶鎵ц璁板綍:**
                        %s
                        
                        **褰撳墠浠诲姟:** %s
                        
                        璇峰垎鏋愬綋鍓嶄换鍔＄姸鎬侊紝璇勪及鎵ц杩涘害锛屽苟鍒跺畾涓嬩竴姝ョ瓥鐣ャ€?
                        """,
                        userInput,
                        step,
                        maxSteps,
                        executionHistory.length() > 0 ? executionHistory.toString() : "[棣栨鎵ц]",
                        currentTask
                );

                String analysisResult = taskAnalyzerClient
                        .prompt(analysisPrompt)
                        .advisors(a -> a
                                .param(CHAT_MEMORY_CONVERSATION_ID_KEY, sessionId + "-analyzer")
                                .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                        .call().content();

                parseAnalysisResult(step, analysisResult);

                // 妫€鏌ユ槸鍚﹀凡瀹屾垚
                if (analysisResult.contains("浠诲姟鐘舵€? COMPLETED") ||
                        analysisResult.contains("瀹屾垚搴﹁瘎浼? 100%")) {
                    isCompleted = true;
                    log.info("鉁?浠诲姟鍒嗘瀽鏄剧ず宸插畬鎴愶紒");
                    break;
                }

                // 绗簩闃舵锛氱簿鍑嗘墽琛?
                log.info("\n鈿?闃舵2: 绮惧噯浠诲姟鎵ц");
                String executionPrompt = String.format("""
                        **鍒嗘瀽甯堢瓥鐣?** %s
                        
                        **鎵ц鎸囦护:** 鏍规嵁涓婅堪鍒嗘瀽甯堢殑绛栫暐锛屾墽琛屽叿浣撶殑浠诲姟姝ラ銆?
                        
                        **鎵ц瑕佹眰:**
                        1. 涓ユ牸鎸夌収绛栫暐鎵ц
                        2. 浣跨敤蹇呰鐨勫伐鍏?
                        3. 纭繚鎵ц璐ㄩ噺
                        4. 璇︾粏璁板綍杩囩▼
                        """, analysisResult);

                String executionResult = precisionExecutorClient
                        .prompt(executionPrompt)
                        .advisors(a -> a
                                .param(CHAT_MEMORY_CONVERSATION_ID_KEY, sessionId + "-executor")
                                .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 120))
                        .call().content();

                parseExecutionResult(step, executionResult);

                // 绗笁闃舵锛氳川閲忕洃鐫?
                log.info("\n馃攳 闃舵3: 璐ㄩ噺鐩戠潱妫€鏌?);
                String supervisionPrompt = String.format("""
                        **鐢ㄦ埛鍘熷闇€姹?** %s
                        
                        **鎵ц缁撴灉:** %s
                        
                        **鐩戠潱瑕佹眰:** 璇疯瘎浼版墽琛岀粨鏋滅殑璐ㄩ噺锛岃瘑鍒棶棰橈紝骞舵彁渚涙敼杩涘缓璁€?
                        """, userInput, executionResult);

                String supervisionResult = qualitySupervisorClient
                        .prompt(supervisionPrompt)
                        .advisors(a -> a
                                .param(CHAT_MEMORY_CONVERSATION_ID_KEY, sessionId + "-supervisor")
                                .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 80))
                        .call().content();

                parseSupervisionResult(step, supervisionResult);

                // 鏍规嵁鐩戠潱缁撴灉鍐冲畾鏄惁闇€瑕侀噸鏂版墽琛?
                if (supervisionResult.contains("鏄惁閫氳繃: FAIL")) {
                    log.info("鉂?璐ㄩ噺妫€鏌ユ湭閫氳繃锛岄渶瑕侀噸鏂版墽琛?);
                    currentTask = "鏍规嵁璐ㄩ噺鐩戠潱鐨勫缓璁噸鏂版墽琛屼换鍔?;
                } else if (supervisionResult.contains("鏄惁閫氳繃: OPTIMIZE")) {
                    log.info("馃敡 璐ㄩ噺妫€鏌ュ缓璁紭鍖栵紝缁х画鏀硅繘");
                    currentTask = "鏍规嵁璐ㄩ噺鐩戠潱鐨勫缓璁紭鍖栨墽琛岀粨鏋?;
                } else {
                    log.info("鉁?璐ㄩ噺妫€鏌ラ€氳繃");
                }

                // 鏇存柊鎵ц鍘嗗彶
                String stepSummary = String.format("""
                        === 绗?%d 姝ュ畬鏁磋褰?===
                        銆愬垎鏋愰樁娈点€?s
                        銆愭墽琛岄樁娈点€?s
                        銆愮洃鐫ｉ樁娈点€?s
                        """, step, analysisResult, executionResult, supervisionResult);

                executionHistory.append(stepSummary);

                // 鎻愬彇涓嬩竴姝ヤ换鍔?
                currentTask = extractNextTask(analysisResult, executionResult, currentTask);

                // 娣诲姞姝ラ闂寸殑寤惰繜
                Thread.sleep(1500);

            } catch (Exception e) {
                log.error("鉂?绗?{} 姝ユ墽琛屽嚭鐜板紓甯? {}", step, e.getMessage(), e);
                executionHistory.append(String.format("\n=== 绗?%d 姝ユ墽琛屽紓甯?===\n閿欒: %s\n", step, e.getMessage()));
                currentTask = "澶勭悊涓婁竴姝ョ殑鎵ц寮傚父锛岀户缁畬鎴愬師濮嬩换鍔?;
            }
        }

        // 鎵ц缁撴灉鎬荤粨
        // 杈撳嚭鎵ц鎬荤粨
        logExecutionSummary(maxSteps, executionHistory, isCompleted);

        // 鐢熸垚鏈€缁堟€荤粨鎶ュ憡
        if (!isCompleted) {
            log.info("\n--- 鐢熸垚鏈畬鎴愪换鍔＄殑鎬荤粨鎶ュ憡 ---");
            String summaryPrompt = String.format("""
                    璇峰浠ヤ笅鏈畬鎴愮殑浠诲姟鎵ц杩囩▼杩涜鎬荤粨鍒嗘瀽锛?
                    
                    **鍘熷鐢ㄦ埛闇€姹?** %s
                    
                    **鎵ц鍘嗗彶:**
                    %s
                    
                    **鍒嗘瀽瑕佹眰:**
                    1. 鎬荤粨宸插畬鎴愮殑宸ヤ綔鍐呭
                    2. 鍒嗘瀽鏈畬鎴愮殑鍘熷洜
                    3. 鎻愬嚭瀹屾垚鍓╀綑浠诲姟鐨勫缓璁?
                    4. 璇勪及鏁翠綋鎵ц鏁堟灉
                    """, userInput, executionHistory.toString());

            String summaryResult = reactChatClient
                    .prompt(summaryPrompt)
                    .advisors(a -> a
                            .param(CHAT_MEMORY_CONVERSATION_ID_KEY, sessionId + "-summary")
                            .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 50))
                    .call().content();

            logFinalReport(summaryResult);
        }

        log.info("\n馃弫 === 鍔ㄦ€佸杞墽琛屾祴璇曠粨鏉?====");
    }

    /**
     * 瑙ｆ瀽浠诲姟鍒嗘瀽缁撴灉
     */
    private void parseAnalysisResult(int step, String analysisResult) {
        log.info("\n馃搳 === 绗?{} 姝ュ垎鏋愮粨鏋?===", step);

        String[] lines = analysisResult.split("\n");
        String currentSection = "";

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.contains("浠诲姟鐘舵€佸垎鏋?")) {
                currentSection = "status";
                log.info("\n馃幆 浠诲姟鐘舵€佸垎鏋?");
                continue;
            } else if (line.contains("鎵ц鍘嗗彶璇勪及:")) {
                currentSection = "history";
                log.info("\n馃搱 鎵ц鍘嗗彶璇勪及:");
                continue;
            } else if (line.contains("涓嬩竴姝ョ瓥鐣?")) {
                currentSection = "strategy";
                log.info("\n馃殌 涓嬩竴姝ョ瓥鐣?");
                continue;
            } else if (line.contains("瀹屾垚搴﹁瘎浼?")) {
                currentSection = "progress";
                String progress = line.substring(line.indexOf(":") + 1).trim();
                log.info("\n馃搳 瀹屾垚搴﹁瘎浼? {}", progress);
                continue;
            } else if (line.contains("浠诲姟鐘舵€?")) {
                currentSection = "task_status";
                String status = line.substring(line.indexOf(":") + 1).trim();
                if (status.equals("COMPLETED")) {
                    log.info("\n鉁?浠诲姟鐘舵€? 宸插畬鎴?);
                } else {
                    log.info("\n馃攧 浠诲姟鐘舵€? 缁х画鎵ц");
                }
                continue;
            }

            switch (currentSection) {
                case "status":
                    log.info("   馃搵 {}", line);
                    break;
                case "history":
                    log.info("   馃搳 {}", line);
                    break;
                case "strategy":
                    log.info("   馃幆 {}", line);
                    break;
                default:
                    log.info("   馃摑 {}", line);
                    break;
            }
        }
    }

    /**
     * 瑙ｆ瀽鎵ц缁撴灉
     */
    private void parseExecutionResult(int step, String executionResult) {
        log.info("\n鈿?=== 绗?{} 姝ユ墽琛岀粨鏋?===", step);

        String[] lines = executionResult.split("\n");
        String currentSection = "";

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.contains("鎵ц鐩爣:")) {
                currentSection = "target";
                log.info("\n馃幆 鎵ц鐩爣:");
                continue;
            } else if (line.contains("鎵ц杩囩▼:")) {
                currentSection = "process";
                log.info("\n馃敡 鎵ц杩囩▼:");
                continue;
            } else if (line.contains("鎵ц缁撴灉:")) {
                currentSection = "result";
                log.info("\n馃搱 鎵ц缁撴灉:");
                continue;
            } else if (line.contains("璐ㄩ噺妫€鏌?")) {
                currentSection = "quality";
                log.info("\n馃攳 璐ㄩ噺妫€鏌?");
                continue;
            }

            switch (currentSection) {
                case "target":
                    log.info("   馃幆 {}", line);
                    break;
                case "process":
                    log.info("   鈿欙笍 {}", line);
                    break;
                case "result":
                    log.info("   馃搳 {}", line);
                    break;
                case "quality":
                    log.info("   鉁?{}", line);
                    break;
                default:
                    log.info("   馃摑 {}", line);
                    break;
            }
        }
    }

    /**
     * 瑙ｆ瀽鐩戠潱缁撴灉
     */
    private void parseSupervisionResult(int step, String supervisionResult) {
        log.info("\n馃攳 === 绗?{} 姝ョ洃鐫ｇ粨鏋?===", step);

        String[] lines = supervisionResult.split("\n");
        String currentSection = "";

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.contains("璐ㄩ噺璇勪及:")) {
                currentSection = "assessment";
                log.info("\n馃搳 璐ㄩ噺璇勪及:");
                continue;
            } else if (line.contains("闂璇嗗埆:")) {
                currentSection = "issues";
                log.info("\n鈿狅笍 闂璇嗗埆:");
                continue;
            } else if (line.contains("鏀硅繘寤鸿:")) {
                currentSection = "suggestions";
                log.info("\n馃挕 鏀硅繘寤鸿:");
                continue;
            } else if (line.contains("璐ㄩ噺璇勫垎:")) {
                currentSection = "score";
                String score = line.substring(line.indexOf(":") + 1).trim();
                log.info("\n馃搳 璐ㄩ噺璇勫垎: {}", score);
                continue;
            } else if (line.contains("鏄惁閫氳繃:")) {
                currentSection = "pass";
                String status = line.substring(line.indexOf(":") + 1).trim();
                if (status.equals("PASS")) {
                    log.info("\n鉁?妫€鏌ョ粨鏋? 閫氳繃");
                } else if (status.equals("FAIL")) {
                    log.info("\n鉂?妫€鏌ョ粨鏋? 鏈€氳繃");
                } else {
                    log.info("\n馃敡 妫€鏌ョ粨鏋? 闇€瑕佷紭鍖?);
                }
                continue;
            }

            switch (currentSection) {
                case "assessment":
                    log.info("   馃搵 {}", line);
                    break;
                case "issues":
                    log.info("   鈿狅笍 {}", line);
                    break;
                case "suggestions":
                    log.info("   馃挕 {}", line);
                    break;
                default:
                    log.info("   馃摑 {}", line);
                    break;
            }
        }
    }

    /**
     * 鎻愬彇涓嬩竴姝ヤ换鍔?
     */
    private String extractNextTask(String analysisResult, String executionResult, String currentTask) {
        // 浠庡垎鏋愮粨鏋滀腑鎻愬彇涓嬩竴姝ョ瓥鐣?
        String[] analysisLines = analysisResult.split("\n");
        for (String line : analysisLines) {
            if (line.contains("涓嬩竴姝ョ瓥鐣?") && analysisLines.length > 1) {
                // 鑾峰彇绛栫暐鍐呭鐨勪笅涓€琛?
                for (int i = 0; i < analysisLines.length - 1; i++) {
                    if (analysisLines[i].contains("涓嬩竴姝ョ瓥鐣?") && !analysisLines[i + 1].trim().isEmpty()) {
                        String nextTask = analysisLines[i + 1].trim();
                        log.info("\n馃幆 涓嬩竴姝ヤ换鍔? {}", nextTask);
                        return nextTask;
                    }
                }
            }
        }

        // 濡傛灉鍒嗘瀽缁撴灉涓病鏈夋壘鍒帮紝浠庢墽琛岀粨鏋滀腑鎻愬彇
        String[] executionLines = executionResult.split("\n");
        for (String line : executionLines) {
            if (line.contains("涓嬩竴姝?) && !line.trim().isEmpty()) {
                String nextTask = line.trim();
                log.info("\n馃幆 涓嬩竴姝ヤ换鍔? {}", nextTask);
                return nextTask;
            }
        }

        // 榛樿缁х画褰撳墠浠诲姟
        log.info("\n馃攧 缁х画褰撳墠浠诲姟");
        return currentTask;
    }

    /**
     * 杈撳嚭鎵ц鎬荤粨淇℃伅
     */
    private void logExecutionSummary(int maxSteps, StringBuilder executionHistory, boolean isCompleted) {
        log.info("\n馃搳 === 鍔ㄦ€佸杞墽琛屾€荤粨 ====");

        int actualSteps = Math.min(maxSteps, executionHistory.toString().split("=== 绗?).length - 1);
        log.info("馃搱 鎬绘墽琛屾鏁? {} 姝?, actualSteps);

        if (isCompleted) {
            log.info("鉁?浠诲姟瀹屾垚鐘舵€? 宸插畬鎴?);
        } else {
            log.info("鈴革笍 浠诲姟瀹屾垚鐘舵€? 鏈畬鎴愶紙杈惧埌鏈€澶ф鏁伴檺鍒讹級");
        }

        // 璁＄畻鎵ц鏁堢巼
        double efficiency = isCompleted ? 100.0 : (double) actualSteps / maxSteps * 100;
        log.info("馃搳 鎵ц鏁堢巼: {:.1f}%", efficiency);
    }

    /**
     * 杈撳嚭鏈€缁堟€荤粨鎶ュ憡
     */
    private void logFinalReport(String summaryResult) {
        log.info("\n馃搵 === 鏈€缁堟€荤粨鎶ュ憡 ===");

        String[] lines = summaryResult.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // 鏍规嵁鍐呭绫诲瀷娣诲姞涓嶅悓鍥炬爣
            if (line.contains("宸插畬鎴?) || line.contains("瀹屾垚鐨勫伐浣?)) {
                log.info("鉁?{}", line);
            } else if (line.contains("鏈畬鎴?) || line.contains("鍘熷洜")) {
                log.info("鉂?{}", line);
            } else if (line.contains("寤鸿") || line.contains("鎺ㄨ崘")) {
                log.info("馃挕 {}", line);
            } else if (line.contains("璇勪及") || line.contains("鏁堟灉")) {
                log.info("馃搳 {}", line);
            } else {
                log.info("馃摑 {}", line);
            }
        }
    }



    //閰嶇疆澶栭儴mcp
    public McpSyncClient sseMcpClient(){
        //閰嶇疆閫氶亾
        HttpClientSseClientTransport httpClientSseClientTransport = HttpClientSseClientTransport.builder("http://appbuilder.baidu.com/v2/ai_search/mcp/")
                .sseEndpoint("sse?api_key=${BAIDU_AI_API_KEY}")
                .build();

        //寤虹珛鍚屾mcp瀹㈡埛绔?
        McpSyncClient mcpSyncClient = McpClient.sync(httpClientSseClientTransport).requestTimeout(Duration.ofMinutes(360)).build();
        //鎻℃墜
        mcpSyncClient.initialize();
        return mcpSyncClient;
    }

    //閰嶇疆鏈満mcp鍚屾瀹㈡埛绔?
    public McpSyncClient stdioMcpClient(){
        var stdioParams = ServerParameters.builder("npx")
                .args("-y", "@modelcontextprotocol/server-filesystem", "/Users/fuzhengwei/Desktop", "/Users/fuzhengwei/coding/gitcode/KnowledgePlanet/ai-agent/ai-agent-station-study/ai-agent-station-study-app")
                .build();

        McpSyncClient mcpSyncClient = McpClient.sync(new StdioClientTransport(stdioParams)).requestTimeout(Duration.ofMinutes(360)).build();
        mcpSyncClient.initialize();
        return mcpSyncClient;
    }
}

