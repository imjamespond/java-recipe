//$Id: checkForm.js,v 1.6 2005/02/16 03:28:06 canny Exp $
//version 1.2 添加了对form的查询条件进行自动拼装的功能getQueryString();
//	对select radio checkbox等元素作了新的定义
//	相对v1.2b版纠正了几个bug
/**
 * 检查提交的form的每个表单元素是否符合填写要求
 * @fm 需要检查的form元素。
 * @return 如果form的所有表单元素都符合要求，将返回true，
 * 否则将会报告不符合的原因，同时返回false。
 * 示例：
 * <form onsubmit="return checkForm(this)">
 *  	<input type=submit>
 *  	<input name=email title="请填写邮件地址~email!">
 * </form>
 * 说明：
 * 在form表单元素的title属性中指明此表单元素期望的格式。
 * 此格式说明如下
 *          请填写邮件地址~email!
 * 以最后一个"~"为界，前面的是提示信息，后面是格式信息。
 * 格式信息请遵守以下规则：
 * [number[f]:][type][!]
 * 说明：
 * number[f]:	一个数值后面跟一个":",表示此域的文本长度不可以超过指定的数值，如果在数值后面有个f表示固定长度必须为多少位
 * type可以是如下表达式
 *	 email	邮件地址
 *	 int		整数
 *	 float	浮点数
 *	 date		日期
 *	 time		时间
 *	 hasChinese     含有中文
 *	 allChinese	全部是中文
 *	 noChinese	没有中文
 *	 /.../[gi]	自定义正则式
 * !表示此处文本不可以为空。
 * 对于<input type=radio > 格式串为
 * "请选择一个选项~!"表示此radio组必须选择一个选项
 * "请选择一个选项~"表示此radio组的选项可以不选。
 * 对于<input type=checkbox >或者<select multiple></select>格式串的意义为
 * 说明信息~min:0max:3
 * 对于<select ></select>非multiple类型
 * "请选择一个选项~!"表示此select不可以选择第一个选项
 * "请选择一个选项~"表示此select可以选择第一个选项
 */
	function checkForm(fm)
	{
		for(var i=0;i<fm.length;i++)
		{
			var title=fm[i].title;
			if(title=="")continue;//忽略未定义title的元素
			var p=title.lastIndexOf("~");
			if(p<0)continue;//忽略title中未定义检查格式的元素
			var info=title.substring(0,p);
			var format=title.substring(p+1,title.length);
			var name=fm[i].name;
			if(name=="")continue;//忽略没有名字的元素
			var value=trim(fm[i].value);
			//fm[i].value=value;//自动除去提交项的两端的空格
			
			if(fm[i].type=="radio")
			{
				if(checkRadio(fm,fm[i]))
				{
					continue;
				}
				else
				{
					return false;
				}
			}
			if(fm[i].type=="checkbox")
			{
				if(checkCheckbox(fm,fm[i]))
				{
					continue;
				}
				else
				{
				 	return false;
				}
			}
			if(fm[i].type=="select-one")
			{
				if(checkSelectOne(fm[i]))
				{
					continue;
				}
				else
				{
					return false;
				}
			}
			if(fm[i].type=="select-multiple")
			{
				if(checkSelectMultiple(fm[i]))
				{
					continue;
				}
				else
				{
					return false;
				}
			}
			var notNull=false;
			if(format.charAt(format.length-1)=="!")
			{
				notNull=true;
				format=format.substring(0,format.length-1);
			}
			if(notNull)
			{
				if(value=="")
				{
					//alert(info+"\n"+name+"的内容不可以为空。");
					alert(info);
					fm[i].focus();
					return false;
				}
			}
			else
			{
				//内容可以为空时，
				if(value=="")
					continue;
			}
			//内容的长度判断
			var colonP=format.indexOf(":");
			if(colonP>0)
			{
				if(format.charAt(colonP-1)=='f')
				{
					var lengthLimit=format.substring(0,colonP-1);
					if(!isNaN(lengthLimit))
					{
						if(value.length!=lengthLimit)
						{
							//alert(info+"\n"+name+"的长度为"+value.length+"位，必须为"+lengthLimit+"位");
							alert(info+"\n"+"必须为"+lengthLimit+"位");
							fm[i].focus();
							return false;
						}
					}
				}
				else
				{
					var lengthLimit=format.substring(0,colonP);
					if(!isNaN(lengthLimit))
					{
						if(value.length>lengthLimit)
						{
							//alert(info+"\n"+name+"的长度("+value.length+")超过限制"+lengthLimit);
							alert(info+"\n"+"超过限制"+lengthLimit);
							fm[i].focus();
							return false;
						}
					}
				}
				format=format.substring(colonP+1,format.length);
			}
			if(format=="email")
			{
				//电子邮件格式
				var found=value.match(/\w+@.+\..+/);
				if(found==null)
				{
					//alert(info+"\n"+name+"的格式不正确:\n\""+value+"\"不是一个Email地址");
					alert(info+"\n\""+value+"\"不是一个Email地址");
					fm[i].focus();
					return false;
				}
			}
			else if(format=="int")
			{
				//整数
				var intVal=parseInt(value);
				if(isNaN(intVal)||intVal!=value)
				{
					//alert(info+"\n"+name+"的格式不正确:\n"+value+"不是一个整数。");
					alert(info+"\n\""+value+"\"不是一个整数。");
					fm[i].focus();
					return false;
				}
			}
			else if(format=="float")
			{
				//浮点数
				var floatVal=parseFloat(value);
				if(isNaN(floatVal)||floatVal!=value)
				{
					//alert(info+"\n"+name+"的格式不正确:\n"+value+"不是一个浮点数。");
					alert(info+"\n\""+value+"\"不是一个浮点数。");
					fm[i].focus();
					return false;
				}
			}
			else if(format=="date")
			{
				//日期
				var found=value.match(/(\d{1,5})-(\d{1,2})-(\d{1,2})/);
				if(found==null||found[0]!=value||found[2]>12||found[3]>31)
				{
					//alert(info+"\n"+name+"的格式不正确:\n\""+value+"\"不是一个日期\n提示：[2000-01-01]");
					alert(info+"\n\""+value+"\"不是一个日期\n提示：[2000-01-01]");
					fm[i].focus();
					return false;
				}
				var year=trim0(found[1]);
				var month=trim0(found[2])-1;
				var date=trim0(found[3]);
				var d=new Date(year,month,date);
				if(d.getFullYear()!=year||d.getMonth()!=month||d.getDate()!=date)
				{
					//alert(info+"\n"+name+"的内容不正确:\n\""+value+"\"不是一个正确的日期\n提示：[2000-01-01]");
					alert(info+"\n\""+value+"\"不是一个正确的日期\n提示：[2000-01-01]");
					fm[i].focus();
					return false;
				}
			}
			else if(format=="time")
			{
				//时间
				var found=value.match(/(\d{2}):(\d{2}):(\d{2})/);
				if(found==null||found[0]!=value||found[1]>23||found[2]>59||found[3]>59)
				{
					//alert(info+"\n"+name+"的格式不正确:\n\""+value+"\"不是一个时间\n提示：[05:38:00]");
					alert(info+"\n\""+value+"\"不是一个时间\n提示：[05:38:00]");
					fm[i].focus();
					return false;
				}
			}
			else if(format=="hasChinese")
			{
				var _hasChinese=false;
				for(var j=0;j<value.length;j++)
				{
					if(value.charCodeAt(j)>255)
					{
						_hasChinese=true;
						break;
					}
				}
				if(!_hasChinese)
				{
					//alert(info+"\n"+name+"的内容需要中文:\n\""+value+"\"不含有任何中文字符");
					alert(info+"\n\""+value+"\"不含有任何中文字符");
					fm[i].focus();
					return false;
				}
			}
			else if(format=="allChinese")
			{
				for(var j=0;j<value.length;j++)
				{
					if(value.charCodeAt(j)<=255)
					{
						//alert(info+"\n"+name+"的内容要求全中文:\n\""+value+"\"含有非中文字符");
						alert(info+"\n\""+value+"\"含有非中文字符");
						fm[i].focus();
						return false;
					}
				}
			}
			else if(format=="noChinese")
			{
				for(var j=0;j<value.length;j++)
				{
					if(value.charCodeAt(j)>255)
					{
						//alert(info+"\n"+name+"的内容要求非中文:\n\""+value+"\"含有中文字符");
						alert(info+"\n\""+value+"\"含有中文字符");
						fm[i].focus();
						return false;
					}
				}
			}
			else if(format!="")
			{
				//自定义
				try
				{
					var found=value.match(eval(format));
					if(found==null||found[0]!=value)
					{
						//alert(info+"\n"+name+"的格式不不符合要求:\""+value+"\"\n提示：["+format+"]");
						alert(info+"\n\""+value+"\"的格式不不符合要求\n提示：["+format+"]");
						fm[i].focus();
						return false;
					}
				}
				catch(e)
				{
					alert(e.name+":\n["+fm[i].name+"]不合法的正则式\""+format+"\"");
					return false;
				}
			}
		}
		return true;
	}
	function checkRadio(fm,opt)
	{
		var title=opt.title;
		if(title=="")return true;//忽略未定义title的元素
		var p=title.lastIndexOf("~");
		if(p<0)return true;//忽略title中未定义检查格式的元素
		var info=title.substring(0,p);
		var format=title.substring(p+1,title.length);
		var name=opt.name;
		if(name=="")return true;//忽略没有名字的元素
		if(format=="!")
		{
			//必须选择一个选项
			if(typeof(fm.all[name].length)=="undefined")
			{
				//同名radio只有一个
				if(opt.checked)
				{
					return true;
				}
				else
				{
					//alert(info+"\n必须选择"+name);
					alert(info);
					opt.focus();
					return false;
				}
			}
			else
			{
				//是一个radio组
				var radios=fm[name];
				for(var j=0;j<radios.length;j++)
				{
					if(radios[j].checked==true)return true;
				}
				//alert(info+"\n必须选择"+name+"的一个选项");
				alert(info);
				opt.focus();
				return false;
			}
		}
		else
		{
			//可以一个选项也不选
			return true;
		}
	}
	function checkCheckbox(fm,opt)
	{
		var title=opt.title;
		if(title=="")return true;//忽略未定义title的元素
		var p=title.lastIndexOf("~");
		if(p<0)return true;//忽略title中未定义检查格式的元素
		var info=title.substring(0,p);
		var format=title.substring(p+1,title.length);
		var name=opt.name;
		if(name=="")return true;//忽略没有名字的元素

		var min=format.match(/min:(\d+)\w*/);
		var max=format.match(/\w*max:(\d+)/);

		if(typeof(fm.all[name].length)=="undefined")
		{
			//只有一个同名checkbox
			if(min!=null)
			{
				if(min[1]==1&&!opt.checked)
				{
					//alert(info+"\n必须选上"+name+"选项");
					alert(info);
					opt.focus();
					return false;
				}
			}
		}
		else
		{
			//一个checkbox组
			var checkboxes=fm.all[name];
			var check_count=0;
			for(var j=0;j<checkboxes.length;j++)
			{
				if(checkboxes[j].checked)check_count++;
			}
			if(min!=null)
			{
				if(min[1]>check_count)
				{
					//alert(info+"\n"+name+"至少需要选择"+min[1]+"个选项");
					alert(info);
					opt.focus();
					return false;
				}
			}
			if(max!=null)
			{
				if(max[1]<check_count)
				{
					//alert(info+"\n"+name+"至多可以选择"+max[1]+"个选项");
					alert(info);
					opt.focus();
					return false;
				}
			}
		}
		return true;
	}
	function checkSelectOne(sel)
	{
		var title=sel.title;
		if(title=="")return true;//忽略未定义title的元素
		var p=title.lastIndexOf("~");
		if(p<0)return true;//忽略title中未定义检查格式的元素
		var info=title.substring(0,p);
		var format=title.substring(p+1,title.length);
		var name=sel.name;
		if(name=="")return true;//忽略没有名字的元素
		
		if(format=="!"&&sel.selectedIndex==0)
		{
			//alert(info+"\n"+name+"不可以选择第一个个选项");
			alert(info);
			sel.focus();
			return false;
		}
		return true;
	}
	function checkSelectMultiple(sel)
	{
		var title=sel.title;
		if(title=="")return true;//忽略未定义title的元素
		var p=title.lastIndexOf("~");
		if(p<0)return true;//忽略title中未定义检查格式的元素
		var info=title.substring(0,p);
		var format=title.substring(p+1,title.length);
		var name=sel.name;
		if(name=="")return true;//忽略没有名字的元素

		var min=format.match(/min:(\d+)\w*/);
		var max=format.match(/\w*max:(\d+)/);
		
		var select_count=0;
		for(var j=0;j<sel.length;j++)
		{
			if(sel[j].selected)select_count++;
		}
		if(min!=null)
		{
			if(min[1]>select_count)
			{
				//alert(info+"\n"+name+"至少需要选择"+min[1]+"个选项");
				alert(info);
				sel.focus();
				return false;
			}
		}
		if(max!=null)
		{
			if(max[1]<select_count)
			{
				//alert(info+"\n"+name+"至多可以选择"+max[1]+"个选项");
				alert(info);
				sel.focus();
				return false;
			}
		}
		return true;
	}
