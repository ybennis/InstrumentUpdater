package com.trading.instrument.data.model;

/**
 * Wrapper object containing the needed information for merging a pair of instruments
 * 
 */
public class InstrumentMergeWrapperObject {
	private Instrument existingInstrument;
	private Instrument newInstrument;

	public InstrumentMergeWrapperObject(Instrument existingInstrument, Instrument newInstrument) {
		this.existingInstrument = existingInstrument;
		this.newInstrument = newInstrument;
	}

	public Instrument getExistingInstrument() {
		return existingInstrument;
	}

	public Instrument getNewInstrument() {
		return newInstrument;
	}

	public void setExistingInstrument(Instrument existingInstrument) {
		this.existingInstrument = existingInstrument;
	}

	public void setNewInstrument(Instrument newInstrument) {
		this.newInstrument = newInstrument;
	}
}
