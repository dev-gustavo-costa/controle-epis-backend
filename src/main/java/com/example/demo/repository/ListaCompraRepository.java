package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ListaCompra;
import com.example.demo.model.StatusListaCompra;

public interface ListaCompraRepository extends JpaRepository<ListaCompra, Integer> {
	
	/* -----------------------------------
	   ----- findByStatusAndDataExpiracaoBefore -----
	   
	   findBy -> é prefixo que diz "isso é uma busca, gere uma query para mim" A partir daqui,
	   o spring vai interpretar o resto do nome como as codições do WHERE.
	   
	   Status -> faz referência ao campo status da entidade ListaCompra. Sozinho, sem nenhuma palavra-chave
	   especial depois depois, significa uma comparação de igualdade, ou seja status = :status.
	   
	   And -> é o conector logico, une duas codições. equivalente ao AND no SQL.
	   
	   DataExpiracaoBefore -> faz referencia a um campo dataExpiracao da entidade, e a palavra-chave
	   Before no final diz "menoe que" ou seja, dataExpiracao < :data .
	   -----------------------------------
	 */
	List<ListaCompra> findByStatusAndDataExpiracaoBefore(StatusListaCompra status, LocalDate data);

}
