package com.djstuff.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.djstuff.demo.model.Urlpair;

public interface UrlPairRepo extends JpaRepository<Urlpair, Integer>{
	
	List<Urlpair> findByOriginal(String original);
	
	List<Urlpair> findByShorter(String shorter);
	
	List<Urlpair> findById(int id);
	
}
