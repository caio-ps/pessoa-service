package pessoa.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pessoa.model.Pessoa;
import pessoa.service.model.PessoaJSON;

@Controller
public class Controler {

	@Autowired
	MongoOperations mongo;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value="/pessoa")
    public HttpEntity<PessoaJSON> getPessoa(
            @RequestParam(value = "nome", required = false, defaultValue = "Caio") String nome) {

    	Query pessoaQuery = new Query(Criteria.where("nome").is(nome));
		Pessoa pessoa = mongo.findOne(pessoaQuery, Pessoa.class);
    	
    	PessoaJSON pessoaJSON = new PessoaJSON(pessoa);
    	pessoaJSON.add(linkTo(methodOn(Controler.class).getPessoa(nome)).withSelfRel());
    	pessoaJSON.add(linkTo(methodOn(Controler.class).addPessoa(new Pessoa())).withRel("POST"));

        return new ResponseEntity<PessoaJSON>(pessoaJSON, HttpStatus.OK);
        
    }
    
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value="/pessoa")
    public HttpEntity<PessoaJSON> addPessoa(@RequestBody Pessoa pessoa) {

    	mongo.save(pessoa);
    	
    	Query pessoaQuery = new Query(Criteria.where("nome").is(pessoa.getNome()));
		Pessoa pessoaSalva = mongo.findOne(pessoaQuery, Pessoa.class);
    	
    	PessoaJSON pessoaJSON = new PessoaJSON(pessoa);
    	pessoaJSON.add(linkTo(methodOn(Controler.class).getPessoa(pessoaSalva.getNome())).withRel("GET"));
    	pessoaJSON.add(linkTo(methodOn(Controler.class).addPessoa(pessoaSalva)).withSelfRel());

        return new ResponseEntity<PessoaJSON>(pessoaJSON, HttpStatus.OK);
        
    }
    
}
