package com.ltb.newbieboot.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltb.newbieboot.entity.system.Menu;
import com.ltb.newbieboot.mapper.system.MenuMapper;
import com.ltb.newbieboot.mapper.system.RoleMenuMapper;
import com.ltb.newbieboot.service.system.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 小李哞哞
 * @date 2023/5/23
 */
@Service
public class MenuService extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<Menu> queryList() {
        // 查询所有数据
        List<Menu> list = list();
        // 找出pid为null的一级菜单
        List<Menu> parentMenus = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
        // 找出一级菜单的子菜单
        for (Menu parentMenu : parentMenus) {
            parentMenu.setChildren(list.stream().filter(m -> parentMenu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return parentMenus;
    }

    @Override
    @Transactional
    public boolean removeByIdPlus(Integer id) {
        removeById(id);
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("menu_id", id);
        roleMenuMapper.deleteByMap(columnMap);
        return true;
    }

    @Override
    public boolean removeBatchByIdsPlus(List<Integer> ids) {
        removeBatchByIds(ids);
        Map<String, Object> columnMap = new HashMap<>();
        ids.forEach(id -> {
            columnMap.put("menu_id", id);
            roleMenuMapper.deleteByMap(columnMap);
            columnMap.clear();
        });
        return true;
    }
}
