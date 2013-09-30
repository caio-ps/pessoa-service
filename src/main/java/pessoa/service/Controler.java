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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pessoa.model.Pessoa;
import pessoa.service.model.PessoaJSON;

@Controller
public class Controler {

	@Autowired
	MongoOperations mongo;

    @ResponseBody
    @RequestMapping("/pessoa")
    public HttpEntity<PessoaJSON> greeting(
            @RequestParam(value = "name", required = false, defaultValue = "World") String name) {

    	Query searchPessoaQuery = new Query(Criteria.where("alias").is("caio.ps"));
		Pessoa savedPessoa = mongo.findOne(searchPessoaQuery, Pessoa.class);
    	
    	PessoaJSON pessoa = new PessoaJSON(savedPessoa);
        pessoa.add(linkTo(methodOn(Controler.class).greeting(name)).withSelfRel());

        return new ResponseEntity<PessoaJSON>(pessoa, HttpStatus.OK);
        
    }
    
}
