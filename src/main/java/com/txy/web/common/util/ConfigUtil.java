package com.txy.web.common.util;

import java.util.HashMap;
import java.util.Map;

import com.txy.common.orm.query.impl.DynamicBean;

/**
 * @ClassName: ConfigUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author lqy
 * @date 2016年1月13日 下午2:46:58
 * 
 */
public class ConfigUtil
{
    private static Map<String, String> MODEL_DATA = new HashMap<String, String>();
    private static Map<String, Map<String, String>> COLUMNAME = new HashMap<String, Map<String, String>>();
    static
    {
        // key为模型名称 value为要查询的sql，
        MODEL_DATA
                .put("wmp_device",
                        "select t.name, a.muncpl as areaname,a.muncpl_id,t.ip, count(k.status) as num_count from wmp_device t left join wmp_station_dic a on t.area = a.muncpl_id left join (select to_status as status, toip from wmp_network_node_data  ) k on t.ip = k.toip where 1 = 1   and t.type = 1 and t.name in (select c.name from wmp_transfer_config_detail c where c.type = 10000)    group by t.name, a.muncpl,a.muncpl_id, t.ip order by num_count desc");
        MODEL_DATA
                .put("wmp_dataTransfer",
                        "select a.iiiii, c.sname,c.muncpl_id, b.ctype_desc, trunc((case when ((a.shishou / a.yingshou) * 100) > 100 then 100 else (a.shishou / a.yingshou) * 100 end), 0) as daobaolv, trunc((a.jishi / a.yingshou) * 100, 0) as jishilv, trunc((a.yuxian / a.yingshou) * 100, 0) as yuxianlv, trunc(((case when (a.yingshou - a.shishou) < 1 then 0 else a.yingshou - a.shishou end) / a.yingshou) * 100, 0) as queshoulv from wmp_tfmonitor_count_day a, tr_datatype_dic b, wmp_station c where a.dtype = b.dtype and a.ctype = b.ctype and a.iiiii = c.iiiii and a.iiiii in ('u1518', '55594', '55586', '55499', 'u1801', 'u1802', 'u1803', 'u1500', 'u1501', 'u1502', 'u1503', 'u1504', 'u1505', 'u1507', 'u1508', 'u1509', 'u1510', 'u1512', 'u1513', 'u1514', 'u1515', 'u1517', 'u1519', '55595', '55590', '55489', '55497', '55592', '55493', '55585', '55591', '55593', 'u1001', 'u1003', 'u1004', 'u1010', 'u1011', 'u1012', 'u1013', 'u1014', 'u1015', 'u1016', 'u1017', 'u1018', 'u1019', 'u1020', 'u1021', 'u1022', 'u1023', 'u1024', 'u1025', 'u1026', 'u1070', 'u1506', 'u1511', 'u1516') and a.dtype in ('aws', 'radi', 'aws', 'aws', 'aws', 'agm', 'aws', 'aws', 'agm', 'aws', 'aws', 'aws', 'aws', 'aws', 'aws', 'aws', 'aws') and a.ctype in ('st_day_qc', 'sad', 'mon', 'st_day', 'st_new', 'asm', 'st_min', 'aws_rd', 'ab', 'st-reg_qc', 'st', 'ss_qc', 'st_new_qc', 'st-reg', 'prf_qc', 'ss', 'prf') and a.d_datetime = to_date('2016-1-18 00:00:00', 'yyyy/mm/dd hh24:mi:ss')");
        
        // 列名称和中文名称 列名称必须与上面sql查询的列名称相对应
        Map<String, String> columName = new HashMap<String, String>();
        columName.put("NAME", "设备名称 ");
        columName.put("monthDate", "时间 ");
        columName.put("AREANAME", "设备区域 ");
        columName.put("IP", "IP地址 ");
        columName.put("status", "状态");
        columName.put("NUM_COUNT", "次数");
        
        Map<String, String> wmpDataTransferColumn = new HashMap<String, String>();
        wmpDataTransferColumn.put("IIIII", "站号");
        wmpDataTransferColumn.put("SNAME", "站名");
        wmpDataTransferColumn.put("CTYPE_DESC", "资料子类型");
        wmpDataTransferColumn.put("DAOBAOLV", "到报");
        wmpDataTransferColumn.put("JISHILV", "及时");
        wmpDataTransferColumn.put("QUESHOULV", "缺失");
        wmpDataTransferColumn.put("YUXIANLV", "逾限");
        
        // 模型名称和列名map
        COLUMNAME.put("wmp_device", columName);
        COLUMNAME.put("wmp_dataTransfer", wmpDataTransferColumn);
        
    }
    
    public static String getModelSql(String model)
    {
        return MODEL_DATA.get(model);
    }
    
    public static Map<String, String> getColumName(String model, DynamicBean bean)
    {
        Map<String, String> result = new HashMap<String, String>();
        Map<String, String> temps = COLUMNAME.get(model);
        if (bean != null && bean.getValue("row_name") != null)
        {
            String[] rowNames = bean.getValue("row_name").split(",");
            for (String rowName : rowNames)
            {
                for (String key : temps.keySet())
                {
                    if (rowName.toLowerCase().equals(key.toLowerCase()))
                    {
                        result.put(key, temps.get(key));
                    }
                }
            }
        }
        return result;
    }
    public static Map<String, String> getColumName(String model)
    {
        return  COLUMNAME.get(model);
    }
}
