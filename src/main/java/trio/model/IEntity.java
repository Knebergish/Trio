package trio.model;


import java.io.Serializable;


public interface IEntity<I extends Serializable> {
	I getId();
}
