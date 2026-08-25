package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ListaCompra;
import com.example.demo.model.StatusListaCompra;

public interface ListaCompraRepository extends JpaRepository<ListaCompra, Integer> {
	
	List<ListaCompra> findByStatusAndDataExpiracaoBefore(StatusListaCompra status, LocalDate data);

}
