package br.com.portfolioManager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.portfolioManager.Entity.Projeto;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

}
