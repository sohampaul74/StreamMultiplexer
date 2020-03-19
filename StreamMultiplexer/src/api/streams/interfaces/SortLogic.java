package api.streams.interfaces;

import java.util.function.Predicate;

@FunctionalInterface
public interface SortLogic<T> extends Predicate<T> {
}
