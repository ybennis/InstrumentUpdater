package com.trading.instrument.data.source;

import com.trading.instrument.data.model.Instrument;
import com.trading.instrument.data.model.SourceType;

public class LMETradingSource extends TradingSource {

	public LMETradingSource() {
		super();
		type = SourceType.LME;
	}

	@Override
	public Instrument mergePairOfIntruments(Instrument oldInstrument, Instrument newInstrument) {
		if (oldInstrument == null) {
			return newInstrument;
		}
		return super.updateInstrument(Instrument.InstrumentBuilder.newInstance()
				.setDeliveryDate(getDeliveryDate(oldInstrument, newInstrument))
				.setLastTradingDate(getLastTradingDate(oldInstrument, newInstrument)).setLabel(newInstrument.getLabel())
				.setInstrumentID(newInstrument.getInstrumentID())
				.setTradable(getTradableFlag(oldInstrument, newInstrument)));
	}

}
