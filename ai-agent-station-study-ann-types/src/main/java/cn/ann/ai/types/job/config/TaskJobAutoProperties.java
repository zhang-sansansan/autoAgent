package cn.ann.ai.types.job.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 浠诲姟璋冨害鍣ㄩ厤缃睘鎬? * @author Fuzhengwei bugstack.cn @灏忓倕鍝? */
@ConfigurationProperties(prefix = "xfg.wrench.task.job", ignoreInvalidFields = true)
public class TaskJobAutoProperties {

    /** 鏄惁鍚敤浠诲姟璋冨害鍣?*/
    private boolean enabled = true;
    
    /** 绾跨▼姹犲ぇ灏?*/
    private int poolSize = 10;
    
    /** 绾跨▼鍚嶇О鍓嶇紑 */
    private String threadNamePrefix = "xfg-ta${OPENAI_API_KEY}";
    
    /** 鍏抽棴鏃剁瓑寰呬换鍔″畬鎴?*/
    private boolean waitForTasksToCompleteOnShutdown = true;
    
    /** 绛夊緟缁堟鏃堕棿锛堢锛?*/
    private int awaitTerminationSeconds = 60;
    
    /** 浠诲姟鍒锋柊闂撮殧锛堟绉掞級 */
    private long refreshInterval = 60000;
    
    /** 娓呯悊鏃犳晥浠诲姟鐨刢ron琛ㄨ揪寮?*/
    private String cleanInvalidTasksCron = "0 0/10 * * * ?";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }

    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }

    public boolean isWaitForTasksToCompleteOnShutdown() {
        return waitForTasksToCompleteOnShutdown;
    }

    public void setWaitForTasksToCompleteOnShutdown(boolean waitForTasksToCompleteOnShutdown) {
        this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
    }

    public int getAwaitTerminationSeconds() {
        return awaitTerminationSeconds;
    }

    public void setAwaitTerminationSeconds(int awaitTerminationSeconds) {
        this.awaitTerminationSeconds = awaitTerminationSeconds;
    }

    public long getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(long refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public String getCleanInvalidTasksCron() {
        return cleanInvalidTasksCron;
    }

    public void setCleanInvalidTasksCron(String cleanInvalidTasksCron) {
        this.cleanInvalidTasksCron = cleanInvalidTasksCron;
    }
}
