package discreta;

import java.io.IOException;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractFrameBodyTextInformation;
import org.farng.mp3.id3.AbstractID3v2;
import org.farng.mp3.id3.AbstractID3v2Frame;
import org.farng.mp3.id3.AbstractID3v2FrameBody;
import org.farng.mp3.id3.FrameBodyTMOO;
import org.farng.mp3.id3.ID3v2_4Frame;

public class ID3 {
	private static final int CHAVE = 7;
	
	private MP3File mp3file;
	private AbstractID3v2 tag;
	private AbstractID3v2Frame frame;
	private AbstractID3v2FrameBody frameBody;

	public ID3(String caminho) throws IOException, TagException {

		mp3file = new MP3File(caminho);
		tag = mp3file.getID3v2Tag();
	}

	public void adicionaMensagemAoArquivo(String mensagem) throws IOException, TagException {
		String mensagemcifrada = cifra(mensagem, CHAVE); 
		if (tag.hasFrame("TMOO")) {
			((AbstractFrameBodyTextInformation) (tag.getFrame("TMOO")).getBody()).setText(mensagemcifrada);
		} else {
			frame = criaFrame();
			tag.setFrame(frame);
			tag.getFrame("TMOO").setBody(frameBody);
			((FrameBodyTMOO) frame.getBody()).setText(mensagemcifrada);
		}
		mp3file.save();
	}

	public AbstractID3v2Frame criaFrame() {
		frameBody = new FrameBodyTMOO((byte) 0, "");
		return new ID3v2_4Frame(frameBody);
	}

	public String getFrameMensagem() throws Exception {
		frame = tag.getFrame("TMOO");
		if (frame == null) {
			throw new Exception("Nao existe mensagem no arquivo selecionado.");
		}
		String mensagemcifrada = ((FrameBodyTMOO) frame.getBody()).getText();
		String mensagemNaoCifrada = decifra(mensagemcifrada, CHAVE);
		return mensagemNaoCifrada;

	}
	
	public String cifra(String mensagem, int chave){
        StringBuilder builder = new StringBuilder();
 
        for (int i = 0; i < mensagem.length(); i++) {
            char c = (char)(mensagem.charAt(i) + chave);
            builder.append(c);
        }
 
        return builder.toString();
    }
 
    public String decifra(String mensagem, int chave){
        return cifra(mensagem, -chave);
    }
}
