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
	/* -------------------------------- 
	   ----- @Autowired -----
	   
	   É um anotação que diz ao Spring: "eu preciso de uma instância de EpiRepository aqui,
	   você que se vire para me arrumar uma". Você não escreve new EpiRepositoryImpl() nem nada do tipo, o spring
	   na inicialização da aplicação , já sabe que que EpiRepository é uma iterface que estende JpaRepository, 
	   e atomaticamente cria uma implementação concreta dela, com todos aqueles metados(buscarPorNomeIgnorandoAcentoECaixa, buscarAbaixoDoEstoqueMinimo,
	   além de metados padrão de JpaRepository como save, findById, delete, etc.). Já funcionando, prontos para acessar o banco.
	   
	   ----- private EpiRepository epiRepository; -----
	   
	   É so a declação do campo em si um atributo privado da classe do serviço, do tipo epiRepository.
	   Sozinho, sem o @Autowired, esse campo ficaria null e você teria que intanciar manualmente.
	   --------------------------------
	*/
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
		dessa forma prevenimos isso.
		
		----- Map<String, Object> campos -----
		
		É um mapa onde a (String) é o nome do campo a ser alterado (por exemplo, "nomeEpi", "estoque"), 
		e o valor (Object) é o novo valor para aquel campo.
		
		 O tipo de valor precisa ser do tipo object(o tipo mais generico do java, do qual tudo herda)
		 porque os campos são tipos diferentes entre si(String, Iteger...), e um map so aceita um tipo fixo de valor,
		 usar o object é o jeito de "aceitar qualquer tipo de valor" ali.
		 
		 ----- Epi epiRepository = epiRepository.findById(id).orElseThrow(() -> new EpiNaoEncotradoException(...)) -----
		 
		 epiRepository.findById(id) -> não devolve um Epi diretamente, ele devolve um Optinal<Epi>.
		   Optinal -> É uma caixa que pode conter um epi dentro(se o id existir) ou estar vazia (se o epi não existir),
		   cirada para evitar que seja recebido null sem aviso e tome um NullPointerException sem enteder de onde veio.
		 
		 
		 .orElseThrow(...) é um  metado desse Optinal: se ele tiver Epi guardado dentro, devolve esse Epi normalmente;
		   se estiver vazi, executa oque foi passado como argumento e lança aquilo como exeção.
		   
		 ==========================================
		 Obs.: Optinal é da biblioteca padrão do Java (desde o java 8). O Spring Data JPA usa,
		 ele em metados como findById() para tratar de forma explicita o caso de "não encotrado",
		 em vez de devolver null sem aviso.
		 ==========================================
		   
		 () -> new EpiEncontradoExcpetion("Epi não encotrado com o id: " + id) => É uma função lambda
		 	() => Nenhuma paramentro foi passado para essa função.
		 	EpiNaoEncontradoException(...) => chamamos a classe de GlobalExeceptionHandler para retornar a mesagem,
		 	quando não existir o epi, dentro do () fica a mesagem que sera retornada.
		 	
		 
		 ----- campos.forEach((campo, valor) -> {...} -----
		 
		campos.forEach((...) ) => Aqui usamos o forEach, que percorre cada par chave-valor do mapa campos, e para cada par,
		 	executa a lambda que foi passada como argumento. Então pega uma entrada por vez nessa iteração,
		 	e o switch olha pro campo dela e se bater com algum case, executa a ação com o valor dequela mesma entrada.
			
		((campo, valor) => são os parametros passado para essa Lambada semdo:
			campo -> vai receber o campo que vamos que quremos atualizar por exemplo nomeEpi (sendo uma String).
			valor -> vai receber o valor a ser alterado.
			
		
		Aqui agora as linhas da parte que esta dentro das chaves dessa lambda:
		
		switch (campo) {...} -> vamos avaliar o campo em cadas um dos casos.
		
		case "nomeEpi" -> epiExistente.setNomeEpi((String) valor); => aqui se o campo for nomeEpi executa,
			a ação definida depois de ->.
		
		epiExistente.setNomeEpi((String) valor -> Aqui nos vamos atualizar o nome do Epi, em epiExistente, 
			que é o proprio objeto Epi já buscado do banco de dados(na linha anterior, com o findById),
			com todos os seus dados originais. Estamos editando esse objeto em mémoria, campo por campo,
			e so no final do metado ele é salvo no banco com save().
			
			(String) valor -> como valor é uma variavel do tipo object nos colocamos entre paretes antes de valor,
			o tipo de variavel que ela sera convertida para que possa ser atribuida ao campo especifico de  
			epiExistente que estamos alterando (nesse case, o nomeEpi que é do tipo String).
				(Caso seja um case do tipo numerico, o cast deve ser para Integer).
			
		=========================================
		Obs.: Se por algum motivo, vier um tipo errado (por exemplo, alguém mandar uma String no lugar de um número pro campo "estoque"), 
			esse cast vai lançar um erro em tempo de execução (ClassCastException).
		
		Vale notar também: se campo for algum nome que não bate com nenhum dos case listados (por exemplo, 
			se alguém tentar mandar "id" ou um nome de campo digitado errado), nada acontece pra aquela entrada — não existe um default no switch, 
			então campos desconhecidos são simplesmente ignorados silenciosamente, sem erro nem aviso.
			
		Obs para desenvolvimento: verificar se possui algum tratamente já implementando para corrigir esse bug apos finalizar as anotaçoes do codigo.
		=========================================
		
		----- return epiRepository.save(epiExistente); -----
		
		Por fim salva o objeto epiExistente ja modificado
		 
		  ====== Explicações extras: =====
		
		
		---- Map<String, Object> campos) ----
		
		O Spring converte o JSON recebido diretamente em um "Dicionario"(Chave = nome do campo, valor = oque veio)
		Assim se vier so um campo, o MAP vai ter so uma entrada, nenhum vestigio dos outros campos,
		nem com 0, nem como null. Eles simplesmente não existem no MAP.
		
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
	/* ----------------------------
	   ----- void -----
	   
	   Significa que o metado não devolve nenhum valor para quem o chamou.
	   
	   ----- existsById(id) -----
	   
	   Verifica se existe um registro com aquele id no banco, é um metado do Spring data JPA que vem de brinde
	   ao exteder JpaRepository.
	   
	   Assim juntado !epiRepository.existsById(id) -> se esse id não existir no banco de dados,
	   vai executar essa cofição do IF.
	   
	    ----- throw -----
	    
	    Quando throw é executado, ele interrompe imediatamente a ececução normal do métado, nenhuma linha depois é executada.
	    Ao iterromper a execução um erro é lançando que é indetificado por GlobalExcepitionHandler(atraves do 
	    @ExcepitionHandler(EpiNaoEncotradoException.class)) e monta a resposta com a mensagem de erro(""Epi não encontrado com o id: " + id).
	    
	    ----- new EpiNaoEncotradoException -----
	    
	    É uma classe (uma exeção personalisada criada para esse projeto, que estende RuntimeException).
	    o "new" aqui instancia um objeto dessa exeção, passando a mensagem de erro para o construtor dela.
	   ----------------------------
	 */
	// Deletar dados da tabela:
	public void deletar(int id) {
		if (!epiRepository.existsById(id)) {
			throw new EpiNaoEncontradoException("Epi não encontrado com o id: " + id);
		}
		epiRepository.deleteById(id);
	}
	
}
