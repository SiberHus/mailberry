package com.siberhus.mailberry.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.ArrayUtils;

import com.siberhus.mailberry.model.base.SpringAuditableModel;

@Entity
@Table(name="subscriber_lists", 
	uniqueConstraints=@UniqueConstraint(columnNames = {"user_id", "list_name"}))
public class SubscriberList extends SpringAuditableModel<Long>{
	
	private static final long serialVersionUID = 1L;
	
	public static class Status{
		public static final String ACTIVE = "ACT";
		public static final String INACTIVE = "INA";
		public static final String LOCKED = "LOC";
		public static final String UNLOCKED = "UNL";
		private static final String VALUES[] = new String[]{ACTIVE,INACTIVE,LOCKED,UNLOCKED};
	}
	
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id")
	private User user;
	
	@OneToMany(mappedBy="list")
	private List<Campaign> campaignList;
	
	@NotNull @Size(max=256)
	@Column(name="list_name", length=256, nullable=false)
	private String listName;
	
	@Size(max=2048)
	@Column(name="description", length=2048)
	private String description;
	
	@NotNull
	@Column(name="status", length=16, nullable=false)
	private String status = Status.ACTIVE;
	
	@Min(value=0)
	@Column(name="field_count")
	private Integer fieldCount;
	
	@Min(value=0)
	@Column(name="subscriber_count")
	private Integer subscriberCount;
	
	@Size(max=32)
	@Column(name="field1_name", length=32)
	private String field1Name;
	
	@Size(max=32)
	@Column(name="field2_name", length=32)
	private String field2Name;
	
	@Size(max=32)
	@Column(name="field3_name", length=32)
	private String field3Name;
	
	@Size(max=32)
	@Column(name="field4_name", length=32)
	private String field4Name;
	
	@Size(max=32)
	@Column(name="field5_name", length=32)
	private String field5Name;
	
	@Size(max=32)
	@Column(name="field6_name", length=32)
	private String field6Name;
	
	@Size(max=32)
	@Column(name="field7_name", length=32)
	private String field7Name;
	
	@Size(max=32)
	@Column(name="field8_name", length=32)
	private String field8Name;
	
	@Size(max=32)
	@Column(name="field9_name", length=32)
	private String field9Name;
	
	@Size(max=32)
	@Column(name="field10_name", length=32)
	private String field10Name;
	
	@Size(max=32)
	@Column(name="field11_name", length=32)
	private String field11Name;
	
	@Size(max=32)
	@Column(name="field12_name", length=32)
	private String field12Name;
	
	@Size(max=32)
	@Column(name="field13_name", length=32)
	private String field13Name;
	
	@Size(max=32)
	@Column(name="field14_name", length=32)
	private String field14Name;
	
	@Size(max=32)
	@Column(name="field15_name", length=32)
	private String field15Name;
	
	@Size(max=32)
	@Column(name="field16_name", length=32)
	private String field16Name;
	
	@Size(max=32)
	@Column(name="field17_name", length=32)
	private String field17Name;
	
	@Size(max=32)
	@Column(name="field18_name", length=32)
	private String field18Name;
	
	@Size(max=32)
	@Column(name="field19_name", length=32)
	private String field19Name;
	
	@Size(max=32)
	@Column(name="field20_name", length=32)
	private String field20Name;
	
