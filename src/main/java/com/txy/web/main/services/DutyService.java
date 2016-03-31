package com.txy.web.main.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.txy.common.bean.PageList;
import com.txy.common.constant.Constant;
import com.txy.common.orm.SqlBeanClient;
import com.txy.common.orm.query.impl.DynamicBean;
import com.txy.common.util.DateFormatUtil;

/** 
* @ClassName: DutyService 
* @Description: 值班处理
* @author lqy 
* @date 2015年11月12日 下午3:55:06 
*  
*/
@Service
public class DutyService {

	@Autowired
	private SqlBeanClient queryClient;

	public PageList<DynamicBean> queryDuty(int start, int pageSize) {
	    String sql = "SELECT t.name AS name,t.id as id,t.age as age FROM T_TEST t";
	    return queryClient.createSqlQuery(sql).page(start, pageSize);
	}
	
	public List<DynamicBean> getDutyUser(){
		String sql = "SELECT t.userid,t.username FROM T_SYSTEM_USER t where t.isduty = 1 order by addtime";
	    return queryClient.createSqlQuery(sql).list();
	}
	
	public List<Map<String, Object>> getDutyUserMap() {
		String sql = "SELECT t.userid,t.username FROM T_SYSTEM_USER t where t.isduty = 1 order by addtime";
		List<Map<String, Object>> list = queryClient.createJdbcTemplate().queryForList(sql);
		return list;
	}
	
	public List<DynamicBean> getUpDutyUser(){
		String dates = DateFormatUtil.formatDate(new Date());
		String sql = "select tt.userId from (SELECT * FROM wmp_duty_user t where t.duty_date < to_date(?,'yyyy-mm-dd')"
				+ "  order by t.duty_date desc) tt where rownum <=1";
	    return queryClient.createSqlQuery(sql,dates).list();
	}
	
	public void addDuty(String selectDates) {
		
		
		if(StringUtils.isNotEmpty(selectDates)){
			Date nowDate = new Date();
			String[] dates = selectDates.split(",");
			for (String string : dates) {
				Date date = DateFormatUtil.parseDate(string);
				if(date.before(nowDate)){
					continue;
				}
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("duty_date", date);
				queryClient.createExecuteQuery().delete("wmp_duty_user", params);
				params.put("id", Constant.COMMON_SEQ);
				queryClient.createExecuteQuery().insert("wmp_duty_user", params);
			}
		}
		
		List<DynamicBean> allDutyUser = getDutyUser();
		
		List<DynamicBean> upDutyUser = getUpDutyUser();
		
		
		String userId = "";
		int userIndex  = 0;
		int userSize = allDutyUser.size();
		
		if(!upDutyUser.isEmpty()){
			userId = upDutyUser.get(0).getValue("userid");
		}
		if(StringUtils.isNotEmpty(userId)){
			for (int i = 0; i < allDutyUser.size(); i++) {
				if(userId.equals(allDutyUser.get(i).getValue("userid"))){
					userIndex = i;
					break;
				}
			}
		}
		List<DynamicBean> notDutyDate = queryClient.createSqlQuery("select * from wmp_duty_user t where t.duty_date >= to_date(?,'yyyy-mm-dd')"
				+ " order by t.duty_date", DateFormatUtil.formatDate(new Date())).list();
		
		for (int i = 0; i < notDutyDate.size(); i++) {
			Map<String, Object> data = notDutyDate.get(i).getMap();
			if(userIndex <= userSize -1){
				data.put("userid", allDutyUser.get(userIndex).getValue("userid"));
				userIndex ++;
			}else{
				userIndex = 0;
				data.put("userid", allDutyUser.get(userIndex).getValue("userid"));
				userIndex++;
			}
			queryClient.createExecuteQuery().update("wmp_duty_user", data, "id", data.get("id"));
		}
		
	}

	public List<DynamicBean> getDutyInfoByMonth(String startDate, String endDate) {
	    String sql = "select a.duty_date,b.username from wmp_duty_user a left join t_system_user b on a.userId = b.userId where"
	    		+ " a.duty_date >= to_date(?,'yyyy-mm-dd') and a.duty_date <= to_date(?,'yyyy-mm-dd') order by a.duty_date";
	    return queryClient.createSqlQuery(sql,startDate,endDate).list();
	}

