package pessoa.service.restinterface;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import pessoa.model.Pessoa;
import pessoa.service.PessoaServico;
import pessoa.service.exception.CamposInvalidosException;
import pessoa.service.exception.OperacaoNaoPermitidaException;
import pessoa.service.exception.RegistroNaoEncontradoException;
import pessoa.service.model.ListaPessoaJSON;
import pessoa.service.model.PessoaJSON;

@Controller
public class PessoaController extends AbstractController<ListaPessoaJSON, PessoaJSON> {

	@Autowired
	PessoaServico pessoaServico;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value="/pessoa")
    @ResponseStatus(value = HttpStatus.OK)
    public HttpEntity<ListaPessoaJSON> getPessoas() throws OperacaoNaoPermitidaException, RegistroNaoEncontradoException {

    	final ListaPessoaJSON pessoasJSON = converteListaParaJson(pessoaServico.buscaTodos());
    	return new ResponseEntity<ListaPessoaJSON>(adicionaListaLinksPermitidos(pessoasJSON), HttpStatus.OK);
        
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value="/pessoa/email")
    @ResponseStatus(value = HttpStatus.OK)
    public HttpEntity<PessoaJSON> getPessoaPorEmail(
    		@RequestParam(value = "email", required = true) String email)
    				throws OperacaoNaoPermitidaException, RegistroNaoEncontradoException {

		final PessoaJSON pessoaJSON = new PessoaJSON(pessoaServico.buscaPorEmail(email));
		return new ResponseEntity<PessoaJSON>(adicionaLinksPermitidos(pessoaJSON), HttpStatus.OK);
			
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value="/pessoa")
    @ResponseStatus(value = HttpStatus.OK)
	public Pessoa validaLogin(Pessoa pessoa) throws OperacaoNaoPermitidaException, RegistroNaoEncontradoException {

		final Pessoa pessoaLogada = pessoaServico.validaLogin(pessoa.getEmail(), pessoa.getSenha());
		return pessoaLogada;
        
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value="/pessoa")
    @ResponseStatus(value = HttpStatus.CREATED)
    public HttpEntity<PessoaJSON> criaPessoa(@RequestBody Pessoa pessoa)
    		throws OperacaoNaoPermitidaException, CamposInvalidosException {

    	pessoaServico.cria(pessoa);
    	return new ResponseEntity<PessoaJSON>(adicionaLinksPermitidos(new PessoaJSON(pessoa)), HttpStatus.OK);
    	
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT, value="/pessoa")
    @ResponseStatus(value = HttpStatus.OK)
    public HttpEntity<PessoaJSON> atualizaPessoa(@RequestBody Pessoa pessoa)
    		throws OperacaoNaoPermitidaException, CamposInvalidosException, RegistroNaoEncontradoException {

    	Pessoa pessoaAtualizada = pessoaServico.atualiza(pessoa);
    	adicionaLinksPermitidos(new PessoaJSON(pessoaAtualizada));
    	return new ResponseEntity<PessoaJSON>(adicionaLinksPermitidos(new PessoaJSON(pessoaAtualizada)), HttpStatus.OK);
    	
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value="/pessoa")
    @ResponseStatus(value = HttpStatus.OK)
    public HttpEntity<ListaPessoaJSON> excluiPessoa(@RequestBody Pessoa pessoa)
    		throws OperacaoNaoPermitidaException, CamposInvalidosException, RegistroNaoEncontradoException {

    	pessoaServico.exclui(pessoa.getEmail());
    	return this.getPessoas();
    	
    }
    
    private ListaPessoaJSON converteListaParaJson(List<Pessoa> pessoas) {
    	final ListaPessoaJSON pessoasJSON = new ListaPessoaJSON();

    	for(Pessoa pessoa : pessoas) {
    		pessoasJSON.adicionaPessoaJSON(new PessoaJSON(pessoa));
    	}
    	
    	return pessoasJSON;
    }

	@Override
	protected ListaPessoaJSON adicionaListaLinksPermitidos(ListaPessoaJSON listaPessoasJSON) {
		try {
    		
			for (PessoaJSON pessoaJSON : listaPessoasJSON.getLista()) {
				adicionaLinksPermitidos(pessoaJSON);
			}
			
			listaPessoasJSON.add(linkTo(methodOn(PessoaController.class).getPessoas()).withRel("GET"));
			listaPessoasJSON.add(linkTo(methodOn(PessoaController.class).criaPessoa(null)).withRel("POST"));
			
		} catch (Exception e) {
		}
		
		return listaPessoasJSON;
		
	}

	@Override
	protected PessoaJSON adicionaLinksPermitidos(PessoaJSON pessoaJSON) {
		try {
			pessoaJSON.add(linkTo(methodOn(PessoaController.class).getPessoaPorEmail(null)).withRel("GET"));
			pessoaJSON.add(linkTo(methodOn(PessoaController.class).atualizaPessoa(null)).withRel("PUT"));
			pessoaJSON.add(linkTo(methodOn(PessoaController.class).excluiPessoa(null)).withRel("DELETE"));
		} catch (Exception e) {	
		}
		
		return pessoaJSON;
	}

}
