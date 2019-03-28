package trio.model;


import trio.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;


@SuppressWarnings("WeakerAccess")
public abstract class AbstractRepo<I extends Serializable, T extends IEntity<I>> implements Repo<I, T> {
	protected static final EntityManager   entityManager   = HibernateUtil.getSessionFactory().createEntityManager();
	protected final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	
	@Override
	public T getById(I id) {
		return entityManager.find(getEntityClass(), id);
	}
	
	@Override
	public void remove(T item) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.remove(item);
		entityManager.flush();
		transaction.commit();
	}
	
	@Override
	public T save(T item) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.persist(item);
		entityManager.flush();
		transaction.commit();
		return item;
	}
	
	@Override
	public List<T> getAll() {
		CriteriaQuery<T> cq        = criteriaBuilder.createQuery(getEntityClass());
		Root<T>          rootEntry = cq.from(getEntityClass());
		CriteriaQuery<T> all       = cq.select(rootEntry);
		TypedQuery<T>    allQuery  = entityManager.createQuery(all);
		return allQuery.getResultList();
	}
	
	protected abstract Class<T> getEntityClass();
}
