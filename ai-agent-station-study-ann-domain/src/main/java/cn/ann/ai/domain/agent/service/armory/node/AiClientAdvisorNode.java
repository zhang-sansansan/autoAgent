package cn.ann.ai.domain.agent.service.armory.node;

import cn.ann.ai.domain.agent.model.entity.ArmoryCommandEntity;
import cn.ann.ai.domain.agent.model.valobj.AiAgentEnumVO;
import cn.ann.ai.domain.agent.model.valobj.AiClientAdvisorTypeEnumVO;
import cn.ann.ai.domain.agent.model.valobj.AiClientAdvisorVO;
import cn.ann.ai.domain.agent.service.armory.node.factory.DefaultArmoryStrategyFactory;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhang san
 * @description
 * @create 2026/1/29 9:16
 */
@Slf4j
@Service
public class AiClientAdvisorNode extends AbstractArmorySupport {


    @Resource
    private VectorStore vectorStore;
    @Resource
    private AiClientNode aiClientNode;

    @Override
    protected String doApply(ArmoryCommandEntity requestParameter, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("Ai Agent 构建节点，Advisor 顾问角色{}", JSON.toJSONString(requestParameter));
        //将动态背包中查询到的值对象取出来，并以此存为bean
        List<AiClientAdvisorVO> aiClientAdvisorList = dynamicContext.getValue(dataName());

        if (aiClientAdvisorList == null || aiClientAdvisorList.isEmpty()) {
            log.warn("没有需要被初始化的 ai client advisor");
            return router(requestParameter, dynamicContext);
        }

        for(AiClientAdvisorVO advisorVO : aiClientAdvisorList){
            Advisor advisor = createAdvisor(advisorVO, vectorStore);
            registerBean(beanName(advisorVO.getAdvisorId()),Advisor.class, advisor);
        }

        return router(requestParameter, dynamicContext);
    }

    @Override//下一个节点是客户端节点
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> get(ArmoryCommandEntity requestParameter, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return aiClientNode;
    }

    public String dataName(){
        return AiAgentEnumVO.AI_CLIENT_ADVISOR.getDataName();
    }

    public String beanName(String beanId){
        return AiAgentEnumVO.AI_CLIENT_ADVISOR.getBeanName(beanId);
    }


    public Advisor createAdvisor(AiClientAdvisorVO aiClientAdvisorVO, VectorStore vectorStore) {
        //根据传进来的值对象获取对应的获取枚举类，获取枚举类之后创建顾问
        AiClientAdvisorTypeEnumVO aiClientAdvisorTypeEnumVO = AiClientAdvisorTypeEnumVO.getByCode(aiClientAdvisorVO.getAdvisorType());
        return aiClientAdvisorTypeEnumVO.createAdvisor(aiClientAdvisorVO, vectorStore);
    }
}
