package api.streams.interfaces;

@FunctionalInterface
public interface FilterLogic<T> {
	public boolean isValid(T data);
}
