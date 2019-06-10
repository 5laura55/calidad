package com.hiveag.geepy.service;

import javax.transaction.Transactional;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hiveag.geepy.dao.AuthorityDAO;
import com.hiveag.geepy.dao.DeviceDAO;
import com.hiveag.geepy.pojo.Device;

@Service
public class DeviceService {
	

	private  static final Logger logger = LoggerFactory.getLogger(DeviceService.class);
	
	@Autowired
	DeviceDAO deviceDAO;
	@Autowired
	AuthorityDAO authorityDAO;
	
	@Transactional
	public List<Device> findByIdPerson(long peId){
		return deviceDAO.findByPersonId(peId);
	}
	@Transactional
	public Device findByIdDevice(long deId){
		return deviceDAO.findById(deId);
	}
	
	@Transactional
	public Device findByDeDevId(String deDevId){
		return deviceDAO.findByDeDevId(deDevId);
	}
	
	@Transactional
	public List<Device> findDevicesLogIn(long peId){
		return deviceDAO.findDevicesLogIn(peId);
	}
	
	@Transactional
	public List<Device> findDevicesLogInForeGround(long peId){
		return deviceDAO.findDevicesLogInForeGround(peId);
	}
	
	
	
	@Transactional
	public void create(Device device){
		Device deviceDB=findByDeDevId(device.getDeDevId());
		device.setDeStatus('F');
		
		if(deviceDB==null)	
		{   deviceDAO.create(device);
	     	logger.info("  Create new device with regID    "+device.getDeRegId());
		}	
		else
			{  deviceDAO.delete(deviceDB);
			   deviceDAO.create(device);
			   logger.info("  Update device with regID    "+device.getDeRegId());
			}
		
	}
	
	@Transactional
	public Device updateRegId(Device device){
		 Device deviceBD=findByDeDevId(device.getDeDevId());
		 deviceBD.setDeRegId("logout");
		 deviceDAO.update(deviceBD);	
		 logger.info("  device logout devId"+deviceBD.getDeId());
		 return deviceBD;
	}
	
	@Transactional
	public Device updateStatus(Device device){
		Device deviceBD = findByIdDevice(device.getDeId());
		deviceBD.setDeStatus(device.getDeStatus());
		 deviceDAO.update(deviceBD);	
		 logger.info("  device " + +deviceBD.getDeId()+" status "+ deviceBD.getDeStatus());
		 return deviceBD;
	}
	
	

}