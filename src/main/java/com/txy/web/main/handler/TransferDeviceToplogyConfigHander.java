package com.txy.web.main.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.txy.common.orm.query.impl.DynamicBean;
import com.txy.web.common.bean.UserInfoBean;
import com.txy.web.frame.proxy.AbstractHandler;
import com.txy.web.main.services.TransferDeviceToplogyConfigService;

@Controller
public class TransferDeviceToplogyConfigHander extends AbstractHandler {

	@Autowired
	private TransferDeviceToplogyConfigService transferDeviceToplogyConfigService;

	public List<DynamicBean> queryDeviceArea() {
		UserInfoBean user = getCurrentSessionUser();
		String areas = user.getAttentionArea();
		List<DynamicBean> pageList = transferDeviceToplogyConfigService
				.queryDeviceArea(areas);
		return pageList;
	}

	public List<DynamicBean> queryAreaNodes() {
		String areaId = getString("areaId");
		List<DynamicBean> pageList = transferDeviceToplogyConfigService
				.queryAreaNodes(areaId);
		return pageList;
	}

}
