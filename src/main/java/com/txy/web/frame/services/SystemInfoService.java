package com.txy.web.frame.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.txy.common.bean.PageList;
import com.txy.common.orm.BaseService;
import com.txy.common.orm.SqlBeanClient;
import com.txy.common.orm.query.impl.DynamicBean;
import com.txy.web.frame.base.SystemContainer;

@Service
public class SystemInfoService extends BaseService
{

	@Autowired
	private SqlBeanClient sqlBeanClient;
	
	public void saveLog(String handler, String method, String currentUserName, String ip, String param, int result, String paramterMark)
	{
		String operStr = "";
		String operContent = "执行:" + handler + "/" + method;
		if (!StringUtils.isEmpty(method))
		{
			if (method.indexOf("add") != -1 || method.indexOf("save") != -1)
			{
				operStr = "添加";
			}
			if (method.indexOf("update") != -1 || method.indexOf("edit") != -1)
			{
				operStr = "修改";
				operContent += " 参数："+paramterMark;
			}
			if (method.indexOf("delete") != -1 || method.indexOf("remove") != -1)
			{
				operStr = "删除";
			}
			if (method.indexOf("login") != -1)
			{
				operStr = "登录";
			}
			if (method.indexOf("logout") != -1)
			{
				operStr = "退出";
			}
			if (method.indexOf("audit") != -1)
			{
				operStr = "审核";
				operContent += " 参数："+paramterMark;
			}
		}
		if("netmonitorHander".equals(handler)&&"saveNetworkConfig".equals(method)){
			return;
		}
		if (org.apache.commons.lang.StringUtils.isNotEmpty(operStr))
		{
			List<Object> args = new ArrayList<Object>();
			args.add(com.txy.common.util.StringUtils.getPrimarykeyId());
			args.add(operStr);
			args.add(1);
			args.add(new Date());
			if(StringUtils.isEmpty(currentUserName)){
				currentUserName = "admin";
			}
			args.add(currentUserName);
			args.add(operContent);
			args.add(result);
			args.add(param);
			args.add(ip);
			sqlBeanClient.createJdbcTemplate().update("insert into T_SYSTEM_LOG values (?,?,?,?,?,?,?,?,?)",args.toArray());
		}
	}

	/**
	 * 按条件查询日志列表
	 * @param start
	 * @param pageSize
	 * @param logType
	 * @param logStatus
	 * @param oper_user
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public PageList<DynamicBean> queryLogList(int start, int pageSize, String logType, String logStatus, String oper_user, String startTime, String endTime)
	{
		StringBuffer sql = new StringBuffer("select * from t_system_log t where 1 = 1");
		List<String> args = new ArrayList<String>();
		if(StringUtils.isNotEmpty(logType)){
			sql.append(" and t.log_type = ?");
			args.add(logType);
		}
		if(StringUtils.isNotEmpty(logStatus)){
			sql.append(" and t.result = ?");
			args.add(logStatus);
		}
		if(StringUtils.isNotEmpty(oper_user)){
			sql.append(" and t.oper_user = ?");
			args.add(oper_user);
		}
		if(StringUtils.isNotEmpty(startTime)){
			sql.append(" and t.record_time > to_date('"+startTime+"','yyyy-MM-dd')");
		}
		if(StringUtils.isNotEmpty(endTime)){
			sql.append(" and t.record_time < to_date('"+endTime+"','yyyy-MM-dd')");
		}
		sql.append(" order by t.record_time desc");
		return sqlBeanClient.createSqlQuery(sql.toString(), args.toArray()).page(start, pageSize);
	}

	/**
	 * 查询首页面板右上角的未读消息条数
	 * @param userId
	 * @return
	 */
	public int getUserInfoMessageCount(String userId)
	{
		String sql = "SELECT count(*) FROM T_SYSTEM_USER_MESSAGE a WHERE a.touser = ? and a.readflag = 0 ";
		return sqlBeanClient.createJdbcTemplate().queryForObject(sql, Integer.class, userId);
	}

	/**
	 * 查询消息列表
	 * @param status
	 * @param start
	 * @param pageSize
	 * @return
	 */
	public PageList<DynamicBean> getMessageInfoList(String status, int start, int pageSize,String readFlag)
	{
		String userId = "1";
		if(null != SystemContainer.getUserSession()){
			userId = SystemContainer.getUserSession().getId();
		}
		StringBuffer sql = new StringBuffer("select t.*,u.username from T_SYSTEM_USER_MESSAGE t left join t_system_user u on u.userid = t.fromuser where t.touser = ?");
		if(StringUtils.isNotEmpty(readFlag)){
			sql.append(" and t.readflag = 0 ");
		}
		sql.append(" order by t.readflag asc,t.createdate desc");
		return sqlBeanClient.createSqlQuery(sql.toString(), userId).page(start, pageSize);
	}

	/**
	 * 清空日志
	 */
	public void cleanLog() {
		String sql = "truncate table t_system_log";
		sqlBeanClient.createJdbcTemplate().execute(sql);
	}
	

