package com.wanmeizhensuo.streams;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonTest {
    @Test
    void jsonPassIn() {
        InputStream fileStream = this.getClass().getClassLoader().getResourceAsStream("asserts/" + "simple-0.json");

        BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));
        System.out.println(reader);
    }
}
