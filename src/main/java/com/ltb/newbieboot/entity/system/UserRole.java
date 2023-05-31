package com.ltb.newbieboot.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 用户角色关系
 *
 * @author 小李哞哞
 * @date 2023/5/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@TableName("sys_user_role")
public class UserRole {
    @TableField("user_id")
    private Integer userId;
    @TableField("role_id")
    private Integer roleId;
}
