package cn.ann.ai.domain.agent.service;

import cn.ann.ai.domain.agent.model.entity.ExecuteCommandEntity;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

/**
 * 鎵ц绛栫暐鎺ュ彛
 *
 * @author xiaofuge bugstack.cn @灏忓倕鍝? * 2025/8/5 09:48
 */
public interface IExecuteStrategy {

    //ResponseBodyEmitter寮傛鍙戦€佸搷搴旀暟鎹? 涓嶅儚浼犵粺http璇锋眰涓€娆℃€у叏鍙戦€?  鑰屾槸鐢熸垚涓€涓氨鍙戦€佷竴涓?閬垮厤http瓒呮椂 鐢ㄦ埛浣撻獙涔熸洿濂?    void execute(ExecuteCommandEntity requestParameter, ResponseBodyEmitter emitter) throws Exception;

}

