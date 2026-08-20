package cn.ann.ai.domain.agent.service.execute.fixed;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class FixedAgentExecuteStrategyTest {

    @Test
    public void shouldSendContentDeltasThenOneCompleteEvent() throws Exception {
        ResponseBodyEmitter emitter = mock(ResponseBodyEmitter.class);
        FixedAgentExecuteStrategy strategy = new FixedAgentExecuteStrategy();

        String content = strategy.streamFinalContent(
                Flux.just("你", "", "好"), emitter, "session-1");

        assertEquals("你好", content);
        ArgumentCaptor<Object> payload = ArgumentCaptor.forClass(Object.class);
        verify(emitter, times(3)).send(payload.capture());
        List<Object> values = payload.getAllValues();
        assertTrue(values.get(0).toString().contains("\"type\":\"content\""));
        assertTrue(values.get(0).toString().contains("\"content\":\"你\""));
        assertTrue(values.get(1).toString().contains("\"type\":\"content\""));
        assertTrue(values.get(1).toString().contains("\"content\":\"好\""));
        assertTrue(values.get(2).toString().contains("\"type\":\"complete\""));
    }
}
