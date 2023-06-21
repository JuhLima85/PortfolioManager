<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="./base.jsp"%>
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
	<div class="card card-custom-width-two">
		<div class="card-body">
			<h1 class="text-center">Cadastrar Projeto</h1>
			<form:form method="POST" action="/codedeving/projetos/salvar">
				<div class="row">
					<div class="col-md-4 mb-3">
						<div class="form-group">
							<br> <label for="nome">Nome do Projeto</label> <input
								type="text" class="form-control" id="nomeProjeto" name="nome"
								required="required" placeholder="Informe o nome do projeto"
								value="${projeto.nome != null ? projeto.nome : ''}">
						</div>
					</div>
					<div class="col-md-6 mb-3">
						<div class="form-group">
							<br> <label for="nome">Descrição</label> <input type="text"
								class="form-control" id="descricaoProjeto" name="descricao"
								required="required" placeholder="Descrição do projeto"
								value="${projeto.descricao != null ? projeto.descricao : ''}">
						</div>
					</div>
					<div class="col-md-2 mb-3">
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
					</div>

				</div>
				<div class="form-row">
					<div class="col-md-2 mb-3">
						<div class="form-group">
							<label for="nome">Data de Início</label> <input type="text"
								class="form-control" id="dataInicioProjeto" name="dataInicio"
								required="required" placeholder="Data de inicio"
								value="<fmt:formatDate value="${projeto.dataInicio}" pattern="dd/MM/yyyy" />">
						</div>
					</div>
					<div class="col-md-2 mb-3">
						<div class="form-group">
							<label for="nome">Previsão de Término</label> <input type="text"
								class="form-control" id="dataPrevisaoFimProjeto"
								name="dataPrevisaoFim" required="required"
								placeholder="Previsão de Término"
								value="<fmt:formatDate value="${projeto.dataPrevisaoFim}" pattern="dd/MM/yyyy" />">
						</div>
					</div>
					<div class="col-md-2 mb-3" id="dataFimField" style="display: none;">
						<div class="form-group">
							<label for="nome">Data Final</label> <input type="text"
								class="form-control" id="dataFim" name="dataFim"
								placeholder="Data Final"
								value="<fmt:formatDate value="${projeto.dataFim}" pattern="dd/MM/yyyy" />">
						</div>
					</div>
					<div class="col-md-2 mb-3">
						<div class="form-group">
							<label for="nome">Orçamento</label> <input type="text"
								class="form-control" id="orcamentoProjeto" name="orcamento"
								required="required" placeholder="Orçamento"
								value="${orcamentoFormatado}">
						</div>
					</div>
					<div class="form-group">
						<label for="statusProjeto">Status</label> <select
							class="form-control" id="statusProjeto" name="status">
							<option value="Planejado"
								${projeto.status == 'Planejado' ? 'selected' : ''}>Planejado</option>
							<option value="Em análise"
								${projeto.status == 'Em análise' ? 'selected' : ''}>Em
								análise</option>
							<option value="Análise realizada"
								${projeto.status == 'Análise realizada' ? 'selected' : ''}>Análise
								realizada</option>
							<option value="Análise aprovada"
								${projeto.status == 'Análise aprovada' ? 'selected' : ''}>Análise
								aprovada</option>
							<option value="Iniciado"
								${projeto.status == 'Iniciado' ? 'selected' : ''}>Iniciado</option>
							<option value="Em andamento"
								${projeto.status == 'Em andamento' ? 'selected' : ''}>Em
								andamento</option>
							<option value="Encerrado"
								${projeto.status == 'Encerrado' ? 'selected' : ''}>Encerrado</option>
							<option value="Cancelado"
								${projeto.status == 'Cancelado' ? 'selected' : ''}>Cancelado</option>
						</select>
					</div>
				</div>
				<div class="d-flex justify-content-center">
					<input type="submit" value="Salvar" class="btn btn-success m-2">
				</div>
			</form:form>

		</div>
	</div>
</div>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery.mask/1.14.16/jquery.mask.min.js"></script>
<script>
	var statusProjeto = document.getElementById("statusProjeto");
	var dataFimField = document.getElementById("dataFimField");

	statusProjeto.addEventListener("change", function() {
		if (statusProjeto.value === "Encerrado") {
			dataFimField.style.display = "block";
		} else {
			dataFimField.style.display = "none";
		}
	});
	$(document).ready(function() {
		$('#orcamentoProjeto').mask('000.000.000.000.000,00', {
			reverse : true
		});
	});

	document.querySelector('form').addEventListener('submit', function() {
		var orcamentoInput = document.getElementById('orcamentoProjeto');
		orcamentoInput.value = orcamentoInput.value.replace('.', '');
		orcamentoInput.value = orcamentoInput.value.replace(',', '.');
	});
</script>
<jsp:include page="rodape.jsp" />