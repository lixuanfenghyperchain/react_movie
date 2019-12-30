/**
 * Copyright (C), 2016-2019, 趣链科技有限有限公司
 * FileName: BlogDaoImpl
 * Author:   lixuanfeng
 * Date:     2019/12/13 下午4:48
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hyperchain.dao.impl;

import com.hyperchain.base.Ps;
import com.hyperchain.base.WMap;
import com.hyperchain.base.dao.IBaseDao;
import com.hyperchain.dao.BlogDao;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author lixuanfeng
 * @create 2019/12/13
 * @since 1.0.0
 */
@Repository
public class BlogDaoImpl implements BlogDao {
    @Autowired
    private IBaseDao baseDao;  //这个是系统自带的

    @Override
    @Transactional
    public long add(Map blog) throws SQLException {
        String sql = "insert into t_blog_article (title,summary,create_time,type,images_url,create_user_id) values  (?,?,?,?,?,?)";
        Ps ps = new Ps();
        ps.addString((String) blog.get("name"));
        ps.addString((String) blog.get("desc"));
        ps.addString(String.valueOf(new Date().getTime()));
        ps.addString((String) blog.get("types"));
        List imgsUrl = (List) blog.get("imgs");
        ps.addString(imgsUrl.get(0).toString());
        ps.addInt(Integer.valueOf((String) blog.get("create_user_id")));
        //返回插入博客标题的主键
        long article_id = baseDao.insertReturnId(sql, ps);
        String sql1 = "insert into t_blog_content (article_id,article_content) values (?,?)";
        Ps ps1 = new Ps();
        ps1.addInt((int) article_id);
        ps1.addString(EmojiParser.removeAllEmojis((String) blog.get("detail")));
        baseDao.save(sql1, ps1);
        return article_id;
    }

    @Override
    public List<WMap> getBlogs() throws SQLException {
//        String sql = "select id,star_num,title,summary as des ,create_time as time,type,images_url as url from t_blog_article  order by create_time desc ";
        String sql = "select id,star_num,title,summary as des ,b.create_time as time," +
                "   type,images_url as url ,user.avatar_url,b.create_user_id ,comment_num,read_num  " +
                "   from t_blog_article  b , user WHERE user.key=b.create_user_id  order by b.create_time desc";
        return baseDao.getMapList(sql);
    }

    @Override
    public List<WMap> getTop10Blogs() throws SQLException {
        String sql = "select id,user.key as userKey,star_num,title,summary as des ,b.create_time as time," +
                "   type,images_url as url ,user.avatar_url,b.create_user_id ,user.name,comment_num,read_num  " +
                "   from t_blog_article  b , user WHERE user.key=b.create_user_id  order by read_num desc  LIMIT 10;";
        return baseDao.getMapList(sql);
    }

    @Override
    public List<WMap> getUserBlogsById(String userId) throws SQLException {
        String sql = "select id,title,summary as des ,b.create_time as time,read_num,star_num,comment_num  " +
                "   from t_blog_article  b  where b.create_user_id=?  order by b.create_time desc";
        Ps ps = new Ps();
        ps.addInt(Integer.valueOf(userId));
        return baseDao.getMapList(sql, ps);
    }

    @Override
    public WMap getBlogDetailById(String id) throws SQLException {
        //查看一次 更新阅读量
        String sql1 = "update t_blog_article set read_num=read_num+1 where id=?";
        Ps ps1 = new Ps();
        ps1.addInt(Integer.valueOf(id));
        baseDao.update(sql1, ps1);
        //查看详情
        String sql = "select a.id,title,summary as des ,star_num,read_num,create_time as time,type, article_content as content from t_blog_article a ,t_blog_content c where a.id=c.article_id and a.id=?";
        Ps ps = new Ps();
        ps.addInt(Integer.valueOf(id));
        return baseDao.getMap(sql, ps);
    }

    @Override
    @Transactional
    public void addBlogComment(String id, String content, int userId, String userName) throws SQLException {
        String sql = "insert into t_blog_comment (com_blog_id,com_content,com_time,com_person_id,com_person_name) values (?,?,?,?,?)";
        Long com_time = new Date().getTime();
        Ps ps = new Ps();
        ps.addInt(Integer.valueOf(id));
        ps.addString(content);
        ps.addString(com_time.toString());
        ps.addInt(userId);
        ps.addString(userName);
        //更新博客表中的评论数量
        String sql1 = "update t_blog_article set comment_num=comment_num+1 where id=?";
        Ps ps1 = new Ps();
        ps1.addInt(Integer.valueOf(id));
        baseDao.save(sql, ps);
        baseDao.update(sql1, ps1);
    }

