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
 * @description  rag知识库检索的顾问
 * @create 2026/1/28 10:53
 */
public class RagAnswerAdvisor implements BaseAdvisor {

    private final VectorStore vectorStore;     //rag检索的向量库
    private final SearchRequest searchRequest; //搜索的请求，可以用来配置请求的一些参数
    private final String userTextAdvise;      //提示词模板

    public RagAnswerAdvisor(VectorStore vectorStore, SearchRequest searchRequest) {
        this.vectorStore = vectorStore;
        this.searchRequest = searchRequest;
        this.userTextAdvise = "\nContext information is below, surrounded by ---------------------\n\n---------------------\n{question_answer_context}\n---------------------\n\nGiven the context and provided history information and not prior knowledge,\nreply to the user comment. If the answer is not in the context, inform\nthe user that you can't answer the question.\n";
    }


    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        HashMap<String,Object>context =new HashMap<>(chatClientRequest.context());//将上下文对象的数据拷贝  避免修改上下文

        String userText = chatClientRequest.prompt().getUserMessage().getText();//获取用户请求的问题
        String advisedUserText = userText + System.lineSeparator() + userTextAdvise;

        String query = (new PromptTemplate(userText)).render();//spring的规范

        //指定检索标准之后到向量库中去检索数据即可
        SearchRequest searchRequestToUse = SearchRequest.from(this.searchRequest).query(query).filterExpression(this.doGetFilterExpression(context)).build();//配置在知识库检索的标准
        List<Document> documents = this.vectorStore.similaritySearch(searchRequestToUse);
        context.put("qa_retrieved_documents", documents);

        //document和documentContext的区别：document是从向量库中查到的数据，包括文本数据和元数据等，
        // 而documentContext是document中的纯文本拼接而成的字符串，用于喂给llm大模型
        String documentContext = documents.stream().map(Document::getText).collect(Collectors.joining(System.lineSeparator()));
        Map<String, Object> advisedUserParams = new HashMap(chatClientRequest.context());
        advisedUserParams.put("question_answer_context", documentContext);
        advisedUserParams.put("qa_retrieved_documents", documents);

        //将经过rag知识库增强的client请求包装后返回
        return ChatClientRequest.builder()
                .prompt(Prompt.builder().messages(new UserMessage(advisedUserText), new AssistantMessage(JSON.toJSONString(advisedUserParams))).build())
                .context(advisedUserParams)//替换为新的上下文数据
                .build();
    }


    //根据复制的上下问的数据，看有没有专门配置rag检索的过滤方向，比如说key为qa_filter_expression，value为userid=1001，则检索知识库时只会搜索userid=1001的数据，实现精准检索
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
        //同步的模型返回逻辑  拦截的流程  before->llm->after
        ChatClientResponse chatClientResponse =callAdvisorChain.nextCall(this.before(chatClientRequest,callAdvisorChain));
        return this.after(chatClientResponse,callAdvisorChain);
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        return BaseAdvisor.super.adviseStream(chatClientRequest, streamAdvisorChain);
    }
}
