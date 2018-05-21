package chow.dan;

public interface Cache {
	public Chunk get(String key);

	public void set(String key, Chunk value);
}
