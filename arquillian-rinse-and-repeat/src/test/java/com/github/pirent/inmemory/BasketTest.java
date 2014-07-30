package com.github.pirent.inmemory;

import javax.ejb.EJB;
import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.pirent.api.OrderRepository;
import com.github.pirent.impl.Basket;
import com.github.pirent.impl.SingletonOrderRepository;

/**
 * Testing the in-memory implementation.
 * 
 * @author pirent
 *
 */
@RunWith(Arquillian.class)
public class BasketTest {
	
	@Deployment
	public static JavaArchive createDeployement() {
		JavaArchive jar = ShrinkWrap
				.create(JavaArchive.class, "test.jar")
				.addClasses(Basket.class, OrderRepository.class,
						SingletonOrderRepository.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		System.out.println("-- Archiving: \n" + jar.toString(true) + " \n--");
		return jar;
	}
	
	@Inject
	private Basket basket;
	
	@EJB
//	(mappedName = "java:global/test/SingletonOrderRepository!com.github.pirent.api.OrderRepository")
	private OrderRepository repo;

	@Test
	@InSequence(1)
	public void placeOrderShouldAddOrder() {
		Assert.assertNotNull("Basket should not null", basket);
		Assert.assertNotNull("OrderRepository should not null", repo);
		basket.addItem("Sunglasses");
		basket.addItem("Suit");
		basket.placeOrder();
		Assert.assertEquals(1, repo.getOrderCount());
		Assert.assertEquals(0, basket.getItemCount());
		
		basket.addItem("Raygun");
		basket.addItem("Spaceship");
		basket.placeOrder();
		Assert.assertEquals(2, repo.getOrderCount());
		Assert.assertEquals(0, basket.getItemCount());
	}
}
