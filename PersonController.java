package com.hiveag.geepy.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.sns.model.PublishResult;
import com.fasterxml.jackson.annotation.JsonView;
import com.hiveag.geepy.dao.PersonDAO;
import com.hiveag.geepy.dto.MessageDTO;
import com.hiveag.geepy.dto.PasswordDTO;
import com.hiveag.geepy.dto.PictureDTO;
import com.hiveag.geepy.pojo.Bike;
import com.hiveag.geepy.pojo.Geepy;
import com.hiveag.geepy.pojo.Person;
import com.hiveag.geepy.pojo.Picture;
import com.hiveag.geepy.pojo.PictureId;
import com.hiveag.geepy.pojo.Record;
import com.hiveag.geepy.service.BikeService;
import com.hiveag.geepy.service.GeepyService;
import com.hiveag.geepy.service.PersonService;
import com.hiveag.geepy.service.RecordService;
import com.hiveag.geepy.util.CustomUserChecker;
import com.hiveag.geepy.util.GestionArchivos;
import com.hiveag.geepy.util.View;

@Controller
public class PersonController {

	private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

	@Autowired
	PersonService personService;

	/**
	 * @author
	 * @param Person
	 *            person pePhNumber Checking which user is making the request.
	 * @return ResponseEntity The information for the client about the request.
	 */
	@RequestMapping(value = "/api/sms/password", method = RequestMethod.PATCH, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> solicitarSms(@RequestBody Person person) {
		MessageDTO entityMessage = new MessageDTO();
		person = personService.findByPhone(person.getPePhNumber());
		try {
			if (person.isPePhStatus()) {
				PublishResult publishResult = personService.sendSMS(person.getPeId());
				return new ResponseEntity<PublishResult>(publishResult, HttpStatus.OK);
			}
		} catch (Exception e) {
		}
		entityMessage.setMeCode(HttpStatus.NOT_FOUND);
		entityMessage.setMeMessage("User status is fail");
		return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NOT_FOUND);
	}

