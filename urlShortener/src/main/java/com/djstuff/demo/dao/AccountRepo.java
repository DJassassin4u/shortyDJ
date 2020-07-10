package com.djstuff.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.djstuff.demo.model.Account;

public interface AccountRepo extends JpaRepository<Account, Integer>{
	
	List<Account> findByAccountId(String accountId);

}
