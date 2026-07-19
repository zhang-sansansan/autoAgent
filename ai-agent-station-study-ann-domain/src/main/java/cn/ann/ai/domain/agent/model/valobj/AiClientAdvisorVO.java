package cn.ann.ai.domain.agent.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 椤鹃棶閰嶇疆锛屽€煎璞? *
 * @author xiaofuge bugstack.cn @灏忓倕鍝? * 2025/6/27 18:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientAdvisorVO {

    /**
     * 椤鹃棶ID
     */
    private String advisorId;

    /**
     * 椤鹃棶鍚嶇О
     */
    private String advisorName;

    /**
     * 椤鹃棶绫诲瀷(PromptChatMemory/RagAnswer/SimpleLoggerAdvisor绛?
     */
    private String advisorType;

    /**
     * 椤哄簭鍙?     */
    private Integer orderNum;

    /**
     * 鎵╁睍锛涜蹇?     */
    private ChatMemory chatMemory;

    /**
     * 鎵╁睍锛況ag 闂瓟
     */
    private RagAnswer ragAnswer;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatMemory {
        private int maxMessages;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RagAnswer {
        private int topK = 4;
        private String filterExpression;
    }

}

