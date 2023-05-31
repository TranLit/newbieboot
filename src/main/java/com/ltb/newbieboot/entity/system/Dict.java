package com.ltb.newbieboot.entity.system;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 通用字典
 *
 * @author 小李哞哞
 * @date 2023/5/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@TableName("sys_dict")
public class Dict {
    private String name;
    private String value;
    private String type;
}
