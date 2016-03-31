package com.txy.web.main.handler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.txy.common.bean.PageList;
import com.txy.common.orm.query.impl.DynamicBean;
import com.txy.common.util.DateFormatUtil;
import com.txy.web.common.bean.UserInfoBean;
import com.txy.web.common.util.DutyExcelUtil;
import com.txy.web.frame.proxy.AbstractHandler;
import com.txy.web.main.services.DutyService;

/**
 * @ClassName: DutyHander
 * @Description: 值班Hander
 * @author lqy
 * @date 2015年11月12日 下午3:54:26
 * 
 */
@Controller
public class DutyHander extends AbstractHandler
{
    
    @Autowired
    private DutyService dutyService;
    
    public PageList<DynamicBean> getDuty()
    {
        int start = getInt("page");
        int pageSize = getInt("rows");
        PageList<DynamicBean> list = dutyService.queryDuty(start, pageSize);
        return list;
    }
    
    public List<Map<String, Object>> getDictListVal()
    {
        List<Map<String, Object>> l_map = dutyService.getDutyUserMap();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("selected", true);
        map.put("USERNAME", "所有");
        map.put("USERID", "");
        l_map.add(map);
        return l_map;
    }
    
    public PageList<DynamicBean> getUserDutyRecord()
    {
        int start = getInt("page");
        int pageSize = getInt("rows");
        String userId = getString("userId");
        String startDate = getString("startDate");
        String endDate = getString("endDate");
        if (StringUtils.isBlank(startDate))
        {
            startDate = DateFormatUtil.formatDate(new Date());
        }
        if (StringUtils.isBlank(endDate))
        {
            endDate = DateFormatUtil.formatDate(new Date());
        }
        return dutyService.getUserDutyRecord(start, pageSize, userId, startDate, endDate);
    }
    
    /**
     * types m : 月 ，q: 季度 ，y: 年 scope 月格式2015-11 ， 季度：201501，年：2015
     * 
     * @return
     */
    public PageList<DynamicBean> queryDutyStatistics()
    {
        int start = getInt("page");
        int pageSize = getInt("rows");
        String types = getString("types");
        String scope = getString("dates");
        if (StringUtils.isBlank(types))
        {
            types = "m";
        }
        if (StringUtils.isBlank(scope))
        {
            scope = DateFormatUtil.formatDate(new Date());
        }
        
        Date startDate = null;
        Date endDate = null;
        if ("m".equals(types))
        {
            startDate = DateFormatUtil.getFirstDayOfMonth(DateFormatUtil
                    .parseDate(scope, "yyyy-MM"));
            endDate = DateFormatUtil.getLastDayOfMonth(DateFormatUtil.parseDate(scope, "yyyy-MM"));
        }
        else if ("q".equals(types))
        {
            // String n = scope.substring(scope.length()-1, scope.length());
            // String y = scope.substring(0,3);
            // String qdate = "";
            // if("1".equals(n)){
            // qdate = String.format("%s-03", y);
            // }else if("2".equals(n)){
            // qdate = String.format("%s-06", y);
            // }else if("3".equals(n)){
            // qdate = String.format("%s-09", y);
            // }else if("4".equals(n)){
            // qdate = String.format("%s-12", y);
            // }
            startDate = DateFormatUtil.getFirstDayOfQuarter(DateFormatUtil.parseDate(
                    DateFormatUtil.getSpecifiedMonthAfter(scope, -3), "yyyy-MM"));
            endDate = DateFormatUtil
                    .getLastDayOfQuarter(DateFormatUtil.parseDate(scope, "yyyy-MM"));
        }
        else if ("y".equals(types))
        {
            startDate = DateFormatUtil
                    .getFirstDayOfYear(DateFormatUtil.parseDate(scope, "yyyy-MM"));
            endDate = DateFormatUtil.getLastDayOfYear(DateFormatUtil.parseDate(scope, "yyyy-MM"));
        }
        PageList<DynamicBean> list = dutyService.queryDutyStatistics(startDate, endDate, start,
                pageSize);
        List<DynamicBean> data = list.getData();
        if (null != data && data.size() > 0)
        {
            for (DynamicBean dynamicBean : data)
            {
                if ("m".equals(types))
                {
                    dynamicBean.add("dates", scope);
                }
                else if ("q".equals(types))
                {
                    dynamicBean.add("dates", getQ(DateFormatUtil.parseDate(scope, "yyyy-MM")));
                }
                else if ("y".equals(types))
                {
                    dynamicBean.add("dates", scope.split("-")[0]);
                }
            }
        }
        return list;
    }
    
