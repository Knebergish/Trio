package trio.model;


import java.io.Serializable;
import java.util.List;


public interface Repo<I extends Serializable, T extends IEntity<I>> {
	T getById(I id);
	
	void remove(T item);
	
	T save(T item);
	
	List<T> getAll();
}
