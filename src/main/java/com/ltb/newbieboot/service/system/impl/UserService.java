package com.ltb.newbieboot.service.system.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltb.newbieboot.common.CacheConstants;
import com.ltb.newbieboot.common.StandardCode;
import com.ltb.newbieboot.controller.dto.UserDTO;
import com.ltb.newbieboot.entity.system.*;
import com.ltb.newbieboot.exception.ServiceException;
import com.ltb.newbieboot.mapper.system.RoleMapper;
import com.ltb.newbieboot.mapper.system.RoleMenuMapper;
import com.ltb.newbieboot.mapper.system.UserMapper;
import com.ltb.newbieboot.mapper.system.UserRoleMapper;
import com.ltb.newbieboot.service.system.IMenuService;
import com.ltb.newbieboot.service.system.IRoleService;
import com.ltb.newbieboot.service.system.IUserService;
import com.ltb.newbieboot.utils.OSSUtil;
import com.ltb.newbieboot.utils.RedisUtil;
import com.ltb.newbieboot.utils.TokenUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author 小李哞哞
 * @date 2023/5/23
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public IPage<User> queryLikePageList(Long pageNum, Long pageSize, User user) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        String username = user.getUsername();
        String nickname = user.getNickname();
        String phone = user.getPhone();
        queryWrapper.like(!StrUtil.isBlank(username), User::getUsername, username);
        queryWrapper.like(!StrUtil.isBlank(nickname), User::getNickname, nickname);
        queryWrapper.like(!StrUtil.isBlank(phone), User::getPhone, phone);
        queryWrapper.orderByDesc(User::getId);

        Page<User> userPage = page(page, queryWrapper);
        List<User> records = userPage.getRecords();
        Map<String, Object> columnMap = new HashMap<>();
        for (User item : records) {
            item.setRole(new Role(-1, "角色暂未分配", "NULL"));
            columnMap.put("user_id", item.getId());
            List<UserRole> userRoleList = userRoleMapper.selectByMap(columnMap);
            if (!userRoleList.isEmpty()) {
                UserRole userRole = userRoleList.get(0);
                Role role = roleService.getById(userRole.getRoleId());
                item.setRole(role);
            }
            columnMap.clear();
        }
        return userPage;
    }

    @Override
    @Transactional
    public UserDTO login(UserDTO userDTO) {

        if (ObjUtil.isEmpty(userDTO)) {
            throw new ServiceException(StandardCode.CODE_400, "参数异常");
        }

        // 校验验证码
        String captcha = userDTO.getCaptcha();
        String uuid = userDTO.getUuid();
        userDTO.setCaptcha(null);
        if (StrUtil.isBlank(captcha)) {
            throw new ServiceException(StandardCode.CODE_600, "验证码不能为空");
        }
        Object o = redisUtil.get(CacheConstants.CAPTCHA_CODE_KEY + uuid);
        if (ObjUtil.isEmpty(o)) {
            throw new ServiceException(StandardCode.CODE_600, "验证码已过期，请重试");
        }
        String correctCaptcha = o.toString();
        if (!captcha.equals(correctCaptcha)) {
            throw new ServiceException(StandardCode.CODE_600, "请输入正确的验证码");
        }

        LambdaQueryWrapper<User> queryUserWrapper = new LambdaQueryWrapper<>();
        queryUserWrapper.eq(User::getUsername, userDTO.getUsername())
                .eq(User::getPassword, userDTO.getPassword());
        User one = getOne(queryUserWrapper);

        if (ObjUtil.isEmpty(one)) {
            throw new ServiceException(StandardCode.CODE_600, "用户名或密码不存在");
        }

        if (one.getStatus() == 0) {
            throw new ServiceException(StandardCode.CODE_600, "该账户被禁用，请联系管理员");
        }

        // 查询该用户的角色
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("user_id", one.getId());
        List<UserRole> userRoles = userRoleMapper.selectByMap(columnMap);
        if (userRoles.isEmpty()) {
            throw new ServiceException(StandardCode.CODE_600, "您没有权限，请联系管理员");
        }

        UserRole userRole = userRoles.get(0);
        LambdaQueryWrapper<Role> queryRoleWrapper = new LambdaQueryWrapper<>();
        queryRoleWrapper.eq(Role::getId, userRole.getRoleId());
        Role role = roleMapper.selectOne(queryRoleWrapper);
        one.setRole(role);

        // 查询该用户所属角色可用菜单id
        LambdaQueryWrapper<RoleMenu> queryRoleMenuWrapper = new LambdaQueryWrapper<>();
        queryRoleMenuWrapper.eq(RoleMenu::getRoleId, role.getId());
        List<RoleMenu> roleMenus = roleMenuMapper.selectList(queryRoleMenuWrapper);

        List<Integer> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());

        LambdaQueryWrapper<Menu> queryMenuWrapper = new LambdaQueryWrapper<>();
        queryMenuWrapper.in(Menu::getId, menuIds);

        // 找出该用户可以访问的所有菜单
        List<Menu> menuList = menuService.list(queryMenuWrapper);
        // 找出一级菜单
        List<Menu> parentMenus = menuList.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
        // 找出一级菜单的子菜单
        for (Menu parentMenu : parentMenus) {
            parentMenu.setChildren(menuList.stream().filter(m -> parentMenu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        userDTO.setMenus(parentMenus);

        BeanUtils.copyProperties(one, userDTO);

        // 生成token
        String token = TokenUtil.getToken(userDTO.getId().toString(), userDTO.getPassword());
        userDTO.setToken(token);

        return userDTO;
    }

    @Override
    @Transactional
    public User uploadAvatar(MultipartFile avatar, User user) {
        String avatarUrl = null;
        String endpoint = OSSUtil.END_POINT;
        String accessKeyId = OSSUtil.ACCESS_KEY_ID;
        String accessKeySecret = OSSUtil.ACCESS_KEY_SECRET;
        String bucketName = OSSUtil.BUCKET_NAME;

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {

            InputStream inputStream = avatar.getInputStream();
            //获取上传的文件的名字
            String filename = avatar.getOriginalFilename();
            //随机uuid是为了拼接文件名，防止用户上传两个名字相同的文件后覆盖掉前一个
            UUID uuid = UUID.randomUUID();
            //拼接成完整的文件名。
            filename = "java/" + uuid + filename;

            // 创建请求对象
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filename, inputStream);

            // 发送请求
            ossClient.putObject(putObjectRequest);
            // 拼接url
            avatarUrl = "https://" + bucketName + "." + endpoint + "/" + filename;
        } catch (IOException e) {
            throw new ServiceException(StandardCode.CODE_600, "头像上传OSS失败");
        }finally {
            ossClient.shutdown();
        }
        user.setAvatarUrl(avatarUrl);
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(User::getAvatarUrl, user.getAvatarUrl())
                .eq(User::getId, user.getId());
        boolean isOk = false;
        try {
            isOk = update(updateWrapper);
        } catch (Exception e) {
            throw new ServiceException(StandardCode.CODE_600, "头像成功上传至OSS但更新数据库失败");
        }
        if (isOk) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void setRole(Integer userId, Integer roleId) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("user_id", userId);
        userRoleMapper.deleteByMap(columnMap);
        UserRole userRole = new UserRole(userId, roleId);
        userRoleMapper.insert(userRole);
    }

    @Override
    @Transactional
    public boolean saveOrUpdatePlus(User user) {
        saveOrUpdate(user);
        setRole(user.getId(), user.getRole().getId());
        return true;
    }

    /**
     * 根据id删除用户信息，并且删除用户角色关联表的记录
     * @param id
     * @return
     */
    @Override
    @Transactional
    public boolean removeByIdPlus(Integer id) {
        removeById(id);
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("user_id", id);
        userRoleMapper.deleteByMap(columnMap);
        return true;
    }

    /**
     * 根据id批量删除用户信息，并且删除用户角色关联表的记录
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public boolean removeBatchByIdsPlus(List<Integer> ids) {
        removeBatchByIds(ids);
        Map<String, Object> columnMap = new HashMap<>();
        ids.forEach(id -> {
            columnMap.put("user_id", id);
            userRoleMapper.deleteByMap(columnMap);
            columnMap.clear();
        });
        return true;
    }
}
