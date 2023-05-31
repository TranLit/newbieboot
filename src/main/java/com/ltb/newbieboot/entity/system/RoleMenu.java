package com.ltb.newbieboot.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 角色菜单关系
 *
 * @author 小李哞哞
 * @date 2023/5/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@TableName("sys_role_menu")
public class RoleMenu {
    @TableField("role_id")
    private Integer roleId;
    @TableField("menu_id")
    private Integer menuId;
}
