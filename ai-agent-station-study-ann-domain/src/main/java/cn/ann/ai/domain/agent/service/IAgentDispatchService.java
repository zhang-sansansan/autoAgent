package cn.ann.ai.domain.agent.service;

import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import cn.ann.ai.domain.agent.model.entity.ExecuteCommandEntity;

//调度接口
public interface IAgentDispatchService {

    
    void dispatch(ExecuteCommandEntity requestParameter,ResponseBodyEmitter emitter) throws Exception;
        
}