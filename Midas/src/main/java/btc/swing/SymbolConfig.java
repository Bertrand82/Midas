package btc.swing;

import java.util.StringTokenizer;

public class SymbolConfig implements Comparable<SymbolConfig>{

	private String name;
	private boolean selected=false;
	private String comment ="";
	
	private Integer order=10000;
	
	public SymbolConfig() {
		super();
	}
	
	public SymbolConfig(String key, String isSelectedStr) {
		super();
		this.name=key;
		this.selected=(""+isSelectedStr).equalsIgnoreCase("true");
	}

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public String toString(){
		return name+";"+selected+";"+comment+";";
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int compareTo(SymbolConfig o) {
		
		return this.order.compareTo(o.order);
	}

	public void setOrder(String s) {
		if(s== null){
			return;
		}
		if(s.length()== 0){
			return;
		}
		try {
			this.order = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			
			e.printStackTrace();
		}
	}
	
	
}
