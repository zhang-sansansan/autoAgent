package cn.ann.ai.domain.agent.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AutoAgent 鎵ц缁撴灉瀹炰綋
 *
 * @author xiaofuge bugstack.cn @灏忓倕鍝? */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AutoAgentExecuteResultEntity {

    /**
     * 鏁版嵁绫诲瀷锛歛nalysis(鍒嗘瀽闃舵), execution(鎵ц闃舵), supervision(鐩戠潱闃舵), summary(鎬荤粨闃舵), error(閿欒淇℃伅), complete(瀹屾垚鏍囪瘑)
     * 缁嗗垎绫诲瀷锛歛nalysis_status(浠诲姟鐘舵€佸垎鏋?, analysis_history(鎵ц鍘嗗彶璇勪及), analysis_strategy(涓嬩竴姝ョ瓥鐣?, analysis_progress(瀹屾垚搴﹁瘎浼?
     *          execution_target(鎵ц鐩爣), execution_process(鎵ц杩囩▼), execution_result(鎵ц缁撴灉), execution_quality(璐ㄩ噺妫€鏌?
     *          supervision_assessment(璐ㄩ噺璇勪及), supervision_issues(闂璇嗗埆), supervision_suggestions(鏀硅繘寤鸿), supervision_score(璐ㄩ噺璇勫垎)
     */
    private String type;

    /**
     * 瀛愮被鍨嬫爣璇嗭紝鐢ㄤ簬鍓嶇缁嗙矑搴﹀睍绀?     */
    private String subType;

    /**
     * 褰撳墠姝ラ
     */
    private Integer step;

    /**
     * 鏁版嵁鍐呭
     */
    private String content;

    /**
     * 鏄惁瀹屾垚
     */
    private Boolean completed;

    /**
     * 鏃堕棿鎴?     */
    private Long timestamp;

    /**
     * 浼氳瘽ID
     */
    private String sessionId;

    /**
     * 鍒涘缓鍒嗘瀽闃舵缁撴灉
     */
    public static AutoAgentExecuteResultEntity createAnalysisResult(Integer step, String content, String sessionId) {
        return AutoAgentExecuteResultEntity.builder()
                .type("analysis")
                .step(step)
                .content(content)
                .completed(false)
                .timestamp(System.currentTimeMillis())
                .sessionId(sessionId)
                .build();
    }

    /**
     * 鍒涘缓鍒嗘瀽闃舵缁嗗垎缁撴灉
     */
    public static AutoAgentExecuteResultEntity createAnalysisSubResult(Integer step, String subType, String content, String sessionId) {
        return AutoAgentExecuteResultEntity.builder()
                .type("analysis")
                .subType(subType)
                .step(step)
                .content(content)
                .completed(false)
                .timestamp(System.currentTimeMillis())
                .sessionId(sessionId)
                .build();
    }

    /**
     * 鍒涘缓鎵ц闃舵缁撴灉
     */
    public static AutoAgentExecuteResultEntity createExecutionResult(Integer step, String content, String sessionId) {
        return AutoAgentExecuteResultEntity.builder()
                .type("execution")
                .step(step)
                .content(content)
                .completed(false)
                .timestamp(System.currentTimeMillis())
                .sessionId(sessionId)
                .build();
    }

    /**
     * 鍒涘缓鎵ц闃舵缁嗗垎缁撴灉
     */
    public static AutoAgentExecuteResultEntity createExecutionSubResult(Integer step, String subType, String content, String sessionId) {
        return AutoAgentExecuteResultEntity.builder()
                .type("execution")
                .subType(subType)
                .step(step)
                .content(content)
                .completed(false)
                .timestamp(System.currentTimeMillis())
                .sessionId(sessionId)
                .build();
    }

    /**
     * 鍒涘缓鐩戠潱闃舵缁撴灉
     */
    public static AutoAgentExecuteResultEntity createSupervisionResult(Integer step, String content, String sessionId) {
        return AutoAgentExecuteResultEntity.builder()
                .type("supervision")
                .step(step)
                .content(content)
                .completed(false)
                .timestamp(System.currentTimeMillis())
                .sessionId(sessionId)
                .build();
    }

    /**
     * 鍒涘缓鐩戠潱闃舵缁嗗垎缁撴灉
     */
    public static AutoAgentExecuteResultEntity createSupervisionSubResult(Integer step, String subType, String content, String sessionId) {
        return AutoAgentExecuteResultEntity.builder()
                .type("supervision")
                .subType(subType)
                .step(step)
                .content(content)
                .completed(false)
                .timestamp(System.currentTimeMillis())
                .sessionId(sessionId)
                .build();
    }

    /**
     * 鍒涘缓鎬荤粨闃舵缁嗗垎鐨勭粨鏋?     */
    public static AutoAgentExecuteResultEntity createSummarySubResult(String subType, String content, String sessionId) {
        return AutoAgentExecuteResultEntity.builder()
                .type("summary")
                .subType(subType)
                .step(4)
                .content(content)
                .completed(false)
                .timestamp(System.currentTimeMillis())
                .sessionId(sessionId)
                .build();
    }

    /**
     * 鍒涘缓鎬荤粨闃舵缁撴灉
     */
    public static AutoAgentExecuteResultEntity createSummaryResult(String content, String sessionId) {
        return AutoAgentExecuteResultEntity.builder()
                .type("summary")
                .step(null)
                .content(content)
                .completed(true)
                .timestamp(System.currentTimeMillis())
                .sessionId(sessionId)
                .build();
    }

    /**
     * 鍒涘缓閿欒缁撴灉
     */
    public static AutoAgentExecuteResultEntity createErrorResult(String content, String sessionId) {
        return AutoAgentExecuteResultEntity.builder()
                .type("error")
                .step(null)
                .content(content)
                .completed(true)
                .timestamp(System.currentTimeMillis())
                .sessionId(sessionId)
                .build();
    }

    /**
     * 鍒涘缓瀹屾垚鏍囪瘑
     */
    public static AutoAgentExecuteResultEntity createCompleteResult(String sessionId) {
        return AutoAgentExecuteResultEntity.builder()
                .type("complete")
                .step(null)
                .content("鎵ц瀹屾垚")
                .completed(true)
                .timestamp(System.currentTimeMillis())
                .sessionId(sessionId)
                .build();
    }

}
