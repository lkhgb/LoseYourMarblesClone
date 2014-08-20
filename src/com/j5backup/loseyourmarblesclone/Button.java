/**************************************************************************
 * Project : Lose Your Marbles Clone
 * File : Button.java
 * Author : j5backup
 * Purpose : Implements a button graphic and hit detection.
 *************************************************************************/

package com.j5backup.loseyourmarblesclone;

public class Button extends GraphicsWrapper{
	
	/**************************************************************************/
	/* Private members
	/**************************************************************************/
	private float m_InitialScale;
	private float m_CurrentScale;
	private float m_ScalingDelta;
	private boolean m_IsPerformingScalingDeltaAnimation;
	private boolean m_IsButtonGrowing;
	private boolean m_FinishAnimation;
	
	/**************************************************************************/
	/* Constructors
	/**************************************************************************/
	Button(Configuration config,
		   float centerX,
		   float centerY,
		   float displayWidth,
		   float displayHeight,
		   float scale,
		   String buttonTexturePathAndName){
		super(config, centerX, centerY, displayWidth, displayHeight, buttonTexturePathAndName);
		Initialize(scale);
	}
	
	Button(Configuration config,
		   float centerX,
		   float centerY,
		   float displayWidth,
		   float displayHeight,
		   float scale,
		   String buttonTexturePathAndName,
		   int textureOriginX,
		   int textureOriginY,
		   int textureWidth,
		   int textureHeight){
			super(config, centerX, centerY, displayWidth, displayHeight, buttonTexturePathAndName,
				  textureOriginX, textureOriginY, textureWidth, textureHeight);
			Initialize(scale, textureOriginX, textureOriginY, textureWidth, textureHeight);
	}
	
	/**************************************************************************/
	/* Public methods
	/**************************************************************************/
	public void Reinitialize(float scale){
		if(m_TextureRegion != null){
			Dispose();
			Initialize(scale);
		}
		else{
			Dispose();
			Initialize(scale, m_TextureOriginX, m_TextureOriginY, m_TextureWidth, m_TextureHeight);
		}
	}
	
	@Override
	public void Update(){
		if(m_IsPerformingScalingDeltaAnimation){
			UpdateScaleChangeAnimation();
		}
	}
	
	public void SetScalingDeltaForAnimation(float delta){
		m_ScalingDelta = delta;
	}
	
	public void StartScaleChangeAnimation(){
		m_IsPerformingScalingDeltaAnimation = true;
	}
	
	public void StopScaleChangeAnimation(){
		if(m_IsPerformingScalingDeltaAnimation){
			m_FinishAnimation = true;
		}
	}
	
	public boolean IsCursorInside(int x, int y){
		if( ( x >= GetLeftBorderX() && x <= GetRightBorderX() ) &&
			( y >= GetBottomBorderY() && y <= GetTopBorderY() ) ){
			return true;
		}
		return false;
	}
	
	/**************************************************************************/
	/* Private methods
	/**************************************************************************/
	private void Initialize(float scale){
		super.Initialize();
		m_InitialScale = scale;
		m_ScalingDelta = 0.018f;
		m_IsPerformingScalingDeltaAnimation = false;
		m_IsButtonGrowing = false;
		m_CurrentScale = scale;
		m_Sprite.setScale(scale, scale);
		m_FinishAnimation = false;
	}
	
	private void Initialize(float scale, int textureOriginX, int textureOriginY,
			                int textureWidth, int textureHeight){
		super.Initialize(textureOriginX, textureOriginY, textureWidth, textureHeight);
		m_InitialScale = scale;
		m_ScalingDelta = 0.018f;
		m_IsPerformingScalingDeltaAnimation = false;
		m_IsButtonGrowing = false;
		m_CurrentScale = scale;
		m_Sprite.setScale(scale, scale);
		m_FinishAnimation = false;
	}
	
	private void UpdateScaleChangeAnimation(){
		if(m_IsButtonGrowing){
			m_CurrentScale += m_ScalingDelta;
			if(m_CurrentScale >= m_InitialScale){
				m_CurrentScale = m_InitialScale;
				m_IsButtonGrowing = false;
				if(m_FinishAnimation){
					m_FinishAnimation = false;
					m_IsPerformingScalingDeltaAnimation = false;
				}
			}
		}
		else{
			m_CurrentScale -= m_ScalingDelta;
			if(m_CurrentScale <= 0.75f){
				m_CurrentScale = 0.75f;
				m_IsButtonGrowing = true;
			}
		}
		m_Sprite.setScale(m_CurrentScale, m_CurrentScale);
	}
}
