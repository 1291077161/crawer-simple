package com.pachong.shop;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONException;
import com.sailheader.mes.common.helper.AssertHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springblade.core.tool.jackson.JsonUtil;
import org.springblade.core.tool.utils.Func;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author YYY
 * @date 2023/12/1
 * @desc 业务执行类
 */

// @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Example {

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static Set<ShopDetailLinks> getRequestFromUrl(String url, int i) throws IOException, JSONException {
		URL firstUrl = new URL(url);
		String protocol = firstUrl.getProtocol();
		String host = firstUrl.getHost();
		String path = firstUrl.getPath();
		String query = firstUrl.getQuery();
		String[] split = query.split("&");
		if (Func.isNotEmpty(split)) {
			for (int i1 = 0; i1 < split.length; i1++) {
				if (split[i1].startsWith("asyncCount")) {
					split[i1] = "asyncCount=20";
				}
				if (split[i1].startsWith("beginPage")) {
					split[i1] = "beginPage=" + i;
				}
			}
			query = Arrays.stream(split).collect(Collectors.joining("&"));
		}
		String real = protocol + "://" + host + path + "?" + query;

		URL realUrl = new URL(real);
		URLConnection conn = realUrl.openConnection();
		String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36";
		String Cookie = "xlly_s=1; t=e531edc3f3e1ca8876fcecb13dfc3666; _tb_token_=e63a9171be3be; cookie2=18d234b9a1dc605038ee70013efc70fc; lid=t_1503557391209_0228; ali_apache_track=c_mid=b2b-3401850809dfc11|c_lid=t_1503557391209_0228|c_ms=1; ali_apache_tracktmp=c_w_signed=Y; last_mid=b2b-3401850809dfc11; __mwb_logon_id__=t_1503557391209_0228; mwb=ng; cna=G3PvHQJo/3UCAXFZ6oPbiA9J; keywordsHistory=%E5%B9%BF%E5%B7%9E%E6%9C%8D%E9%A5%B0%3B%E5%B9%BF%E5%B7%9E%E6%9C%8D%E9%A5%B0%E5%8E%82%3B%E6%9C%8D%E9%A5%B0; sgcookie=E100xltyth4vdV0sKa9eYjB1W2DJ7y1j3YZwEU7yxDQB84uv9RHO4gs80lsPUMn2%2BUclY40mDPiqhpyqfFuerAZFd8bSNJcx1Rp%2FHWolOCkS%2BSA%3D; csg=36692380; uc4=id4=0%40UgP8I7JUe08wbI%2Fe%2BjNIVl40Nhe4&nk4=0%40FbMocxnJdH470je0A8o9%2BUhnyP3DGwrKKekJwbGB%2Fg%3D%3D; _csrf_token=1701359270592; _m_h5_tk=af2af46d03f3519773082d95fbf95d22_1701402755368; _m_h5_tk_enc=08c73e8fd4918705baffcb9b6497145d; __cn_logon__=false; isg=BAMDdiRtYgOPJy72IYYXD-bkkseteJe6bNQ0wDXhfGLL9CMWvUvSC6xlboS61O-y; l=fBEq05mnPFE99gr_BO5Clurza779FIOb4sPzaNbMiIEGa6tROpSbKNCTf0Xy5dtjgTCUietyMAhYGdLHR3AJwxDDBpKMwLUrFxv9-etzR; tfstk=eruJRdDrLKvoDdBamUKDT-USKIdDS4hPlYl1-J2lAxHxOAdztLunDrNTN9ZBUz0KHvM4qJxrteMxTvySx3JzOvhKwL7ka3rQpYymKwcoJm7K_XukayAzzbzURdvMSvhraykZoLwJS-IupLpMIFYmabzURdmoqXJlmICP6H7Guqu3NhUZTqkdgIL44RG7Fbcol3EP4bwxdv0YwjUENh_CRqF82g5oSVhDcO2TtgOvMMSUVS8CiDcfmy85cSeMweIFYoR4MRAvMMSUVSPYIQpRYMr2g";
		conn.setRequestProperty("User-Agent", userAgent);
		conn.setRequestProperty("Accept", "*/*");
		conn.setRequestProperty("connection", "Keep-Alive");
		conn.setRequestProperty("Cookie", Cookie);
		conn.connect();

		InputStream instream = conn.getInputStream();
		Set<ShopDetailLinks> shopDetails = new HashSet<>();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(instream, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			AssertHelper.isException(!jsonText.startsWith("{"), "已被1688反爬机制检测到，请重新换取登陆账号");
			handleResponseData(jsonText, shopDetails);
			return shopDetails;
		} finally {
			instream.close();
		}
	}


	public static Set<ShopDetailLinks> handleResponseData(String url, Set<ShopDetailLinks> links) {
		Map<String, Object> stringObjectMap = JsonUtil.toMap(url);
		if (Func.isNotEmpty(stringObjectMap)) {
			Map<String, Object> data1 = (Map) stringObjectMap.get("data");
			Map<String, Object> data2 = (Map) data1.get("data");
			List data3 = (List) data2.get("companyWithOfferLists");
			for (Object o : data3) {
				String s = JsonUtil.toJson(o);
				Map<String, Object> stringObjectMap1 = JsonUtil.toMap(s);
				Object factoryInfo = stringObjectMap1.get("factoryInfo");
				String s2 = JsonUtil.toJson(factoryInfo);
				Map<String, Object> stringObjectMap2 = JsonUtil.toMap(s2);
				Object factoryInfo2 = stringObjectMap2.get("factoryDetailUrl");
				links.add(new ShopDetailLinks(factoryInfo2.toString()));
			}
		}
		return links;
	}


	public static void main(String[] args) throws IOException, JSONException, InterruptedException {
		Set<ShopDetailLinks> links = new HashSet<>();
		Scanner sc = new Scanner(System.in);
		// System.out.println("请输入需要爬取的网页url地址：");
		// String url = sc.nextLine();
		// while (!url.startsWith("http")) {
		//     System.out.println("请输入正确格式的url地址");
		//     url = sc.nextLine();
		// }

		// System.out.println("请输入需要爬取的网页的页数(数字类型)，数量一次性不能大于10");
		// System.out.println("假设第一次输入的是10，下次再启动输入11即可");
		// int i1 = sc.nextInt();
		// while (i1 > 10 || i1 <= 0) {
		//     System.out.println("请重新输入，数值不能大于10也不能小等于0");
		//     i1 = sc.nextInt();
		// }

		System.out.println("请输入处理的线程数大小（数字类型），线程数量不能大于8");
		int i2 = sc.nextInt();
		while (i2 > 8 || i2 <= 0) {
			System.out.println("请重新输入，数值不能大于8也不能小等于0");
			i2 = sc.nextInt();
		}

		// for (int i = 1; i <= i1; i++) {
		//     System.out.println("----------------------正在爬取第" + i + "页数据---------------------------");
		//     List<ShopDetail> strings = getRequestFromUrl(url, i);
		//     links.addAll(strings);
		//     System.out.println("---------第" + i + "页数据爬取完毕，暂停线程休眠2秒，2秒后继续爬取第" + (i + 1) + "页数据-----------");
		//     TimeUnit.SECONDS.sleep(2);
		// }


		System.out.println("--------------------文本文档读取中----------------");
		// 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
		String pathname = "C:\\测试数据.txt";
		// Package aPackage = Example.class.getPackage();
		// String replace =  aPackage.getName().replace(".", "\\");
		// URL resource = Example.class.getResource("");
		// String path = resource.getPath();
		// String pathname = Example.class.getResource("测试数据.txt").getFile();
		// String pathname = resource.getPath() + "\\测试数据.txt";
		// 要读取以上路径的input。txt文件
		File filename = new File(pathname);
		// 建立一个输入流对象reader
		InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
		// 建立一个对象，它把文件内容转成计算机能读懂的语言
		BufferedReader br = new BufferedReader(reader);
		List<String> responseList = new ArrayList<String>();
		String line = "";
		while (line != null) {
			// 一次读入一行数据
			line = br.readLine();
			if (Func.isNotEmpty(line)) {
				responseList.add(line);
			}
		}

		responseList.forEach(item -> handleResponseData(item, links));
		System.out.println();
		System.out.println("读取文档数据共获取到" + links.size() + "条店铺详情链接");
		System.out.println();
		System.out.println("----------------------店铺详情数据爬取中---------------------------");

		// 多线程爬取
		Thread[] threads = new Thread[i2];
		List<MyThread1> myThreadList = new ArrayList<>();
		ArrayList<ShopDetailLinks> shopDetailLinks = new ArrayList<>(links);
		List<List<ShopDetailLinks>> partition = averageAssign(shopDetailLinks, threads.length);


		for (int i = 0; i < threads.length; i++) {
			MyThread1 myThread = new MyThread1(partition.get(i));
			myThreadList.add(myThread);
			threads[i] = new Thread(myThread, "当前线程名称：" + (i + 1));
		}

		for (Thread value : threads) {
			if (Func.isNotEmpty(value)) {
				System.out.println("线程：" + value.getName() + "启动中");
				value.start();
			}
			TimeUnit.MILLISECONDS.sleep(100);
		}

		// 确保子线程执行完，主线程再执行
		for (Thread thread : threads) {
			thread.join();
		}

		List<ShopInfo> realShopInfo = new ArrayList<ShopInfo>();
		List<ShopDetailLinks> realFailureLinks = new ArrayList<ShopDetailLinks>();

		for (MyThread1 myThread : myThreadList) {
			List<ShopDetailLinks> failureLinks1 = myThread.getFailureLinks();
			List<ShopInfo> shopInfos1 = myThread.getShopInfos();
			realShopInfo.addAll(shopInfos1);
			realFailureLinks.addAll(failureLinks1);
		}


		// if (Func.isNotEmpty(links)) {
		// 	int a = 1;
		// 	for (ShopDetailLinks link : links) {
		// 		// getdizhi(link, shopInfos);
		// 		handleShopInfo(link, shopInfos, failureLinks);
		// 		System.out.println("---------爬取完第" + a + "家店铺详情信息，休眠2秒-----------");
		// 		System.out.println();
		// 		a++;
		// 		TimeUnit.SECONDS.sleep(2);
		//
		// 	}
		// }

		// 测试用
		// handleShopInfo(new ShopDetailLinks("https://sale.1688.com/factory/card.html?memberId=b2b-221653890890368b6b&aHdkaW5n_isCentral=true&aHdkaW5n_isGrayed=false&aHdkaW5n_isUseGray=true"), shopInfos, failureLinks);

		// 控制台换行
		System.out.println();
		System.out.println();
		System.out.println();

		if (Func.isNotEmpty(realShopInfo)) {
			System.out.println();
			System.out.println("--------------共爬取到" + realShopInfo.size() + "条记录----------------");
			System.out.println("--------------店铺详情导出中，默认导出到D盘目录“爬虫”文件下----------------");
			System.out.println();
			writeFileIntoDisk(realShopInfo);
			System.out.println("--------------------------店铺详情导出完毕------------------------------");
			System.out.println();
			TimeUnit.SECONDS.sleep(2);
		}


		if (Func.isNotEmpty(realFailureLinks)) {
			System.out.println();
			System.out.println("--------------爬取失败的链接共有" + realFailureLinks.size() + "条记录----------------");
			System.out.println("--------------爬取失败的链接导出中，默认导出到D盘目录“爬虫”文件下----------------");
			System.out.println();
			writeFailureFileIntoDisk(realFailureLinks);
			System.out.println("--------------------------爬取失败的链接导出完毕------------------------------");
			System.out.println();
			TimeUnit.SECONDS.sleep(2);
		}

	}

	public static <T> List<List<T>> averageAssign(List<T> source, int n) {
		List<List<T>> result = new ArrayList<List<T>>();
		int remaider = source.size() % n;  //(先计算出余数)
		int number = source.size() / n;  //然后是商
		int offset = 0;//偏移量
		for (int i = 0; i < n; i++) {
			List<T> value = null;
			if (remaider > 0) {
				value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
				remaider--;
				offset++;
			} else {
				value = source.subList(i * number + offset, (i + 1) * number + offset);
			}
			result.add(value);
		}
		return result;
	}

	private static void writeFailureFileIntoDisk(List<ShopDetailLinks> failureLinks) {
		File file = new File("D:\\\\爬虫\\\\爬取失败的店铺链接");
		if (!file.exists()) {
			file.mkdir();
		}

		File file2 = new File("D:\\\\爬虫\\\\爬取失败的店铺链接");
		File[] files = file2.listFiles();
		if (Func.isNotEmpty(files) && files.length > 0) {
			String fileName = "D:\\\\爬虫\\\\爬取失败的店铺链接\\\\爬取失败的店铺链接" + (files.length + 1) + ".xlsx";
			System.out.println();
			System.out.println("导出的文件名称为：爬取失败的店铺链接" + (files.length + 1) + ".xlsx");
			System.out.println();
			EasyExcel.write(fileName, ShopDetailLinks.class).sheet("爬取失败的店铺链接").doWrite(failureLinks);
		} else {
			String fileName = "D:\\\\爬虫\\\\爬取失败的店铺链接\\\\爬取失败的店铺链接1.xlsx";
			System.out.println();
			System.out.println("导出的文件名称为：爬取失败的店铺链接1.xlsx");
			System.out.println();
			EasyExcel.write(fileName, ShopDetailLinks.class).sheet("爬取失败的店铺链接").doWrite(failureLinks);
		}
	}

	private static void writeFileIntoDisk(List<ShopInfo> shopInfos) {
		File file = new File("D:\\\\爬虫");
		if (!file.exists()) {
			file.mkdir();
		}

		File file2 = new File("D:\\\\爬虫");
		File[] files = file2.listFiles();
		if (Func.isNotEmpty(files) && files.length > 0) {
			String fileName = "D:\\\\爬虫\\\\1688店铺详情" + (files.length + 1) + ".xlsx";
			System.out.println();
			System.out.println("导出的文件名称为：1688店铺详情" + (files.length + 1) + ".xlsx");
			System.out.println();
			EasyExcel.write(fileName, ShopInfo.class).sheet("1688店铺详情").doWrite(shopInfos);
		} else {
			String fileName = "D:\\\\爬虫\\\\1688店铺详情1.xlsx";
			System.out.println();
			System.out.println("导出的文件名称为：1688店铺详情1.xlsx");
			System.out.println();
			EasyExcel.write(fileName, ShopInfo.class).sheet("1688店铺详情").doWrite(shopInfos);
		}
	}


	public static void handleShopInfo(ShopDetailLinks url, List<ShopInfo> shopInfos, List<ShopDetailLinks> failureLinks, int count) {


		try {
			URL realUrl = new URL(url.getLinks());
			Document detail = Jsoup.parse(realUrl, 3000);
			String title = detail.title();
			String[] split1 = title.split("-");
			String shopName = split1[0];

			Element getById1 = detail.body().getElementById("pc_card_baseinfo");
			Element attr = getById1.attr("component-id", "6");

			Elements links = attr.select("a[href]");
			String initSiteUrl = "";
			for (Element link : links) {
				String href = link.attr("href");
				if (href.contains("uri.amap.com")) {
					initSiteUrl = href;
				}
				String text = link.text();
			}

			String[] split = initSiteUrl.split("&");
			// 地址链接
			String siteUrl = split[0];
			// 地址名称
			String site = split[1].substring(5);


			Element getById2 = detail.body().getElementById("pc_card_exhibition");
			Element attr2 = getById2.attr("component-id", "10");
			// Elements div2 = attr2.getElementsByTag("div");
			Elements info1 = attr2.getElementsByClass("ability_info");
			String element3 = info1.get(0).text();

			// String[] s1 = element3.split(" ");
			// String createTime = s1[1];
			// String tradeAmount = s1[3];
			// String area = s1[5];
			// String number = s1[7];


			String element4 = info1.get(1).text();
			// String[] s2 = element4.split(" ");
			// String dingZhi = s2[1];
			// String tiePai = s2[3];

			String text = "";
			Element getById3 = detail.body().getElementById("pc_card_production");
			if (Func.isNotEmpty(getById3)) {
				Element attr3 = getById3.attr("component-id", "12");
				// Elements div2 = attr2.getElementsByTag("div");
				Elements info2 = attr3.getElementsByClass("ability_info");
				text = info2.get(0).text();
				// String[] s3 = text.split(" ");
				// String buyTime = s3[5];
			}

			String relationInfo = element3 + " " + element4 + " " + text;
			// 获取店铺描述
			String description = detail.select("meta[name=description]").get(0).attr("content");
			ShopInfo shopInfo = new ShopInfo(shopName, site, siteUrl, url.getLinks(), relationInfo, description);

			System.out.println();
			System.out.println("当前正在执行任务的线程:" + Thread.currentThread().getName());
			System.out.println("当前正在爬取的店铺详情链接: " + url.getLinks());
			System.out.println();

			System.out.println("爬取的店铺信息 = " + shopInfo);
			shopInfos.add(shopInfo);

			int sleepSecond = new Random().nextInt(3);
			System.out.println("线程:" + Thread.currentThread().getName() + "---------爬取完第" + count + "家店铺详情信息，休眠" + sleepSecond + "秒-----------");
			System.out.println();
			TimeUnit.SECONDS.sleep(sleepSecond);

		} catch (IOException | InterruptedException e) {
			System.out.println("爬取任务失败的店铺详情链接: " + url.getLinks());
			failureLinks.add(new ShopDetailLinks(url.getLinks()));
		}

	}


}





