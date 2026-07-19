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
//椤鹃棶绫诲瀷鐨勬灇涓?
public enum AiClientAdvisorTypeEnumVO {

    //鍒涘缓鏋氫妇瀹炰緥
    CHAT_MEMORY("chatMemory","涓婁笅鏂囪蹇?鍐呭瓨妯″紡)") {//璋冪敤鏋勯€犲嚱鏁板垵濮嬪寲瀹炰緥
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

    RAG_ANSWER("RagAnswer","鐭ヨ瘑搴?){
        @Override
        public Advisor createAdvisor(AiClientAdvisorVO aiClientAdvisorVO, VectorStore vectorStore) {
            AiClientAdvisorVO.RagAnswer ragAnswer = aiClientAdvisorVO.getRagAnswer();
            return new RagAnswerAdvisor(vectorStore, SearchRequest.builder()
                    .topK(ragAnswer.getTopK())
                    .filterExpression(ragAnswer.getFilterExpression())
                    .build());
        }
    };


    //鏋氫妇绫荤殑瀹炰緥鐨勫睘鎬?
    private String code;
    private String info;

    //鍒涘缓涓€涓潤鎬佺殑map锛岀敤浜庢牴鎹甤ode蹇€熸煡鎵惧埌瀵瑰簲鐨勬灇涓惧疄渚?
    private static final Map<String,AiClientAdvisorTypeEnumVO>CODE_Map = new HashMap<String,AiClientAdvisorTypeEnumVO>();

    //浣跨敤闈欐€佷唬鐮佸揩锛屽湪绫诲姞杞芥椂灏卞皢map濉厖
    static {
        //杩欎釜values鏄痵pring瀵规灇涓剧被鎻愪緵鐨勬柟娉曪紝瀛樻斁鏋氫妇瀹炰緥鐨勫垵濮嬪寲鐨勫€硷紝鐩存帴璋冪敤鍗冲彲
        for(AiClientAdvisorTypeEnumVO enumVo : values()){
            CODE_Map.put(enumVo.getCode(), enumVo);
        }
    }

    //鍙傛暟涓哄€煎璞″拰鍚戦噺搴? 鍏朵腑鍚戦噺搴撶敤浜巖ag鐭ヨ瘑搴撶殑妫€绱?
    public abstract Advisor createAdvisor(AiClientAdvisorVO aiClientAdvisorVO, VectorStore vectorStore);

    //闈欐€佹柟娉曪紝鏍规嵁code鍘昏幏鍙栨灇涓剧殑瀹炰緥
    public static AiClientAdvisorTypeEnumVO getByCode(String code) {
        AiClientAdvisorTypeEnumVO enumVO = CODE_Map.get(code);
        if(enumVO == null){
            throw new RuntimeException("error" + code + "not exist");
        }
        return enumVO;
    }
}

