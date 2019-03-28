package util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

import javax.swing.SwingUtilities;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.CopyStreamAdapter;

import email.Version;
import menu.Teste;

public class Update {

	String ftpUrl = "ftp://%s:%s@%s/%s;type=d";
	public static String host = "jesistemas.com.br";
	public static String user = "c48bdwgpklyz";
	public static String pass = "Dqv1mdcs1pdp*";
	String dirPath = "public_html/suporte/JJE XML";
	public String filePath = "public_html/suporte/JJE XML/";
	public String destination = "C://Temp//JJE XML.exe";
	
	FTPClient ftp = null;
	
	Version version;
	
	FTPFile file;
	
	private static Teste frame = new Teste();
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Singleton

	private static Update instance = null;

	public static Update getInstance() {
		if(instance == null) {
			instance = new Update();
		}
		return instance;
	}	

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	public String verificar() {
		
		version = Version.getInstance();

		ftpUrl = String.format(ftpUrl, user, pass, host, filePath);
		System.out.println("URL: " + ftpUrl);

		try {
			URL url = new URL(ftpUrl);
			URLConnection conn = url.openConnection();
			InputStream inputStream = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

			String line = null;
			System.out.println("--- START ---");
			//System.out.println(version.getVersion().substring(2, 5));
			while ((line = reader.readLine()) != null) {
				//System.out.println(line);
				if(line.contains("JJE")) {

					//System.out.println("Oi");
					System.out.println(line);
					return line;
				}else {

				}
			}
			
			System.out.println("--- END ---");
			//System.out.println(version.getNewVersion());

			inputStream.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public Update() {
		
	}
	
	// Constructor to connect to the FTP Server
    public Update(String host, int port, String username, String password) throws Exception{
        
        ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        int reply;
        ftp.connect(host,port);
        System.out.println("FTP URL is:"+ftp.getDefaultPort());
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new Exception("Exception in connecting to FTP Server");
        }
        ftp.login(username, password);
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();        
    }    
    
    // Download the FTP File from the FTP Server
    public void downloadFTPFile(String source, String destination) {
    	
    	//frame.setModal(true);
    	//frame.setVisible(true);
    	
    	System.out.println(this.ftp);
    	
    	try {
			file = this.ftp.mlistFile(source);
			System.out.println("O arquivo é :" + file);
    	} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	CopyStreamAdapter streamListener = new CopyStreamAdapter() {

            @Override
            public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
                //this method will be called everytime some bytes are transferred
            	
                int percent = (int) (totalBytesTransferred * 100 / file.getSize());
                //frame.progresso(percent);
            }
    	};
    	this.ftp.setCopyStreamListener(streamListener);
        try (FileOutputStream fos = new FileOutputStream(destination)) {
        	
            this.ftp.retrieveFile(source, fos);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Disconnect the connection to FTP
    public void disconnect(){
        if (this.ftp.isConnected()) {
            try {
                this.ftp.logout();
                this.ftp.disconnect();
            } catch (IOException f) {
                // do nothing as file is already saved to server
            }
        }
    }
    
    public void conectar(String file) throws Exception {
    	Update up = new Update(host, 21, user, pass);
    	//up.downloadFTPFile(filePath+"/"+file, "C://Temp//JJE XML.exe");
    	//up.disconnect();
    }

	public static void main (String [] args) throws Exception  {
		Update up = new Update(host, 21, user, pass);
		Teste frame = new Teste();
		up.downloadFTPFile("public_html/suporte/JJE XML/"+"JJE XML 1.7.exe", "C://JJE XML.exe");
		//up.disconnect();
		//up.verificar();
	}
}