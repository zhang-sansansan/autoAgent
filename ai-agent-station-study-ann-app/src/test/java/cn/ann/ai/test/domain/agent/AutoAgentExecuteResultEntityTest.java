package cn.ann.ai.test.domain.agent;

import cn.ann.ai.domain.agent.model.entity.AutoAgentExecuteResultEntity;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AutoAgentExecuteResultEntityTest {

    @Test
    public void shouldCreateIncompleteContentDelta() {
        AutoAgentExecuteResultEntity result =
                AutoAgentExecuteResultEntity.createContentResult("你好", "session-1");

        assertEquals("content", result.getType());
        assertEquals("你好", result.getContent());
        assertEquals(Boolean.FALSE, result.getCompleted());
        assertEquals("session-1", result.getSessionId());
        assertNotNull(result.getTimestamp());
    }
}
