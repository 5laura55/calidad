package com.hiveag.geepy.dao;

import org.springframework.stereotype.Repository;

import com.hiveag.geepy.pojo.Assignment;
import com.hiveag.geepy.pojo.AssignmentId;

@Repository
public class AssignmentDAO extends AbstractDAO<Assignment, AssignmentId>{

	public AssignmentDAO() {
		// TODO Auto-generated constructor stub
		super(Assignment.class);
	}

}
