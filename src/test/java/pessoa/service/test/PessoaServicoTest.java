package pessoa.service.test;

import org.junit.Assert;
import org.junit.Test;

import br.com.caiosousa.exception.CamposInvalidosException;
import br.com.caiosousa.pessoa.model.Pessoa;
import br.com.caiosousa.pessoa.service.PessoaServico;

public class PessoaServicoTest {

	private PessoaServico pessoaServico = new PessoaServico();
	
	@Test
	public void testValidaCamposObrigatoriosParaCriacao() {
		
		//Pessoa OK
		Pessoa pessoa = new Pessoa();
		pessoa.setEmail("teste@test.com");
		pessoa.setTenant(1L);
		
		try {
			pessoaServico.validaCamposObrigatoriosParaCriacao(pessoa);
		} catch (CamposInvalidosException e) {
			Assert.fail();
		}
		
		//Pessoa com email vazio
		pessoa = new Pessoa();
		pessoa.setEmail("");
		pessoa.setTenant(1L);
		
		try {
			pessoaServico.validaCamposObrigatoriosParaCriacao(pessoa);
			Assert.fail();
		} catch (CamposInvalidosException e) {
			Assert.assertEquals(1, e.getCamposInvalidos().size());
		}
		
		//Pessoa sem nenhum tenant
		pessoa = new Pessoa();
		pessoa.setEmail("teste@test.com");
		
		try {
			pessoaServico.validaCamposObrigatoriosParaCriacao(pessoa);
			Assert.fail();
		} catch (CamposInvalidosException e) {
			Assert.assertEquals(1, e.getCamposInvalidos().size());
		}
		
		//Pessoa sem nenhum tenant e sem email
		pessoa = new Pessoa();
		
		try {
			pessoaServico.validaCamposObrigatoriosParaCriacao(pessoa);
			Assert.fail();
		} catch (CamposInvalidosException e) {
			Assert.assertEquals(2, e.getCamposInvalidos().size());
		}
		
	}
	
}
