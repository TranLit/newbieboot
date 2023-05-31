package com.ltb.newbieboot.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 用户
 *
 * @author 小李哞哞
 * @date 2023/5/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;

    private String password;

    private String nickname;

    private String phone;

    private Integer status;

    @TableField("avatar_url")
    private String avatarUrl;

    @TableField("create_time")
    private String createTime;

    @TableField(exist = false)
    private Role role;
}
