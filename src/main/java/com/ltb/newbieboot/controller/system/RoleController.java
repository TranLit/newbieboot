package com.ltb.newbieboot.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ltb.newbieboot.annotation.DemoEnabled;
import com.ltb.newbieboot.common.Result;
import com.ltb.newbieboot.common.StandardCode;
import com.ltb.newbieboot.entity.system.Role;
import com.ltb.newbieboot.service.system.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理
 *
 * @author 小李哞哞
 * @date 2023/5/24
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    /**
     * 模糊分页查找所有角色
     * @return
     */
    @GetMapping("/page")
    @DemoEnabled
    public Result list(Long pageNum, Long pageSize, Role role) {
        IPage<Role> roleList = roleService.queryLikePageList(pageNum, pageSize, role);
        return Result.success(roleList);
    }

    /**
     * 保存或修改角色信息
     * @param role
     * @return
     */
    @PostMapping
    public Result save(@RequestBody Role role) {
        boolean isOk = roleService.saveOrUpdate(role);
        if (isOk) {
            return Result.success();
        }
        return Result.error(StandardCode.CODE_600, "保存角色信息失败");
    }

    /**
     * 根据id删除角色信息，并且删除角色菜单关联表的记录
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id) {
        boolean isOk = roleService.removeByIdPlus(id);
        if (isOk) {
            return Result.success();
        }
        return Result.error(StandardCode.CODE_600, "删除角色失败");
    }

    /**
     * 批量删除，并且删除角色菜单关联表的记录
     * @param ids
     * @return
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        boolean isOk = roleService.removeBatchByIdsPlus(ids);
        if (isOk) {
            return Result.success();
        }
        return Result.error(StandardCode.CODE_600, "批量删除失败");
    }

    /**
     * 赋予角色访问菜单的权限
     * @param roleId
     * @param menuIds
     * @return
     */
    @PostMapping("/grant/{roleId}")
    public Result grant(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
        roleService.grant(roleId, menuIds);
        return Result.success();
    }

    /**
     * 查询角色所有的menuId
     * @param roleId
     * @return
     */
    @GetMapping("/{roleId}")
    @DemoEnabled
    public Result getMenuIdsByRoleId(@PathVariable Integer roleId) {
        List<Integer> menuIds = roleService.getMenuIdsByRoleId(roleId);
        return Result.success(menuIds);
    }

    /**
     * 查询所有的role
     * @return
     */
    @GetMapping
    @DemoEnabled
    public Result queryList() {
        return Result.success(roleService.list());
    }
}
