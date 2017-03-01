package victor.training.java8.voxxed.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import victor.training.java8.voxxed.order.entity.Customer;
import victor.training.java8.voxxed.order.entity.Order;
import victor.training.java8.voxxed.order.entity.Order.Status;
import victor.training.java8.voxxed.order.entity.OrderLine;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SearchStreamsTest {

	private SearchStreams service = new SearchStreams();

	@Test
	public void p1_getActiveOrders() {
		Order order1 = new Order(Status.ACTIVE);
		Order order2 = new Order(Status.ACTIVE);
		Order order3 = new Order(Status.DRAFT);
		Customer customer = new Customer(order1, order2, order3);
		assertEquals(Arrays.asList(order1, order2), service.getActiveOrders(customer));
	}

	@Test
	public void p2_getOrderById() {
		List<Order> orders = Arrays.asList(new Order(1L), new Order(2L), new Order(3L));
		assertEquals(1L, (long) service.getOrderById(orders, 1L).getId());
	}

	@Test
	public void p3_hasActiveOrders_true() {
		Customer customer = new Customer(new Order(Status.INACTIVE), new Order(Status.ACTIVE));
		assertTrue(service.hasActiveOrders(customer));
	}

	@Test
	public void p3_hasActiveOrders_false() {
		Customer customer = new Customer(new Order(Status.INACTIVE));
		assertFalse(service.hasActiveOrders(customer));
	}
	
	@Test
	public void p4_canBeReturned_frue() {
		Order order = new Order(new OrderLine());
		assertTrue(service.canBeReturned(order));
	}
	
	@Test
	public void p4_canBeReturned_false() {
		OrderLine specialOffer = new OrderLine().setSpecialOffer(true);
		Order order = new Order(specialOffer, new OrderLine());
		assertFalse(service.canBeReturned(order));
	}
	
	@Test
	public void p5_getMaxPriceOrder() {
		Order order1 = new Order().setTotalPrice(BigDecimal.ONE);
		Order order2 = new Order().setTotalPrice(BigDecimal.TEN);
		assertEquals(order2, service.getMaxPriceOrder(new Customer(order1, order2)));
	}
	
	@Test
	public void p5_getMaxPriceOrder_returnsNothing() {
		assertNull(service.getMaxPriceOrder(new Customer()));
	}
	
	@Test
	public void p6_getLast3Orders() {
		Order order1 = new Order().setCreationDate(LocalDate.parse("2016-01-01"));
		Order order2 = new Order().setCreationDate(LocalDate.parse("2016-01-02"));
		Order order3 = new Order().setCreationDate(LocalDate.parse("2016-01-03"));
		Order order4 = new Order().setCreationDate(LocalDate.parse("2016-01-04"));
		
		Customer customer = new Customer(order1, order2, order3, order4);
		assertEquals(Arrays.asList(order4,order3,order2), service.getLast3Orders(customer));
	}
	
	@Test
	public void p6_getLast3Orders_whenOnlyTwoOrders() {
		Order order1 = new Order().setCreationDate(LocalDate.parse("2016-01-01"));
		Order order2 = new Order().setCreationDate(LocalDate.parse("2016-01-02"));
		
		Customer customer = new Customer(order1, order2);
		assertEquals(Arrays.asList(order2, order1), service.getLast3Orders(customer));
	}
	
	@Test
	public void p6_getLast3Orders_whenNoOrders() {
		Customer customer = new Customer();
		assertEquals(Arrays.asList(), service.getLast3Orders(customer));
	}
	

}