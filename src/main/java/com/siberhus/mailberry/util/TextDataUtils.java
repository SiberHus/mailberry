package com.siberhus.mailberry.util;

import java.io.IOException;
import java.io.Reader;

public class TextDataUtils {
	
	public static int countLineNumber(Reader reader, char lb) throws IOException {
		char[] c = new char[1024];
		int count = 0;
		int readChars = 0;
		try{
			while ((readChars = reader.read(c)) != -1) {
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == lb)
						++count;
				}
			}
		}finally{
			reader.reset();
		}
		return count;
	}
	
	public static int countLineNumber(Reader reader) throws IOException {
		return countLineNumber(reader, guessLineBreak(reader));
	}
	
	public static char guessLineBreak(Reader reader) throws IOException {
		char[] c = new char[1024];
		int readChars = 0;
		try{
			while ((readChars = reader.read(c)) != -1) {
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n' || c[i] =='\r'){
						return c[i];
					}
				}
			}
		}finally{
			reader.reset();
		}
		return '\n';
	}
	
	public static String guessLineBreak(String data){
		if(data.contains("\\r\\n")){
			return "\\r\\n"; //WINDOWS
		}else if(data.contains("\\r")){
			return "\\r"; //MAC
		}else{
			return "\\n"; //UNIX
		}
	}
	
	
}
