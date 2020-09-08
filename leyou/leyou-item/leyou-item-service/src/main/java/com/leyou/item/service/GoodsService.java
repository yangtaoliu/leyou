package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;

public interface GoodsService {
    PageResult<SpuBo> queryGoodsByPage(String key, Boolean saleable, Integer page, Integer rows);
}
