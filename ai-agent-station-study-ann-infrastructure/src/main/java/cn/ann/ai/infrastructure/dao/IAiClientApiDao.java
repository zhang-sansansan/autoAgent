package cn.ann.ai.infrastructure.dao;

import cn.ann.ai.infrastructure.dao.po.AiClientApi;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * AI瀹㈡埛绔疉PI閰嶇疆琛?DAO
 * @author bugstack铏礊鏍? * @description AI瀹㈡埛绔疉PI閰嶇疆琛ㄦ暟鎹闂璞? */
@Mapper
public interface IAiClientApiDao {

    /**
     * 鎻掑叆AI瀹㈡埛绔疉PI閰嶇疆
     * @param aiClientApi AI瀹㈡埛绔疉PI閰嶇疆瀵硅薄
     * @return 褰卞搷琛屾暟
     */
    int insert(AiClientApi aiClientApi);

    /**
     * 鏍规嵁ID鏇存柊AI瀹㈡埛绔疉PI閰嶇疆
     * @param aiClientApi AI瀹㈡埛绔疉PI閰嶇疆瀵硅薄
     * @return 褰卞搷琛屾暟
     */
    int updateById(AiClientApi aiClientApi);

    /**
     * 鏍规嵁API ID鏇存柊AI瀹㈡埛绔疉PI閰嶇疆
     * @param aiClientApi AI瀹㈡埛绔疉PI閰嶇疆瀵硅薄
     * @return 褰卞搷琛屾暟
     */
    int updateByApiId(AiClientApi aiClientApi);

    /**
     * 鏍规嵁ID鍒犻櫎AI瀹㈡埛绔疉PI閰嶇疆
     * @param id 涓婚敭ID
     * @return 褰卞搷琛屾暟
     */
    int deleteById(Long id);

    /**
     * 鏍规嵁API ID鍒犻櫎AI瀹㈡埛绔疉PI閰嶇疆
     * @param apiId API ID
     * @return 褰卞搷琛屾暟
     */
    int deleteByApiId(String apiId);

    /**
     * 鏍规嵁ID鏌ヨAI瀹㈡埛绔疉PI閰嶇疆
     * @param id 涓婚敭ID
     * @return AI瀹㈡埛绔疉PI閰嶇疆瀵硅薄
     */
    AiClientApi queryById(Long id);

    /**
     * 鏍规嵁API ID鏌ヨAI瀹㈡埛绔疉PI閰嶇疆
     * @param apiId API ID
     * @return AI瀹㈡埛绔疉PI閰嶇疆瀵硅薄
     */
    AiClientApi queryByApiId(String apiId);

    /**
     * 鏌ヨ鎵€鏈夊惎鐢ㄧ殑AI瀹㈡埛绔疉PI閰嶇疆
     * @return AI瀹㈡埛绔疉PI閰嶇疆鍒楄〃
     */
    List<AiClientApi> queryEnabledApis();

    /**
     * 鏌ヨ鎵€鏈堿I瀹㈡埛绔疉PI閰嶇疆
     * @return AI瀹㈡埛绔疉PI閰嶇疆鍒楄〃
     */
    List<AiClientApi> queryAll();

}
