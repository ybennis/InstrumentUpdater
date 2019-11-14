package com.trading.instrument.data.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.trading.instrument.data.factory.TradingSourceFactory;
import com.trading.instrument.data.model.Instrument;
import com.trading.instrument.data.model.InstrumentMergeWrapperObject;
import com.trading.instrument.data.model.InstrumentMergerCapability;
import com.trading.instrument.data.model.SourceType;
import com.trading.instrument.data.source.TradingSource;

/**
 * Thread Safe controller object that receives instrument to merge and proceed
 * to merging with any of the stored instruments by delegating to the
 * specialized merger object {@link InstrumentMergerCapability}
 *
 */
public class InstrumentMergingController implements BiFunction<Instrument, Instrument, Instrument> {
	private static int PROCESSOR_NUMBER = Runtime.getRuntime().availableProcessors();

	private static Map<String, Instrument> instruments = new ConcurrentHashMap<>(100, 0.75f, PROCESSOR_NUMBER);

	public InstrumentMergingController() {

	}

	public Instrument receiveNewInstrument(Instrument newInstrument) {

		Instrument existingInstrument = instruments.get(newInstrument.getMappingKey());
		// synchronizing on key instrument before merging to ensure
		// chaining of merged instruments. A side effect of non synchronizing even with
		// the use of a ConcurrentHashMap
		// will result in a case where two threads invoking this method simultaneously
		// with the same key to get for each
		// of these threads a different merged instrument , updating the hashmap without
		// having applied the merge with each other.
		if (existingInstrument != null) {
			synchronized (existingInstrument) {
				return apply(existingInstrument, newInstrument);
			}
		} else {
			return apply(existingInstrument, newInstrument);

		}
	}

	@Override
	public Instrument apply(Instrument existingInstrument, Instrument newInstrument) {
		InstrumentMergerCapability merger = TradingSourceFactory.getTradingSource(newInstrument.getType());
		Instrument mergedInstrument = merger.mergePairOfIntruments(existingInstrument, newInstrument);
		instruments.put(mergedInstrument.getMappingKey(), mergedInstrument);
		return mergedInstrument;
	}
}
