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
			<h1 class="text-center">Cadastrar Pessoa</h1>
			<form:form id="formPessoa" method="POST" action="/portfolio/pessoas/salvar"
				modelAttribute="cadastrar-pessoa">

				<div class="form-group">
					<label for="nome">Nome</label> <input type="text"
						class="form-control" id="nomePessoa" name="nome"
						required="required" placeholder="Digite o nome da pessoa"
						value="${pessoa.nome != null ? pessoa.nome : ''}">
				</div>
				<div class="form-group">
					<label for="nome">CPF</label>&nbsp;&nbsp; <a
						href="https://www.4devs.com.br/gerador_de_cpf" target="_blank">    Simule
						um CPF válido aqui</a> <input type="text" class="form-control"
						id="cpfPessoa" name="cpf" required="required"
						placeholder="00000000000"
						value="${pessoa.cpf != null ? pessoa.cpf : ''}">
				</div>				
				<div class="form-group">
					<label for="nome">Data de Nascimento</label> <input type="text"
						class="form-control" id="dataNascimentoPessoa"
						name="dataNascimento" required="required" placeholder="DD/MM/AAAA"
						value="<fmt:formatDate value="${pessoa.dataNascimento}" pattern="dd/MM/yyyy" />">
					<small id="dataNascimentoError" class="form-text text-danger"></small>
				</div>
				<div class="form-group">
					<label for="opcao">Funcionário?</label> <select name="funcionario"
						required="required" id="funcionario" class="form-control">
						<option value="" ${pessoa.funcionario == null ? 'selected' : ''}>Selecione</option>
						<option value="true"
							${pessoa.funcionario == true ? 'selected' : ''}>SIM</option>
						<option value="false"
							${pessoa.funcionario == false ? 'selected' : ''}>NÃO</option>
					</select>
				</div>
				<div class="text-center">
					<input type="submit" value="Salvar" class=" btn btn-success m-2">
				</div>
			</form:form>
		</div>
	</div>
</div>
<jsp:include page="rodape.jsp" />
