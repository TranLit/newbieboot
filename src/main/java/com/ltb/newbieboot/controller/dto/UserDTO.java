package com.ltb.newbieboot.controller.dto;

import com.ltb.newbieboot.entity.system.Menu;
import com.ltb.newbieboot.entity.system.User;
import lombok.*;

import java.util.List;

/**
 * @author 小李哞哞
 * @date 2023/5/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserDTO extends User {
    private String token;
    private List<Menu> menus;
    private String captcha;
    private String uuid;
}
