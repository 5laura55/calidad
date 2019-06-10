package com.hiveag.geepy.dao;

import org.springframework.stereotype.Repository;

import com.hiveag.geepy.pojo.Permission;

@Repository
public class PermissionDAO extends AbstractDAO<Permission, Short> {

	public PermissionDAO() {
		// TODO Auto-generated constructor stub
		super(Permission.class);
	}

}
