/**
 * Copyright (C), 2016-2018, 趣链科技有限有限公司
 * FileName: BaseResult
 * Author:   lixuanfeng
 * Date:     2018/7/15 下午5:12
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hyperchain.response;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author lixuanfeng
 * @create 2018/7/15
 * @since 1.0.0
 */
public class BaseResult<T> {
    //返回的响应码
    private String code;
    //返回的响应信息
    private String message;
    //返回的数据
    private T data;

    public BaseResult() {
    }


    public static BaseResult response(Code code) {
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(code.getCode());
        baseResult.setMessage(code.getMsg());
        return baseResult;
    }


    public static <T> BaseResult response(Code code, T data) {
        BaseResult baseResult = response(code);
        baseResult.setData(data);
        return baseResult;
    }

    public static <T> BaseResult response(String code, String msg, T data) {
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(code);
        baseResult.setMessage(msg);
        baseResult.setData(data);
        return baseResult;

    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}