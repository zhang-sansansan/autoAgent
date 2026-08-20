package cn.ann.ai.domain.agent.service.armory.business.data;


import cn.ann.ai.domain.agent.model.entity.ArmoryCommandEntity;
import cn.ann.ai.domain.agent.service.armory.node.factory.DefaultArmoryStrategyFactory;

/**
 * 数据加载策略
 *
 * @author xiaofuge bugstack.cn @小傅哥
 * 2025/6/27 17:16
 */
public interface ILoadDataStrategy {
    //策略接口
    void loadData(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext);

}
