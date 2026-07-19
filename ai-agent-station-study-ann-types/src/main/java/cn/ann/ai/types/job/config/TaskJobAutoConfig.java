package cn.ann.ai.types.job.config;

import cn.ann.ai.types.job.TaskJob;
import cn.ann.ai.types.job.provider.ITaskDataProvider;
import cn.ann.ai.types.job.service.ITaskJobService;
import cn.ann.ai.types.job.service.TaskJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.List;

/**
 * 浠诲姟璋冨害鍣ㄨ嚜鍔ㄩ厤缃被
 *
 * @author @灏忓倕鍝? */
@Configuration
@EnableScheduling
@EnableConfigurationProperties(TaskJobAutoProperties.class)
@ConditionalOnProperty(prefix = "xfg.wrench.task.job", name = "enabled", havingValue = "true", matchIfMissing = true)
public class TaskJobAutoConfig {

    private final Logger log = LoggerFactory.getLogger(TaskJobAutoConfig.class);

    /**
     * 鍒涘缓绾跨▼姹犱换鍔¤皟搴﹀櫒瀹炰緥锛岀敤浜庢墽琛屽畾鏃朵换鍔″拰寮傛浠诲姟璋冨害
     */
    @Bean("xfgWrenchTaskScheduler")
    public TaskScheduler taskScheduler(TaskJobAutoProperties properties) {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(properties.getPoolSize());
        scheduler.setThreadNamePrefix(properties.getThreadNamePrefix());
        scheduler.setWaitForTasksToCompleteOnShutdown(properties.isWaitForTasksToCompleteOnShutdown());
        scheduler.setAwaitTerminationSeconds(properties.getAwaitTerminationSeconds());
        scheduler.initialize();
        
        log.info("xfg-wrench锛屼换鍔¤皟搴﹀櫒鍒濆鍖栧畬鎴愩€傜嚎绋嬫睜澶у皬: {}, 绾跨▼鍚嶅墠缂€: {}", 
                properties.getPoolSize(), properties.getThreadNamePrefix());
        
        return scheduler;
    }

    @Bean
    public ITaskJobService taskJobService(TaskScheduler xfgWrenchTaskScheduler, List<ITaskDataProvider> taskDataProviders) {
        // 瀹炰緥鍖栦换鍔″苟鍒濆鍖栬皟搴?        TaskJobService taskJobService = new TaskJobService(xfgWrenchTaskScheduler, taskDataProviders);
        taskJobService.initializeTasks();

        return taskJobService;
    }

    /**
     * 鑷姩妫€娴嬩换鍔?     */
    @Bean
    public TaskJob taskJob(TaskJobAutoProperties properties, ITaskJobService taskJobService) {
        log.info("xfg-wrench锛屼换鍔¤皟搴︿綔涓氬垵濮嬪寲瀹屾垚銆傚埛鏂伴棿闅? {}ms, 娓呯悊cron: {}", 
                properties.getRefreshInterval(), properties.getCleanInvalidTasksCron());
        return new TaskJob(properties, taskJobService);
    }

}
