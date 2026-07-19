package cn.ann.ai.types.job;

import cn.ann.ai.types.job.config.TaskJobAutoProperties;
import cn.ann.ai.types.job.service.ITaskJobService;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 浠诲姟璋冨害浣滀笟
 * 瀹氭椂鑾峰彇鏈夋晥鐨勪换鍔¤皟搴﹂厤缃紝骞跺姩鎬佸垱寤烘柊鐨勪换鍔? *
 * @author @灏忓倕鍝? */
public class TaskJob {

    private final TaskJobAutoProperties properties;
    private final ITaskJobService taskJobService;

    public TaskJob(TaskJobAutoProperties properties, ITaskJobService taskJobService) {
        this.properties = properties;
        this.taskJobService = taskJobService;
    }

    /**
     * 瀹氭椂鍒锋柊浠诲姟璋冨害閰嶇疆
     */
    @Scheduled(fixedRateString = "${xfg.wrench.task.job.refresh-interval:60000}")
    public void refreshTasks() {
        if (!properties.isEnabled()) {
            return;
        }
        taskJobService.refreshTasks();
    }

    /**
     * 瀹氭椂娓呯悊鏃犳晥浠诲姟
     */
    @Scheduled(cron = "${xfg.wrench.task.job.clean-invalid-tasks-cron:0 0/10 * * * ?}")
    public void cleanInvalidTasks() {
        if (!properties.isEnabled()) {
            return;
        }
        taskJobService.cleanInvalidTasks();
    }

}
