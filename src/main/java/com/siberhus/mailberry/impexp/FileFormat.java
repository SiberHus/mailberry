package com.siberhus.mailberry.impexp;

public enum FileFormat {
	
	WIN, UNIX, MAC;
	
	public String getLineBreak(){
		if(this==FileFormat.WIN) return "\r\n";
		else if(this==FileFormat.UNIX) return "\n";
		else //if(format==FileFormat.MAC) 
			return "\r";
	}
	
}
