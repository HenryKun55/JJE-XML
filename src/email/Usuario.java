package email;

public class Usuario {
	
	private static Usuario instance = null;
	
	private Usuario() {

	}
	
	public static Usuario getInstance() {
		if(instance == null) {
			instance = new Usuario();
		}
		return instance;
	}
		
	private String remetente = "jje.xml@gmail.com";
	private String nomeEmpresa = "JJE";
	private String senha = "csilbgezuyeimjfs";
	private String destinatario = "jje.xml@gmail.com";
	private String usuarioBanco = "postgres";
	private String senhaBanco = "Teste55";
	private String mes;
	private String ano;
	private String assunto = "Enviando XML";
	private String host = gmail();
	private String hostControl;

	public String getRemetente() {
		return remetente;
	}
	public void setRemetente(String remetente) {
		this.remetente = remetente;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	public String getUsuarioBanco() { return usuarioBanco; }
	public void setUsuarioBanco(String usuarioBanco) { this.usuarioBanco = usuarioBanco; }
	public String getSenhaBanco() { return senhaBanco; }
	public void setSenhaBanco(String senhaBanco) { this.senhaBanco = senhaBanco; }
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public String getAno() {
		return ano;
	}
	public void setAno(String ano) {
		this.ano = ano;
	}
	public String getAssunto() {
		return assunto;
	}
	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}
	public String getNomeEmpresa() {
		return nomeEmpresa;
	}
	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getHostControl() { return hostControl; }
	public void setHostControl(String hostControl) { this.hostControl = hostControl; }

	public String gmail() {
		return "smtp.gmail.com";
	}
	
	public String hotmail() {
		return "smtp.live.com";
	}
	
	public String yahoo() {
		return "smtp.mail.yahoo.com";
	}

}
