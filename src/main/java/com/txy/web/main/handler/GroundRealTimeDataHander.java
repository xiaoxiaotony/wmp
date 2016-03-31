package com.txy.web.main.handler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.txy.common.bean.PageList;
import com.txy.common.bean.TreeInfoBean;
import com.txy.common.orm.query.impl.DynamicBean;
import com.txy.common.util.StringUtils;
import com.txy.web.common.bean.UserInfoBean;
import com.txy.web.frame.proxy.AbstractHandler;
import com.txy.web.main.services.GroundRealTimeDataService;

/**
 * @ClassName: GroundRealTimeDataHander
 * @Description: 地面实时传输资料
 * @author lqy
 * @date 2015年12月8日 上午9:34:46
 * 
 */
@Controller
public class GroundRealTimeDataHander extends AbstractHandler
{
    @Autowired
    private GroundRealTimeDataService groundRealTimeDataService;
    
    public PageList<DynamicBean> getGroundRealTimeData()
    {
        String time = getValue("time");
        String stationTypes = getValue("stationTypes");
        String stationCode = getValue("stationCode");
        // String area = getValue("area");
        int start = getInt("page");
        int pageSize = getInt("rows");
        String userId = ((UserInfoBean) getSessionAttr("user")).getId();
        PageList<DynamicBean> pagelist = groundRealTimeDataService.getGroundRealTimeData(time,
                stationCode, start, pageSize, stationTypes, userId);
        
        List<Map<String, Object>> shresholds = groundRealTimeDataService.queryThresholdValues();
        
        List<DynamicBean> datas = pagelist.getData();
        for (DynamicBean bean : datas)
        {
            if (bean.getValue("wd") != null)
            {
                bean.add("wd",
                        compare(Integer.parseInt(bean.getValue("wd")) / 10, "wd", shresholds));
            }
            if (bean.getValue("sl") != null)
            {
                bean.add("sl",
                        compare(Integer.parseInt(bean.getValue("sl")) / 10, "sl", shresholds));
            }
            if (bean.getValue("fs") != null)
            {
                bean.add("fs",
                        compare(Integer.parseInt(bean.getValue("fs")) / 10, "fs", shresholds));
            }
            if (bean.getValue("sd") != null)
            {
                bean.add("sd",
                        compare(Integer.parseInt(bean.getValue("sd")) / 10, "sd", shresholds));
            }
            if (bean.getValue("qy") != null)
            {
                bean.add("qy",
                        compare(Integer.parseInt(bean.getValue("qy")) / 10, "qy", shresholds));
            }
        }
        
        return pagelist;
    }
    
    private String compare(int value, String code, List<Map<String, Object>> thresholds)
    {
        for (Map<String, Object> threshold : thresholds)
        {
            if (code.equals(threshold.get("ELEMENTCODE") + ""))
            {
                if (value > Integer.parseInt(threshold.get("ELEMENTVALUE") + ""))
                {
                    return "<font style='color:red;'>" + value + "</font>";
                }
            }
        }
        return value + "";
    }
    
    /**
     * @Title: getListTree
     * @Description: 获取关注的所有树
     * @return List<TreeInfoBean>
     * @author lqy
     * @throws
     */
    public List<TreeInfoBean> getListTree()
    {
        WebApplicationContext webApplicationContext = ContextLoader
                .getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();
        @SuppressWarnings("unchecked")
        // 所有区域
        List<TreeInfoBean> listTree = (List<TreeInfoBean>) servletContext.getAttribute("listTree");
        List<TreeInfoBean> attenTree = new ArrayList<TreeInfoBean>();
        // 获取关注区域
        String userId = ((UserInfoBean) getSessionAttr("user")).getId();
        Set<String> sets = groundRealTimeDataService.getAttenAres(userId);
        if (sets != null && sets.size() > 0)
        {
            for (String areaId : sets)
            {
                for (TreeInfoBean bean : listTree)
                {
                    if (areaId != null && areaId.equals(bean.getId()))
                    {
                        attenTree.add(bean);
                    }
                }
            }
        }
        return attenTree.size() == 0 ? listTree : attenTree;
    }
    
