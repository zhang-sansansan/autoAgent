package cn.ann.ai.infrastructure.dao;

import cn.ann.ai.infrastructure.dao.po.AiClientAdvisor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 椤鹃棶閰嶇疆琛?DAO
 * @author bugstack铏礊鏍? * @description 椤鹃棶閰嶇疆琛ㄦ暟鎹闂璞? */
@Mapper
public interface IAiClientAdvisorDao {

    /**
     * 鎻掑叆椤鹃棶閰嶇疆
     * @param aiClientAdvisor 椤鹃棶閰嶇疆瀵硅薄
     * @return 褰卞搷琛屾暟
     */
    int insert(AiClientAdvisor aiClientAdvisor);

    /**
     * 鏍规嵁ID鏇存柊椤鹃棶閰嶇疆
     * @param aiClientAdvisor 椤鹃棶閰嶇疆瀵硅薄
     * @return 褰卞搷琛屾暟
     */
    int updateById(AiClientAdvisor aiClientAdvisor);

    /**
     * 鏍规嵁椤鹃棶ID鏇存柊椤鹃棶閰嶇疆
     * @param aiClientAdvisor 椤鹃棶閰嶇疆瀵硅薄
     * @return 褰卞搷琛屾暟
     */
    int updateByAdvisorId(AiClientAdvisor aiClientAdvisor);

    /**
     * 鏍规嵁ID鍒犻櫎椤鹃棶閰嶇疆
     * @param id 涓婚敭ID
     * @return 褰卞搷琛屾暟
     */
    int deleteById(Long id);

    /**
     * 鏍规嵁椤鹃棶ID鍒犻櫎椤鹃棶閰嶇疆
     * @param advisorId 椤鹃棶ID
     * @return 褰卞搷琛屾暟
     */
    int deleteByAdvisorId(String advisorId);

    /**
     * 鏍规嵁ID鏌ヨ椤鹃棶閰嶇疆
     * @param id 涓婚敭ID
     * @return 椤鹃棶閰嶇疆瀵硅薄
     */
    AiClientAdvisor queryById(Long id);

    /**
     * 鏍规嵁椤鹃棶ID鏌ヨ椤鹃棶閰嶇疆
     * @param advisorId 椤鹃棶ID
     * @return 椤鹃棶閰嶇疆瀵硅薄
     */
    AiClientAdvisor queryByAdvisorId(String advisorId);

    /**
     * 鏌ヨ鎵€鏈夐【闂厤缃?     * @return 椤鹃棶閰嶇疆鍒楄〃
     */
    List<AiClientAdvisor> queryAll();

    /**
     * 鏍规嵁鐘舵€佹煡璇㈤【闂厤缃?     * @param status 鐘舵€?     * @return 椤鹃棶閰嶇疆鍒楄〃
     */
    List<AiClientAdvisor> queryByStatus(Integer status);

    /**
     * 鏍规嵁椤鹃棶绫诲瀷鏌ヨ椤鹃棶閰嶇疆
     * @param advisorType 椤鹃棶绫诲瀷
     * @return 椤鹃棶閰嶇疆鍒楄〃
     */
    List<AiClientAdvisor> queryByAdvisorType(String advisorType);

}
