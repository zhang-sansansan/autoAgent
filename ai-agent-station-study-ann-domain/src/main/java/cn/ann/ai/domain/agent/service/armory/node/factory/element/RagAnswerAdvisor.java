package cn.ann.ai.domain.agent.service.armory.node.factory.element;

import com.alibaba.fastjson.JSON;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionTextParser;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhang san
 * @description  rag鐭ヨ瘑搴撴绱㈢殑椤鹃棶
 * @create 2026/1/28 10:53
 */
public class RagAnswerAdvisor implements BaseAdvisor {

    private final VectorStore vectorStore;     //rag妫€绱㈢殑鍚戦噺搴?
    private final SearchRequest searchRequest; //鎼滅储鐨勮姹傦紝鍙互鐢ㄦ潵閰嶇疆璇锋眰鐨勪竴浜涘弬鏁?
    private final String userTextAdvise;      //鎻愮ず璇嶆ā鏉?

    public RagAnswerAdvisor(VectorStore vectorStore, SearchRequest searchRequest) {
        this.vectorStore = vectorStore;
        this.searchRequest = searchRequest;
        this.userTextAdvise = "\nContext information is below, surrounded by ---------------------\n\n---------------------\n{question_answer_context}\n---------------------\n\nGiven the context and provided history information and not prior knowledge,\nreply to the user comment. If the answer is not in the context, inform\nthe user that you can't answer the question.\n";
    }


    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        HashMap<String,Object>context =new HashMap<>(chatClientRequest.context());//灏嗕笂涓嬫枃瀵硅薄鐨勬暟鎹嫹璐? 閬垮厤淇敼涓婁笅鏂?

        String userText = chatClientRequest.prompt().getUserMessage().getText();//鑾峰彇鐢ㄦ埛璇锋眰鐨勯棶棰?
        String advisedUserText = userText + System.lineSeparator() + userTextAdvise;

        String query = (new PromptTemplate(userText)).render();//spring鐨勮鑼?

        //鎸囧畾妫€绱㈡爣鍑嗕箣鍚庡埌鍚戦噺搴撲腑鍘绘绱㈡暟鎹嵆鍙?
        SearchRequest searchRequestToUse = SearchRequest.from(this.searchRequest).query(query).filterExpression(this.doGetFilterExpression(context)).build();//閰嶇疆鍦ㄧ煡璇嗗簱妫€绱㈢殑鏍囧噯
        List<Document> documents = this.vectorStore.similaritySearch(searchRequestToUse);
        context.put("qa_retrieved_documents", documents);

        //document鍜宒ocumentContext鐨勫尯鍒細document鏄粠鍚戦噺搴撲腑鏌ュ埌鐨勬暟鎹紝鍖呮嫭鏂囨湰鏁版嵁鍜屽厓鏁版嵁绛夛紝
        // 鑰宒ocumentContext鏄痙ocument涓殑绾枃鏈嫾鎺ヨ€屾垚鐨勫瓧绗︿覆锛岀敤浜庡杺缁檒lm澶фā鍨?
        String documentContext = documents.stream().map(Document::getText).collect(Collectors.joining(System.lineSeparator()));
        Map<String, Object> advisedUserParams = new HashMap(chatClientRequest.context());
        advisedUserParams.put("question_answer_context", documentContext);
        advisedUserParams.put("qa_retrieved_documents", documents);

        //灏嗙粡杩噐ag鐭ヨ瘑搴撳寮虹殑client璇锋眰鍖呰鍚庤繑鍥?
        return ChatClientRequest.builder()
                .prompt(Prompt.builder().messages(new UserMessage(advisedUserText), new AssistantMessage(JSON.toJSONString(advisedUserParams))).build())
                .context(advisedUserParams)//鏇挎崲涓烘柊鐨勪笂涓嬫枃鏁版嵁
                .build();
    }


    //鏍规嵁澶嶅埗鐨勪笂涓嬮棶鐨勬暟鎹紝鐪嬫湁娌℃湁涓撻棬閰嶇疆rag妫€绱㈢殑杩囨护鏂瑰悜锛屾瘮濡傝key涓簈a_filter_expression锛寁alue涓簎serid=1001锛屽垯妫€绱㈢煡璇嗗簱鏃跺彧浼氭悳绱serid=1001鐨勬暟鎹紝瀹炵幇绮惧噯妫€绱?
    protected Filter.Expression doGetFilterExpression(HashMap<String, Object> context) {
        return context.containsKey("qa_filter_expression") && StringUtils.hasText(context.get("qa_filter_expression").toString()) ? (new FilterExpressionTextParser()).parse(context.get("qa_filter_expression").toString()) : this.searchRequest.getFilterExpression();
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        ChatResponse.Builder chatResponseBuilder = ChatResponse.builder().from(chatClientResponse.chatResponse());
        chatResponseBuilder.metadata("qa_retrieved_documents", chatClientResponse.context().get("qa_retrieved_documents"));

        return chatClientResponse.builder().chatResponse(chatResponseBuilder.build())
                .context(chatClientResponse.context()).build();
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        //鍚屾鐨勬ā鍨嬭繑鍥為€昏緫  鎷︽埅鐨勬祦绋? before->llm->after
        ChatClientResponse chatClientResponse =callAdvisorChain.nextCall(this.before(chatClientRequest,callAdvisorChain));
        return this.after(chatClientResponse,callAdvisorChain);
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        return BaseAdvisor.super.adviseStream(chatClientRequest, streamAdvisorChain);
    }
}

