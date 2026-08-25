package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository; // Repositerio que vai gera os SQL sozinho
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Epi;

public interface EpiRepository extends JpaRepository<Epi, Integer> {
	// Depois entender e comentar sobre a verção modificada do pesquisar pro nome ignorando acento e case
	@Query(value = "SELECT * FROM almoxarifado_epi WHERE unaccent(nome_epi) ILIKE unaccent(CONCAT('%', :nome, '%'))", nativeQuery = true)
	List<Epi> buscarPorNomeIgnorandoAcentoECaixa(@Param("nome") String nome); // Vai retornar o a linha iteira do epi buscado.
	@Query("SELECT e FROM Epi e WHERE e.estoque < e.estoqueMin")
	List<Epi> buscarAbaixoDoEstoqueMinimo();
}
