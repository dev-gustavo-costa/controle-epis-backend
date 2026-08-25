package com.example.demo.repository;

import java.util.List;

// Interface que gera automaticamente as consultas SQL com base no nome dos métodos.
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Epi;

public interface EpiRepository extends JpaRepository<Epi, Integer> {
	// Busca por nome ignorando acentuação e diferenças entre maiúsculas/minúsculas, usando a função unaccent do PostgreSQL.
	@Query(value = "SELECT * FROM almoxarifado_epi WHERE unaccent(nome_epi) ILIKE unaccent(CONCAT('%', :nome, '%'))", nativeQuery = true)
	// Retorna a lista completa de EPIs que correspondem ao nome buscado.
	List<Epi> buscarPorNomeIgnorandoAcentoECaixa(@Param("nome") String nome);
	@Query("SELECT e FROM Epi e WHERE e.estoque < e.estoqueMin")
	List<Epi> buscarAbaixoDoEstoqueMinimo();
}
