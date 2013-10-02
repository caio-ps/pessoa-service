package pessoa.service.restinterface;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
import pessoa.service.model.ListaPessoaJSON;
import pessoa.service.model.PessoaJSON;

@Controller
public class PessoaControler {

	@Autowired
	PessoaServico pessoaServico;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value="/pessoa")
    public HttpEntity<ListaPessoaJSON> getPessoas() throws OperacaoNaoPermitidaException {

    	final ListaPessoaJSON pessoasJSON = buscaTodos();
    	adicionaOperacoesPermitidas(pessoasJSON);
	    	
        return new ResponseEntity<ListaPessoaJSON>(pessoasJSON, HttpStatus.OK);
        
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value="/pessoa/email")
    public HttpEntity<PessoaJSON> getPessoaPorEmail(
    		@RequestParam(value = "email", required = true) String email) throws OperacaoNaoPermitidaException {

		final PessoaJSON pessoaJSON = buscaPorEmail(email);
		adicionaOperacoesPermitidas(pessoaJSON);
			
		return new ResponseEntity<PessoaJSON>(pessoaJSON, HttpStatus.OK);
			
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value="/pessoa")
    public HttpEntity<PessoaJSON> criaPessoa(@RequestBody Pessoa pessoa)
    		throws OperacaoNaoPermitidaException, CamposInvalidosException {

    	pessoaServico.cria(pessoa);
    	return new ResponseEntity<PessoaJSON>(new PessoaJSON(pessoa), HttpStatus.OK);
    	
    }
    
    private ListaPessoaJSON buscaTodos() throws OperacaoNaoPermitidaException {
    	List<Pessoa> pessoas = pessoaServico.buscaTodos();
    	return converteListaParaJson(pessoas);
    }
    
    private ListaPessoaJSON converteListaParaJson(List<Pessoa> pessoas) {
    	final ListaPessoaJSON pessoasJSON = new ListaPessoaJSON();

    	for(Pessoa pessoa : pessoas) {
    		pessoasJSON.adicionaPessoaJSON(new PessoaJSON(pessoa));
    	}
    	
    	return pessoasJSON;
    }

    private PessoaJSON buscaPorEmail(String email) throws OperacaoNaoPermitidaException {
    	Pessoa pessoa = pessoaServico.buscaPorEmail(email);
    	return new PessoaJSON(pessoa);
    }

    private void adicionaOperacoesPermitidas(ListaPessoaJSON listaPessoasJSON) {
    	try {
    		
			for (PessoaJSON pessoaJSON : listaPessoasJSON.getLista()) {
				adicionaOperacoesPermitidas(pessoaJSON);
			}
			
			listaPessoasJSON.add(linkTo(methodOn(PessoaControler.class).getPessoas()).withRel("GET"));
			listaPessoasJSON.add(linkTo(methodOn(PessoaControler.class).criaPessoa(null)).withRel("POST"));
			
		} catch (Exception e) {
		} 
    }
    
    private void adicionaOperacoesPermitidas(PessoaJSON pessoaJSON) {
    	try {
			pessoaJSON.add(linkTo(methodOn(PessoaControler.class).getPessoaPorEmail(null)).withRel("GET"));
		} catch (Exception e) {	
		}
    }
    
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    public String handleException(Exception ex) {
        return ex.toString();
    }
    
}