    /**
     * @Title: stationTypes
     * @Description: 站类型
     * @return List<Map<String,Object>>
     * @author lqy
     * @throws
     */
    public List<Map<String, Object>> stationTypes()
    {
        WebApplicationContext webApplicationContext = ContextLoader
                .getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> l_map = (List<Map<String, Object>>) servletContext
                .getAttribute("stationTypes");
        return l_map;
    }
    
    /**
     * @Title: getGroundChartData
     * @Description: 统计数据
     * @return String
     * @author lqy
     * @throws
     */
    public Map<String, String> getGroundChartData()
    {
        String time = getValue("time");
        String stationTypes = getValue("stationTypes");
        String stationCode = getValue("stationCode");
        String userId = ((UserInfoBean) getSessionAttr("user")).getId();
        List<DynamicBean> list = groundRealTimeDataService.getGroundChartData(time, stationCode,
                stationTypes, userId);
        StringBuffer sb1 = new StringBuffer();
        StringBuffer sb1_time = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        StringBuffer sb2_time = new StringBuffer();
        StringBuffer sb3 = new StringBuffer();
        StringBuffer sb3_time = new StringBuffer();
        sb1.append("[");
        sb1_time.append("[");
        sb2.append("[");
        sb2_time.append("[");
        sb3.append("[");
        sb3_time.append("[");
        for (int i = 0; i < list.size(); i++)
        {
            String dataTime = list.get(i).getValue("time");
            String date = new SimpleDateFormat("HH:mm").format(paresDate(dataTime));
            if (list.get(i).getValue("wd") != null && !"".equals(list.get(i).getValue("wd"))
                    && Integer.parseInt(list.get(i).getValue("wd")) < 99999)
            {
                sb1_time.append("'" + date + "'");
                
                sb1.append(Integer.parseInt(list.get(i).getValue("wd")) / 10);
                if (i != list.size() - 1)
                {
                    sb1.append(",");
                    sb1_time.append(",");
                }
                
            }
            if (list.get(i).getValue("sl") != null && !"".equals(list.get(i).getValue("sl"))
                    && Integer.parseInt(list.get(i).getValue("sl")) < 99999)
            {
                sb2_time.append("'" + date + "'");
                sb2.append(Integer.parseInt(list.get(i).getValue("sl")) / 10);
                if (i != list.size() - 1)
                {
                    sb2_time.append(",");
                    sb2.append(",");
                }
            }
            if (list.get(i).getValue("qy") != null && !"".equals(list.get(i).getValue("qy"))
                    && Integer.parseInt(list.get(i).getValue("qy")) < 99999)
            {
                sb3_time.append("'" + date + "'");
                sb3.append(Integer.parseInt(list.get(i).getValue("qy")) / 10);
                if (i != list.size() - 1)
                {
                    sb3_time.append(",");
                    sb3.append(",");
                }
            }
        }
        sb1.append("]");
        sb1_time.append("]");
        sb2.append("]");
        sb2_time.append("]");
        sb3.append("]");
        sb3_time.append("]");
        Map<String, String> map = new HashMap<String, String>();
        map.put("wd", sb1.toString());
        map.put("sl", sb2.toString());
        map.put("qy", sb3.toString());
        map.put("wd_time", sb1_time.toString());
        map.put("sl_time", sb2_time.toString());
        map.put("qy_time", sb3_time.toString());
        return map;
    }
    
