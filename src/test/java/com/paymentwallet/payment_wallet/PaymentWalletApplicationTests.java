package com.paymentwallet.payment_wallet;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PaymentWalletApplicationTests {


	@Test
	void mainMethodRuns() {
		PaymentWalletApplication.main(new String[] {});
		assertTrue(true);
	}


	@Test
	void contextLoads() {
		assertTrue(true);
	}

}
