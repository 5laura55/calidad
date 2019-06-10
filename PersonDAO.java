package com.hiveag.geepy.dao;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Repository;

import com.hiveag.geepy.pojo.Person;

@Repository
public class PersonDAO extends AbstractDAO<Person, Long> {

	public PersonDAO() {
		// TODO Auto-generated constructor stub
		super(Person.class);
	}

	@SuppressWarnings("unchecked")
	public Person findByUsername(String peEMail) {

		log.debug("Getting by peEMail " + PersonDAO.class + " instance");

		List<Person> persons;
		Person person = null;

		try {
			persons = getCurrentSession().createQuery("from Person p where p.peEMail = :peEMail")
					.setParameter("peEMail", peEMail).list();
			if (persons.size() > 0)
				person = persons.get(0);
			log.debug("getting by peEMail successful");
		} catch (RuntimeException re) {
			log.error("getting by peEMail failed", re);
			throw re;
		}

		return person;
	}

	

	@SuppressWarnings("unchecked")
	public Person findByPhone(long pePhNumber) {

		log.debug("Getting by pePhNumber " + PersonDAO.class + " instance");

		List<Person> persons;
		Person person = null;

		try {
			persons = getCurrentSession().createQuery("from Person p where p.pePhNumber = :pePhNumber")
					.setParameter("pePhNumber", pePhNumber).list();
			if (persons.size() > 0)
				person = persons.get(0);
			log.debug("getting by pePhNumber successful");
		} catch (RuntimeException re) {
			log.error("getting by pePhNumber failed", re);
			throw re;
		}

		return person;
	}

}
