package com.txy.web.main.handler;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.txy.capture.task.ExportTimer;
import com.txy.common.bean.AreaInfoBean;
import com.txy.common.bean.PageList;
import com.txy.common.bean.TreeInfoBean;
import com.txy.common.constant.Constant;
import com.txy.common.orm.query.impl.DynamicBean;
import com.txy.common.util.StringUtils;
import com.txy.web.common.util.FTPUtil;
import com.txy.web.frame.proxy.AbstractHandler;
import com.txy.web.main.services.SystemBackInfoServie;

/**
 * 后台管理
 * 
 * @author AFEI
 * 
 */
@Controller
public class SystemBackInfoHandler extends AbstractHandler
{
    
    @Autowired
    private SystemBackInfoServie systemBackInfoServie;
    
    /**
     * 查询行政区域页面左边面板的树
     * 
     * @param model
     * @return
     */
    public AreaInfoBean getAreaListTree()
    {
        AreaInfoBean areaInfoBean = systemBackInfoServie.getAreaListTree();
        return areaInfoBean;
    }
    
    /**
     * 查询行政区域管理页面右侧数据
     * 
     * @return
     */
    public PageList<AreaInfoBean> queryAreaPageRight()
    {
        String parentId = getValue("parentId");
        int start = getInt("page");
        int pageSize = getInt("rows");
        PageList<AreaInfoBean> pageList = systemBackInfoServie.queryAreaPageRight(parentId, start,
                pageSize);
        return pageList;
    }
    
    /**
     * 地面实时观测的要素维护列表
     * 
     * @return
     */
    public PageList<DynamicBean> getElementPageList()
    {
        int start = getInt("page");
        int pageSize = getInt("rows");
        String searchKey = getValue("searchKey");
        return systemBackInfoServie.getElementPageList(start, pageSize, searchKey);
    }
    
    /**
     * 查询传输监控资料配置维护
     * 
     * @return
     */
    public PageList<DynamicBean> getFileMaintenancePageList()
    {
        int start = getInt("page");
        int pageSize = getInt("rows");
        String searchKey = getValue("searchKey");
        return systemBackInfoServie.getFileMaintenancePageList(start, pageSize, searchKey);
    }
    
    /**
     * 删除要素数据
     * 
     * @return
     */
    public String delete()
    {
        int result = 0;
        
        String ids = getValue("id");
        
        if (StringUtils.isEmpty(ids))
        {
            
        }
        else
        {
            result = systemBackInfoServie.delete(ids);
        }
        
        if (result > 0)
        {
            return "{success : true}";
        }
        else
        {
            return "{success : false}";
        }
        
    }
    
    /**
     * 删除数据
     * 
     * @return
     */
    public String deleteFile()
    {
        int result = 0;
        
        String dtype = getValue("dtype");
        String ctype = getValue("ctype");
        
        if (!StringUtils.isEmpty(dtype) && !StringUtils.isEmpty(ctype))
        {
            result = systemBackInfoServie.deleteFile(dtype, ctype);
        }
        
        if (result > 0)
        {
            return "{success : true}";
        }
        return "{success : false}";
        
    }
    
    /**
     * 验证编码是否重复
     */
    public String checkCode()
    {
        String code = getValue("code");
        int count = systemBackInfoServie.checkCode(code);
        if (count > 0)
        {
            return "{success : false}";
        }
        return "{success : true}";
    }
    
    /**
     * 验证资料类型是否重复
     */
    public String checkDtype()
    {
        String dtype = getValue("dtype");
        int count = systemBackInfoServie.checkDtype(dtype);
        if (count > 0)
        {
            return "{success : false}";
        }
        return "{success : true}";
    }
    
    /**
     * 验证资料子类型是否重复
     */
    public String checkCtype()
    {
        String ctype = getValue("ctype");
        int count = systemBackInfoServie.checkCtype(ctype);
        if (count > 0)
        {
            return "{success : false}";
        }
        return "{success : true}";
    }
    
