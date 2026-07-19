package cn.ann.ai.domain.agent.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 鐭ヨ瘑搴撹鍗?
 * @author Fuzhengwei bugstack.cn @灏忓倕鍝?
 * 2025-05-05 20:02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiRagOrderVO {

    /**
     * 鐭ヨ瘑搴撳悕绉?
     */
    private String ragName;

    /**
     * 鐭ヨ瘑鏍囩
     */
    private String knowledgeTag;

}

