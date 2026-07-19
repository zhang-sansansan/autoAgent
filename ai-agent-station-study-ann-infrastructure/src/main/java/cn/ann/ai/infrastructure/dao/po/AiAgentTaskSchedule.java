package cn.ann.ai.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 鏅鸿兘浣撲换鍔¤皟搴﹂厤缃〃
 * @author bugstack铏礊鏍? * @description 鏅鸿兘浣撲换鍔¤皟搴﹂厤缃〃 PO 瀵硅薄
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiAgentTaskSchedule {

    /**
     * 涓婚敭ID
     */
    private Long id;

    /**
     * 鏅鸿兘浣揑D
     */
    private String agentId;

    /**
     * 浠诲姟鍚嶇О
     */
    private String taskName;

    /**
     * 浠诲姟鎻忚堪
     */
    private String description;

    /**
     * 鏃堕棿琛ㄨ揪寮?濡? 0/3 * * * * *)
     */
    private String cronExpression;

    /**
     * 浠诲姟鍏ュ弬閰嶇疆(JSON鏍煎紡)
     */
    private String taskParam;

    /**
     * 鐘舵€?0:鏃犳晥,1:鏈夋晥)
     */
    private Integer status;

    /**
     * 鍒涘缓鏃堕棿
     */
    private LocalDateTime createTime;

    /**
     * 鏇存柊鏃堕棿
     */
    private LocalDateTime updateTime;

}
