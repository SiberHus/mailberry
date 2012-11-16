package com.siberhus.mailberry.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.siberhus.mailberry.model.base.AbstractModel;

@Entity
@Table(name="attachments")
public class Attachment extends AbstractModel<Long>{

	private static final long serialVersionUID = 1L;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="campaign_id", referencedColumnName="id", nullable=false)
	private Campaign campaign;
	
	@NotNull @Size(max=256)
	@Column(name="file_name", length=256, nullable=false)
	private String fileName;
	
	@NotNull @Size(max=512)
	@Column(name="file_path", length=512, nullable=false)
	private String filePath;
	
	@Column(name="compressed")
	private boolean compressed = false;
	
	@Column(name="archive_passwd")
	private String archivePasswd;
	
	public Attachment(){}
	public Attachment(String fileName, String filePath){
		this.fileName = fileName;
		this.filePath = filePath;
	}
	public Attachment(String fileName, String filePath, boolean compressed, String archivePasswd){
		this.fileName = fileName;
		this.filePath = filePath;
		this.compressed = compressed;
		this.archivePasswd = archivePasswd;
	}
	
	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isCompressed() {
		return compressed;
	}

	public void setCompressed(boolean compressed) {
		this.compressed = compressed;
	}

	public String getArchivePasswd() {
		return archivePasswd;
	}

	public void setArchivePasswd(String archivePasswd) {
		this.archivePasswd = archivePasswd;
	}
	
}
