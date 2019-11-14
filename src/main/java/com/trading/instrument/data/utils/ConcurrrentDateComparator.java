package com.trading.instrument.data.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trading.instrument.data.model.Instrument.InstrumentBuilder;

/**
 * Thread Safe Date comparator
 *
 */
public class ConcurrrentDateComparator {
	private static String DATE_FORMAT = "dd-MM-yyyy";
	private static final Logger logger = LoggerFactory.getLogger(ConcurrrentDateComparator.class);

	private ThreadLocal<DateFormat> dateComparatorLocalThread = new ThreadLocal<DateFormat>() {
		protected DateFormat initialValue() {
			return new SimpleDateFormat(DATE_FORMAT);
		};

		public DateFormat get() {
			return super.get();
		}

		public void remove() {
			super.remove();
		}

		public void set(DateFormat arg0) {
			super.set(arg0);
		}

	};

	public boolean isDateAfter(String firstDate, String secondDate) {
		DateFormat dateTimeFormatter = dateComparatorLocalThread.get();
		try {
			return dateTimeFormatter.parse(firstDate).after(dateTimeFormatter.parse(secondDate));
		} catch (ParseException e) {
			logger.error("Error in parsing malformatted dates : firstDate {} , secondDate {}", firstDate, secondDate);
		}

		return false;
	}

}