    public PageList<DynamicBean> getCimissData()
    {
        
        String time = getValue("time");
        String stationCode = getValue("stationCode");
        String stationTypes = getValue("stationTypes");
        Date startTime = null;
        Date endTime = null;
        
        if (StringUtils.isEmpty(time))
        {
            endTime = new Date();
        }
        else
        {
            try
            {
                endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
        startTime = new Date(endTime.getTime() - 1000 * 60 * 60 * 24);
        PageList<DynamicBean> list = new PageList<DynamicBean>();
        List<DynamicBean> l = groundRealTimeDataService.getCimissData(startTime, endTime,
                stationCode,stationTypes);
        list.setData(l);
        list.setTotal(l.size());
        // PageList<DynamicBean> list = new PageList<DynamicBean>();
        return list;
    }
    
    public Map<String, String> getGroundCimissChartData()
    {
        Date startTime = null;
        Date endTime = null;
        String time = getValue("time");
        if (StringUtils.isEmpty(time))
        {
            endTime = new Date();
        }
        else
        {
            try
            {
                endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
        String stationCode = getValue("stationCode");
        startTime = new Date(endTime.getTime() - 1000 * 60 * 60 * 24);
        List<DynamicBean> list = groundRealTimeDataService.getCimissDataReport(startTime, endTime,
                stationCode);
        // List<DynamicBean> list = new ArrayList<DynamicBean>();
        // getSurfEleByTimeRangeAndStaID
        // DynamicBean bean = new DynamicBean();
        // bean.add("area", "1");
        // bean.add("time", "1");
        // bean.add("zh", "1");
        // bean.add("zm", "1");
        // bean.add("wd", "1");
        // bean.add("sl", "1");
        // bean.add("fs", "1");
        // bean.add("fx", "1");
        // bean.add("sd", "1");
        // bean.add("qy", "1");
        // list.add(bean);
        
        StringBuffer sb1 = new StringBuffer();
        StringBuffer sb1_time = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        StringBuffer sb2_time = new StringBuffer();
        StringBuffer sb3 = new StringBuffer();
        StringBuffer sb3_time = new StringBuffer();
        sb1.append("[");
        sb1_time.append("[");
        sb2.append("[");
        sb2_time.append("[");
        sb3.append("[");
        sb3_time.append("[");
        for (int i = 0; i < list.size(); i++)
        {
            String dateTime = list.get(i).getValue("time");
            String date = new SimpleDateFormat("HH:mm").format(paresDate(dateTime));
            if (list.get(i).getValue("wd") != null && !"".equals(list.get(i).getValue("wd"))
                    && Double.parseDouble(list.get(i).getValue("wd")) < 99999)
            {
                sb1_time.append("'" + date + "'");
                
                sb1.append(Double.parseDouble(list.get(i).getValue("wd")) / 10);
                if (i != list.size() - 1)
                {
                    sb1.append(",");
                    sb1_time.append(",");
                }
                
            }
            if (list.get(i).getValue("sl") != null && !"".equals(list.get(i).getValue("sl"))
                    && Double.parseDouble(list.get(i).getValue("sl")) < 99999)
            {
                sb2_time.append("'" + date + "'");
                sb2.append(Double.parseDouble(list.get(i).getValue("sl")) / 10);
                if (i != list.size() - 1)
                {
                    sb2_time.append(",");
                    sb2.append(",");
                }
            }
            if (list.get(i).getValue("qy") != null && !"".equals(list.get(i).getValue("qy"))
                    && Double.parseDouble(list.get(i).getValue("qy")) < 99999)
            {
                sb3_time.append("'" + date + "'");
                sb3.append(Double.parseDouble(list.get(i).getValue("qy")) / 10);
                if (i != list.size() - 1)
                {
                    sb3_time.append(",");
                    sb3.append(",");
                }
            }
        }
        strUtil(sb1);
        strUtil(sb1_time);
        strUtil(sb2);
        strUtil(sb2_time);
        strUtil(sb3);
        strUtil(sb3_time);
        sb1.append("]");
        sb1_time.append("]");
        sb2.append("]");
        sb2_time.append("]");
        sb3.append("]");
        sb3_time.append("]");
        Map<String, String> map = new HashMap<String, String>();
        map.put("wd", sb1.toString());
        map.put("sl", sb2.toString());
        map.put("qy", sb3.toString());
        map.put("wd_time", sb1_time.toString());
        map.put("sl_time", sb2_time.toString());
        map.put("qy_time", sb3_time.toString());
        return map;
    }
    
    public void strUtil(StringBuffer sb)
    {
        char c = sb.charAt(sb.length() - 1);
        if (c == ',')
        {
            int index = sb.toString().lastIndexOf(',');
            sb.deleteCharAt(index);
        }
    }
    
    /**
     * 查询要素阀值
     * 
     * @return
     */
    public List<Map<String, Object>> queryThresholdValue()
    {
        List<Map<String, Object>> value = groundRealTimeDataService.queryThresholdValues();
        return value;
    }
    
    public Date paresDate(String time)
    {
        Date date = null;
        try
        {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
        }
        catch (ParseException e)
        {
            date =  new Date();
        }
        return date;
    }
    
}
