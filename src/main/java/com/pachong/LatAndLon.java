package com.pachong;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.pachong.latandlon.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springblade.core.tool.jackson.JsonUtil;
import org.springblade.core.tool.utils.Func;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author YYY
 * @date 2023/12/1
 * @desc 业务执行类
 */

public class LatAndLon {

	// final static String AK = "edGc5mIugVxx7lwUx9YpraKeWmExG64o";
	// final static String AK = "NOLdfy1eKDKeEbe7hYBVGFmr3qGqYrUg";
	final static String AK = "e723d5b92f1dad1ea06871bf9180925e";

	/**
	 * 地理编码 URL
	 */
	// final static String ADDRESS_TO_LONGITUDEA_URL = "http://api.map.baidu.com/geocoder/v2?output=json&location=showLocation";
	// final static String ADDRESS_TO_LONGITUDEA_URL = "https://api.map.baidu.com/geocoding/v3/?output=json&callback=showLocation&t=20231026185850";
	final static String ADDRESS_TO_LONGITUDEA_URL = "http://restapi.amap.com/v3/geocode/geo?&output=JSON";


	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		System.out.println("请输入处理的线程数大小（数字类型），线程数量不能大于8也不能小等于0");
		int i2 = sc.nextInt();
		while (i2 > 8 || i2 <= 0) {
			System.out.println("请重新输入，数值不能大于8也不能小等于0");
			i2 = sc.nextInt();
		}

		// 多线程爬取
		Thread[] threads = new Thread[i2];
		List<MyThread> myThreadList = new ArrayList<>();


