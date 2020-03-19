package api.streams.interfaces;

@FunctionalInterface
public interface DisplayLogic<T> {
	public void display(T data);
}
