package com.ltb.newbieboot.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 角色
 *
 * @author 小李哞哞
 * @date 2023/5/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@TableName("sys_role")
public class Role {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    @TableField("role_key")
    private String roleKey;
}