	/**
	 * 查询字典表下拉数据
	 * @param dict_code
	 * @return
	 */
	public List<Map<String, Object>> getDictListVal(String dict_code)
	{
		String sql = "select dict_code as dict_code,dict_value as dict_value,dict_val_name as dict_val_name from t_system_dict t where t.dict_code = ?";
		List<Map<String, Object>> list = sqlBeanClient.createJdbcTemplate().queryForList(sql,dict_code);
		return list;
	}
	
	/**
	 * 到设备类型表
	 * @return
	 */
    public List<Map<String, Object>> getDictVal(){
    	return sqlBeanClient.createJdbcTemplate().queryForList("SELECT * FROM T_SYSTEM_DICT t ");
    }
    
    /**
     * 根据ids集合查设备类型
     */
    public String getDictNames(String ids){
    	String sql = "SELECT DICT_VAL_NAME FROM T_SYSTEM_DICT WHERE VAL_ID IN ("+ids+")";
    	List<DynamicBean> bean = sqlBeanClient.createSqlQuery(sql).list();
    	Map<String,Object> map;
    	String names = null;
        for (int i = 0; i < bean.size(); i++) {
        	map = bean.get(i).getMap();
            if(i == 0){
            	names = map.get("dict_val_name").toString();
            }else{
            	names +=","+ map.get("dict_val_name").toString();
            }
		}
        return names;
    }
    
    /**
     * 发送消息的接口
     * @param toUser
     * @param title
     * @param content
     */
    public void sendMessage(String toUser,String title, String content){
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("ID", com.txy.common.util.StringUtils.getPrimarykeyId());
    	map.put("FROMUSER", SystemContainer.getUserSession().getId());
    	map.put("TOUSER", toUser);
    	map.put("SUBJECT", title);
    	map.put("CREATEDATE", new Date());
    	map.put("MESSAGECONTENT", content);
    	map.put("READFLAG", 0);
    	sqlBeanClient.createExecuteQuery().insert("T_SYSTEM_USER_MESSAGE", map);
    }

    /**
     * 查询消息详情
     * @param id
     * @return
     */
	public DynamicBean getMessageDetailInfo(String id)
	{
		String sql = "select t.*,u.username from T_SYSTEM_USER_MESSAGE t left join t_system_user u on t.fromuser = u.userid where id = ?";
		//标记这条消息已经为查看
		Map<String, Object> map = new HashMap<>();
		map.put("readflag", 1);
		sqlBeanClient.createExecuteQuery().update("T_SYSTEM_USER_MESSAGE", map, "id", id);
		return sqlBeanClient.createSqlQuery(sql, id).uniqueResult();
	}

	/**
	 * 清空已读消息信息
	 * @return
	 */
	public boolean clearReadMessage()
	{
		String sql = "delete from T_SYSTEM_USER_MESSAGE t where t.readflag = 1";
		if(sqlBeanClient.createJdbcTemplate().update(sql) != 0){
			return true;
		}
		return false;
	}

	public String getAreaNameById(String attentionArea) {
		String sql = "select wm_concat(muncpl) as name from wmp_station_dic a where a.muncpl_id in ("+attentionArea+")";
		return sqlBeanClient.createSqlQuery(sql).uniqueResult().getValue("name");
	}

	public List<Map<String, Object>> getAllAreaList(String rootAreaCode) {
		String sql = "select muncpl_id,muncpl from wmp_station_dic t where t.parentid = ?";
		List<Map<String, Object>> list = sqlBeanClient.createJdbcTemplate().queryForList(sql,rootAreaCode);
		return list;
	}

	public List<Map<String, Object>> getStations(String area) {
	    	if (area == null || "".equals(area) || "-1".equals(area))
	    	{
        	    	String sql = "select iiiii,sname from wmp_station t";
        	    	List<Map<String, Object>> list = sqlBeanClient.createJdbcTemplate().queryForList(sql);
        	    	return list;
	    	}
	    	String sql = "select iiiii,sname from wmp_station t where t.muncpl_id = ?";
	    	List<Map<String, Object>> list = sqlBeanClient.createJdbcTemplate().queryForList(sql,area);
		return list;
	}

	/**
	 * 查询站类型
	 * @return
	 */
	public List<Map<String, Object>> getStationType() {
		String sql = "select * from WMP_STATION_TYPE t";
		return sqlBeanClient.createJdbcTemplate().queryForList(sql);
	}

	public List<Map<String, Object>> getAreas(String userId) {
		
		String sqlUserAtten = "select ATTENTION_AREA from T_SYSTEM_USER where USERID = ?";
		DynamicBean userAttenObj = sqlBeanClient.createSqlQuery(sqlUserAtten, userId).uniqueResult();
		String userAtten = userAttenObj.getValue("attention_area");
		String sql = "select * from WMP_STATION_DIC t ";
		if (userAtten != null && userAtten != "")
		{
			String[] attens = userAtten.split(",");
			if (attens != null && attens.length > 0)
			{
				sql+=" where t.MUNCPL_ID in (";
			}
			
			for (int i = 0; i < attens.length; i++) {
				sql += attens[i];
				if (attens.length -1 != i)
				{
					sql+=",";
				}
			}
			if (attens != null && attens.length > 0)
			{
				sql+=")";
			}
		}
		
		
		return sqlBeanClient.createJdbcTemplate().queryForList(sql);
	}
	
}
