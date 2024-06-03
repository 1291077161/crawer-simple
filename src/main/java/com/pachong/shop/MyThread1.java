package com.pachong.shop;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class MyThread1 implements Runnable {
	private List<ShopInfo> shopInfos = new ArrayList<>();

	private List<ShopDetailLinks> failureLinks = new ArrayList<>();

	private List<ShopDetailLinks> url;

	public MyThread1(List<ShopDetailLinks> url) {
		this.url = url;
	}

	@Override
	public void run() {
		for (int i = 0; i < url.size(); i++) {
			Example.handleShopInfo(url.get(i), shopInfos, failureLinks, i);
		}
	}
}