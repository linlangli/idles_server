package io.github.grooters.idles.utils;

import io.github.grooters.idles.base.Code;
import io.github.grooters.idles.bean.User;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZFCatcher {

	private String number, password, name;
	private String personInfoUrl = "http://jwxt.gcu.edu.cn/xsgrxx.aspx?xh=201610098237&xm=%C0%EE%C1%D6%C0%CB&gnmkdm=N121501";
	private final String HOME_URL = "http://jwxt.gcu.edu.cn/";
	private final String LOGIN_URL = HOME_URL + "default2.aspx";
	private final String TARGET_URL = "http://jwxt.gcu.edu.cn/xs_main.aspx?xh=";
	private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.75 Safari/537.36";
	private final String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8";
	private final String ACCEPT_ENCODING = "gzip, deflate, sdch";
	private final String ACCEPT_LANGUAGE = "zh-CN,zh;q=0.8";
	private final String CACHE_CONTROL = "max-age=0";
	private final String CONNECTION = "keep-alive";
	private final String HOST = "jwxt.gcu.edu.cn";
	private final String UPGRADE_INSECURE_REQUESTS = "1";
	private CloseableHttpClient client;
	private String viewState;
	private String checkCode;

	public static void main(String...args){

		ZFCatcher zfCatch = new ZFCatcher();

		zfCatch.requestLogin("201610098237","lll5304122919");

	}

	public int requestLogin(String number, String password) {

		this.number = number;

		this.password = password;

		getViewState();

		getCheckCode();

		List<NameValuePair> nvps = new ArrayList<>();

		nvps.add(new BasicNameValuePair("__VIEWSTATE", viewState));
		nvps.add(new BasicNameValuePair("txtUserName", number));
		nvps.add(new BasicNameValuePair("TextBox2", password));
		nvps.add(new BasicNameValuePair("txtSecretCode", checkCode));
		nvps.add(new BasicNameValuePair("RadioButtonList1", "学生"));
		nvps.add(new BasicNameValuePair("Button1", "登录"));

		HttpPost httPost = new HttpPost("http://jwxt.gcu.edu.cn/default2.aspx");

		httPost.addHeader("Accept", ACCEPT);
		httPost.addHeader("Accept-Encoding", ACCEPT_ENCODING);
		httPost.addHeader("Accept-Language", ACCEPT_LANGUAGE);
		httPost.addHeader("Cache-Control", CACHE_CONTROL);
		httPost.addHeader("Connection", CONNECTION);
		httPost.addHeader("Host", HOST);
		httPost.addHeader("Referer", LOGIN_URL);
		httPost.addHeader("Upgrade-Insecure-Requests", UPGRADE_INSECURE_REQUESTS);
		httPost.addHeader("User-Agent", USER_AGENT);

		try{
		httPost.setEntity(new UrlEncodedFormEntity(nvps, "GB2312"));
		RequestConfig config = RequestConfig.custom().setSocketTimeout(1000).setConnectTimeout(1000)
				.setConnectionRequestTimeout(1000).setRedirectsEnabled(false).build();
		httPost.setConfig(config);
		CloseableHttpResponse loginResponse = client.execute(httPost);
		String resultCode = loginResponse.getStatusLine().toString();
		String body = EntityUtils.toString(loginResponse.getEntity());
        String passwordCode = getResult("(?<=alert\\(').+(?=\\!\\'\\))", body);
        String verificationCode = getResult("(?<=alert\\(').+(?=\\！\\！\\'\\))", body);
        Printer.print("教务系统模拟登录结果反馈", "resultCode"+ resultCode,
				"passwordCode:" + passwordCode, "verificationCode:" + verificationCode,"personInfoUrl:"+ personInfoUrl);
		if (resultCode.equals("HTTP/1.1 302 Found")) {
			HttpGet httpGet = new HttpGet(TARGET_URL + number);
			CloseableHttpResponse targetResponse = client.execute(httpGet);
			String content = EntityUtils.toString(targetResponse.getEntity());
			Printer.print("教务系统抓取的信息内容", "content:"+content.substring(0, 150));
			name = getResult("(?<=\\\"\\>).+(?=同学)", content);
			personInfoUrl = HOME_URL + "xsgrxx.aspx?xh=" + number + "&xm=" + name + "&gnmkdm=N121501";
			getPersonalInfo();
		} else {
			loginResponse.close();
			if (verificationCode != null && body.contains("用户名不存在")) {
				return Code.LOGIN_FAILURE_NUMBER;
			}
			else if (passwordCode != null) {
				return Code.LOGIN_FAILURE_PASSWORD;
			} else if (verificationCode != null && body.contains("验证码")){
				return Code.LOGIN_FAILURE_VERIFICATION;
			}else{
				return Code.UNKNOWN;
			}
		}}catch (IOException e){
			e.printStackTrace();
			return Code.UNKNOWN;
		}
		return Code.LOGIN_SUCCESS;
	}

	public User getPersonalInfo() {

		User user = new User();

		try {
			HttpGet httpGet = new HttpGet(personInfoUrl);
			httpGet.setHeader("Referer", TARGET_URL + number);
			CloseableHttpResponse personResponse = client.execute(httpGet);
			String resultContent = EntityUtils.toString(personResponse.getEntity());
			Printer.print("获取个人信息后得到的内容", resultContent);
			FileIOer.writeString("D:\\content.txt", resultContent);
			String sex = getResult("(?<=\\\"lbl_xb\\\">).+(?=\\<\\/span)", resultContent);
			String phone = getResult("(?<=\\\"lbl_TELNUMBER\\\">).+(?=\\<\\/span)", resultContent);
			user.setUserNumber(number);
			user.setPassword(password);
			user.setName(name);
			user.setGender(sex);
			user.setEmail(phone);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return user;

	}

	private void getViewState() {
		try {
			client = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(LOGIN_URL);
			httpGet.addHeader("Accept", ACCEPT);
			httpGet.addHeader("Accept-Encoding", ACCEPT_ENCODING);
			httpGet.addHeader("Accept-Language", ACCEPT_LANGUAGE);
			httpGet.addHeader("Cache-Control", CACHE_CONTROL);
			httpGet.addHeader("Connection", CONNECTION);
			httpGet.addHeader("Upgrade-Insecure-Requests", UPGRADE_INSECURE_REQUESTS);
			httpGet.addHeader("User-Agent", USER_AGENT);
			httpGet.addHeader("Host", HOST);
			CloseableHttpResponse response = client.execute(httpGet);
			String body = EntityUtils.toString(response.getEntity());
			response.close();
			Document doc = Jsoup.parse(body);
			viewState = doc.getElementsByTag("input").get(0).val();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getCheckCode() {
		try {
			HttpGet httpGet = new HttpGet(HOME_URL + "CheckCode.aspx");
			httpGet.addHeader("Host", HOST);
			httpGet.addHeader("Referer", LOGIN_URL);
			httpGet.addHeader("User-Agent", USER_AGENT);
			CloseableHttpResponse checkCodeResponse = client.execute(httpGet);
			BufferedImage image = ImageIO.read(checkCodeResponse.getEntity().getContent());
			checkCodeResponse.close();
			Map<BufferedImage, String> map = loadTrainOcr();
			checkCode = getAllOcr(getImgBinary(image), map);
			System.out.println(checkCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getResult(String rex, String content) {
		Pattern pattern = Pattern.compile(rex);
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	private static int index = 0;

	public static void downloadImage(int i) throws IOException {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet("http://jw.hzau.edu.cn/CheckCode.aspx");
		CloseableHttpResponse response;
		response = httpClient.execute(httpGet);
		if (response.getStatusLine().getStatusCode() == 200) {
			BufferedInputStream bis = new BufferedInputStream(response.getEntity().getContent());
			FileOutputStream fos = new FileOutputStream(
					new File(System.getProperty("user.dir") + File.separator + "test" + File.separator + i + ".gif"));
			byte[] buff = new byte[1024];
			while (bis.read(buff, 0, 1024) > -1) {
				fos.write(buff);
				fos.flush();
			}
			System.out.println(i + ".gif " + "已保存！！");
			fos.close();
			bis.close();
		}
		response.close();
	}

	private static int isBlack(int colorInt) {
		Color color = new Color(colorInt);
		if (color.getRed() + color.getGreen() + color.getBlue() <= 100) {
			return 1;
		}
		return 0;
	}

	public static int isWhite(int colorInt) {
		Color color = new Color(colorInt);
		if (color.getRed() + color.getGreen() + color.getBlue() > 600) {
			return 1;
		}
		return 0;
	}

	private static int isBlue(int colorInt) {
		Color color = new Color(colorInt);
		int rgb = color.getRed() + color.getGreen() + color.getBlue();
		if (rgb == 153) {
			return 1;
		}
		return 0;
	}

	private static BufferedImage getImgBinary(BufferedImage bi) {
		bi = bi.getSubimage(5, 1, bi.getWidth() - 5, bi.getHeight() - 2);
		bi = bi.getSubimage(0, 0, 50, bi.getHeight());
		int width = bi.getWidth();
		int height = bi.getHeight();
		System.out.println(width + " " + height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = bi.getRGB(i, j);
				if (isBlue(rgb) == 1) {
					bi.setRGB(i, j, Color.BLACK.getRGB());
				} else {
					bi.setRGB(i, j, Color.WHITE.getRGB());
				}
			}
		}
		return bi;
	}

	private static List<BufferedImage> getCharSplit(BufferedImage image) {
		List<BufferedImage> biList = new ArrayList<>();
		int width = image.getWidth() / 4;
		int height = image.getHeight();
		biList.add(image.getSubimage(0, 0, width, height));
		biList.add(image.getSubimage(width, 0, width, height));
		biList.add(image.getSubimage(width * 2, 0, width, height));
		biList.add(image.getSubimage(width * 3, 0, width, height));

		System.out.println(biList.size());
		return biList;
	}

	private static Map<BufferedImage, String> loadTrainOcr() throws IOException {
		Map<BufferedImage, String> map = new HashMap<>();
		File file = new File(System.getProperty("user.dir") + File.separator + "train\\");
		System.out.println("file:" + file.getAbsolutePath());
		File[] files = file.listFiles();
		for (File f : Objects.requireNonNull(files)) {
			map.put(ImageIO.read(f), f.getName().charAt(0) + "");
		}
		System.out.println("训练完毕！！！");
		return map;
	}

	private static String charOcr(BufferedImage image, Map<BufferedImage, String> map) {
		String s = "";
		int width = image.getWidth();
		int height = image.getHeight();
		int min = width * height;
		for (BufferedImage bi : map.keySet()) {
			int count = 0;
			if (Math.abs(bi.getWidth() - width) > 2)
				continue;
			int widthMin = Math.min(width, bi.getWidth());
			int heightMin = Math.min(height, bi.getHeight());
			loop: for (int i = 0; i < widthMin; i++) {
				for (int j = 0; j < heightMin; j++) {
					if (isBlack(bi.getRGB(i, j)) != isBlack(image.getRGB(i, j))) {
						count++;
					}
					if (count >= min)
						break loop;
				}
			}

			if (count < min) {
				min = count;
				s = map.get(bi);
			}
		}
		return s;
	}

	public static void trainOcr() throws IOException {
		File file = new File(System.getProperty("user.dir") + File.separator + "temp\\");
		File[] files = file.listFiles();
		for (File f : Objects.requireNonNull(files)) {
			List<BufferedImage> list = getCharSplit(getImgBinary(ImageIO.read(f)));
			if (list.size() == 4) {
				for (int i = 0; i < list.size(); i++) {
					ImageIO.write(list.get(i), "gif", new File(System.getProperty("user.dir") + File.separator
							+ "train\\" + f.getName().charAt(i) + "-" + (index++) + ".gif"));
				}
			}
		}
		System.out.println("success trained!!");
	}

	private static String getAllOcr(BufferedImage imgBinary, Map<BufferedImage, String> map) {
		StringBuilder result = new StringBuilder();
		List<BufferedImage> splitList = getCharSplit(imgBinary);
		for (BufferedImage img : splitList) {
			result.append(charOcr(img, map));
		}
		return result.toString();
	}

	private static void test(String[] args) throws IOException {
		File fil = new File(System.getProperty("user.dir") + File.separator + "test\\");
		Map<BufferedImage, String> map = loadTrainOcr();
		File[] fiels = fil.listFiles();

		assert fiels != null;
		for (File f : fiels) {
			BufferedImage imgBinary = getImgBinary(ImageIO.read(f));
			System.out.println(getAllOcr(imgBinary, map));
		}

	}

	private static void createCode(String content) {
		String url = "http://qr.liantu.com/api.php?bg=f3f3f3&fg=ff0000&gc=222222&el=l&w=200&m=10&text=" + content;
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);
		try {
			CloseableHttpResponse response = client.execute(get);
			InputStream inputStream = response.getEntity().getContent();
			FileOutputStream outputStream = new FileOutputStream("F:\\code" + content + ".jpg");
			byte[] buffer = new byte[1024];
			int len;
			while ((len = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
			}
			inputStream.close();
			outputStream.close();
			response.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
