package com.example.demo.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(EpiNaoEncontradoException.class)
	public ResponseEntity<Map<String, String>>tratarEpiNaoEncontrado(EpiNaoEncontradoException ex){
		Map<String, String> corpo = new HashMap<>();
		corpo.put("erro", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(corpo);
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>>tratarValidacao(MethodArgumentNotValidException ex) {
		Map<String, String> erros = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(erro ->
		erros.put(erro.getField(), erro.getDefaultMessage()));
		return ResponseEntity.badRequest().body(erros);	
	}
	@ExceptionHandler(ListaCompraNaoEncontradaException.class)
	public ResponseEntity<Map<String, String>> tratarListaNaoEncontrada(ListaCompraNaoEncontradaException ex) {
		Map<String, String> corpo = new HashMap<>();
		corpo.put("erro", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(corpo);
		
	}
	@ExceptionHandler(OperacaoInvalidaException.class)
	public ResponseEntity<Map<String, String>> tratarOperacaoInvalida(OperacaoInvalidaException ex) {
		Map<String, String> corpo = new HashMap<>();
		corpo.put("erro", ex.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(corpo);
	}

}
