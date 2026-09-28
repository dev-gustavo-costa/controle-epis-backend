package com.example.demo.repository;

import java.util.List;

// Interface que gera automaticamente as consultas SQL com base no nome dos métodos.
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Epi;

public interface EpiRepository extends JpaRepository<Epi, Integer> {
	/* ----------------------------------
	   ----- @Query(value = "SELECT * FROM almoxarifado_epi WHERE unaccent(nome_epi) ILike unaccent(CONCAT('%', :nome, '%')), nativeQuery = true) -----
	   
	   @Query -> é oque permite você escrever manualmente a consulta que vai ser executada,
	   em vez de deixar o Spring Data JPA gerar ela sozinha a partir do nome do métado 
	   (que oque elr faz automaticamente quando você usa conveções  findByNomeEpi, por exemplo).
	   
	   SELECT * FROM almoxarifado_epi WHERE -> busca todas em todas as colunas de almoxarifado_epi,
	   depois do filtro do WHERE que vai decidir quais linhas voltam.
	   
	   unaccent(nome_epi) -> é ima função do PostgreSQL que remove acentuação de um texto, transforma "Óculos" em "Oculos".
	   Ela não vem habilitada por padrão, para funcinar o banco precisa ter a extensão unaccent ativada (normalmente com o comando
	   CREATE EXTENSION IF NOT EXISTS unaccent;, executada uma vez no banco).
	   Obs.: Se essa extenção não estiver ativa, essa query vai falhar em tempo de execução com um erro do tipo: 
	   "function unaccent(character varying) does not exist".
	   
	   ILIKE -> ignora maiúscula/minúscula, ou seja ILIKE já resolve sozinho o "Luva" vs "luva" vs "LUVA".
	   
	   CONCAT('%', :nome, '%') -> monta o padrão de comparação colocando o símbulo %(curinga, que segnifica
	   "qualquer sequência de caracteres, inclusive vazia") antes e depois do nome buscado. Isso faz funcionar como
	   um "contén", se voce buscar "luva", ela vai encontrar "Luva de Proteção Nível 3", "Par de Luvas", etc., porque o termo
	    aparece em algum lugar do texto, não precisa ser o nomeexato nem começar com pro ele.
	    
	    ----- :nome e o @Param -----
	    
	    O :nome -> um espaço reservado que spring vai prencher na hora de executar o query
	    
	    @Param("nome") String nome -> no metado é oque faz a ligação: diz "o valor que a pessoa passar aqui no parâmentro 
	    Java nome deve substituir o :nome na query". Isso é feiro de forma segura (com prepared statement pro trás dos panos),
	    então não tem risco de SQL injection, mesmo sendo uma query nativa escrita "na mão".
	    
	    ----- List<Epi> ----
	    
	    É o tipo de retorno do metado.
	   ---------------------------------- 
	 */
	// Busca por nome ignorando acentuação e diferenças entre maiúsculas/minúsculas, usando a função unaccent do PostgreSQL.
	@Query(value = "SELECT * FROM almoxarifado_epi WHERE unaccent(nome_epi) ILIKE unaccent(CONCAT('%', :nome, '%'))", nativeQuery = true)
	List<Epi> buscarPorNomeIgnorandoAcentoECaixa(@Param("nome") String nome);
	
	// Busca todos os epis quais seus estoques sejam menor que o estoque minimo.
	@Query("SELECT e FROM Epi e WHERE e.estoque < e.estoqueMin")
	List<Epi> buscarAbaixoDoEstoqueMinimo();
}
