package com.trading.instrument.data.source;

import com.trading.instrument.data.model.Instrument;
import com.trading.instrument.data.model.SourceType;

public class PRIMETradingSource extends TradingSource {

	public final static String PRIME_EXCHANGE_CODE_FIELD = "EXCHANGE_CODE";

	public PRIMETradingSource() {
		super();
		type = SourceType.PRIME;
	}

	@Override
	public Instrument mergePairOfIntruments(Instrument oldInstrument, Instrument newInstrument) {
		if (oldInstrument == null) {
			return newInstrument;
		}
		return super.updateInstrument(Instrument.InstrumentBuilder.newInstance()
				.setDeliveryDate(getDeliveryDate(oldInstrument, newInstrument))
				.setLastTradingDate(getLastTradingDate(oldInstrument, newInstrument)).setLabel(newInstrument.getLabel())
				.setInstrumentID(newInstrument.getInstrumentID()).setMarket(oldInstrument.getMarket())
				.setTradable(getTradableFlag(oldInstrument, newInstrument)));
	}

}
