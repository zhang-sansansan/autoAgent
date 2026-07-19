package cn.ann.ai.types.job.service;

import cn.ann.ai.types.job.model.TaskScheduleVO;
import cn.ann.ai.types.job.provider.ITaskDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * 浠诲姟璋冨害鏈嶅姟瀹炵幇绫? *
 * @author @灏忓倕鍝? */
public class TaskJobService implements ITaskJobService, DisposableBean {

    private final Logger log = LoggerFactory.getLogger(TaskJobService.class);

    private final TaskScheduler taskScheduler;
    private final List<ITaskDataProvider> taskDataProviders;

    /**
     * 浠诲姟ID涓庝换鍔℃墽琛屽櫒鐨勬槧灏勶紝鐢ㄤ簬璁板綍宸叉坊鍔犵殑浠诲姟
     */
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    /**
     * 鏂扮殑鏋勯€犲嚱鏁帮紝涓嶄緷璧朓TaskExecutor
     */
    public TaskJobService(TaskScheduler taskScheduler,
                         List<ITaskDataProvider> taskDataProviders) {
        this.taskScheduler = taskScheduler;
        this.taskDataProviders = taskDataProviders;
    }
    
    @Override
    public void initializeTasks() {
        log.info("寮€濮嬪垵濮嬪寲浠诲姟璋冨害閰嶇疆");
        try {
            // 鑱氬悎鎵€鏈夋暟鎹彁渚涜€呯殑浠诲姟璋冨害閰嶇疆
            List<TaskScheduleVO> allTaskSchedules = new ArrayList<>();
            for (ITaskDataProvider provider : taskDataProviders) {
                List<TaskScheduleVO> taskSchedules = provider.queryAllValidTaskSchedule();
                if (taskSchedules != null) {
                    allTaskSchedules.addAll(taskSchedules);
                }
            }
            
            // 澶勭悊姣忎釜浠诲姟璋冨害閰嶇疆
            for (TaskScheduleVO task : allTaskSchedules) {
                // 鍒涘缓骞惰皟搴︽柊浠诲姟
                scheduleTask(task);
            }
            
            log.info("浠诲姟璋冨害閰嶇疆鍒濆鍖栧畬鎴愶紝宸插姞杞戒换鍔℃暟: {}", scheduledTasks.size());
        } catch (Exception e) {
            log.error("鍒濆鍖栦换鍔¤皟搴﹂厤缃椂鍙戠敓閿欒", e);
        }
    }

