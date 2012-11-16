package com.siberhus.mailberry.impexp.reader;

import java.io.IOException;

public interface ItemReader {
	
	public String[] readNextItem() throws IOException;
	
	public void close();
	
}
