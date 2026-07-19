package cn.ann.ai.api;

import cn.ann.ai.api.dto.AdminUserLoginRequestDTO;
import cn.ann.ai.api.dto.AdminUserQueryRequestDTO;
import cn.ann.ai.api.dto.AdminUserRequestDTO;
import cn.ann.ai.api.dto.AdminUserResponseDTO;
import cn.ann.ai.api.response.Response;

import java.util.List;

/**
 * 绠＄悊鍛樼敤鎴风鐞嗘湇鍔℃帴鍙?
 *
 * @author bugstack铏礊鏍?
 * @description 绠＄悊鍛樼敤鎴风鐞嗘湇鍔℃帴鍙?
 */
public interface IAdminUserAdminService {

    /**
     * 鍒涘缓绠＄悊鍛樼敤鎴?
     * @param request 绠＄悊鍛樼敤鎴疯姹傚璞?
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> createAdminUser(AdminUserRequestDTO request);

    /**
     * 鏍规嵁ID鏇存柊绠＄悊鍛樼敤鎴?
     * @param request 绠＄悊鍛樼敤鎴疯姹傚璞?
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> updateAdminUserById(AdminUserRequestDTO request);

    /**
     * 鏍规嵁鐢ㄦ埛ID鏇存柊绠＄悊鍛樼敤鎴?
     * @param request 绠＄悊鍛樼敤鎴疯姹傚璞?
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> updateAdminUserByUserId(AdminUserRequestDTO request);

    /**
     * 鏍规嵁ID鍒犻櫎绠＄悊鍛樼敤鎴?
     * @param id 涓婚敭ID
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> deleteAdminUserById(Long id);

    /**
     * 鏍规嵁鐢ㄦ埛ID鍒犻櫎绠＄悊鍛樼敤鎴?
     * @param userId 鐢ㄦ埛ID
     * @return 鎿嶄綔缁撴灉
     */
    Response<Boolean> deleteAdminUserByUserId(String userId);

    /**
     * 鏍规嵁ID鏌ヨ绠＄悊鍛樼敤鎴?
     * @param id 涓婚敭ID
     * @return 绠＄悊鍛樼敤鎴峰璞?
     */
    Response<AdminUserResponseDTO> queryAdminUserById(Long id);

    /**
     * 鏍规嵁鐢ㄦ埛ID鏌ヨ绠＄悊鍛樼敤鎴?
     * @param userId 鐢ㄦ埛ID
     * @return 绠＄悊鍛樼敤鎴峰璞?
     */
    Response<AdminUserResponseDTO> queryAdminUserByUserId(String userId);

    /**
     * 鏍规嵁鐢ㄦ埛鍚嶆煡璇㈢鐞嗗憳鐢ㄦ埛
     * @param username 鐢ㄦ埛鍚?
     * @return 绠＄悊鍛樼敤鎴峰璞?
     */
    Response<AdminUserResponseDTO> queryAdminUserByUsername(String username);

    /**
     * 鏌ヨ鎵€鏈夊惎鐢ㄧ姸鎬佺殑绠＄悊鍛樼敤鎴?
     * @return 绠＄悊鍛樼敤鎴峰垪琛?
     */
    Response<List<AdminUserResponseDTO>> queryEnabledAdminUsers();

    /**
     * 鏍规嵁鐘舵€佹煡璇㈢鐞嗗憳鐢ㄦ埛鍒楄〃
     * @param status 鐘舵€?
     * @return 绠＄悊鍛樼敤鎴峰垪琛?
     */
    Response<List<AdminUserResponseDTO>> queryAdminUsersByStatus(Integer status);

    /**
     * 鏍规嵁鏉′欢鏌ヨ绠＄悊鍛樼敤鎴峰垪琛?
     * @param request 鏌ヨ鏉′欢
     * @return 绠＄悊鍛樼敤鎴峰垪琛?
     */
    Response<List<AdminUserResponseDTO>> queryAdminUserList(AdminUserQueryRequestDTO request);

    /**
     * 鏌ヨ鎵€鏈夌鐞嗗憳鐢ㄦ埛
     * @return 绠＄悊鍛樼敤鎴峰垪琛?
     */
    Response<List<AdminUserResponseDTO>> queryAllAdminUsers();

    /**
     * 鐢ㄦ埛鐧诲綍楠岃瘉
     * @param request 鐧诲綍璇锋眰瀵硅薄
     * @return 绠＄悊鍛樼敤鎴峰璞?
     */
    Response<AdminUserResponseDTO> loginAdminUser(AdminUserLoginRequestDTO request);

    /**
     * 鐢ㄦ埛鐧诲綍鏍￠獙
     * @param request 鐧诲綍璇锋眰瀵硅薄
     * @return 鐧诲綍鏍￠獙缁撴灉锛屾垚鍔熻繑鍥瀟rue锛屽け璐ヨ繑鍥瀎alse
     */
    Response<Boolean> validateAdminUserLogin(AdminUserLoginRequestDTO request);

}
