package com.hiveag.geepy.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.hiveag.geepy.dto.BikeDTO;
import com.hiveag.geepy.dto.PoiLimitDTO;
import com.hiveag.geepy.pojo.Person;
import com.hiveag.geepy.pojo.Poi;

@Repository
public class PoiDAO extends AbstractDAO<Poi, Long> {

	public PoiDAO() {
		// TODO Auto-generated constructor stub
		super(Poi.class);
	}

	/** distancia entre dos puntos https://gist.github.com/Usse/4086343 */
	public List<Object[]> pointsRadio(PoiLimitDTO limit) {
		List<Object[]> rows = null;

		try {
			String sql = "";
			String sqlType = "";

			sql = "select * " + "from  " + "  (select *,6371 * 2 * ASIN(SQRT(   "
					+ "            POWER(SIN((po_latitude - abs(:poLatitude)) * pi()/180 / 2), "
					+ "            2) + COS(po_latitude * pi()/180 ) * COS(abs(:poLatitude) * "
					+ "            pi()/180) * POWER(SIN((po_longitude - :poLongitude) * "
					+ "            pi()/180 / 2), 2) )) as distance  " + "            from poi)  t_distance  "
					+ "            where distance <:distance        ";

			if (limit.getPoTypes() != null) {

				int size = limit.getPoTypes().length;
				for (int i = 0; i < size; i++) {
					if (i == 0)
						sqlType += " and ( po_type=" + "'" + limit.getPoTypes()[i] + "'";
					else
						sqlType += " or  po_type=" + "'" + limit.getPoTypes()[i] + "'";
				}

				sqlType += ")";
				sql += sqlType;

			}

			rows = getCurrentSession().createNativeQuery(sql).setParameter("distance", limit.getDistance())
					.setParameter("poLatitude", limit.getLat()).setParameter("poLongitude", limit.getLng()).list();

		} catch (RuntimeException re) {
			throw re;
		}
		return rows;
	}

	public String toString(Object object) {
		String s = Optional.ofNullable(object).map(Object::toString).orElse("null");
		return s;

	}

}
