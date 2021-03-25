package io.github.grooters.idles.api;

import com.alibaba.fastjson.JSON;
import io.github.grooters.idles.base.Code;
import io.github.grooters.idles.bean.Goods;
import io.github.grooters.idles.bean.Result;
import io.github.grooters.idles.bean.User;
import io.github.grooters.idles.controller.GoodsC;
import io.github.grooters.idles.dao.GoodsDao;
import io.github.grooters.idles.dao.UserDao;
import io.github.grooters.idles.utils.Printer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class GoodsApi {

    private UserDao userDao;

    private GoodsDao goodsDao;

    private GoodsC goodsC;

    private Result result;

    @Autowired
    public GoodsApi(UserDao userDao, GoodsDao goodsDao){

        this.userDao = userDao;

        this.goodsDao = goodsDao;

        result = new Result();

        goodsC = new GoodsC();
    }

    @PostMapping(value = "/getSeller")
    public String getSeller(@RequestParam("tokenNumber") String tokenNumber,
                            @RequestParam("goodsNumber") String goodsNumber){

        User user = userDao.findByTokenNumber(tokenNumber);

        Goods goods = goodsDao.findByGoodsNumber(goodsNumber);

        if(user == null){
            user = new User();
            user.setCode(Code.LOGIN_INVALID);
            user.setMessage("登录失效");
        }else if (goods == null){
            user.setCode(Code.GOODS_SUCCESS_EMPTY);
            user.setMessage("该商品不存在");
        }else{
            user.setCode(Code.GOODS_SUCCESS_GETS_SELLER);
            user.setMessage("获取商家信息成功");
        }

        return JSON.toJSONString(user);

    }

    @PostMapping(value = "/getGoods")
    public String getGoods(@RequestParam("tokenNumber") String tokenNumber){

        List<Goods> data;

        data = goodsDao.findAll();

        Printer.print("账户信息", "user == null前tokenNumber：" + tokenNumber);

        User user = userDao.findByTokenNumber(tokenNumber);

        if(user == null){
            Goods goods = new Goods();
            Printer.print("账户信息", "tokenNumber：" + tokenNumber);
            goods.setCode(Code.LOGIN_INVALID);
            goods.setMessage("登录失效，请重新登录");
            data.add(goods);

        }else if(data.size() == 0){
            Goods goods = new Goods();
            goods.setCode(Code.GOODS_SUCCESS_EMPTY);
            goods.setMessage("商品列表获取成功");
            data.add(goods);
        }else{
            data.get(0).setCode(Code.GOODS_SUCCESS_GET_ALL);
            data.get(0).setMessage("商品列表获取成功");
        }

        return JSON.toJSONString(data);
    }

    @PostMapping(value = "/pushGoods")
    public String pushGoods(@RequestParam("tokenNumber") String tokenNumber, @RequestParam("goods") String goodsJson){

        User user = userDao.findByTokenNumber(tokenNumber);
        Goods goods = JSON.parseObject(goodsJson, Goods.class);

        if(user == null){
            result.setCode(Code.LOGIN_INVALID);
            result.setMessage("登录失效，请重新登录");

        }else {
            result.setCode(Code.GOODS_SUCCESS_PUSH);
            result.setMessage("发布成功");
            user.setMyGoodsPushNumber(goodsC.addNewGoods(user, goods.getGoodsNumber()));
            userDao.save(user);
            goodsDao.save(goods);
        }
        return JSON.toJSONString(result);
    }

    @PostMapping(value = "/collectGoods")
    public String collectGoods(@RequestParam(value = "tokenNumber") String tokenNumber,
                               @RequestParam(value = "goodsNumber") String goodsNumber){

        User user = userDao.findByTokenNumber(tokenNumber);

        if(user == null){
            result.setCode(Code.LOGIN_INVALID);
            result.setMessage("登录失效，请重新登录");
            return JSON.toJSONString(result);
        }

        String[] myGoodsCollectionNumbers = user.getMyGoodsCollectionNumber();
        String[] temp;
        if(myGoodsCollectionNumbers == null) {
            temp = new String[1];
            temp[0] = goodsNumber;

        }else{
            for(String collectionNumber : myGoodsCollectionNumbers){

                if(collectionNumber.equals(goodsNumber)){
                    temp = new String[myGoodsCollectionNumbers.length - 1];

                    for(int i = 0; i < temp.length - 1; i++){
                        if(!goodsNumber.equals(myGoodsCollectionNumbers[i])){
                            temp[i] = myGoodsCollectionNumbers[i];
                        }
                    }

                    user.setMyGoodsCollectionNumber(temp);
                    userDao.save(user);
                    temp = null;
                    result.setCode(Code.GOODS_SUCCESS_CANCEL_COLLECT);
                    result.setMessage("取消收藏成功");
                    return JSON.toJSONString(result);
                }
            }
            temp = new String[myGoodsCollectionNumbers.length + 1];
            temp[myGoodsCollectionNumbers.length] = goodsNumber;

        }
        user.setMyGoodsCollectionNumber(temp);
        userDao.save(user);
        temp = null;
        result.setCode(Code.GOODS_SUCCESS_COLLECT);
        result.setMessage("收藏成功");
        return JSON.toJSONString(result);
    }

    @GetMapping("/test")
    public void test(){
        Goods goods = new Goods();
        goods.setComeLatelyTime("ss");
        goods.setGoodsNumber("sdd");
        goods.setFansNumber(1);
        goods.setGoodsName("dwdw");
        goods.setPrice(12);
        goods.setTitleImage("dwdwdw");
        goods.setIntroImage(new String[]{"ss","ddd"});
        goods.setTime("dwdw");
        goods.setDescription("dwdwd");
        goods.setSellerNumber("dfwdwd");
        goodsDao.save(goods);
    }
}
