/**
 * Copyright (C), 2016-2019, 趣链科技有限有限公司
 * FileName: UserDao
 * Author:   lixuanfeng
 * Date:     2019/12/6 下午3:22
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hyperchain.dao;


import com.hyperchain.base.WMap;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author lixuanfeng
 * @create 2019/12/6
 * @since 1.0.0
 */
public interface UserDao {

    public List<WMap> getUserList() throws SQLException;

    public int add(Map user) throws SQLException;

    public int update(Integer id, Map user);

    public int delete(Integer id) throws SQLException;

    WMap findUserByUserName(String userName) throws SQLException;

    WMap getUserInfoById(String key) throws SQLException;

    void updateUserAvatar(Integer key, String avatarUrl) throws SQLException;

    void attentionPerson(Integer myKey, Integer attentionUserKey, String username, String avatar_url) throws SQLException;

    List<WMap> attentionPersons(Integer myKey) throws SQLException;

    void cancelAttention(Integer myKey, Integer attentionUserKey) throws SQLException;

    WMap isAttention(Integer myKey, Integer attentionUserKey);

    List<WMap> getUserCollectFiles(Integer key) throws SQLException;

    void addCollectFile(Integer key, String name, Integer isPublic, String des) throws SQLException;

    WMap isCollectBlog(Integer key, Integer blogId, Integer collectFileId);

    List<WMap> getFans(Integer key) throws SQLException;

    WMap getUserTotalInfoByid(Integer key);

    void updateUserInfo(Map updateUserVO) throws SQLException;

    WMap searchUserByName(String name);

    void updateUserPwd(Map updateUserVo) throws SQLException;

    WMap getPwdById(String key) throws SQLException;
}