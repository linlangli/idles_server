package io.github.grooters.idles.utils;

import io.github.grooters.idles.base.Code;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Emailer {

    private HtmlEmail htmlEmail;

    public final String HOST_NAME_163 = "smtp.163.com";
    public final String HOST_NAME_QQ = "smtp.QQ.com";

    private String sendEmail;

    public Emailer(String sendEmail, String number, String password){

        htmlEmail = new HtmlEmail();

        this.sendEmail = sendEmail;

        htmlEmail.setHostName("smtp." + sendEmail.split("@")[1]);

//        htmlEmail.setSmtpPort(25);

        htmlEmail.setAuthentication(number, password);

        htmlEmail.setStartTLSEnabled(false);

        htmlEmail.setSSLOnConnect(false);

    }

//    public static void main(String[] args) {
//        try {
//            getEmail();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public int sendEmail(String title, String content, String...receiveEmail){

        try {

            htmlEmail.setFrom(sendEmail);

            htmlEmail.addTo(receiveEmail);

            htmlEmail.setCharset("UTF-8");

            htmlEmail.setSubject(title);

            htmlEmail.setHtmlMsg(content);

            Printer.print("email", "发送邮箱：" + sendEmail, "接收邮箱：" + receiveEmail[0]);

            htmlEmail.send();

            return Code.REGISTER_SUCCESS_VERIFICATION;

        } catch (EmailException e) {

            e.printStackTrace();

            return Code.REGISTER_FAILURE_VERIFICATION;

        }

    }


    /**
     * 抓取邮箱
     * @author lsh
     *
     */
//    static void getEmail() throws IOException {
//
//        URL url = new URL("https://book.douban.com/subject/24753651/discussion/58975313/");
//
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//        InputStream is = conn.getInputStream();
//
//        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
//
//        // 书写正则规则
//
//        String regex = "\\w+@[0-9a-z]{2,8}\\.com";
//
//        // 获得正则对象
//
//        Pattern compile = Pattern.compile(regex);
//
//        // line 始终代表网页中一行数据
//
//        String line = br.readLine();
//
//        while(line != null){
//
//            // 正则对象和 要操作字符串关联  得到匹配引擎
//
//            Matcher matcher = compile.matcher(line);
//
//            while(matcher.find()){
//
//                System.out.println("邮箱 : "+matcher.group());
//
//            }
//
//            line = br.readLine();
//
//        }
//    }

}
