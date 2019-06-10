package com.hiveag.geepy.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hiveag.geepy.dao.CatalogDAO;
import com.hiveag.geepy.pojo.Catalog;

@Service
public class CatalogService {

	private static final Logger logger = LoggerFactory.getLogger(CatalogService.class);

	@Autowired
	CatalogDAO catalogDAO;

	@Transactional
	public Catalog create(Catalog catalog) {
		catalogDAO.create(catalog);
		return catalog;
	}

	@Transactional
	public List<Catalog> getParents(int caId) {
		List<Catalog> parents = new ArrayList<Catalog>();

		Catalog catalog = catalogDAO.findById(caId);
		parents.add(catalog);
		Catalog parent = catalogDAO.getParent(catalog.getCaId());

		while (parent != null) {
			parents.add(parent);
			try {
				if (parent.getCaId() > 0) {
					parent = catalogDAO.getParent(parent.getCaId());
				} else {
					parent = null;
				}

			} catch (Exception e) {
				parent = null;
				logger.info("  not found parent catalog ");
			}

		}
		
		Collections.reverse(parents);
		return parents;

	}

	@Transactional
	public Catalog findById(int caId) {
		return catalogDAO.findById(caId);
	}

	@Transactional
	public List<Catalog> getAll() {
		return catalogDAO.findAll();
	}

	@Transactional
	public List<Catalog> getBrands() {
		return catalogDAO.getBrands();
	}

	@Transactional
	public List<Catalog> getChilds(int caId) {
		return catalogDAO.getChild(caId);
	}

}