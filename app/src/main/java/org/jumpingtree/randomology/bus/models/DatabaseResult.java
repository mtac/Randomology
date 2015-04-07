package org.jumpingtree.randomology.bus.models;

public class DatabaseResult {

	private boolean success = false;

	public DatabaseResult(boolean success) {
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

}