    @Override
    public List<WMap> getBlogComments(String id) throws SQLException {
//        String sql = "select id,com_content,com_time ,com_person_name,com_person_id from t_blog_comment where com_blog_id=? order by com_time desc";
        String sql = "select id,com_content,com_time ,com_person_name,com_person_id , `user`.avatar_url from t_blog_comment ,`user` where `key`=com_person_id and com_blog_id=? order by com_time desc";
        Ps ps = new Ps();
        ps.addInt(Integer.valueOf(id));
        return baseDao.getMapList(sql, ps);
    }

    @Override
    @Transactional
    public void clickStar(String userId, String blogId) throws SQLException {
        String sql = "insert into t_user_blog_star (user_id,blog_id) values (?,?)";
        Ps ps = new Ps();
        ps.addInt(Integer.valueOf(userId));
        ps.addInt(Integer.valueOf(blogId));
        baseDao.save(sql, ps);
        String sql1 = "update  t_blog_article  set star_num= star_num+1 where id=?";
        Ps ps1 = new Ps();
        ps1.addInt(Integer.valueOf(blogId));
        baseDao.save(sql1, ps1);
    }

    @Override
    public WMap isClickStar(String userId, String blogId) {
        String sql = "select * from t_user_blog_star where user_id=? and blog_id =? ";
        Ps ps = new Ps();
        ps.addInt(Integer.valueOf(userId));
        ps.addInt(Integer.valueOf(blogId));
        return baseDao.getMap(sql, ps);
    }

    @Override
    @Transactional
    public void cancleStar(String userId, String blogId) throws SQLException {
        String sql = "delete from t_user_blog_star   where user_id=? and blog_id =?";
        Ps ps = new Ps();
        ps.addInt(Integer.valueOf(userId));
        ps.addInt(Integer.valueOf(blogId));
        baseDao.save(sql, ps);
        String sql1 = "update  t_blog_article  set star_num= star_num-1 where id=?";
        Ps ps1 = new Ps();
        ps1.addInt(Integer.valueOf(blogId));
        baseDao.save(sql1, ps1);
    }

    @Override
    @Transactional
    public void collectBlog(String userId, String blogId, String collectFileId, String collectFileName) throws SQLException {
        String sql = "insert into t_blog_collect (user_id,blog_id,file_id,file_name) values (?,?,?,?)";
        Ps ps = new Ps();
        ps.addInt(Integer.valueOf(userId));
        ps.addInt(Integer.valueOf(blogId));
        ps.addInt(Integer.valueOf(collectFileId));
        ps.addString(collectFileName);
        baseDao.save(sql, ps);
        //更新收藏夹表，
        String sql1 = "update t_blog_collect_file set blog_num=blog_num+1 where id=?";
        Ps ps1 = new Ps();
        ps1.addInt(Integer.valueOf(collectFileId));
        baseDao.save(sql1, ps1);
    }


    @Override
    @Transactional
    public void cancelCollectBlog(String userId, String blogId, String collectFileId) throws SQLException {
        String sql = "delete  from  t_blog_collect where user_id=? and blog_id=? and file_id=?  ";
        Ps ps = new Ps();
        ps.addInt(Integer.valueOf(userId));
        ps.addInt(Integer.valueOf(blogId));
        ps.addInt(Integer.valueOf(collectFileId));
        baseDao.delete(sql, ps);
        //更新收藏夹表，
        String sql1 = "update t_blog_collect_file set blog_num=blog_num-1 where id=?";
        Ps ps1 = new Ps();
        ps1.addInt(Integer.valueOf(collectFileId));
        baseDao.update(sql1, ps1);
    }

    @Override
    public List<WMap> getCollectedBlogs(String userId, String fileType) throws SQLException {
//        String sql = "select id,title,star_num,summary as des from t_blog_article  WHERE id in (select blog_id FROM t_blog_collect where user_id=?) order by create_time desc";
        String sql = "select id,title,star_num,summary  as des ,user.avatar_url  from t_blog_article b ,`user` WHERE b.create_user_id=user.key  " +
                "   and b.id in(select DISTINCT blog_id FROM t_blog_collect where user_id=? and file_id=?)  order by b.create_time desc";
        Ps ps = new Ps();
        ps.addInt(Integer.valueOf(userId));
        ps.addInt(Integer.valueOf(fileType));
        return baseDao.getMapList(sql, ps);
    }

    @Override
    public List<WMap> fuzzySearchBlogs(String name) throws SQLException {
        String sql = "select id,star_num,title,summary as des ,b.create_time as time," +
                "   type,images_url as url ,user.avatar_url,b.create_user_id ,comment_num,read_num  " +
                "   from t_blog_article  b , user WHERE user.key=b.create_user_id and title like '%" + name + "%' order by b.create_time desc ";
        return baseDao.getMapList(sql);
    }

}