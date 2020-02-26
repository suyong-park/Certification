package com.certification.servlets.board;

public class NextAction {
	private String nextPath;
	private boolean isRedirect;
	
	public NextAction() {}
	public NextAction(String nextPath, boolean isRedirect) {
		this.nextPath = nextPath;
		this.isRedirect = isRedirect;
	}
	
	
	public String getNextPath() {
		return nextPath;
	}
	
	public void setNextPath(String nextPath) {
		this.nextPath = nextPath;
	}
	public boolean isRedirect() {
		return isRedirect;
	}
	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}
	
	
}
