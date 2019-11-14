package com.trading.instrument.data.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.trading.instrument.data.model.SourceType;
import com.trading.instrument.data.source.LMETradingSource;
import com.trading.instrument.data.source.PRIMETradingSource;
import com.trading.instrument.data.source.TradingSource;
import com.trading.instrument.data.test.builder.TestDataBuilder;

public class TradingSourceFactoryTest {

	@Test
	public void getLMETradingSourceFromFactoryTest() {
		boolean typeCheck = TradingSourceFactory.getTradingSource(SourceType.LME) instanceof LMETradingSource;
		assertTrue("Trading source returned by TradingFactory is not of LME Type", typeCheck);
	}

	@Test
	public void getPRIMETradingSourceFromFactoryTest() {
		boolean typeCheck = TradingSourceFactory.getTradingSource(SourceType.PRIME) instanceof PRIMETradingSource;
		assertTrue("Trading source returned by TradingFactory is not of PRIME Type", typeCheck);
	}

	@Test
	public void injectTradableFlagRuleTest() {
		TradingSource lmeSource = TradingSourceFactory.getTradingSource(SourceType.LME);
		assertNull(lmeSource.getTradableFlagRule());
		TradingSourceFactory.injectTradableFlagRule(TestDataBuilder.createTradableFlagRule(SourceType.LME),
				SourceType.LME);
		lmeSource = TradingSourceFactory.getTradingSource(SourceType.LME);
		assertNotNull(lmeSource.getTradableFlagRule());
	}

	@Test
	public void injectLastTradingDateRuleTest() {
		TradingSource lmeSource = TradingSourceFactory.getTradingSource(SourceType.LME);
		assertNull(lmeSource.getLastTradingDateRule());
		TradingSourceFactory.injectLastTradingDateRule(TestDataBuilder.createLastTradingDateRule(SourceType.LME),
				SourceType.LME);
		lmeSource = TradingSourceFactory.getTradingSource(SourceType.LME);
		assertNotNull(lmeSource.getLastTradingDateRule());
	}

	@Test
	public void injectDeliveryDateRuleTest() {
		TradingSource lmeSource = TradingSourceFactory.getTradingSource(SourceType.LME);
		assertNull(lmeSource.getDeliveryDateRule());
		TradingSourceFactory.injectDeliveryDateRule(TestDataBuilder.createDeliveryDateRule(SourceType.LME),
				SourceType.LME);
		lmeSource = TradingSourceFactory.getTradingSource(SourceType.LME);
		assertNotNull(lmeSource.getDeliveryDateRule());
	}

}
