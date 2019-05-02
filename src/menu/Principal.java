package menu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.apache.commons.io.FileUtils;

import email.Email;
import email.Usuario;
import email.Version;
import mensagens.Error;
import util.*;

public class Principal extends JFrame {

	private Configuracoes frame ;

	private PropriedadesTela pt;
	private JPanel contentPane;
	private JComboBox comboBoxAno;
	private JComboBox comboBoxMes;
	private Font fonteLabelHeader = new Font("Tahoma", Font.PLAIN, 28);
	private Font fontePadrao = new Font("Tahoma", Font.PLAIN, 17);
	private Font fontePlaceHolder= new Font("Tahoma", Font.PLAIN, 17);
	private JLabel label;
	private JTextField textFieldEmail;
	private String[] meses = {"01Janeiro", "02Fevereiro", "03Março", "04Abril", "05Maio", "06Junho", "07Julho", "08Agosto", "09Setembro", "10Outubro", "11Novembro", "12Dezembro"};
	private String[] anos = new String[11] ;
	private String[][] mes = new String[12][12];
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	private JMenuItem mntmConfiguraes;
	private JMenuItem mntmSobre;
	private JMenuItem mntmSair;
	private JButton buttonEnviar;

	private String destino;
	private Image icone;
	private String destinoControl;

	public Image getIcone() {
		return icone;
	}

	private boolean emailConfigurado;

	public String getDestino() { return destino; }

	public String getDestinoControl() { return destinoControl; }

	private Error error = new Error();
	private Zip zip = new Zip();
	private Email email = new Email();
	private Usuario usuario;
	private Version version;
	private Connect con = new Connect();
	private Update update = Update.getInstance();

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//Iniciar o JFrame

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				Principal window = new Principal();
				window.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//ZipTemp