		System.out.println("============读文件==============");
		//待解析的文件路径
		String filePath = "D:\\区域.xlsx";
		//第一个sheet,从第0行开始读取数据
		Sheet sheet = new Sheet(1, 1, CompanyInfo.class);
		//创建自定义的监听对象，后面要用这个对象去获取解析的数据
		ExcelModelListener attrDataListener = new ExcelModelListener();
		try {
			//传参，进行解析；会自动调用自定义监听器中的invoke方法
			EasyExcelFactory.readBySax(new FileInputStream(new File(filePath)), sheet, attrDataListener);
			//得到解析的数据
			List<CompanyInfo> data = attrDataListener.getData();
			int size = data.size();
			System.out.println("解析Excel文件，共有:" + size + "条");
			// AssertHelper.isException(size >= 5000, "excel表格条数不能大于5000");

			// 将数据拆分
			List<List<CompanyInfo>> partition = averageAssign(data, threads.length);
			System.out.println();
			System.out.println("集合被拆成：" + partition.size() + "份");
			System.out.println();
			TimeUnit.SECONDS.sleep(1);

			for (int i = 0; i < threads.length; i++) {
				MyThread myThread = new MyThread(partition.get(i));
				myThreadList.add(myThread);
				threads[i] = new Thread(myThread, "当前线程名称：" + (i + 1));
			}
			for (Thread value : threads) {
				if (Func.isNotEmpty(value)) {
					System.out.println(value.getName() + "启动中");
					value.start();
				}
				TimeUnit.MILLISECONDS.sleep(500);
			}
			// 确保子线程执行完，主线程再执行
			for (Thread thread : threads) {
				thread.join();
			}

			System.out.println();
			System.out.println("多线程执行完毕");
			System.out.println();
			TimeUnit.SECONDS.sleep(2);

			List<CompanyInfo> realNullCompanyInfo = new ArrayList<CompanyInfo>();
			List<CompanyInfo> realFailureCompanyInfo = new ArrayList<CompanyInfo>();
			List<CompanyInfoDetail> realCompanyInfoDetail = new ArrayList<CompanyInfoDetail>();

			for (MyThread myThread : myThreadList) {
				List<CompanyInfoDetail> companyInfoDetails = myThread.getCompanyInfoDetails();
				List<CompanyInfo> failureCompanyInfo = myThread.getFailureCompanyInfo();
				List<CompanyInfo> nullSiteCompanyInfo = myThread.getNullSiteCompanyInfo();
				realCompanyInfoDetail.addAll(companyInfoDetails);
				realFailureCompanyInfo.addAll(failureCompanyInfo);
				realNullCompanyInfo.addAll(nullSiteCompanyInfo);
			}

			// 控制台换行
			System.out.println();
			System.out.println();
			System.out.println();

			if (Func.isNotEmpty(realCompanyInfoDetail)) {
				System.out.println();
				System.out.println("--------------共爬取到" + realCompanyInfoDetail.size() + "条记录----------------");
				System.out.println("--------------工厂地址导出中，默认导出到D盘目录“工厂地址”文件下----------------");
				System.out.println();
				writeFileIntoDisk(realCompanyInfoDetail);
				System.out.println("--------------------------工厂地址导出完毕------------------------------");
				System.out.println();
				TimeUnit.SECONDS.sleep(2);
			}

			if (Func.isNotEmpty(realFailureCompanyInfo)) {
				System.out.println();
				System.out.println("--------------爬取失败的链接共有" + realFailureCompanyInfo.size() + "条记录----------------");
				System.out.println("--------------爬取失败的链接导出中，默认导出到D盘目录“工厂地址”文件下----------------");
				System.out.println();
				writeFailureFileIntoDisk(realFailureCompanyInfo);
				System.out.println("--------------------------爬取失败的信息导出完毕------------------------------");
				System.out.println();
				TimeUnit.SECONDS.sleep(2);
			}

			if (Func.isNotEmpty(realNullCompanyInfo)) {
				System.out.println();
				System.out.println("--------------地址为空的链接共有" + realNullCompanyInfo.size() + "条记录----------------");
				System.out.println("--------------地址为空的链接导出中，默认导出到D盘目录“工厂地址”文件下----------------");
				System.out.println();
				writeNullFileIntoDisk(realNullCompanyInfo);
				System.out.println("--------------------------地址为空的信息导出完毕------------------------------");
				System.out.println();
				TimeUnit.SECONDS.sleep(2);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	private static void writeFileIntoDisk(List<CompanyInfoDetail> realCompanyInfoDetail) {
		File file = new File("D:\\\\工厂地址");
		if (!file.exists()) {
			file.mkdir();
		}

		File file2 = new File("D:\\\\工厂地址");
		File[] files = file2.listFiles();
		if (Func.isNotEmpty(files) && files.length > 0) {
			String fileName = "D:\\\\工厂地址\\\\工厂详情" + (files.length + 1) + ".xlsx";
			System.out.println();
			System.out.println("导出的文件名称为：工厂详情" + (files.length + 1) + ".xlsx");
			System.out.println();
			EasyExcel.write(fileName, CompanyInfoDetail.class).sheet("工厂详情").doWrite(realCompanyInfoDetail);
		} else {
			String fileName = "D:\\\\工厂地址\\\\工厂详情1.xlsx";
			System.out.println();
			System.out.println("导出的文件名称为：工厂详情1.xlsx");
			System.out.println();
			EasyExcel.write(fileName, CompanyInfoDetail.class).sheet("工厂详情").doWrite(realCompanyInfoDetail);
		}
	}


	private static void writeFailureFileIntoDisk(List<CompanyInfo> nullSiteCompanyInfo) {
		File file = new File("D:\\\\工厂地址\\\\爬取失败的工厂信息");
		if (!file.exists()) {
			file.mkdir();
		}

		File file2 = new File("D:\\\\工厂地址\\\\爬取失败的工厂信息");
		File[] files = file2.listFiles();
		if (Func.isNotEmpty(files) && files.length > 0) {
			String fileName = "D:\\\\工厂地址\\\\爬取失败的工厂信息\\\\爬取失败的工厂信息" + (files.length + 1) + ".xlsx";
			System.out.println();
			System.out.println("导出的文件名称为：爬取失败的工厂信息" + (files.length + 1) + ".xlsx");
			System.out.println();
			EasyExcel.write(fileName, CompanyInfo.class).sheet("爬取失败的工厂信息").doWrite(nullSiteCompanyInfo);
		} else {
			String fileName = "D:\\\\工厂地址\\\\爬取失败的工厂信息\\\\爬取失败的工厂信息1.xlsx";
			System.out.println();
			System.out.println("导出的文件名称为：爬取失败的工厂信息1.xlsx");
			System.out.println();
			EasyExcel.write(fileName, CompanyInfo.class).sheet("爬取失败的工厂信息").doWrite(nullSiteCompanyInfo);
		}
	}

	private static void writeNullFileIntoDisk(List<CompanyInfo> nullSiteCompanyInfo) {
		File file = new File("D:\\\\工厂地址\\\\地址为空的工厂信息");
		if (!file.exists()) {
			file.mkdir();
		}

		File file2 = new File("D:\\\\工厂地址\\\\地址为空的工厂信息");
		File[] files = file2.listFiles();
		if (Func.isNotEmpty(files) && files.length > 0) {
			String fileName = "D:\\\\工厂地址\\\\地址为空的工厂信息\\\\地址为空的工厂信息" + (files.length + 1) + ".xlsx";
			System.out.println();
			System.out.println("导出的文件名称为：地址为空的工厂信息" + (files.length + 1) + ".xlsx");
			System.out.println();
			EasyExcel.write(fileName, CompanyInfo.class).sheet("地址为空的工厂信息").doWrite(nullSiteCompanyInfo);
		} else {
			String fileName = "D:\\\\工厂地址\\\\地址为空的工厂信息\\\\地址为空的工厂信息1.xlsx";
			System.out.println();
			System.out.println("导出的文件名称为：地址为空的工厂信息1.xlsx");
			System.out.println();
			EasyExcel.write(fileName, CompanyInfo.class).sheet("地址为空的工厂信息").doWrite(nullSiteCompanyInfo);
		}
	}


	/**
	 * 地理编码
	 *
	 * @param companyInfo
	 * @return
	 */
	public static void addressTolongitudea(CompanyInfo companyInfo, List<CompanyInfo> failureCompanyInfo,
	                                       List<CompanyInfoDetail> companyInfoDetails, int count) {

		System.out.println();
		System.out.println("当前正在执行任务的线程:" + Thread.currentThread().getName());
		System.out.println("当前正在爬取的工厂信息: " + companyInfo);
		System.out.println();

		String url = ADDRESS_TO_LONGITUDEA_URL + "&key=" + AK + "&address=" + companyInfo.getAddress();
		// String url = ADDRESS_TO_LONGITUDEA_URL + "&ak=" + AK + "&address=" + address;
		// 创建默认http连接
		CloseableHttpClient client = HttpClients.createDefault();
		// 创建一个post请求
		HttpGet post = new HttpGet();
		post.setURI(URI.create(url));
		// post.setHeader("Referer", "tianyancha.com");
		try {
			// 用http连接去执行get请求并且获得http响应
			CloseableHttpResponse response = client.execute(post);
			// 从response中取到响实体
			HttpEntity entity = response.getEntity();
			// 把响应实体转成文本
			String html = EntityUtils.toString(entity);
			System.out.println("地址信息 = " + html);
			Map<String, Object> stringObjectMap = JsonUtil.toMap(html);
			Object geoCodes1 = stringObjectMap.get("geocodes");
			String geoCodes2 = JsonUtil.toJson(geoCodes1);
			String geocode = geoCodes2.substring(1, geoCodes2.length() - 1);
			System.out.println("geocode =========== " + geocode);
			CompanyInfoDTO parse = JsonUtil.parse(geocode, CompanyInfoDTO.class);
			initProperties(companyInfo, parse, companyInfoDetails);

			int sleepSecond = new Random().nextInt(5);
			System.out.println(Thread.currentThread().getName() + "---------爬取完第" + count + "家工厂地址信息，休眠" + sleepSecond + "秒-----------");
			System.out.println();
			TimeUnit.SECONDS.sleep(sleepSecond);

		} catch (Exception e) {
			System.out.println("爬取异常的工厂信息：" + companyInfo);
			// 爬取异常的
			failureCompanyInfo.add(companyInfo);
		}
	}

	private static void initProperties(CompanyInfo companyInfo, CompanyInfoDTO parse, List<CompanyInfoDetail> companyInfoDetails) {
		CompanyInfoDetail companyInfoDetail = new CompanyInfoDetail();
		if (Func.isNotEmpty(parse)) {
			companyInfoDetail.setAdcode(parse.getAdcode());
			companyInfoDetail.setStreet(parse.getStreet());
			// companyInfoDetail.setNumber(parse.getNumber());
			String location = parse.getLocation();
			if (Func.isNotEmpty(location)) {
				String[] split = location.split(",");
				// 经度
				companyInfoDetail.setLongitude(split[0]);
				// 纬度
				companyInfoDetail.setLatitude(split[1]);
			}
		}
		companyInfoDetail.setAddress(companyInfo.getAddress());
		companyInfoDetail.setCompanyName(companyInfo.getCompanyName());
		companyInfoDetail.setCompanyWebSite(companyInfo.getCompanyWebSite());
		companyInfoDetail.setPageSite(companyInfo.getPageSite());
		companyInfoDetails.add(companyInfoDetail);
	}


	public static <T> List<List<T>> averageAssign(List<T> source, int n) {
		List<List<T>> result = new ArrayList<List<T>>();
		//(先计算出余数)
		int remaider = source.size() % n;
		//然后是商
		int number = source.size() / n;
		//偏移量
		int offset = 0;
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

}



