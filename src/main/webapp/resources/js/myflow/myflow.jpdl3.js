(function($){
var myflow = $.myflow;
$.extend(true,myflow.config.tools.states,{
	state : {type : 'state',
		name : {text:'<<state>>'},
		text : {text:'状态'},
		img : {src : '../resources/js/myflow/img/16/task_empty.png',width : 16, height:16},
		props : {
			text: {name:'text',label: '显示', value:'test', editor: function(){return new myflow.editors.textEditor();}, value:'状态'}
		}}
});
})(jQuery);