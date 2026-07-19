package cn.ann.ai.config;

//import cn.bugstack.wrench.task.job.TaskJob;
//import cn.bugstack.wrench.task.job.config.TaskJobAutoConfig;
//import cn.bugstack.wrench.task.job.config.TaskJobAutoProperties;
//import cn.bugstack.wrench.task.job.provider.ITaskDataProvider;
//import cn.bugstack.wrench.task.job.service.ITaskJobService;
//import cn.bugstack.wrench.task.job.service.TaskJobService;


/**
 *
 * @author xiaofuge bugstack.cn @灏忓倕鍝?
 * 2025/9/13 16:41
 */

//@Configuration
//@EnableScheduling
//@EnableConfigurationProperties({TaskJobAutoProperties.class})
//@ConditionalOnProperty(
//        prefix = "xfg.wrench.task.job",
//        name = {"enabled"},
//        havingValue = "true",
//        matchIfMissing = true
//)
public class TaskJobAutoConfigBean {

//    private final Logger log = LoggerFactory.getLogger(TaskJobAutoConfig.class);
//
//    @Bean({"xfgWrenchTaskScheduler"})
//    public TaskScheduler taskScheduler(TaskJobAutoProperties properties) {
//        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
//        scheduler.setPoolSize(properties.getPoolSize());
//        scheduler.setThreadNamePrefix(properties.getThreadNamePrefix());
//        scheduler.setWaitForTasksToCompleteOnShutdown(properties.isWaitForTasksToCompleteOnShutdown());
//        scheduler.setAwaitTerminationSeconds(properties.getAwaitTerminationSeconds());
//        scheduler.initialize();
//        this.log.info("xfg-wrench锛屼换鍔¤皟搴﹀櫒鍒濆鍖栧畬鎴愩€傜嚎绋嬫睜澶у皬: {}, 绾跨▼鍚嶅墠缂€: {}", properties.getPoolSize(), properties.getThreadNamePrefix());
//        return scheduler;
//    }
//
//    @Bean
//    public ITaskJobService taskJobService(TaskScheduler xfgWrenchTaskScheduler, List<ITaskDataProvider> taskDataProviders) {
//        TaskJobService taskJobService = new TaskJobService(xfgWrenchTaskScheduler, taskDataProviders);
//        taskJobService.initializeTasks();
//        return taskJobService;
//    }
//
//    @Bean
//    public TaskJob taskJob(TaskJobAutoProperties properties, ITaskJobService taskJobService) {
//        this.log.info("xfg-wrench锛屼换鍔¤皟搴︿綔涓氬垵濮嬪寲瀹屾垚銆傚埛鏂伴棿闅? {}ms, 娓呯悊cron: {}", properties.getRefreshInterval(), properties.getCleanInvalidTasksCron());
//        return new TaskJob(properties, taskJobService);
//    }

}

