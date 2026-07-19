package cn.ann.ai.domain.agent.model.valobj.enums;


import cn.ann.ai.domain.agent.model.valobj.AiClientAdvisorVO;
import cn.ann.ai.domain.agent.service.armory.node.factory.element.RagAnswerAdvisor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.HashMap;
import java.util.Map;

/**
 * 椤鹃棶绫诲瀷鏋氫妇
 *
 * @author xiaofuge bugstack.cn @灏忓倕鍝? * 2025/7/19 09:02
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AiClientAdvisorTypeEnumVO {

    CHAT_MEMORY("ChatMemory", "涓婁笅鏂囪蹇嗭紙鍐呭瓨妯″紡锛?) {
        @Override
        public Advisor createAdvisor(AiClientAdvisorVO aiClientAdvisorVO, VectorStore vectorStore) {
            AiClientAdvisorVO.ChatMemory chatMemory = aiClientAdvisorVO.getChatMemory();
            return PromptChatMemoryAdvisor.builder(
                    MessageWindowChatMemory.builder()
                            .maxMessages(chatMemory.getMaxMessages())
                            .build()
            ).build();
        }
    },
    
    RAG_ANSWER("RagAnswer", "鐭ヨ瘑搴?) {
        @Override
        public Advisor createAdvisor(AiClientAdvisorVO aiClientAdvisorVO, VectorStore vectorStore) {
            AiClientAdvisorVO.RagAnswer ragAnswer = aiClientAdvisorVO.getRagAnswer();
            return new RagAnswerAdvisor(vectorStore, SearchRequest.builder()
                    .topK(ragAnswer.getTopK())
                    .filterExpression(ragAnswer.getFilterExpression())
                    .build());
        }
    }
    
    ;

    private String code;
    private String info;
    
    // 闈欐€丮ap缂撳瓨锛岀敤浜庡揩閫熸煡鎵?    private static final Map<String, AiClientAdvisorTypeEnumVO> CODE_MAP = new HashMap<>();
    
    // 闈欐€佸垵濮嬪寲鍧楋紝鍦ㄧ被鍔犺浇鏃跺垵濮嬪寲Map
    static {
        for (AiClientAdvisorTypeEnumVO enumVO : values()) {
            CODE_MAP.put(enumVO.getCode(), enumVO);
        }
    }
    
    /**
     * 绛栫暐鏂规硶锛氬垱寤洪【闂璞?     * @param aiClientAdvisorVO 椤鹃棶閰嶇疆瀵硅薄
     * @param vectorStore 鍚戦噺瀛樺偍
     * @return 椤鹃棶瀵硅薄
     */
    public abstract Advisor createAdvisor(AiClientAdvisorVO aiClientAdvisorVO, VectorStore vectorStore);
    
    /**
     * 鏍规嵁code鑾峰彇鏋氫妇
     * @param code 缂栫爜
     * @return 鏋氫妇瀵硅薄
     */
    public static AiClientAdvisorTypeEnumVO getByCode(String code) {
        AiClientAdvisorTypeEnumVO enumVO = CODE_MAP.get(code);
        if (enumVO == null) {
            throw new RuntimeException("err! advisorType " + code + " not exist!");
        }
        return enumVO;
    }

}

