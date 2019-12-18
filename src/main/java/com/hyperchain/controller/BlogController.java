/**
 * Copyright (C), 2016-2019, 趣链科技有限有限公司
 * FileName: BokeController
 * Author:   lixuanfeng
 * Date:     2019/12/12 下午6:46
 * Description: 博客控制器层
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hyperchain.controller;

import com.hyperchain.base.WMap;
import com.hyperchain.dao.BlogDao;
import com.hyperchain.response.BaseResult;
import com.hyperchain.response.Code;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈博客控制器层〉
 *
 * @author lixuanfeng
 * @create 2019/12/12
 * @since 1.0.0
 */
@RestController
@RequestMapping(value = "/blog")
@Slf4j
public class BlogController {
    @Autowired
    private BlogDao blogDao;

    @PostMapping(value = "/addBlog")
    public BaseResult addBlog(@RequestBody Map map) throws SQLException {
        blogDao.add(map);
        return BaseResult.response(Code.RETURN_SUCCESS);
    }

    @GetMapping(value = "/getBlogs")
    public BaseResult getBlogs() throws SQLException {
        List<WMap> blogs = blogDao.getBlogs();
        return BaseResult.response(Code.RETURN_SUCCESS, blogs);
    }

    @GetMapping(value = "/getBlogDetailById")
    public BaseResult getBlogDetailById(String id) {
        WMap blog = blogDao.getBlogDetailById(id);
        return BaseResult.response(Code.RETURN_SUCCESS, blog);

    }

    @GetMapping(value = "/addBlogComment")
    public BaseResult addBlogComment(String id, String comment) throws SQLException {
        if ("".equals(comment.trim())) {
            return BaseResult.response("000001", "评论内容不能为空", "");
        }
        blogDao.addBlogComment(id, comment);
        return BaseResult.response(Code.RETURN_SUCCESS);

    }

    /**
     * 通过博客id，获取评论列表
     *
     * @param id
     * @return
     * @throws SQLException
     */
    @GetMapping(value = "/getBlogComments")
    public BaseResult getBlogComments(String id) throws SQLException {
        List<WMap> blogComments = blogDao.getBlogComments(id);
        return BaseResult.response(Code.RETURN_SUCCESS, blogComments);
    }

    @GetMapping(value = "/clickStar")
    public BaseResult clickStar(String userId, String blogId, boolean clickOrCancle) throws SQLException {
        if (!clickOrCancle) {
            //当为false时 进行点赞
            blogDao.clickStar(userId, blogId);
        } else {
            //当为true 取消点赞
            blogDao.cancleStar(userId, blogId);
        }
        return BaseResult.response(Code.RETURN_SUCCESS);
    }

    /**
     * 判断某用户是否对某博客点过赞
     *
     * @param userId
     * @param blogId
     * @return
     * @throws SQLException
     */
    @GetMapping(value = "/isClickStar")
    public BaseResult isClickStar(String userId, String blogId) throws SQLException {
        WMap isClickStar = blogDao.isClickStar(userId, blogId);
        if (null == isClickStar) {
            return BaseResult.response(Code.RETURN_FALSE);
        }
        return BaseResult.response(Code.RETURN_SUCCESS);
    }


}