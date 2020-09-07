package com.leyou.item.controller;

import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class GoodsController {
    @Autowired
    private GoodsService goodsService;


}
