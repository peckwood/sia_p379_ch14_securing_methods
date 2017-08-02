package spittr.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import spittr.config.ExpressionSecuredConfig;
import spittr.config.SecuredConfig;
import spittr.domain.Spitter;
import spittr.domain.Spittle;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ExpressionSecuredConfig.class})
public class ExpressionSecuredSpittleServiceTest {
	@Autowired
	private SpittleService spittleService;

	@Before
	public void clearContext() {
		SecurityContextHolder.clearContext();
	}

	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	public void testSecuredMethod_noCredentials() {
		Spitter spitter = new Spitter(1L, "habuma", null, "Craig Walls", "craig@habuma.com", true);
		Spittle spittle = new Spittle(1L, spitter, "", new Date());
		spittleService.addSpittle(spittle);
	}

	//use case: only premium users can publish a spittle with text having more than 10 letters
	@Test(expected = AccessDeniedException.class)
	public void testSecuredMethod_InsufficientPrivilege_SpittleTooLong() {
		setupUser("ROLE_USER");
		
		Spitter spitter = new Spitter(1L, "habuma", null, "Craig Walls", "craig@habuma.com", true);
		Spittle spittle = new Spittle(1L, spitter, "this spittle has more than 10 letters", new Date());
		spittleService.addSpittle(spittle);
	}
	
	//use case: only premium users can publish a spittle with text having more than 10 letters
	@Test
	public void testSecuredMethod_SufficientPrivilege_PremiumUser() {
		setupUser("ROLE_PREMIUM");
		
		Spitter spitter = new Spitter(1L, "habuma", null, "Craig Walls", "craig@habuma.com", true);
		Spittle spittle = new Spittle(1L, spitter, "this spittle has more than 10 letters", new Date());
		spittleService.addSpittle(spittle);
	}
	
	@Test
	public void testSecuredMethod_SufficientPrivilege_QueryOwnSpittle() {
		//username is "user1"
		setupUserWithUsername("user1", "ROLE_USER");
		spittleService.getSpittleById(88L);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void testSecuredMethod_InsufficientPrivilege_QueryOthersSpittle() {
		//username is "user2"
		setupUserWithUsername("user2", "ROLE_USER");
		spittleService.getSpittleById(88L);
	}
	//user case: this method return ALL offensive spittles, admin can see all, a user can only see his own
	@Test
	public void testSecuredMethod__QueryOwnOffensiveSpittles() {
		//username is "user1"
		setupUserWithUsername("user1", "ROLE_USER");
		//note that user2's are not shown
		System.out.println("user1 sees: ");
		System.out.println(spittleService.getOffensiveSpittles());
	}
	
	//user case: this method deletes spittles, only admin can delete all passed as parameters, a user can only delete his own
	@Test
	public void testSecuredMethod__DeleteOwnSpittles() {
		//username is "user1"
		setupUserWithUsername("user1", "ROLE_USER");
		
		Spitter spitter1 = new Spitter(1L, "user1", "user1pass", "user1fullname", "user1@user1.com", true);
		Spitter spitter2 = new Spitter(2L, "user2", "user2pass", "user2fullname", "user2@user2.com", true);
		Spittle spittle1 = new Spittle(89L, spitter1, "Check out this adult-only website: xxx.com!", new Date());
		Spittle spittle2 = new Spittle(90L, spitter1, "The government is assassinating some anti-government people", new Date());
		Spittle spittle3 = new Spittle(91L, spitter2, "The places that you can buy drugs...", new Date());
		List<Spittle> spittles = new ArrayList<>();
		spittles.add(spittle1);
		spittles.add(spittle2);
		spittles.add(spittle3);
		
		//note that user2's are not deleted
		spittleService.deleteSpittles(spittles);
	}
	
	@Test
	public void testSecuredMethod__QueryOfensiveSpittlesAsAdmin() {
		//user is admin
		setupUser("ROLE_ADMIN");
		//note that user2's are not shown
		System.out.println("admin sees: ");
		System.out.println(spittleService.getOffensiveSpittles());
	}
	
	
	
	private void setupUserWithUsername(String username, String... privs) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (String priv : privs) {
			authorities.add(new SimpleGrantedAuthority(priv));
		}
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				"password", authorities);
		securityContext.setAuthentication(authenticationToken);
	}
	private void setupUser(String... privs) {
		setupUserWithUsername("user", privs);
	}
	
}
