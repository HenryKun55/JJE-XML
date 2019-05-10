package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Zip {

    private String folderPath;
    private String zipPath;

    private static ArrayList<String> fileList = null;
    private static String sourceFolder = null;

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getZipPath() {
        return zipPath;
    }

    public void setZipPath(String zipPath) {
        this.zipPath = zipPath;
    }

    public Zip(){}

    public Zip(String folderPath, String zipPath){
        this.folderPath = folderPath;
        this.zipPath = zipPath;
    }

    public Zip(String zipPath){
        this.zipPath = zipPath;
    }

    public boolean zip() throws IOException{

        byte[] buffer = new byte[1024];

        FileInputStream fis = null;
        ZipOutputStream zos = null;

        fileList = new ArrayList<String>();

        try{
            zos = new ZipOutputStream(new FileOutputStream(this.zipPath));
            updateSourceFolder(new File(this.folderPath));

            if (sourceFolder == null) {
                zos.close();
                return false;
            }

            generateFileAndFolderList(new File(this.folderPath));

            for (String unzippedFile: fileList) {

            	if(unzippedFile.endsWith(".pdf")) {
            		
            	} else {
            	
	                //System.out.println(sourceFolder + unzippedFile);
	
	                ZipEntry entry = new ZipEntry(unzippedFile);
	                zos.putNextEntry(entry);
	
	                if ((unzippedFile.substring(unzippedFile.length()-1)).equals(File.separator))
	                    continue;
	
	                try{
	                    fis = new FileInputStream(sourceFolder + unzippedFile);
	
	                    int len=0;
	                    while ((len = fis.read(buffer))>0) {
	                        zos.write(buffer,0,len);
	                    }
	                }catch(IOException e){
	
	                    e.printStackTrace();
	                    return false;
	
	                }finally{
	
	                    if (fis!=null)
	                        fis.close();
	                }
            	}
            }

            zos.closeEntry();

        }catch(IOException e){

            e.printStackTrace();
            return false;

        }finally{

            zos.close();
            fileList = null;
            sourceFolder = null;
        }

        return true;
    }

    public boolean unzip() throws IOException{

        byte[] buffer = new byte[1024];

        ZipInputStream zis = null;
        ZipEntry entry = null;
        FileOutputStream fos = null;

        fileList = new ArrayList<String>();

        try {

            File file = new File(this.zipPath);
            zis = new ZipInputStream(new FileInputStream(file));

            while((entry = zis.getNextEntry())!= null){

                String dir = entry.getName();

                try{

                    String fileSeparator = dir.substring(dir.length()-1);

                    boolean isFolder=(fileSeparator.equals("/") || fileSeparator.equals("\\"));

                    if (isFolder){

                        dir = dir.substring(0,dir.lastIndexOf(fileSeparator)+1);
                        dir = (file.getParent() == null? "":file.getParent()+ File.separator) + dir;

                        (new File(dir)).mkdirs();

                        continue;

                    }

                }catch(Exception e){}finally{}

                //System.out.println("dir:" + dir);

                fos = new FileOutputStream((file.getParent() == null? "":file.getParent()+ File.separator) + entry.getName());

                int len=0;
                while ((len = zis.read(buffer))>0) {
                    fos.write(buffer,0,len);
                }
                fos.close();
            }

            zis.close();

            return true;

        }catch (IOException ioe){

            ioe.printStackTrace();
            return false;

        }finally{

            fileList = null;
            sourceFolder = null;
        }
    }

    //Fills filesList with file name and their paths
    private void generateFileAndFolderList(File node){

        // Add file only
        if (node.isFile()){

            fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
        }

        if (node.isDirectory()){
        	//System.out.println(node);
        	if(node.isFile()) {
        		//System.out.println("Contém arquivo");
        	}else {

	            String dir = node.getAbsoluteFile().toString();
	            fileList.add(dir.substring(sourceFolder.length(), dir.length()) + File.separator);
	
	            String[] subNode = node.list();
	            for (String fileOrFolderName : subNode){
	
	                generateFileAndFolderList(new File(node, fileOrFolderName));
	            }
        	}
        }
    }

    //Generate file name based on source folder
    private String generateZipEntry(String file){

        return file.substring(sourceFolder.length(), file.length());
    }

    //Updates source folder based on source type: File or Folder
    private void updateSourceFolder(File node){

        if (node.isFile() || node.isDirectory()){

            String sf = node.getAbsoluteFile().toString();

            sourceFolder = sf.substring(0,(sf.lastIndexOf("/")>0?sf.lastIndexOf("/"):sf.lastIndexOf("\\")));
            sourceFolder += File.separator;

        } else
            sourceFolder=null;//the file does not exists
    }
    
    public void ziparPasta(String fileDirectory) {

        Zip zip = new Zip (fileDirectory, "C:\\Temp\\XML.zip");

        try {
            zip.zip();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main (String [] args) {
    	Zip zip = new Zip();
    	zip.ziparPasta("C:\\Program Files (x86)\\arpa\\control\\nfe\\ano_2018\\mes_07");
    }
}