/**
 * 除去字符串变量s两端的空格。
 */
	function trim(s)
	{
		s=s.replace(/^ */,"");
		s=s.replace(/ *$/,"");
		return s;
	}
/**
 * 除去字符串表示的数值变量开头的所有的"0"。
 * 比如
 * 	trim0("01")将返回"1"
 * 	trim0("1")将返回"1"
 * 	trim0("10")将返回"10"
 * 	trim0("000")将返回"0"
 */
	function trim0(s)
	{
		if(s.length==0)return s;
		s=s.replace(/^0*/,"");
		if(s.length==0)s="0";
		return s;
	}
/**
 * 取得一个form对象所提交时内部提交参数的QueryString
 * 形如:
 * ?accountName=&userName=&email=&area=0&credit_low=&credit_high=&age_low=&age_high=&userLevel=0
 */
	function getQueryString(fm)
	{
		var qStr="";
		for(var i=0;i<fm.length;i++)
		{
			if(!fm[i].disabled)
			{
				var n=fm[i].name;
				if(n==null)continue;
				if(n.length==0)continue;
				if(fm[i].type=="select-multiple")
				{
					var _vs=fm[i].options;
					for(var _j=0;_j<_vs.length;_j++)
					{
						var _opt=_vs(_j);
						if(_opt.selected)
						{
							var v=_opt.value;
							qStr=qStr+"&"+n+"="+ec(v);
						}
					}
				}
				else
				{
					var v=fm[i].value;
					if(fm[i].type=="radio"||fm[i].type=="checkbox")
					{
						if(!fm[i].checked)continue;
					}
					qStr=qStr+"&"+n+"="+ec(v);
				}
			}
		}
		if(qStr.length>0)qStr="?"+qStr.substr(1);
		return qStr;
	}
	function ec(va)
	{
		return va.replace(/\n/g,"%0D%0A");
	}