	/**
	 * @author
	 * @param Person
	 *            person pePhNumber valida el codigo sms
	 * @return ResponseEntity The information for the client about the request.
	 */
	@RequestMapping(value = "/api/sms/password", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	@ResponseBody

	public ResponseEntity<MessageDTO> checkSMSPassword(@RequestBody Person smsCode) {

		MessageDTO entityMessage = new MessageDTO();
		Person person = personService.findByPhone(smsCode.getPePhNumber());
		try {
			if (person.isPePhStatus()) {
				if (smsCode.getPeSmsCode() != null
						&& personService.checkSMSRecoverPassword(smsCode, person.getPeId())) {
					entityMessage.setMeCode(HttpStatus.OK);
					entityMessage.setMeMessage("User was verified");
					return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.OK);
				} else {

					entityMessage.setMeCode(HttpStatus.BAD_REQUEST);
					entityMessage.setMeMessage("Incorrect information, check the sms code and validity");
					return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.BAD_REQUEST);
				}
			}
		} catch (Exception e) {

		}
		entityMessage.setMeCode(HttpStatus.NOT_FOUND);
		entityMessage.setMeMessage("User does not exist yet");
		return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NOT_FOUND);
	}

	/**
	 * @author
	 * @param Person
	 *            person pePhNumber valida el codigo sms
	 * @return ResponseEntity The information for the client about the request.
	 */
	@RequestMapping(value = "/api/sms/recoverpassword", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	@ResponseBody
	@JsonView(View.Summary.class)
	public ResponseEntity<?> recoverPassword(@RequestBody Person smsCode) {

		MessageDTO entityMessage = new MessageDTO();
		logger.info(smsCode.getPePhNumber() + "  " + smsCode.getPeSmsCode());
		Person person = personService.findByPhone(smsCode.getPePhNumber());
		try {
			if (person.isPePhStatus()) {
				if (smsCode.getPeSmsCode() != null
						&& personService.checkSMSRecoverPassword(smsCode, person.getPeId())) {
					person = personService.recoverPassword(smsCode);

					return new ResponseEntity<Person>(person, HttpStatus.OK);
				} else {

					entityMessage.setMeCode(HttpStatus.BAD_REQUEST);
					entityMessage.setMeMessage("Incorrect information, check the sms code and validity");
					return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.BAD_REQUEST);
				}
			}
		} catch (Exception e) {

		}
		entityMessage.setMeCode(HttpStatus.NOT_FOUND);
		entityMessage.setMeMessage("User does not exist yet");
		return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NOT_FOUND);
	}

	/**
	 * @author
	 * @param PasswordDTO
	 *            currentPassword, newPassword valida la contrase�a actual con la
	 *            contrase�a de la base de datos, si son iguales permite almacenar
	 *            la nueva contrase�a
	 * @return ResponseEntity The information for the client about the request.
	 */
	@RequestMapping(value = "/api/person/changepassword", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	@ResponseBody
	@PreAuthorize("#person.peId== principal.peId ")
	public ResponseEntity<?> changePassword(@RequestBody PasswordDTO passwordDto, OAuth2Authentication authentication) {
		MessageDTO entityMessage = new MessageDTO();
		long peId = new CustomUserChecker().checker(authentication);
		Person person = personService.findById(peId);

		if (person == null) {
			entityMessage.setMeCode(HttpStatus.NOT_FOUND);
			entityMessage.setMeMessage("User does not exist yet");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NOT_FOUND);
		}

		if (!person.isPePhStatus()) {
			entityMessage.setMeCode(HttpStatus.CONFLICT);
			entityMessage.setMeMessage("phone number status false");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.CONFLICT);

		}

		boolean passwordChange = personService.changePassword(person, passwordDto);
		if (passwordChange == false ) {
			entityMessage.setMeCode(HttpStatus.CONFLICT);
			entityMessage.setMeMessage("password error is not similar");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.CONFLICT);
		}
		entityMessage.setMeCode(HttpStatus.OK);
		entityMessage.setMeMessage("password user was change");
		return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.OK);

	}

	@RequestMapping(value = "/api/person", method = RequestMethod.GET, produces = "application/json")
	@JsonView(View.Summary.class)
	@ResponseBody
	public ResponseEntity<?> getAll() {

		List<Person> person = personService.getAll();
		MessageDTO entityMessage = new MessageDTO();

		if (person == null) {
			entityMessage.setMeCode(HttpStatus.NO_CONTENT);
			entityMessage.setMeMessage("There are not users in the system");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<List<Person>>(person, HttpStatus.OK);

	}

	@RequestMapping(value = "/api/person/{id}", method = RequestMethod.GET, produces = "application/json")
	@JsonView(View.Summary.class)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") long id, OAuth2Authentication authentication) {
		MessageDTO entityMessage = new MessageDTO();
		long peId = new CustomUserChecker().checker(authentication);

		Person person = personService.findById(id);
		if (person == null) {
			entityMessage.setMeCode(HttpStatus.NOT_FOUND);
			entityMessage.setMeMessage("User does not exist yet");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Person>(person, HttpStatus.OK);

	}

	// @ModelAttribute("person") @Valid Person person, BindingResult result,
	@RequestMapping(value = "/api/person", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> create(@Valid @RequestBody Person person, BindingResult result) {
		MessageDTO entityMessage = new MessageDTO();

		if (result.hasErrors()) {
			entityMessage.setMeCode(HttpStatus.CONFLICT);
			entityMessage.setMeMessage("errors data");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.BAD_REQUEST);
		}

		if (personService.validate(person)) {
			entityMessage.setMeCode(HttpStatus.CONFLICT);
			entityMessage.setMeMessage("User exists already");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.CONFLICT);
		}
		person = personService.create(person);
		entityMessage.setMeCode(HttpStatus.CREATED);
		entityMessage.setMeMessage("User was created");
		return new ResponseEntity<Person>(person, HttpStatus.CREATED);

	}

	@RequestMapping(value = "/api/person", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	@ResponseBody
	@PreAuthorize("#person.peId== principal.peId  ")
	public ResponseEntity<MessageDTO> update(@RequestBody Person person, OAuth2Authentication authentication) {
		MessageDTO entityMessage = new MessageDTO();

		/*
		 * long peId = new CustomUserChecker().checker(authentication);
		 * 
		 * if(peId!=person.getPeId()) { return new
		 * ResponseEntity<MessageDTO>(entityMessage, HttpStatus.UNAUTHORIZED); }
		 */

		Person personDB = personService.findById(person.getPeId());
		if (personDB == null) {
			entityMessage.setMeCode(HttpStatus.NOT_FOUND);
			entityMessage.setMeMessage("User does not exist yet");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NOT_FOUND);

		}

		personService.update(person);
		entityMessage.setMeCode(HttpStatus.OK);
		entityMessage.setMeMessage("User was updated");
		return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.OK);

	}

	@RequestMapping(value = "/api/person/terms", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	@ResponseBody
	@PreAuthorize("#person.peId== principal.peId  ")
	public ResponseEntity<MessageDTO> updatePeTermTs(@RequestBody Person person) {
		MessageDTO entityMessage = new MessageDTO();

		person = personService.findById(person.getPeId());

		if (person != null) {
			personService.updateTerm(person);
			entityMessage.setMeCode(HttpStatus.OK);
			entityMessage.setMeMessage("PeTemrs attribute User was updated");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.OK);
		}
		entityMessage.setMeCode(HttpStatus.NOT_FOUND);
		entityMessage.setMeMessage("User does not exist yet");
		return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/api/person/phonenumber", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	@ResponseBody
	@PreAuthorize("#person.peId== principal.peId ")
	public ResponseEntity<MessageDTO> updatePhonenumber(@RequestBody Person person) {
		MessageDTO entityMessage = new MessageDTO();

		boolean bandera = personService.updatePhoneNumber(person);
		if (bandera) {
			entityMessage.setMeCode(HttpStatus.OK);
			entityMessage.setMeMessage("PeTemrs attribute User was updated");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.OK);
		}
		entityMessage.setMeCode(HttpStatus.NOT_FOUND);
		entityMessage.setMeMessage("User does not exist yet");
		return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NOT_FOUND);
	}

	/**
	 * @author Jorge D�az
	 * @param authentication
	 *            Checking which user is making the request.
	 * @return ResponseEntity The information for the client about the request.
	 */
	@RequestMapping(value = "/api/sms", method = RequestMethod.PATCH, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> sendSMS(OAuth2Authentication authentication) {

		MessageDTO entityMessage = new MessageDTO();
		long peId = new CustomUserChecker().checker(authentication);

		if (peId != 0) {

			PublishResult publishResult = personService.sendSMS(peId);
			return new ResponseEntity<PublishResult>(publishResult, HttpStatus.OK);

		}

		entityMessage.setMeCode(HttpStatus.NOT_FOUND);
		entityMessage.setMeMessage("User does not exist yet");

		return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NOT_FOUND);
	}

	/**
	 * @author Jorge D�az
	 * @param smsCode
	 *            Here the client must attach the sms code sent to the telephone
	 *            number registered.
	 * @param authentication
	 *            Checking what user is making the request.
	 * @return ResponseEntity The information for the client about the request.
	 */
	@RequestMapping(value = "/api/sms", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<MessageDTO> checkSMS(@RequestBody Person smsCode, OAuth2Authentication authentication) {

		MessageDTO entityMessage = new MessageDTO();
		long peId = new CustomUserChecker().checker(authentication);

		if (peId != 0) {

			if (smsCode.getPeSmsCode() != null && personService.checkSMS(smsCode, peId)) {

				entityMessage.setMeCode(HttpStatus.OK);
				entityMessage.setMeMessage("User was verified");
				return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.OK);

			} else {

				entityMessage.setMeCode(HttpStatus.BAD_REQUEST);
				entityMessage.setMeMessage("Incorrect information, check the sms code and validity");
				return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.BAD_REQUEST);

			}

		}

		entityMessage.setMeCode(HttpStatus.NOT_FOUND);
		entityMessage.setMeMessage("User does not exist yet");

		return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/api/person/{peId}/picture", method = RequestMethod.POST, consumes = {
			"application/octet-stream", "multipart/form-data" })
	public ResponseEntity<?> uploadPicturePerfil(@PathVariable("peId") Long peId,
			@RequestParam("file") MultipartFile file) {
		MessageDTO entityMessage = new MessageDTO();

		if (file.isEmpty()) {
			entityMessage.setMeCode(HttpStatus.CONFLICT);
			entityMessage.setMeMessage("la imagen no puede ser  vacia");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.CONFLICT);
		}

		if (!(file.getContentType().toLowerCase().equals("image/jpg")
				|| file.getContentType().toLowerCase().equals("image/jpeg")
				|| file.getContentType().toLowerCase().equals("image/png"))) {

			entityMessage.setMeCode(HttpStatus.CONFLICT);
			entityMessage.setMeMessage("solo se permiten imagenes en formato jpg o png");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.CONFLICT);
		}

		if (!GestionArchivos.validateSize(file.getSize())) {
			entityMessage.setMeCode(HttpStatus.CONFLICT);
			entityMessage.setMeMessage("la imagen no puede tener un tamaño superior a 5 MB");
			return new ResponseEntity<MessageDTO>(entityMessage, HttpStatus.CONFLICT);
		}

		personService.createPicture(peId, file);

		return new ResponseEntity("imagen almacenada de perfil se ha almacenado con exito", HttpStatus.OK);

	}

	@InitBinder
	public void initBinder(WebDataBinder binder, HttpServletRequest request) {
		String httpMethod = request.getMethod();
		if ("POST".equals(httpMethod)) {
			binder.setDisallowedFields("peHeight", "peWeight");
		}

	}

}
