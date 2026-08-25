package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Epi;
import com.example.demo.service.ServiceEpi;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

/* -------------------------------------
 ---- @RestController ----
 É uma junçãod e duas anotações sendo o @Controller e @ResponseBody.
 
 -- Elas tem duas funções, sendo:
 
 1 - Sinalizar que essa classe vai receber requisições HTTP.
 
 2 - Tudo que os metados retornaem devem ser convertido em JSON.
 
  ---- @RequestMapping ----
  
  Define o prefixo para a URL usar para os metados dessa classe.
  
---------------------------------------- */
@RestController
@RequestMapping("/api/epis")
public class ControllerEpi {

	@Autowired
	private ServiceEpi serviceEpi;
	
	/* -------------------------------
	---- ResponseEntity ----
	
	Permite controlar o corpo da resposta e o codigo de status HTTP (220 OK, 404 NOT FOUND, etc).
	
	---------------------------------- */ 
	
	// GET /api/epis
	@GetMapping // Sinalisa que esse merado usado o verbo HTTP GET
	public ResponseEntity<List<Epi>> listarTodos() {
		return ResponseEntity.ok(serviceEpi.listarTodos());
	}
	
	/* -------------------------------
	---- @RequestParam ----
	
	Pega um valor que vem da URL como paramentro para o query(Consulta).
	
	---------------------------------- */ 
	// GET /api/epis/buscar?nome=capacete
	@GetMapping("/buscar")
	public ResponseEntity<List<Epi>> buscarPorNome(@RequestParam String nome) {
		return ResponseEntity.ok(serviceEpi.buscarPorNome(nome));
	}
	
	/* -------------------------------
	---- @RequestBody ----
	
	Pega o JSON e converte automaticamente num objeto completo,
	com os campos prenchidos.
	
	---------------------------------- */ 
	// GET /api/epis
	@PostMapping // Sinalisa que esse merado usado o verbo HTTP POST.
	public ResponseEntity<Epi> salvar(@Valid @RequestBody Epi epi) {
		Epi salvo = serviceEpi.salvar(epi);
		return ResponseEntity.ok(salvo);
	}
	
	// PUT /api/epis/5
	@PutMapping("/{id}") //Sinalisa que esse merado usado o verbo HTTP PUT.
	public ResponseEntity<Epi> atualizar(@PathVariable int id, @RequestBody Map<String, Object> campos) {
		Epi atualizado =  serviceEpi.atualizarParcial(id, campos);
		return ResponseEntity.ok(atualizado);
	}
	
	/* ---------------------------------
	 ---- ResponseEntity<Void> ---- 
	 
	 Sinaliza que esse metado não precisa devolver nenhum conteúdo no corpo da resposta, so um codigo de resposta.
	 
	 ---- .noContent().build() ----
	 
	 Retorna o codigo 204 (Padrão em DELETE bem sucedido).
	 
	 ---- @PathVariable ----
	 
	 é usado quando o valor idetifica um recurso especifico de forma obrigatoria
	------------------------------------ */
	// Delete /api/epis/5
	@DeleteMapping("/{id}") //Sinalisa que esse merado usado o verbo HTTP DELETE.
	public ResponseEntity<Void> deletar(@PathVariable int id) {
		serviceEpi.deletar(id);
		return ResponseEntity.noContent().build();
	}
	
	
}