    @Override
    public boolean addTask(TaskScheduleVO task) {
        try {
            if (task == null || task.getId() == null) {
                log.warn("浠诲姟閰嶇疆涓虹┖鎴栦换鍔D涓虹┖锛屾棤娉曟坊鍔犱换鍔?);
                return false;
            }

            // 濡傛灉浠诲姟宸插瓨鍦紝鍏堢Щ闄ゆ棫浠诲姟
            if (scheduledTasks.containsKey(task.getId())) {
                log.info("浠诲姟宸插瓨鍦紝鍏堢Щ闄ゆ棫浠诲姟锛孖D: {}", task.getId());
                removeTask(task.getId());
            }

            // 璋冨害鏂颁换鍔?            scheduleTask(task);

            log.info("浠诲姟娣诲姞鎴愬姛锛孖D: {}, 鎻忚堪: {}", task.getId(), task.getDescription());
            return true;
        } catch (Exception e) {
            log.error("娣诲姞浠诲姟鏃跺彂鐢熼敊璇紝ID: {}", task != null ? task.getId() : "null", e);
            return false;
        }
    }

    @Override
    public boolean removeTask(Long taskId) {
        try {
            if (taskId == null) {
                log.warn("浠诲姟ID涓虹┖锛屾棤娉曠Щ闄や换鍔?);
                return false;
            }

            ScheduledFuture<?> future = scheduledTasks.remove(taskId);
            if (future != null) {
                future.cancel(true);
                log.info("浠诲姟绉婚櫎鎴愬姛锛孖D: {}", taskId);
                return true;
            } else {
                log.warn("鏈壘鍒拌绉婚櫎鐨勪换鍔★紝ID: {}", taskId);
                return false;
            }
        } catch (Exception e) {
            log.error("绉婚櫎浠诲姟鏃跺彂鐢熼敊璇紝ID: {}", taskId, e);
            return false;
        }
    }

    /**
     * 璋冨害鍗曚釜浠诲姟
     */
    private void scheduleTask(TaskScheduleVO task) {
        try {
            log.info("寮€濮嬭皟搴︿换鍔★紝ID: {}, 鎻忚堪: {}, Cron琛ㄨ揪寮? {}", task.getId(), task.getDescription(), task.getCronExpression());

            // 浣跨敤鏂扮殑鍑芥暟寮忕紪绋嬫柟寮?            ScheduledFuture<?> future = taskScheduler.schedule(
                    () -> executeTaskWithFunction(task),
                    new CronTrigger(task.getCronExpression())
            );

            scheduledTasks.put(task.getId(), future);

            log.info("浠诲姟璋冨害鎴愬姛锛堝嚱鏁板紡锛夛紝ID: {}", task.getId());
        } catch (Exception e) {
            log.error("璋冨害浠诲姟鏃跺彂鐢熼敊璇紝ID: {}", task.getId(), e);
        }
    }

    /**
     * 浣跨敤鍑芥暟寮忕紪绋嬫柟寮忔墽琛屼换鍔?     */
    private void executeTaskWithFunction(TaskScheduleVO task) {
        try {
            log.info("寮€濮嬫墽琛屼换鍔★紙鍑芥暟寮忥級锛孖D: {}, 鎻忚堪: {}", task.getId(), task.getDescription());

            // 鑾峰彇骞舵墽琛屼换鍔?            Runnable taskRunnable = task.getTaskExecutor().get();
            taskRunnable.run();

            log.info("浠诲姟鎵ц瀹屾垚锛堝嚱鏁板紡锛夛紝ID: {}", task.getId());
        } catch (Exception e) {
            log.error("鎵ц浠诲姟鏃跺彂鐢熼敊璇紙鍑芥暟寮忥級锛孖D: {}", task.getId(), e);
        }
    }
    
    @Override
    public void refreshTasks() {
        log.info("寮€濮嬪埛鏂颁换鍔¤皟搴﹂厤缃紙鍔ㄦ€佹洿鏂帮級");
        try {
            // 鑱氬悎鎵€鏈夋暟鎹彁渚涜€呯殑浠诲姟璋冨害閰嶇疆
            List<TaskScheduleVO> allTaskSchedules = new ArrayList<>();
            for (ITaskDataProvider provider : taskDataProviders) {
                List<TaskScheduleVO> taskSchedules = provider.queryAllValidTaskSchedule();
                if (taskSchedules != null) {
                    allTaskSchedules.addAll(taskSchedules);
                }
            }

            // 璁板綍褰撳墠閰嶇疆涓殑浠诲姟ID
            Map<Long, Boolean> currentTaskIds = new ConcurrentHashMap<>();

            // 澶勭悊姣忎釜浠诲姟璋冨害閰嶇疆
            for (TaskScheduleVO task : allTaskSchedules) {
                Long taskId = task.getId();
                currentTaskIds.put(taskId, true);

                // 濡傛灉浠诲姟宸茬粡瀛樺湪锛屽垯璺宠繃
                if (scheduledTasks.containsKey(taskId)) {
                    continue;
                }

                // 鍒涘缓骞惰皟搴︽柊浠诲姟
                scheduleTask(task);
            }

            // 绉婚櫎宸蹭笉瀛樺湪鐨勪换鍔?            scheduledTasks.keySet().removeIf(taskId -> {
                if (!currentTaskIds.containsKey(taskId)) {
                    ScheduledFuture<?> future = scheduledTasks.remove(taskId);
                    if (future != null) {
                        future.cancel(true);
                        log.info("宸茬Щ闄や换鍔★紝ID: {}", taskId);
                    }
                    return true;
                }
                return false;
            });

            log.info("浠诲姟璋冨害閰嶇疆鍒锋柊瀹屾垚锛屽綋鍓嶆椿璺冧换鍔℃暟: {}", scheduledTasks.size());
        } catch (Exception e) {
            log.error("鍒锋柊浠诲姟璋冨害閰嶇疆鏃跺彂鐢熼敊璇?, e);
        }
    }

    @Override
    public void cleanInvalidTasks() {
        log.info("寮€濮嬫竻鐞嗘棤鏁堢殑浠诲姟");
        try {
            // 鑱氬悎鎵€鏈夋暟鎹彁渚涜€呯殑鏃犳晥浠诲姟ID
            List<Long> allInvalidTaskIds = new ArrayList<>();
            for (ITaskDataProvider provider : taskDataProviders) {
                List<Long> invalidTaskIds = provider.queryAllInvalidTaskScheduleIds();
                if (invalidTaskIds != null) {
                    allInvalidTaskIds.addAll(invalidTaskIds);
                }
            }
            
            if (allInvalidTaskIds.isEmpty()) {
                log.info("娌℃湁鍙戠幇鏃犳晥鐨勪换鍔￠渶瑕佹竻鐞?);
                return;
            }
            
            log.info("鍙戠幇{}涓棤鏁堜换鍔￠渶瑕佹竻鐞?, allInvalidTaskIds.size());
            
            // 浠庤皟搴﹀櫒涓Щ闄よ繖浜涗换鍔?            for (Long taskId : allInvalidTaskIds) {
                ScheduledFuture<?> future = scheduledTasks.remove(taskId);
                if (future != null) {
                    future.cancel(true);
                    log.info("宸茬Щ闄ゆ棤鏁堜换鍔★紝ID: {}", taskId);
                }
            }
            
            log.info("鏃犳晥浠诲姟娓呯悊瀹屾垚锛屽綋鍓嶆椿璺冧换鍔℃暟: {}", scheduledTasks.size());
        } catch (Exception e) {
            log.error("娓呯悊鏃犳晥浠诲姟鏃跺彂鐢熼敊璇?, e);
        }
    }

    @Override
    public void stopAllTasks() {
        log.info("寮€濮嬪仠姝㈡墍鏈変换鍔?);
        scheduledTasks.forEach((id, future) -> {
            if (future != null) {
                future.cancel(true);
                log.info("宸插彇娑堜换鍔★紝ID: {}", id);
            }
        });
        scheduledTasks.clear();
        log.info("鎵€鏈変换鍔″凡鍋滄");
    }

    @Override
    public int getActiveTaskCount() {
        return scheduledTasks.size();
    }

    @Override
    public void destroy() {
        stopAllTasks();
    }

}
