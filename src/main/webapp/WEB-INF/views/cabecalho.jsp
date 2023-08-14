<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

</head>
<body>
	<div>
		<img src="${pageContext.request.contextPath}/imagens/logo.png" alt="AR - Soluções em Tecnologia" class="logo-cabecalho">
	</div>
		<div class="card-body text-center mt-3">			
			<a href="${pageContext.request.contextPath}/pessoas/novo"
				class="btn btn-red-personalizado">Cadastrar Pessoa</a>			
			<a href="${pageContext.request.contextPath}/pessoas/listar"
				class="btn btn-red-personalizado">Pessoas Cadastradas</a>
			<a href="${pageContext.request.contextPath}/projetos/novo"
				class="btn btn-red-personalizado">Cadastrar Projeto</a>		
			<a href="${pageContext.request.contextPath}/projetos/listar"
				class="btn btn-red-personalizado">Projetos Cadastrados</a>
		</div>	
</body>
</html>