	public PageList<DynamicBean> queryDutyStatistics(Date startDate, Date endDate, int start, int pageSize) {
		String sql = "select c.name, b.username, count(1) as nums from  wmp_duty_user a left join t_system_user b on a.userid = b.userid left join wmp_area c on b.orgid = c.areaid where a.duty_date >= to_date(?,'yyyy-mm-dd') and  a.duty_date <= to_date(?,'yyyy-mm-dd')  and a.userid is not null GROUP BY a.userid,c.name,b.username";
	    return queryClient.createSqlQuery(sql,DateFormatUtil.formatDate(startDate),DateFormatUtil.formatDate(endDate)).page(start, pageSize);
	}

	public PageList<DynamicBean> getUserDutyRecord(int start, int pageSize, String userId, String startDate, String endDate) {
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id,c.name,b.username,a.start_date,a.end_date,a.duty_date from wmp_duty_user a left join t_system_user b on a.userid = b.userid left join wmp_area c on b.orgid = c.areaid where a.duty_date >= to_date(?,'yyyy-mm-dd') and  a.duty_date <= to_date(?,'yyyy-mm-dd')");
	   if(StringUtils.isNotBlank(userId)){
		   sql.append(" and a.userid ='"+userId+"'");
	   }
		return queryClient.createSqlQuery(sql.toString(),startDate,endDate).page(start, pageSize);
	}

	public void workDuty(String dutyType, String userId, String nowDate) {
		DynamicBean bean = queryClient.createSqlQuery("select * from wmp_duty_user t where t.duty_date = to_date(?,'yyyy-mm-dd')"
				+ " and userid = ?", nowDate,userId).uniqueResult();
		if(null != bean){
			Map<String, Object> params = bean.getMap();
			if("start".equals(dutyType)){
				params.put("start_date", DateFormatUtil.formateDatetime(new Date()));
			}else{
				params.put("end_date", DateFormatUtil.formateDatetime(new Date()));
			}
			queryClient.createExecuteQuery().update("wmp_duty_user", bean.getMap(), "id", bean.getMap().get("id"));
		}
	}

	public PageList<DynamicBean> getDutyInfoByUser(String userId, String queryDate, int start, int pageSize) {
		String sql = "select b.record_time,c.username,b.log_type,b.log_content from WMP_DUTY_USER a left "
				+ "join wmp_duty_log b on a.id = b.dutyid left join T_SYSTEM_USER c on a.userid = c.userid where"
				+ " a.duty_date = to_date(?,'yyyy-mm-dd') and a.userid = ?";
	    return queryClient.createSqlQuery(sql,queryDate,userId).page(start, pageSize);
	}
	
	public PageList<DynamicBean> getDutyErrorByUser(String userId, String queryDate, int start, int pageSize) {
		String sql = "select b.record_time,b.trouble_type,b.opertion_time,b.opertion_status,c.username as record_name,d.username as"
				+ " opertion_name from wmp_duty_trouble b left join T_SYSTEM_USER c on b.record_userid = c.userid left join T_SYSTEM_USER "
				+ "d on b.opertion_userid = c.userid where to_date(b.record_time,'yyyy-mm-dd') = to_date(?,'yyyy-mm-dd') and b.record_userid = ?";
	    return queryClient.createSqlQuery(sql,queryDate,userId).page(start, pageSize);
	}
	
	/**
	 * 查询当前选中人值班状态
	 * @param userId 用户id
	 * @param seq 值班班次
	 * @return 上下班时间
	 */
	public DynamicBean getDutyRecordByUser(String userId,String seq){
		
		String sql="select DOWN_TIME as down ,UP_TIME as up from WMP_DUTY_RECORD where DUTY_SEQ=? AND USER_ID=?";
		
		return queryClient.createSqlQuery(sql, seq,userId).uniqueResult();
	}
	
	/**
	 * 根据用户id查询当天值班记录
	 * @param userId
	 * @return
	 */
	public PageList<DynamicBean> queryLogByUser(String userId){
		
		String sql="SELECT ID, RECORD_TIME AS time,LOG_TYPE AS type,LOG_CONTENT AS content"
				+ " FROM WMP_DUTY_LOG WHERE TO_CHAR(RECORD_TIME,'YYYY-MM-DD')=? AND USERID=? ";
		
		String time=com.txy.common.util.StringUtils.formartDate(new Date(),"yyyy-MM-dd");
		return queryClient.createSqlQuery(sql, time,userId).page(1, 100);
		
	}
	
