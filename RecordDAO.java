package com.hiveag.geepy.dao;

import org.springframework.stereotype.Repository;

import com.hiveag.geepy.pojo.Record;
import com.hiveag.geepy.pojo.RecordId;

@Repository
public class RecordDAO extends AbstractDAO<Record, RecordId>{

	public RecordDAO() {
		// TODO Auto-generated constructor stub
		super(Record.class);
	}
	
	public Record findByBiId(long biId) {
		Record record= null;
		try {
			record= (Record) getCurrentSession()
					.createQuery("from Record r where r.id.biId= :biId")
					.setParameter("biId", biId).getSingleResult();
		} catch (RuntimeException re) {
			throw re;
		}

		return record;
	}
	
	public Record findByBiGeImei(long geImei) {
		Record record= null;
		try {
			record= (Record) getCurrentSession()
					.createQuery("from Record r where r.id.geImei= :geImei")
					.setParameter("geImei", geImei).getSingleResult();
		} catch (RuntimeException re) {
			throw re;
		}

		return record;
	}
	
	

}
