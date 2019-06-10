
package com.hiveag.geepy.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.hiveag.geepy.dao.AuthorityDAO;
import com.hiveag.geepy.dao.PersonDAO;
import com.hiveag.geepy.dto.PasswordDTO;
import com.hiveag.geepy.pojo.Authority;
import com.hiveag.geepy.pojo.AuthorityId;
import com.hiveag.geepy.pojo.Person;
import com.hiveag.geepy.util.GestionArchivos;

@Service
public class PersonService {
	private  static final Logger logger = LoggerFactory.getLogger(PersonService.class);

	@Autowired
	PersonDAO personDAO;

	@Autowired
	AuthorityDAO authorityDAO;

	@Transactional
	public Person create(Person person) {
		person.setPePassword(new BCryptPasswordEncoder().encode(person.getPePassword()));

		String name = Character.toUpperCase(person.getPeName().charAt(0))
				+ person.getPeName().substring(1, person.getPeName().length());
		String lastName = Character.toUpperCase(person.getPeLastName().charAt(0))
				+ person.getPeLastName().substring(1, person.getPeLastName().length());
		person.setPeName(name);
		person.setPeLastName(lastName);

		personDAO.create(person);

		Authority authority = new Authority();
		AuthorityId authorityId = new AuthorityId();

		authorityId.setPeId(person.getPeId());
		authorityId.setRoId((short) 3);
		authority.setId(authorityId);
		authority.setAuTimestamp(new Date());

		authorityDAO.create(authority);
		logger.info("Create Person "+ person.getPeId());
		return person;
	}

	@Transactional
	public boolean changePassword(Person person, PasswordDTO passwordDTO) {
		BCryptPasswordEncoder bCryp = new BCryptPasswordEncoder();
		person = personDAO.findById(person.getPeId());
		if (bCryp.matches(passwordDTO.getCurrentPassword(), person.getPePassword())) {
			person.setPePassword(new BCryptPasswordEncoder().encode(passwordDTO.getNewPassword()));
			personDAO.update(person);
			logger.info("change password   "+ person.getPeId());
			return true;
			
		}

		return false;
	}

	@Transactional
	public Person findById(long peId) {
		return personDAO.findById(peId);
	}

	@Transactional
	public Person findByUsername(String peEMail) {
		return personDAO.findByUsername(peEMail);
	}

	@Transactional
	public List<Person> getAll() {
		return personDAO.findAll();
	}

	@Transactional
	public void update(Person person) {
		Person personDB = personDAO.findById(person.getPeId());
		personDB.setPeBirth(DateUtils.addDays(person.getPeBirth(), 1));
		String name = Character.toUpperCase(person.getPeName().charAt(0))
				+ person.getPeName().substring(1, person.getPeName().length());
		String lastName = Character.toUpperCase(person.getPeLastName().charAt(0))
				+ person.getPeLastName().substring(1, person.getPeLastName().length());
		personDB.setPeName(name);
		personDB.setPeLastName(lastName);
		personDB.setPeGenre(person.getPeGenre());
		personDB.setPeWeight(person.getPeWeight());
		personDB.setPeHeight(person.getPeHeight());
		personDB.setPeEMail(person.getPeEMail());
		logger.info("update info Person "+ person.getPeId());
		personDAO.update(personDB);
	}

	@Transactional
	public void updateTerm(Person person) {
		person = personDAO.findById(person.getPeId());
		if (person != null) {
			person.setPeTermTs(LocalDateTime.now());
			personDAO.update(person);
		}
	}

	@Transactional
	public boolean updatePhoneNumber(Person person) {
		Person personA = personDAO.findById(person.getPeId());
		Person personPhone = null;

		if (personA == null) {
			return false;
		}

		if (personA.getPePhNumber() != person.getPePhNumber()) {
			personPhone = personDAO.findByPhone(person.getPePhNumber());
		}

		if (personPhone == null) {
			personA.setPePhNumber((person.getPePhNumber()));
			personA.setLocation(person.getLocation());
			personDAO.update(personA);
			logger.info("update Person "+ personA.getPeId());
			return true;
		}

		return false;
	}

