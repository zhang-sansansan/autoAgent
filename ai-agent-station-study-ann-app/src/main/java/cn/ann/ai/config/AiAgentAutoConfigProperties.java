package cn.ann.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;



/**
 * @author zhang san
 * @description
 * @create 2026/2/3 15:08
 */
//鑷姩瑁呴厤缃睘鎬?
@Data
@ConfigurationProperties(prefix = "spring.ai.agent.auto-config")
public class AiAgentAutoConfigProperties {
    /**
     * 鏄惁鍚敤AI Agent鑷姩瑁呴厤
     */
    private boolean enabled = false;

}

