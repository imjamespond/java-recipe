<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./common/taglib.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<head>
	<style type="text/css">
		body {
			padding-top: 40px;
			padding-bottom: 40px;
			background-color: #f5f5f5;
		}

		.form-signin {
			max-width: 300px;
			padding: 19px 29px 29px;
			margin: 0 auto 20px;
			background-color: #fff;
			border: 1px solid #e5e5e5;
			-webkit-border-radius: 5px;
			-moz-border-radius: 5px;
			border-radius: 5px;
			-webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
			-moz-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
			box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
		}

		.form-signin .form-signin-heading,
		.form-signin .checkbox {
			margin-bottom: 10px;
		}

		.form-signin input[type="text"],
		.form-signin input[type="password"] {
			font-size: 16px;
			height: auto;
			margin-bottom: 15px;
			padding: 7px 9px;
		}

	</style>

</head>
<div class="container">

	<form class="form-signin" name='f' action='/insure/post' method='POST'>
		<h2 class="form-signin-heading">填写保单</h2>

		快递公司:
		<input type="text" class="input-block-level" name="express" >
		</br>
		快递单号:
		<input type="text" class="input-block-level" name='expressid'>
		</br>
		物品:
		<input type="text" class="input-block-level" name='item'>
		</br>
		物品金额:
		<input type="text" class="input-block-level" name='ivalue'>
		</br>
				
		保费:
		<input type="text" class="input-block-level" name='cost'>
		</br>

		<button class="btn btn-large btn-primary" type="submit">提交</button>
	</form>

</div>