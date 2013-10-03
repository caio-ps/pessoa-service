package pessoa.service.exception;

public enum Mensagens {

	PESSOA_JA_EXISTE("Já existe uma pessoa cadastrada com este e-mail. Utilize a função de atualização."),
	CAMPO_EMAIL_OBRIGATORIO("Campo e-mail é obrigatório para realizar esta operação."),
	PELO_MENOS_UM_TENANT_OBRIGATORIO("É necessário informar ao menos um tenant."),
	REGISTRO_NAO_ENCONTRADO("Não foi encontrado nenhum registro");
	
	private String mensagem;

	private Mensagens(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getMensagem() {
		return mensagem;
	}

}
