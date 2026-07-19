package cn.ann.ai.test.dao;

import cn.ann.ai.infrastructure.dao.IAiAgentDao;
import cn.ann.ai.infrastructure.dao.po.AiAgent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AI鏅鸿兘浣撻厤缃〃 DAO 娴嬭瘯
 * @author bugstack铏礊鏍? * @description AI鏅鸿兘浣撻厤缃〃鏁版嵁璁块棶瀵硅薄娴嬭瘯
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AiAgentDaoTest {

    @Resource
    private IAiAgentDao aiAgentDao;

    @Test
    public void test_insert() {
        AiAgent aiAgent = AiAgent.builder()
                .agentId("test_001")
                .agentName("娴嬭瘯鏅鸿兘浣?)
                .description("杩欐槸涓€涓祴璇曟櫤鑳戒綋")
                .channel("agent")
                .status(1)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        int result = aiAgentDao.insert(aiAgent);
        log.info("鎻掑叆缁撴灉: {}, 鐢熸垚ID: {}", result, aiAgent.getId());
    }

    @Test
    public void test_updateById() {
        AiAgent aiAgent = AiAgent.builder()
                .id(1L)
                .agentId("test_001")
                .agentName("鏇存柊鍚庣殑娴嬭瘯鏅鸿兘浣?)
                .description("杩欐槸涓€涓洿鏂板悗鐨勬祴璇曟櫤鑳戒綋")
                .channel("chat_stream")
                .status(1)
                .updateTime(LocalDateTime.now())
                .build();

        int result = aiAgentDao.updateById(aiAgent);
        log.info("鏇存柊缁撴灉: {}", result);
    }

    @Test
    public void test_updateByAgentId() {
        AiAgent aiAgent = AiAgent.builder()
                .agentId("test_001")
                .agentName("閫氳繃AgentId鏇存柊鐨勬櫤鑳戒綋")
                .description("閫氳繃AgentId鏇存柊鐨勬弿杩?)
                .channel("agent")
                .status(0)
                .updateTime(LocalDateTime.now())
                .build();

        int result = aiAgentDao.updateByAgentId(aiAgent);
        log.info("閫氳繃AgentId鏇存柊缁撴灉: {}", result);
    }

    @Test
    public void test_queryById() {
        AiAgent aiAgent = aiAgentDao.queryById(1L);
        log.info("鏍规嵁ID鏌ヨ缁撴灉: {}", aiAgent);
    }

    @Test
    public void test_queryByAgentId() {
        AiAgent aiAgent = aiAgentDao.queryByAgentId("1");
        log.info("鏍规嵁AgentId鏌ヨ缁撴灉: {}", aiAgent);
    }

    @Test
    public void test_queryEnabledAgents() {
        List<AiAgent> aiAgents = aiAgentDao.queryEnabledAgents();
        log.info("鏌ヨ鍚敤鐨勬櫤鑳戒綋鏁伴噺: {}", aiAgents.size());
        aiAgents.forEach(agent -> log.info("鍚敤鐨勬櫤鑳戒綋: {}", agent));
    }

    @Test
    public void test_queryByChannel() {
        List<AiAgent> aiAgents = aiAgentDao.queryByChannel("agent");
        log.info("鏍规嵁娓犻亾鏌ヨ缁撴灉鏁伴噺: {}", aiAgents.size());
        aiAgents.forEach(agent -> log.info("娓犻亾鏅鸿兘浣? {}", agent));
    }

    @Test
    public void test_queryAll() {
        List<AiAgent> aiAgents = aiAgentDao.queryAll();
        log.info("鏌ヨ鎵€鏈夋櫤鑳戒綋鏁伴噺: {}", aiAgents.size());
        aiAgents.forEach(agent -> log.info("鏅鸿兘浣? {}", agent));
    }

    @Test
    public void test_deleteById() {
        int result = aiAgentDao.deleteById(1L);
        log.info("鏍规嵁ID鍒犻櫎缁撴灉: {}", result);
    }

    @Test
    public void test_deleteByAgentId() {
        int result = aiAgentDao.deleteByAgentId("test_001");
        log.info("鏍规嵁AgentId鍒犻櫎缁撴灉: {}", result);
    }

}