	public void createTemp() {

		File theDir = new File("C:\\Temp\\");

		if (!theDir.exists()) {
			boolean result = false;
			try{
				theDir.mkdir();
				result = true;
			} 
			catch(SecurityException se){ }
			if(result) { }
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//CheckDB

	public Connection checkDb() {
		String sql2 = "SELECT * FROM XMLSEND";
		Connection conn = null;
		try {
			conn = Connect.conectar();
			Statement stmt  = conn.createStatement();
			ResultSet rs    = stmt.executeQuery(sql2);
			while (rs.next()) {
				usuario.setNomeEmpresa(rs.getString("NOME_EMPRESA"));
				usuario.setRemetente(rs.getString("USUARIO"));
				usuario.setDestinatario(rs.getString("DESTINATARIO"));
				usuario.setAssunto(rs.getString("ASSUNTO"));
				usuario.setHost(rs.getString("HOST"));
				usuario.setSenha(rs.getString("SENHA"));
				usuario.setUsuarioBanco(rs.getString("USUARIO_BANCO"));
				usuario.setSenhaBanco(rs.getString("SENHA_BANCO"));
			}
		} catch (SQLException e) { 
			e.printStackTrace();
		}
		return conn;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//Insert New Data

	public Connection update(String NOME_EMPRESA, String USUARIO, String DESTINATARIO, String ASSUNTO, String HOST, String SENHA, String USUARIO_BANCO, String SENHA_BANCO) {
		String sql = "UPDATE XMLSEND SET NOME_EMPRESA = ?, USUARIO = ?, DESTINATARIO = ?, ASSUNTO = ?, HOST = ?, SENHA = ?, USUARIO_BANCO = ?, SENHA_BANCO = ?";
		try {
			Connection conn = con.conectar();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, NOME_EMPRESA);
			pstmt.setString(2, USUARIO);
			pstmt.setString(3, DESTINATARIO);
			pstmt.setString(4, ASSUNTO);
			pstmt.setString(5, HOST);
			pstmt.setString(6, SENHA);
			pstmt.setString(7, USUARIO_BANCO);
			pstmt.setString(8, SENHA_BANCO);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//Insert New Data Version

	public Connection updateVersion(String VERSION) {
		String sql = "UPDATE VERSION SET VERSION = ?";

		try {
			Connection conn = con.conectar();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, VERSION);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//Construtor 

	public Principal() {
		setIcone();
		setIconImage(icone);
		usuario = Usuario.getInstance();
		version = Version.getInstance();
		UIManager.put("Menu.font", fontePadrao);
		UIManager.put("MenuItem.font", fontePadrao);
		UIManager.put("Label.font", fontePadrao);
		UIManager.put("Button.font", fontePadrao);
		pt = new PropriedadesTela(returnIn());
		setTitle("JJE XML");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane();
		setContentPane(contentPane);
		setResizable(false);
		setSize(615, 410);
		setLocationRelativeTo(null);
		createTemp();
		checkDb();
		design();
		updateVersion(version.getVersion());
		checkForUpdates();
		readFile();
	}

	private void readFile(){
		List<String> lines = null;
		try {
			lines = FileUtils.readLines(new File(getDestinoControl()+"config.ini"), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String line: lines) {
			if(line.contains("host")){
				line = line.substring(5);
				usuario.setHostControl(line);
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//Métodos que contém o menu Princiapl

	public void design() {
		panelHeader();
		menu();
		labels();	
		boxes();
		inputs();
		buttons();
	}

	public void contentPane() {
		contentPane = new JPanel();
		contentPane.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(0, 0, 0)));
		contentPane.setLayout(null);
		contentPane.setBackground(Color.decode("#AA3939"));
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//Painel do Header

	public void panelHeader() {
		JPanel panelHeader = new JPanel();
		panelHeader.setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(0, 0, 0)));
		panelHeader.setBackground(new Color(32, 37, 80));
		contentPane.add(panelHeader);
		panelHeader.setLayout(null);
		panelHeader.setSize((int)(getWidth() * 1), (int) (getHeight() * 0.005));

	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//Painel de Menu Principal

	public void menu() {
		menuBar = new JMenuBar();
		mnNewMenu = new JMenu("Menu");
		setJMenuBar(menuBar);
		menuBar.add(mnNewMenu);

		configuracoes();
		sair();
	}

	public boolean password() {
		Box box = Box.createHorizontalBox();

		JLabel jl = new JLabel("Senha: ");
		box.add(jl);

		JPasswordField jpf = new JPasswordField(10);
		box.add(jpf);

		jpf.addAncestorListener(new AncestorListener() {

			@Override
			public void ancestorRemoved(AncestorEvent event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void ancestorMoved(AncestorEvent event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void ancestorAdded(AncestorEvent event) {
				event.getComponent().requestFocusInWindow();

			}
		});

		int button = JOptionPane.showConfirmDialog(null, box, "Insira a Senha", JOptionPane.OK_CANCEL_OPTION);

		if (button == JOptionPane.OK_OPTION) {

			char[] input = jpf.getPassword();
			String passString = new String(input);

			if(passString.equals("jje*314159")){
				//System.out.println("true");
				return true;
			}
		}
		//System.out.println("false");
		return false;
	}

	public void configuracoes() {
		mntmConfiguraes = new JMenuItem("Configura\u00E7\u00F5es");
		mnNewMenu.add(mntmConfiguraes);
		mntmConfiguraes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(password()) {
					frame = Configuracoes.getInstance();
					frame.setModal(true);
					frame.textFieldEmailRemetente.setText(usuario.getRemetente());
					frame.textFieldEmailDestinatario.setText(usuario.getDestinatario());
					frame.textFieldNomeDaEmpresa.setText(usuario.getNomeEmpresa());
					frame.textFieldAssunto.setText(usuario.getAssunto());
					frame.textFieldEmailSenhaRemetente.setText(usuario.getSenha());
					frame.setVisible(true);
					usuario.setDestinatario(frame.getEmailDestinatario());
					textFieldEmail.setFont(fontePadrao);
					textFieldEmail.setForeground(Color.BLACK);
					textFieldEmail.setText(usuario.getDestinatario());
					emailConfigurado = frame.getSalvar();
					//System.out.println(usuario.getHost());
					//System.out.println(usuario.getRemetente());
				}
			}
		});
	}

	public void sobre() {
		mntmSobre = new JMenuItem("Sobre");
		mnNewMenu.add(mntmSobre);
		mntmSobre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//frame = new Configuracoes();
				//frame.setVisible(true);
			}
		});
	}

	public void sair() {
		mntmSair = new JMenuItem("Sair");
		mnNewMenu.add(mntmSair);
		mntmSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//caixa de dialogo retorna um inteiro
				int resposta = JOptionPane.showConfirmDialog(null,"Deseja fechar?","Sair",JOptionPane.YES_NO_OPTION);

				//sim = 0, nao = 1
				if (resposta == 0)
				{
					System.exit(0);
				}
			}
		});
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//Insere os combo box

	public void boxes() {
		boxMes();
		boxAno();
	}

	public void labels() {
		labelHeader();
		labelVersion();
		labelAno();
		labelMes();
		labelInput();
		labelDesenvolvedor();
		labelEmpresa();
	}

	public void inputs() {
		inputEmail();
	}

	public void buttons() {
		buttonEnviar();
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//ComboBox do Painel Principal

	public void boxAno() {
		comboBoxAno = new JComboBox();
		verificarDiretorio();
		comboBoxAno.setBounds(245, 79, 150, 22);
		contentPane.add(comboBoxAno);
		comboBoxAno.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					comboBoxMes.removeAllItems();
					for (int i = 0; i < anos.length; i++) {
						if(anos[i] != null) {
							for (int j = 0; j < mes.length; j++) {
								if(mes[i][j] != null) {
									if(comboBoxAno.getSelectedItem().equals(anos[i])) {
										comboBoxMes.addItem(mes[i][j].toString());

									}
								}
							}
						}
					}
					usuario.setAno(comboBoxAno.getSelectedItem().toString());
					//System.out.println(usuario.getAno());
				}
			}
		});
	}

	public void boxMes() {
		comboBoxMes = new JComboBox();
		comboBoxMes.setBounds(245, 119, 150, 22);
		contentPane.add(comboBoxMes);
		comboBoxMes.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					for (int i = 0; i < meses.length; i++) {
						if(Arrays.asList(meses[i].substring(2,meses[i].length())).contains(comboBoxMes.getSelectedItem().toString())) {
							usuario.setMes(meses[i].substring(0,2).toString());
							//System.out.println(usuario.getMes().toString());
						}
					}
					//System.out.println();
				}
			}
		});
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Labels do Painel Principal

	public void labelHeader() {
		label = new JLabel("Envio de XML");
		label.setFont(fonteLabelHeader);
		label.setForeground(Color.WHITE);
		label.setBounds(220, 20, 200, 22);
		contentPane.add(label);
	}

	public void labelVersion() {
		label = new JLabel("V 2.2");
		version.setVersion(label.getText());
		//System.out.println(getVersion());
		label.setForeground(Color.WHITE);
		label.setBounds(275, 330, 200, 16);
		contentPane.add(label);
	}

	public void labelAno() {
		label = new JLabel("Ano: ");
		label.setBounds(205, 80, 56, 16);
		label.setForeground(Color.WHITE);
		contentPane.add(label);
	}

	public void labelMes() {
		label = new JLabel("M\u00EAs: ");
		label.setBounds(205, 120, 56, 16);
		label.setForeground(Color.WHITE);
		contentPane.add(label);
	}

	public void labelInput() {
		label = new JLabel("Email do Destinatario: ");
		label.setBounds(76, 160, 200, 16);
		label.setForeground(Color.WHITE);
		contentPane.add(label);
	}

	public void labelDesenvolvedor() {
		label = new JLabel("Desenvolvido por Flávio Henrique ");
		label.setBounds(360, 330, 350, 16);
		label.setForeground(Color.WHITE);
		contentPane.add(label);
	}

	public void labelEmpresa() {
		label = new JLabel("JJE Automação Comercial Ltda.");
		label.setBounds(2, 330, 350, 16);
		label.setForeground(Color.WHITE);
		contentPane.add(label);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Inputs do Painel Principal
	public void inputEmail() {	
		textFieldEmail = new JTextField();
		textFieldEmail.setBounds(245, 159, 280, 22);
		contentPane.add(textFieldEmail);
		textFieldEmail.setFont(fontePadrao);
		textFieldEmail.setColumns(10);
		textFieldEmail.setFont(fontePlaceHolder);
		//textFieldEmail.setForeground(colorPlaceHolder);
		textFieldEmail.setText(usuario.getDestinatario());
		textFieldEmail.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				if(textFieldEmail.getText().length() == 0 || 
						textFieldEmail.getText().isEmpty() ) {
					textFieldEmail.setFont(fontePlaceHolder);
					//textFieldEmail.setForeground(colorPlaceHolder);
					textFieldEmail.setText("destinatario@email.com");
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				if(textFieldEmail.getText().length() == 0 || 
						textFieldEmail.getText().isEmpty() ||
						textFieldEmail.getText().equals("destinatario@email.com")) {
					textFieldEmail.setFont(fontePadrao);
					textFieldEmail.setForeground(Color.BLACK);
					textFieldEmail.setText("");
				}
			}
		});
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Botões do Painel Principal
	public void buttonEnviar() {
		buttonEnviar = new JButton("Enviar");
		buttonEnviar.setBounds(250, 250, 97, 30);
		contentPane.add(buttonEnviar);
		buttonEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println(comboBoxMes.getSelectedIndex());
				if(comboBoxAno.getSelectedIndex() != -1 && 
						comboBoxMes.getSelectedIndex() != -1 && 
						!textFieldEmail.getText().equals("destinatario@email.com")
						){
					zip.setFolderPath(getDestino()+"\\ano_"+usuario.getAno()+"\\mes_"+usuario.getMes());
					zip.ziparPasta(zip.getFolderPath());
					usuario.setDestinatario(textFieldEmail.getText());
					update(usuario.getNomeEmpresa(), usuario.getRemetente(), usuario.getDestinatario(),
							usuario.getAssunto(),usuario.getHost(), usuario.getSenha(), usuario.getUsuarioBanco(), usuario.getSenhaBanco());

					try {
						email.send();
					} catch (MessagingException ex) {
						ex.printStackTrace();
					} catch (UnsupportedEncodingException ex) {
						ex.printStackTrace();
					}

				}else if(!emailConfigurado) {
					error.setMensagem(error.EmailNaoConfigurado());
					mensagem(error.getMensagem());

				}else {
					error.setMensagem(error.AnoEMesNaoEncontrado());
					mensagem(error.getMensagem());
				}
			}
		});
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Métodos para modificação da Tela

	public Insets returnIn() {
		Insets in = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());
		return in;
	}

	public int local(float tamPanel, float tamContainer, float por) {
		por = por / 100;
		float local = (tamPanel - tamContainer) * por;
		return (int)local;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void mensagem(String mensagem) {
		JOptionPane optionPane = new JOptionPane(new JLabel(mensagem,JLabel.CENTER));
		JDialog dialog = optionPane.createDialog("");
		dialog.setModal(true);
		dialog.setVisible(true);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void verificarDiretorio() {
		int contAno = 0;
		int contMes = 0;
		int cont = 0 ;
		comboBoxAno.addItem("Selecione o Ano");
		try {
			String[] ap = {
					"Arquivos de Programas",
					"Arquivos de Programas (x64)", 
					"Arquivos de Programas (x86)", 
					"Program Files", 
					"Program Files (x86)",
			"Program Files (x64)"};
			File diretorio = new File("c:\\");
			String[] directories = diretorio.list(new FilenameFilter() {
				@Override
				public boolean accept(File current, String name) {
					return new File(current, name).isDirectory();
				}
			});

			for(int i = 0; i < ap.length; i++) {	
				for (String s: directories) {
					if (s.equals(ap[i])) {
						diretorio = new File("c:\\"+ap[i].toString());
						//System.out.println(diretorio.toString());
						String[] directoriesInside = diretorio.list(new FilenameFilter() {
							@Override
							public boolean accept(File current, String name) {
								return new File(current, name).isDirectory();
							}
						});
						if(Arrays.toString(directoriesInside).contains("arpa")){
							//icone = diretorio.toString()+"\\JJE XML\\xml.ico";
							destinoControl = diretorio.toString()+"\\arpa\\control\\";
							destino = diretorio.toString()+"\\arpa\\control\\nfe";
							//System.out.println(destino);
							diretorio = new File(diretorio.toString()+"\\arpa\\control\\nfe");
							String[] directoriesInsideArpa = diretorio.list(new FilenameFilter() {
								@Override
								public boolean accept(File current, String name) {
									return new File(current, name).isDirectory();
								}
							});
							for (String a: directoriesInsideArpa) {
								File novo = new File(diretorio.toString()+"\\"+a);
								//System.out.println(novo);
								String[] directoriesInsideArpaAno = novo.list(new FilenameFilter() {
									@Override
									public boolean accept(File current, String name) {
										return new File(current, name).isDirectory();
									}
								});
								//System.out.println(Arrays.toString(directoriesInsideArpaAno));
								//System.out.println(a);
								if(a.equals("importadas")) {
									//System.out.println("tem");
								}else {
									comboBoxAno.addItem(a.substring(4,8));
								}
								//System.out.println(a);
								anos[contAno] = a.substring(4,8);
								String ano = a.substring(4,8);
								//System.out.println(ano);
								//System.out.println(a.substring(4,8));
								for (String m: directoriesInsideArpaAno) {
									//System.out.println(m.substring(4,6));
									//System.out.println(m.length());
									//System.out.println(m);
									for(int j = 0; j < meses.length ; j++) {
										//System.out.println(meses[j].substring(0,2).trim().toString());
										//System.out.println(m.substring(4,6).trim().toString());
										//System.out.println("----------------");
										//System.out.println("mes atual: "+m.substring(4,6).trim().toString());
										//System.out.println("mes para comparar: "+ meses[j].substring(0,2).trim().toString());
										if(m.substring(4,6).trim().toString().equals(meses[j].substring(0,2).trim().toString())) {
											mes[contAno][contMes] = meses[j].substring(2,meses[j].length());				        				
											//System.out.println(meses[j].substring(2,meses[j].length()));
											//System.out.println("o mes é: "+contMes);
											contMes++;
											//System.out.println(meses[j].substring(2,meses[j].length()));
											//if(Arrays.asList(yourArray).contains(yourValue)) {
											//m.substring(4,6)
										}
									}
								}
								//System.out.println("Terminou");
								contMes = 0;
								contAno++;
							}
							//System.out.println(directoriesInsideArpa[0].substring(4, 8));
						}
					}
				}
			}

		}catch(Exception e) {

		}
		while (anos[cont] == null) {
			error.setMensagem(error.pastaNaoEncontradaArpa());
			mensagem(error.getMensagem());
			System.exit(0);
			break;
		}

		boolean notNull = false;
		for(String[] array : mes){
			for(String val : array){
				if(val!=null){
					notNull=true;
					break;
				}
			}
		}
		if(!notNull){
			error.setMensagem(error.pastaNaoEncontradaArpa());
			mensagem(error.getMensagem());
			System.exit(0);
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// Check for Updates
	
	public void checkForUpdates() {
		
		String versao = update.verificar();
		File jjexml = null;
		File desktop = new File(System.getProperty("user.home")+"/Desktop/");
		String[] desktopinside = desktop.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isFile();
			}
		});
		//System.out.println("------------------------------------");
		//System.out.println(Arrays.toString(desktopinside));
		//System.out.println("------------------------------------");
		try {
			for(String a : desktopinside) {
				if(a.contains("JJE XML")) {
					jjexml = new File(desktop+"/"+a);
					//System.out.println(jjexml);
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}

		try {
			update = new Update(update.host,21, update.user, update.pass);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//System.out.println(oi.substring(8, 11));
		//System.out.println(version.getVersion().substring(2, 5));
		String file = (versao.isEmpty() ? "" : versao.substring(62,77));
		
		File pasta = new File(getDestino());
		//System.out.println(pasta);
		pasta = pasta.getParentFile().getParentFile().getParentFile();
		//System.out.println(pasta);
		pasta = new File(pasta+"//JJE XML");
		//if(pasta.isDirectory()) {
			//System.out.println("opa");
		//}
		//System.out.println(jjexml);
		if(!file.isEmpty()) {
			if (!version.getVersion().substring(2, 5).contains(versao.substring(70, 73))) {
				int reply = JOptionPane.showConfirmDialog(null, "Tem atualização disponível, deseja atualizar para a versão " + versao.substring(70, 73) + " ?", "JJE XML", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {

					try {
						update.downloadFTPFile(update.filePath + file, update.destination);
						update.disconnect();
						try {
							jjexml.delete();
							FileUtils.deleteDirectory(pasta);
						} catch (Exception e) {
							//e.printStackTrace();
						}
						error.setMensagem(error.DownloadCompleto());
						mensagem(error.getMensagem());
						String command = update.destination;
						ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", command);
						Process p = pb.start();
						System.exit(0);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
				}
			}
		}
	}

	public void setIcone() {
		this.icone = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/logo.png"));
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
