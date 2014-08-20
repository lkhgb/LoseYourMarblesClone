/**************************************************************************
 * Project : Lose Your Marbles Clone
 * File : GraphicsWrapper.java
 * Author : j5backup
 * Purpose : Base class for sprites.
 *************************************************************************/

package com.j5backup.loseyourmarblesclone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GraphicsWrapper{
	
	/**************************************************************************/
	/* Protected members
	/**************************************************************************/
	protected float m_OriginX;
	protected float m_OriginY;
	protected String m_TexturePathAndName;
	protected float m_CenterX;
	protected float m_CenterY;
	protected float m_DisplayWidth;
	protected float m_DisplayHeight;
	protected float m_ScaleX;
	protected float m_ScaleY;
	protected Texture m_Texture;
	protected TextureRegion m_TextureRegion;
	protected int m_TextureOriginX;
	protected int m_TextureOriginY;
	protected int m_TextureWidth;
	protected int m_TextureHeight;
	protected Sprite m_Sprite;
	protected float m_Opacity;
	protected Configuration m_Configuration;
	
	/**************************************************************************/
	/* Constructors
	/**************************************************************************/
	GraphicsWrapper(Configuration config, float spriteCenterX, float spriteCenterY, 
			        float spriteDisplayWidth, float spriteDisplayHeight, String texturePathAndName){
		m_Configuration = config;
		m_CenterX = spriteCenterX;
		m_CenterY = spriteCenterY;
		m_DisplayWidth = spriteDisplayWidth;
		m_DisplayHeight = spriteDisplayHeight;
		m_TexturePathAndName = texturePathAndName;
		Initialize();
	}
	
	GraphicsWrapper(Configuration config, float spriteCenterX, float spriteCenterY,
			        float spriteDisplayWidth, float spriteDisplayHeight, String texturePathAndName,
					int textureOriginX, int textureOriginY, int textureWidth, int textureHeight){
		m_Configuration = config;
		m_CenterX = spriteCenterX;
		m_CenterY = spriteCenterY;
		m_DisplayWidth = spriteDisplayWidth;
		m_DisplayHeight = spriteDisplayHeight;
		m_TexturePathAndName = texturePathAndName;
		m_TextureOriginX = textureOriginX;
		m_TextureOriginY = textureOriginY;
		m_TextureWidth = textureWidth;
		m_TextureHeight = textureHeight;
		Initialize(textureOriginX, textureOriginY, textureWidth, textureHeight);
	}
	
	/**************************************************************************/
	/* Public methods
	/**************************************************************************/
	public void Reinitialize(){
		Dispose();
		if(m_TextureRegion != null){
			Initialize(m_TextureOriginX, m_TextureOriginY, m_TextureWidth, m_TextureHeight);
		}
		else{
			Initialize();
		}
	}
	
	public void SetSize(float sizeX, float sizeY){
		m_DisplayWidth = sizeX;
		m_DisplayHeight = sizeY;
		m_Sprite.setSize(sizeX, sizeY);
	}
	
	public void SetScale(float scaleX, float scaleY){
		m_ScaleX = scaleX;
		m_ScaleY = scaleY;
		m_Sprite.setScale(scaleX, scaleY);
	}
	
	public void SetPositionCenter(float x, float y){
		m_CenterX = x;
		m_CenterY = y;
		m_Sprite.setPosition(m_CenterX - m_Sprite.getWidth()/2, m_CenterY - m_Sprite.getHeight()/2);
	}
	
	public void SetOrigin(float x, float y){
		m_Sprite.setOrigin(x, y);
	}
	
	public void Hide(){
		m_Sprite.setColor(1f, 1f, 1f, 0f);
	}
	
	public void Show(){
		m_Sprite.setColor(1f, 1f, 1f, m_Opacity);
	}
	
	public float GetCenterX(){
		return m_CenterX;
	}
	
	public float GetCenterY(){
		return m_CenterY;
	}
	
	public float GetWidth(){
		return m_Sprite.getWidth();
	}
	
	public float GetHeight(){
		return m_Sprite.getHeight();
	}
	
	public void SetOpacity(float opacity){
		m_Opacity = opacity;
		m_Sprite.setColor(1f, 1f, 1f, opacity);
	}
	
	public void Rotate(float rotateDegrees){
		m_Sprite.rotate(rotateDegrees);
	}
	
	public float GetRightBorderX(){
		return m_CenterX + m_Sprite.getWidth()/2f;
	}
	
	public float GetLeftBorderX(){
		return m_CenterX - m_Sprite.getWidth()/2f;
	}
	
	public float GetTopBorderY(){
		return m_CenterY + m_Sprite.getHeight()/2f;
	}
	
	public float GetBottomBorderY(){
		return m_CenterY - m_Sprite.getHeight()/2f;
	}
	
	public float GetSizeX(){
		return m_Sprite.getWidth();
	}
	
	public float GetSizeY(){
		return m_Sprite.getHeight();
	}
	
	public void Render(SpriteBatch batch){
		if(m_Sprite != null){
			m_Sprite.draw(batch);
		}
	}
	
	public void Dispose(){
		if(m_Texture != null){
			m_Texture.dispose();
		}
	}
	
	/**************************************************************************/
	/* Protected methods
	/**************************************************************************/
	protected void Initialize(){
		m_Texture = new Texture( Gdx.files.internal(m_TexturePathAndName) );
		m_Texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		m_Sprite = new Sprite(m_Texture);
		SetSize(m_DisplayWidth, m_DisplayHeight);
		SetOrigin(m_DisplayWidth/2, m_DisplayHeight/2);
		SetPositionCenter(m_CenterX, m_CenterY);
		m_Opacity = 1f;
	}
	
	protected void Initialize(int textureOriginX, int textureOriginY, int textureWidth, int textureHeight){
		m_Texture = new Texture( Gdx.files.internal(m_TexturePathAndName) );
		m_Texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		m_TextureRegion = 
				new TextureRegion(m_Texture,
								  textureOriginX,
								  textureOriginY,
								  textureWidth,
								  textureHeight);
		m_Sprite = new Sprite(m_TextureRegion);
		SetSize(m_DisplayWidth, m_DisplayHeight);
		SetOrigin(m_DisplayWidth/2, m_DisplayHeight/2);
		SetPositionCenter(m_CenterX, m_CenterY);
		m_Opacity = 1f;
	}
	
	protected void Update(){
		// Override and provide updates as required
	}
}
