package com.hiveag.geepy.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amazonaws.services.sns.model.PublishResult;
import com.fasterxml.jackson.annotation.JsonView;
import com.hiveag.geepy.dto.MessageDTO;
import com.hiveag.geepy.dto.PasswordDTO;
import com.hiveag.geepy.pojo.Person;
import com.hiveag.geepy.service.PersonService;
import com.hiveag.geepy.util.CustomUserChecker;
import com.hiveag.geepy.util.View;


@Controller
public class TestOscarController {

	@Autowired
	PersonService personService;

	/**
	 * @author
	 * @param Person
	 *            person pePhNumber Checking which user is making the request.
	 * @return ResponseEntity The information for the client about the request.
	 */
	@RequestMapping(value = "/api/sms/password", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
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
	@RequestMapping(value = "/api/sms/validate/password", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
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
	 *            person pePhNumber valida el codigo sms y almacena la nueva contraseña
	 * @return ResponseEntity The information for the client about the request.
	 */
	@RequestMapping(value = "/api/sms/recoverpassword", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	@JsonView(View.Summary.class)
	public ResponseEntity<?> recoverPassword(@RequestBody Person smsCode) {

		MessageDTO entityMessage = new MessageDTO();

		System.out.println(smsCode.getPePhNumber() + "  " + smsCode.getPeSmsCode() + "   " + smsCode.getPePassword());
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
	 * @author Jorge Dï¿½az
	 * @param authentication
	 *            Checking which user is making the request.
	 * @return ResponseEntity The information for the client about the request.
	 */
	@RequestMapping(value = "/api/sms", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
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
	 * @author Jorge Dï¿½az
	 * @param smsCode
	 *            Here the client must attach the sms code sent to the telephone
	 *            number registered.
	 * @param authentication
	 *            Checking what user is making the request.
	 * @return ResponseEntity The information for the client about the request.
	 */
	@RequestMapping(value = "/api/sms/validate", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
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

	
}
