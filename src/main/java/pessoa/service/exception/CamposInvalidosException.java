package pessoa.service.exception;

import java.util.ArrayList;
import java.util.List;

public class CamposInvalidosException extends Exception {

	private static final long serialVersionUID = 3651727544180199810L;

	private List<String> camposInvalidos;

	public List<String> getCamposInvalidos() {
		if (camposInvalidos == null) {
			camposInvalidos = new ArrayList<>();
		}
		return camposInvalidos;
	}

	public void addCampoInvalido(Mensagens campoEMensagem) {
		getCamposInvalidos().add(campoEMensagem.getMensagem());
	}

	@Override
	public String toString() {
		StringBuilder mensagensFormatadas = new StringBuilder();
		
		for (String mensagemCampoInvalido : getCamposInvalidos()) {
			mensagensFormatadas.append(mensagemCampoInvalido);
			mensagensFormatadas.append("\n");
		}
		
		return mensagensFormatadas.toString();
	}

}
