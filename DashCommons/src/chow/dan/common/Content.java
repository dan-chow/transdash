package chow.dan.common;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class Content implements Serializable {

	public final static Content EMPTY = new Content(new byte[0]);

	private static final long serialVersionUID = 1L;
	byte[] data;

	public Content(byte[] content) {
		this.data = content;
	}

	public byte[] getData() {
		return data;
	}

	public String toString(String charsetName) throws UnsupportedEncodingException {
		return new String(data, charsetName);
	}
}
