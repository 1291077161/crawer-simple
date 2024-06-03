package com.pachong.latandlon;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/***
 *  监听器
 */
public class ExcelModelListener extends AnalysisEventListener<CompanyInfo> {

	/**
	 * 用于接收解析的所有数据
	 */
	private final List<CompanyInfo> data = new ArrayList<>();

	/**
	 * 提供一个对外访问的方法
	 * @return
	 */
	public List<CompanyInfo> getData() {
		return data;
	}

	/**
	 * 记录解析的数据总数
	 */
	int count = 0;

	/**
	 * easyexcel解析方式为一行一行解析，而每解析一行都会调用invoke方法
	 * 该方法的第个参数位解析这一行的结果
	 * @param objects
	 * @param analysisContext
	 */

	@Override
	public void invoke(CompanyInfo objects, AnalysisContext analysisContext) {
		//将这一行的解析结果添加到总数据集中
		data.add(objects);
		count++;
	}


	/**
	 * 所有都解析完毕后要执行的操作
	 * @param analysisContext
	 */
	@Override
	public void doAfterAllAnalysed(AnalysisContext analysisContext) {
		System.out.println("解析完毕，共" + (count) + "条数据");
	}


}