	/**
	 * 根据用户id查询当天上报故障记录
	 * @param userId
	 * @return
	 */
	public PageList<DynamicBean> queryRecordByUser(String userId){
		
		StringBuffer sql=new StringBuffer("SELECT ID, RECORD_TIME,TROUBLE_TYPE,(SELECT USERNAME FROM T_SYSTEM_USER WHERE ");
		sql.append("USERID=RECORD_USERID) AS recordName,OPERTION_TIME,OPERTION_STATUS,");
		sql.append("(SELECT USERNAME FROM T_SYSTEM_USER WHERE USERID=REPORTED_USERID) AS reportedName");
		sql.append(" FROM WMP_DUTY_TROUBLE WHERE TO_CHAR(RECORD_TIME,'YYYY-MM-DD')=? AND RECORD_USERID=? ");
		
//		String sql_count="SELECT COUNT(0) FROM WMP_DUTY_TROUBLE WHERE TO_CHAR(RECORD_TIME,'YYYY-MM-DD')=? AND RECORD_USERID=? ";
		
		String time=com.txy.common.util.StringUtils.formartDate(new Date(),"yyyy-MM-dd");
		
		return queryClient.createSqlQuery(sql.toString(), time,userId).page(1, 100);
		
	}
	
	/**
	 * 上报
	 * @param id
	 * @return
	 */
	public int addTrouble(String id){
		String sql="SELECT RECORD_TIME,LOG_TYPE,LOG_CONTENT,USERID,DUTYID FROM WMP_DUTY_LOG WHERE ID=?";
		
		DynamicBean dybean=queryClient.createSqlQuery(sql,id).uniqueResult();
		
		Map<String, Object> map=new HashMap<String, Object>();
		
		map.put("ID", Constant.COMMON_SEQ);
		map.put("RECORD_TIME", DateFormatUtil.parseDate(dybean.getValue("record_time"),"yyyy-MM-dd HH:mm:ss"));
		map.put("TROUBLE_TYPE", Integer.parseInt(dybean.getValue("log_type")));
		map.put("CONTENT", dybean.getValue("log_content"));
		map.put("RECORD_USERID", dybean.getValue("userid"));
		map.put("REPORTED_USERID", dybean.getValue("userid"));
		map.put("DUTY_ID", Integer.parseInt(dybean.getValue("dutyid")));
		map.put("OPERTION_STATUS", 0);
		map.put("REPORTED_TIME", new Date());
		return queryClient.createExecuteQuery().insert("WMP_DUTY_TROUBLE", map);
	}
	
	/**
	 * 新增值班日志
	 * @param type
	 * @param content
	 * @param userId
	 * @return
	 */
	public int addLog(String type,String content,String userId){
		
		Map<String, Object> map=new HashMap<String, Object>();
		
		map.put("ID", Constant.COMMON_SEQ);
		
		map.put("RECORD_TIME", new Date());
		map.put("LOG_TYPE", type);
		map.put("LOG_CONTENT", content);
		map.put("USERID", userId);
		map.put("DUTYID", com.txy.common.util.StringUtils.formartDate(new Date(), "YYYYMMdd"));
		
		return queryClient.createExecuteQuery().insert("WMP_DUTY_LOG", map);
	}
	
