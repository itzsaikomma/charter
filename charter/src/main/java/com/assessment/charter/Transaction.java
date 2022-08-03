package com.assessment.charter;

import java.util.List;

public class Transaction {
	private String month;
	private List<Long> amounts;
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public List<Long> getAmounts() {
		return amounts;
	}
	public void setAmounts(List<Long> amounts) {
		this.amounts = amounts;
	}
	
	
}
