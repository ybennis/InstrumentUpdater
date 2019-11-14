package com.trading.instrument.data.test.builder;

import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.trading.instrument.data.TradingInstrumentUpdater;
import com.trading.instrument.data.model.Instrument;
import com.trading.instrument.data.model.SourceType;
import com.trading.instrument.data.source.PRIMETradingSource;

/**
 * 
 * Utility Class that construct necessary objects for testing purposes like
 * instantiating rules,..etc
 */
public class TestDataBuilder {

	static public BiFunction<Instrument, Instrument, Boolean> createTradableFlagRule(SourceType type) {
		if (type == SourceType.LME) {
			return TradingInstrumentUpdater.TRADABLE_FLAG_LME_LAMBDA_RULE;
		}
		return TradingInstrumentUpdater.TRADBLE_FLAG_PRIME_LAMBDA_RULE;
	}

	static public BiFunction<Instrument, Instrument, String> createLastTradingDateRule(SourceType type) {
		if (type == SourceType.LME) {
			return TradingInstrumentUpdater.LAST_TRADING_DATE_LME_LAMBDA_RULE;
		}
		return TradingInstrumentUpdater.LAST_TRADING_DATE_PRIME_LAMBDA_RULE;
	}

	static public BiFunction<Instrument, Instrument, String> createDeliveryDateRule(SourceType type) {
		if (type == SourceType.LME) {
			return TradingInstrumentUpdater.DELIVERY_DATE_LME_LAMBDA_RULE;
		}
		return TradingInstrumentUpdater.DELIVERY_DATE_PRIME_LAMBDA_RULE;
	}

	static public Function<Instrument, String> createMappingKeyRule(SourceType type) {
		if (type == SourceType.LME) {
			return TradingInstrumentUpdater.LME_MAPPING_KEY_RULE;
		}
		return TradingInstrumentUpdater.PRIME_MAPPING_KEY_RULE;
	}

}
