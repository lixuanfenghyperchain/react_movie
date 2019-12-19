/**
 * Copyright (C), 2016-2019, 趣链科技有限有限公司
 * FileName: UserDaoImpl
 * Author:   lixuanfeng
 * Date:     2019/12/6 下午3:26
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hyperchain.dao.impl;

import com.hyperchain.base.Ps;
import com.hyperchain.base.WMap;
import com.hyperchain.base.dao.IBaseDao;
import com.hyperchain.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private IBaseDao baseDao;  //这个是系统自带的


    @Override
    public List<WMap> getUserList() throws SQLException {
        String sql = "SELECT * FROM `user`";
        return baseDao.getMapList(sql);
    }

    @Override
    public int add(Map user) throws SQLException {
        String sql = "insert into user (name,password,mail,cellPhone,address,age) values (?,?,?,?,?,?)";
        Ps ps = new Ps();
        ps.addString((String) user.get("name"));
        ps.addString((String) user.get("password"));
        ps.addString((String) user.get("mail"));
        ps.addString((String) user.get("phone"));
        ps.addString((String) user.get("address"));
        ps.addInt(Integer.valueOf((String) user.get("age")));
        return baseDao.save(sql, ps);
    }

    @Override
    public int update(Integer id, Map user) {
        return 0;
    }

    @Override
    public int delete(Integer id) throws SQLException {
        String sql = "delete from user where `key` =?";
        Ps ps = new Ps();
        ps.addInt(id);
        return baseDao.delete(sql, ps);

    }

    @Override
    public WMap findUserByUserName(String userName) throws SQLException {
        String sql = "select `key`,name as userName ,password as passWord from user where name=? ";
        Ps ps = new Ps();
        ps.addString(userName);
        WMap map = baseDao.getMap(sql, ps);
        return map;
    }


    public WMap getUserInfoById(String key) throws SQLException {
        String sql = "select `key`,name as userName ,age,address,mail,cellPhone,avatar_url,user_des from user where `key`=? ";
        Ps ps = new Ps();
        ps.addString(key);
        WMap map = baseDao.getMap(sql, ps);
        return map;
    }

    @Override
    public void updateUserAvatar(Integer key, String avatarUrl) throws SQLException {
        String sql = "update user set avatar_url=? where `key`=?";
        Ps ps = new Ps();
        ps.addString(avatarUrl);
        ps.addInt(key);
        baseDao.update(sql, ps);
    }
}
