package menu;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

import email.Usuario;
import mensagens.Error;
import util.Connect;
import util.PropriedadesTela;

public class Configuracoes extends JDialog {

	private PropriedadesTela pt;
	private JPanel contentPane;
	private JLabel label;
	
	public JTextField textFieldEmailRemetente;
	public JTextField textFieldEmailDestinatario;
	public JTextField textFieldNomeDaEmpresa;
	public JTextField textFieldAssunto;
	public JTextField textFieldUsuarioBanco;
	public JTextField textFieldSenhaBanco;
	public JPasswordField textFieldEmailSenhaRemetente;
	
	private JButton buttonSalvar;
	
	private boolean salvar = false;
	
	public int resposta;
	
	private Font fontePadrao = new Font("Tahoma", Font.PLAIN, 17);
	private Font fontePlaceHolder= new Font("Tahoma", Font.PLAIN, 17);
	private Color colorPlaceHolder = Color.decode("#898B8C");
	
	private Error error = new Error();
	private Principal principal = new Principal();
	private static Usuario usuario;

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Singleton
	
	private static Configuracoes instance = null;
	
	public static Configuracoes getInstance() {
		if(instance == null) {
			instance = new Configuracoes();
		}
		return instance;
	}	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Configuracoes frame = Configuracoes.getInstance();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public String getEmailDestinatario() {
		return usuario.getDestinatario();
	}
	
	public boolean getSalvar() {
		return salvar;
	}
	
