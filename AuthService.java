package com.hiveag.geepy.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hiveag.geepy.dao.PersonDAO;
import com.hiveag.geepy.dao.RoleDAO;
import com.hiveag.geepy.pojo.Person;
import com.hiveag.geepy.pojo.Role;
import com.hiveag.geepy.util.CustomUserDetails;

@Service
public class AuthService implements UserDetailsService{
	
	private static final Logger log = LoggerFactory.getLogger(AuthService.class);
	
	@Autowired
	PersonDAO personDAO;
	
	@Autowired
	RoleDAO roleDAO;
	
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		log.info("Start login with Custom User Detail");
			
		Person person = personDAO.findByUsername(username);
		CustomUserDetails matchingUser = null;
		
		if(person == null){
			
			log.info("Wrong username or password");
			throw new UsernameNotFoundException("Wrong username or password");
			
		}else{
			
			matchingUser = new CustomUserDetails(person.getPeId(), person.getPeEMail(), person.getPePassword(), getRoles(person.getPeId()));
			log.info("Login done username: " + username);			
			return matchingUser;
			
		}		
	}
	
	
	public List<GrantedAuthority> getRoles(long peId){
		
		List<Role> roles = roleDAO.findRoles(peId);		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(roles.size());

		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getRoName()));
		}
		
		return authorities;
	}

}
