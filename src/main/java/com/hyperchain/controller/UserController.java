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
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.*;

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
                userVO.put("avatar_url", userMap.get("avatar_url"));
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
        //根据用户名获取用户 ，如何存在不给予注册
        if (userDao.searchUserByName(userVO.get("name").toString()) != null) {
            return BaseResult.response(Code.USER_EXIT);
        }
        userDao.add(userVO);
        return BaseResult.response(Code.RETURN_SUCCESS);

    }

    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    public BaseResult updateUserInfo(@RequestBody Map updateUserVO) throws SQLException {
        log.info(updateUserVO.toString());
        //根据用户名获取用户 ，如何存在不给予注册
        userDao.updateUserInfo(updateUserVO);
        return BaseResult.response(Code.RETURN_SUCCESS);

    }

    @RequestMapping(value = "/getUserInfoById", method = RequestMethod.GET)
    public BaseResult getUserInfoById(String key) throws SQLException {
        WMap userInfoById = userDao.getUserInfoById(key);
        // 粉丝数量、
        int fansNum = userDao.getFans(Integer.valueOf(key)).size();
        //关注数量
        int attentionSize = userDao.attentionPersons(Integer.valueOf(key)).size();
        userInfoById.setInt("fans_total", fansNum);
        userInfoById.setInt("attention_total", attentionSize);
        return BaseResult.response(Code.RETURN_SUCCESS, userInfoById);
    }

    @GetMapping(value = "/getUserTotalInfoByid")
    public BaseResult getUserTotalInfoByid(Integer key) throws SQLException {
        //用户基本信息
        WMap userInfo = userDao.getUserInfoById(key.toString());
        //用户博客数量、博客获赞总数、博客评论总数、博客访问量总数、
        WMap totalMap = userDao.getUserTotalInfoByid(key);
        // 粉丝数量、
        int fansNum = userDao.getFans(key).size();
        //关注数量
        int attentionSize = userDao.attentionPersons(key).size();
        totalMap.setInt("fans_total", fansNum);
        totalMap.setInt("attention_total", attentionSize);
        userInfo.putAll(totalMap);
        userInfo.setInt("userKey", Integer.valueOf(userInfo.get("key").toString()));
        userInfo.remove("key");
        return BaseResult.response(Code.RETURN_SUCCESS, userInfo);
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

    //更新用户头像
    @RequestMapping(value = "/updateUserAvatar", method = RequestMethod.GET)
    public BaseResult updateUserAvatar(String key, String avatarUrl) throws SQLException {
        userDao.updateUserAvatar(Integer.valueOf(key), avatarUrl);
        return BaseResult.response(Code.RETURN_SUCCESS);
    }

    //关注用户
    @GetMapping(value = "/attentionPersonReq")
    public BaseResult attentionPersonReq(Integer myKey, Integer attentionUserKey) throws SQLException {
        //先查询用户详情，然后再把用户信息添加到关注表中
        WMap attentionUserInfo = userDao.getUserInfoById(attentionUserKey.toString());
        // `key`,name as userName ,age,address,mail,cellPhone,avatar_url,user_des
        String username = attentionUserInfo.getString("username");
        String avatar_url = attentionUserInfo.getString("avatar_url");
        userDao.attentionPerson(myKey, attentionUserKey, username, avatar_url);
        return BaseResult.response(Code.RETURN_SUCCESS);
    }

    //关注列表
    @GetMapping(value = "/attentionPersonsReq")
    public BaseResult attentionPersonsReq(Integer myKey) throws SQLException {
        List<WMap> attentionPersons = userDao.attentionPersons(myKey);
        return BaseResult.response(Code.RETURN_SUCCESS, attentionPersons);
    }

    //获取我的粉丝
    @GetMapping(value = "/getFans")
    public BaseResult getFans(Integer key) throws SQLException {

        List<WMap> fans = userDao.getFans(key);
        // 38 41 45

        List<WMap> attentionPersons = userDao.attentionPersons(key);
        // 38 41 45

        Set<Integer> sames = new HashSet<Integer>();  //用来存放两个数组中相同的元素


        Set<Integer> temArr = new HashSet<>();  // 粉丝和关注人的id都往临时数组里面放，如何放不进去 说明 该粉丝我已经关注过。  135
        for (WMap fan : fans) {
            Integer id = fan.getInt("id");
            temArr.add(id);
        }

        for (WMap attention : attentionPersons) {
            Integer id = attention.getInt("oid");
            if (!temArr.add(id))
                sames.add(id);
        }

        for (WMap fan : fans) {   //38 41 45
            fan.setInt("is_attent", 0); //首先都设置为未关注
            for (Integer same : sames) { //45 41 38
                if (fan.getInt("id") == same) { //找到一个相同的id 设置未关注 并跳出
                    fan.setInt("is_attent", 1);  //已经关注
                    break;
                }
            }
        }


        return BaseResult.response(Code.RETURN_SUCCESS, fans);
    }

    //关注用户
    @GetMapping(value = "/cancelAttentionReq")
    public BaseResult cancelAttentionReq(Integer myKey, Integer attentionUserKey) throws SQLException {
        //先查询用户详情，然后再把用户信息添加到关注表中
        userDao.cancelAttention(myKey, attentionUserKey);
        return BaseResult.response(Code.RETURN_SUCCESS);
    }

    //是否关注
    @GetMapping(value = "/isAttentionReq")
    public BaseResult isAttentionReq(Integer myKey, Integer attentionUserKey) throws SQLException {
        //先查询用户详情，然后再把用户信息添加到关注表中
        WMap attention = userDao.isAttention(myKey, attentionUserKey);
        if (null == attention) {
            //没关注
            return BaseResult.response(Code.RETURN_SUCCESS, "0");
        }
        //已经关注
        return BaseResult.response(Code.RETURN_SUCCESS, "1");
    }

    //获取收藏夹
    @GetMapping(value = "/getUserCollectFiles")
    public BaseResult getUserCollectFiles(Integer key, Integer blogId) throws SQLException {
        List<WMap> collectFiles = userDao.getUserCollectFiles(key);
        // 遍历博客文件id ，判断
        for (WMap map : collectFiles) {
            Integer collectFileId = map.getInt("id");
            // key 、blogId 、  collectFileId 三者去数据库查看是否在该类型下收藏过该博客
            WMap wMap = userDao.isCollectBlog(key, blogId, collectFileId);
            if (null == wMap) {
                //说明没有收藏
                map.setInt("is_collect", 0);
            } else {
                map.setInt("is_collect", 1);
            }
        }
        return BaseResult.response(Code.RETURN_SUCCESS, collectFiles);
    }

    //添加收藏夹
    @GetMapping(value = "/addCollectFile")
    public BaseResult addCollectFile(Integer key, String name, Integer isPublic, String des) throws SQLException {
        userDao.addCollectFile(key, name, isPublic, des);
        return BaseResult.response(Code.RETURN_SUCCESS);
    }

    //添加收藏夹
    @GetMapping(value = "/searchUserByNameReq")
    public BaseResult searchUserByNameReq(String name) throws SQLException {
        WMap user = userDao.searchUserByName(name);
        if (null != user) {
            return BaseResult.response(Code.USER_EXIT);
        } else {
            return BaseResult.response(Code.RETURN_SUCCESS);

        }
    }

    //更新用密码
    @PostMapping(value = "/updateUserPwd")
    public BaseResult updateUserPwd(@RequestBody Map updateUserVo) throws SQLException {
        //userKey, oldPwd, pwd, confirmPwd
        String pwd = updateUserVo.get("pwd").toString();
        String confirmPwd = updateUserVo.get("confirmPwd").toString();
        String oldPwd = updateUserVo.get("oldPwd").toString();
        if (!pwd.equals(confirmPwd)) {
            return BaseResult.response(Code.PASSWORD_NOT_EQUAL);
        }
        //判断该用户的旧密码是否正确
        WMap userInfo = userDao.getPwdById(updateUserVo.get("userKey").toString());
        if (!oldPwd.equals(userInfo.getString("password"))) {
            return BaseResult.response(Code.PASSWORD_ERROR);
        }
        userDao.updateUserPwd(updateUserVo);
        return BaseResult.response(Code.RETURN_SUCCESS);

    }

}