	public PageList<DynamicBean> getDutyErrorByType(String type, String beginDate, String endDate, int start, int pageSize) {
		String sql = "select a.id,d.name,c.username,a.trouble_type,a.duty_id from wmp_duty_trouble a left join wmp_duty_record b on"
				+ " a.duty_id=b.duty_seq left join t_system_user c  on b.user_id = c.userid left join wmp_area d on c.orgid = d.areaid where  1=1 ";
		if(StringUtils.isNotBlank(type)) {
			//sql = "select d.name,c.username,a.trouble_type,a.duty_id from wmp_duty_trouble a left join wmp_duty_record b on a.duty_id=b.duty_seq left join t_system_user c  on b.user_id = c.userid left join wmp_area d on c.orgid = d.areaid where a.trouble_type=" + type + " and to_date(a.record_time,'yyyy-mm-dd') >=  to_date(?,'yyyy-mm-dd') and to_date(a.record_time,'yyyy-mm-dd') <=  to_date(?,'yyyy-mm-dd')";
			sql = sql + " and a.trouble_type=" + type;
		}
		if(StringUtils.isNotBlank(beginDate)) {
			sql = sql + " and to_date(to_char(a.record_time,'yyyy-mm-dd'),'yyyy-mm-dd') >=  to_date('" + beginDate + "','yyyy-mm-dd') ";
		}
		if(StringUtils.isNotBlank(endDate)) {
			sql = sql + " and to_date(to_char(a.record_time,'yyyy-mm-dd'),'yyyy-mm-dd') <=  to_date('" + endDate + "','yyyy-mm-dd') ";
		}
	    
		return queryClient.createSqlQuery(sql).page(start, pageSize);
	}
	
	public DynamicBean getDutyErrorById(String id) {
		String sql = "select a.content, a.opertion_status, a.record_time, a.opertion_time,a.opertion_userid,a.record_userid,a.reported_time,"
				+ "a.reported_userid,a.trouble_type from wmp_duty_trouble a where a.id=?";
		DynamicBean bean = queryClient.createSqlQuery(sql,id).uniqueResult();
		return bean;
	}
	/**
	 * 运维值班记录检索
	 * @param start
	 * @param pageSize
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public PageList<DynamicBean> getUserDutyRecordretrieval(int start, int pageSize, String userId, String startDate,String endDate) {
	

		String sql ="SELECT WMP_DUTY_RECORD.ID,NAME,USERNAME,DUTY_SEQ,UP_TIME,DOWN_TIME FROM WMP_DUTY_RECORD LEFT JOIN T_SYSTEM_USER ON WMP_DUTY_RECORD.USER_ID = T_SYSTEM_USER.USERID LEFT JOIN WMP_AREA ON T_SYSTEM_USER.ORGID = WMP_AREA.AREAID"
				+ " WHERE to_char(UP_TIME,'yyyy-mm-dd') >= ? AND to_char(UP_TIME, 'yyyy-mm-dd') <= ?";
	
		if(StringUtils.isNotBlank(userId)){
		
			sql = sql+" and WMP_DUTY_RECORD.USER_ID ='"+userId+"'";
		}
		return queryClient.createSqlQuery(sql,startDate,endDate).page(start, pageSize);
	}
	
	
	public PageList<DynamicBean> getUserDutyRecordretrievalId(String id){
		String sql="select duty_seq,user_id from wmp_duty_record where id=?";
		
		DynamicBean record=queryClient.createSqlQuery(sql, id).uniqueResult();
		
		String sql_trouble="select content from wmp_duty_trouble where reported_userid=?";
		
		return  queryClient.createSqlQuery(sql_trouble, record.getValue("user_id")).page(1,100);

	}
	
	
	/**
	 * 处理日常故障
	 * @param map
	 * @return
	 */
	public int updateTrouble(Map<String, Object> map){
		
		return queryClient.createExecuteQuery().update("WMP_DUTY_TROUBLE", map, "ID",map.get("ID"));
	}

	public void addDutyDetail(Map<String, Object> detailMap) {
	    queryClient.createExecuteQuery().insert("wmp_duty_detail", detailMap);
	}
	public void addDutyInfo(Map<String, Object> infoMap) {
	    queryClient.createExecuteQuery().insert("wmp_duty_info", infoMap);
	}
	public void addDutyItem(Map<String, Object> itemMap) {
	    queryClient.createExecuteQuery().insert("wmp_duty_item", itemMap);
	}

	public PageList<DynamicBean> getDutyInfos(int start, int pageSize,String searchKey, String dutyDate) {
		String sql = "select * from (select a.*,i.item_name,i.id as item_id,d.detail_name,d.id as did from wmp_duty_detail d left join wmp_duty_item i on d.ITEM_ID = i.id left join  wmp_duty_info a on a.id = i.duty_id   union all select a.*,w.item_name,w.id as item_id, '' detail_name,'' did  from wmp_wether_detail w left join  wmp_duty_info a on a.id = w.duty_id  ) a where 1=1 ";
		if (searchKey != null && !"".equals(searchKey))
		{
		    sql+="  and a.item_name  like '%"+searchKey+"%' or a.detail_name like '%"+searchKey+"%'";
		}
		 if(StringUtils.isNotBlank(dutyDate)) {
			sql = sql + " and to_date(to_char(a.duty_date,'yyyy/mm/dd'),'yyyy-mm-dd') =  to_date('" + dutyDate + "','yyyy-mm-dd') ";
		}
		sql = sql + " order by a.duty_date desc ";
		return queryClient.createSqlQuery(sql).page(start, pageSize);
	}

