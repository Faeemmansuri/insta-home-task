package com.instagram;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = InstaHomeApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InstaHomeApplicationTests {

    @Test
	public void contextLoads() {
        assertTrue(true);
	}
	
}
