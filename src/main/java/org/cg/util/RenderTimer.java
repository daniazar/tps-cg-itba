package org.cg.util;

public class RenderTimer {

	long startMillis;
	
	public RenderTimer() {
		startMillis = System.currentTimeMillis();
	}
	
	public long getTime() {
		return System.currentTimeMillis() - startMillis;
	}
}
