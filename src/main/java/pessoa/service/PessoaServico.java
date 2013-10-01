package pessoa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import pessoa.model.Pessoa;
import pessoa.service.exception.OperacaoNaoPermitidaException;

@Component
public class PessoaServico {

	@Autowired
	MongoOperations mongo;
	
	public void cria(Pessoa pessoa) throws OperacaoNaoPermitidaException {
		this.verificaPermissao();
		mongo.save(pessoa);
	}
	
	public List<Pessoa> buscaTodos() throws OperacaoNaoPermitidaException {
		List<Pessoa> pessoas = mongo.findAll(Pessoa.class);
		return pessoas;
	}
	
	public Pessoa buscaPorEmail(String email) throws OperacaoNaoPermitidaException {
		Query pessoaQuery = new Query(Criteria.where("email").is(email));
		Pessoa pessoa = mongo.findOne(pessoaQuery, Pessoa.class);
		return pessoa;
	}
	
	private void verificaPermissao() throws OperacaoNaoPermitidaException {
		
	}
	
}
