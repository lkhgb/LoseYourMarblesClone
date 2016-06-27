/**************************************************************************
 * Project : Lose Your Marbles Clone
 * File : Marble.java
 * Author : j5backup
 * Purpose : Implements graphics responsibilities for a game marble.
 *************************************************************************/

package com.j5backup.loseyourmarblesclone;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Marble extends GraphicsWrapper{

	/**************************************************************************/
	/* Enumerations
	/**************************************************************************/
	public enum COLOR{
		RED, BLUE, GREEN, YELLOW, BLACK, PURPLE
	}
	
	/**************************************************************************/
	/* Private members
	/**************************************************************************/
	private boolean m_IsSpawning;
	private boolean m_IsDespawning;
	private int m_SpawnBlinkCounter;
	private COLOR m_Color;
	private float m_Scale;
	private int m_CurrentRollingAnimationFrameNumber;
	private boolean m_IsDespawnComplete;
	private MarbleTextures m_MarbleTextures;
	private int m_NumberOfFrames;
	
	/**************************************************************************/
	/* Constructors
	/**************************************************************************/
	Marble(Configuration config,
		   COLOR color,
		   float spriteCenterX,
		   float spriteCenterY,
		   float spriteDisplayWidth,
		   float spriteDisplayHeight,
		   MarbleTextures marbleTextures,
		   boolean playSpawnAnimation){
		
		super( config, spriteCenterX, spriteCenterY,
			   spriteDisplayWidth, spriteDisplayHeight,
			   GetImagePathAndName(color) );
		m_Color = color;
		m_MarbleTextures = marbleTextures;
		m_NumberOfFrames = m_MarbleTextures.GetNumberOfFramesUsed();
		m_CurrentRollingAnimationFrameNumber = GenerateRandomInteger(0, m_NumberOfFrames-1);
		m_Sprite.setTexture( m_MarbleTextures.GetTexture(color, m_CurrentRollingAnimationFrameNumber) );
		m_Opacity = 0.0f;
		m_IsSpawning = true;
		m_IsDespawning = false;
		m_SpawnBlinkCounter = 0;
		m_IsDespawnComplete = false;
		m_Scale = 1f;
	}
	
	Marble(Configuration config,
	       COLOR color,
	       float spriteCenterX,
	       float spriteCenterY,
	       float spriteDisplayWidth,
	       float spriteDisplayHeight,
	       boolean playSpawnAnimation){
		
		super( config, spriteCenterX, spriteCenterY,
			   spriteDisplayWidth, spriteDisplayHeight,
			   GetImagePathAndName(color) );
		m_Color = color;		
		m_Opacity = 0.0f;
		m_IsSpawning = true;
		m_IsDespawning = false;
		m_SpawnBlinkCounter = 0;
		m_IsDespawnComplete = false;
		m_Scale = 1f;
	}
	
	/**************************************************************************/
	/* Static methods
	/**************************************************************************/
	static public String GetImagePathAndName(COLOR color){
		switch(color){
			case RED:
				return Configuration.RED_MARBLE_IMAGE_0000;
			case BLUE:
				return Configuration.BLUE_MARBLE_IMAGE_0000;
			case GREEN:
				return Configuration.GREEN_MARBLE_IMAGE_0000;
			case YELLOW:
				return Configuration.YELLOW_MARBLE_IMAGE_0000;
			case BLACK:
				return Configuration.BLACK_MARBLE_IMAGE_0000;
			case PURPLE:
				return Configuration.PURPLE_MARBLE_IMAGE_0000;
			default:
				return "";
		}
	}
	
	static public Marble.COLOR GenerateRandomColor(){
		int val = (int) Math.floor(Math.random()*6);
		switch(val){
		case 0:
			return Marble.COLOR.RED;
		case 1:
			return Marble.COLOR.BLUE;
		case 2:
			return Marble.COLOR.GREEN;
		case 3:
			return Marble.COLOR.YELLOW;
		case 4:
			return Marble.COLOR.BLACK;
		case 5:
			return Marble.COLOR.PURPLE;
		default:
			return Marble.COLOR.RED;
		}
	}
	
	static public Marble.COLOR GenerateRandomColorOrNull(){
		int val = (int) Math.floor(Math.random()*7);
		switch(val){
		case 0:
			return Marble.COLOR.RED;
		case 1:
			return Marble.COLOR.BLUE;
		case 2:
			return Marble.COLOR.GREEN;
		case 3:
			return Marble.COLOR.YELLOW;
		case 4:
			return Marble.COLOR.BLACK;
		case 5:
			return Marble.COLOR.PURPLE;
		case 6:
			return null;
		default:
			return null;
		}
	}
	
	/**************************************************************************/
	/* Public methods
	/**************************************************************************/
	public void Render(SpriteBatch batch){
		if(m_IsSpawning){
			PlaySpawnAnimation();
		}
		else if(m_IsDespawning){
			PlayDespawnAnimation();
		}
		m_Sprite.draw(batch);
	}
	
	public boolean IsDespawned(){
		return m_IsDespawnComplete;
	}
	
	public boolean IsDespawning(){
		return m_IsDespawning;
	}
	
	public void IncrementNextRollingUpAnimationFrame(){
		m_CurrentRollingAnimationFrameNumber++;
		if(m_CurrentRollingAnimationFrameNumber >= m_NumberOfFrames){
			m_CurrentRollingAnimationFrameNumber = 0;
		}
		m_Sprite.setTexture( m_MarbleTextures.GetTexture(m_Color, m_CurrentRollingAnimationFrameNumber) );
	}
	
	public void DecrementNextRollingUpAnimationFrame(){
		m_CurrentRollingAnimationFrameNumber--;
		if(m_CurrentRollingAnimationFrameNumber < 0){
			m_CurrentRollingAnimationFrameNumber = m_NumberOfFrames-1;
		}
		m_Sprite.setTexture( m_MarbleTextures.GetTexture(m_Color, m_CurrentRollingAnimationFrameNumber) );
	}
	
	public void StartDespawnAnimation(){
		m_IsDespawning = true;
	}
	
	public void Dispose(){
		if(m_Texture != null){
			m_Texture.dispose();
		}
	}
	
	public COLOR GetColor(){
		return m_Color;
	}
	
	/**************************************************************************/
	/* Private methods
	/**************************************************************************/
	private void PlaySpawnAnimation(){	
		m_Opacity += 0.25f;
		if(m_Opacity >= 1.0f && m_SpawnBlinkCounter < Configuration.NUMBER_OF_MARBLE_SPAWN_BLINKS){
			m_Opacity = 0f;
			m_SpawnBlinkCounter++;
		}	
		else if(m_Opacity >= 1.0f){
			m_Opacity = 1.0f;
			m_IsSpawning = false;
		}
			
		SetOpacity(m_Opacity);
	}
	
	private void PlayDespawnAnimation(){
		m_Scale /= 1.3;
		if(m_Scale<0.02){
			m_IsDespawning = false;
			m_IsDespawnComplete = true;
		}
		else{
			SetScale(m_Scale, m_Scale);
		}
	}
	
	private int GenerateRandomInteger(int lowThreshold, int highThreshold){
		int val = highThreshold+1;
		while(val > highThreshold){
			val = (int) Math.floor(Math.random()*((highThreshold-lowThreshold)+1)) + lowThreshold;
		}
		return val;
	}
}
