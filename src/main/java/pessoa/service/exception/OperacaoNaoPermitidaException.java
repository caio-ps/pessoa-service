package pessoa.service.exception;

public class OperacaoNaoPermitidaException extends Exception {

	private static final long serialVersionUID = 3651727544180199810L;
	
	public static final OperacaoNaoPermitidaException PESSOA_JA_EXISTE = 
			new OperacaoNaoPermitidaException(Mensagens.PESSOA_JA_EXISTE);
	
	private String mensagem;

	public OperacaoNaoPermitidaException(Mensagens mensagem) {
		this.mensagem = mensagem.getMensagem();
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	@Override
	public String toString() {
		return getMensagem();
	}
	
}
