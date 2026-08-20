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

    private static String requireEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required environment variable: " + name);
        }
        return value;
    }


    @Test
    public void test() throws Exception {
        //构建模型
        //模型包括api、mcp工具
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder()
                        .baseUrl("https://apis.itedus.cn")
                        .apiKey("sk-sLvFUs1wSIgtbWcE03464f199d25****可以联系小傅哥申请")
                        .completionsPath("v1/chat/completions")//拼接baseUrl，指定要调用的具体功能是聊天，比如调用call方法，就是往这个地址发post请求
                        .embeddingsPath("v1/embeddings")//向量接口的路径，也是拼接baseUrl，作用是将文章转为向量，使用rag时会调用，比如把数据存到向量库
                        .build())
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4.1")
                        .toolCallbacks(new SyncMcpToolCallbackProvider(sseMcpClient()).getToolCallbacks())
                        .build())
                .build();

        ChatResponse call = chatModel.call(Prompt.builder().messages(new UserMessage("搜索小傅哥技术博客有哪些项目")).build());
        log.info("测试结果:{}", JSON.toJSONString(call.getResult()));
    }

    private McpSyncClient sseMcpClient() {
        HttpClientSseClientTransport sseClientTransport = HttpClientSseClientTransport.builder("http://appbuilder.baidu.com/v2/ai_search/mcp/")
                .sseEndpoint("sse?api_key=" + requireEnvironment("BAIDU_AI_API_KEY"))
                .build();

        McpSyncClient mcpSyncClient = McpClient.sync(sseClientTransport).requestTimeout(Duration.ofMinutes(360)).build();
        var initialize = mcpSyncClient.initialize();
        log.info("Tool SSE MCP Initialized {}", initialize);

        return mcpSyncClient;
    }


}
