package cn.ann.ai.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 鏅鸿兘浣?瀹㈡埛绔叧鑱旇〃
 * @author bugstack铏礊鏍? * @description 鏅鸿兘浣?瀹㈡埛绔叧鑱旇〃 PO 瀵硅薄
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiAgentFlowConfig {

    /**
     * 涓婚敭ID
     */
    private Long id;

    /**
     * 鏅鸿兘浣揑D
     */
    private String agentId;

    /**
     * 瀹㈡埛绔疘D
     */
    private String clientId;

    /**
     * 瀹㈡埛绔悕绉?     */
    private String clientName;

    /**
     * 瀹㈡埛绔灇涓?     */
    private String clientType;

    /**
     * 搴忓垪鍙?鎵ц椤哄簭)
     */
    private Integer sequence;

    /**
     * 鎵ц姝ラ鎻愮ず璇?     */
    private String stepPrompt;

    /**
     * 鍒涘缓鏃堕棿
     */
    private LocalDateTime createTime;

}
