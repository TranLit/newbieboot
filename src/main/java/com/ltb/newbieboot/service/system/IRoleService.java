package com.ltb.newbieboot.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ltb.newbieboot.entity.system.Role;

import java.util.List;

/**
 * @author 小李哞哞
 * @date 2023/5/23
 */
public interface IRoleService extends IService<Role> {
    IPage<Role> queryLikePageList(Long pageNum, Long pageSize, Role role);

    /**
     * 赋予角色访问菜单的权限，先删除原有权限，后新增权限
     * @param roleId
     * @param menuIds
     */
    void grant(Integer roleId, List<Integer> menuIds);

    List<Integer> getMenuIdsByRoleId(Integer roleId);

    boolean removeByIdPlus(Integer id);

    boolean removeBatchByIdsPlus(List<Integer> ids);
}
