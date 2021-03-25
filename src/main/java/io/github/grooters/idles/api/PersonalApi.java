package io.github.grooters.idles.api;

import com.alibaba.fastjson.JSON;
import io.github.grooters.idles.base.Code;
import io.github.grooters.idles.bean.Goods;
import io.github.grooters.idles.bean.Result;
import io.github.grooters.idles.bean.User;
import io.github.grooters.idles.bean.Works;
import io.github.grooters.idles.controller.PersonalC;
import io.github.grooters.idles.dao.GoodsDao;
import io.github.grooters.idles.dao.UserDao;
import io.github.grooters.idles.dao.WorksDao;
import io.github.grooters.idles.entity.Universities;
import io.github.grooters.idles.utils.Printer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PersonalApi {
    private UserDao userDao;

    private WorksDao worksDao;

    private GoodsDao goodsDao;

    private Result result;

    private List<Works> worksList;

    private List<Goods> goodsList;

    private PersonalC personalC;

    @Autowired
    public PersonalApi(UserDao userDao, GoodsDao goodsDao, WorksDao worksDao){
        this.userDao = userDao;
        this.worksDao = worksDao;
        this.goodsDao = goodsDao;
        result = new Result();
        worksList = new ArrayList<>();
        goodsList = new ArrayList<>();
        personalC = new PersonalC();
    }


    @PostMapping(value = "/getMyHistoryGoods")
    public String getHistoryGoods(@RequestParam("tokenNumber") String tokenNumber){

        goodsList.clear();

        User user = userDao.findByTokenNumber(tokenNumber);

        if(userDao.findByTokenNumber(tokenNumber) == null){
            Goods goods = new Goods();
            goods.setCode(Code.LOGIN_INVALID);
            goods.setMessage("登录失效，请重新登录");
            goodsList.add(goods);

        }else{
            String[] myGoodsHistoryNumbers = user.getMyGoodsHistoryNumber();
            goodsList.clear();

            Goods goods;

            if(myGoodsHistoryNumbers != null){
                for(String myGoodsHistoryNumber : myGoodsHistoryNumbers){
                    goods = goodsDao.findByGoodsNumber(myGoodsHistoryNumber);
                    goodsList.add(goods);
                }
                goodsList.get(0).setMessage("我的浏览历史获取成功");
                goodsList.get(0).setCode(Code.PERSONAL_SUCCESS_HISTORY);
            }else{
                goods = new Goods();
                goods.setCode(Code.GOODS_SUCCESS_EMPTY);
                goods.setMessage("暂时没有商品浏览历史");
                goodsList.add(goods);
            }
        }

        return JSON.toJSONString(goodsList);
    }

    @PostMapping(value = "/getMyHistoryWorks")
    public String getHistoryWorks(@RequestParam("tokenNumber") String tokenNumber){

        worksList.clear();

        User user = userDao.findByTokenNumber(tokenNumber);

        if(userDao.findByTokenNumber(tokenNumber) == null){
            Works works = new Works();
            works.setCode(Code.LOGIN_INVALID);
            works.setMessage("登录失效，请重新登录");
            worksList.add(works);

        }else{
            String[] myWorksCHistoryNumbers = user.getMyWorksHistoryNumber();
            worksList.clear();
            Works works;

            if(myWorksCHistoryNumbers != null){
                for(String myWorksHistoryNumber : myWorksCHistoryNumbers){
                    works = worksDao.findByWorksNumber(myWorksHistoryNumber);
                    worksList.add(works);
                }
                worksList.get(0).setMessage("我的浏览历史获取成功");
                worksList.get(0).setCode(Code.PERSONAL_SUCCESS_HISTORY);
            }else{
                works = new Works();
                works.setCode(Code.WORKS_SUCCESS_EMPTY);
                works.setMessage("暂时没有事务浏览历史");
                worksList.add(works);
            }
        }

        return JSON.toJSONString(worksList);
    }

    @PostMapping(value = "/getMyCollectionGoods")
    public String getMyCollectionGoods(@RequestParam("tokenNumber") String tokenNumber){

        goodsList.clear();

        User user = userDao.findByTokenNumber(tokenNumber);

        if(userDao.findByTokenNumber(tokenNumber) == null){
            Goods goods = new Goods();
            goods.setCode(Code.LOGIN_INVALID);
            goods.setMessage("登录失效，请重新登录");
            goodsList.add(goods);

        }else{
            String[] myGoodsCollectionNumbers = user.getMyGoodsCollectionNumber();
            goodsList.clear();

            Goods goods;

            if(myGoodsCollectionNumbers != null){
                for(String myGoodsCollectionNumber : myGoodsCollectionNumbers){
                    goods = goodsDao.findByGoodsNumber(myGoodsCollectionNumber);
                    goodsList.add(goods);
                }
                goodsList.get(0).setMessage("我的商品收藏获取成功");
                goodsList.get(0).setCode(Code.PERSONAL_SUCCESS_COLLECTION);
            }else{
                goods = new Goods();
                goods.setCode(Code.GOODS_SUCCESS_EMPTY);
                goods.setMessage("暂时没有收藏商品");
                goodsList.add(goods);
            }

        }

        return JSON.toJSONString(goodsList);
    }

    @PostMapping(value = "/getMyCollectionWorks")
    public String getMyCollectionWorks(@RequestParam("tokenNumber") String tokenNumber){

        worksList.clear();

        User user = userDao.findByTokenNumber(tokenNumber);

        if(userDao.findByTokenNumber(tokenNumber) == null){
            Works works = new Works();
            works.setCode(Code.LOGIN_INVALID);
            works.setMessage("登录失效，请重新登录");
            worksList.add(works);

        }else{
            String[] myWorksCollectionNumbers = user.getMyWorksCollectionNumber();
            worksList.clear();
            Works works;

            if(myWorksCollectionNumbers != null){
                for(String myWorksCollectionNumber : myWorksCollectionNumbers){
                    works = worksDao.findByWorksNumber(myWorksCollectionNumber);
                    worksList.add(works);
                }
                worksList.get(0).setMessage("我的收藏获取成功");
                worksList.get(0).setCode(Code.PERSONAL_SUCCESS_COLLECTION);
            }else{
                works = new Works();
                works.setCode(Code.WORKS_SUCCESS_EMPTY);
                works.setMessage("暂时没有收藏事务");
                worksList.add(works);
            }
        }

        return JSON.toJSONString(worksList);
    }

    @PostMapping(value = "/getMyOrderGoods")
    public String getMyOrderGoods(@RequestParam("tokenNumber") String tokenNumber){

        Printer.print("getMyOrderGoods", "tokenNumber：" + tokenNumber);

        goodsList.clear();

        User user = userDao.findByTokenNumber(tokenNumber);

        if(userDao.findByTokenNumber(tokenNumber) == null){
            Goods goods = new Goods();
            goods.setCode(Code.LOGIN_INVALID);
            goods.setMessage("登录失效，请重新登录");
            goodsList.add(goods);

        }else{
            String[] myGoodsOrderNumbers = user.getMyGoodsOrderNumber();
            goodsList.clear();

            Goods goods;

            if(myGoodsOrderNumbers != null){
                for(String myGoodsOrderNumber : myGoodsOrderNumbers){
                    goods = goodsDao.findByGoodsNumber(myGoodsOrderNumber);
                    goodsList.add(goods);
                }
                goodsList.get(0).setMessage("我的订单获取成功");
                goodsList.get(0).setCode(Code.PERSONAL_SUCCESS_BUY);
            }else{
                goods = new Goods();
                goods.setCode(Code.GOODS_SUCCESS_EMPTY);
                goods.setMessage("暂时没有商品订单");
                goodsList.add(goods);
            }
        }

        return JSON.toJSONString(goodsList);
    }

    @PostMapping(value = "/getMyOrderWorks")
    public String getMyOrderWorks(@RequestParam("tokenNumber") String tokenNumber){

        worksList.clear();

        User user = userDao.findByTokenNumber(tokenNumber);

        if(userDao.findByTokenNumber(tokenNumber) == null){
            Works works = new Works();
            works.setCode(Code.LOGIN_INVALID);
            works.setMessage("登录失效，请重新登录");
            worksList.add(works);

        }else{
            String[] myWorksOrderNumbers = user.getMyWorksOrderNumber();
            worksList.clear();
            Works works;

            if(myWorksOrderNumbers != null){
                for(String myWorksOrderNumber : myWorksOrderNumbers){
                    works = worksDao.findByWorksNumber(myWorksOrderNumber);
                    worksList.add(works);
                }
                worksList.get(0).setMessage("我的事务订单获取成功");
                worksList.get(0).setCode(Code.PERSONAL_SUCCESS_BUY);
            }else{
                works = new Works();
                works.setCode(Code.WORKS_SUCCESS_EMPTY);
                works.setMessage("暂时没有事务订单");
                worksList.add(works);
            }
        }

        return JSON.toJSONString(worksList);
    }

    @PostMapping(value = "/getMyPushGoods")
    public String getMyPushGoods(@RequestParam("tokenNumber") String tokenNumber){

        goodsList.clear();

        User user = userDao.findByTokenNumber(tokenNumber);

        if(userDao.findByTokenNumber(tokenNumber) == null){
            Goods goods = new Goods();
            goods.setCode(Code.LOGIN_INVALID);
            goods.setMessage("登录失效，请重新登录");
            goodsList.add(goods);

        }else{
            String[] myGoodsPushNumbers = user.getMyGoodsPushNumber();
            goodsList.clear();

            Goods goods;

            if(myGoodsPushNumbers != null){
                for(String myGoodsPushNumber : myGoodsPushNumbers){
                    goods = goodsDao.findByGoodsNumber(myGoodsPushNumber);
                    goodsList.add(goods);
                }
                goodsList.get(0).setMessage("我的销售商品获取成功");
                goodsList.get(0).setCode(Code.PERSONAL_SUCCESS_BUY);
            }else{
                goods = new Goods();
                goods.setCode(Code.GOODS_SUCCESS_EMPTY);
                goods.setMessage("暂时没有销售物品");
                goodsList.add(goods);
            }
        }

        return JSON.toJSONString(goodsList);
    }

    @PostMapping(value = "/getMyPushWorks")
    public String getMyPushWorks(@RequestParam("tokenNumber") String tokenNumber){

        worksList.clear();

        User user = userDao.findByTokenNumber(tokenNumber);

        if(userDao.findByTokenNumber(tokenNumber) == null){
            Works works = new Works();
            works.setCode(Code.LOGIN_INVALID);
            works.setMessage("登录失效，请重新登录");
            worksList.add(works);

        }else{
            String[] myWorksPushNumbers = user.getMyWorksPushNumber();
            worksList.clear();
            Works works;

            if(myWorksPushNumbers != null){
                for(String myWorksPushNumber : myWorksPushNumbers){
                    works = worksDao.findByWorksNumber(myWorksPushNumber);
                    worksList.add(works);
                }
                worksList.get(0).setMessage("我的销售事务获取成功");
                worksList.get(0).setCode(Code.PERSONAL_SUCCESS_SELL);
            }else{
                works = new Works();
                works.setCode(Code.WORKS_SUCCESS_EMPTY);
                works.setMessage("暂时没有销售事务");
                worksList.add(works);
            }
        }

        return JSON.toJSONString(worksList);
    }

    @PostMapping(value = "/getUser")
    public String getUser(@RequestParam("tokenNumber") String tokenNumber){

        User user = userDao.findByTokenNumber(tokenNumber);

        Printer.print("账号信息", "tokenNumber：" + tokenNumber);
        if(userDao.findByTokenNumber(tokenNumber) == null){
            user= new User();
            user.setCode(Code.LOGIN_INVALID);
            user.setMessage("登录失效，请重新登录");
        }

        long totalTime = (System.currentTimeMillis() - AccountApi.startTime) /1000;
        long time;
        if(totalTime % 60 > 50){
            time = totalTime / 60 + 1;
        }else {

            time = totalTime / 60;
        }

        user.setTime(user.getTime() + time);

        int grade =(int) ((user.getTime() - (Math.pow(user.getLevel(), 2)) * 10) / (Math.pow(user.getLevel() + 1, 2) * 10));

        user.setGrade(grade * 100 + "%");
        user.setMessage("账号信息获取成功");
        user.setCode(Code.PERSONAL_SUCCESS_INFO);
        if( (user.getTime() - Math.pow(user.getLevel(), 2) * 10 + time) >= (Math.pow(user.getLevel() + 1, 2)) * 10 ){
            user.setLevel(user.getLevel() + 1);
        }

        userDao.save(user);

        return JSON.toJSONString(user);
    }

    @PostMapping(value = "/setUniversity")
    public String setUniversity(@RequestParam("tokenNumber") String tokenNumber, @RequestParam("universityName") String universityName){
        User user = userDao.findByTokenNumber(tokenNumber);

        if(userDao.findByTokenNumber(tokenNumber) == null){ ;
            result.setCode(Code.LOGIN_INVALID);
            result.setMessage("登录失效，请重新登录");
        }else {
            user.setUniversity(universityName);
            userDao.save(user);
            result.setCode(Code.PERSONAL_SUCCESS_SETTING);
            result.setMessage("学院设置成功");
        }

        return JSON.toJSONString(result);
    }

    @PostMapping(value = "/setResume")
    public String setResume(@RequestParam("tokenNumber") String tokenNumber, @RequestParam("resume") String resume){
        User user = userDao.findByTokenNumber(tokenNumber);

        if(userDao.findByTokenNumber(tokenNumber) == null){ ;
            result.setCode(Code.LOGIN_INVALID);
            result.setMessage("登录失效，请重新登录");
        }else {
            user.setResume(resume);
            userDao.save(user);
            result.setCode(Code.PERSONAL_SUCCESS_SETTING);
            result.setMessage("个人简介设置成功");
        }

        return JSON.toJSONString(result);
    }

    @GetMapping(value = "/getUniversity")
    public String getUniversity(){

        return personalC.getPlace("https://lilinlang.gitlab.io/idles_server/resources/university.js");
    }

    @GetMapping(value = "/searchUniversity")
    public String searchUniversity(@RequestParam("key")String key){
        Universities universities = new Universities();
        String content = personalC.getPlace("https://lilinlang.gitlab.io/idles_server/resources/university.js");
        List<String> results = personalC.searchUniversity(content, key);
        Printer.print("搜索结果", results.get(0));
        universities.setName(results);

        return JSON.toJSONString(universities);
    }

    @PostMapping(value = "/setUserData")
    public String setUserData(@RequestParam("userNumber")String userNumber, @RequestParam("newUserNumber")String newUserNumber,
                              @RequestParam("name")String name, @RequestParam("gender")String gender, @RequestParam("home")String home, @RequestParam("avatar")String avatar){

        User user = userDao.findByUserNumber(userNumber);
        if(user == null) {
            result.setCode(Code.PERSONAL_FAILURE_SETTING);
            result.setMessage("用户不存在，请重新注册");
        } else if(userDao.findByUserNumber(newUserNumber) != null){
            result.setCode(Code.PERSONAL_FAILURE_SETTING);
            result.setMessage("该账号已存在，请重新选择");
        }else{
            user.setUserNumber(newUserNumber);
            user.setLocation(home);
            user.setGender(gender);
            user.setName(name);
            user.setAvatarUrl(avatar);
            userDao.save(user);
            result.setCode(Code.PERSONAL_SUCCESS_SETTING);
            result.setMessage("账户信息更新成功");
        }
        return JSON.toJSONString(result);
    }
}
