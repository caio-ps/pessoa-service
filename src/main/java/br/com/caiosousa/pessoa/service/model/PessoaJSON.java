package br.com.caiosousa.pessoa.service.model;

import org.springframework.hateoas.ResourceSupport;

import br.com.caiosousa.pessoa.model.Pessoa;


public class PessoaJSON extends ResourceSupport {

	private Pessoa content;

	public PessoaJSON(Pessoa content) {
		content.setSenha(null);
		this.content = content;
	}

	public Pessoa getContent() {
		return content;
	}

}
