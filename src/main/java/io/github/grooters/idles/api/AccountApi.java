package io.github.grooters.idles.api;

import com.alibaba.fastjson.JSON;
import io.github.grooters.idles.base.Code;
import io.github.grooters.idles.bean.*;
import io.github.grooters.idles.controller.AccountC;
import io.github.grooters.idles.dao.TokenDao;
import io.github.grooters.idles.dao.UserDao;
import io.github.grooters.idles.dao.VerificationDao;
import io.github.grooters.idles.utils.Emailer;
import io.github.grooters.idles.utils.Printer;
import io.github.grooters.idles.utils.Regexer;
import io.github.grooters.idles.utils.ZFCatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class AccountApi {

    private final UserDao userDao;
    private final TokenDao tokenDao;

    private Result result;

    private User user;

    private AccountC accountC;

    private Verification verification;
    private VerificationDao verificationDao;

    public static long startTime;

    @Autowired
    public AccountApi(UserDao userDao, VerificationDao verificationDao, TokenDao tokenDao){
        this.userDao = userDao;
        this.tokenDao = tokenDao;
        this.verificationDao = verificationDao;
        result = new Result();
        accountC = new AccountC();
    }

    @PostMapping(value = "/login")
    public String login(@RequestParam(value = "number") String number,
                      @RequestParam(value = "password") String password){

        String tokenNumber = accountC.createToken(8);
        startTime = System.currentTimeMillis();
        Printer.print("登录信息", "number：" + number, "password：" + password, "tokenNumber：" + tokenNumber);

        // 用邮箱登录
        if(Regexer.isEmail(number)){
            Printer.print("登录方式","使用邮箱登录");
            user = userDao.findByEmail(number);
            if(user != null){
                if(!user.getPassword().equals(password)){
                    user.setCode(Code.LOGIN_FAILURE_PASSWORD);
                    user.setMessage("账户或密码错误");
                }else{
                    user.setTokenNumber(tokenNumber);
                    user.setCode(Code.LOGIN_SUCCESS);
                    user.setMessage("登录成功");
                    Printer.print("用户信息", JSON.toJSONString(user));
                    userDao.save(user);
                }
            }else{
                user = new User();
                user.setCode(Code.LOGIN_FAILURE_NUMBER);
                user.setMessage("账户不存在");
            }
        // 用教务系统账户登录
        }else{
            // 判断数据库中是否存在该学生记录
            user = userDao.findByUserNumber(number);
            if(user != null){
                Printer.print("登录方式","从数据库中获取教务系统账户登录");
                if(!user.getPassword().equals(password)){
                    user.setCode(Code.LOGIN_FAILURE_NUMBER);
                    user.setMessage("账户或密码错误");
                }else{
                    user.setTokenNumber(tokenNumber);
                    user.setCode(Code.LOGIN_SUCCESS);
                    user.setMessage("登录成功");
                    Printer.print("用户信息", JSON.toJSONString(user));
                    userDao.save(user);
                }
            }
            // 若无则去模拟教务系统登录获取学生信息
            else{
                Printer.print("登录方式","从云端中模拟教务系统账户登录");
                ZFCatcher zfCatch = new ZFCatcher();
                // 请求登录
                int code = Code.UNKNOWN;
                for(int i = 0; i < 20; i++){
                    code = zfCatch.requestLogin(number, password);
                    if (code != Code.LOGIN_FAILURE_VERIFICATION )
                        break;
                }
                switch (code){
                    case Code.LOGIN_FAILURE_PASSWORD:
                        user = new User();
                        user.setCode(code);
                        user.setMessage("账户或密码错误");
                        break;
                    case Code.LOGIN_FAILURE_NUMBER:
                        user = new User();
                        user.setCode(code);
                        user.setMessage("账户不存在");
                        break;
                    case Code.UNKNOWN:
                        user = new User();
                        user.setCode(code);
                        user.setMessage("未知错误,请重新尝试");
                        break;
                    case Code.LOGIN_SUCCESS:
                        // 请求登录成功，获取个人信息
                        user = zfCatch.getPersonalInfo();
                        if( user == null ){
                            user = new User();
                            Printer.print("模拟教务系统登录出错代码","code = " + code);
                            user.setCode(Code.UNKNOWN);
                            user.setMessage("个人信息获取失败");
                            break;
                        }
                        user.setTokenNumber(tokenNumber);
                        user.setCode(code);
                        user.setMessage("登录成功");
                        Printer.print("用户信息", JSON.toJSONString(user));
                        userDao.save(user);
                        break;
                }
            }
        }
        Printer.print("返回客户端信息", "number：" + number, "password：" + password, "tokenNumber：" + tokenNumber);
        return JSON.toJSONString(user);
    }

    @GetMapping(value = "/setTime")
    public String setTime(@RequestParam(value = "tokenNumber") String tokenNumber){

        User user = userDao.findByTokenNumber(tokenNumber);
        if(user == null){
            result.setCode(Code.LOGIN_INVALID);
            result.setMessage("登录失效");
            return JSON.toJSONString(result);
        }
        long totalTime = (System.currentTimeMillis() - startTime) /1000;
        long time;
        Printer.print("统计时长：" , "上线总时间：" + totalTime+"秒");
        if(totalTime % 60 > 50){
            time = totalTime / 60 + 1;
        }else {

            time = totalTime / 60;
        }
        user.setTime(user.getTime() + time);
        userDao.save(user);
        result.setCode(Code.PERSONAL_SUCCESS_SETTING);
        result.setMessage("登录时长更新成功");
        return JSON.toJSONString(result);
    }

    @GetMapping(value = "/getToken")
    public String getToken(){

        String tokenNumber = accountC.createToken(8);

        Token token = new Token();

        token.setTokenNumber(tokenNumber);

        token.setUserNumber("");

        token.setCode(Code.LOGIN_SUCCESS_TOKEN);

        token.setMessage("令牌获取成功");

        tokenDao.save(token);

        return JSON.toJSONString(token);
    }

    @PostMapping(value = "/getVerification")
    public String getVerification(@RequestParam(value = "email") String email,
                                  @RequestParam(value = "emailServer") String emailServer,
                                  @RequestParam(value = "emailServerKey") String emailServerKey){

        verificationDao.deleteAll();

        user = userDao.findByEmail(email);

        if(user != null){

            result.setCode(Code.REGISTER_FAILURE_EMAIL);

            result.setMessage("该邮箱已存在");

            return JSON.toJSONString(result);
        }

        verification = new Verification();

        String verificationNumber = accountC.createVerification(5);

        Printer.print("邮箱服务器", "emailServer：" + emailServer, "emailServerKey：" + emailServerKey);

        Emailer emailer = new Emailer(emailServer, emailServer, emailServerKey);

        int code = emailer.sendEmail("Idles注册验证码", verificationNumber, email);

        if(code == Code.REGISTER_FAILURE_VERIFICATION){

            result.setCode(code);

            result.setMessage("验证码发送失败");

            return JSON.toJSONString(result);
        }

        verification.setEmail(email);

        long start = accountC.getNetworkTime();

        Date date=new Date(start);

        SimpleDateFormat dateFormat=new SimpleDateFormat("YYYY-MM-dd HH:mm");

        Printer.print("验证码获取","获取时间："+ start, dateFormat.format(date));

        verification.setTime(start);

        verification.setNumber(verificationNumber);

        verificationDao.save(verification);

        result.setCode(Code.REGISTER_SUCCESS_VERIFICATION);

        result.setMessage("验证码已发送，请前往邮箱查收");

        return JSON.toJSONString(result);

    }

    @PostMapping(value = "/verify")
    public Result verify(@RequestParam("email") String email, @RequestParam("verificationNumber") String verificationNumber){

        verification = verificationDao.findByEmail(email);

        Printer.print("需要验证的邮箱和验证码", "邮箱：" + email, "验证码：" + verificationNumber);

        Printer.print("数据库对应的验证码", "验证码：" + verificationDao.findByEmail(email).getNumber());

        if(verification == null){

            result.setCode(Code.REGISTER_FAILURE_VERIFICATION);

            result.setMessage("请先获取验证码");

        }else if(!verification.getNumber().equals(verificationNumber)){

            result.setCode(Code.REGISTER_FAILURE_VERIFY);

            result.setMessage("验证码错误");

        }else if (verification.getNumber().equals(verificationNumber)){

            result.setCode(Code.REGISTER_SUCCESS_VERIFY);

            result.setMessage("验证成功");

        }else{

            result.setCode(Code.UNKNOWN);

            result.setMessage("未知错误");
        }

        return result;
    }

    @PostMapping(value = "/register")
    public String register(@RequestParam(value = "email") String email, @RequestParam(value = "password") String password){

        Printer.print("需要注册的账户", "邮箱：" + email, "密码：" + password);

        user = new User();

        verification = verificationDao.findByEmail(email);

        if (verification == null){
            user.setMessage("邮箱未验证或验证失效");
            user.setCode(Code.REGISTER_FAILURE);
            return JSON.toJSONString(user);
        }

        long end = accountC.getNetworkTime();

        Date date=new Date(end);

        SimpleDateFormat dateFormat=new SimpleDateFormat("YYYY-MM-dd HH:mm");

        Printer.print("验证码时间",
                "验证验证码时的时间："+ end,
                "格式化后：" + dateFormat.format(date),
                "验证码中设置的时间："+ verification.getTime(),
                "格式化后：" + dateFormat.format(verification.getTime()),
                "相差的时间："+(end - verification.getTime())/1000);

        if((end - verification.getTime())/1000 > 120){
            user.setCode(Code.REGISTER_FAILURE_VERIFICATION);
            user.setMessage("验证码已过期，请重新验证");
            return JSON.toJSONString(user);
        }

        user.setEmail(email);
        user.setName(accountC.createRandomName());
        user.setPassword(password);
        user.setUserNumber(accountC.createRandomNumber());
        user.setTokenNumber("");

        userDao.save(user);

        user.setCode(Code.REGISTER_SUCCESS);
        user.setMessage("注册成功");

        return JSON.toJSONString(user);
    }

}
