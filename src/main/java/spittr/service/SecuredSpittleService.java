package spittr.service;

import java.util.List;

import org.springframework.security.access.annotation.Secured;

import spittr.domain.Spittle;

public class SecuredSpittleService implements SpittleService{

	@Override
	@Secured({"ROLE_ADMIN, ROLE_USER"})
	public void addSpittle(Spittle spittle){
		System.out.println("addSpittle executed");
	}

	@Override
	public Spittle getSpittleById(long id) {
		return null;
	}
	@Override
	public List<Spittle> getOffensiveSpittles() {
		return null;
	}
	@Override
	public void deleteSpittles(List<Spittle> spittles) {
	}
}
