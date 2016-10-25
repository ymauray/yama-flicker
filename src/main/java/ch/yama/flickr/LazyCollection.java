package ch.yama.flickr;

import java.util.AbstractCollection;
import java.util.Iterator;

/**
 * @author yannick
 *
 * @param <T>
 */
public class LazyCollection<T> extends AbstractCollection<T> {

	@Override
	public Iterator<T> iterator() {
		return new LazyIterator<>(this);
	}

	@Override
	public int size() {
		return this.lazyCollectionProvider.getTotal();
	}

	// Implementation specific

	private final LazyCollectionProvider<T> lazyCollectionProvider;

	public LazyCollection(LazyCollectionProvider<T> lazyCollectionProvider) {
		this.lazyCollectionProvider = lazyCollectionProvider;
		this.lazyCollectionProvider.fetch();
	}

	boolean hasNext() {
		return this.lazyCollectionProvider.hasNext();
	}

	T next() {
		return this.lazyCollectionProvider.next();
	}

	/**
	 *
	 */
	private class LazyIterator<E> implements Iterator<E> {

		private final LazyCollection<E> collection;

		public LazyIterator(final LazyCollection<E> collection) {
			this.collection = collection;
		}

		@Override
		public boolean hasNext() {
			return collection.hasNext();
		}

		@Override
		public E next() {
			return collection.next();
		}

	}

}
