package cn.ann.ai.infrastructure.dao;

import cn.ann.ai.infrastructure.dao.po.AdminUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 绠＄悊鍛樼敤鎴疯〃 DAO
 * @author bugstack铏礊鏍?
 * @description 绠＄悊鍛樼敤鎴疯〃鏁版嵁璁块棶瀵硅薄
 */
@Mapper
public interface IAdminUserDao {

    /**
     * 鎻掑叆绠＄悊鍛樼敤鎴?
     * @param adminUser 绠＄悊鍛樼敤鎴峰璞?
     * @return 褰卞搷琛屾暟
     */
    int insert(AdminUser adminUser);

    /**
     * 鏍规嵁ID鏇存柊绠＄悊鍛樼敤鎴?
     * @param adminUser 绠＄悊鍛樼敤鎴峰璞?
     * @return 褰卞搷琛屾暟
     */
    int updateById(AdminUser adminUser);

    /**
     * 鏍规嵁鐢ㄦ埛ID鏇存柊绠＄悊鍛樼敤鎴?
     * @param adminUser 绠＄悊鍛樼敤鎴峰璞?
     * @return 褰卞搷琛屾暟
     */
    int updateByUserId(AdminUser adminUser);

    /**
     * 鏍规嵁ID鍒犻櫎绠＄悊鍛樼敤鎴?
     * @param id 涓婚敭ID
     * @return 褰卞搷琛屾暟
     */
    int deleteById(Long id);

    /**
     * 鏍规嵁鐢ㄦ埛ID鍒犻櫎绠＄悊鍛樼敤鎴?
     * @param userId 鐢ㄦ埛ID
     * @return 褰卞搷琛屾暟
     */
    int deleteByUserId(String userId);

    /**
     * 鏍规嵁ID鏌ヨ绠＄悊鍛樼敤鎴?
     * @param id 涓婚敭ID
     * @return 绠＄悊鍛樼敤鎴峰璞?
     */
    AdminUser queryById(Long id);

    /**
     * 鏍规嵁鐢ㄦ埛ID鏌ヨ绠＄悊鍛樼敤鎴?
     * @param userId 鐢ㄦ埛ID
     * @return 绠＄悊鍛樼敤鎴峰璞?
     */
    AdminUser queryByUserId(String userId);

    /**
     * 鏍规嵁鐢ㄦ埛鍚嶆煡璇㈢鐞嗗憳鐢ㄦ埛
     * @param username 鐢ㄦ埛鍚?
     * @return 绠＄悊鍛樼敤鎴峰璞?
     */
    AdminUser queryByUsername(String username);

    /**
     * 鏌ヨ鍚敤鐘舵€佺殑绠＄悊鍛樼敤鎴峰垪琛?
     * @return 绠＄悊鍛樼敤鎴峰垪琛?
     */
    List<AdminUser> queryEnabledUsers();

    /**
     * 鏍规嵁鐘舵€佹煡璇㈢鐞嗗憳鐢ㄦ埛鍒楄〃
     * @param status 鐘舵€?
     * @return 绠＄悊鍛樼敤鎴峰垪琛?
     */
    List<AdminUser> queryByStatus(Integer status);

    /**
     * 鏌ヨ鎵€鏈夌鐞嗗憳鐢ㄦ埛
     * @return 绠＄悊鍛樼敤鎴峰垪琛?
     */
    List<AdminUser> queryAll();

    /**
     * 鐢ㄦ埛鐧诲綍楠岃瘉
     * @param username 鐢ㄦ埛鍚?
     * @param password 瀵嗙爜
     * @return 绠＄悊鍛樼敤鎴峰璞?
     */
    AdminUser queryByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

}

