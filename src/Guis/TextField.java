package Guis;

import Font.GUIText;

public class TextField 
{
	private GUIText text;	
	
	private boolean focus;
	
	public TextField(GUIText text) 
	{
		this.text = text;
		text.setColour(0, 0, 0);
	}
	
	public void setText(String text) {
		this.text.setTextString(text);
	}

	public boolean isFocus() {
		return focus;
	}

	public void setFocus(boolean focus) {
		this.focus = focus;
	}

	public GUIText getText() {
		return text;
	}
}
