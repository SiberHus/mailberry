package com.siberhus.mailberry.impexp.writer;

import java.io.IOException;

public interface ItemWriter {
	
	public void writeNextItem(String values[]) throws IOException;
	
	public void close();
	
}
