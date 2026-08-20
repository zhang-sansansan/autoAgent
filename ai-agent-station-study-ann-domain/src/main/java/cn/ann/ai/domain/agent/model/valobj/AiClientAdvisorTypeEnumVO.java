package cn.ann.ai.domain.agent.model.valobj;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import cn.ann.ai.domain.agent.service.armory.node.factory.element.RagAnswerAdvisor;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
//顾问类型的枚举
public enum AiClientAdvisorTypeEnumVO {

    //创建枚举实例
    CHAT_MEMORY("ChatMemory","上下文记忆(内存模式)") {//调用构造函数初始化实例
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

    RAG_ANSWER("RagAnswer","知识库"){
        @Override
        public Advisor createAdvisor(AiClientAdvisorVO aiClientAdvisorVO, VectorStore vectorStore) {
            AiClientAdvisorVO.RagAnswer ragAnswer = aiClientAdvisorVO.getRagAnswer();
            return new RagAnswerAdvisor(vectorStore, SearchRequest.builder()
                    .topK(ragAnswer.getTopK())
                    .filterExpression(ragAnswer.getFilterExpression())
                    .build());
        }
    };


    //枚举类的实例的属性
    private String code;
    private String info;

    //创建一个静态的map，用于根据code快速查找到对应的枚举实例
    private static final Map<String,AiClientAdvisorTypeEnumVO>CODE_Map = new HashMap<String,AiClientAdvisorTypeEnumVO>();

    //使用静态代码快，在类加载时就将map填充
    static {
        //这个values是spring对枚举类提供的方法，存放枚举实例的初始化的值，直接调用即可
        for(AiClientAdvisorTypeEnumVO enumVo : values()){
            CODE_Map.put(enumVo.getCode(), enumVo);
        }
    }

    //参数为值对象和向量库  其中向量库用于rag知识库的检索
    public abstract Advisor createAdvisor(AiClientAdvisorVO aiClientAdvisorVO, VectorStore vectorStore);

    //静态方法，根据code去获取枚举的实例
    public static AiClientAdvisorTypeEnumVO getByCode(String code) {
        AiClientAdvisorTypeEnumVO enumVO = CODE_Map.get(code);
        if(enumVO == null){
            throw new RuntimeException("error" + code + "not exist");
        }
        return enumVO;
    }
}
