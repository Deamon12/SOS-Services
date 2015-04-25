package com.mysql.results;

import java.util.List;

import com.mysql.dto.Standard;


public class StandardResult {

	private int expectResults = 0;
	private int success = 0;
	//private List<Standard> results;
	private Object result;
	
	/*public List<Standard> getResults() {
		return results;
	}
	public void setResults(List<Standard> objects) {
		this.results = objects;
	}*/
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
