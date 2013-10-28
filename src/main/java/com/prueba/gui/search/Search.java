package com.prueba.gui.search;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.SelectItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prueba.model.ExampleBase;
import com.prueba.util.Constants;
import com.prueba.util.JSFUtils;
import com.prueba.util.introspector.BeanProxy;

public abstract class Search <T extends ExampleBase> implements Serializable{
	private static final long serialVersionUID = 1L;
	protected static final Logger LOGGER = LoggerFactory.getLogger(Search.class);
	protected List<SelectItem> fields;
	private String text;
	private String criteriaSelection;
	protected BeanProxy beanProxy;	
	
	
	public List<SelectItem> getFields() {
		if(fields == null)
		{
			createFiels();
		}
		return translate();
	}
	
	protected abstract void createFiels();
	
	private List<SelectItem> translate() {
		for(SelectItem item : fields){
			item.setLabel(JSFUtils.getStringFromBundle(item.getDescription()));
		}
		return this.fields;
	}

	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getCriteriaSelection() {
		return criteriaSelection;
	}
	
	public void setCriteriaSelection(String criteriaSelection) {
		this.criteriaSelection = criteriaSelection;
	}
	
	public String getTextSearch(){
		String result = getText();
		if (result ==null || result.length() == 0){
			return "";
		}
		if(result.contains("*")){
			result = getText().replace('*', '%');
		}
		return result;
	}
	
	public void clean(){
		setText("");
		setCriteriaSelection(Constants.ALL_CRITERIA);
	}	
	
	public abstract T createExampleSearch();
	
}
