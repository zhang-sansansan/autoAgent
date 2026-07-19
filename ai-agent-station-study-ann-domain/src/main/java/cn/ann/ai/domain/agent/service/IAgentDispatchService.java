package cn.ann.ai.domain.agent.service;

import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import cn.ann.ai.domain.agent.model.entity.ExecuteCommandEntity;

//璋冨害鎺ュ彛
public interface IAgentDispatchService {

    
    void dispatch(ExecuteCommandEntity requestParameter,ResponseBodyEmitter emitter) throws Exception;
        
}
