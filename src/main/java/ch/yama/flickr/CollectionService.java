package ch.yama.flickr;

public interface CollectionService<T> {

	int getTotal();

	boolean hasNext();

	T next();

}