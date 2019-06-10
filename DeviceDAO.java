package com.hiveag.geepy.dao;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hiveag.geepy.pojo.Device;
import com.hiveag.geepy.pojo.Person;

@Repository
public class DeviceDAO extends AbstractDAO<Device, Long> {

	public DeviceDAO() {
		// TODO Auto-generated constructor stub
		super(Device.class);
	}

	@SuppressWarnings("unchecked")
	public List<Device> findByPersonId(long peId) {
		log.debug("Getting by PeId " + DeviceDAO.class + " instance");

		List<Device> devices = null;
		try {
			devices = getCurrentSession().createQuery("from Device d where d.person.peId = :peId")
					.setParameter("peId", peId).getResultList();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return devices;
	}
	
	
	/**/
	@SuppressWarnings("unchecked")
	public List<Device> findDevicesLogInForeGround(long peId) {
		List<Device> devices = null;
		try {
			devices = getCurrentSession()
					.createQuery("from Device d where "
					+ "            (d.deOs='A' or d.deOs='I')       "
					+ "            and   d.person.peId = :peId "
					+ "            and d.deRegId not like 'logout' "
					+ "            and d.deStatus='F'"
					 )
					.setParameter("peId", peId).getResultList();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return devices;
	}
	@SuppressWarnings("unchecked")
	public List<Device> findDevicesLogIn(long peId) {
		List<Device> devices = null;
		try {
			devices = getCurrentSession()
					.createQuery("from Device d where "
					+ "            (d.deOs='A' or d.deOs='I')       "
					+ "            and   d.person.peId = :peId "
					+ "            and d.deRegId not like 'logout' "
					 )
					.setParameter("peId", peId).getResultList();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return devices;
	}

	@SuppressWarnings("unchecked")
	public Device findByDeId(long deId) {
		log.debug("Getting by deId " + DeviceDAO.class + " instance");

		Device device = null;

		try {
			device = (Device) getCurrentSession().createQuery("from Device d where d.deId = :deId")
					.setParameter("deId", deId).getSingleResult();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return device;

	}

	@SuppressWarnings("unchecked")
	public Device findByDeDevId(String deDevId) {
		log.debug("Getting by deDevId " + DeviceDAO.class + " instance");
		Device device = null;
		try {
			device = (Device) getCurrentSession().createQuery("from Device d where d.deDevId like :deDevId")
					.setParameter("deDevId", deDevId).getResultList().get(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return device;
	}

	public int updateDevice(Device device) {
		
		final int changes;
		try {
			getCurrentSession().getTransaction().begin();
			changes =getCurrentSession()
					.createNativeQuery("  update device set pe_id=:peId ," + "    de_reg_id=:deRegId, "
							+ "    de_app_version=:deAppVersion, " + "    de_os=  :deOs, "
							+ "    de_os_version=:deOsVersion," + "    de_last_logon=:deLastLogon             "
							+ " where de_dev_id=:deDevId ")
					.setParameter("peId", device.getPerson().getPeId()).setParameter("deDevId", device.getDeDevId())
					.setParameter("deRegId", device.getDeRegId()).setParameter("deAppVersion", device.getDeAppVersion())
					.setParameter("deOs", device.getDeOs()).setParameter("deOsVersion", device.getDeOsVersion())
					.setParameter("deLastLogon", device.getDeLastLogon()).executeUpdate();
			
			getCurrentSession().getTransaction().commit();
			getCurrentSession().close();
			
		} catch (RuntimeException re) {
			throw re;
		}
		return changes;
	}

}