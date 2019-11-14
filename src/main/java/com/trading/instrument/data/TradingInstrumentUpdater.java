package com.trading.instrument.data;

import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.trading.instrument.data.controller.InstrumentPublisherEngine;
import com.trading.instrument.data.model.Instrument;
import com.trading.instrument.data.model.SourceType;
import com.trading.instrument.data.source.PRIMETradingSource;
import com.trading.instrument.data.utils.ConcurrrentDateComparator;

public class TradingInstrumentUpdater {

	// Define Rules For LastTradingDate and DeliveryDate
	static public BiFunction<Instrument, Instrument, String> LAST_TRADING_DATE_LME_LAMBDA_RULE = (oldInstrument,
			newInstrument) -> (oldInstrument.getType() == SourceType.LME)
					? (new ConcurrrentDateComparator().isDateAfter(newInstrument.getLastTradingDate(),
							oldInstrument.getLastTradingDate()) ? newInstrument.getLastTradingDate()
									: oldInstrument.getLastTradingDate())
					: newInstrument.getLastTradingDate();

	static public BiFunction<Instrument, Instrument, String> LAST_TRADING_DATE_PRIME_LAMBDA_RULE = (oldInstrument,
			newInstrument) -> (oldInstrument.getType() == SourceType.LME) ? oldInstrument.getLastTradingDate()
					: (new ConcurrrentDateComparator().isDateAfter(newInstrument.getLastTradingDate(),oldInstrument.getLastTradingDate()))
							? newInstrument.getLastTradingDate()
							: oldInstrument.getLastTradingDate();
	static public BiFunction<Instrument, Instrument, String> DELIVERY_DATE_LME_LAMBDA_RULE = (oldInstrument,
			newInstrument) -> (oldInstrument.getType() == SourceType.LME)
					? (new ConcurrrentDateComparator().isDateAfter(newInstrument.getDeliveryDate(),
							oldInstrument.getDeliveryDate()) ? newInstrument.getDeliveryDate()
									: oldInstrument.getDeliveryDate())
					: newInstrument.getDeliveryDate();

	static public BiFunction<Instrument, Instrument, String> DELIVERY_DATE_PRIME_LAMBDA_RULE = (oldInstrument,
			newInstrument) -> (oldInstrument.getType() == SourceType.LME) ? oldInstrument.getDeliveryDate()
					: (new ConcurrrentDateComparator().isDateAfter(newInstrument.getDeliveryDate(),
							oldInstrument.getDeliveryDate()) ? newInstrument.getDeliveryDate()
									: oldInstrument.getDeliveryDate());
	// Define Rules For Tradable Flag
	static public BiFunction<Instrument, Instrument, Boolean> TRADABLE_FLAG_LME_LAMBDA_RULE = (oldInstrument,
			newInstrument) -> oldInstrument.getType() == SourceType.LME ? newInstrument.getTradable()
					: oldInstrument.getTradable();
	static public BiFunction<Instrument, Instrument, Boolean> TRADBLE_FLAG_PRIME_LAMBDA_RULE = (oldInstrument,
			newInstrument) -> newInstrument.getTradable();
	static public Function<Instrument, String> LME_MAPPING_KEY_RULE = (inst) -> inst.getInstrumentID();
	static public Function<Instrument, String> PRIME_MAPPING_KEY_RULE = (intrument) -> intrument.getAdditionalFields()
			.getOrDefault(PRIMETradingSource.PRIME_EXCHANGE_CODE_FIELD, intrument.getInstrumentID());

	public static void main(String[] args) {
		InstrumentPublisherEngine instrumentEngine = InstrumentPublisherEngine.getInstance();
		// For Demonstration purpose but can be generally applied, this is a way to
		// dynamically inject rules for a given Trading Source

		// Inject defined rules dynamically
		instrumentEngine.addDynamicallySpecificMergeLastTradingDateRule(LAST_TRADING_DATE_LME_LAMBDA_RULE,
				SourceType.LME);
		instrumentEngine.addDynamicallySpecificMergeLastTradingDateRule(LAST_TRADING_DATE_PRIME_LAMBDA_RULE,
				SourceType.PRIME);
		instrumentEngine.addDynamicallySpecificMergeDeliveryDateRule(DELIVERY_DATE_LME_LAMBDA_RULE, SourceType.LME);
		instrumentEngine.addDynamicallySpecificMergeDeliveryDateRule(DELIVERY_DATE_PRIME_LAMBDA_RULE, SourceType.PRIME);
		instrumentEngine.addDynamicallySpecificTradableFlagRule(TRADABLE_FLAG_LME_LAMBDA_RULE, SourceType.LME);
		instrumentEngine.addDynamicallySpecificTradableFlagRule(TRADBLE_FLAG_PRIME_LAMBDA_RULE, SourceType.PRIME);
		instrumentEngine.addMappingKeyRule(LME_MAPPING_KEY_RULE, SourceType.LME);
		instrumentEngine.addMappingKeyRule(PRIME_MAPPING_KEY_RULE, SourceType.PRIME);
		instrumentEngine.run();
		Instrument firstInstrument = Instrument.InstrumentBuilder.newInstance().setDeliveryDate("17-03-2018")
				.setType(SourceType.LME).setLastTradingDate("15-03-2018").setMarket("LME_PB")
				.setLabel("Lead 13 March 2018").setInstrumentID("PB_03_2018").setTradable(true).build();
		Instrument secondInstrument = Instrument.InstrumentBuilder.newInstance().setDeliveryDate("18-03-2018")
				.setType(SourceType.PRIME).setLastTradingDate("14-03-2018").setMarket("LME_PB").setTradable(false)
				.setLabel("Lead 13 March 2018").setInstrumentID("PB_03_2018")
				.addAdditionalField(PRIMETradingSource.PRIME_EXCHANGE_CODE_FIELD, "PB_03_2018").build();
		instrumentEngine.register(firstInstrument);
		instrumentEngine.register(secondInstrument);
		instrumentEngine.shutdown();
	}
}
