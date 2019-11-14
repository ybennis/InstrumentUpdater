package com.trading.instrument.data.model;

public enum SourceType {
	LME("LME_PB"), PRIME("PB");

	private String market;

	SourceType(String market) {
		this.market = market;
	}

	public String getMarket() {
		return market;
	}
}
