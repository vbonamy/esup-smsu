package org.esupportail.smsu.web;

import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/* use our own version until we switch to spring 4 */
public class OptionalValidatorFactoryBean extends LocalValidatorFactoryBean {
	@Override
	public void afterPropertiesSet() {}
}
