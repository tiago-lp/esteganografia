package interfaceGrafica;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import discreta.ID3;

public class Menu extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ID3 id3;
	private Container cards;
	private CardLayout layout;
	private JButton selecArquivo, encriptar, decriptar, voltar, ok;
	private JTextField caminho, mensagem;

	public Menu() {
		super("Esteganografia em audio");
		setSize(500, 200);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.layout = new CardLayout();

		setLayout(this.layout);
		this.cards = getContentPane();

		// Inicializando botoes
		this.selecArquivo = new JButton("Selecionar arquivo");
		this.encriptar = new JButton("Encriptar");
		this.decriptar = new JButton("Decriptar");
		this.ok = new JButton("Ok");
		this.voltar = new JButton("Voltar");

		// Adicionando cards
		this.layout.show(cards, "inicial");
		this.cards.add(telaInicial(), "inicial");
		this.cards.add(encriptacao(), "encriptar");

	}

	private Container telaInicial() {
		JLabel msg = new JLabel("Selecione um arquivo MP3 e clique no que deseja fazer");

		this.selecArquivo.setPreferredSize(new Dimension(180, 25));
		this.selecArquivo.addActionListener(this);

		this.caminho = new JTextField(20);
		this.caminho.setEditable(false);

		this.encriptar.setPreferredSize(new Dimension(100, 25));
		this.encriptar.addActionListener(this);

		this.decriptar.setPreferredSize(new Dimension(100, 25));
		this.decriptar.addActionListener(this);

		Container principal = new JPanel();
		principal.setLayout(new GridBagLayout());

		GridBagConstraints cont = new GridBagConstraints();
		cont.weightx = 1;
		cont.weighty = 1;

		cont.gridwidth = 2;
		cont.gridx = 0;
		cont.gridy = 0;
		principal.add(msg, cont);

		cont.gridwidth = 1;
		cont.gridx = 0;
		cont.gridy = 1;
		principal.add(this.caminho, cont);

		cont.gridx = 1;
		principal.add(this.selecArquivo, cont);

		cont.gridx = 0;
		cont.gridy = 2;
		principal.add(this.encriptar, cont);

		cont.gridx = 1;
		principal.add(this.decriptar, cont);

		return principal;
	}

	private Container encriptacao() {
		JLabel encriptarMsg = new JLabel("Digite a mensagem a ser encriptada: ");
		this.mensagem = new JTextField(20);
		this.mensagem.addActionListener(this);

		this.ok.addActionListener(this);

		this.voltar.setPreferredSize(new Dimension(100, 25));
		this.voltar.addActionListener(this);

		Container encriptacao = new JPanel();
		encriptacao.setLayout(new GridBagLayout());

		GridBagConstraints cont = new GridBagConstraints();
		cont.weightx = 1;
		cont.weighty = 1;

		cont.gridx = 0;
		cont.gridy = 0;
		encriptacao.add(encriptarMsg, cont);

		cont.gridx = 1;
		encriptacao.add(this.mensagem, cont);

		cont.gridy = 1;
		cont.gridx = 0;
		encriptacao.add(this.voltar, cont);

		cont.gridx = 1;
		encriptacao.add(this.ok, cont);

		return encriptacao;
	}

	private JFileChooser abrirArquivo() {
		JFileChooser file = new JFileChooser();
		file.setFileSelectionMode(JFileChooser.FILES_ONLY);

		FileNameExtensionFilter filterFile = new FileNameExtensionFilter("Arquivos MP3", "mp3");
		file.addChoosableFileFilter(filterFile);
		file.setAcceptAllFileFilterUsed(false);
		file.setFileFilter(filterFile);
		return file;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.selecArquivo) {
			JFileChooser file = abrirArquivo();
			int retorno = file.showOpenDialog(null);

			if (retorno == JFileChooser.APPROVE_OPTION) {
				this.caminho.setText(file.getSelectedFile().getAbsolutePath());
				try {
					this.id3 = new ID3(this.caminho.getText());
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Arquivo invalido", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		} else if (e.getSource() == this.encriptar) {
			if (this.caminho.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Nenhum arquivo selecionado", "ERROR", JOptionPane.ERROR_MESSAGE);
			} else {
				this.layout.show(this.cards, "encriptar");
			}
		} else if (e.getSource() == this.ok || e.getSource() == this.mensagem) {
			try {
				this.id3.adicionaMensagemAoArquivo(this.mensagem.getText());
				JOptionPane.showMessageDialog(null, "Encriptacao feita com sucesso.", "Encriptacao",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null, "Eh necessario pelo menos 1 caractere para encriptacao.", "Aviso!",
						JOptionPane.WARNING_MESSAGE); //
			}
		} else if (e.getSource() == this.decriptar) {
			if (this.caminho.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Nenhum arquivo selecionado", "ERROR", JOptionPane.ERROR_MESSAGE);
			} else {
				try {
					JOptionPane.showMessageDialog(null, this.id3.getFrameMensagem(), "Mensagem decriptada",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		} else if (e.getSource() == this.voltar) {
			this.layout.show(this.cards, "inicial");
		}
	}
}