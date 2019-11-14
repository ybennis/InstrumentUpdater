package com.trading.instrument.data.controller;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.trading.instrument.data.model.Instrument;
import com.trading.instrument.data.model.SourceType;
import com.trading.instrument.data.source.LMETradingSource;
import com.trading.instrument.data.source.PRIMETradingSource;
import com.trading.instrument.data.test.builder.TestDataBuilder;
import com.trading.instrument.data.utils.ConcurrrentDateComparator;

public class InstrumentMergingControllerTest {
	private static LMETradingSource lmeSource = new LMETradingSource();
	private static PRIMETradingSource primeSource = new PRIMETradingSource();
	private static String DATE_FORMAT = "dd-MM-yyyy";

	@BeforeClass
	public static void init() {
		lmeSource.setDeliveryDateRule(TestDataBuilder.createDeliveryDateRule(SourceType.LME));
		primeSource.setDeliveryDateRule(TestDataBuilder.createDeliveryDateRule(SourceType.PRIME));
		lmeSource.setLastTradingDateRule(TestDataBuilder.createLastTradingDateRule(SourceType.LME));
		primeSource.setLastTradingDateRule(TestDataBuilder.createLastTradingDateRule(SourceType.PRIME));
		lmeSource.setTradableFlagRule(TestDataBuilder.createTradableFlagRule(SourceType.LME));
		primeSource.setTradableFlagRule(TestDataBuilder.createTradableFlagRule(SourceType.PRIME));
		lmeSource.setMappingKeyRule(TestDataBuilder.createMappingKeyRule(SourceType.LME));
		primeSource.setMappingKeyRule(TestDataBuilder.createMappingKeyRule(SourceType.PRIME));
	}

	@Test
	public void mergeSameTypeInstrumentsByLME()  {
		Instrument firstInstrument = Instrument.InstrumentBuilder.newInstance().setDeliveryDate("17-03-2018")
				.setType(SourceType.LME).setLastTradingDate("15-03-2018").setMarket("LME_PB")
				.setLabel("Lead 13 March 2018").setInstrumentID("PB_03_2018").setTradable(true).build();
		Instrument secondInstrument = Instrument.InstrumentBuilder.newInstance().setDeliveryDate("18-03-2018")
				.setType(SourceType.LME).setLastTradingDate("14-03-2018").setMarket("LME_PB").setTradable(false)
				.setLabel("Lead 14 March 2018").setInstrumentID("PB_03_2018").build();

		Instrument mergedInstrument = lmeSource.mergePairOfIntruments(firstInstrument, secondInstrument);
		assertEquals(mergedInstrument.getLastTradingDate(), "15-03-2018");
		assertEquals(mergedInstrument.getDeliveryDate(), "18-03-2018");
		assertEquals(mergedInstrument.getLabel(), "Lead 14 March 2018");
		assertEquals(mergedInstrument.getTradable(), false);
	}

	@Test
	public void mergeDifferentTypeInstrumentsByPRIME()  {
		Instrument firstInstrument = Instrument.InstrumentBuilder.newInstance().setDeliveryDate("17-03-2018")
				.setType(SourceType.LME).setLastTradingDate("15-03-2018").setMarket("LME_PB")
				.setLabel("Lead 13 March 2018").setInstrumentID("PB_03_2018").setTradable(false).build();
		Instrument secondInstrument = Instrument.InstrumentBuilder.newInstance().setDeliveryDate("18-03-2018")
				.setType(SourceType.PRIME).setLastTradingDate("16-03-2018").setMarket("LME_PB").setTradable(true)
				.setLabel("Lead 14 March 2018").setInstrumentID("PB_03_2018")
				.addAdditionalField(PRIMETradingSource.PRIME_EXCHANGE_CODE_FIELD, "PB_03_2018").build();

		Instrument mergedInstrument = primeSource.mergePairOfIntruments(firstInstrument, secondInstrument);
		assertEquals(mergedInstrument.getDeliveryDate(), "17-03-2018");
		assertEquals(mergedInstrument.getLastTradingDate(), "15-03-2018");
		assertEquals(mergedInstrument.getLabel(), "Lead 14 March 2018");
		assertEquals(mergedInstrument.getTradable(), true);
	}

	@Test
	public void mergeDifferentTypeInstrumentsByLME()  {
		Instrument firstInstrument = Instrument.InstrumentBuilder.newInstance().setDeliveryDate("18-03-2018")
				.setType(SourceType.PRIME).setLastTradingDate("16-03-2018").setMarket("LME_PB").setTradable(true)
				.setLabel("Lead 14 March 2018").setInstrumentID("PB_03_2018")
				.addAdditionalField(PRIMETradingSource.PRIME_EXCHANGE_CODE_FIELD, "PB_03_2018").build();

		Instrument secondInstrument = Instrument.InstrumentBuilder.newInstance().setDeliveryDate("17-03-2018")
				.setType(SourceType.LME).setLastTradingDate("15-03-2018").setMarket("LME_PB")
				.setLabel("Lead 13 March 2018").setInstrumentID("PB_03_2018").setTradable(false).build();
		Instrument mergedInstrument = lmeSource.mergePairOfIntruments(firstInstrument, secondInstrument);
		assertEquals(mergedInstrument.getLastTradingDate(), "15-03-2018");
		assertEquals(mergedInstrument.getDeliveryDate(), "17-03-2018");
		assertEquals(mergedInstrument.getLabel(), "Lead 13 March 2018");
		assertEquals(mergedInstrument.getTradable(), true);
	}

}
