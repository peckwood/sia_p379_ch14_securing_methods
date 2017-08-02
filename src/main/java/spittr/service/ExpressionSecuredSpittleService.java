package spittr.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

import spittr.domain.Spitter;
import spittr.domain.Spittle;

public class ExpressionSecuredSpittleService implements SpittleService{

	//use case: only premium users can publish a spittle with text having more than 10 letters
	
	@Override
	//use "le" instead of "<=" is ok (le stands for [L]ess than or [E]qual to)
	//length() is a method, length will not work
	
	@PreAuthorize(" (hasRole('ROLE_USER') and #spittle.text.length() le 10) or hasRole('ROLE_PREMIUM')")
	public void addSpittle(Spittle spittle) {
		System.out.println("addSpittle executed");
	}

	//use case: users can only query spittles wrote by him, admin can query all
	
	@Override
	@Secured("ROLE_USER")
	//'returnObject', not '#returnObject' or '#result'
	//textbook is "== pricipal.username", which returns error: username is not a property of a String object
	@PostAuthorize("returnObject.spitter.username == principal")
	public Spittle getSpittleById(long id) {
		Spitter spitter = new Spitter(1L, "user1", "user1pass", "user1fullname", "user1@user1.com", true);
		Spittle spittle = new Spittle(88L, spitter, "hi guys!", new Date());
		return spittle;
	}

	//user case: this method return ALL offensive spittles, admin can see all, a user can only see his own
	@Override
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@PostFilter("filterObject.spitter.username == principal || hasRole('ROLE_ADMIN')")
	public List<Spittle> getOffensiveSpittles() {
		Spitter spitter1 = new Spitter(1L, "user1", "user1pass", "user1fullname", "user1@user1.com", true);
		Spitter spitter2 = new Spitter(2L, "user2", "user2pass", "user2fullname", "user2@user2.com", true);
		Spittle spittle1 = new Spittle(89L, spitter1, "Check out this adult-only website: xxx.com!", new Date());
		Spittle spittle2 = new Spittle(90L, spitter1, "The government is assassinating some anti-government people", new Date());
		Spittle spittle3 = new Spittle(91L, spitter2, "The places that you can buy drugs...", new Date());
		List<Spittle> offensiveSpittles = new ArrayList<>();
		offensiveSpittles.add(spittle1);
		offensiveSpittles.add(spittle2);
		offensiveSpittles.add(spittle3);
		return offensiveSpittles;
	}
	
	//user case: this method deletes spittles, only admin can delete all passed as parameters, a user can only delete his own
	@Override
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	//Version 1: expression
	//@PreFilter("filterObject.spitter.username == principal || hasRole('ROLE_ADMIN')")
	
	//Version 2 : custom permission evaluator
	@PreFilter("hasPermission(filterObject, 'delete')")
	public void deleteSpittles(List<Spittle> spittles) {
		System.out.println("spittles deleted: ");
		System.out.println(spittles);
	}
}
