package spittr.security;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import spittr.domain.Spittle;

public class SpittlePermissionEvaluator implements PermissionEvaluator{

	private static final GrantedAuthority ADMIN_AUTHORITY = new GrantedAuthorityImpl("ROLE_ADMIN");
	
	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		if(targetDomainObject instanceof Spittle){
			Spittle spittle = (Spittle) targetDomainObject;
			String username = spittle.getSpitter().getUsername();
			if("delete".equals(permission)){
				return isAdmin(authentication) || username.equals(authentication.getName());
			}
		}
		throw new UnsupportedOperationException("hasPermission not supported for object <" + targetDomainObject
				+ "> and permission <" + permission + ">");
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		throw new UnsupportedOperationException();
	}
	
	private boolean isAdmin(Authentication authentication){
		return authentication.getAuthorities().contains(ADMIN_AUTHORITY);
	}
}
