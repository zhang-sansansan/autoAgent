package cn.ann.ai.api;

import java.util.List;

import cn.ann.ai.api.dto.AiAgentDrawConfigQueryRequestDTO;
import cn.ann.ai.api.dto.AiAgentDrawConfigRequestDTO;
import cn.ann.ai.api.dto.AiAgentDrawConfigResponseDTO;
import cn.ann.ai.api.response.Response;

/**
 * AI鏅鸿兘浣撴嫋鎷夋嫿閰嶇疆绠＄悊鏈嶅姟鎺ュ彛
 *
 * @author xiaofuge bugstack.cn @灏忓倕鍝? * 2025/9/28 07:35
 */
public interface IAiAgentDrawAdminService {

    /**
     * 淇濆瓨鎷栨媺鎷芥祦绋嬪浘閰嶇疆
     *
     * @param request 閰嶇疆璇锋眰鍙傛暟
     * @return 淇濆瓨缁撴灉
     */
    Response<String> saveDrawConfig(AiAgentDrawConfigRequestDTO request);

    /**
     * 鑾峰彇鎷栨媺鎷芥祦绋嬪浘閰嶇疆
     *
     * @param configId 閰嶇疆ID
     * @return 閰嶇疆鏁版嵁
     */
    Response<AiAgentDrawConfigResponseDTO> getDrawConfig(String configId);

    /**
     * 鍒嗛〉鏌ヨ鎷栨媺鎷芥祦绋嬪浘閰嶇疆鍒楄〃
     *
     * @param request 鏌ヨ鏉′欢涓庡垎椤靛弬鏁?     * @return 閰嶇疆鍒楄〃
     */
    Response<List<AiAgentDrawConfigResponseDTO>> queryDrawConfigList(AiAgentDrawConfigQueryRequestDTO request);


    /**
     * 鍒犻櫎鎷栨媺鎷芥祦绋嬪浘閰嶇疆
     *
     * @param configId 閰嶇疆ID
     * @return 鍒犻櫎缁撴灉
     */
    Response<String> deleteDrawConfig(String configId);

}

