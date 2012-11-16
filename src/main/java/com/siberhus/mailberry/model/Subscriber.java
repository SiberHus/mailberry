package com.siberhus.mailberry.model;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.validator.constraints.Email;

import com.siberhus.mailberry.model.base.SpringAuditableModel;

@Entity
@Table(name="subscribers", uniqueConstraints=
	@UniqueConstraint(columnNames = {"list_id", "email"}))
public class Subscriber extends SpringAuditableModel<Long>{
	
	private static final long serialVersionUID = 1L;
	
	public static class Status{
		public static final String ACTIVE = "ACT";
		public static final String INACTIVE = "INA";
		public static final String UNSUBSCRIBED = "UNS";
		public static final String BLOCKED = "BLO";
		public static final String TEST = "TES";
		private static final String VALUES[] = new String[]{ACTIVE,INACTIVE,UNSUBSCRIBED,BLOCKED,TEST};
	}
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="list_id", referencedColumnName="id", nullable=false)
	private SubscriberList list;
	
	@NotNull @Size(max=256) @Email
	@Column(name="email", length=256, nullable=false)
	private String email;
	
	@NotNull
	@Column(name="status", length=16, nullable=false)
	private String status = Status.ACTIVE;
	
	@Size(max=512)
	@Column(name="field1_value", length=512)
	private String field1Value;
	
	@Size(max=512)
	@Column(name="field2_value", length=512)
	private String field2Value;
	
	@Size(max=512)
	@Column(name="field3_value", length=512)
	private String field3Value;
	
	@Size(max=512)
	@Column(name="field4_value", length=512)
	private String field4Value;
	
	@Size(max=512)
	@Column(name="field5_value", length=512)
	private String field5Value;
	
	@Size(max=512)
	@Column(name="field6_value", length=512)
	private String field6Value;
	
	@Size(max=512)
	@Column(name="field7_value", length=512)
	private String field7Value;
	
	@Size(max=512)
	@Column(name="field8_value", length=512)
	private String field8Value;
	
	@Size(max=512)
	@Column(name="field9_value", length=512)
	private String field9Value;
	
	@Size(max=512)
	@Column(name="field10_value", length=512)
	private String field10Value;
	
	@Size(max=512)
	@Column(name="field11_value", length=512)
	private String field11Value;
	
	@Size(max=512)
	@Column(name="field12_value", length=512)
	private String field12Value;
	
	@Size(max=512)
	@Column(name="field13_value", length=512)
	private String field13Value;
	
	@Size(max=512)
	@Column(name="field14_value", length=512)
	private String field14Value;
	
	@Size(max=512)
	@Column(name="field15_value", length=512)
	private String field15Value;
	
	@Size(max=512)
	@Column(name="field16_value", length=512)
	private String field16Value;
	
	@Size(max=512)
	@Column(name="field017_value", length=512)
	private String field17Value;
	
	@Size(max=512)
	@Column(name="field18_value", length=512)
	private String field18Value;
	
	@Size(max=512)
	@Column(name="field19_value", length=512)
	private String field19Value;
	
	@Size(max=512)
	@Column(name="field20_value", length=512)
	private String field20Value;
	
	@Override
	public String toString(){
		return email;
	}
	
	public SubscriberList getList() {
		return list;
	}

	public void setList(SubscriberList list) {
		this.list = list;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
	
	public String getField1Value() {
		return field1Value;
	}

	public void setField1Value(String field1Value) {
		this.field1Value = field1Value;
	}

	public String getField2Value() {
		return field2Value;
	}

	public void setField2Value(String field2Value) {
		this.field2Value = field2Value;
	}

	public String getField3Value() {
		return field3Value;
	}

	public void setField3Value(String field3Value) {
		this.field3Value = field3Value;
	}

	public String getField4Value() {
		return field4Value;
	}

	public void setField4Value(String field4Value) {
		this.field4Value = field4Value;
	}

	public String getField5Value() {
		return field5Value;
	}

	public void setField5Value(String field5Value) {
		this.field5Value = field5Value;
	}

	public String getField6Value() {
		return field6Value;
	}

	public void setField6Value(String field6Value) {
		this.field6Value = field6Value;
	}

	public String getField7Value() {
		return field7Value;
	}

	public void setField7Value(String field7Value) {
		this.field7Value = field7Value;
	}

	public String getField8Value() {
		return field8Value;
	}

	public void setField8Value(String field8Value) {
		this.field8Value = field8Value;
	}

	public String getField9Value() {
		return field9Value;
	}

	public void setField9Value(String field9Value) {
		this.field9Value = field9Value;
	}

	public String getField10Value() {
		return field10Value;
	}

	public void setField10Value(String field10Value) {
		this.field10Value = field10Value;
	}

	public String getField11Value() {
		return field11Value;
	}

	public void setField11Value(String field11Value) {
		this.field11Value = field11Value;
	}

	public String getField12Value() {
		return field12Value;
	}

	public void setField12Value(String field12Value) {
		this.field12Value = field12Value;
	}

	public String getField13Value() {
		return field13Value;
	}

	public void setField13Value(String field13Value) {
		this.field13Value = field13Value;
	}

	public String getField14Value() {
		return field14Value;
	}

	public void setField14Value(String field14Value) {
		this.field14Value = field14Value;
	}

	public String getField15Value() {
		return field15Value;
	}

	public void setField15Value(String field15Value) {
		this.field15Value = field15Value;
	}

	public String getField16Value() {
		return field16Value;
	}

	public void setField16Value(String field16Value) {
		this.field16Value = field16Value;
	}

	public String getField17Value() {
		return field17Value;
	}

	public void setField17Value(String field17Value) {
		this.field17Value = field17Value;
	}

	public String getField18Value() {
		return field18Value;
	}

	public void setField18Value(String field18Value) {
		this.field18Value = field18Value;
	}

	public String getField19Value() {
		return field19Value;
	}

	public void setField19Value(String field19Value) {
		this.field19Value = field19Value;
	}

	public String getField20Value() {
		return field20Value;
	}

	public void setField20Value(String field20Value) {
		this.field20Value = field20Value;
	}
	
}
