package com.leyou.cart.service;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private static final String KEY_PREFIX = "user:cart:";

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void addCart(Cart cart) {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();

        //查询记录
        BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());

        String key = cart.getSkuId().toString();

        Integer num = cart.getNum();

        //判断商品是否在购物车中
        if(hashOps.hasKey(key)){
            //在  更新
            String cartJson = hashOps.get(key).toString();
            cart = JsonUtils.parse(cartJson, Cart.class);
            cart.setNum(cart.getNum() + num);
        }
        else {
            //不在 新增
            Sku sku = this.goodsClient.querySkuBySkuId(cart.getSkuId());

            cart.setUserId(userInfo.getId());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setImage(StringUtils.isBlank(sku.getImages())? "" : StringUtils.split(sku.getImages(),",")[0]);
            cart.setPrice(sku.getPrice());
        }
        hashOps.put(key, JsonUtils.serialize(cart));
    }

    public List<Cart> queryCarts() {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        String key = KEY_PREFIX + userInfo.getId();
        //判断用户是否有购物车记录
        if(!this.stringRedisTemplate.hasKey(key)){
            return null;
        }
        BoundHashOperations<String, Object, Object> hashOps = this.stringRedisTemplate.boundHashOps(key);
        //获取购物车中所有cart值得集合
        List<Object> cartJson = hashOps.values();
        //购物车集合为空直接返回null
        if(CollectionUtils.isEmpty(cartJson)){
            return null;
        }
        //把一个List<Object>转化成一个List<Cart>
        return cartJson.stream().map(json -> {
            return JsonUtils.parse(json.toString(), Cart.class);
        }).collect(Collectors.toList());
    }

    public void updateNum(Cart cart) {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        String key = KEY_PREFIX + userInfo.getId();
        //判断用户是否有购物车记录
        if(!this.stringRedisTemplate.hasKey(key)){
            return ;
        }
        Integer num = cart.getNum();

        BoundHashOperations<String, Object, Object> hashOps = this.stringRedisTemplate.boundHashOps(key);

        String cartJson = hashOps.get(cart.getSkuId().toString()).toString();

        cart = JsonUtils.parse(cartJson, Cart.class);

        cart.setNum(num);

        hashOps.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));
    }

    public void deleteCart(String skuId) {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        String key = KEY_PREFIX + userInfo.getId();
        //判断用户是否有购物车记录
        if(!this.stringRedisTemplate.hasKey(key)){
            return ;
        }
        BoundHashOperations<String, Object, Object> hashOps = this.stringRedisTemplate.boundHashOps(key);
        hashOps.delete(skuId);

    }
}
