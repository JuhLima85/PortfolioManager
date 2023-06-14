<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./base.jsp"%>

<div class="container mt-3 text-center">
	<div class="card card-custom-width">
		<div class="card-body">

			<h1>Ocorreu um erro!</h1>
			
			<img src="${pageContext.request.contextPath}/imagens/erro3.jpg"
				alt="Erro" style="width: 50%;" />			
			<p style="font-size: 20px;">${mensagem}</p>		
		</div>
	</div>
</div>
