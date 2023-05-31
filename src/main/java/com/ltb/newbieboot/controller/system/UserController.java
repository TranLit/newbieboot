package com.ltb.newbieboot.controller.system;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltb.newbieboot.annotation.DemoEnabled;
import com.ltb.newbieboot.common.Result;
import com.ltb.newbieboot.common.StandardCode;
import com.ltb.newbieboot.controller.dto.UserDTO;
import com.ltb.newbieboot.entity.system.User;
import com.ltb.newbieboot.service.system.IUserService;
import com.ltb.newbieboot.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户管理
 *
 * @author 小李哞哞
 * @date 2023/5/21
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;


    /**
     * 模糊分页查找所有用户
     * @return
     */
    @GetMapping("/page")
    @DemoEnabled
    public Result list(Long pageNum, Long pageSize, User user) {
        IPage<User> userList = userService.queryLikePageList(pageNum, pageSize, user);
        return Result.success(userList);
    }

    /**
     * 用户登录
     * @param userDTO
     * @return
     */
    @PostMapping("/login")
    @DemoEnabled
    public Result login(@RequestBody UserDTO userDTO) {
        userDTO = userService.login(userDTO);
        return Result.success(userDTO);
    }

    /**
     * 新增或修改用户信息
     * @param user
     * @return
     */
    @PostMapping
    public Result save(@RequestBody User user) {
        boolean isOk = userService.saveOrUpdatePlus(user);
        if (isOk) {
            return Result.success();
        }
        return Result.error(StandardCode.CODE_600, "保存用户信息失败");
    }

    /**
     * 根据id删除用户信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id) {
        boolean isOk = userService.removeByIdPlus(id);
        if (isOk) {
            return Result.success();
        }
        return Result.error(StandardCode.CODE_600, "删除用户失败");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        boolean isOk = userService.removeBatchByIdsPlus(ids);
        if (isOk) {
            return Result.success();
        }
        return Result.error(StandardCode.CODE_600, "批量删除失败");
    }

    /**
     * 获取当前登录的用户
     * @return
     */
    @GetMapping("/getCurUser")
    @DemoEnabled
    public Result getCurUser() {
        User user = TokenUtil.getCurrentUser();
        return Result.success(user);
    }

    /**
     * 上传头像
     * @param avatar
     * @param user
     * @return
     */
    @PostMapping("/uploadAvatar")
    public Result uploadAvatar(MultipartFile avatar, User user) {
        user = userService.uploadAvatar(avatar, user);
        if (ObjUtil.isEmpty(user)) {
            return Result.error(StandardCode.CODE_600, "更新数据库失败");
        }
        return Result.success(user.getAvatarUrl());
    }
}
