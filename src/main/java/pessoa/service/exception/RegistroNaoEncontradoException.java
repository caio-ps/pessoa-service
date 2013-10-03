package pessoa.service.exception;

public class RegistroNaoEncontradoException extends Exception {

	private static final long serialVersionUID = 3651727544180199810L;
	
	public static final RegistroNaoEncontradoException DEFAULT = new RegistroNaoEncontradoException();
	
	private String mensagem;

	public RegistroNaoEncontradoException() {
		this.mensagem = Mensagens.REGISTRO_NAO_ENCONTRADO.getMensagem();
	}

	public String getMensagem() {
		return mensagem;
	}

	@Override
	public String toString() {
		return getMensagem();
	}
	
}
