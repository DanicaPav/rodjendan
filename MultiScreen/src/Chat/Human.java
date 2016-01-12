package Chat;

import java.io.InputStream;

public class Human {
	
	private int godina;
	
	private InputStream in;

	public void setGodina(int godina) {
		if (godina < 0) {
			godina = 0;
		} else {
			this.godina = godina;
		}
	}
	
	public int getGodina() {
		return godina;
	}
	
	public InputStream getIn() {
		return in;
	}
}
