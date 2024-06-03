package com.pachong.latandlon;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公司详情
 * @author Joy
 * @date 2024/6/3
 * @param
 *
 */

@Data
@ColumnWidth(25)
@AllArgsConstructor
@NoArgsConstructor
@HeadRowHeight(20)
@ContentRowHeight(18)
public class CompanyInfoDetail {


	/**
	 * 企业名称
	 */
	@ColumnWidth(30)
	@ApiModelProperty("企业名称")
	@ExcelProperty("企业名称")
	private String companyName;

	/**
	 * 注册地址
	 */
	@ColumnWidth(50)
	@ApiModelProperty("注册地址")
	@ExcelProperty("注册地址")
	private String address;


	/**
	 * 页面网址
	 */
	@ColumnWidth(50)
	@ApiModelProperty("页面网址")
	@ExcelProperty("页面网址")
	private String pageSite;


	/**
	 * 公司网站
	 */
	@ColumnWidth(50)
	@ApiModelProperty("公司网站")
	@ExcelProperty("公司网站")
	private String companyWebSite;


	/**
	 * 道路
	 */
	@ColumnWidth(50)
	@ApiModelProperty("道路")
	@ExcelProperty("道路")
	private String street;

	/**
	 * 号数
	 */
	@ColumnWidth(50)
	@ApiModelProperty("号数")
	@ExcelProperty("号数")
	private String number;


	/**
	 * 经度
	 */
	@ColumnWidth(50)
	@ApiModelProperty("经度")
	@ExcelProperty("经度")
	private String longitude;

	/**
	 * 纬度
	 */
	@ColumnWidth(50)
	@ApiModelProperty("纬度")
	@ExcelProperty("纬度")
	private String latitude;

	/**
	 * 邮政编码
	 */
	@ColumnWidth(50)
	@ApiModelProperty("邮政编码")
	@ExcelProperty("邮政编码")
	private String adcode;


}