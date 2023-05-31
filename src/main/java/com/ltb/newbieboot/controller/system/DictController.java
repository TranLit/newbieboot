package com.ltb.newbieboot.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ltb.newbieboot.annotation.DemoEnabled;
import com.ltb.newbieboot.common.Result;
import com.ltb.newbieboot.entity.system.Dict;
import com.ltb.newbieboot.service.system.IDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 字典管理
 *
 * @author 小李哞哞
 * @date 2023/5/23
 */
@RestController
@RequestMapping("/dict")
public class DictController {

    @Autowired
    private IDictService dictService;

    @GetMapping
    @DemoEnabled
    public Result queryList(String type) {
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dict::getType, type);
        List<Dict> dictList = dictService.list(queryWrapper);
        return Result.success(dictList);
    }
}
