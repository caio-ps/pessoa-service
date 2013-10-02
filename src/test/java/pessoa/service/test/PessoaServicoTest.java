package pessoa.service.test;

import org.junit.Assert;
import org.junit.Test;

import pessoa.model.Pessoa;
import pessoa.service.PessoaServico;
import pessoa.service.exception.CamposInvalidosException;

public class PessoaServicoTest {

	private PessoaServico pessoaServico = new PessoaServico();
	
	@Test
	public void testValidaCamposObrigatoriosParaCriacao() {
		
		//Pessoa OK
		Pessoa pessoa = new Pessoa();
		pessoa.setEmail("teste@test.com");
		pessoa.addTenant(1L);
		
		try {
			pessoaServico.validaCamposObrigatoriosParaCriacao(pessoa);
		} catch (CamposInvalidosException e) {
			Assert.fail();
		}
		
		//Pessoa com email vazio
		pessoa = new Pessoa();
		pessoa.setEmail("");
		pessoa.addTenant(1L);
		
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
