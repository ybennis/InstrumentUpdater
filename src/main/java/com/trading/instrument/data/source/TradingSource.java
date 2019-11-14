package com.trading.instrument.data.source;

import com.trading.instrument.data.model.SourceType;

import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.trading.instrument.data.model.Instrument;
import com.trading.instrument.data.model.Instrument.InstrumentBuilder;
import com.trading.instrument.data.model.InstrumentMergerCapability;

public abstract class TradingSource implements InstrumentMergerCapability {
	protected BiFunction<Instrument, Instrument, String> lastTradingDateRule;
	protected SourceType type;
	protected String name;
	private BiFunction<Instrument, Instrument, String> deliveryDateRule;
	private BiFunction<Instrument, Instrument, Boolean> tradableFlagRule;
	private Function<Instrument, String> mappingKeyRule;

	public TradingSource() {
	}

	public SourceType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public void setType(SourceType type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Instrument updateInstrument(InstrumentBuilder builder) {
		return builder.setType(type).setMarket(type.getMarket()).build();
	}

	public BiFunction<Instrument, Instrument, String> getLastTradingDateRule() {
		return lastTradingDateRule;
	}

	public void setLastTradingDateRule(BiFunction<Instrument, Instrument, String> dateRule) {
		this.lastTradingDateRule = dateRule;
	}

	public void setDeliveryDateRule(BiFunction<Instrument, Instrument, String> dateRule) {
		this.deliveryDateRule = dateRule;
	}

	public BiFunction<Instrument, Instrument, String> getDeliveryDateRule() {
		return deliveryDateRule;
	}

	public void setTradableFlagRule(BiFunction<Instrument, Instrument, Boolean> rule) {
		this.tradableFlagRule = rule;
	}

	public BiFunction<Instrument, Instrument, Boolean> getTradableFlagRule() {
		return tradableFlagRule;
	}

	public Function<Instrument, String> getMappingKeyRule() {
		return mappingKeyRule;
	}

	/**
	 * Returns the last trading date for the merged instrument. Rules :
	 * 
	 * @param oldInstrument
	 * @param newInstrument
	 * @return
	 */
	public String getLastTradingDate(Instrument oldInstrument, Instrument newInstrument) {
		return getLastTradingDateRule().apply(oldInstrument, newInstrument);
	}

	public String getDeliveryDate(Instrument oldInstrument, Instrument newInstrument) {
		return getDeliveryDateRule().apply(oldInstrument, newInstrument);
	}

	public Boolean getTradableFlag(Instrument oldInstrument, Instrument newInstrument) {
		return getTradableFlagRule().apply(oldInstrument, newInstrument);
	}

	public void setMappingKeyRule(Function<Instrument, String> mappingKey) {
		this.mappingKeyRule = mappingKey;
	}

	@Override
	public String getInstrumentMappingKey(Instrument intrument) {
		return mappingKeyRule.apply(intrument);
	}

}
