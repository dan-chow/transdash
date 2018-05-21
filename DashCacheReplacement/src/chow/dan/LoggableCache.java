package chow.dan;

public abstract class LoggableCache implements Cache {
	protected boolean logging = false;

	public void setLogEnabled(boolean enabled) {
		logging = enabled;
	}

}
