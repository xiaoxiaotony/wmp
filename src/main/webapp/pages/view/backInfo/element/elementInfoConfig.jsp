<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div title="地面数据要素维护" data-options="closable:true">
	<!-- 查询面板 -->
	<div class="search_bar" id="elementInfo_search_bar_div"></div>
	<!-- 工具栏 -->
	<div class="cont_tit" id="elementInfo_button_bar_user_div"></div>
	<!--table-->
	<div class="table_cont">
		<table id="elementInfo_list_data">
			<thead>
				<tr>
					<th field="elementname" width="20%" sortable="true">要素名称</th>
					<th field="elementcode" width="20%">要素编码</th>
					<th field="elementvalue" width="20%">阈值</th>
					<th field="isshow" width="20%" formatter="isshow">是否显示</th>
					<th field="id" width="20%" data-options="align:'center'" formatter="oper_element">操作</th>
				</tr>
			</thead>
		</table>
	</div>
	<!--底部分页 end-->
</div>
<script>

var queryParams = {
	queryId : "elementInfo_search_bar_div",
	body : [{
		column : 'searchKey',
		name : '模糊信息：',
		type : "text",
		placeholder : "关键字"
	} ],
	queryDataGrid : "elementInfo_list_data"
}

/**
 * 创建查询面板
 */
DataGrid.initQueryPanel(queryParams);

/**
 * start format
 */
function oper_element(val,row){
	
	return "<a href='javascript:void(0);' onclick='update_element(\""+val
			+"\")'>修改</a>";
	
}
function isshow(val,row){
	if (val == "0")
	{
		return "显示";
	}
	return "不显示";
}
/* var toolbarParams = {
	boolbarId : "elementInfo_button_bar_user_div",
	title : "地面实时观测的要素维护列表",
	body :[{
		icon : "icon-plus",
		text : "新增",
		hidden : false,
		event : function(){
              add();
		}
	}]
} */
/**
 * 添加操作工具面板
 */
DataGrid.initToolsBarPanel(toolbarParams);


/**
 * 初始化表格数据
 */
$('#elementInfo_list_data').datagrid({
    nowrap: false,  //一行显示数据，提高加载性能true
    striped: true,  //隔行换色
    collapsible:false,//是否可折叠的 
    fit: true,//height 100%
    url:contentPath+"/systemBackInfoHandler/getElementPageList", 
    remoteSort:false, //要排序的数据从服务器定义 
	pageSize: 25,//每页显示的记录条数，默认为10 
    pageList: [25,30,50,100],//可以设置每页记录条数的列表 
    pagination:true,//分页控件 
    rownumbers:false,//行号 
    //冻结列在左
    frozenColumns:[[
       {field:'ck',checkbox:true} 
    ]]
});


/**
 * 批量删除用户
 */
function delete_element(id){
	$.messager.confirm('消息', '你确定要删除？', function(r){
		if(r){
			var url = contentPath+"/systemBackInfoHandler/delete";
			var data = {id:id};
			Util.getAjaxData(url,data,function(data){
				       if(data.success){
				    	   topCenter("数据删除成功！");
				    	   $('#elementInfo_list_data').datagrid('reload');
				       }else{
				    	   topCenter("数据删除失败！");
				       }
			},true);
		}
	});
	
}

function update_element(id){
	$('#dialog_content').dialog({
		title : '修改要素数据',
		width : 400,
		height : 300,
		closed : false,
		cache : false,
		href : 'view/backInfo/element/updateElementInfo.jsp?id=' +id,
		modal : true
	});
}

/**
 * 添加要素数据
 */
function add(){
		$('#dialog_content').dialog({
			title: '添加要素数据',
			width: 500,
			height: 500,
			closed: false,
			cache: false,
			href: 'view/backInfo/element/addElementInfo.jsp',
			modal: true
		});
}

//提示消息弹窗
function topCenter(messges,dialog_size){
	if(!dialog_size) dialog_size=0;
	$.messager.show({
		title:'消息',
		msg:messges,
		showType:'slide',
		style:{
				right:'',
				top:document.body.scrollTop+document.documentElement.scrollTop-dialog_size,
				bottom:''
		}
	});
}


//设置分页控件 
var p = $('#elementInfo_list_data').datagrid('getPager');
$(p).pagination({ 
    beforePageText: '第',//页数文本框前显示的汉字 
    afterPageText: '页    共 {pages} 页', 
    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
});
</script>
