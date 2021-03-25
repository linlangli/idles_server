package io.github.grooters.idles.api;

import com.alibaba.fastjson.JSON;
import io.github.grooters.idles.base.Code;
import io.github.grooters.idles.bean.Result;
import io.github.grooters.idles.bean.User;
import io.github.grooters.idles.bean.Works;
import io.github.grooters.idles.dao.UserDao;
import io.github.grooters.idles.dao.WorksDao;
import io.github.grooters.idles.utils.Printer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class WorksApi {

    private UserDao userDao;

    private WorksDao worksDao;

    private Result result;

    @Autowired
    public WorksApi(UserDao userDao, WorksDao worksDao){

        this.userDao = userDao;

        this.worksDao = worksDao;

        result = new Result();
    }

    @PostMapping(value = "/buyWorks")
    public Result buyWorks(@RequestParam(value = "tokenNumber") String tokenNumber,
                                     @RequestParam(value = "worksNumber") String worksNumber){

        User user = userDao.findByTokenNumber(tokenNumber);

        if(user == null){
            result.setCode(Code.LOGIN_INVALID);
            result.setMessage("登录失效，请重新登录");
            return result;
        }

        Works works = worksDao.findByWorksNumber(worksNumber);

        String[] buyerNumbers = works.getBuyerNumber();

        String[] temp;

        if(buyerNumbers == null){

            temp = new String[]{user.getUserNumber()};


        }else{

            temp = new String[buyerNumbers.length + 1];

            temp[buyerNumbers.length] = worksNumber;
        }

        works.setBuyerNumber(temp);

        temp = null;

        worksDao.save(works);

        result.setCode(Code.LOGIN_INVALID);
        result.setMessage("接单成功");

        return result;
    }

    @PostMapping(value = "/getWorks")
    public String getWorks(@RequestParam("tokenNumber") String tokenNumber){

        List<Works> data;
        data = worksDao.findAll();
        Printer.print("登录信息", "tokenNumber：" + tokenNumber);
        User user = userDao.findByTokenNumber(tokenNumber);

        if(user == null){
            Works works = new Works();
            works.setCode(Code.LOGIN_INVALID);
            works.setMessage("登录失效，请重新登录");
            data.add(works);

        }else if(data.size() == 0){
            Works works = new Works();
            works.setCode(Code.WORKS_SUCCESS_EMPTY);
            works.setMessage("事务列表获取成功");
            data.add(works);

        }else{
            data.get(0).setCode(Code.WORKS_SUCCESS_GET_ALL);
            data.get(0).setMessage("商品列表获取成功");
        }

        return JSON.toJSONString(data);
    }

    @PostMapping(value = "/pushWorks")
    public Result pushWorks(@RequestParam("tokenNumber") String tokenNumber,
                            @RequestParam("works") String worksJson){

        Printer.print("pushWorks接口", "tokenNumber：" + tokenNumber, "worksJson：" + worksJson);

        User user = userDao.findByTokenNumber(tokenNumber);
        Works works = JSON.parseObject(worksJson, Works.class);
        if(user == null){
            result.setCode(Code.LOGIN_INVALID);
            result.setMessage("登录失效，请重新登录");
            return result;
        }

        worksDao.save(works);

        String[] saleNumbers = user.getMyWorksPushNumber();
        String[] temp;

        if(saleNumbers == null){
            temp = new String[1];
            temp[0] = works.getWorksNumber();
        }else{
            temp = new String[saleNumbers.length + 1];
            temp[saleNumbers.length] = works.getWorksNumber();
        }

        user.setMyWorksPushNumber(temp);
        temp = null;
        userDao.save(user);

        result.setCode(Code.WORKS_SUCCESS_PUSH);
        result.setMessage("发布成功");

        return result;
    }

}