	public PageList<DynamicBean> getDutyDetails(int start, int pageSize,String detailName,String id) {
	    String sql = "select i.ITEM_NAME,i.ITEM_EXPLAIN,d.DETAIL_NAME,d.AM_TIME,d.PM_TIME,d.NE_TIME from "
	            + " wmp_duty_detail d left join wmp_duty_item i  on i.id=d.item_id where 1 = 1 ";
	    if (detailName != null && !"".equals(detailName))
	    {
		    sql+="  and d.DETAIL_NAME like '"+detailName+"'";
	    }
	    if (id != null && !"".equals(id))
	    {
	        sql+="  and  i.duty_id = '"+id+"'";
	    }
	    
	    return queryClient.createSqlQuery(sql).page(start, pageSize);
	}

	public void clearDuty() {
		String sql1 = "delete from wmp_duty_info";
		String sql2 = "delete from wmp_duty_item";
		String sql3 = "delete from  wmp_duty_detail";
		queryClient.createJdbcTemplate().execute(sql1);
		queryClient.createJdbcTemplate().execute(sql2);
		queryClient.createJdbcTemplate().execute(sql3);
	}
	
	
	public List<DynamicBean> queryDutyPersons(String dutyDate) {
	    String sql = "select a.duty_person,a.user_id from wmp_duty_info a where 1 =1 ";
	    if(StringUtils.isNotBlank(dutyDate)) {
	    	sql = sql + " and to_date(to_char(a.duty_date,'yyyy/mm'),'yyyy-mm') =  to_date('" + dutyDate + "','yyyy-mm') ";
	    }
	    /*if(StringUtils.isNotBlank(endDate)) {
	    	sql = sql + " and to_date(to_char(a.duty_date,'yyyy/mm/dd'),'yyyy-mm-dd') <=  to_date('" + endDate + "','yyyy-mm-dd') ";
	    }*/
	    sql+="  group by a.user_id,a.duty_person";
	    return queryClient.createSqlQuery(sql).list();
	}

	public int queryCountByUserId(String userId) {
		String sql = "select a.duty_person from wmp_duty_info a where a.user_id  = ? ";
		return queryClient.createSqlQuery(sql,userId).count();
	}

    public Map<String, Object> queryDutyInfoById(String id)
    {
        String sql = "SELECT * FROM wmp_duty_info d where d.id = ? ";
        Map<String, Object> map = queryClient.createJdbcTemplate().queryForMap(sql,id);
        return map;
    }

    public List<Map<String, Object>> queryDutyItemByInfoId(String id)
    {
        String sql = "SELECT * FROM wmp_duty_item d where d.DUTY_ID = ? order by d.sort_num asc";
        List<Map<String, Object>> list = queryClient.createJdbcTemplate()
                        .queryForList(sql,id);
        return list;
    }

    public List<Map<String, Object>> queryDutyDetailsByItemId(Object id)
    {
        String sql = "SELECT * FROM wmp_duty_detail d where d.ITEM_ID = ?";
        List<Map<String, Object>> list = queryClient.createJdbcTemplate()
                        .queryForList(sql,id);
        return list;
    }

    public void addWetherDutyItem(Map<String, Object> paramItem)
    {
        queryClient.createExecuteQuery().insert("wmp_wether_detail", paramItem);
    }

