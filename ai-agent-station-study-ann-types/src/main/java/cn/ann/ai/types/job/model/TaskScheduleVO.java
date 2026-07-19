package cn.ann.ai.types.job.model;

import lombok.Data;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * 浠诲姟璋冨害鍊煎璞? * @author @灏忓倕鍝? */
@Data
public class TaskScheduleVO {

    /** 浠诲姟ID */
    private Long id;
    
    /** 浠诲姟鎻忚堪 */
    private String description;
    
    /** Cron琛ㄨ揪寮?*/
    private String cronExpression;
    
    /** 浠诲姟鍙傛暟 */
    private String taskParam;
    
    /** 浠诲姟鎵ц鍣ㄥ嚱鏁板紡鎺ュ彛 */
    private Supplier<Runnable> taskExecutor;

    public TaskScheduleVO() {
    }

    /**
     * 渚挎嵎鏂规硶锛氳缃换鍔℃墽琛岄€昏緫
     * @param taskLogic 浠诲姟鎵ц閫昏緫
     */
    public void setTaskLogic(Runnable taskLogic) {
        this.taskExecutor = () -> taskLogic;
    }
    
    /**
     * 渚挎嵎鏂规硶锛氳缃甫鍙傛暟鐨勪换鍔℃墽琛岄€昏緫
     * @param taskLogic 浠诲姟鎵ц閫昏緫锛屾帴鏀秚askId鍜宼askParam
     */
    public void setTaskLogic(BiConsumer<Long, String> taskLogic) {
        this.taskExecutor = () -> () -> taskLogic.accept(this.id, this.taskParam);
    }

    @Override
    public String toString() {
        return "TaskScheduleVO{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", cronExpression='" + cronExpression + '\'' +
                ", taskParam='" + taskParam + '\'' +
                ", hasTaskExecutor=" + (taskExecutor != null) +
                '}';
    }
}
