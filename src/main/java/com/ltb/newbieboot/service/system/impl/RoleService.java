package com.ltb.newbieboot.service.system.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltb.newbieboot.entity.system.Menu;
import com.ltb.newbieboot.entity.system.Role;
import com.ltb.newbieboot.entity.system.RoleMenu;
import com.ltb.newbieboot.mapper.system.RoleMapper;
import com.ltb.newbieboot.mapper.system.RoleMenuMapper;
import com.ltb.newbieboot.service.system.IMenuService;
import com.ltb.newbieboot.service.system.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 小李哞哞
 * @date 2023/5/23
 */
@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private IMenuService menuService;

    @Override
    public IPage<Role> queryLikePageList(Long pageNum, Long pageSize, Role role) {
        Page<Role> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        String name = role.getName();
        queryWrapper.like(!StrUtil.isBlank(name), Role::getName, name);
        queryWrapper.orderByDesc(Role::getId);
        return page(page, queryWrapper);
    }

    @Override
    @Transactional
    public void grant(Integer roleId, List<Integer> menuIds) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("role_id", roleId);
        roleMenuMapper.deleteByMap(columnMap);
        for (Integer menuId : menuIds) {
            RoleMenu roleMenu = new RoleMenu(roleId, menuId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    @Override
    public List<Integer> getMenuIdsByRoleId(Integer roleId) {
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, roleId);
        List<RoleMenu> roleMenus = roleMenuMapper.selectList(queryWrapper);
        if (!roleMenus.isEmpty()) {
            List<Integer> menuIds = new ArrayList<>();
            roleMenus.forEach(m -> menuIds.add(m.getMenuId()));
            // 将叶子节点的id筛选出来，若不筛选，则前端可能会出现全选的情况
            LambdaQueryWrapper<Menu> queryMenuWrapper = new LambdaQueryWrapper<>();
            queryMenuWrapper.in(Menu::getId, menuIds);
            List<Menu> menuList = menuService.list(queryMenuWrapper);
            menuIds.clear();
            menuList.forEach(menu -> {
                if (!StrUtil.isBlank(menu.getPath())) {
                    menuIds.add(menu.getId());
                }
            });
            return menuIds;
        }
        return null;
    }

    /**
     * 根据id删除角色信息，并且删除角色菜单关联表的记录
     * @param id
     * @return
     */
    @Override
    @Transactional
    public boolean removeByIdPlus(Integer id) {
        removeById(id);
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("role_id", id);
        roleMenuMapper.deleteByMap(columnMap);
        return true;
    }

    /**
     * 批量删除，并且删除角色菜单关联表的记录
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public boolean removeBatchByIdsPlus(List<Integer> ids) {
        removeBatchByIds(ids);
        Map<String, Object> columnMap = new HashMap<>();
        ids.forEach(id -> {
            columnMap.put("role_id", id);
            roleMenuMapper.deleteByMap(columnMap);
            columnMap.clear();
        });
        return true;
    }
}