    public Map<String, Object> queryNetInfoByUserId(String userId, String date)
    {
        String sql = "SELECT * FROM wmp_duty_info d where d.USER_ID = ? and d.type = 0 and (to_char(d.DUTY_DATE,'yyyy-mm-dd')= '"+date+"' "
                + " or d.duty_status = 0)";
        List<Map<String, Object>> list = queryClient.createJdbcTemplate()
                .queryForList(sql,userId);
        return list.size() > 0 ? list.get(0) : new HashMap<String, Object>();
    }
    public Map<String, Object> queryWetInfoByUserId(String userId, String date)
    {
        String sql = "SELECT * FROM wmp_duty_info d where d.USER_ID = ? and d.type = 1 and (to_char(d.DUTY_DATE,'yyyy-mm-dd')= '"+date+"' "
                + " or  d.duty_status = 0)";
        List<Map<String, Object>> list = queryClient.createJdbcTemplate()
                .queryForList(sql,userId);
        return list.size() > 0 ? list.get(0) : new HashMap<String, Object>();
    }

    public void deleteDutyAll(String id)
    {
        List<Map<String, Object>>  items = queryDutyItemByInfoId(id);
        for (Map<String, Object> map : items)
        {
            queryClient.createExecuteQuery().delete("WMP_DUTY_DETAIL", "ITEM_ID", map.get("ID")+"");
            
        }
        queryClient.createExecuteQuery().delete("WMP_DUTY_ITEM", "DUTY_ID", id);
        queryClient.createExecuteQuery().delete("WMP_DUTY_INFO", "ID", id);
      
    }

    public List<Map<String, Object>> queryDutyWeItemByInfoId(String id)
    {
        String sql = "SELECT * FROM WMP_WETHER_DETAIL d where d.DUTY_ID = ? order by d.sort_num asc";
        List<Map<String, Object>> list = queryClient.createJdbcTemplate()
                        .queryForList(sql,id);
        return list;
    }

    public PageList<DynamicBean> getDutyList(int start, int pageSize, String searchKey)
    {
        String sql = "select * from wmp_duty_info a where 1 = 1 ";
        if (searchKey != null && !"".equals(searchKey))
        {
            sql+="  and a.id in (select duty_id from (select i.duty_id from wmp_duty_item i where i.item_name like '%"+searchKey+
                    "%' union all select i.duty_id from wmp_duty_detail d left join  wmp_duty_item i on i.id = d.item_id where d.detail_name like '%"+searchKey+"%'  union all select w.duty_id from  wmp_wether_detail w where w.item_name  like '%"+searchKey
                    +"%') group by duty_id) or a.duty_person like '"+searchKey+"'";
        }
        sql = sql + " order by a.duty_date desc ";
        return queryClient.createSqlQuery(sql).page(start, pageSize);
    }

    public void deleteWetherDutyAll(String id)
    {
        queryClient.createExecuteQuery().delete("WMP_WETHER_DETAIL", "DUTY_ID", id);
        queryClient.createExecuteQuery().delete("WMP_DUTY_INFO", "ID", id);
    }

    public void updateDutyStatus(String id)
    {
        Map<String, Object> map =  new HashMap<String, Object>();
        map.put("DUTY_STATUS", 1);
        map.put("DUTY_END_DATE", new Date());
        queryClient.createExecuteQuery().update("WMP_DUTY_INFO", map, "ID", id);
    }

    public List<Map<String, Object>> queryUsers()
    {
        String sql = "select USERID,USERNAME from T_SYSTEM_USER";
        return queryClient.createJdbcTemplate()
                .queryForList(sql);
    }

    public PageList<DynamicBean> queryDutyCount(int start, int pageSize,String dutyDate)
    {
        String sql = "SELECT COUNT(TYPE) as count,t.duty_person,t.type,to_char(t.duty_date,'yyyy-mm') as duty_date,user_id from wmp_duty_info t GROUP BY to_char(t.duty_date,'yyyy-mm'),duty_person,TYPE,user_id";
        if(StringUtils.isNotBlank(dutyDate)) {
	    	sql = sql + " having to_char(t.duty_date,'yyyy-mm') =  '" + dutyDate + "'";
	    }
        return queryClient.createSqlQuery(sql).page(start, pageSize);
    }

    public PageList<DynamicBean> queryDutyRecord(int start, int pageSize,int userId,int type,String dutyDate)
    {
        String sql = "SELECT * from wmp_duty_info t where t.user_id = ? and type = ? and to_char(t.duty_date,'yyyy-mm') = ?";
        return queryClient.createSqlQuery(sql,userId,type,dutyDate).page(start, pageSize);
    }
}
