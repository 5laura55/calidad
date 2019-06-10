package com.hiveag.geepy.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amazonaws.util.IOUtils;
import com.hiveag.geepy.dao.PictureDAO;
import com.hiveag.geepy.pojo.Picture;
import com.hiveag.geepy.pojo.PictureId;
import com.hiveag.geepy.util.GestionArchivos;

@Service
public class PictureService {

	private  static final Logger logger = LoggerFactory.getLogger(PictureService.class);
	@Autowired
	PictureDAO pictureDAO;

	@Autowired
	ServletContext context;

	@Transactional
	public List<Picture> getAll() {
		return pictureDAO.findAll();
	}

	@Transactional
	public List<Picture> findByBiId(Long biId) {
		return pictureDAO.findByBiId(biId);
	}

	@Transactional
	public Picture findById(PictureId id) {
		return pictureDAO.findById(id);
	}

	@Transactional
	public Picture findSinglePictureByBiId(Long biId) {
		return pictureDAO.findSinglePictureByBiId(biId);
	}

	@Transactional
	public boolean deletePicture(Picture picture) {
		try {

			GestionArchivos.deleteFile(picture.getPiPath());
			pictureDAO.delete(picture);

			return true;
		} catch (Exception e) {
			logger.info("error delete file");
		}

		return false;

	}

	@Transactional
	public boolean create(Picture picture) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		picture.setPiTimestamp(new Date());
		String ext = GestionArchivos.getExtensionOfFile(picture.getFile().getOriginalFilename());
		picture.setPiPath(
				picture.getId().getBiId() + "_" + picture.getId().getPiSlot() + "_" + timestamp.getTime() + "." + ext);

		try {

			GestionArchivos.saveUploadedFiles(picture.getPiPath(), picture.getFile());
			pictureDAO.create(picture);

		} catch (IOException e) {
			logger.info("error create file");
			return false;
		}

		return true;
	}

	public short getSlot(List<Picture> pictures) {
		for (short i = 1; i <= 4; i++) {
			if (!containsSlot(pictures, i))
				return i;

		}

		return 0;

	}

	public @ResponseBody byte[] getImageWithMediaType(String pathId) throws IOException {
		File file = new File(GestionArchivos.getUPLOADED_FOLDER() + pathId);
		InputStream in = FileUtils.openInputStream(file);
		return IOUtils.toByteArray(in);
	}

	public boolean containsSlot(final List<Picture> pictures, short piSlot) {
		return pictures.stream().filter(p -> p.getId().getPiSlot() == piSlot).findFirst().isPresent();
	}

}
