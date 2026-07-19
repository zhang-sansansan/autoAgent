package cn.ann.ai.domain.agent.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 鐭ヨ瘑搴撴帴鍙ｆ湇鍔? * @author xiaofuge bugstack.cn @灏忓倕鍝? * 2025/10/4 09:11
 */
public interface IRagService {

    void storeRagFile(String name, String tag, List<MultipartFile> files);

}

