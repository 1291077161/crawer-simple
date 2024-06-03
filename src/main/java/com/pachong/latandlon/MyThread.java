package com.pachong.latandlon;

import com.pachong.LatAndLon;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springblade.core.tool.utils.Func;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class MyThread implements Runnable {
	private List<CompanyInfo> failureCompanyInfo = new ArrayList<>();
	private List<CompanyInfoDetail> companyInfoDetails = new ArrayList<>();
	private List<CompanyInfo> nullSiteCompanyInfo = new ArrayList<>();
	private List<CompanyInfo> companyInfos;

	public MyThread(List<CompanyInfo> companyInfos) {
		this.companyInfos = companyInfos;
	}

	@Override
	public void run() {
		for (int i = 0; i < companyInfos.size(); i++) {
			CompanyInfo companyInfo = companyInfos.get(i);
			if (Func.isNotEmpty(companyInfo.getAddress())) {
				LatAndLon.addressTolongitudea(companyInfo, failureCompanyInfo, companyInfoDetails, i);
			} else {
				nullSiteCompanyInfo.add(companyInfo);
			}
		}
	}
}