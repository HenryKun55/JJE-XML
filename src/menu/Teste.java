package menu;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JDialog;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import java.lang.Thread;

public class Teste extends JDialog {

	private JPanel contentPane;
	private JProgressBar progressBar;

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Singleton

	private static Teste instance = null;

	public static Teste getInstance() {
		if(instance == null) {
			instance = new Teste();
		}
		return instance;
	}	

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Teste frame = Teste.getInstance();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Teste() {
		setBounds(100, 100, 200, 100);
		setUndecorated(true);

		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
		contentPane.setForeground(Color.PINK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);

		progressBar = new JProgressBar();
		progressBar.setBounds(12, 35, 176, 30);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		contentPane.add(progressBar);
	}

	public void progresso(int percent) {
		progressBar.setValue(percent);
	}
}
