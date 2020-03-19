package api.streams.interfaces;

@FunctionalInterface
public interface MultiplyLogic<T> {
	public T multiply(T t1);
}
