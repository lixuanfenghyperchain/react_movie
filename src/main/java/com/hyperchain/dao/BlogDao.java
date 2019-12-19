/**
 * Copyright (C), 2016-2019, 趣链科技有限有限公司
 * FileName: BlogDao
 * Author:   lixuanfeng
 * Date:     2019/12/13 下午4:48
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
 * @create 2019/12/13
 * @since 1.0.0
 */
public interface BlogDao {
    public long add(Map blog) throws SQLException;

    public List<WMap> getBlogs() throws SQLException;

    WMap getBlogDetailById(String id);

    void addBlogComment(String id, String content, int userId, String userName) throws SQLException;

    List<WMap> getBlogComments(String id) throws SQLException;

    void clickStar(String userId, String blogId) throws SQLException;

    WMap isClickStar(String userId, String blogId);

    void cancleStar(String userId, String blogId) throws SQLException;
}