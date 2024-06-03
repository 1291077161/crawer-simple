package com.pachong.latandlon;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.metadata.BaseRowModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 *
 * @author Joy
 * @date 2024/6/3
 * @param
 *
 */

@Data
@ColumnWidth(25)
@AllArgsConstructor
@HeadRowHeight(20)
@ContentRowHeight(18)
@NoArgsConstructor
public class CompanyInfo extends BaseRowModel {


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

}