package cn.ann.ai.infrastructure.dao;

import cn.ann.ai.infrastructure.dao.po.AiClientModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 鑱婂ぉ妯″瀷閰嶇疆琛?DAO
 * @author bugstack铏礊鏍? * @description 鑱婂ぉ妯″瀷閰嶇疆琛ㄦ暟鎹闂璞? */
@Mapper
public interface IAiClientModelDao {

    /**
     * 鎻掑叆鑱婂ぉ妯″瀷閰嶇疆
     * @param aiClientModel 鑱婂ぉ妯″瀷閰嶇疆瀵硅薄
     * @return 褰卞搷琛屾暟
     */
    int insert(AiClientModel aiClientModel);

    /**
     * 鏍规嵁ID鏇存柊鑱婂ぉ妯″瀷閰嶇疆
     * @param aiClientModel 鑱婂ぉ妯″瀷閰嶇疆瀵硅薄
     * @return 褰卞搷琛屾暟
     */
    int updateById(AiClientModel aiClientModel);

    /**
     * 鏍规嵁妯″瀷ID鏇存柊鑱婂ぉ妯″瀷閰嶇疆
     * @param aiClientModel 鑱婂ぉ妯″瀷閰嶇疆瀵硅薄
     * @return 褰卞搷琛屾暟
     */
    int updateByModelId(AiClientModel aiClientModel);

    /**
     * 鏍规嵁ID鍒犻櫎鑱婂ぉ妯″瀷閰嶇疆
     * @param id 涓婚敭ID
     * @return 褰卞搷琛屾暟
     */
    int deleteById(Long id);

    /**
     * 鏍规嵁妯″瀷ID鍒犻櫎鑱婂ぉ妯″瀷閰嶇疆
     * @param modelId 妯″瀷ID
     * @return 褰卞搷琛屾暟
     */
    int deleteByModelId(String modelId);

    /**
     * 鏍规嵁ID鏌ヨ鑱婂ぉ妯″瀷閰嶇疆
     * @param id 涓婚敭ID
     * @return 鑱婂ぉ妯″瀷閰嶇疆瀵硅薄
     */
    AiClientModel queryById(Long id);

    /**
     * 鏍规嵁妯″瀷ID鏌ヨ鑱婂ぉ妯″瀷閰嶇疆
     * @param modelId 妯″瀷ID
     * @return 鑱婂ぉ妯″瀷閰嶇疆瀵硅薄
     */
    AiClientModel queryByModelId(String modelId);

    /**
     * 鏍规嵁API閰嶇疆ID鏌ヨ鑱婂ぉ妯″瀷閰嶇疆
     * @param apiId API閰嶇疆ID
     * @return 鑱婂ぉ妯″瀷閰嶇疆鍒楄〃
     */
    List<AiClientModel> queryByApiId(String apiId);

    /**
     * 鏍规嵁妯″瀷绫诲瀷鏌ヨ鑱婂ぉ妯″瀷閰嶇疆
     * @param modelType 妯″瀷绫诲瀷
     * @return 鑱婂ぉ妯″瀷閰嶇疆鍒楄〃
     */
    List<AiClientModel> queryByModelType(String modelType);

    /**
     * 鏌ヨ鎵€鏈夊惎鐢ㄧ殑鑱婂ぉ妯″瀷閰嶇疆
     * @return 鑱婂ぉ妯″瀷閰嶇疆鍒楄〃
     */
    List<AiClientModel> queryEnabledModels();

    /**
     * 鏌ヨ鎵€鏈夎亰澶╂ā鍨嬮厤缃?     * @return 鑱婂ぉ妯″瀷閰嶇疆鍒楄〃
     */
    List<AiClientModel> queryAll();

}
