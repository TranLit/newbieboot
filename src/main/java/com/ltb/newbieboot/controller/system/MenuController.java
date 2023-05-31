package com.ltb.newbieboot.controller.system;

import com.ltb.newbieboot.annotation.DemoEnabled;
import com.ltb.newbieboot.common.Result;
import com.ltb.newbieboot.common.StandardCode;
import com.ltb.newbieboot.entity.system.Menu;
import com.ltb.newbieboot.service.system.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理
 *
 * @author 小李哞哞
 * @date 2023/5/24
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @GetMapping
    @DemoEnabled
    public Result queryList() {
        List<Menu> menus = menuService.queryList();
        return Result.success(menus);
    }

    /**
     * 保存或修改菜单信息
     * @param menu
     * @return
     */
    @PostMapping
    public Result save(@RequestBody Menu menu) {
        boolean isOk = menuService.saveOrUpdate(menu);
        if (isOk) {
            return Result.success();
        }
        return Result.error(StandardCode.CODE_600, "保存菜单信息失败");
    }

    /**
     * 根据id删除菜单信息，并且删除角色菜单关联表的数据
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id) {
        boolean isOk = menuService.removeByIdPlus(id);
        if (isOk) {
            return Result.success();
        }
        return Result.error(StandardCode.CODE_600, "删除菜单失败");
    }

    /**
     * 批量删除，并且删除角色菜单关联表的数据
     * @param ids
     * @return
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        boolean isOk = menuService.removeBatchByIdsPlus(ids);
        if (isOk) {
            return Result.success();
        }
        return Result.error(StandardCode.CODE_600, "批量删除失败");
    }
}
