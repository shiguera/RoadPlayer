package com.mlab.roadplayer.video;

import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestFrameExtractor {

	@BeforeClass
	public static void before() {
		PropertyConfigurator.configure("log4j.properties");
	}
	@Test
	public void test() {
		System.out.println("TestFrameExtractor.test()");
	}

}