	@OneToMany(mappedBy="list", cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
	@OrderBy("fieldNumber")
	private List<FieldValidator> fieldValidators;
	
	@OneToMany(mappedBy="list", cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
	protected List<Subscriber> subscribers;
	
	@OneToMany(mappedBy="list", cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
	protected List<FileImport> fileImports;
	
	@Override
	public String toString(){
		return listName;
	}
	
	@Transient
	public boolean isLocked(){
		return status.equals(Status.LOCKED);
	}
	
	public Map<String, Integer> getFieldNameNumbers(){
		return new HashMap<String, Integer>(){
			private static final long serialVersionUID = 1L;
		{
			if(getField1Name()!=null)put(getField1Name(), 1);
			if(getField2Name()!=null)put(getField2Name(), 2);
			if(getField3Name()!=null)put(getField3Name(), 3);
			if(getField4Name()!=null)put(getField4Name(), 4);
			if(getField5Name()!=null)put(getField5Name(), 5);
			if(getField6Name()!=null)put(getField6Name(), 6);
			if(getField7Name()!=null)put(getField7Name(), 7);
			if(getField8Name()!=null)put(getField8Name(), 8);
			if(getField9Name()!=null)put(getField9Name(), 9);
			if(getField10Name()!=null)put(getField10Name(), 10);
			if(getField11Name()!=null)put(getField11Name(), 11);
			if(getField12Name()!=null)put(getField12Name(), 12);
			if(getField13Name()!=null)put(getField13Name(), 13);
			if(getField14Name()!=null)put(getField14Name(), 14);
			if(getField15Name()!=null)put(getField15Name(), 15);
			if(getField16Name()!=null)put(getField16Name(), 16);
			if(getField17Name()!=null)put(getField17Name(), 17);
			if(getField18Name()!=null)put(getField18Name(), 18);
			if(getField19Name()!=null)put(getField19Name(), 19);
			if(getField20Name()!=null)put(getField20Name(), 20);
		}};
	}
	
	@Transient
	public void setFieldNames(List<String> fieldNames){
		for(int i=1;i<=fieldNames.size();i++){
			String fieldName = fieldNames.get(i-1);
			_setFieldNameAt(i, fieldName);
		}
		for(int i=20;i>fieldNames.size();i--){
			_setFieldNameAt(i, null);
		}
	}
	
	private void _setFieldNameAt(int pos, String fieldName){
		switch(pos){
		case 1: setField1Name(fieldName);break;
		case 2: setField2Name(fieldName);break;
		case 3: setField3Name(fieldName);break;
		case 4: setField4Name(fieldName);break;
		case 5: setField5Name(fieldName);break;
		case 6: setField6Name(fieldName);break;
		case 7: setField7Name(fieldName);break;
		case 8: setField8Name(fieldName);break;
		case 9: setField9Name(fieldName);break;
		case 10: setField10Name(fieldName);break;
		case 11: setField11Name(fieldName);break;
		case 12: setField12Name(fieldName);break;
		case 13: setField13Name(fieldName);break;
		case 14: setField14Name(fieldName);break;
		case 15: setField15Name(fieldName);break;
		case 16: setField16Name(fieldName);break;
		case 17: setField17Name(fieldName);break;
		case 18: setField18Name(fieldName);break;
		case 19: setField19Name(fieldName);break;
		case 20: setField20Name(fieldName);break;
		}
	}
	
	@Transient
	public List<String> getFieldNames(){
		List<String> fieldList = new ArrayList<String>(){
			private static final long serialVersionUID = 1L;
		{
			if(getField1Name()!=null)add(getField1Name());
			if(getField2Name()!=null)add(getField2Name());
			if(getField3Name()!=null)add(getField3Name());
			if(getField4Name()!=null)add(getField4Name());
			if(getField5Name()!=null)add(getField5Name());
			if(getField6Name()!=null)add(getField6Name());
			if(getField7Name()!=null)add(getField7Name());
			if(getField8Name()!=null)add(getField8Name());
			if(getField9Name()!=null)add(getField9Name());
			if(getField10Name()!=null)add(getField10Name());
			if(getField11Name()!=null)add(getField11Name());
			if(getField12Name()!=null)add(getField12Name());
			if(getField13Name()!=null)add(getField13Name());
			if(getField14Name()!=null)add(getField14Name());
			if(getField15Name()!=null)add(getField15Name());
			if(getField16Name()!=null)add(getField16Name());
			if(getField17Name()!=null)add(getField17Name());
			if(getField18Name()!=null)add(getField18Name());
			if(getField19Name()!=null)add(getField19Name());
			if(getField20Name()!=null)add(getField20Name());
		}};
		return fieldList;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Campaign> getCampaignList() {
		return campaignList;
	}

	public void setCampaignList(List<Campaign> campaignList) {
		this.campaignList = campaignList;
	}

	public String getListName() {
		return listName;
	}
	
	public void setListName(String listName) {
		this.listName = listName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	
	public Integer getFieldCount() {
		return fieldCount;
	}

	public void setFieldCount(Integer fieldCount) {
		this.fieldCount = fieldCount;
	}
	
	public Integer getSubscriberCount() {
		return subscriberCount;
	}

	public void setSubscriberCount(Integer subscriberCount) {
		this.subscriberCount = subscriberCount;
	}

	public String getField1Name() {
		return field1Name;
	}

	public void setField1Name(String field1Name) {
		this.field1Name = field1Name;
	}

	public String getField2Name() {
		return field2Name;
	}

	public void setField2Name(String field2Name) {
		this.field2Name = field2Name;
	}

	public String getField3Name() {
		return field3Name;
	}

	public void setField3Name(String field3Name) {
		this.field3Name = field3Name;
	}

	public String getField4Name() {
		return field4Name;
	}

	public void setField4Name(String field4Name) {
		this.field4Name = field4Name;
	}

	public String getField5Name() {
		return field5Name;
	}

	public void setField5Name(String field5Name) {
		this.field5Name = field5Name;
	}

	public String getField6Name() {
		return field6Name;
	}

	public void setField6Name(String field6Name) {
		this.field6Name = field6Name;
	}

	public String getField7Name() {
		return field7Name;
	}

	public void setField7Name(String field7Name) {
		this.field7Name = field7Name;
	}

	public String getField8Name() {
		return field8Name;
	}

	public void setField8Name(String field8Name) {
		this.field8Name = field8Name;
	}

	public String getField9Name() {
		return field9Name;
	}

	public void setField9Name(String field9Name) {
		this.field9Name = field9Name;
	}

	public String getField10Name() {
		return field10Name;
	}
	public void setField10Name(String field10Name) {
		this.field10Name = field10Name;
	}
	public String getField11Name() {
		return field11Name;
	}
	public void setField11Name(String field11Name) {
		this.field11Name = field11Name;
	}
	public String getField12Name() {
		return field12Name;
	}
	public void setField12Name(String field12Name) {
		this.field12Name = field12Name;
	}
	public String getField13Name() {
		return field13Name;
	}
	public void setField13Name(String field13Name) {
		this.field13Name = field13Name;
	}
	public String getField14Name() {
		return field14Name;
	}
	public void setField14Name(String field14Name) {
		this.field14Name = field14Name;
	}
	public String getField15Name() {
		return field15Name;
	}
	public void setField15Name(String field15Name) {
		this.field15Name = field15Name;
	}
	public String getField16Name() {
		return field16Name;
	}
	public void setField16Name(String field16Name) {
		this.field16Name = field16Name;
	}
	public String getField17Name() {
		return field17Name;
	}
	public void setField17Name(String field17Name) {
		this.field17Name = field17Name;
	}
	public String getField18Name() {
		return field18Name;
	}
	public void setField18Name(String field18Name) {
		this.field18Name = field18Name;
	}
	public String getField19Name() {
		return field19Name;
	}
	public void setField19Name(String field19Name) {
		this.field19Name = field19Name;
	}
	public String getField20Name() {
		return field20Name;
	}
	public void setField20Name(String field20Name) {
		this.field20Name = field20Name;
	}

	public List<FieldValidator> getFieldValidators() {
		return fieldValidators;
	}
	public void setFieldValidators(List<FieldValidator> fieldValidators) {
		this.fieldValidators = fieldValidators;
	}
	
}