	@Transactional
	public boolean createPicture(long peId, MultipartFile file) {
		Person person = personDAO.findById(peId);
		if (person == null) {
			return false;
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String ext = GestionArchivos.getExtensionOfFile(file.getOriginalFilename());
		String pePicPath = peId + "_" + timestamp.getTime() + "." + ext;

		try {
			GestionArchivos.deleteFile(person.getPePicPath());
		} catch (Exception e) {
			logger.info("error delete picture person_peId   "+ peId);
		}

		try {
			GestionArchivos.saveUploadedFiles(pePicPath, file);
			person.setPePicPath(pePicPath);
			personDAO.update(person);

		} catch (IOException e) {
			return false;
		}

		return true;
	}

	@Transactional
	public boolean validate(Person person) {
		boolean r = false;
		Person a = personDAO.findByUsername(person.getPeEMail());
		if (a == null) {
			Person b = personDAO.findByPhone(person.getPePhNumber());

			if (b != null) {
				r = true;
			}

		} else {
			r = true;
		}

		return r;

	}

	@Transactional
	public boolean validatePhoneNumber(Person person) {
		Person a = personDAO.findByPhone(person.getPePhNumber());
		if (a == null) {
			return true;

		}
		return false;

	}

	@Transactional
	public boolean validateEmail(Person person) {
		Person a = personDAO.findByUsername(person.getPeEMail());
		if (a == null) {
			return true;

		}
		return false;

	}

	@Transactional
	public Person findByPhone(long pePhNumber) {
		Person person = personDAO.findByPhone(pePhNumber);
		return person;
	}

	@Transactional
	public PublishResult sendSMS(long peId) {

		Person person = personDAO.findById(peId);

		AWSCredentials awsCredentials = new BasicAWSCredentials("AKIARTQFUJBNWXZ65FNR",
				"kw11crEronucyXL7TqsunrvCdOmrXgViIncISFfd");
		ClientConfiguration cfg = new ClientConfiguration();
		AmazonSNS snsClient = AmazonSNSClient.builder().withRegion("us-west-2").withClientConfiguration(cfg)
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();

		short peSmsCode = (short) randomGen();
		String message = "Your geepy verification code is: " + peSmsCode + ". The code expires in two hours.";
		String phoneNumber = "+" + person.getLocation().getLoCode().replaceAll("\\s+", "") + person.getPePhNumber();
		Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
		smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue().withStringValue("Promotional") // Sets the
																											// type to
																											// transactional.
				.withDataType("String"));

		PublishResult publishResult = sendSMSMessage(snsClient, message, phoneNumber, smsAttributes);

		if (publishResult.getSdkHttpMetadata().getHttpStatusCode() == 200) {

			person.setPeSmsCode(peSmsCode);
			LocalDateTime currentTime = LocalDateTime.now();
			LocalDateTime peSmsExpiration = currentTime.plusHours(2);
			person.setPeSmsExpiration(peSmsExpiration);
			personDAO.update(person);

		}

		return publishResult;
	}

	@Transactional
	public boolean checkSMSRecoverPassword(Person smsCode, long peId) {
		Person person = personDAO.findById(peId);
		if (person.getPeSmsCode().equals(smsCode.getPeSmsCode())
				&& LocalDateTime.now().isBefore(person.getPeSmsExpiration())) {
			return true;
		}
		return false;
	}

	@Transactional
	public Person recoverPassword(Person smsCode) {
		Person person = personDAO.findByPhone(smsCode.getPePhNumber());
		if (person.getPeSmsCode().equals(smsCode.getPeSmsCode())
				&& LocalDateTime.now().isBefore(person.getPeSmsExpiration())) {
			person.setPeSmsCode(null);
			person.setPeSmsExpiration(null);

			person.setPePassword(new BCryptPasswordEncoder().encode(smsCode.getPePassword()));
			personDAO.update(person);
			return person;
		}
		return null;
	}

	@Transactional
	public boolean checkSMS(Person smsCode, long peId) {
		boolean b = false;
		Person person = personDAO.findById(peId);
		if (person.getPeSmsCode().equals(smsCode.getPeSmsCode())
				&& LocalDateTime.now().isBefore(person.getPeSmsExpiration())) {
			person.setPeSmsCode(null);
			person.setPeSmsExpiration(null);
			person.setPePhStatus(true);
			personDAO.update(person);
			b = true;
		}
		return b;
	}

	public static PublishResult sendSMSMessage(AmazonSNS snsClient, String message, String phoneNumber,
			Map<String, MessageAttributeValue> smsAttributes) {
		PublishResult result = snsClient.publish(new PublishRequest().withMessage(message).withPhoneNumber(phoneNumber)
				.withMessageAttributes(smsAttributes));
		return result;
	}

	/**
	 * @author Jorge D�az
	 * @return Generator of the random value for validating the phone number.
	 * @category Non transactional method
	 */
	public int randomGen() {
		int max = 9999;
		int min = 1000;
		Random random = new Random();
		int randomInteger = random.nextInt((max - min) + 1) + min;
		return randomInteger;
	}

}
