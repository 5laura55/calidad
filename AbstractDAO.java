package com.hiveag.geepy.dao;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;;


/**
 * @see SessionFactory
 * 
 * @author Jorge Díaz
 * @param <T>
 * @param <I>
 */

public class AbstractDAO < T extends Serializable, I extends Serializable > {
	
	private Class< T > clazz;
	protected static final Log log = LogFactory.getLog(AbstractDAO.class);
	
	@Autowired
	SessionFactory sessionFactory;
	
	
	public AbstractDAO(Class< T > clazzToSet) {
		this.clazz = clazzToSet;
	}

	protected final Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}
	
	
	public T findById(I id){

		log.debug("Getting " + clazz.getName() + " instance");
		
		T t = null;
		
		try {
			t = (T) getCurrentSession()
						.get(clazz, id);
			log.debug("getting by id successful");
		} catch (RuntimeException re) {
			log.error("getting by id failed", re);
			throw re;
		}
			
		return t;
	}
	
	@SuppressWarnings("unchecked")
	public List< T > findAll(){

		log.debug("Getting All " + clazz.getName() + " instance");
		
		List< T > t = null;
		
		try {
			t = getCurrentSession()
					.createQuery( "from " + clazz.getName()).list();
			log.debug("getting all successful");
		} catch (RuntimeException re) {
			log.error("getting all failed", re);
			throw re;
		}
		
		return t;
	}
	
	public void create(T entity){
		
		log.debug("Creating " + clazz.getName() + " instance");
		
		try {
			getCurrentSession()
				.persist(entity);
			log.debug("add successful");
		} catch (RuntimeException re) {
			log.error("add failed", re);
			throw re;
		}
	}
	
	public void update(T entity){
		
		log.debug("Updating " + clazz.getName() + " instance");
		
		try {
			getCurrentSession()
				.update(entity);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}
	
	public void delete(T entity){
		
		log.debug("Deleting " + clazz.getName() + " instance");
		
		try {
			getCurrentSession()
				.delete(entity);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	public void deleteById(I id){

		log.debug("Deleting " + clazz.getName() + " instance");
		
		try {
			T entity = findById(id);
			delete( entity );
			log.debug("delete by id successful");
		} catch (RuntimeException re) {
			log.error("delete by id failed", re);
			throw re;
		}
	}
	
}
