package interfaceGrafica;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.farng.mp3.TagException;

import discreta.ID3;
import discreta.Vigenere;

public class Janela implements ActionListener, KeyListener{
	
	ID3 id3;
	
	JFrame janela;
	JButton encript;
	JButton decript;
	JButton selecArquivo;
	JButton okE;
	JButton okD;
	JTextField mensagem;
	JTextField caminho;
	JButton voltar;
	
	Container cards;
	CardLayout c1;
	
	String caminhoArq;
	
	public Janela(){
		janela = new JFrame("Esteganografia em audio");
		janela.setSize(500, 200);
		janela.setVisible(true); //teste
		janela.setResizable(false); 
		janela.setLocationRelativeTo(null);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel msg = new JLabel("Selecione um arquivo MP3 e clique no que deseja fazer");
		
		selecArquivo = new JButton("Selecionar arquivo");
		selecArquivo.addActionListener(this);
		selecArquivo.setPreferredSize(new Dimension(180,25));
		
		caminho = new JTextField(20);
		caminho.setEditable(false);
		
		encript = new JButton("Encriptar"); 
		encript.addActionListener(this);
		encript.setPreferredSize(new Dimension(100,25));
		
		decript = new JButton("Decriptar");
		decript.addActionListener(this);
		decript.setPreferredSize(new Dimension(100,25));
		
		voltar = new JButton("Voltar");
		voltar.addActionListener(this);
		voltar.setPreferredSize(new Dimension(180,25));
		
//		Container principal = new JPanel();
//		Container interno = new JPanel();
		
		cards = new JPanel(new CardLayout());
		janela.add(cards, BorderLayout.CENTER);
//		cards.add(principal, "principal");
//		
//		interno.setLayout(new GridLayout(2, 2, 10, 10));
//		principal.setLayout(new BorderLayout(10, 10));
		
		Container principal = new JPanel();
		principal.setLayout(new GridBagLayout());
		
		GridBagConstraints cons = new GridBagConstraints();
		cons.weightx = 1;
		cons.weighty = 1;
		
		cons.gridwidth = 2;
		cons.gridx = 0;
		cons.gridy = 0;
		principal.add(msg, cons); // teste
		
		cons.gridwidth = 1;
		
		cons.gridy = 1;
		
		cons.gridx = 0;
		principal.add(caminho, cons);
		
		cons.gridx = 1;
		principal.add(selecArquivo, cons);
		
		cons.gridy = 2;
		
		cons.gridx = 0;
		principal.add(encript, cons);
		
		cons.gridx = 1;
		principal.add(decript, cons);
		
//		principal.add(msg, BorderLayout.NORTH);
//		principal.add(interno, BorderLayout.CENTER);
//		interno.add(caminho);
//		interno.add(selecArquivo);
//		interno.add(encript);
//		interno.add(decript);
		
		cards.add(principal, "principal");
		
		c1 = (CardLayout) cards.getLayout();
		c1.show(cards, "principal");
		
		caminhoArq = "";
	}
	
	public void encriptar(){
		
		JLabel encriptarMsg = new JLabel("Digite a mensagem a ser encriptada: ");
		mensagem = new JTextField(10);
		mensagem.addKeyListener(this);
		okE = new JButton("Ok");
		okE.addActionListener(this);
		
		
		Container encriptacao = new JPanel();
		encriptacao.setLayout(new GridBagLayout());
		
		GridBagConstraints cons = new GridBagConstraints();
		cons.weightx = 1;
		cons.weighty = 1;
		
		cons.gridx = 0;
		cons.gridy = 0;
		encriptacao.add(encriptarMsg, cons);
		
		cons.gridx = 1;
		encriptacao.add(mensagem, cons);
		
		cons.gridy = 1;
		cons.gridx = 0;
		encriptacao.add(voltar, cons);
		
		cons.gridx = 1;
		encriptacao.add(okE, cons);
		
//		encriptacao.setLayout(new FlowLayout());
//		encriptacao.add(encriptarMsg);
//		encriptacao.add(mensagem);
//		encriptacao.add(okE);
//		encriptacao.add(voltar);
		
		cards.add(encriptacao, "encrip");
	}
	
	public void decriptar(){
		try{
		JLabel decriptarMsg = new JLabel(Vigenere.decrypt(id3.getFrameMensagem()));
//		mensagem = new JTextField(10);
//		okD = new JButton("Ok");
//		okD.addActionListener(this);
		
		
		Container encriptacao = new JPanel();
		encriptacao.setLayout(new FlowLayout());
		encriptacao.add(decriptarMsg);
//		encriptacao.add(mensagem);
//		encriptacao.add(okD);
		encriptacao.add(voltar);
		
		cards.add(encriptacao, "decrip");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == selecArquivo){
			
			JFileChooser file = new JFileChooser();
			file.setFileSelectionMode(JFileChooser.FILES_ONLY);
			
			FileNameExtensionFilter filterFile = new FileNameExtensionFilter("Arquivos MP3", "mp3");
			file.addChoosableFileFilter(filterFile);
			file.setAcceptAllFileFilterUsed(false);
			file.setFileFilter(filterFile);
			
			int retorno = file.showOpenDialog(null);
			
			if(retorno == JFileChooser.APPROVE_OPTION){
				caminhoArq = file.getSelectedFile().getAbsolutePath();
				caminho.setText(caminhoArq);
				try {
					id3 = new ID3(caminhoArq);
				} catch (IOException | TagException e1) {
					e1.printStackTrace();
				}
			}
		} else if(e.getSource() == encript){
			if(caminhoArq.trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Nenhum arquivo selecionado", "Erro", JOptionPane.ERROR_MESSAGE);
			} else{
				encriptar();
				c1.show(cards, "encrip");
			}
		} else if(e.getSource() == decript){
			if(caminhoArq.trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Nenhum arquivo selecionado", "Error", JOptionPane.ERROR_MESSAGE);
			} else{
				try {
					JOptionPane.showMessageDialog(null, Vigenere.decrypt(id3.getFrameMensagem()));
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} else if(e.getSource() == okE){
			try {
				id3.adicionaMensagemAoArquivo(Vigenere.encrypt(mensagem.getText()));
				JOptionPane.showMessageDialog(null, mensagem.getText() + " encriptado com sucesso.", "Encriptacao", JOptionPane.PLAIN_MESSAGE);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null, "Eh necessario pelo menos 1 caractere para encriptacao.", "Aviso!", JOptionPane.WARNING_MESSAGE); //
			} 
//			c1.show(cards, "principal");
		} else if(e.getSource() == okD){
//			ver se vai ser necessário digitar a chava pra descriptar
		} else if(e.getSource() == voltar){
			c1.show(cards, "principal");
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			try {
				id3.adicionaMensagemAoArquivo(Vigenere.encrypt(mensagem.getText()));
				JOptionPane.showMessageDialog(null, mensagem.getText() + " encriptado com sucesso.");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			c1.show(cards, "principal");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {	}
	
}
