package com.trading.instrument.data.model;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trading.instrument.data.factory.TradingSourceFactory;
import com.trading.instrument.data.source.TradingSource;

public class Instrument {

	private final String instrumentID;
	private final SourceType type;
	private final String lastTradingDate;
	private final String deliveryDate;
	private final String market;
	private final String label;
	private Boolean tradable = true;
	// Additional fields used to embed extra fields not defined
	// in the base definition of an instrument
	private Map<String, String> additionalFields = new HashMap<String, String>();

	private Instrument(InstrumentBuilder builder) {
		this.instrumentID = builder.instrumentID;
		this.type = builder.type;
		this.lastTradingDate = builder.lastTradingDate;
		this.deliveryDate = builder.deliveryDate;
		this.market = builder.market;
		this.label = builder.label;
		this.tradable = builder.tradable;
		this.additionalFields = builder.additionalFields;
	}

	public String getInstrumentID() {
		return instrumentID;
	}

	public SourceType getType() {
		return type;
	}

	public String getLastTradingDate() {
		return lastTradingDate;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public String getMarket() {
		return market;
	}

	public String getLabel() {
		return label;
	}

	public Boolean getTradable() {
		return tradable;
	}

	public Map<String, String> getAdditionalFields() {
		return additionalFields;
	}

	public String getMappingKey() {
		TradingSource tradingSource = TradingSourceFactory.getTradingSource(type);
		if (tradingSource == null) {
			return instrumentID;
		}
		return tradingSource.getInstrumentMappingKey(this);
	}

	/**
	 * Static Builder used to hide building complexity of the Instrument Object
	 *
	 */
	public static class InstrumentBuilder {

		private String instrumentID;
		private SourceType type;
		private String lastTradingDate;
		private String deliveryDate;
		private String market;
		private String label;
		private Boolean tradable = true;
		private Map<String, String> additionalFields = new HashMap<String, String>();

		public static InstrumentBuilder newInstance() {
			return new InstrumentBuilder();
		}

		private InstrumentBuilder() {
		}

		// Setter methods
		public InstrumentBuilder setInstrumentID(String instrumentID) {
			this.instrumentID = instrumentID;
			return this;
		}

		public InstrumentBuilder setType(SourceType type) {
			this.type = type;
			return this;
		}

		public InstrumentBuilder setLastTradingDate(String lastTradingDate) {
			this.lastTradingDate = lastTradingDate;
			return this;
		}

		public InstrumentBuilder setDeliveryDate(String deliveryDate) {
			this.deliveryDate = deliveryDate;
			return this;
		}

		public InstrumentBuilder setMarket(String market) {
			this.market = market;
			return this;
		}

		public InstrumentBuilder setLabel(String label) {
			this.label = label;
			return this;
		}

		public InstrumentBuilder setTradable(Boolean tradable) {
			this.tradable = tradable;
			return this;
		}

		public InstrumentBuilder setAdditionalFields(Map<String, String> additionalFields) {
			this.additionalFields = additionalFields;
			return this;
		}

		public InstrumentBuilder addAdditionalField(String fieldName, String fieldValue) {
			additionalFields.put(fieldName, fieldValue);
			return this;
		}

		public Instrument build() {
			return new Instrument(this);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((additionalFields == null) ? 0 : additionalFields.hashCode());
		result = prime * result + ((deliveryDate == null) ? 0 : deliveryDate.hashCode());
		result = prime * result + ((instrumentID == null) ? 0 : instrumentID.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((lastTradingDate == null) ? 0 : lastTradingDate.hashCode());
		result = prime * result + ((market == null) ? 0 : market.hashCode());
		result = prime * result + ((tradable == null) ? 0 : tradable.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Instrument other = (Instrument) obj;
		if (additionalFields == null) {
			if (other.additionalFields != null)
				return false;
		} else if (!additionalFields.equals(other.additionalFields))
			return false;
		if (deliveryDate == null) {
			if (other.deliveryDate != null)
				return false;
		} else if (!deliveryDate.equals(other.deliveryDate))
			return false;
		if (instrumentID == null) {
			if (other.instrumentID != null)
				return false;
		} else if (!instrumentID.equals(other.instrumentID))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (lastTradingDate == null) {
			if (other.lastTradingDate != null)
				return false;
		} else if (!lastTradingDate.equals(other.lastTradingDate))
			return false;
		if (market == null) {
			if (other.market != null)
				return false;
		} else if (!market.equals(other.market))
			return false;
		if (tradable == null) {
			if (other.tradable != null)
				return false;
		} else if (!tradable.equals(other.tradable))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Instrument [instrumentID=" + instrumentID + ", type=" + type + ", lastTradingDate=" + lastTradingDate
				+ ", deliveryDate=" + deliveryDate + ", market=" + market + ", label=" + label + ", tradable="
				+ tradable + ", additionalFields=" + additionalFields + "]";
	}

}
