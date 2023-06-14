<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ include file="./base.jsp"%>

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
			<form:form method="POST" action="/codedeving/pessoas/salvar"
				modelAttribute="gerenteRequest">
				
				<div class="form-group">
					<label for="nome">Nome</label> <input type="text"
						class="form-control" id="nomePessoa" name="nome" required="required"
						placeholder="Digite o nome da pessoa">
				</div>
				<div class="form-group">
					<label for="nome">CPF</label> <input type="text"
						class="form-control" id="cpfPessoa" name="cpf" required="required"
						placeholder="Digite o CPF. Apenas números.">
				</div>
				<div class="form-group">
					<label for="nome">Data de Nascimento</label> <input type="text"
						class="form-control" id="dataNascimentoPessoa"
						name="dataNascimento" required="required" placeholder="DD/MM/YYYY">
				</div>
				<div class="form-group">
					<label for="opcao">Funcionário?</label> <select name="funcionario"
						id="funcionario" class="form-control">
						<option value="true">SIM</option>
						<option value="false">NÃO</option>
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
