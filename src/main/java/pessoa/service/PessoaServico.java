package pessoa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import pessoa.model.Pessoa;
import pessoa.service.exception.CamposInvalidosException;
import pessoa.service.exception.Mensagens;
import pessoa.service.exception.OperacaoNaoPermitidaException;

@Component
public class PessoaServico {

	@Autowired
	MongoOperations mongo;
	
	public void cria(Pessoa pessoa) throws CamposInvalidosException, OperacaoNaoPermitidaException {
		verificaSeTemPermissao();
		validaCriacao(pessoa);
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

	private void validaCriacao(Pessoa pessoa) throws CamposInvalidosException, OperacaoNaoPermitidaException {
		validaCamposObrigatoriosParaCriacao(pessoa);
		if (pessoaJaExiste(pessoa)) {
			throw OperacaoNaoPermitidaException.PESSOA_JA_EXISTE;
		}
	}
	
	private void validaCamposObrigatoriosParaCriacao(Pessoa pessoa) throws CamposInvalidosException {
		
		final CamposInvalidosException camposInvalidos = 
				new CamposInvalidosException();
		
		if (pessoa.getEmail() == null || pessoa.getEmail().equals("")) {
			camposInvalidos.addCampoInvalido(Mensagens.CAMPO_EMAIL_OBRIGATORIO);
		}
		
		if (pessoa.getTenants() == null || pessoa.getTenants().isEmpty()) {
			camposInvalidos.addCampoInvalido(Mensagens.PELO_MENOS_UM_TENANT_OBRIGATORIO);
		}
		
		if (camposInvalidos.getCamposInvalidos().size() > 0) {
			throw camposInvalidos;
		}
		
	}

	public boolean pessoaJaExiste(Pessoa novaPessoa) throws OperacaoNaoPermitidaException {
		Pessoa pessoaJaExistente = buscaPorEmail(novaPessoa.getEmail());
		return pessoaJaExistente != null;
	}
	
	private void verificaSeTemPermissao() throws OperacaoNaoPermitidaException {
		
	}
	
}
