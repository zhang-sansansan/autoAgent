package cn.ann.ai.domain.agent.service;

import cn.ann.ai.domain.agent.model.entity.ExecuteCommandEntity;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

/**
 * 执行策略接口
 *
 * @author xiaofuge bugstack.cn @小傅哥
 * 2025/8/5 09:48
 */
public interface IExecuteStrategy {

    //ResponseBodyEmitter异步发送响应数据  不像传统http请求一次性全发送   而是生成一个就发送一个 避免http超时 用户体验也更好
    void execute(ExecuteCommandEntity requestParameter, ResponseBodyEmitter emitter) throws Exception;

}
