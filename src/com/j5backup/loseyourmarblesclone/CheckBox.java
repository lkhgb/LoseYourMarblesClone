/**************************************************************************
 * Project : Lose Your Marbles Clone
 * File : CheckBox.java
 * Author : j5backup
 * Purpose : Implements a check box graphic.
 *************************************************************************/

package com.j5backup.loseyourmarblesclone;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CheckBox{
	
	/**************************************************************************/
	/* Private members
	/**************************************************************************/
	private Configuration m_Configuration;
	private float m_CenterX;
	private float m_CenterY;
	private float m_DisplayWidth;
	private float m_DisplayHeight;
	private String m_EmptyCheckBoxTexturePathAndName;
	private String m_FilledCheckBoxTexturePathAndName;
	private boolean m_IsChecked;
	private GraphicsWrapper m_EmptyCheckBox;
	private GraphicsWrapper m_FilledCheckBox;
	
	/**************************************************************************/
	/* Constructor
	/**************************************************************************/
	CheckBox(Configuration config,
			 float centerX,
			 float centerY,
			 float displayWidth,
			 float displayHeight,
			 String emptyCheckBoxTexturePathAndName,
			 String filledCheckBoxTexturePathAndName){
		m_Configuration = config;
		m_CenterX = centerX;
		m_CenterY = centerY;
		m_DisplayWidth = displayWidth;
		m_DisplayHeight = displayHeight;
		m_EmptyCheckBoxTexturePathAndName = emptyCheckBoxTexturePathAndName;
		m_FilledCheckBoxTexturePathAndName = filledCheckBoxTexturePathAndName;
		Initialize();
	}
	
	/**************************************************************************/
	/* Public methods
	/**************************************************************************/
	public void Reinitialize(){
		Dispose();
		Initialize();
	}
	
	public void Render(SpriteBatch batch){
		if(!m_IsChecked){
			if(m_EmptyCheckBox != null){
				m_EmptyCheckBox.Render(batch);
			}
		}
		else{
			if(m_FilledCheckBox != null){
				m_FilledCheckBox.Render(batch);
			}
		}
	}
	
	public void Dispose(){
		if(m_EmptyCheckBox != null){
			m_EmptyCheckBox.Dispose();
		}
		if(m_FilledCheckBox != null){
			m_FilledCheckBox.Dispose();
		}
	}

	public void SetChecked(){
		m_IsChecked = true;
	}
	
	public void SetUnchecked(){
		m_IsChecked = false;
	}
	
	public void Hide(){
		m_EmptyCheckBox.SetOpacity(0f);
		m_FilledCheckBox.SetOpacity(0f);
	}
	
	public void Show(){
		m_EmptyCheckBox.SetOpacity(1f);
		m_FilledCheckBox.SetOpacity(1f);
	}
	
	/**************************************************************************/
	/* Private methods
	/**************************************************************************/
	private void Initialize(){
		m_IsChecked = false;
		
		m_EmptyCheckBox =
				new GraphicsWrapper(m_Configuration,
									m_CenterX,
									m_CenterY,
									m_DisplayWidth,
									m_DisplayHeight,
									m_EmptyCheckBoxTexturePathAndName);
		
		m_FilledCheckBox =
				new GraphicsWrapper(m_Configuration,
									m_CenterX,
									m_CenterY,
									m_DisplayWidth,
									m_DisplayHeight,
									m_FilledCheckBoxTexturePathAndName);
	}
}
