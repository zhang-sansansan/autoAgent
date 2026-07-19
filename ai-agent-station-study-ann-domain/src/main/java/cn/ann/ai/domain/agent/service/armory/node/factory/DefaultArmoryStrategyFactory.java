package cn.ann.ai.domain.agent.service.armory.node.factory;

import cn.ann.ai.domain.agent.model.entity.ArmoryCommandEntity;
import cn.ann.ai.domain.agent.service.armory.node.RootNode;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 宸ュ巶绫? *
 * @author xiaofuge bugstack.cn @灏忓倕鍝? * 2025/6/27 07:14
 */
@Service
public class DefaultArmoryStrategyFactory {

    private final RootNode rootNode;

    public DefaultArmoryStrategyFactory(RootNode rootNode) {
        this.rootNode = rootNode;
    }//閫氳繃鏋勯€犲嚱鏁版敞鍏ュ鍣ㄤ腑鐨剅ootNode鐨刡ean

    //璇ュ伐绋嬬被鐩存帴杩斿洖鐨勫璞℃槸rootnode,閫氳繃spring鐨勬瀯閫犳柟娉曟敞鍏? 浠栨槸涓€涓瓥鐣ュ鐞嗗櫒
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> armoryStrategyHandler(){
        return rootNode;
    }
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext {

        private Map<String, Object> dataObjects = new HashMap<>();

        public <T> void setValue(String key, T value) {
            dataObjects.put(key, value);
        }

        public <T> T getValue(String key) {
            return (T) dataObjects.get(key);
        }
    }

}

