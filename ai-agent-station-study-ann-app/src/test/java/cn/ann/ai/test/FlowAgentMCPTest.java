package cn.ann.ai.test;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@springboottest
public class FlowAgentMCPTest {

        @Test
        public void test() {
            OpenAiChatModel chatModel = OpenAiChatModel.builder()
                    .openAiApi(OpenAiApi.builder()
                            .baseUrl("https://apis.itedus.cn")
                        .apiKey("sk-uzT8fdbgqb6SIZQF69469bFdFcD34a618bAd2d8e3d9a06E7")
                            .completionsPath("v1/chat/completions")
                            .embeddingsPath("v1/embeddings")
                            .build())
                    .defaultOptions(OpenAiChatOptions.builder()
                            .model("gpt-4.1")
                        .toolCallbacks(new SyncMcpToolCallbackProvider(stdioMcpClientElasticsearch()).getToolCallbacks())
                            .build())
                    .build();
            ChatResponse call = chatModel.call(Prompt.builder().messages(new UserMessage("有哪些工具可以使用")).build());
            log.info("测试结果:{}", JSON.toJSONString(call.getResult().getOutput().getText()));
        }
                                    
        /**
         * https://github.com/awesimon/elasticsearch-mcp
         * https://www.npmjs.com/package/@awesome-ai/elasticsearch-mcp
         * npm i @awesome-ai/elasticsearch-mcp
         */
        public McpSyncClient stdioMcpClientElasticsearch() {
            Map<String, String> env = new HashMap<>();
            env.put("ES_HOST", "http://127.0.0.1:9200");
            env.put("ES_API_KEY", "none");
            var stdioParams = ServerParameters.builder("npx.cmd")
                    .args("-y", "@awesome-ai/elasticsearch-mcp")
                    .env(env)
                    .build();
            var mcpClient = McpClient.sync(new StdioClientTransport(stdioParams))
                    .requestTimeout(Duration.ofSeconds(100)).build();
            var init = mcpClient.initialize();
            System.out.println("Stdio MCP Initialized: " + init);
            return mcpClient;
        }
}