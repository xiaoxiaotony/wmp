
$.extend($.fn.validatebox.defaults.rules, {
	equals: {
	validator: function(value,param){
		return value == $(param[0]).val();
	},
	message: '输入的密码不一致'
	}
});

//输入的字符长度验证
$.extend($.fn.validatebox.defaults.rules, {
	mylength: {
	validator: function(value,param){
		var len = value.length;
		return (len >=param[0] && len <= param[1]);
	},
	message: '{2}'
	}
});

//检查登陆名是否重复
$.extend($.fn.validatebox.defaults.rules, {
	checkName: {
	validator: function(value,param){
        var json = '{"'+param[1]+'":"'+value+'"}';
        var flag = '';
		$.ajax({type:"POST",
				url:contentPath+param[0],
				data:$.parseJSON(json),
				async : false,
				success:function(data){
					flag = data.data.success;
				}
		});
		return flag;
	},
	message: '{2}'
	}
});

//验证手机号码
$.extend($.fn.validatebox.defaults.rules, {
	phone: {
	validator: function(value,param){
		var length = value.length;
		var mobile = /^(((13[0-9]{1})|(15[0-9]{1}))+\d{8})$/;
		return (length == 11 && mobile.test(value));
	},
	message: '请输入正确的电话号码'
	}
});

//特殊字符过滤
$.extend($.fn.validatebox.defaults.rules, {
	isChar: {
	validator: function(value,param){
	    var mobile = /[~#^$@%&!*]/gi;  
	    return !(mobile.test(value));
	},
	message: '不能有特殊字符[~#^$@%&!*]'
	}
});