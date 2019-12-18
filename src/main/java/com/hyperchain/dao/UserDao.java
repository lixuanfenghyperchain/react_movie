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
    Map getUserById(Integer id) throws SQLException;

    public List<WMap> getUserList() throws SQLException;

    public int add(Map user) throws SQLException;

    public int update(Integer id, Map user);

    public int delete(Integer id) throws SQLException;

    WMap findUserByUserName(String userName) throws SQLException;
}