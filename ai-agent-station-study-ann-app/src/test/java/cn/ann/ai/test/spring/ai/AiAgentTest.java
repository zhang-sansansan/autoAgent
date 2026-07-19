package cn.ann.ai.test.spring.ai;


import cn.ann.ai.test.spring.ai.advisors.RagAnswerAdvisor;
import com.alibaba.fastjson.JSON;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AiAgentTest {

    private ChatModel chatModel;

    private ChatClient chatClient;

    @Resource
    private PgVectorStore vectorStore;

    public static final String CHAT_MEMORY_CONVERSATION_ID_KEY = "chat_memory_conversation_id";
    public static final String CHAT_MEMORY_RETRIEVE_SIZE_KEY = "chat_memory_response_size";

    @Before
    public void init() {

        OpenAiApi openAiApi = OpenAiApi.builder()
                .baseUrl("https://apis.itedus.cn")
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .completionsPath("v1/chat/completions")
                .embeddingsPath("v1/embeddings")
                .build();

        chatModel = OpenAiChatModel.builder()//鏅鸿兘浣撴€濊€冪殑澶磋剳
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4.1-mini")
                        .toolCallbacks(new SyncMcpToolCallbackProvider(stdioMcpClient(), sseMcpClient01(), sseMcpClient02()).getToolCallbacks())
                        .build())
                .build();

        chatClient = ChatClient.builder(chatModel)//鏅鸿兘浣撶殑韬共
                //浜鸿
                .defaultSystem("""
                        	 浣犳槸涓€涓?AI Agent 鏅鸿兘浣擄紝鍙互鏍规嵁鐢ㄦ埛杈撳叆淇℃伅鐢熸垚鏂囩珷锛屽苟鍙戦€佸埌 CSDN 骞冲彴浠ュ強瀹屾垚寰俊鍏紬鍙锋秷鎭€氱煡锛屼粖澶╂槸 {current_date}銆?                        
                        	 浣犳搮闀夸娇鐢≒lanning妯″紡锛屽府鍔╃敤鎴风敓鎴愯川閲忔洿楂樼殑鏂囩珷銆?                        
                        	 浣犵殑瑙勫垝搴旇鍖呮嫭浠ヤ笅鍑犱釜鏂归潰锛?                        	 1. 鍒嗘瀽鐢ㄦ埛杈撳叆鐨勫唴瀹癸紝鐢熸垚鎶€鏈枃绔犮€?                        	 2. 鎻愬彇锛屾枃绔犳爣棰橈紙闇€瑕佸惈甯︽妧鏈偣锛夈€佹枃绔犲唴瀹广€佹枃绔犳爣绛撅紙澶氫釜鐢ㄨ嫳鏂囬€楀彿闅斿紑锛夈€佹枃绔犵畝杩帮紙100瀛楋級灏嗕互涓婂唴瀹瑰彂甯冩枃绔犲埌CSDN
                        	 3. 鑾峰彇鍙戦€佸埌 CSDN 鏂囩珷鐨?URL 鍦板潃銆?                        	 4. 寰俊鍏紬鍙锋秷鎭€氱煡锛屽钩鍙帮細CSDN銆佷富棰橈細涓烘枃绔犳爣棰樸€佹弿杩帮細涓烘枃绔犵畝杩般€佽烦杞湴鍧€锛氫粠鍙戝竷鏂囩珷鍒癈SDN鑾峰彇 URL 鍦板潃
                        """)
//                .defaultToolCallbacks(new SyncMcpToolCallbackProvider(stdioMcpClient(), sseMcpClient01(), sseMcpClient02()).getToolCallbacks())
                //瀵硅瘽璁板繂鍜宺ag鐭ヨ瘑搴撳姛鑳?                .defaultAdvisors(
                        PromptChatMemoryAdvisor.builder(
                                MessageWindowChatMemory.builder()
                                        .maxMessages(100)
                                        .build()
                        ).build(),
                        new RagAnswerAdvisor(vectorStore, SearchRequest.builder()
                                .topK(5)
                                .filterExpression("knowledge == 'article-prompt-words'")
                                .build()),
                        SimpleLoggerAdvisor.builder().build())
                .build();
    }

    @Test
    public void test_chat_model_stream_01() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Prompt prompt = Prompt.builder()
                .messages(new UserMessage(
                        """
                                鏈夊摢浜涘伐鍏峰彲浠ヤ娇鐢?                                """))
                .build();

        // 闈炴祦寮忥紝chatModel.call(prompt)
        Flux<ChatResponse> stream = chatModel.stream(prompt);

        stream.subscribe(
                chatResponse -> {
                    AssistantMessage output = chatResponse.getResult().getOutput();
                    log.info("娴嬭瘯缁撴灉: {}", JSON.toJSONString(output));
                },
                Throwable::printStackTrace,
                () -> {
                    countDownLatch.countDown();
                    System.out.println("Stream completed");
                }
        );

        countDownLatch.await();
    }

    @Test
    public void test_chat_model_call() {
        Prompt prompt = Prompt.builder()
                .messages(new UserMessage(
                        """
                                鏈夊摢浜涘伐鍏峰彲浠ヤ娇鐢?                                """))
                .build();

        ChatResponse chatResponse = chatModel.call(prompt);

        log.info("娴嬭瘯缁撴灉(call):{}", JSON.toJSONString(chatResponse));
    }

    @Test
    public void test_02() {
        String userInput = "鐜嬪ぇ鐡滀粖骞村嚑宀?;
        System.out.println("\n>>> QUESTION: " + userInput);
        System.out.println("\n>>> ASSISTANT: " + chatClient
                .prompt(userInput)
                .system(s -> s.param("current_date", LocalDate.now().toString()))
                .call().content());
    }

    @Test
    public void test_client03() {
        ChatClient chatClient01 = ChatClient.builder(chatModel)
                .defaultSystem("""
                        浣犳槸涓€涓笓涓氱殑AI鎻愮ず璇嶄紭鍖栦笓瀹躲€傝甯垜浼樺寲浠ヤ笅prompt锛屽苟鎸夌収浠ヤ笅鏍煎紡杩斿洖锛?                        
                        # Role: [瑙掕壊鍚嶇О]
                        
                        ## Profile
                        - language: [璇█]
                        - description: [璇︾粏鐨勮鑹叉弿杩癩
                        - background: [瑙掕壊鑳屾櫙]
                        - personality: [鎬ф牸鐗瑰緛]
                        - expertise: [涓撲笟棰嗗煙]
                        - target_audience: [鐩爣鐢ㄦ埛缇
                        
                        ## Skills
                        
                        1. [鏍稿績鎶€鑳界被鍒玗
                           - [鍏蜂綋鎶€鑳絔: [绠€瑕佽鏄嶿
                           - [鍏蜂綋鎶€鑳絔: [绠€瑕佽鏄嶿
                           - [鍏蜂綋鎶€鑳絔: [绠€瑕佽鏄嶿
                           - [鍏蜂綋鎶€鑳絔: [绠€瑕佽鏄嶿
                        
                        2. [杈呭姪鎶€鑳界被鍒玗
                           - [鍏蜂綋鎶€鑳絔: [绠€瑕佽鏄嶿
                           - [鍏蜂綋鎶€鑳絔: [绠€瑕佽鏄嶿
                           - [鍏蜂綋鎶€鑳絔: [绠€瑕佽鏄嶿
                           - [鍏蜂綋鎶€鑳絔: [绠€瑕佽鏄嶿
                        
                        ## Rules
                        
                        1. [鍩烘湰鍘熷垯]锛?                           - [鍏蜂綋瑙勫垯]: [璇︾粏璇存槑]
                           - [鍏蜂綋瑙勫垯]: [璇︾粏璇存槑]
                           - [鍏蜂綋瑙勫垯]: [璇︾粏璇存槑]
                           - [鍏蜂綋瑙勫垯]: [璇︾粏璇存槑]
                        
                        2. [琛屼负鍑嗗垯]锛?                           - [鍏蜂綋瑙勫垯]: [璇︾粏璇存槑]
                           - [鍏蜂綋瑙勫垯]: [璇︾粏璇存槑]
                           - [鍏蜂綋瑙勫垯]: [璇︾粏璇存槑]
                           - [鍏蜂綋瑙勫垯]: [璇︾粏璇存槑]
                        
                        3. [闄愬埗鏉′欢]锛?                           - [鍏蜂綋闄愬埗]: [璇︾粏璇存槑]
                           - [鍏蜂綋闄愬埗]: [璇︾粏璇存槑]
                           - [鍏蜂綋闄愬埗]: [璇︾粏璇存槑]
                           - [鍏蜂綋闄愬埗]: [璇︾粏璇存槑]
                        
                        ## Workflows
                        
                        - 鐩爣: [鏄庣‘鐩爣]
                        - 姝ラ 1: [璇︾粏璇存槑]
                        - 姝ラ 2: [璇︾粏璇存槑]
                        - 姝ラ 3: [璇︾粏璇存槑]
                        - 棰勬湡缁撴灉: [璇存槑]
                        
                        
                        ## Initialization
                        浣滀负[瑙掕壊鍚嶇О]锛屼綘蹇呴』閬靛畧涓婅堪Rules锛屾寜鐓orkflows鎵ц浠诲姟銆?                        
                        璇峰熀浜庝互涓婃ā鏉匡紝浼樺寲骞舵墿灞曚互涓媝rompt锛岀‘淇濆唴瀹逛笓涓氥€佸畬鏁翠笖缁撴瀯娓呮櫚锛屾敞鎰忎笉瑕佹惡甯︿换浣曞紩瀵艰瘝鎴栬В閲婏紝涓嶈浣跨敤浠ｇ爜鍧楀寘鍥淬€?                        """)
                .defaultAdvisors(
                        PromptChatMemoryAdvisor.builder(
                                MessageWindowChatMemory.builder()
                                        .maxMessages(100)
                                        .build()
                        ).build(),
                        new RagAnswerAdvisor(vectorStore, SearchRequest.builder()
                                .topK(5)
                                .filterExpression("knowledge == 'article-prompt-words'")
                                .build())
                )
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4.1")
                        .build())
                .build();

        String content = chatClient01
                .prompt("鐢熸垚涓€绡囨枃绔?)
                .system(s -> s.param("current_date", LocalDate.now().toString()))
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, "chatId-101")
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .call().content();

        System.out.println("\n>>> ASSISTANT: " + content);

        ChatClient chatClient02 = ChatClient.builder(chatModel)
                .defaultSystem("""
                        	 浣犳槸涓€涓?AI Agent 鏅鸿兘浣擄紝鍙互鏍规嵁鐢ㄦ埛杈撳叆淇℃伅鐢熸垚鏂囩珷锛屽苟鍙戦€佸埌 CSDN 骞冲彴浠ュ強瀹屾垚寰俊鍏紬鍙锋秷鎭€氱煡锛屼粖澶╂槸 {current_date}銆?                        
                        	 浣犳搮闀夸娇鐢≒lanning妯″紡锛屽府鍔╃敤鎴风敓鎴愯川閲忔洿楂樼殑鏂囩珷銆?                        
                        	 浣犵殑瑙勫垝搴旇鍖呮嫭浠ヤ笅鍑犱釜鏂归潰锛?                        	 1. 鍒嗘瀽鐢ㄦ埛杈撳叆鐨勫唴瀹癸紝鐢熸垚鎶€鏈枃绔犮€?                        	 2. 鎻愬彇锛屾枃绔犳爣棰橈紙闇€瑕佸惈甯︽妧鏈偣锛夈€佹枃绔犲唴瀹广€佹枃绔犳爣绛撅紙澶氫釜鐢ㄨ嫳鏂囬€楀彿闅斿紑锛夈€佹枃绔犵畝杩帮紙100瀛楋級灏嗕互涓婂唴瀹瑰彂甯冩枃绔犲埌CSDN
                        	 3. 鑾峰彇鍙戦€佸埌 CSDN 鏂囩珷鐨?URL 鍦板潃銆?                        	 4. 寰俊鍏紬鍙锋秷鎭€氱煡锛屽钩鍙帮細CSDN銆佷富棰橈細涓烘枃绔犳爣棰樸€佹弿杩帮細涓烘枃绔犵畝杩般€佽烦杞湴鍧€锛氫负鍙戝竷鏂囩珷鍒癈SDN鑾峰彇 URL鍦板潃 CSDN鏂囩珷閾炬帴 https 寮€澶寸殑鍦板潃銆?                        """)
