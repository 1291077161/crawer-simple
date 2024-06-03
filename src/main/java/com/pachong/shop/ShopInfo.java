package com.pachong.shop;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@ColumnWidth(25)
@AllArgsConstructor
@HeadRowHeight(20)
@ContentRowHeight(18)
public class ShopInfo {


	/**
	 * 店铺名称
	 */
	@ColumnWidth(30)
	@ApiModelProperty("店铺名称")
	@ExcelProperty("店铺名称")
	private String shopName;


	/**
	 * 创立时间
	 */
	// @ColumnWidth(30)
	// @ApiModelProperty("创立时间")
	// @ExcelProperty("创立时间")
	// private String createTime;

	/**
	 * 地址
	 */
	@ColumnWidth(50)
	@ApiModelProperty("地址")
	@ExcelProperty("地址")
	private String site;

	/**
	 * 地址链接
	 */
	@ColumnWidth(50)
	@ApiModelProperty("地址链接")
	@ExcelProperty("地址链接")
	private String siteUrl;

	/**
	 * 店铺链接
	 */
	@ColumnWidth(80)
	@ApiModelProperty("店铺链接")
	@ExcelProperty("店铺链接")
	private String links;

	/**
	 * 年交易额
	 */
	// @ColumnWidth(30)
	// @ApiModelProperty("年交易额")
	// @ExcelProperty("年交易额")
	// private String tradeAmount;


	/**
	 * 厂房面积
	 */
	// @ColumnWidth(30)
	// @ApiModelProperty("厂房面积")
	// @ExcelProperty("厂房面积")
	// private String area;


	/**
	 * 员工总数
	 */
	// @ColumnWidth(30)
	// @ApiModelProperty("员工总数")
	// @ExcelProperty("员工总数")
	// private String number;

	/**
	 * 定制起订量
	 */
	// @ColumnWidth(30)
	// @ApiModelProperty("定制起订量")
	// @ExcelProperty("定制起订量")
	// private String dingZhi;

	/**
	 * 贴牌起订量
	 */
	// @ColumnWidth(30)
	// @ApiModelProperty("贴牌起订量")
	// @ExcelProperty("贴牌起订量")
	// private String tiePai;

	/**
	 * 原材料采购时间
	 */
	// @ColumnWidth(30)
	// @ApiModelProperty("原材料采购时间")
	// @ExcelProperty("原材料采购时间")
	// private String buyTime;


	/**
	 * 相关信息
	 */
	@ColumnWidth(100)
	@ApiModelProperty("相关信息")
	@ExcelProperty("相关信息")
	private String relationInfo;

	/**
	 * 店铺描述
	 */
	@ColumnWidth(120)
	@ApiModelProperty("店铺描述")
	@ExcelProperty("店铺描述")
	private String desc;

}
