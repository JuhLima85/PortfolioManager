<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
			<h1 class="text-center">Editar Projeto</h1>
			<form method="POST" action="/codedeving/projetos/atualizar">
				<div class="form-group d-none">
					<label for="id">Id</label> <input type="text" value="${projeto.id}"
						class="form-control" id="nomeProjeto" name="id"
						readonly="readonly">
				</div>
				<div class="form-group">
					<label for="nome">Nome</label> <input type="text"
						value="${projeto.nome}" class="form-control" id="nomeProjeto"
						name="nome" required="required">
				</div>
				<div class="form-group">
					<label for="nome">Descrição</label> <input type="text"
						value="${projeto.descricao}" class="form-control"
						id="descricaoProjeto" name="descricao" required="required">
				</div>
				<div class="form-group">
					<label for="nome">Data de Início</label> <input type="text"
						value="<fmt:formatDate value="${projeto.dataInicio}" pattern="dd/MM/yyyy" />"
						class="form-control" id="dataInicioProjeto" name="dataInicio" required="required" placeholder="DD/MM/AAAA">
				</div>
				<div class="form-group" id="dataFimField" style="display: none;">
					<label for="nome">Data do Término</label> <input type="text"
						value="<fmt:formatDate value="${projeto.dataFim}" pattern="dd/MM/yyyy" />"
						class="form-control" id="dataFimProjeto" name="dataFim" placeholder="DD/MM/AAAA">
				</div>
				<div class="form-group">
					<label for="nome">Previsão de Término</label> <input type="text"
						value="<fmt:formatDate value="${projeto.dataPrevisaoFim}" pattern="dd/MM/yyyy" />"
						class="form-control" id="dataPrevisaoFimProjeto"
						name="dataPrevisaoFim" required="required" placeholder="DD/MM/AAAA">
				</div>
				<div class="form-group">
					<label for="nome">Orçamento</label> <input type="text"
						class="form-control" id="orcamentoProjeto" name="orcamento" required="required"
						value="${orcamentoFormatado}">
				</div>
				<div class="form-group">
					<label for="statusProjeto">Status</label> <select
						class="form-control" id="statusProjeto" name="selectedStatus">
						<option value="${projeto.status}" selected>${projeto.status}</option>
						<c:choose>
							<c:when test="${projeto.status == 'Planejado'}">
								<option value="Em análise">Em análise</option>
								<option value="Análise realizada">Análise realizada</option>
								<option value="Análise aprovada">Análise aprovada</option>
								<option value="Iniciado">Iniciado</option>
								<option value="Em andamento">Em andamento</option>
								<option value="Encerrado">Encerrado</option>
								<option value="Cancelado">Cancelado</option>
							</c:when>
							<c:when test="${projeto.status == 'Em análise'}">
								<option value="Análise realizada">Análise realizada</option>
								<option value="Análise aprovada">Análise aprovada</option>
								<option value="Iniciado">Iniciado</option>
								<option value="Em andamento">Em andamento</option>
								<option value="Encerrado">Encerrado</option>
								<option value="Cancelado">Cancelado</option>
							</c:when>
							<c:when test="${projeto.status == 'Análise realizada'}">
								<option value="Análise aprovada">Análise aprovada</option>
								<option value="Iniciado">Iniciado</option>
								<option value="Em andamento">Em andamento</option>
								<option value="Encerrado">Encerrado</option>
								<option value="Cancelado">Cancelado</option>
							</c:when>
							<c:when test="${projeto.status == 'Análise aprovada'}">
								<option value="Iniciado">Iniciado</option>
								<option value="Em andamento">Em andamento</option>
								<option value="Encerrado">Encerrado</option>
								<option value="Cancelado">Cancelado</option>
							</c:when>
							<c:when test="${projeto.status == 'Iniciado'}">
								<option value="Em andamento">Em andamento</option>
								<option value="Encerrado">Encerrado</option>
								<option value="Cancelado">Cancelado</option>
							</c:when>
							<c:when test="${projeto.status == 'Em andamento'}">
								<option value="Encerrado">Encerrado</option>
								<option value="Cancelado">Cancelado</option>
							</c:when>
							<c:when test="${projeto.status == 'Encerrado'}">
								<option value="Cancelado">Cancelado</option>
							</c:when>
							<c:otherwise>
								<option value="Planejado">Planejado</option>
								<option value="Em análise">Em análise</option>
								<option value="Análise realizada">Análise realizada</option>
								<option value="Análise aprovada">Análise aprovada</option>
								<option value="Iniciado">Iniciado</option>
								<option value="Em andamento">Em andamento</option>
								<option value="Encerrado">Encerrado</option>
								<option value="Cancelado">Cancelado</option>
							</c:otherwise>
						</c:choose>
					</select>
				</div>
				<div class="form-group">
					<label for="opcao">Gerente Responsável</label> <select
						class="form-control" name="gerentes" id="gerentes">
						<c:forEach items="${pessoas}" var="proximo">
							<c:if test="${proximo.funcionario}">
								<option value="${proximo.id}"
									${projeto.gerenteResponsavel != null && projeto.gerenteResponsavel.id == proximo.id ? 'selected' : ''}>
									${proximo.nome}</option>
							</c:if>
						</c:forEach>
					</select>
				</div>				
				<div class="text-center">
					<button type="submit" class="btn btn-success m-2">Atualizar</button>
				</div>
			</form>
		</div>
	</div>
</div>

<script>
	function handleStatusChange() {
		var statusProjeto = document.getElementById("statusProjeto");
		var dataFimField = document.getElementById("dataFimField");

		if (statusProjeto.value === "Encerrado") {
			dataFimField.style.display = "block";
		} else {
			dataFimField.style.display = "none";
		}
	}

	document.addEventListener('DOMContentLoaded', function() {
		formatarOrcamento();
	});

	function formatarOrcamento() {
		var orcamentoInput = document.getElementById('orcamentoProjeto');
		orcamentoInput.value = orcamentoInput.value.replace(/([^0-9.])/g, '');
		orcamentoInput.value = orcamentoInput.value.replace(/\./g, '');
		orcamentoInput.value = orcamentoInput.value.replace(
				/(\d)(?=(\d{3})+(?!\d))/g, '$1.');
		orcamentoInput.value = orcamentoInput.value.replace(/,/g, '.');
	}

	$(document).ready(function() {
		$('#orcamentoProjeto').mask('000.000.000.000.000,00', {
			reverse : true
		});
	});

	document.querySelector('form').addEventListener('submit', function() {
		var orcamentoInput = document.getElementById('orcamentoProjeto');
		orcamentoInput.value = orcamentoInput.value.replace(/\./g, '');
		orcamentoInput.value = orcamentoInput.value.replace(',', '.');
	});

	var statusProjeto = document.getElementById("statusProjeto");
	statusProjeto.addEventListener("change", handleStatusChange);
	window.addEventListener("load", handleStatusChange);
</script>
<jsp:include page="rodape.jsp" />
