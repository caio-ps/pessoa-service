package br.com.caiosousa.pessoa.service;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import br.com.caiosousa.exception.CamposInvalidosException;
import br.com.caiosousa.exception.Mensagens;
import br.com.caiosousa.exception.OperacaoNaoPermitidaException;
import br.com.caiosousa.exception.RegistroNaoEncontradoException;
import br.com.caiosousa.pessoa.model.Pessoa;

@Component
public class PessoaServico {

	@Autowired
	MongoOperations mongo;
	
	public void cria(Pessoa pessoa) throws CamposInvalidosException, OperacaoNaoPermitidaException {
		verificaSeTemPermissao();
		validaCriacao(pessoa);
		mongo.save(pessoa);
	}
	
	public Pessoa atualiza(Pessoa pessoa)
			throws CamposInvalidosException, OperacaoNaoPermitidaException, RegistroNaoEncontradoException {
		
		verificaSeTemPermissao();
		final Pessoa pessoaJaExistente = validaAtualizacao(pessoa);
		final Pessoa pessoaAtualizada = preencheCamposParaAtualizar(pessoaJaExistente, pessoa);
		mongo.save(pessoaAtualizada);
		return buscaPorEmail(pessoaAtualizada.getEmail());
		
	}
	
	public List<Pessoa> buscaTodos() throws RegistroNaoEncontradoException {
		
		List<Pessoa> pessoas = mongo.findAll(Pessoa.class);
		
		if (pessoas == null || pessoas.isEmpty()) {
			throw RegistroNaoEncontradoException.DEFAULT;
		}
		
		return pessoas;
		
	}
	
	public Pessoa buscaPorEmail(String email) throws RegistroNaoEncontradoException {
		
		Query pessoaQuery = new Query(Criteria.where("email").is(email));
		Pessoa pessoa = mongo.findOne(pessoaQuery, Pessoa.class);
		
		if (pessoa == null) {
			throw RegistroNaoEncontradoException.DEFAULT;
		}
		
		return pessoa;
		
	}
	
	public void exclui(String email) throws RegistroNaoEncontradoException, OperacaoNaoPermitidaException{
		
		verificaSeTemPermissao();
		Pessoa pessoa = buscaPorEmail(email);
		validaExclusao(pessoa);
		// Chama o serviço de controle de tenants e desativa a pessoa no tenant corrente.
		
	}

	public void validaCamposObrigatoriosParaCriacao(Pessoa pessoa) throws CamposInvalidosException {
		
		final CamposInvalidosException camposInvalidos = 
				new CamposInvalidosException();
		
		if (pessoa.getEmail() == null || pessoa.getEmail().equals("")) {
			camposInvalidos.addCampoInvalido(Mensagens.CAMPO_EMAIL_OBRIGATORIO);
		}
		
		if (pessoa.getTenant() == null) {
			camposInvalidos.addCampoInvalido(Mensagens.PELO_MENOS_UM_TENANT_OBRIGATORIO);
		}
		
		if (camposInvalidos.getCamposInvalidos().size() > 0) {
			throw camposInvalidos;
		}
		
	}

	public boolean pessoaJaExiste(Pessoa novaPessoa) {
		try {
			buscaPorEmail(novaPessoa.getEmail());
			return Boolean.TRUE;
		} catch (RegistroNaoEncontradoException e) {
			return Boolean.FALSE;
		}
		
	}
	
	private void validaCriacao(Pessoa pessoa) throws CamposInvalidosException, OperacaoNaoPermitidaException {
		validaCamposObrigatoriosParaCriacao(pessoa);
		if (pessoaJaExiste(pessoa)) {
			throw OperacaoNaoPermitidaException.PESSOA_JA_EXISTE;
		}
	}
	
	private Pessoa validaAtualizacao(Pessoa pessoa) throws CamposInvalidosException, RegistroNaoEncontradoException {
		
		final CamposInvalidosException camposInvalidos = 
				new CamposInvalidosException();
		
		if (pessoa.getEmail() == null || pessoa.getEmail().equals("")) {
			camposInvalidos.addCampoInvalido(Mensagens.CAMPO_EMAIL_OBRIGATORIO);
		}
		
		return buscaPorEmail(pessoa.getEmail());
		
	}
	
	private void validaExclusao(Pessoa pessoa) throws OperacaoNaoPermitidaException {

		/*
		 * Valida se pode ser desativada do tenant em questão.
		 */

	}
	
	private Pessoa preencheCamposParaAtualizar(Pessoa pessoaJaExistente, Pessoa novosCampos) {
		
		/*TODO: Refazer setando valores na mão
		 * E-mail e tenant não podem ser alterados, pois é a chave primária
		 */
		for (Field campo : Pessoa.class.getDeclaredFields()) {
			
			try {

				if (!campo.getName().equalsIgnoreCase("email")) {
					campo.setAccessible(Boolean.TRUE);
					campo.set(pessoaJaExistente, campo.get(novosCampos));
				}
				
			} catch (Exception e) {
			}
			
		}
		
		return pessoaJaExistente;
		
	}
	
	private void verificaSeTemPermissao() throws OperacaoNaoPermitidaException {
		
	}
	
}