	public int windowClosing() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing (WindowEvent e)
            {
                resposta = JOptionPane.showConfirmDialog(null,"Salvar Configurações?","Finalizar",JOptionPane.YES_NO_OPTION);

                if (resposta == JOptionPane.YES_OPTION)
                {
                	char[] input = textFieldEmailSenhaRemetente.getPassword();
        		    String passString = new String(input);
                	principal.update(
                			textFieldNomeDaEmpresa.getText(), 
                			textFieldEmailRemetente.getText(), 
                			textFieldEmailDestinatario.getText(), 
                			textFieldAssunto.getText(), 
                			usuario.getHost(), 
                			passString,
							usuario.getUsuarioBanco(),
							usuario.getSenhaBanco());
                	usuario.setRemetente(textFieldEmailRemetente.getText());
					usuario.setDestinatario(textFieldEmailDestinatario.getText());
					usuario.setNomeEmpresa(textFieldNomeDaEmpresa.getText());
					usuario.setAssunto(textFieldAssunto.getText());
						
					if(smtp(usuario.getRemetente())) {
						if(passString.equals("*********")) {
							usuario.setSenha(usuario.getSenha());
						}else {
							usuario.setSenha(passString);
						}
						
						principal.update(usuario.getNomeEmpresa(), usuario.getRemetente(), usuario.getDestinatario(),
								usuario.getAssunto(),usuario.getHost(), usuario.getSenha(), usuario.getUsuarioBanco(), usuario.getSenhaBanco());
						
						error.setMensagem(error.DadosSalvosConfiguracoes());
						principal.mensagem(error.getMensagem());
					}
                	dispose();
                }else {
                	dispose();
                }
				
            }
		});
		return 1;
	}

	public Configuracoes() {
		setIconImage(principal.getIcone());
		usuario = Usuario.getInstance();
		pt = new PropriedadesTela(returnIn());
		setDefaultCloseOperation(windowClosing());
		setAutoRequestFocus(false);
		//setModal(true);
		setTitle("Configurações");
		contentPane();
		setContentPane(contentPane);
		setResizable(false);
		setSize(590, 400);
		setLocationRelativeTo(null);
		design();
	}
	
	
	public void contentPane() {
		contentPane = new JPanel();
		contentPane.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(0, 0, 0)));
		contentPane.setLayout(null);
		contentPane.setBackground(Color.decode("#AA3939"));
	}
	
	public void design() {
		labels();
		inputs();
		buttons();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void labels() {
		labelRemetente();
		labelEmailSenhaRemetente();
		labelDestinatario();
		labelNomeDaEmpresa();
		labelAssunto();
		labelUsuarioBanco();
		labelSenhaBanco();
	}
	
	public void inputs() {
		inputRemetente();
		inputSenhaEmailRemetente();
		inputDestinatario();
		inputNomeDaEmpresa();
		inputAssunto();
		inputUsuarioBanco();
		inputSenhaBanco();
	}
	
	public void buttons() {
		buttonSalvar();
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// Labels
	
	public void labelRemetente() {
		label = new JLabel("Remetente: ");
		label.setFont(fontePadrao);
		label.setForeground(Color.WHITE);
		label.setBounds(100, 20, 200, 22);
		contentPane.add(label);
	}
	
	public void labelEmailSenhaRemetente() {
		label = new JLabel("Senha: ");
		label.setFont(fontePadrao);
		label.setForeground(Color.WHITE);
		label.setBounds(100, 60, 200, 22);
		contentPane.add(label);
	}
	
	public void labelDestinatario() {
		label = new JLabel("Destinatário: ");
		label.setFont(fontePadrao);
		label.setForeground(Color.WHITE);
		label.setBounds(100, 100, 200, 22);
		contentPane.add(label);
	}
	
	public void labelNomeDaEmpresa() {
		label = new JLabel("Nome da Empresa/Físico: ");
		label.setFont(fontePadrao);
		label.setForeground(Color.WHITE);
		label.setBounds(100, 140, 200, 22);
		contentPane.add(label);
	}
	
	public void labelAssunto() {
		label = new JLabel("Assunto: ");
		label.setFont(fontePadrao);
		label.setForeground(Color.WHITE);
		label.setBounds(100, 180, 200, 22);
		contentPane.add(label);
	}

	public void labelUsuarioBanco() {
		label = new JLabel("Usuário Banco: ");
		label.setFont(fontePadrao);
		label.setForeground(Color.WHITE);
		label.setBounds(100, 220, 200, 22);
		contentPane.add(label);
	}

	public void labelSenhaBanco() {
		label = new JLabel("Senha Banco: ");
		label.setFont(fontePadrao);
		label.setForeground(Color.WHITE);
		label.setBounds(100, 260, 200, 22);
		contentPane.add(label);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//Inputs
	
	public void inputRemetente() {	
		textFieldEmailRemetente = new JTextField();
		textFieldEmailRemetente.setBounds(190, 21, 284, 22);
		contentPane.add(textFieldEmailRemetente);
		textFieldEmailRemetente.setFont(fontePadrao);
		textFieldEmailRemetente.setColumns(10);
		textFieldEmailRemetente.setFont(fontePlaceHolder);
		//textFieldEmailRemetente.setForeground(colorPlaceHolder);
		textFieldEmailRemetente.setText(usuario.getRemetente());
		textFieldEmailRemetente.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(textFieldEmailRemetente.getText().length() == 0 || 
						textFieldEmailRemetente.getText().isEmpty() ||
						textFieldEmailRemetente.getText().equals("jje.xml@gmail.com")) {
							textFieldEmailRemetente.setFont(fontePlaceHolder);
							//textFieldEmailRemetente.setForeground(colorPlaceHolder);
							textFieldEmailRemetente.setText("jje.xml@gmail.com");
					}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if(textFieldEmailRemetente.getText().length() == 0 || 
						textFieldEmailRemetente.getText().isEmpty() ||
						textFieldEmailRemetente.getText().equals("jje.xml@gmail.com")) {
							textFieldEmailRemetente.setFont(fontePadrao);
							textFieldEmailRemetente.setForeground(Color.BLACK);
							textFieldEmailRemetente.setText("");
				}
			}
		});
	}
	
	public void inputSenhaEmailRemetente() {	
		textFieldEmailSenhaRemetente = new JPasswordField();
		textFieldEmailSenhaRemetente.setBounds(152, 61, 322, 22);
		contentPane.add(textFieldEmailSenhaRemetente);
		textFieldEmailSenhaRemetente.setFont(fontePadrao);
		textFieldEmailSenhaRemetente.setColumns(10);
		textFieldEmailSenhaRemetente.setFont(fontePlaceHolder);
		textFieldEmailSenhaRemetente.setText(usuario.getSenha());
		textFieldEmailSenhaRemetente.addFocusListener(new FocusListener() {
			
			char[] input = textFieldEmailSenhaRemetente.getPassword();
		    String passString = new String(input);
			
			@Override
			public void focusLost(FocusEvent e) {
				if(passString.length() == 0 || 
						passString.isEmpty() ||
						passString.equals("")) {
							textFieldEmailSenhaRemetente.setFont(fontePlaceHolder);
							//textFieldEmailSenhaRemetente.setForeground(colorPlaceHolder);
							textFieldEmailSenhaRemetente.setText("*********");
					}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if(passString.length() == 0 || 
						passString.isEmpty() ||
						passString.equals("*********")) {
							textFieldEmailSenhaRemetente.setFont(fontePadrao);
							textFieldEmailSenhaRemetente.setForeground(Color.BLACK);
							textFieldEmailSenhaRemetente.setText("");
				}
			}
		});
	}
	
	public void inputDestinatario() {	
		textFieldEmailDestinatario = new JTextField();
		textFieldEmailDestinatario.setBounds(198, 101, 276, 22);
		contentPane.add(textFieldEmailDestinatario);
		textFieldEmailDestinatario.setFont(fontePadrao);
		textFieldEmailDestinatario.setColumns(10);
		textFieldEmailDestinatario.setFont(fontePlaceHolder);
		//textFieldEmailDestinatario.setForeground(colorPlaceHolder);
		textFieldEmailDestinatario.setText(usuario.getDestinatario());
		textFieldEmailDestinatario.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(textFieldEmailDestinatario.getText().length() == 0 || 
						textFieldEmailDestinatario.getText().isEmpty() ||
						textFieldEmailDestinatario.getText().equals("jje.xml@gmail.com")) {
							textFieldEmailDestinatario.setFont(fontePlaceHolder);
							//textFieldEmailDestinatario.setForeground(colorPlaceHolder);
							textFieldEmailDestinatario.setText("jje.xml@gmail.com");
					}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if(textFieldEmailDestinatario.getText().length() == 0 || 
						textFieldEmailDestinatario.getText().isEmpty() ||
						textFieldEmailDestinatario.getText().equals("jje.xml@gmail.com")) {
							textFieldEmailDestinatario.setFont(fontePadrao);
							textFieldEmailDestinatario.setForeground(Color.BLACK);
							textFieldEmailDestinatario.setText("");
				}
			}
		});
	}
	
	public void inputNomeDaEmpresa() {	
		textFieldNomeDaEmpresa = new JTextField();
		textFieldNomeDaEmpresa.setBounds(292, 141, 183, 22);
		contentPane.add(textFieldNomeDaEmpresa);
		textFieldNomeDaEmpresa.setFont(fontePadrao);
		textFieldNomeDaEmpresa.setColumns(10);
		textFieldNomeDaEmpresa.setFont(fontePlaceHolder);
		//textFieldNomeDaEmpresa.setForeground(colorPlaceHolder);
		textFieldNomeDaEmpresa.setText(usuario.getNomeEmpresa());
		textFieldNomeDaEmpresa.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(textFieldNomeDaEmpresa.getText().length() == 0 || 
						textFieldNomeDaEmpresa.getText().isEmpty() ||
						textFieldNomeDaEmpresa.getText().equals("JJE")) {
							textFieldNomeDaEmpresa.setFont(fontePlaceHolder);
							//textFieldNomeDaEmpresa.setForeground(colorPlaceHolder);
							textFieldNomeDaEmpresa.setText("JJE");
					}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if(textFieldNomeDaEmpresa.getText().length() == 0 || 
						textFieldNomeDaEmpresa.getText().isEmpty() ||
						textFieldNomeDaEmpresa.getText().equals("JJE")) {
							textFieldNomeDaEmpresa.setFont(fontePadrao);
							textFieldNomeDaEmpresa.setForeground(Color.BLACK);
							textFieldNomeDaEmpresa.setText("");
				}
			}
		});
	}
	
	public void inputAssunto() {	
		textFieldAssunto = new JTextField();
		textFieldAssunto.setBounds(168, 181, 308, 22);
		contentPane.add(textFieldAssunto);
		textFieldAssunto.setFont(fontePadrao);
		textFieldAssunto.setColumns(10);
		textFieldAssunto.setFont(fontePlaceHolder);
		//textFieldAssunto.setForeground(colorPlaceHolder);
		textFieldAssunto.setText(usuario.getAssunto());
		textFieldAssunto.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(textFieldAssunto.getText().length() == 0 || 
						textFieldAssunto.getText().isEmpty() ||
						textFieldAssunto.getText().equals("Enviando XML")) {
							textFieldAssunto.setFont(fontePlaceHolder);
							//textFieldAssunto.setForeground(colorPlaceHolder);
							textFieldAssunto.setText("Enviando XML");
					}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if(textFieldAssunto.getText().length() == 0 || 
						textFieldAssunto.getText().isEmpty() ||
						textFieldAssunto.getText().equals("Enviando XML")) {
							textFieldAssunto.setFont(fontePadrao);
							textFieldAssunto.setForeground(Color.BLACK);
							textFieldAssunto.setText("");
				}
			}
		});
	}

	public void inputUsuarioBanco() {
		textFieldUsuarioBanco = new JTextField();
		textFieldUsuarioBanco.setBounds(215, 221, 262, 22);
		contentPane.add(textFieldUsuarioBanco);
		textFieldUsuarioBanco.setFont(fontePadrao);
		textFieldUsuarioBanco.setColumns(10);
		textFieldUsuarioBanco.setFont(fontePlaceHolder);
		textFieldUsuarioBanco.setText(usuario.getUsuarioBanco());
	}

	public void inputSenhaBanco() {
		textFieldSenhaBanco = new JPasswordField();
		textFieldSenhaBanco.setBounds(205, 261, 272, 22);
		contentPane.add(textFieldSenhaBanco);
		textFieldSenhaBanco.setFont(fontePadrao);
		textFieldSenhaBanco.setColumns(10);
		textFieldSenhaBanco.setFont(fontePlaceHolder);
		textFieldSenhaBanco.setText(usuario.getSenhaBanco());
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	// Buttons
	
	public void buttonSalvar() {
		buttonSalvar = new JButton("Salvar");
		buttonSalvar.setBounds(260, 320, 97, 30);
		contentPane.add(buttonSalvar);
		
		buttonSalvar.addActionListener(e -> {
			if(textFieldEmailRemetente.getText().equals("") ||
			   textFieldEmailDestinatario.getText().equals("") ||
			   textFieldNomeDaEmpresa.getText().equals("") || textFieldNomeDaEmpresa.getText().isEmpty() ||
			   textFieldAssunto.getText().equals("") || textFieldAssunto.getText().isEmpty()) {
					error.setMensagem(error.DadosIncompletosConfiguracoes());
					principal.mensagem(error.getMensagem());

			}else {
				if( validar(textFieldEmailRemetente.getText()) && validar(textFieldEmailDestinatario.getText()) )  {

					char[] input = textFieldEmailSenhaRemetente.getPassword();
					String passString = new String(input);

					//System.out.println(passString);

					usuario.setRemetente(textFieldEmailRemetente.getText());
					usuario.setDestinatario(textFieldEmailDestinatario.getText());
					usuario.setNomeEmpresa(textFieldNomeDaEmpresa.getText());
					usuario.setAssunto(textFieldAssunto.getText());
					usuario.setSenha(passString);
					usuario.setUsuarioBanco(textFieldUsuarioBanco.getText());
					usuario.setSenhaBanco(textFieldSenhaBanco.getText());

					// System.out.println(smtp(usuario.getRemetente()));

					principal.update(usuario.getNomeEmpresa(), usuario.getRemetente(), usuario.getDestinatario(),
							usuario.getAssunto(),usuario.getHost(), usuario.getSenha(), usuario.getUsuarioBanco(), usuario.getSenhaBanco());

					error.setMensagem(error.DadosSalvosConfiguracoes());
					principal.mensagem(error.getMensagem());
					salvar = true;
					dispose();
					}else {
						error.setMensagem(error.EmailErrado());
						principal.mensagem(error.getMensagem());
					}
			}
		});
	}
	
		
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	// Labels
		
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
	
	//Utilidades
	
	//Validando Email
	
	public static boolean validar(String email)
    {
        boolean isEmailIdValid = false;
        if (email != null && email.length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                isEmailIdValid = true;
            }
        }
        return isEmailIdValid;
    }
	
	//Verificando qual SMTP usar

	public static boolean smtp(String email) {
		if(email.contains("gmail")) {
			usuario.setHost(usuario.gmail());
			return true;
		}
		if(email.contains("yahoo")) {
			usuario.setHost(usuario.yahoo());
			return true;
		}
		if(email.contains("hotmail") || email.contains("outlook") || email.contains("live") ) {
			usuario.setHost(usuario.hotmail());
			return true;
		}
		return false;
	}

}
