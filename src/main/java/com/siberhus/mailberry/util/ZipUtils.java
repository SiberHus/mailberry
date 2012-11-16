package com.siberhus.mailberry.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

import de.idyl.crypto.zip.AesZipFileEncrypter;


public class ZipUtils {
	
	public static void zipFile(File inFile, File outFile) throws IOException{
		// Create a buffer for reading the files
		ZipOutputStream fout = null;
		try{
			fout = new ZipOutputStream(new FileOutputStream(outFile));
			putZipEntry(fout, inFile, inFile.getName());
		}finally{
			IOUtils.closeQuietly(fout);
		}
	}
	
	public static void putZipEntry(ZipOutputStream fout, File inFile, String fileName) throws IOException{
		if(inFile.isDirectory()){
			File filesInDir[] = inFile.listFiles();
			if(filesInDir!=null){
				for(File eachFile: inFile.listFiles()){
					String eachFileName = fileName+'/'+eachFile.getName();
					putZipEntry(fout, eachFile, eachFileName);
				}
			}else{
				//add empty directory (fileName must end with / (slash)
				fout.putNextEntry(new ZipEntry(fileName+'/'));
				fout.closeEntry();
			}
		}else{
			// Create a buffer for reading the files
			byte[] buffer = new byte[1024];
			// Compress the files
			FileInputStream fin = null;
			try{
				fin = new FileInputStream(inFile);
				// Add ZIP entry to output stream.
				fout.putNextEntry(new ZipEntry(fileName));
				// Transfer bytes from the file to the ZIP file
				int length = -1;
				while ((length = fin.read(buffer)) > 0) {
					fout.write(buffer, 0, length);
				}
				// Complete the entry
				fout.closeEntry();
			}finally{
				IOUtils.closeQuietly(fin);
			}
		}
	}
	
	public static void zipFile(File inFile, File outFile, String passwd) throws IOException{
		// Create a buffer for reading the files
		AesZipFileEncrypter fout = null;
		try{
			fout = new AesZipFileEncrypter(outFile);
			putZipEntry(fout, inFile, inFile.getName(), passwd);
		}finally{
			fout.close();
		}
	}
	
	public static void putZipEntry(AesZipFileEncrypter fout, File inFile, String fileName, String passwd) throws IOException{
		if(inFile.isDirectory()){
			File filesInDir[] = inFile.listFiles();
			if(filesInDir!=null){
				for(File eachFile: inFile.listFiles()){
					String eachFileName = fileName+'/'+eachFile.getName();
					putZipEntry(fout, eachFile, eachFileName, passwd);
				}
			}else{
				//add empty directory (fileName must end with / (slash)
				fout.add(inFile, fileName+'/', passwd);
			}
		}else{
			fout.add(inFile, fileName, passwd);
		}
	}
	
	
	public static void main(String[] args) throws IOException {
//		ZipUtil.zipFile(new File("D:/tmp/test"), new File("D:/tmp/test.zip")); 
		ZipUtils.zipFile(new File("D:/tmp/test"), new File("D:/tmp/testSingle.zip"), "password");
	}
}
