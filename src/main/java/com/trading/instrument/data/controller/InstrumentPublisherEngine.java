package com.trading.instrument.data.controller;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trading.instrument.data.factory.TradingSourceFactory;
import com.trading.instrument.data.model.Instrument;
import com.trading.instrument.data.model.SourceType;
import com.trading.instrument.data.source.TradingSource;

/**
 * Engine Object responsable of managing the publishing of the incoming
 * instruments. Designed as singleton but offers a pool of threads controlled by
 * a {@link ScheduledExecutorService} serving the merging capability of
 * instruments.
 * 
 */
public class InstrumentPublisherEngine {

	private static final Logger logger = LoggerFactory.getLogger(InstrumentPublisherEngine.class);
	private static final long TIME_INTERVAL_POLLING_MILLISECONDS = 500l;
	private static final Integer MAXIMUM_CAPACITY_IN_QUEUE = 100;
	private static final Integer EXECUTOR_SERVICE_TERMINATION_AWAITING_TIME_SEC = 5;
	private static ScheduledExecutorService executor;
	private static InstrumentPublisherEngine instance;
	private BlockingQueue<Instrument> messageQueue;
	private InstrumentMergingController merger;

	private InstrumentPublisherEngine() {
		init();
	}

	public static InstrumentPublisherEngine getInstance() {
		synchronized (InstrumentPublisherEngine.class) {
			if (instance == null) {
				instance = new InstrumentPublisherEngine();
			}
		}
		return instance;
	}

	private void init() {
		int processors = Runtime.getRuntime().availableProcessors();
		executor = Executors.newScheduledThreadPool(processors);
		messageQueue = new LinkedBlockingQueue<Instrument>(MAXIMUM_CAPACITY_IN_QUEUE);
		merger = new InstrumentMergingController();
	}

	public void run() {
		logger.info("run method");
		executor.scheduleAtFixedRate(getPublisherEngineExecutorRunnable(), 0l, TIME_INTERVAL_POLLING_MILLISECONDS,
				TimeUnit.MILLISECONDS);
	}

	private Runnable getPublisherEngineExecutorRunnable() {
		return () -> {
			Instrument element;
			try {
				element = messageQueue.poll();
				if (element != null) {
					processNewElementInQueue(element);
				}
			} catch (Throwable e) {
				logger.error("An error occured while running the Instrument Publisher Engine", e);
			}
		};

	}

	private void processNewElementInQueue(Instrument newInstrument) {
		Instrument mergedInstrument = merger.receiveNewInstrument(newInstrument);
		publish(mergedInstrument);
	}

	private void publish(Instrument mergedInstrument) {
		logger.info("Instrument published : {}", mergedInstrument);
	}

	/**
	 * Publish messages asynchronuously. Message is put in a waiting queue until it
	 * is processed.
	 * 
	 * @param instrument
	 * @return true if message has been put in waiting queue successfully
	 */
	public synchronized boolean register(Instrument instrument) {
		return messageQueue.offer(instrument);
	}

	/**
	 * After ensuring no more data in queue, will complete the execution of the
	 * engine gracefully by terminating the scheduling of the internal
	 * ScheduledExecutorService
	 */
	public void shutdown() {
		try {
			executor.awaitTermination(EXECUTOR_SERVICE_TERMINATION_AWAITING_TIME_SEC, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			logger.error("executor interrupted abruptly by an external agent (should not happen)");
		} finally {
			logger.info("Instrument Publisher Engine Terminated");
		}
	}

	/**
	 * convenient method that illustrates a dynamic way to inject rules into the
	 * instrument publisher engine
	 * 
	 * @param lastTradingDateRule
	 * @param type                sourceType to apply the rule
	 */
	public void addDynamicallySpecificMergeLastTradingDateRule(BiFunction<Instrument, Instrument, String> dateRule,
			SourceType type) {
		TradingSourceFactory.injectLastTradingDateRule(dateRule, type);
	}

	public void addDynamicallySpecificMergeDeliveryDateRule(BiFunction<Instrument, Instrument, String> dateRule,
			SourceType type) {
		TradingSourceFactory.injectDeliveryDateRule(dateRule, type);

	}

	public void addDynamicallySpecificTradableFlagRule(BiFunction<Instrument, Instrument, Boolean> tradableFlagRule,
			SourceType type) {
		TradingSourceFactory.injectTradableFlagRule(tradableFlagRule, type);
	}

	public void addMappingKeyRule(Function<Instrument, String> mappinggKey, SourceType type) {
		TradingSourceFactory.injectMappingKeyRule(mappinggKey, type);

	}
}
