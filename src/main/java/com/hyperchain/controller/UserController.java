/**
 * Copyright (C), 2016-2019, 趣链科技有限有限公司
 * FileName: UserController
 * Author:   lixuanfeng
 * Date:     2019/12/3 下午7:18
 * Description: 用户controller
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hyperchain.controller;

import com.hyperchain.base.WMap;
import com.hyperchain.dao.UserDao;
import com.hyperchain.response.BaseResult;
import com.hyperchain.response.Code;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈用户controller〉
 *
 * @author lixuanfeng
 * @create 2019/12/3
 * @since 1.0.0
 */

@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserController {
    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseResult login(@RequestBody Map userVO) throws SQLException {
        BaseResult baseResult = new BaseResult();
        log.info(userVO.toString());
        //{userName=李经纬, passWord=123456}
        WMap userMap = userDao.findUserByUserName((String) userVO.get("userName"));
        if (null == userMap) {
            baseResult.setMessage("用户不存在！");
            baseResult.setCode("000002");
            return baseResult;
        } else {
            String userId = String.valueOf(userMap.get("key"));
            String passWordDo = String.valueOf(userMap.get("password"));
            String passWord = (String) userVO.get("passWord");
            if ("" != passWord.trim() && passWord.equals(passWordDo)) {
                //用户名密码相同，返回登陆成功
                baseResult.setCode("0");
                userVO.put("key", userId);
                baseResult.setData(userVO);
                baseResult.setMessage("登陆成功！");
                return baseResult;
            } else {
                baseResult.setMessage("密码错误！");
                baseResult.setCode("000003");
                return baseResult;
            }
        }
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public BaseResult register(@RequestBody Map userVO) throws SQLException {
        log.info(userVO.toString());
        userDao.add(userVO);
        return BaseResult.response(Code.RETURN_SUCCESS);

    }

    @RequestMapping(value = "/getUserInfoById", method = RequestMethod.GET)
    public Map getUserInfoById(String id) throws SQLException {
        return userDao.getUserById(Integer.valueOf(id));
    }

    @RequestMapping(value = "/getUsers", method = RequestMethod.GET)
    public BaseResult getUsers() throws SQLException {
        List<WMap> userList = userDao.getUserList();
        return BaseResult.response(Code.RETURN_SUCCESS, userList);
    }


    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public BaseResult deleteUserById(String key) throws SQLException {
        int delete = userDao.delete(Integer.valueOf(key));
        return BaseResult.response(Code.RETURN_SUCCESS);
    }


}