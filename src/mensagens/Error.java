package mensagens;

public class Error {
	
	private String mensagem;

	public String pastaNaoEncontradaArpa() {
		return "<html>Voc� n�o tem o control instalado ou est� em outro diret�rio,<br><center> verifique ou contate o nosso suporte.<br><center>(81) 3719-3557 ou (81) 3136-0155.";
	}
	
	public String AnoEMesNaoEncontrado() {
		return "<html>Por favor, escolha o ano e o m�s corretamente";
	}
	
	public String DadosIncompletosConfiguracoes() {
		return "<html>Por favor, complete os campos corretamente";
	}
	
	public String DadosSalvosConfiguracoes() {
		return "<html>Dados Salvos!";
	}

	public String EmailErrado() {
		return "<html>Verifique se o email est� correto!";
	}
	
	public String EmailNaoConfigurado() {
		return "<html>Email n�o Configurado!";
	}

	public String getMensagem() {
		return mensagem;
	}
	
	public String DownloadCompleto() {
		return "<html>Download Completo!";
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
}