    private String getQ(Date nowdate)
    {
        int q = DateFormatUtil.getSeason(nowdate);
        int y = DateFormatUtil.getYear(nowdate);
        return String.format("%s(%s季度)", y, q);
    }
    
    public boolean addDuty()
    {
        String selectDates = getString("dates");
        dutyService.addDuty(selectDates);
        return true;
    }
    
    public List<DynamicBean> getDutyInfoByMonth()
    {
        String startDate = getString("startDate");
        String endDate = getString("endDate");
        return dutyService.getDutyInfoByMonth(startDate, endDate);
    }
    
    /**
     * 上下班签到
     */
    public void workDuty()
    {
        String dutyType = getString("dutyType");
        String userId = ((UserInfoBean) getSessionAttr("user")).getId();
        String nowDate = DateFormatUtil.formatDate(new Date());
        dutyService.workDuty(dutyType, userId, nowDate);
    }
    
    /**
     * 查询值班记录
     * 
     * @return
     */
    public PageList<DynamicBean> getDutyInfoByUser()
    {
        String userId = getString("userId");
        String queryDate = getString("dates");
        int start = getInt("page");
        int pageSize = getInt("rows");
        return dutyService.getDutyInfoByUser(userId, queryDate, start, pageSize);
    }
    
    /**
     * 查询故障记录
     * 
     * @return
     */
    public PageList<DynamicBean> getDutyErrorByUser()
    {
        String userId = getString("userId");
        String queryDate = getString("dates");
        int start = getInt("page");
        int pageSize = getInt("rows");
        return dutyService.getDutyErrorByUser(userId, queryDate, start, pageSize);
    }
    
    /**
     * 值班记录检索
     * 
     * @return
     */
    public PageList<DynamicBean> getUserDutyRecordretrieval()
    {
        int start = getInt("page");
        int pageSize = getInt("rows");
        String userId = getString("userId");
        String startDate = getString("startDate");
        String endDate = getString("endDate");
        if (StringUtils.isBlank(startDate))
        {
            startDate = DateFormatUtil.formatDate(new Date());
        }
        if (StringUtils.isBlank(endDate))
        {
            endDate = DateFormatUtil.formatDate(new Date());
        }
        
        return dutyService.getUserDutyRecordretrieval(start, pageSize, userId, startDate, endDate);
    }
    
    public PageList<DynamicBean> getUserDutyRecordretrievalID()
    {
        String id = getString("id");
        return dutyService.getUserDutyRecordretrievalId(id);
    }
    
    /**
     * 获取用户上下班登记情况
     * 
     * @return
     */
    public int getDutyRecordByUser()
    {
        
        String userId = getString("userId");
        String seq = getString("seq");
        
        DynamicBean record = dutyService.getDutyRecordByUser(userId, seq);
        int result = 1;
        
        if (null == record)
        {
            result = 1;// 未进行上班登记；
        }
        else
        {
            if (null != record.getValue("up") && null != record.getValue("down"))
            {
                result = 2;// 已进行下班登记
            }
            else if (null != record.getValue("up") && StringUtils.isBlank(record.getValue("down")))
            {
                result = 3;// 值班中
            }
        }
        
        return result;
    }
    
    /**
     * 根据用户id查询当天值班记录
     * 
     * @param userId
     * @return
     */
    public PageList<DynamicBean> queryLogByUser()
    {
        UserInfoBean user = (UserInfoBean) getSessionAttr("user");
        String userId = "";
        
        if (null == user)
        {
            userId = "1";
        }
        else
        {
            userId = user.getId();
        }
        
        return dutyService.queryLogByUser(userId);
    }
    
    /**
     * 根据用户id查询当天上报故障记录
     * 
     * @param userId
     * @return
     */
    public PageList<DynamicBean> queryRecordByUser()
    {
        UserInfoBean user = (UserInfoBean) getSessionAttr("user");
        String userId = "";
        
        if (null == user)
        {
            userId = "1";
        }
        else
        {
            userId = user.getId();
        }
        
        return dutyService.queryRecordByUser(userId);
    }
    
    /**
     * 上报故障
     * 
     * @return
     */
    public int addTrouble()
    {
        String id = getValue("id");
        int result = 0;
        
        if (StringUtils.isNotBlank(id))
        {
            result = dutyService.addTrouble(id);
        }
        
        return result;
    }
    
