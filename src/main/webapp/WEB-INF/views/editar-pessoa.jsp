<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="./base.jsp"%>
<%
SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
%>

<div class="d-flex justify-content-center">
	<c:if test="${not empty mensagem}">
		<div class="alert alert-success" role="alert">${mensagem}</div>
	</c:if>
	<c:if test="${not empty mensagem_error}">
		<div class="alert alert-danger" role="alert">${mensagem_error}</div>
	</c:if>
</div>
<div class="container mt-3">
	<div class="card card-custom-width">
		<div class="card-body">
			<h1 class="text-center">Editar Pessoa</h1>
			<form method="POST" action="/portfolio/pessoas/atualizar"
				modelAttribute="editar-pessoa">
				<div class="form-group d-none">
					<label for="id">Id</label> <input type="text" value="${pessoa.id}"
						class="form-control" id="nomePessoa" name="id" readonly="readonly">
				</div>
				<div class="form-group">
					<label for="nome">Nome</label> <input type="text"
						value="${pessoa.nome}" class="form-control" id="nomePessoa"
						name="nome">
				</div>
				<div class="form-group">
					<label for="nome">CPF</label>&nbsp;&nbsp; <a
						href="https://www.4devs.com.br/gerador_de_cpf" target="_blank">Simule
						um CPF válido aqui</a> <input type="text" value="${pessoa.cpf}"
						class="form-control" id="cpfPessoa" name="cpf">
				</div>
				<div class="form-group">
					<label for="nome">Data de Nascimento</label> <input type="text"
						value="<fmt:formatDate value="${pessoa.dataNascimento}" pattern="dd/MM/yyyy" />"
						class="form-control" id="dataNascimentoPessoa"
						name="dataNascimento" placeholder="DD/MM/AAAA">
				</div>
				<div class="form-group">
					<label for="funcionarioPessoa">Funcionário?</label> <select
						class="form-control" id="funcionarioPessoa" name="funcionario">
						<option value="true" ${pessoa.funcionario ? 'selected' : ''}>SIM</option>
						<option value="false" ${!pessoa.funcionario ? 'selected' : ''}>NÃO</option>
					</select>
				</div>
				<div class="text-center">
					<button type="submit" class="btn btn-success m-2">Atualizar</button>
				</div>
			</form>

		</div>
	</div>
</div>
<jsp:include page="rodape.jsp" />
