package com.mysql.results;


public class StandardResult {

	private int expectResults = 0;
	private int success = 0;
	private Object result;
	
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public int getExpectResults() {
		return expectResults;
	}
	public void setExpectResults(int expectResults) {
		this.expectResults = expectResults;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	
	
}
