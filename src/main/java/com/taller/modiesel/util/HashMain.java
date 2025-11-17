package com.taller.modiesel.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashMain {
    public static void main(String[] args) {
        String raw = "123456";
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        String hash = bcrypt.encode(raw);
        System.out.println(hash);
    }
}

