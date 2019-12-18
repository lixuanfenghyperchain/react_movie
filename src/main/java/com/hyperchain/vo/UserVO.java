/**
 * Copyright (C), 2016-2019, 趣链科技有限有限公司
 * FileName: UserVO
 * Author:   lixuanfeng
 * Date:     2019/12/4 下午12:19
 * Description: 用户试图层实体
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hyperchain.vo;

import lombok.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈用户试图层实体〉
 *
 * @author lixuanfeng
 * @create 2019/12/4
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserVO {

    private String userName;
    private String passWord;
    private String mail;
    private String cellPhone;

}