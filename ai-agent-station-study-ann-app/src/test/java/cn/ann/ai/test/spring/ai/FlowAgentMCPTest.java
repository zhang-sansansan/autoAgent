package cn.ann.ai.test.spring.ai;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhang san
 * @description
 * @create 2026/2/4 18:03
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class FlowAgentMCPTest {

    @Test
    public void test() {
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder()
                        .baseUrl("https://apis.itedus.cn")
                        .apiKey(System.getenv("OPENAI_API_KEY"))
                        .completionsPath("v1/chat/completions")
                        .embeddingsPath("v1/embeddings")
                        .build())
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4.1")
                        .toolCallbacks(new SyncMcpToolCallbackProvider(stdioMcpClientElasticsearch()).getToolCallbacks())
                        .build())
                .build();


    }

    public McpSyncClient stdioMcpClientElasticsearch(){
        //閰嶇疆涓€涓嬭繍琛岀殑鐜
        Map<String, String> env = new HashMap<>();
        env.put("ES_HOST", "http://192.168.1.110:9200");
        env.put("ES_API_KEY", "none");
        var stdioParams = ServerParameters.builder("npx")
                .env(env)
                .args("-y", "@awesome-ai/elasticsearch-mcp")
                .build();

        var mcpSyncClient = McpClient.sync(new StdioClientTransport(stdioParams)).requestTimeout(Duration.ofMinutes(100)).build();
        var init = mcpSyncClient.initialize();
        return mcpSyncClient;


    }

}

