package org.esupportail.smsu.business;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.esupportail.commons.services.ldap.LdapUser;
import org.esupportail.smsu.exceptions.ldap.LdapUserNotFoundException;
import org.esupportail.smsu.services.client.SmsuapiWS;
import org.esupportail.smsu.services.ldap.LdapUtils;
import org.esupportail.smsu.services.smtp.SmtpServiceUtils;
import org.esupportail.smsuapi.utils.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

/**
 * Business layer concerning smsu notification invalid mail.
 *
 */
public class NotificationByMailForInvalidPhoneManager {
	
	@Autowired private MessageSource messageSource;
	@Autowired private LdapUtils ldapUtils;
	@Autowired private SmtpServiceUtils smtpServiceUtils;
	@Autowired private SmsuapiWS smsuapiWS;

	private final Logger logger = Logger.getLogger(getClass());
		
	/**
	 * Get list of phone numbers in blacklist.
	 * @return return set of phone numbers 
	 * @throws HttpException 
	 */
	private Set<String> getListePhoneNumberInBlackList() throws HttpException {
		return smsuapiWS.getListPhoneNumbersInBlackList(); 
	}
	
	/**
	 * retrieve mail based on uid.
	 * @param uid
	 * @return
	 */
	private String getMail(final String uid) {
		if (logger.isDebugEnabled()) {
			logger.debug("Get the mail for user:  " + uid);
		}
		String mail = null;
		try {
			mail = ldapUtils.getUserEmailAdressByUid(uid);
			if (logger.isDebugEnabled()) {
				logger.debug("The mail is:  " + mail);
			}
		} catch (LdapUserNotFoundException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage());
			}
		}
		
		return mail;
	}

	///////////////////////////////////////
	//  Public method
	//////////////////////////////////////
	/**
	 * sendMails method called by Quartz task.
	 * @return
	 * @throws HttpException 
	 */
	public void sendMails() throws HttpException {
		if (logger.isDebugEnabled()) {
			logger.debug("Enter to sendMails method");
		}
		// 1 - Retrieve the list of phone numbers in blacklist 
		Set<String> listPhones = getListePhoneNumberInBlackList();
		
			for (String phoneNumber : listPhones) {
				if (StringUtils.isEmpty(phoneNumber)) // ensure weird numbers are discarded
					continue;

				if (logger.isDebugEnabled()) {
					logger.debug("search ldapUser for phone number:" + phoneNumber);
				}
			// 2 - Retrieve the ldapUser 	
			List<LdapUser>  list = ldapUtils.searchLdapUsersByPhoneNumber(phoneNumber);
		if (list.size() == 1) {
    		String uid = list.get(0).getId();
    		String mail = getMail(uid);
			    		if (mail != null) {
			    		// 3 - Send mail
			        	String subject = messageSource.getMessage("MSG.SUBJECT.MAIL.TO.INVALIDPHONE", new String[] {}, Locale.getDefault());
			        	
			    		String textBody = messageSource.getMessage("MSG.TEXTBOX.MAIL.TO.INVALIDPHONE", new String[] {phoneNumber}, Locale.getDefault());
			        	
			        	smtpServiceUtils.sendOneMessage(mail, subject, textBody);
			    		} else {
			    		logger.error("no mail for blacklisted uid " + uid + " with phone number " + phoneNumber);
			    		}
    		} else {
    		logger.info("no user found for once-invalid-so-blacklisted phone number: " + phoneNumber);
    		}
			}	
	}

}
