package com.hiveag.geepy.dao;

import org.springframework.stereotype.Repository;

import com.hiveag.geepy.pojo.Authority;
import com.hiveag.geepy.pojo.AuthorityId;

@Repository
public class AuthorityDAO extends AbstractDAO<Authority, AuthorityId> {

	public AuthorityDAO() {
		// TODO Auto-generated constructor stub
		super(Authority.class);
	}

}
