package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ItemRecebidoDTO;
import com.example.demo.model.Epi;
import com.example.demo.model.ItemListaCompra;
import com.example.demo.model.ListaCompra;
import com.example.demo.service.GeradorPdfListaCompra;
import com.example.demo.service.ServiceListaCompra;

@RestController
@RequestMapping("/api/listas-compra")
public class ControllerListaCompra {
	
	@Autowired
	private ServiceListaCompra serviceListaCompra;
	@Autowired
	private GeradorPdfListaCompra geradorPdfListaCompra;
	
	// GET /api/listas-compta/sugestao
	@GetMapping("/sugestao")
	public ResponseEntity<List<Epi>> sugestao() {
		return ResponseEntity.ok(serviceListaCompra.sugestao());
	}
	
	// POST /api/listas-compra
	@PostMapping
	public ResponseEntity<ListaCompra> criar(@RequestBody List<ItemListaCompra> itens) {
		ListaCompra criada = serviceListaCompra.criar(itens);
		return ResponseEntity.ok(criada);
	}
	@PostMapping("/{id}/aplicar")
	public ResponseEntity<ListaCompra> aplicar(@PathVariable int id, @RequestBody List<ItemRecebidoDTO> itensRecebidos) {
		ListaCompra aplicada = serviceListaCompra.aplicar(id, itensRecebidos);
		return ResponseEntity.ok(aplicada);
	}
	
	// GET /api/listas-compra
	@GetMapping
	public ResponseEntity<List<ListaCompra>> listarTodos() {
		return ResponseEntity.ok(serviceListaCompra.listarTodos());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ListaCompra> buscarPorId(@PathVariable int id) {
		return ResponseEntity.ok(serviceListaCompra.buscarPorId(id));
	}
	@GetMapping("/{id}/pdf")
	public ResponseEntity<byte[]> exportarPdf(@PathVariable int id) {
		ListaCompra lista = serviceListaCompra.buscarPorId(id);
		byte[] pdf = geradorPdfListaCompra.gerar(lista);
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=lista-compra-" + id + ".pdf")
				.contentType(MediaType.APPLICATION_PDF)
				.body(pdf);
	}

}
