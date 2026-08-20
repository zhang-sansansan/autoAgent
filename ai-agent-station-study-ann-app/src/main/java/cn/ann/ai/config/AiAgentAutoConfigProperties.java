package cn.ann.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;



/**
 * @author zhang san
 * @description
 * @create 2026/2/3 15:08
 */
//自动装配置属性
@Data
@ConfigurationProperties(prefix = "spring.ai.agent.auto-config")
public class AiAgentAutoConfigProperties {
    /**
     * 是否启用AI Agent自动装配
     */
    private boolean enabled = false;

}
