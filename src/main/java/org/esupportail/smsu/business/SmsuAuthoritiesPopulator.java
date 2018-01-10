package org.esupportail.smsu.business;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class SmsuAuthoritiesPopulator implements LdapAuthoritiesPopulator {
	
	@Autowired private SecurityManager securityManager;

	private final Logger logger = Logger.getLogger(getClass());

	@Override
	public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData,
			String username) {
		
		Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
		for(String roleName : securityManager.loadUserRightsByUsername(username)) {
			roles.add(new SimpleGrantedAuthority(roleName));
		}
		return roles;
	}
	
}

