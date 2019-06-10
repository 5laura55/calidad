package com.hiveag.geepy.dao;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hiveag.geepy.pojo.Person;
import com.hiveag.geepy.pojo.Picture;
import com.hiveag.geepy.pojo.PictureId;
import com.hiveag.geepy.pojo.Record;

@Repository
public class PictureDAO extends AbstractDAO<Picture, PictureId> {

	public PictureDAO() {
		// TODO Auto-generated constructor stub
		super(Picture.class);
	}

	public List<Picture> findByBiId(long biId) {
		List<Picture> pictures=null;

		try {
			pictures = getCurrentSession().createQuery("from Picture p where p.id.biId= :biId")
					.setParameter("biId", biId).getResultList();
		} catch (RuntimeException re) {
			throw re;
		}

		return pictures;
	}

	public Picture findSinglePictureByBiId(long biId) {
		Picture picture;

		try {
			picture = (Picture) getCurrentSession().createQuery("from Picture p where p.id.biId= :biId")
					.setParameter("biId", biId).getSingleResult();
		} catch (RuntimeException re) {
			throw re;
		}

		return picture;
	}

	

}