    /**
     * 添加要素数据
     * 
     * @return
     */
    public String addElement()
    {
        String name = getValue("name");
        String code = getValue("code");
        String description = getValue("description");
        
        if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(code))
        {
            systemBackInfoServie.addElement(name, code, description);
            return "{success:true}";
        }
        else
        {
            return "{success:false}";
        }
    }
    
    /**
     * 根据id查询要素数据
     * 
     * @return
     */
    public DynamicBean queryById()
    {
        String id = getValue("id");
        
        return systemBackInfoServie.queryById(id);
    }
    
    /**
     * 修改要素数据
     * 
     * @return
     */
    public String updateElement()
    {
        Map<String, Object> map = getParamMap();
        
        if (null != map.get("id") && !StringUtils.isEmpty(map.get("id").toString()))
        {
            systemBackInfoServie.updateElement(map);
            return "{success:true}";
        }
        else
        {
            return "{success:false}";
        }
    }
    
    public String addFile()
    {
        Map<String, Object> map = getParamMap();
        if (!StringUtils.isEmpty(map.get("dtype").toString())
                && !StringUtils.isEmpty(map.get("ctype").toString()))
        {
            systemBackInfoServie.addFile(map);
            return "{success:true}";
        }
        else
        {
            return "{success:false}";
        }
    }
    
    /**
     * 根据id查询要素数据
     * 
     * @return
     */
    public DynamicBean queryByDtype()
    {
        String dtype = getValue("dtype");
        String ctype = getValue("ctype");
        return systemBackInfoServie.queryByDtype(dtype, ctype);
    }
    
    /**
     * 修改
     * 
     * @return
     */
    public String updateFile()
    {
        Map<String, Object> map = getParamMap();
        if (!StringUtils.isEmpty(map.get("dtype").toString())
                && !StringUtils.isEmpty(map.get("ctype").toString()))
        {
            systemBackInfoServie.updateFile(map);
            return "{success:true}";
        }
        else
        {
            return "{success:false}";
        }
    }
    
    /**
     * 添加行政区域管理的列表
     * 
     * @return
     */
    public String addArea()
    {
        Map<String, Object> map = getParamMap();
        if (systemBackInfoServie.addArea(map) != 0)
        {
            return "{success:true}";
        }
        else
        {
            return "{success:false}";
        }
    }
    
    /**
     * 删除区域下面的区县
     * 
     * @return
     */
    public String deleteArea()
    {
        String id = getValue("id");
        if (systemBackInfoServie.deleteArea(id) != 0)
        {
            return "{success:true}";
        }
        else
        {
            return "{success:false}";
        }
    }
    
    /**
     * 保存自定义的数据
     * 
     * @return
     */
    public String addCustomDataInfo()
    {
        String name = getValue("name");
        String filepath = getValue("filepath");
        String period = getValue("period");
        String format_file = getValue("format_file");
        String username = getValue("username");
        String pwd = getValue("pwd");
        String ip = getValue("ip");
        String rowname = getValue("rowname");
        String area = getValue("area");
        String exploerModel = getValue("exploerModel");
        String storeType = getValue("storeType");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("ID", Constant.COMMON_SEQ);
        param.put("NAME", name);
        param.put("CREATETIME", new Date());
        param.put("FILEPATH", filepath);
        param.put("PERIOD", period);
        param.put("FORMAT_FILE", format_file);
        param.put("MODEL_TYPE", exploerModel);
        
        param.put("STORE_TYPE", storeType);
        param.put("USERNAME", username);
        param.put("IP", ip);
        param.put("PWD", pwd);
        param.put("AREA", area);
        param.put("ROW_NAME", rowname);
        param.put("USER_ID", getCurrentSessionUser().getId());
        int temp = systemBackInfoServie.addCustomDataInfo(param);
        if (0 != temp)
        {
            ExportTimer.addTimer(exploerModel);
            return "{success:true}";
        }
        return "{success:false}";
    }
    
    /**
     * 查询列表
     * 
     * @return
     */
    public PageList<DynamicBean> getCustomDataQueryListData()
    {
        int start = getInt("page");
        int pageSize = getInt("rows");
        //查詢此用戶的定制数据
        String userId = getCurrentSessionUser().getId();
        String searchKey = getValue("searchKey");
        return systemBackInfoServie.getCustomDataQueryListData(start, pageSize, searchKey, userId);
    }
    
    /**
     * @Title: queryCustomDataModels
     * @Description: 查询已经配置的模块
     * @return List<DynamicBean>
     * @author lqy
     * @throws
     */
    public List<Map<String, Object>> queryCustomDataModels()
    {
        return systemBackInfoServie.queryCustomDataModels();
    }
    
    public List<TreeInfoBean> getAreaTree()
    {
        return systemBackInfoServie.getAreaTree();
    }
    
    public List<TreeInfoBean> getRowTree()
    {
        String modelType = getValue("modelType");
        return systemBackInfoServie.getRowTree(modelType);
    }
    
    /**
     * 删除定义数据
     * 
     * @return
     */
    public String deleteCustomDataQuery()
    {
        String id = getValue("id");
        int temp = systemBackInfoServie.deleteCustomDataQuery(id);
        if (0 != temp)
        {
            return "{success:true}";
        }
        return "{success:false}";
    }
    
    /**
     * 查询单条数据
     * 
     * @return
     */
    public DynamicBean getCustomDataInfoById()
    {
        int id = getInt("id");
        return systemBackInfoServie.getTransferMonitorById(id);
    }
    
    /**
     * 修改数据
     * 
     * @return
     */
    public String updateCustomDataInfo()
    {
        String name = getValue("name");
        String filepath = getValue("filepath");
        String period = getValue("period");
        String format_file = getValue("format_file");
        String exploerModel = getValue("exploerModel");
        String id = getValue("id");
        
        String username = getValue("username");
        String pwd = getValue("pwd");
        String ip = getValue("ip");
        String rowname = getValue("rowname");
        String area = getValue("area");
        String storeType = getValue("storeType");
        
        DynamicBean bean = systemBackInfoServie.queryCustomDataModelById(id);
        
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("NAME", name);
        param.put("CREATETIME", new Date());
        param.put("FILEPATH", filepath);
        param.put("PERIOD", period);
        param.put("FORMAT_FILE", format_file);
        param.put("MODEL_TYPE", exploerModel);
        
        param.put("STORE_TYPE", storeType);
        param.put("USERNAME", username);
        param.put("IP", ip);
        param.put("PWD", pwd);
        param.put("AREA", area);
        param.put("ROW_NAME", rowname);
        int temp = systemBackInfoServie.updateCustomDataInfo(param, id);
        if (0 != temp)
        {
            ExportTimer.changeTimer(exploerModel, bean.getValue("model_type"));
            return "{success:true}";
        }
        return "{success:false}";
    }
    
    public String checkStoreSpace()
    {
        
        boolean result = false;
        String username = getValue("username");
        String pwd = getValue("pwd");
        String ip = getValue("ip");
        String filepath = getValue("filepath");
        String storeType = getValue("storeType");
        
        if ("3".equals(storeType))
        {
            result = FTPUtil.checkFtp(ip, username, pwd, filepath);
        }
        else if ("2".equals(storeType))
        {
            result = FTPUtil.checkSmbFile(username, pwd, ip, filepath);
        }
        else
        {
            try
            {
                File file = new File(filepath);
                    if (!file.exists())
                    {
                        result = file.mkdirs();
                    }
                    else
                    {
                        result = true;
                    }
            }
            catch (Exception e)
            {
                result = false;
            }
        }
        return result == true ? "{success:true}" : "{success:false}";
    }
}
