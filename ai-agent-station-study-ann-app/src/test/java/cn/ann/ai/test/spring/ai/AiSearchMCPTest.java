package cn.ann.ai.test.spring.ai;

import com.alibaba.fastjson.JSON;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.util.List;

/**
 * @author zhang san
 * @description
 * @create 2026/1/30 14:48
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AiSearchMCPTest {

    @Test
    public void test() throws Exception {
        //鏋勫缓妯″瀷
        //妯″瀷鍖呮嫭api銆乵cp宸ュ叿
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder()
                        .baseUrl("https://apis.itedus.cn")
                        .apiKey("${OPENAI_API_KEY}互鑱旂郴灏忓倕鍝ョ敵璇?)
                        .completionsPath("v1/chat/completions")//鎷兼帴baseUrl锛屾寚瀹氳璋冪敤鐨勫叿浣撳姛鑳芥槸鑱婂ぉ锛屾瘮濡傝皟鐢╟all鏂规硶锛屽氨鏄線杩欎釜鍦板潃鍙憄ost璇锋眰
                        .embeddingsPath("v1/embeddings")//鍚戦噺鎺ュ彛鐨勮矾寰勶紝涔熸槸鎷兼帴baseUrl锛屼綔鐢ㄦ槸灏嗘枃绔犺浆涓哄悜閲忥紝浣跨敤rag鏃朵細璋冪敤锛屾瘮濡傛妸鏁版嵁瀛樺埌鍚戦噺搴?
                        .build())
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4.1")
                        .toolCallbacks(new SyncMcpToolCallbackProvider(sseMcpClient()).getToolCallbacks())
                        .build())
                .build();

        ChatResponse call = chatModel.call(Prompt.builder().messages(new UserMessage("鎼滅储灏忓倕鍝ユ妧鏈崥瀹㈡湁鍝簺椤圭洰")).build());
        log.info("娴嬭瘯缁撴灉:{}", JSON.toJSONString(call.getResult()));
    }

    private McpSyncClient sseMcpClient() {
        HttpClientSseClientTransport sseClientTransport = HttpClientSseClientTransport.builder("http://appbuilder.baidu.com/v2/ai_search/mcp/")
                .sseEndpoint("sse?api_key=${BAIDU_AI_API_KEY}")
                .build();

        McpSyncClient mcpSyncClient = McpClient.sync(sseClientTransport).requestTimeout(Duration.ofMinutes(360)).build();
        var initialize = mcpSyncClient.initialize();
        log.info("Tool SSE MCP Initialized {}", initialize);

        return mcpSyncClient;
    }


}

