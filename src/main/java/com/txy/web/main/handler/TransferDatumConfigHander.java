package com.txy.web.main.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.txy.common.bean.PageList;
import com.txy.common.bean.TreeInfoBean;
import com.txy.common.orm.query.impl.DynamicBean;
import com.txy.web.frame.proxy.AbstractHandler;
import com.txy.web.main.services.TransferDatumConfigService;

/**
 * 资料传输流程配置控制类
 * 
 * @author AFEI
 *
 */
@Controller
public class TransferDatumConfigHander extends AbstractHandler {

	@Autowired
	private TransferDatumConfigService transferDatumConfigService;

	/**
	 * 查询区域站点信息
	 * 
	 * @return
	 */
	public List<TreeInfoBean> queryAreaStations() {
		int type = getInt("type");
		return transferDatumConfigService.queryAreaStations(type);
	}

	/**
	 * 查询所有节点信息
	 * 
	 * @return
	 */
	public List<TreeInfoBean> queryNodes() {
		return transferDatumConfigService.queryNodes();
	}

	
	/**
	 * 保存站点与节点的关系信息
	 * @return
	 */
	public String savaSatationNodeRef(){
		String leftSataion = getValue("leftSataion");
		String rigthNode = getValue("rigthNode");
		boolean flag = transferDatumConfigService.savaSatationNodeRef(leftSataion,rigthNode);
		if(flag){
			return "{success:true}";
		}
		return "{success:false}";
	}
	
	
	/**
	 * 获取传输节点的配置列表信息
	 * @return
	 */
	public PageList<DynamicBean> getTransferDatumConfigPage(){
		int start = getInt("page");
		int pageSize = getInt("rows");
		return transferDatumConfigService.getTransferDatumConfigPage(start,pageSize);
	}
	
	
	/**
	 * 获取配置预览详情
	 * @return
	 */
	public List<DynamicBean> getPreviewTransferConfig(){
		String config_id = getValue("config_id");
		return transferDatumConfigService.getPreviewTransferConfig(config_id);
	}
	
	
	/**
	 * 撤销配置信息
	 * @return
	 */
	public String revokeConfigInfo(){
		String config_id = getValue("config_id");
		int result = transferDatumConfigService.revokeConfigInfo(config_id);
		if(result!=0){
			return "{success:true}";
		}
		return "{success:false}";
	}
}
