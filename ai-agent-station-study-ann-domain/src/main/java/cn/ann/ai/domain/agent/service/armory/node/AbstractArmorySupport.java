package cn.ann.ai.domain.agent.service.armory.node;

import cn.ann.ai.domain.agent.adapter.repository.IAgentRepository;
import cn.ann.ai.domain.agent.model.entity.ArmoryCommandEntity;
import cn.ann.ai.domain.agent.model.valobj.AiAgentEnumVO;
import cn.ann.ai.domain.agent.service.armory.node.factory.DefaultArmoryStrategyFactory;
import cn.bugstack.wrench.design.framework.tree.AbstractMultiThreadStrategyRouter;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

/**
 * @author zhang san
 * @description装配支撑类，为子类数据加载承上启下  将Request、Context、Result都给具体的指定出来了
 * 且子类有的只需要简单计算不需要开启多线程，所以给multiThread传空
 * @create 2026/1/16 11:22
 */
public abstract class AbstractArmorySupport extends AbstractMultiThreadStrategyRouter<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> {
    private final Logger log = LoggerFactory.getLogger(AbstractArmorySupport.class);

    //将通用资源注入，减少重复代码
    @Resource
    protected ApplicationContext applicationContext;//动态获取Bean  spring容器

    @Resource
    protected ThreadPoolExecutor threadPoolExecutor;//多线程并发

    @Resource
    protected IAgentRepository repository;//子类查数据库

    @Override
    protected void multiThread(ArmoryCommandEntity requestParameter, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {
        // 缺省的
    }

    //在基类创建一个注册bean的方法，因为在执行完业务逻辑之后，需要调用父类的router方法，不能返回结果注册bean
    protected synchronized <T> void registerBean(String beanName,Class<T>beanClass,T beanInstance){
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
        //从容器中获取bean工厂
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass, () -> beanInstance);
        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        //将beanDefinition对象拿出来直接
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        if(beanFactory.containsBeanDefinition(beanName)){
            beanFactory.removeBeanDefinition(beanName);
        }
        beanFactory.registerBeanDefinition(beanName, beanDefinition);

        log.info("成功注册Bean: {}", beanName);
    }

    protected <T> T getBean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }

    protected String beanName(String beanId){
        //封装，根据beanId直接从枚举类获得baenName
        return null;
    }

    protected String dataName() {
        return null;
    }



}
