package com.trading.instrument.data.model;

/**
 * Object type fulfilling the merging capability of two instruments
 *
 */
public interface InstrumentMergerCapability {
	public Instrument mergePairOfIntruments(Instrument oldIntrument, Instrument newIntrument);

	public String getInstrumentMappingKey(Instrument intrument);
}
