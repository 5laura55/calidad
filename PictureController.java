package com.hiveag.geepy.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.hiveag.geepy.dto.MessageDTO;
import com.hiveag.geepy.dto.PictureDTO;

import com.hiveag.geepy.exception.ResourceNotFoundException;
import com.hiveag.geepy.pojo.Picture;
import com.hiveag.geepy.pojo.PictureId;
import com.hiveag.geepy.service.PictureService;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hiveag.geepy.util.GestionArchivos;
import com.hiveag.geepy.util.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PictureController {

	@RequestMapping(value = "/api/bike/{biId}/picture/form")
	@ResponseBody
	public ModelAndView vista() {
		return new ModelAndView("emnify");
	}

	@Autowired
	PictureService pictureService;

	@RequestMapping(value = "/api/bike/{biId}/picture", method = RequestMethod.GET)
	@JsonView(View.Summary.class)
	@ResponseBody
	public ResponseEntity<?> getByBiId(@PathVariable("biId") long biId) {
		List<Picture> pictures = pictureService.findByBiId(biId);
		if (pictures == null) {
			throw new ResourceNotFoundException(biId);
		}
		return new ResponseEntity<List<Picture>>(pictures, HttpStatus.OK);
	}
	
	
    @GetMapping(value = "/api/bike/{biId}/picture/first", produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
	 public @ResponseBody byte[]  getByBiIdFirst(@PathVariable("biId") long biId) throws IOException  {
		List<Picture> pictures = pictureService.findByBiId(biId);
		if (pictures == null) {
			throw new ResourceNotFoundException(biId);
		}
		 try { 
			 return pictureService.getImageWithMediaType(pictures.get(0).getPiPath());
		 }
		 catch(IOException e) {}
		   return null;
	}
   
    @RequestMapping(value = "/api/bike/{biId}/picture/firstpath", method = RequestMethod.GET)
	@JsonView(View.Summary.class)
	@ResponseBody
	public ResponseEntity<?> getByBiIdPictureJson(@PathVariable("biId") long biId) {
    	MessageDTO entityMessage = new MessageDTO();
    	List<Picture> pictures = pictureService.findByBiId(biId);
    	try {
    		return new ResponseEntity<Picture>(pictures.get(0), HttpStatus.OK);
    	}catch(Exception e) {
    		entityMessage.setMeCode(HttpStatus.NOT_FOUND);
			entityMessage.setMeMessage("not found pictures ");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NOT_FOUND);	
    	}
    	
    	
		
		
	}
    
   

	
	@RequestMapping(value = "/api/picture", method = RequestMethod.POST, consumes = { "application/octet-stream",
			"multipart/form-data" })
	public ResponseEntity<?> multiUploadFileModel(@ModelAttribute PictureDTO pictureDTO, HttpServletRequest request) {
		MessageDTO entityMessage = new MessageDTO();

		Picture picture = new Picture();
		PictureId id = new PictureId();
		id.setBiId(pictureDTO.getBiId());
		id.setPiSlot(pictureDTO.getPiSlot());
		
		picture.setId(id);
		picture.setFile(pictureDTO.getFile());

		if (picture.getFile().isEmpty()) {
			entityMessage.setMeCode(HttpStatus.CONFLICT);
			entityMessage.setMeMessage("la imagen no puede ser  vacia");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.CONFLICT);
		}

		if (!(picture.getFile().getContentType().toLowerCase().equals("image/jpg")
				|| picture.getFile().getContentType().toLowerCase().equals("image/jpeg")
				|| picture.getFile().getContentType().toLowerCase().equals("image/png"))) {

			entityMessage.setMeCode(HttpStatus.CONFLICT);
			entityMessage.setMeMessage("solo se permiten imagenes en formato jpg o png");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.CONFLICT);
		}

		

		if (!GestionArchivos.validateSize(picture.getFile().getSize())) {
			entityMessage.setMeCode(HttpStatus.CONFLICT);
			entityMessage.setMeMessage("la imagen no puede tener un tamaño superior a 5 MB");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.CONFLICT);
		}

		List<Picture> pictures = null;
		pictures = pictureService.findByBiId(picture.getId().getBiId());
		int piSlot =1;
		if(pictures!=null)
         {   piSlot = pictureService.getSlot(pictures);
         }


		if (pictures.size()>=4) {
			entityMessage.setMeCode(HttpStatus.CONFLICT);
			entityMessage.setMeMessage("ha excedido el número maximo de 4 imagenes ");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.CONFLICT);
		}

		
		picture.getId().setPiSlot((short) piSlot);
		
		pictureService.create(picture);
		entityMessage.setMeCode(HttpStatus.OK);
		entityMessage.setMeMessage("imagen almacendada con exito");

		return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.OK);

	}

	

	@RequestMapping(value = "/api/picture", method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> deletePicture(@RequestParam("biId") Long biId, @RequestParam("piSlot") short piSlot) {
		MessageDTO entityMessage = new MessageDTO();

		PictureId id = new PictureId();
		id.setBiId(biId);
		id.setPiSlot(piSlot);
		Picture picture = new Picture();
		picture.setId(id);

		picture = pictureService.findById(picture.getId());

		if (picture == null) {
			entityMessage.setMeCode(HttpStatus.CONFLICT);
			entityMessage.setMeMessage("la imagen no existe");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.CONFLICT);
		}

		if (pictureService.deletePicture(picture)) {
			entityMessage.setMeCode(HttpStatus.OK);
			entityMessage.setMeMessage("la imagen  ha sido eliminada");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.OK);

		}
		entityMessage.setMeCode(HttpStatus.CONFLICT);
		entityMessage.setMeMessage("error al eliminar la imagen ");
		return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.CONFLICT);

	}
	
	
	
	 @GetMapping(value = "/img/{pathId:.+}", produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
	    public @ResponseBody byte[] getImageWithMediaType(@PathVariable("pathId") String pathId) throws IOException {
		 
		 try { 
			 return pictureService.getImageWithMediaType(pathId);
		 }
		 catch(IOException e) {}
		
		   return null;
	    }
	
	
	

	

	

}
