package util;

import email.Usuario;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.*;

public class PrintReport extends JFrame {

    private Connection connection;
    private static final long serialVersionUID = 1L;

    public void showReport(String ip) throws JRException {
        Usuario u = Usuario.getInstance();
        connection = Connect.print(ip, u.getNomeBase(), u.getUsuarioBanco(), u.getSenhaBanco());

        //System.out.println(getClass().getResource("/xml"));

        JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("/xml/report.jrxml"));
        JasperDesign  jasperDesignSR = JRXmlLoader.load(getClass().getResourceAsStream("/xml/subReport.jrxml"));

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperReport jasperReportSR = JasperCompileManager.compileReport(jasperDesignSR);

        int month = Integer.parseInt(u.getMes().substring(u.getMes().length() - 1));

        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(u.getAno()), month - 1 , 1);

        Date dateIn = c.getTime();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateIn);

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("subReport", jasperReportSR);
        parameters.put("data_inicial", c.getTime());
        parameters.put("data_final", calendar.getTime());

        //JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResource("/jasper/"+reports.get(positionReport)).getFile()); //PARA INTELLIJ

        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, connection);
        JasperExportManager.exportReportToPdfFile(print, "C:\\Temp\\report.pdf");
//        JRViewer viewer = new JRViewer(print);
//        viewer.setOpaque(true);
//        this.add(viewer);
//        this.setSize(sizeScreen());
//        this.setVisible(true);
//        this.toFront();
//        this.repaint();
    }

    private Dimension sizeScreen(){
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        return screenSize;
    }
}