//                .defaultTools(new SyncMcpToolCallbackProvider(sseMcpClient01(), sseMcpClient02()))
                .defaultAdvisors(
                        PromptChatMemoryAdvisor.builder(
                                MessageWindowChatMemory.builder()
                                        .maxMessages(100)
                                        .build()
                        ).build(),
                        new SimpleLoggerAdvisor()
                )
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4.1")
                        .build())
                .build();

        String userInput = "鐢熸垚涓€绡囨枃绔狅紝瑕佹眰濡備笅 \r\n" + content;
        System.out.println("\n>>> QUESTION: " + userInput);
        System.out.println("\n>>> ASSISTANT: " + chatClient02
                .prompt(userInput)
                .system(s -> s.param("current_date", LocalDate.now().toString()))
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, "chatId-101")
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .call().content());
    }

    public McpSyncClient stdioMcpClient() {//鏈湴鐩磋繛妯″紡

        // based on
        // https://github.com/modelcontextprotocol/servers/tree/main/src/filesystem
        //鏋勫缓涓€涓惎鍔ㄥ弬鏁?        var stdioParams = ServerParameters.builder("npx")
                .args("-y", "@modelcontextprotocol/server-filesystem", "D:/妗岄潰/鏂囨。/鏂囨。鐩?aiworkspace", "D:/妗岄潰/鏂囨。/鏂囨。鐩?aiworkspace")
                .build();

        //鍒涘缓瀹㈡埛绔?   StdioClientTransport琛ㄧず閫氳繃鈥滄爣鍑嗚緭鍏ヨ緭鍑烘祦鈥濋€氫俊锛岃€屼笉鏄綉缁滅鍙?        var mcpClient = McpClient.sync(new StdioClientTransport(stdioParams))
                .requestTimeout(Duration.ofSeconds(10)).build();

        //鎻℃墜鍒濆鍖?        var init = mcpClient.initialize();

        System.out.println("Stdio MCP Initialized: " + init);

        return mcpClient;

    }

    public McpSyncClient sseMcpClient01() {//杩滅▼缃戠粶妯″紡

        //HttpClientSseClientTransport閫氳繃 HTTP鍗忚鐨?SSE (Server-Sent Events) 鎶€鏈€氫俊
        HttpClientSseClientTransport sseClientTransport = HttpClientSseClientTransport.builder("http://127.0.0.1:8102").build();

        //鍒涘缓瀹㈡埛绔?        McpSyncClient mcpSyncClient = McpClient.sync(sseClientTransport).requestTimeout(Duration.ofMinutes(180)).build();

        //鎻℃墜鍒濆鍖?        var init = mcpSyncClient.initialize();
        System.out.println("SSE MCP Initialized: " + init);

        return mcpSyncClient;
    }

    public McpSyncClient sseMcpClient02() {

        HttpClientSseClientTransport sseClientTransport = HttpClientSseClientTransport.builder("http://127.0.0.1:8101").build();

        McpSyncClient mcpSyncClient = McpClient.sync(sseClientTransport).requestTimeout(Duration.ofMinutes(180)).build();

        var init = mcpSyncClient.initialize();
        System.out.println("SSE MCP Initialized: " + init);

        return mcpSyncClient;
    }

}

