package com.github.pirent;

import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

// Telling JUnit to use Arquillian as test controller
@RunWith(Arquillian.class)
public class GreeterTest {

	/* Arquillian looks for a public static method annotated with @Deployment annotation
	* to retrieve the test archive (i.e. micro-deployment). 
	* Note: @Deployment method is only mandatory for tests that run inside the container
	*/
	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class);
		jar.addClass(Greeter.class).addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		
		System.out.println("*---- Archiving: \n" + jar.toString(true) + "\n ----*");
		
		return jar;
	}
	
	@Inject
	private Greeter greeter;
	
	@Test
	public void shouldCreateGreeting() {
		Assert.assertEquals("Hello, pirent!", greeter.createGreeting("pirent"));
		greeter.greet(System.out, "pirent");
	}
}
