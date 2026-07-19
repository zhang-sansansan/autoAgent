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
        log.info("Ai Agent 鏋勫缓鑺傜偣锛孉dvisor 椤鹃棶瑙掕壊{}", JSON.toJSONString(requestParameter));
        //灏嗗姩鎬佽儗鍖呬腑鏌ヨ鍒扮殑鍊煎璞″彇鍑烘潵锛屽苟浠ユ瀛樹负bean
        List<AiClientAdvisorVO> aiClientAdvisorList = dynamicContext.getValue(dataName());

        if (aiClientAdvisorList == null || aiClientAdvisorList.isEmpty()) {
            log.warn("娌℃湁闇€瑕佽鍒濆鍖栫殑 ai client advisor");
            return router(requestParameter, dynamicContext);
        }

        for(AiClientAdvisorVO advisorVO : aiClientAdvisorList){
            Advisor advisor = createAdvisor(advisorVO, vectorStore);
            registerBean(beanName(advisorVO.getAdvisorId()),Advisor.class, advisor);
        }

        return router(requestParameter, dynamicContext);
    }

    @Override//涓嬩竴涓妭鐐规槸瀹㈡埛绔妭鐐?
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
        //鏍规嵁浼犺繘鏉ョ殑鍊煎璞¤幏鍙栧搴旂殑鑾峰彇鏋氫妇绫伙紝鑾峰彇鏋氫妇绫讳箣鍚庡垱寤洪【闂?
        AiClientAdvisorTypeEnumVO aiClientAdvisorTypeEnumVO = AiClientAdvisorTypeEnumVO.getByCode(aiClientAdvisorVO.getAdvisorType());
        return aiClientAdvisorTypeEnumVO.createAdvisor(aiClientAdvisorVO, vectorStore);
    }
}

