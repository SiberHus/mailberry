package com.siberhus.mailberry.model;

import java.io.File;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.siberhus.mailberry.impexp.FileType;
import com.siberhus.mailberry.model.base.SpringAuditableModel;

@Entity
@Table(name="file_imports")
public class FileImport extends SpringAuditableModel<Long>{

	private static final long serialVersionUID = 1L;
	
	public static class Status {
		public static final String PREPARED = "PREP";
		public static final String PROCESSING = "PROC";
		public static final String DONE = "DONE";
		private static final String VALUES[] = new String[]{PREPARED,PROCESSING,DONE}; 
	}
	
	@ManyToOne
	@JoinColumn(name="list_id", referencedColumnName="id", nullable=false)
	private SubscriberList list;
	
	@Column(name="original_name", length=128, nullable=false)
	private String originalName;
	
	@Enumerated(EnumType.STRING)
	@Column(name="file_type", length=8, nullable=false)
	private FileType fileType;
	
	@Column(name="source_file_path", length=512, nullable=false)
	private String sourceFilePath;
	
	@Column(name="error_file_path", length=512, nullable=false)
	private String errorFilePath;
	
	@Column(name="tracking_id", length=8, nullable=false)
	private String trackingId;
	
	@Column(name="fatal_error", length=512)
	private String fatalError;
	
	@Column(name="success_count")
	private int success = -1;
	
	@Column(name="error_count")
	private int error = -1;
	
	@Column(name="updated_count")
	private int updated = -1;
	
	@Column(name="created_count")
	private int created = -1;
	
	@Column(name="status", length=4)
	private String status = Status.PREPARED;
	
	@Transient
	private File sourceFile;
	
	@Transient
	private File errorFile;
	
	public boolean isHasErrorFile(){
		return getErrorFile().exists();
	}
	
	public boolean isHasSourceFile(){
		return getSourceFile().exists();
	}
	
	public SubscriberList getList() {
		return list;
	}

	public void setList(SubscriberList list) {
		this.list = list;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	
	public FileType getFileType() {
		return fileType;
	}

	public void setFileType(FileType fileType) {
		this.fileType = fileType;
	}

	public String getSourceFilePath() {
		return sourceFilePath;
	}

	public void setSourceFilePath(String sourceFilePath) {
		this.sourceFilePath = sourceFilePath;
	}

	public String getErrorFilePath() {
		return errorFilePath;
	}

	public void setErrorFilePath(String errorFilePath) {
		this.errorFilePath = errorFilePath;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if(!ArrayUtils.contains(Status.VALUES, status)){
			throw new IllegalArgumentException("status must be any of "
					+Arrays.toString(Status.VALUES));
		}
		this.status = status;
	}
	
	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	public String getFatalError() {
		return fatalError;
	}

	public void setFatalError(String fatalError) {
		this.fatalError = StringUtils.abbreviate(fatalError, 500);
	}
	
	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public int getUpdated() {
		return updated;
	}

	public void setUpdated(int updated) {
		this.updated = updated;
	}

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}

	public File getSourceFile() {
		if(sourceFile==null){
			sourceFile = new File(sourceFilePath);
		}
		return sourceFile;
	}

	public void setSourceFile(File sourceFile) {
		this.sourceFilePath = sourceFile.getAbsolutePath();
		this.sourceFile = sourceFile;
	}

	public File getErrorFile() {
		if(errorFile==null){
			errorFile = new File(errorFilePath);
		}
		return errorFile;
	}
	
	public void setErrorFile(File errorFile) {
		this.errorFilePath = errorFile.getAbsolutePath();
		this.errorFile = errorFile;
	}
	
	
}
