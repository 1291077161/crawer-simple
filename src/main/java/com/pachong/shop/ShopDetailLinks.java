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
public class ShopDetailLinks {

	/**
	 * 店铺详情链接
	 */
	@ApiModelProperty("店铺详情链接")
	@ExcelProperty("店铺详情链接")
	private String links;

}
