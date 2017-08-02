package spittr.service;

import java.util.List;

import spittr.domain.Spittle;

public interface SpittleService {

	void addSpittle(Spittle spittle);

	Spittle getSpittleById(long id);

	List<Spittle> getOffensiveSpittles();

	void deleteSpittles(List<Spittle> spittles);
}