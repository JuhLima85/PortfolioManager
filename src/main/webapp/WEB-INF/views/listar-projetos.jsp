<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./base.jsp"%>

<%
SimpleDateFormat dataFormatada  = new SimpleDateFormat("dd/MM/yyyy");
 %>
 
 <div class="d-flex justify-content-center"> <c:if
	test="${not empty mensagem}">
	<div class="alert alert-success" role="alert">${mensagem}</div>
</c:if> <c:if test="${not empty mensagem_error}">
	<div class="alert alert-danger" role="alert">${mensagem_error}</div>
</c:if> </div>
<div class="container-fluid mt-3">
	<div class="card card-custom-width-two">
		<div class="card-body">
			<h1 class="text-center">Projetos Cadastrados</h1>
			<div class="table-responsive">
				<table class="table table-bordered table-striped">
					<thead>
						<tr>
							<th class="align-middle text-center">Nome</th>
							<th class="align-middle text-center">Descrição</th>
							<th class="align-middle text-center">Gerente Responsável</th>
							<th class="align-middle text-center">Inicio</th>
							<th class="align-middle text-center">Previsão de Fim</th>
							<th class="align-middle text-center">Fim</th>
							<th class="align-middle text-center">Orçamento</th>
							<th class="align-middle text-center">Status</th>
							<th class="align-middle text-center">Risco</th>
							<th class="align-middle text-center">Opções</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="projeto" items="${projetos}">
							<tr>
								<td class="align-middle">${projeto.nome}</td>
								<td class="align-middle">${projeto.descricao}</td>
								<td class="align-middle">${projeto.gerenteResponsavel.nome}</td>
								<td class="align-middle text-center"><fmt:formatDate value="${projeto.dataInicio}" pattern="dd/MM/yyyy" /></td>	
								<td class="align-middle text-center"><fmt:formatDate value="${projeto.dataPrevisaoFim}" pattern="dd/MM/yyyy" /></td>								
								<td class="align-middle text-center"><c:choose>
										<c:when test="${projeto.status == 'Encerrado'}">
            								<fmt:formatDate value="${projeto.dataFim}" pattern="dd/MM/yyyy" />
        								</c:when>
										<c:otherwise>
          									  -
       									 </c:otherwise>
									</c:choose></td>
								<td class="align-middle text-center orcamentoProjeto">${projeto.orcamento}</td>
								<td class="align-middle text-center">${projeto.status}</td>
								<td class="align-middle text-center">${projeto.risco}</td>
								<td class="align-middle text-center"><form:form
										method="POST"
										action="/codedeving/projetos/remover/${projeto.id}">

										<c:choose>
											<c:when
												test="${projeto.status == 'Iniciado' || projeto.status == 'Em andamento' || projeto.status == 'Encerrado'}">
												<button type="submit" value="excluir" class="btn btn-danger"
													onclick="exibirMensagem(); event.preventDefault();">
													<i id="boot-icon" class="bi bi-trash"></i>
												</button>
											</c:when>
											<c:otherwise>
												<button type="submit" value="excluir" class="btn btn-danger"
													onclick="return confirmarExclusao('${projeto.nome}');">
													<i id="boot-icon" class="bi bi-trash"></i>
												</button>
											</c:otherwise>
										</c:choose>

										<a href="/codedeving/projetos/editar/${projeto.id}"
											class="btn btn-primary"> <i id="boot-icon"
											class="bi bi-pencil-fill"></i>
										</a>
									</form:form></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.mask/1.14.16/jquery.mask.min.js"></script>

<script>
	function exibirMensagem() {
		alert("Projetos com status 'Iniciado', 'Em andamento' ou 'Encerrado' não podem ser excluídos!");
	}

	function confirmarExclusao(nomeProjeto) {
        return confirm("Tem certeza que deseja excluir o projeto '" + nomeProjeto + "'?");
    }	
		
    $(document).ready(function() {
        $('.orcamentoProjeto').each(function() {
            var orcamento = $(this).text();
            var numeroFormatado = parseFloat(orcamento.replace(',', '.'));
            $(this).text(numeroFormatado.toLocaleString('pt-BR', { minimumFractionDigits: 2 }));
        });
    });
	
</script>
<jsp:include page="rodape.jsp" />