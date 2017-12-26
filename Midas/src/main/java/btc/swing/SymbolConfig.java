package btc.swing;

import java.util.StringTokenizer;

public class SymbolConfig implements Comparable<SymbolConfig>{

	private String name;
	private boolean selected=false;
	private String comment ="";
	
	
	
	public SymbolConfig() {
		super();
	}
	
	public SymbolConfig(String str) {
		super();
		StringTokenizer st = new StringTokenizer(str, ";");
		if (st.hasMoreTokens()){
			this.name=st.nextToken();
		}
		
		if (st.hasMoreTokens()){
			this.selected=st.nextToken().equalsIgnoreCase("true");
		}
		if (st.hasMoreTokens()){
			this.comment=st.nextToken();
		}

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
		
		return this.name.compareTo(o.name);
	}
	
	
}
