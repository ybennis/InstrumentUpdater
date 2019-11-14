package com.trading.instrument.data.factory;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.trading.instrument.data.model.Instrument;
import com.trading.instrument.data.model.SourceType;
import com.trading.instrument.data.source.LMETradingSource;
import com.trading.instrument.data.source.PRIMETradingSource;
import com.trading.instrument.data.source.TradingSource;

/**
 * Factory Object For the TradingSource Object
 *
 */
public class TradingSourceFactory {

	static private LMETradingSource lmeTradingSource = new LMETradingSource();
	static private PRIMETradingSource primeTradingSource = new PRIMETradingSource();
	private static final Map<SourceType, TradingSource> factoryMap = Collections
			.unmodifiableMap(new HashMap<SourceType, TradingSource>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = -8193816668240181211L;

				{
					put(SourceType.LME, lmeTradingSource);
					put(SourceType.PRIME, primeTradingSource);
				}
			});

	static public TradingSource getTradingSource(SourceType type) {
		return factoryMap.get(type);
	}

	static public void injectLastTradingDateRule(BiFunction<Instrument, Instrument, String> rule, SourceType type) {
		factoryMap.get(type).setLastTradingDateRule(rule);
	}

	static public void injectDeliveryDateRule(BiFunction<Instrument, Instrument, String> rule, SourceType type) {
		factoryMap.get(type).setDeliveryDateRule(rule);
	}

	static public void injectTradableFlagRule(BiFunction<Instrument, Instrument, Boolean> rule, SourceType type) {
		factoryMap.get(type).setTradableFlagRule(rule);
	}

	static public void injectMappingKeyRule(Function<Instrument, String> mappingKey, SourceType type) {
		factoryMap.get(type).setMappingKeyRule(mappingKey);
	}

}
