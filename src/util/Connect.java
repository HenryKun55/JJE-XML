package util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import email.Usuario;
import email.Version;

public class Connect {

	private Usuario usuario;
	private Version version;

	public Connection conectar() {
		usuario = Usuario.getInstance();
		version = Version.getInstance();
		Connection con = null;
		PreparedStatement stmt = null;
		String sql = null;
		try {
			if(criarDiretorio()) {
				sql = "CREATE TABLE XMLSEND " +
						"(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
						" NOME_EMPRESA           TEXT    NOT NULL, " + 
						" USUARIO            CHAR(80)     NOT NULL, " + 
						" DESTINATARIO        CHAR(80), " + 
						" ASSUNTO          CHAR(80)    NOT NULL, " + 
						" HOST          CHAR(30)    NOT NULL, " + 
						" SENHA            CHAR(30)     NOT NULL );";
				con = DriverManager.getConnection("jdbc:sqlite:C:\\Temp\\arpa\\xmlSend.db");
				stmt = con.prepareStatement(sql);
				stmt.executeUpdate();

				sql = "CREATE TABLE VERSION " +
						"(VERSION         CHAR(5)   NOT NULL );";
				stmt = con.prepareStatement(sql);
				stmt.executeUpdate();

				sql = "INSERT INTO VERSION(VERSION) VALUES(?)";
				String sql2 = "INSERT INTO XMLSEND(NOME_EMPRESA,USUARIO,DESTINATARIO,ASSUNTO,HOST,SENHA) VALUES(?,?,?,?,?,?)";

				try (Connection conn = con;
						PreparedStatement pstmt = conn.prepareStatement(sql);
								PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
					pstmt.setString(1, version.getVersion());
					pstmt.executeUpdate();
					pstmt2.setString(1, usuario.getNomeEmpresa());
					pstmt2.setString(2, usuario.getRemetente());
					pstmt2.setString(3, usuario.getDestinatario());
					pstmt2.setString(4, usuario.getAssunto());
					pstmt2.setString(5, usuario.getHost());
					pstmt2.setString(6, usuario.getSenha());
					pstmt2.executeUpdate();
					
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
			con = DriverManager.getConnection("jdbc:sqlite:C:\\Temp\\arpa\\xmlSend.db");
		}catch(Exception se){
			//System.err.println(se.getMessage());
		}
		finally {
			if (con != null) {
				//con.close();
			}
		}
		return con;
	}

	public static boolean criarDiretorio() {
		File theDir = new File("C:\\Temp\\arpa");
		boolean result = false;

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			System.out.println("creating directory: " + theDir.getName());

			try{
				theDir.mkdir();
				result = true;
			} 
			catch(SecurityException se){

			}        
		}
		return result;
	}
}
