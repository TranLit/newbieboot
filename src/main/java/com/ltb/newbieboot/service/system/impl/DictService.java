package com.ltb.newbieboot.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltb.newbieboot.entity.system.Dict;
import com.ltb.newbieboot.mapper.system.DictMapper;
import com.ltb.newbieboot.service.system.IDictService;
import org.springframework.stereotype.Service;

/**
 * @author 小李哞哞
 * @date 2023/5/23
 */
@Service
public class DictService extends ServiceImpl<DictMapper, Dict> implements IDictService {
}
