package pessoa.service.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

public class ListaPessoaJSON extends ResourceSupport {

	private List<PessoaJSON> lista;

	public void adicionaPessoaJSON(PessoaJSON pessoaJSON) {
		this.getLista().add(pessoaJSON);
	}

	public List<PessoaJSON> getLista() {
		if (lista == null) {
			lista = new ArrayList<>();
		}
		return lista;
	}

}
