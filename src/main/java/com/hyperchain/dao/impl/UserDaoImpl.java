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
        String sql = "insert into user (name,password,avatar_url) values (?,?,?)";
        Ps ps = new Ps();
        ps.addString((String) user.get("name"));
        ps.addString((String) user.get("password"));
        ps.addString("default_avatar.png");
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
        String sql = "select `key`,name as userName ,password as passWord ,avatar_url from user where name=? ";
        Ps ps = new Ps();
        ps.addString(userName);
        WMap map = baseDao.getMap(sql, ps);
        return map;
    }


    public WMap getUserInfoById(String key) throws SQLException {
        String sql = "select `key`,name as userName ,age,address,mail,cellPhone,avatar_url,nickname, position, sex, birthday, user_des from user where `key`=? ";
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

    @Override
    public void attentionPerson(Integer myKey, Integer attentionUserKey, String username, String avatar_url) throws SQLException {
        String sql = "insert into t_blog_attention (my_id,attention_user_id,attention_user_name,attention_user_url) values (?,?,?,?)";
        Ps ps = new Ps();
        ps.addInt(myKey);
        ps.addInt(attentionUserKey);
        ps.addString(username);
        ps.addString(avatar_url);
        baseDao.save(sql, ps);
    }

    @Override
    public List<WMap> attentionPersons(Integer myKey) throws SQLException {
        String sql = "select my_id as id ,attention_user_id as oid ,attention_user_name as name ,attention_user_url as url from t_blog_attention  where my_id=?";
        Ps ps = new Ps();
        ps.addInt(myKey);
        return baseDao.getMapList(sql, ps);
    }

    @Override
    public List<WMap> getFans(Integer key) throws SQLException {
        String sql = "select my_id as id , u.name as name ,u.avatar_url as url from t_blog_attention a ,user  u where  attention_user_id=? and a.my_id =u.key";
        Ps ps = new Ps();
        ps.addInt(key);
        return baseDao.getMapList(sql, ps);
    }

    @Override
    public WMap getUserTotalInfoByid(Integer key) {
        String sql = "select count(id) as blog_total,SUM(comment_num) as comment_total ,SUM(read_num) as read_total,SUM(star_num) as star_total  " +
                " from t_blog_article where create_user_id=?";
        Ps ps = new Ps();
        ps.addInt(key);
        return baseDao.getMap(sql, ps);
    }

    @Override
    public void updateUserInfo(Map updateUserVO) throws SQLException {
        //key, nickname, mail, position, sex, birthday, user_des
        String sql = "update user  set nickname=? ,mail=?,position=? ,sex=?,birthday=?,user_des=? where `key`=?";
        Ps ps = new Ps();
        ps.addString(updateUserVO.get("nickname").toString());
        ps.addString(updateUserVO.get("mail").toString());
        ps.addString(updateUserVO.get("position").toString());
        ps.addString(updateUserVO.get("sex").toString());
        ps.addString(updateUserVO.get("birthday").toString());
        ps.addString(updateUserVO.get("user_des").toString());
        ps.addInt(Integer.valueOf(updateUserVO.get("key").toString()));
        baseDao.update(sql, ps);
    }

    @Override
    public WMap searchUserByName(String name) {
        String sql = "select name from user where name=?";
        Ps ps = new Ps();
        ps.addString(name);
        return baseDao.getMap(sql, ps);
    }

    @Override
    public void updateUserPwd(Map updateUserVo) throws SQLException {
        //userKey, oldPwd, pwd, confirmPwd
        String sql = "update user set password=? where `key`=?";
        Ps ps = new Ps();
        ps.addString(updateUserVo.get("pwd").toString());
        ps.addInt(Integer.valueOf(updateUserVo.get("userKey").toString()));
        baseDao.update(sql, ps);
    }

    @Override
    public WMap getPwdById(String key) throws SQLException {
        String sql = "select password from user where `key`=?";
        Ps ps = new Ps();
        ps.addInt(Integer.valueOf(key));
        return baseDao.getMap(sql, ps);
    }


    @Override
    public void cancelAttention(Integer myKey, Integer attentionUserKey) throws SQLException {
        String sql = "delete from t_blog_attention where my_id=? and  attention_user_id=?";
        Ps ps = new Ps();
        ps.addInt(myKey);
        ps.addInt(attentionUserKey);
        baseDao.delete(sql, ps);
    }

    @Override
    public WMap isAttention(Integer myKey, Integer attentionUserKey) {
        String sql = "select my_id from t_blog_attention where my_id=? and  attention_user_id=?";
        Ps ps = new Ps();
        ps.addInt(myKey);
        ps.addInt(attentionUserKey);
        WMap map = baseDao.getMap(sql, ps);
        return map;
    }


    @Override
    public List<WMap> getUserCollectFiles(Integer key) throws SQLException {
        String sql = "select id ,user_id,name,blog_num from t_blog_collect_file  where user_id=?";
        Ps ps = new Ps();
        ps.addInt(key);
        return baseDao.getMapList(sql, ps);
    }

    @Override
    public void addCollectFile(Integer key, String name, Integer isPublic, String des) throws SQLException {
        String sql = "insert into t_blog_collect_file (user_id,name,is_public,des) values(?,?,?,?)";
        Ps ps = new Ps();
        ps.addInt(key);
        ps.addString(name);
        ps.addInt(isPublic);
        ps.addString(des);
        baseDao.save(sql, ps);
    }

    @Override
    public WMap isCollectBlog(Integer key, Integer blogId, Integer collectFileId) {
        String sql = "select id from t_blog_collect where user_id=? and blog_id=? and file_id=?  ";
        Ps ps = new Ps();
        ps.addInt(key);
        ps.addInt(blogId);
        ps.addInt(collectFileId);
        return baseDao.getMap(sql, ps);
    }


}
