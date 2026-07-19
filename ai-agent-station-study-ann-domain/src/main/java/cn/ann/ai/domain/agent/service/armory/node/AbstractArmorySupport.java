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
 * @description瑁呴厤鏀拺绫伙紝涓哄瓙绫绘暟鎹姞杞芥壙涓婂惎涓? 灏哛equest銆丆ontext銆丷esult閮界粰鍏蜂綋鐨勬寚瀹氬嚭鏉ヤ簡
 * 涓斿瓙绫绘湁鐨勫彧闇€瑕佺畝鍗曡绠椾笉闇€瑕佸紑鍚绾跨▼锛屾墍浠ョ粰multiThread浼犵┖
 * @create 2026/1/16 11:22
 */
public abstract class AbstractArmorySupport extends AbstractMultiThreadStrategyRouter<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> {
    private final Logger log = LoggerFactory.getLogger(AbstractArmorySupport.class);

    //灏嗛€氱敤璧勬簮娉ㄥ叆锛屽噺灏戦噸澶嶄唬鐮?
    @Resource
    protected ApplicationContext applicationContext;//鍔ㄦ€佽幏鍙朆ean  spring瀹瑰櫒

    @Resource
    protected ThreadPoolExecutor threadPoolExecutor;//澶氱嚎绋嬪苟鍙?

    @Resource
    protected IAgentRepository repository;//瀛愮被鏌ユ暟鎹簱

    @Override
    protected void multiThread(ArmoryCommandEntity requestParameter, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {
        // 缂虹渷鐨?
    }

    //鍦ㄥ熀绫诲垱寤轰竴涓敞鍐宐ean鐨勬柟娉曪紝鍥犱负鍦ㄦ墽琛屽畬涓氬姟閫昏緫涔嬪悗锛岄渶瑕佽皟鐢ㄧ埗绫荤殑router鏂规硶锛屼笉鑳借繑鍥炵粨鏋滄敞鍐宐ean
    protected synchronized <T> void registerBean(String beanName,Class<T>beanClass,T beanInstance){
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
        //浠庡鍣ㄤ腑鑾峰彇bean宸ュ巶
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass, () -> beanInstance);
        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        //灏哹eanDefinition瀵硅薄鎷垮嚭鏉ョ洿鎺?
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        if(beanFactory.containsBeanDefinition(beanName)){
            beanFactory.removeBeanDefinition(beanName);
        }
        beanFactory.registerBeanDefinition(beanName, beanDefinition);

        log.info("鎴愬姛娉ㄥ唽Bean: {}", beanName);
    }

    protected <T> T getBean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }

    protected String beanName(String beanId){
        //灏佽锛屾牴鎹産eanId鐩存帴浠庢灇涓剧被鑾峰緱baenName
        return null;
    }

    protected String dataName() {
        return null;
    }



}

