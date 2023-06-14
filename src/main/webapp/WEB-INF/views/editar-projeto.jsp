<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./base.jsp"%>

<%
SimpleDateFormat dataFormatada  = new SimpleDateFormat("dd/MM/yyyy");
 %>

<div class="container mt-3">
	<div class="card card-custom-width">
		<div class="card-body">
			<h1 class="text-center">Editar Projeto</h1>
			<form method="POST" action="/codedeving/projetos/atualizar"
				modelAttribute="gerenteRequest"">
				<div class="form-group d-none">
					<label for="id">Id</label> <input type="text" value="${projeto.id}"
						class="form-control" id="nomeProjeto" name="id"
						readonly="readonly">
				</div>
				<div class="form-group">
					<label for="nome">Nome</label> <input type="text"
						value="${projeto.nome}" class="form-control" id="nomeProjeto"
						name="nome">
				</div>
				<div class="form-group">
					<label for="nome">Descrição</label> <input type="text"
						value="${projeto.descricao}" class="form-control"
						id="descricaoProjeto" name="descricao">
				</div>
				
				
				<div class="form-group">
					<label for="nome">Data de Início</label> 
					<input type="text" value="<fmt:formatDate value="${projeto.dataInicio}" pattern="dd/MM/yyyy" />" class="form-control" id="dataInicioProjeto" name="dataInicio">
				</div>
				
				<div class="form-group" id="dataFimField" style="display: none;">
					<label for="nome">Data do Término</label> 
					<input type="text" value="<fmt:formatDate value="${projeto.dataFim}" pattern="dd/MM/yyyy" />" class="form-control" id="dataFimProjeto" name="dataFim">
				</div>
						
				<div class="form-group">
					<label for="nome">Previsão de Término</label> 
					<input type="text" value="<fmt:formatDate value="${projeto.dataPrevisaoFim}" pattern="dd/MM/yyyy" />" class="form-control" id="dataPrevisaoFimProjeto" name="dataPrevisaoFim">
				</div>
							
				<div class="form-group">
					<label for="nome">Orçamento</label> <input type="text"
						value="${projeto.orcamento}" class="form-control"
						id="orcamentoProjeto" name="orcamento">
				</div>
				<div class="form-group">
					<label for="statusProjeto">Status</label> <select
						class="form-control" id="statusProjeto" name="selectedStatus">
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
						<option value="Planejado"
							${projeto.status == 'Planejado' ? 'selected' : ''}>Planejado</option>
						<option value="Em andamento"
							${projeto.status == 'Em andamento' ? 'selected' : ''}>Em
							andamento</option>
						<option value="Encerrado"
							${projeto.status == 'Encerrado' ? 'selected' : ''}>Encerrado</option>
						<option value="Cancelado"
							${projeto.status == 'Cancelado' ? 'selected' : ''}>Cancelado</option>
					</select>
				</div>				
				<div class="form-group">
					<label for="opcao">Gerente Responsável</label> 
					<select name="gerentes" id="gerentes" class="form-control">
					<c:forEach items="${pessoas}" var="proximo">
						<c:if test="${proximo.funcionario}">
							<option value="${proximo.id}" selected>${proximo.nome}</option>
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

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.mask/1.14.16/jquery.mask.min.js"></script>

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

	statusProjeto.addEventListener("change", handleStatusChange);
	window.addEventListener("load", handleStatusChange);
	
	
	   $(document).ready(function() {
	    $('#orcamentoProjeto').mask('000.000.000.000.000,00', { reverse: true });
	  }); 
	
	  document.querySelector('form').addEventListener('submit', function() {
	        var orcamentoInput = document.getElementById('orcamentoProjeto');
	        orcamentoInput.value = orcamentoInput.value.replace('.', '');
	        orcamentoInput.value = orcamentoInput.value.replace(',', '.');
	    });
</script>


<jsp:include page="rodape.jsp" />