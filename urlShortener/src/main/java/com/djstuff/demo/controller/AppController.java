package com.djstuff.demo.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.djstuff.demo.dao.AccountRepo;
import com.djstuff.demo.dao.UrlPairRepo;
import com.djstuff.demo.model.Account;
import com.djstuff.demo.model.Urlpair;

@RestController
public class AppController {
	
	
	@Autowired
	UrlPairRepo repo;
	@Autowired
	AccountRepo users;
	
	public static String authtoken = "abcde";
	
	@PostMapping(path="/administration/register", consumes= {"application/json"}, produces= {"application/json"}) 
	public Map<String, String> makeNewUser(@RequestBody Account user) {
		HashMap<String, String> map = new HashMap<>();
		
		Account acc = new Account();
		
		if(!users.findByAccountId(user.getAccountId()).isEmpty()) {
			map.put("success", "false");
			map.put("description", "Account ID already exists");
			return map;
		}
		
		acc.setAccountId(user.getAccountId());
		acc.setId((int)users.count()+1);
		String pass = genRng(6);
		acc.setPassword(pass);
		users.save(acc);
		map.put("success", "true");	
		map.put("password", pass);	
		return map;
	}
	
	@PostMapping(path="/administration/short", consumes= {"application/json"}, produces= {"application/json"})
	public Map<String, String> makeShorty(HttpServletRequest request, @RequestBody Urlpair url) {
		HashMap<String, String> map = new HashMap<>();
		
		String token;
		try {
			String authHeader = request.getHeader("authorization");
			token = authHeader.substring(authHeader.indexOf(' ')+1);
		} catch (Exception e) {
			map.put("description","failed because authentication token wasn't provided");
			return map;
		}
//		System.out.println(token);
		if(!token.equals(authtoken)) {
			map.put("description","failed because provided authentication token is incorrect ");
			return map;
		}
		
		if(!repo.findByOriginal(url.getOriginal()).isEmpty()) {
//			System.out.println("već postoji");
			map.put("description", "failed because that link already has shorty");
			return map;
		}	
				
		Urlpair urlrez = new Urlpair();
		
		String shorterHashCode = Integer.toString(Math.abs((url.getOriginal().hashCode() % Short.MAX_VALUE)));
		urlrez.setShorter(shorterHashCode);
		urlrez.setId((int)repo.count()+1);
		urlrez.setOriginal(url.getOriginal());
//		System.out.println(urlrez);
		repo.save(urlrez);
		map.put("shortURL", urlrez.getShorter());
		return map;
	}
	
	@RequestMapping(value = "/{id}")
	public void redirectToUrl(@PathVariable String id, HttpServletResponse resp) {
//		System.out.println(id);
        if(repo.findByShorter("localhost:8080/"+id).isEmpty()) {
        	return;
        } else {
//        	System.out.println("redirecting");
        	resp.addHeader("Location", repo.findByShorter("localhost:8080/"+id).get(0).getOriginal());
        	resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        }
        
    }
	
	@GetMapping(path="/administration/statistics", produces= {"application/json"})
	public Map<String, String> statistics(HttpServletRequest request) {
		HashMap<String, String> map = new HashMap<>();
		
		String encodedAuth;
		String decodedAuth;
		try {
			String authHeader = request.getHeader("authorization");
			encodedAuth = authHeader.substring(authHeader.indexOf(' ')+1);
			decodedAuth = new String(Base64.getDecoder().decode(encodedAuth));
		} catch (Exception e) {
			map.put("description","failed because authentication info wasn't provided or wrong kind was provided");
			return map;
		}
		String username = decodedAuth.substring(0, decodedAuth.indexOf(':'));
		String password = decodedAuth.substring(decodedAuth.indexOf(':')+1);
		
//		System.out.println(username + " " + password);
//		System.out.println(users.findByAccountId(username).get(0).getAccountId() + " " +users.findByAccountId(username).get(0).getPassword());
		
		if(users.findByAccountId(username).isEmpty()) {
			map.put("description","failed because that account ID doesn't exist");
			return map;
		} else if(!password.equals(users.findByAccountId(username).get(0).getPassword())) {
			map.put("description","failed because password is incorrect");
			return map;
		}		
		
		for(int i = 0; i < repo.count(); i++) {
			map.put(repo.findAll().get(i).getOriginal(), repo.findAll().get(i).getShorter());
		}
		return map;
	}
	
	public static String genRng(int size)
	{
		Random rng = new Random();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < size; i++) {
			char znak =(char) ('0' + rng.nextInt(74));
			if(Character.isDigit(znak) || Character.isLetter(znak)) {
				sb.append(znak);
			} else {
				i--;
			}
		}
	    return sb.toString();
	}
}