    /**
     * 添加值班日志
     * 
     * @return
     */
    public int addLog()
    {
        String type = getValue("type");
        String content = getValue("content");
        
        int result = 0;
        
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(content))
        {
            UserInfoBean user = (UserInfoBean) getSessionAttr("user");
            String userId = "";
            
            if (null == user)
            {
                userId = "1";
            }
            else
            {
                userId = user.getId();
            }
            
            result = dutyService.addLog(type, content, userId);
        }
        
        return result;
    }
    
    /**
     * 查询故障记录
     * 
     * @return
     */
    public PageList<DynamicBean> getDutyErrorByType()
    {
        String type = getString("type");
        String beginDate = getString("beginDate");
        String endDate = getString("endDate");
        int start = getInt("page");
        int pageSize = getInt("rows");
        return dutyService.getDutyErrorByType(type, beginDate, endDate, start, pageSize);
    }
    
    /**
     * 查询故障记录明细
     * 
     * @return
     */
    public DynamicBean getDutyErrorDetail()
    {
        String id = getString("id");
        return dutyService.getDutyErrorById(id);
    }
    
    /**
     * 处理日常故障
     * 
     * @return
     */
    public int updateTrouble()
    {
        int result = 0;
        Map<String, Object> map = getParamMap();
        
        if (null != map.get("ID") && StringUtils.isNotBlank(map.get("ID").toString()))
        {
            map.put("OPERTION_TIME", new Date());
            UserInfoBean user = (UserInfoBean) getSessionAttr("user");
            String userId = "";
            
            if (null == user)
            {
                userId = "1";
            }
            else
            {
                userId = user.getId();
            }
            map.put("OPERTION_USERID", userId);
            result = dutyService.updateTrouble(map);
        }
        
        return result;
        
    }
    
    /**
     * @Title: importDutyInfo
     * @Description:导入值班信息
     * @return String
     * @author lqy
     * @throws
     */
    public String importDutyInfo()
    {
        InputStream inStream = null;
        try
        {
            Date date = new Date();
            HttpServletRequest request = getRequest();
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile dutyFile = multipartRequest.getFile("dutyFile");
            byte[] dutys = dutyFile.getBytes();
            
            if (dutys == null)
            {
                return "{success:false}";
            }
            inStream = new ByteArrayInputStream(dutys);
            List<Map<String, Object>> infos = DutyExcelUtil.presExcel(inStream);
            
            for (Map<String, Object> infoMap : infos)
            {
                String infoId = UUID.randomUUID().toString();
                String dutyName = infoMap.get("DUTY_NAME") + "";
                String dutyDate = infoMap.get("DUTY_DATE") + "";
                String dutyPerson = infoMap.get("DUTY_PERSON") + "";
                String dutyRemark = infoMap.get("DUTY_REMARK") + "";
                String dutyFaultReason = infoMap.get("DUTY_FAULT_REASON") + "";
                Object itemsObj = infoMap.get("infos");
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("ID", infoId);
                params.put("DUTY_NAME", dutyName);
                params.put("DUTY_DATE", new SimpleDateFormat("yyyy年MM月dd").parse(dutyDate));
                params.put("DUTY_PERSON",
                        com.txy.common.util.StringUtils.dutyPersonPres(dutyPerson));
                params.put("DUTY_REMARK", dutyRemark);
                params.put("DUTY_FAULT_REASON", dutyFaultReason);
                params.put("CREATE_TIME", date);
                
                if (itemsObj != null && itemsObj instanceof List)
                {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> items = (List<Map<String, Object>>) itemsObj;
                    for (Map<String, Object> itemMap : items)
                    {
                        String itemId = UUID.randomUUID().toString();
                        String itemExplain = itemMap.get("ITEM_EXPLAIN") + "";
                        String itemName = itemMap.get("ITEM_NAME") + "";
                        Object detailsObj = itemMap.get("items");
                        
                        Map<String, Object> paramItem = new HashMap<String, Object>();
                        paramItem.put("ID", itemId);
                        paramItem.put("ITEM_NAME", itemName);
                        paramItem.put("ITEM_EXPLAIN", itemExplain);
                        paramItem.put("CREATE_TIME", date);
                        paramItem.put("DUTY_ID", infoId);
                        
                        if (detailsObj != null && detailsObj instanceof List)
                        {
                            @SuppressWarnings("unchecked")
                            List<Map<String, Object>> detailList = (List<Map<String, Object>>) detailsObj;
                            for (Map<String, Object> detailMap : detailList)
                            {
                                String detailId = UUID.randomUUID().toString();
                                detailMap.put("ID", detailId);
                                detailMap.put("ITEM_ID", itemId);
                                detailMap.put("CREATE_TIME", date);
                                dutyService.addDutyDetail(detailMap);
                            }
                        }
                        dutyService.addDutyItem(paramItem);
                    }
                }
                dutyService.addDutyInfo(params);
            }
        }
        catch (Exception e)
        {
            return "{success:false}";
        }
        return "{success:true}";
    }
    
    /**
     * @Title: getDutyInfos
     * @Description: 每天值班信息
     * @return PageList<DynamicBean>
     * @author lqy
     * @throws
     */
    public PageList<DynamicBean> getDutyInfos()
    {
        int start = getInt("page");
        int pageSize = getInt("rows");
        String searchKey = getString("searchKey");
        String dutyDate = getString("dutyDate");
        return dutyService.getDutyInfos(start, pageSize, searchKey, dutyDate);
    }
    //值班列表
    public PageList<DynamicBean> getDutyList()
    {
        int start = getInt("page");
        int pageSize = getInt("rows");
        String dutyPerson = getString("searchKey");
        return dutyService.getDutyList(start, pageSize, dutyPerson);
    }
    
    /**
     * @Title: getDutyDetails
     * @Description:详细信息
     * @return PageList<DynamicBean>
     * @author lqy
     * @throws
     */
    public PageList<DynamicBean> getDutyDetails()
    {
        int start = getInt("page");
        int pageSize = getInt("rows");
        String detailName = getString("detailName");
        String id = getString("id");
        return dutyService.getDutyDetails(start, pageSize, detailName,id);
    }
    
    /**
     * @return
     */
    public String clearDuty()
    {
        try
        {
            dutyService.clearDuty();
        }
        catch (Exception e)
        {
            return "{success:false}";
        }
        return "{success:true}";
    }
    
    public Map<String, String> getDutyReportData()
    {
        String dutyDate = getString("dutyDate");
        //String endDate = getString("endDate");
        List<DynamicBean> list = dutyService.queryDutyPersons(dutyDate);
        StringBuffer sb1 = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        sb1.append("[");
        sb2.append("[");
        int count = 0;
        for (DynamicBean dynamicBean : list)
        {
            count++;
            String dutyPerson = dynamicBean.getValue("duty_person");
            String userId = dynamicBean.getValue("user_id");
            if (dutyPerson != null)
            {
                sb1.append("'" + dutyPerson + "'");
                int dutyCount = dutyService.queryCountByUserId(userId);
                sb2.append(dutyCount);
                if (list.size() != count)
                {
                    sb1.append(",");
                    sb2.append(",");
                }
            }
            
        }
        sb1.append("]");
        sb2.append("]");
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("result", sb2.toString());
        map.put("title", sb1.toString());
        return map;
    }
    
    public Map<String, Object> getDutyLogDetails()
    {
        String id = getString("id");
        Map<String, Object> map = dutyService.queryDutyInfoById(id);
        List<Map<String, Object>> items = dutyService.queryDutyItemByInfoId(id);
        for (Map<String, Object> item : items)
        {
            List<Map<String, Object>> details = dutyService.queryDutyDetailsByItemId(item.get("id"));
            item.put("details", details);
        }
        if (map.get("DUTY_DATE") != null)
        {
            SimpleDateFormat fmt = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
            map.put("DUTY_DATE",fmt.format((Date)map.get("DUTY_DATE")));
        }
        if (map.get("DUTY_END_DATE") != null)
        {
            SimpleDateFormat fmt = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
            map.put("DUTY_END_DATE",fmt.format((Date)map.get("DUTY_END_DATE")));
        }
        map.put("items", items);
        return map;
    }
    
    public Map<String, Object> getDutyWeDetails()
    {
        String id = getString("id");
        Map<String, Object> map = dutyService.queryDutyInfoById(id);
        List<Map<String, Object>> items = dutyService.queryDutyWeItemByInfoId(id);
       
        if (map.get("DUTY_DATE") != null)
        {
            SimpleDateFormat fmt = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
            map.put("DUTY_DATE",fmt.format((Date)map.get("DUTY_DATE")));
        }
        if (map.get("DUTY_END_DATE") != null)
        {
            SimpleDateFormat fmt = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
            map.put("DUTY_END_DATE",fmt.format((Date)map.get("DUTY_END_DATE")));
        }
        map.put("items", items);
        return map;
    }
    
    public String updateNetDutyLog()
    {
        try
        {
            String id = getString("id");
            dutyService.deleteDutyAll(id);
            return  saveNetDutyLog();
        }
        catch (Exception e)
        {
            return "{success:false}";
        }
    }
    public String updateWehther()
    {
        try
        {
            String id = getString("id");
            dutyService.deleteWetherDutyAll(id);
            return saveWetherNetDutyLog();
        }
        catch (Exception e)
        {
            return "{success:false}";
        }
    }
    
    public String saveNetDutyLog()
    {
        
        Date date = new Date();
        //String name = getString("name");
        String time = getString("time");
        String reason = getString("reason");
        String infoId = UUID.randomUUID().toString();
        try
        {
            
            String userId = ((UserInfoBean) getSessionAttr("user")).getId();
            SimpleDateFormat fmt = new SimpleDateFormat("yyy-MM-dd");
            Map<String, Object> map =  dutyService.queryNetInfoByUserId(userId,fmt.format(new Date()));
            if (map != null && map.size() > 0)
            {
                return "{success:false}";
            }
            
            //String data = getString("itemData");
            String data = new String(getString("itemData").getBytes("ISO-8859-1"),"utf-8");
            
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("ID", infoId);
            params.put("DUTY_NAME", "信息网络中心网络维护值班日志");
            params.put("DUTY_DATE", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time));
            params.put("DUTY_PERSON",((UserInfoBean) getSessionAttr("user")).getName());
            params.put("DUTY_REMARK", "");
            params.put("DUTY_FAULT_REASON", reason);
            params.put("CREATE_TIME", date);
            params.put("USER_ID", userId);
            params.put("TYPE", 0);//0 网络管理  1气象通讯传输
            params.put("DUTY_STATUS", 0);//0值班中 1结束值班
            
            JSONArray jsons = JSONArray.fromObject(data);
            @SuppressWarnings("unchecked")
            Iterator<JSONObject> itors = jsons.iterator();
            int sortNum = 0;
            while(itors.hasNext())
            {
                JSONObject items =  (JSONObject)itors.next();
                String itemName = items.getString("itemName");
                String itemExplain = items.getString("itemExplain");
                String itemId = UUID.randomUUID().toString();
                Map<String, Object> paramItem = new HashMap<String, Object>();
                paramItem.put("ID", itemId);
                paramItem.put("ITEM_NAME", itemName);
                paramItem.put("ITEM_EXPLAIN", itemExplain);
                paramItem.put("CREATE_TIME", date);
                paramItem.put("DUTY_ID", infoId);
                paramItem.put("SORT_NUM", sortNum);
                sortNum++;
                
                JSONArray details =  items.getJSONArray("details");
                for (int i = 0; i < details.size(); i++)
                {
                    JSONObject  detailObj =  (JSONObject)details.get(i);
                    String detail = detailObj.getString("detail");
                    String isnomal =  detailObj.getString("isnomal");
                    Map<String, Object> detailMap = new HashMap<String, Object>();
                    String detailId = UUID.randomUUID().toString();
                    detailMap.put("ID", detailId);
                    detailMap.put("ITEM_ID", itemId);
                    detailMap.put("CREATE_TIME", date);
                    detailMap.put("DETAIL_NAME", detail);
                    detailMap.put("ISNOMAL", Integer.parseInt(isnomal));
                    dutyService.addDutyDetail(detailMap);
                }
                
                dutyService.addDutyItem(paramItem);
            }
            dutyService.addDutyInfo(params);
         
        }
        catch (Exception e)
        {
            return "{success:false}";
        }
        return "{\"success\":true,\"id\":\""+infoId+"\"}";
    }
    
    public String saveWetherNetDutyLog()
    {
        Date date = new Date();
        //String name = getString("name");
        String time = getString("time");
        String infoId = UUID.randomUUID().toString();
        try
        {
            
            String userId = ((UserInfoBean) getSessionAttr("user")).getId();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Map<String, Object> map =  dutyService.queryWetInfoByUserId(userId,fmt.format(new Date()));
            if (map != null && map.size() > 0)
            {
                return "{success:false}";
            }
            
            //String data = getString("itemData");
            String data = new String(getString("itemData").getBytes("ISO-8859-1"),"utf-8");
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("ID", infoId);
            params.put("DUTY_NAME", "信息网络中心网络维护值班日志");
            params.put("DUTY_DATE", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time));
            params.put("DUTY_PERSON",((UserInfoBean) getSessionAttr("user")).getName());
            params.put("DUTY_REMARK", "");
            params.put("CREATE_TIME", date);
            params.put("USER_ID", userId);
            params.put("TYPE", 1);//0 网络管理  1气象通讯传输
            params.put("DUTY_STATUS", 0);//0值班中 1结束值班
            JSONArray jsons = JSONArray.fromObject(data);
            @SuppressWarnings("unchecked")
            Iterator<JSONObject> itors = jsons.iterator();
            int sortNum = 0;
            while(itors.hasNext())
            {
                JSONObject items =  (JSONObject)itors.next();
                String isall = items.getString("isall");
                String itemName = items.getString("itemName");
                String loseStation = items.getString("loseStation");
                String troubleItem = items.getString("troubleItem");
                String solve = items.getString("solve");
                String itemId = UUID.randomUUID().toString();
                Map<String, Object> paramItem = new HashMap<String, Object>();
                paramItem.put("ID", itemId);
                paramItem.put("ITEM_NAME", itemName);
                paramItem.put("ISALL", Integer.parseInt(isall));
                paramItem.put("CREATE_TIME", date);
                paramItem.put("DUTY_ID", infoId);
                paramItem.put("LOSE_STATION", loseStation);
                paramItem.put("TROUBLE_ITEM", troubleItem);
                paramItem.put("SOLVE", solve);
                paramItem.put("SORT_NUM", sortNum);
                sortNum++;
                
                dutyService.addWetherDutyItem(paramItem);
            }
            dutyService.addDutyInfo(params);
         
        }
        catch (Exception e)
        {
            return "{success:false}";
        }
        return "{\"success\":true,\"id\":\""+infoId+"\"}";
        
    }
    
    public Map<String, Object> queryNetInfo()
    {
        String userId = ((UserInfoBean) getSessionAttr("user")).getId();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> map =  dutyService.queryNetInfoByUserId(userId,fmt.format(new Date()));
        
        String id = (String) map.get("ID");

        List<Map<String, Object>> items = dutyService.queryDutyItemByInfoId(id);
        for (Map<String, Object> item : items)
        {
            List<Map<String, Object>> details = dutyService.queryDutyDetailsByItemId(item.get("id"));
            item.put("details", details);
        }
        if (map.get("DUTY_DATE") != null)
        {
            map.put("DUTY_DATE",fmt1.format((Date)map.get("DUTY_DATE")));
        }
        if (map.get("DUTY_END_DATE") != null)
        {
            map.put("DUTY_END_DATE",fmt1.format((Date)map.get("DUTY_END_DATE")));
        }
        map.put("items", items);
        map.put("username", ((UserInfoBean) getSessionAttr("user")).getName());
        return map;
    }
    
    public Map<String, Object> getDutyWeInfo()
    {
        String userId = ((UserInfoBean) getSessionAttr("user")).getId();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> map =  dutyService.queryWetInfoByUserId(userId,fmt.format(new Date()));
        
        String id = (String) map.get("ID");
        List<Map<String, Object>> items = dutyService.queryDutyWeItemByInfoId(id);
       
        if (map.get("DUTY_DATE") != null)
        {
            map.put("DUTY_DATE",fmt1.format((Date)map.get("DUTY_DATE")));
        }
        if (map.get("DUTY_END_DATE") != null)
        {
            map.put("DUTY_END_DATE",fmt1.format((Date)map.get("DUTY_END_DATE")));
        }
        map.put("items", items);
        map.put("username", ((UserInfoBean) getSessionAttr("user")).getName());
        return map;
    }
    
    public String updateDutyStatus()
    {
        try
        {
            String id = getString("id");
            dutyService.updateDutyStatus(id);
        }
        catch (Exception e)
        {
            return "{success:false}";
        }
        return "{success:true}";
    }
    
    public String  queryUsers()
    {
        
        return ((UserInfoBean) getSessionAttr("user")).getName();
        //return dutyService.queryUsers();
    }
    
    public PageList<DynamicBean> queryDutyCount()
    {
        int start = getInt("page");
        int pageSize = getInt("rows");
        String dutyDate = getString("dutyDate");
        PageList<DynamicBean> list =  dutyService.queryDutyCount(start,pageSize,dutyDate);
        return list;
    }
    
    public PageList<DynamicBean> queryDutyRecord()
    {
        int start = getInt("page");
        int pageSize = getInt("rows");
        int userId = getInt("userId");
        int type = getInt("type");
        String dutyDate = getString("dutyDate");
        PageList<DynamicBean> list =  dutyService.queryDutyRecord(start,pageSize,userId,type,dutyDate);
        return list;
    }
}
