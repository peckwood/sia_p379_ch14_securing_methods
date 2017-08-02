package spittr.service;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.security.access.annotation.Secured;

import spittr.domain.Spittle;

public class JSR250SpittleService implements SpittleService{

	@Override
	@RolesAllowed({"ROLE_ADMIN, ROLE_USER"})
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
