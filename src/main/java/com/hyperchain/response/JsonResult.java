/**
 * Copyright (C), 2016-2019, 趣链科技有限有限公司
 * FileName: JsonResult
 * Author:   lixuanfeng
 * Date:     2019/12/6 下午4:46
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hyperchain.response;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author lixuanfeng
 * @create 2019/12/6
 * @since 1.0.0
 */
@Data
public class JsonResult implements Serializable {

    private int code;   //返回码 非0即失败
    private String message; //消息提示
    private Map<String, Object> data; //返回的数据

    public JsonResult() {
    }

    ;

    public JsonResult(int code, String msg, Map<String, Object> data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    public static String success() {
        return success(new HashMap(0));
    }

    public static String success(Map<String, Object> data) {
        return JSON.toJSONString(new JsonResult(0, "成功", data));
    }

    public static String failed() {
        return failed("失败");
    }

    public static String failed(String msg) {
        return failed(-1, msg);
    }

    public static String failed(int code, String msg) {
        return JSON.toJSONString(new JsonResult(code, msg, new HashMap(0)));
    }

}