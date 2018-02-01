package com.gobenefit.exception;

public class FlatException extends Exception {
	private static final long serialVersionUID = -8458452504651161355L;

	private String message;
	private int flatNumber;
	private int floorNumber;
	private String block;

	public FlatException(String message, int flatNumber, int floorNumber, String block) {
		super(message);
		this.message = message;
		this.flatNumber = flatNumber;
		this.floorNumber = floorNumber;
		this.block = block;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getFlatNumber() {
		return flatNumber;
	}

	public void setFlatNumber(int flatNumber) {
		this.flatNumber = flatNumber;
	}

	public int getFloorNumber() {
		return floorNumber;
	}

	public void setFloorNumber(int floorNumber) {
		this.floorNumber = floorNumber;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

}
