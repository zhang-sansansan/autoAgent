package cn.ann.ai.infrastructure.dao;

import cn.ann.ai.infrastructure.dao.po.AiClientSystemPrompt;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 绯荤粺鎻愮ず璇嶉厤缃〃 DAO
 */
@Mapper
public interface IAiClientSystemPromptDao {

    /**
     * 鎻掑叆绯荤粺鎻愮ず璇嶉厤缃?     */
    void insert(AiClientSystemPrompt aiClientSystemPrompt);

    /**
     * 鏍规嵁ID鏇存柊绯荤粺鎻愮ず璇嶉厤缃?     */
    int updateById(AiClientSystemPrompt aiClientSystemPrompt);

    /**
     * 鏍规嵁鎻愮ず璇岻D鏇存柊绯荤粺鎻愮ず璇嶉厤缃?     */
    int updateByPromptId(AiClientSystemPrompt aiClientSystemPrompt);

    /**
     * 鏍规嵁ID鍒犻櫎绯荤粺鎻愮ず璇嶉厤缃?     */
    int deleteById(Long id);

    /**
     * 鏍规嵁鎻愮ず璇岻D鍒犻櫎绯荤粺鎻愮ず璇嶉厤缃?     */
    int deleteByPromptId(String promptId);

    /**
     * 鏍规嵁ID鏌ヨ绯荤粺鎻愮ず璇嶉厤缃?     */
    AiClientSystemPrompt queryById(Long id);

    /**
     * 鏍规嵁鎻愮ず璇岻D鏌ヨ绯荤粺鎻愮ず璇嶉厤缃?     */
    AiClientSystemPrompt queryByPromptId(String promptId);

    /**
     * 鏌ヨ鍚敤鐨勭郴缁熸彁绀鸿瘝閰嶇疆
     */
    List<AiClientSystemPrompt> queryEnabledPrompts();

    /**
     * 鏍规嵁鎻愮ず璇嶅悕绉版煡璇㈢郴缁熸彁绀鸿瘝閰嶇疆
     */
    List<AiClientSystemPrompt> queryByPromptName(String promptName);

    /**
     * 鏌ヨ鎵€鏈夌郴缁熸彁绀鸿瘝閰嶇疆
     */
    List<AiClientSystemPrompt> queryAll();

}
