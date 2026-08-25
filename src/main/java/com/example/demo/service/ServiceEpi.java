package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.EpiNaoEncontradoException;
import com.example.demo.model.Epi;
import com.example.demo.repository.EpiRepository;

import java.util.List;
import java.util.Map;

@Service
public class ServiceEpi {
	
	@Autowired
	private EpiRepository epiRepository;
	
	// Vai retornar todos os dados da tabela Epi no banco de dados.
	public List<Epi> listarTodos() {
		return epiRepository.findAll();
	}
	// Vai salvar os dados no banco ao ser chamado
	public Epi salvar(Epi epi) {
		return epiRepository.save(epi);
	}
	//Buscar por nome
	// É uma ponte que conecta ao repository, e permite adição de validações de dados.
	public List<Epi> buscarPorNome(String nomeEpi) {
		return epiRepository.buscarPorNomeIgnorandoAcentoECaixa(nomeEpi);
	}
	
	//Atualizar dados da tabela
	/* -------------------------------------
		---- Porque não direto epiRepository.save? ----
		
		Se fizessimos assim, ao salvar caso os dados que fosse fornecidos para alteração fosse somente
		nome e ca, poderia fazer com que os demais dados fossem atualizado para 0 ou null,
		dessa forma previnimos isso.
		
		---- orElseThrow ----
		
		Verifica se na tabela tem o id que foi informado para selecionar a linha desejada,
		se não existir o id na tabela, ele lança uma exceção definida por nós, com uma mensagem clara,
		em vez do erro genérico que seria confuso para quem consome a API.
		
		---- Map<String, Object> campos) ----
		
		O Spring converte o JSON recebido diretamente em um "Dicionario"(Chave = nome do campo, valor = oque veio)
		Assim se vier so um campo, o MAP vai ter so uma entrada, nenhum vestigio dos outros campos,
		nem com 0, nem como null. Eles simplesmente não existem no MAP.
		
		---- campos.forEach((Campo, valor) -> {...} ----
		
		Percorre só as chaves que vieram de verdade.
		
		---- switch com case "nomeEpi" -> ... ----
		
		Sintaxe moderna do java (switch expression)
		
		---- Porque Integer e não int ----
		
		Quando o Jackson desserializa um JSON dentro de um Map<String, Object>, números viram objetos
		integer(não int primitivo) então o cast precisa ser pro tipo "encaixotado"(integer) mesmo
		que o set da entidade espere int (o Java converte automaticamente de Integer para int nessa atribuição, chamado
		"unboxing" automático.
		
	---------------------------------------- */
	public Epi atualizarParcial(int id, Map<String, Object> campos) {
		Epi epiExistente =  epiRepository.findById(id).orElseThrow(() -> new EpiNaoEncontradoException("Epi não encontrado com o id: " + id));
		
		campos.forEach((campo, valor) -> {
			switch (campo) {
				case "nomeEpi" -> epiExistente.setNomeEpi((String) valor);
				case "ca" -> epiExistente.setCa((Integer) valor);
				case "estoque" -> epiExistente.setEstoque((Integer) valor);
				case "mediaGasta" -> epiExistente.setMediaGasta((Integer) valor);
				case "estoqueMin" -> epiExistente.setEstoqueMin((Integer) valor);
			}
		});
			
		return epiRepository.save(epiExistente);
	}
	
	// Deletar dados da tabela:
	public void deletar(int id) {
		if (!epiRepository.existsById(id)) {
			throw new EpiNaoEncontradoException("Epi não encontrado com o id: " + id);
		}
		epiRepository.deleteById(id);
	}
	
}
