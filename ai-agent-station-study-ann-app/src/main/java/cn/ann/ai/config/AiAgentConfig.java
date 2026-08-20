package cn.ann.ai.config;


import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author zhang san
 * @description
 * @create 2026/1/15 8:37
 */
@Configuration
public class AiAgentConfig {

    @Bean("vectorStore")//pgvectorStore负责将文本内容或者对象通过模型转变为向量然后自动生成sql语句去
    // 写入参数中的jdbctemplate对应的向量数据库中或者从向量库中查询结果后通过模型之后转为java中的对象，相当于对向量数据库操作的封装
    public PgVectorStore pgVectorStore(@Value("${spring.ai.openai.base-url}") String baseUrl,
                                       @Value("${spring.ai.openai.api-key}") String apiKey,
                                       @Qualifier("pgVectorJdbcTemplate") JdbcTemplate jdbcTemplate) {
        OpenAiApi openaiApi = OpenAiApi.builder().baseUrl(baseUrl).apiKey(apiKey).build();
        OpenAiEmbeddingModel openAiEmbeddingModel = new OpenAiEmbeddingModel(openaiApi);
        return PgVectorStore.builder(jdbcTemplate, openAiEmbeddingModel)
                .vectorTableName("vector_store_openai").build();
    }

    @Bean//作用是将文本切割为小份发送给模型来转换为向量，因为ai模型的输入窗口是有限的
    public TokenTextSplitter tokenTextSplitter(){
        return new TokenTextSplitter();
    }
}
