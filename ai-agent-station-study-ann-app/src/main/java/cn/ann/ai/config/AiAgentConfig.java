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

    @Bean("vectorStore")//pgvectorStore璐熻矗灏嗘枃鏈唴瀹规垨鑰呭璞￠€氳繃妯″瀷杞彉涓哄悜閲忕劧鍚庤嚜鍔ㄧ敓鎴恠ql璇彞鍘?
    // 鍐欏叆鍙傛暟涓殑jdbctemplate瀵瑰簲鐨勫悜閲忔暟鎹簱涓垨鑰呬粠鍚戦噺搴撲腑鏌ヨ缁撴灉鍚庨€氳繃妯″瀷涔嬪悗杞负java涓殑瀵硅薄锛岀浉褰撲簬瀵瑰悜閲忔暟鎹簱鎿嶄綔鐨勫皝瑁?
    public PgVectorStore pgVectorStore(@Value("${spring.ai.openai.base-url}") String baseUrl,
                                       @Value("${spring.ai.openai.api-key}") String apiKey,
                                       @Qualifier("pgVectorJdbcTemplate") JdbcTemplate jdbcTemplate) {
        OpenAiApi openaiApi = OpenAiApi.builder().baseUrl(baseUrl).apiKey(apiKey).build();
        OpenAiEmbeddingModel openAiEmbeddingModel = new OpenAiEmbeddingModel(openaiApi);
        return PgVectorStore.builder(jdbcTemplate, openAiEmbeddingModel)
                .vectorTableName("vector_store_openai").build();
    }

    @Bean//浣滅敤鏄皢鏂囨湰鍒囧壊涓哄皬浠藉彂閫佺粰妯″瀷鏉ヨ浆鎹负鍚戦噺锛屽洜涓篴i妯″瀷鐨勮緭鍏ョ獥鍙ｆ槸鏈夐檺鐨?
    public TokenTextSplitter tokenTextSplitter(){
        return new TokenTextSplitter();
    }
}

