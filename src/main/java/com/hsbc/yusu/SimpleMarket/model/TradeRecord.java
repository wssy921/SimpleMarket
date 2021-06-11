package com.hsbc.yusu.SimpleMarket.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

/**
 * @author esuxyux
 *
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TradeRecord {

	private String tradeReference;
	private String accountNumber;
	private String stockCode;
	private String quantity;
	private String currency;
	private String price;
	private String broker;
	private String amount;
	private String receivedTimestamp;

	public TradeRecord() {
	}

	public TradeRecord(String tradeReference, String accountNumber, String stockCode, String quantity, String currency, String price, String broker) {
		this.tradeReference = tradeReference;
		this.accountNumber = accountNumber;
		this.stockCode = stockCode;
		this.quantity = quantity;
		this.currency = currency;
		this.price = price;
		this.broker = broker;
	}

}
