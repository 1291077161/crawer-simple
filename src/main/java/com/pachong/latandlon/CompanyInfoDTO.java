package com.pachong.latandlon;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author Joy
 * @date 2023/12/14
 * @param
 *
 */

@Data
@AllArgsConstructor
public class CompanyInfoDTO {


	/**
	 * 地址
	 */
	@ApiModelProperty("地址")
	private String formatted_address;


	/**
	 * 道路
	 */

	@ApiModelProperty("道路")
	private String street;

	// /**
	//  * 号数
	//  */
	// @ApiModelProperty("号数")
	// private String number;


	/**
	 * 经纬度
	 */
	@ApiModelProperty("经纬度")
	private String location;


	/**
	 * 邮政编码
	 */
	@ApiModelProperty("邮政编码")
	private String adcode;

	/**
	 * 级别
	 */
	@ApiModelProperty("级别")
	private String level;


}