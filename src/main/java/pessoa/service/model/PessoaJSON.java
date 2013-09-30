package pessoa.service.model;

import org.springframework.hateoas.ResourceSupport;

import pessoa.model.Pessoa;

public class PessoaJSON extends ResourceSupport {

	private Pessoa content;

	public PessoaJSON(Pessoa content) {
		this.content = content;
	}

	public Pessoa getContent() {
		return content;
	}

}
