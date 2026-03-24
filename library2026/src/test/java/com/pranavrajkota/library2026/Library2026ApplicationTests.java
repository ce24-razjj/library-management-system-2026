package com.pranavrajkota.library2026;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Library2026ApplicationTests {

//	@Test
//	void contextLoads() {
//	}

	public static class Test {
		public static void main(String[] args) {
			BCryptPasswordEncoder encoder1 = new BCryptPasswordEncoder();
			System.out.println(encoder1.encode("admin"));

			BCryptPasswordEncoder encoder2 = new BCryptPasswordEncoder();
			String raw = "admin";
			String hashFromDB = "<PASTE DB PASSWORD HERE>";
			System.out.println(encoder2.matches(raw, hashFromDB));
		}

	}



}
