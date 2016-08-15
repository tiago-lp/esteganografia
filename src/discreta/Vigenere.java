package discreta;

import java.util.Scanner;

public class Vigenere {

	private Scanner scanner = new Scanner(System.in);

	// A chave tem valor padrao
	private static String chave = "DISCRETA";

	public static String encrypt(String text) throws Exception {

		if (text.trim().isEmpty()) {
			throw new Exception("Mensagem nao pode ser vazia!");
		}

		String res = "";
		text = text.toUpperCase();
		for (int i = 0, j = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c < 'A' || c > 'Z')
				continue;
			res += (char) ((c + chave.charAt(j) - 2 * 'A') % 26 + 'A');
			j = ++j % chave.length();
		}
		return res;
	}

	public static String decrypt(String text) {
		String res = "";
		text = text.toUpperCase();
		for (int i = 0, j = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c < 'A' || c > 'Z')
				continue;
			res += (char) ((c - chave.charAt(j) + 26) % 26 + 'A');
			j = ++j % chave.length();
		}
		return res;
	}

	public String leituraMensagem(String prompt) {
		System.out.print(prompt);
		return scanner.nextLine();
	}

	public int leituraOpcao(String prompt) {
		System.out.print(prompt);
		int numero = scanner.nextInt();
		scanner.nextLine();
		return numero;
	}
}
