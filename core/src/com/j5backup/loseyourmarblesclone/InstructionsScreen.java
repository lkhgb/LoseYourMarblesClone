/**************************************************************************
 * Project : Lose Your Marbles Clone
 * File : InstructionsScreen.java
 * Author : j5backup
 * Purpose : Displays graphics briefly explaining how to play.
 *************************************************************************/

package com.j5backup.loseyourmarblesclone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class InstructionsScreen extends Screen{
	
	/**************************************************************************/
	/* Private enumerations
	/**************************************************************************/
	private enum GRAPHIC{
		GOAL, 
		GAMEPLAY1,
		GAMEPLAY2,
		GAMEPLAY3,
		GAMEPLAY4,
		ANDROID_CONTROLS,
		DESKTOP_CONTROLS
	}
	
	/**************************************************************************/
	/* Private members
	/**************************************************************************/
	private GraphicsWrapper m_BackgroundMarble;
	private GraphicsWrapper m_GoalGraphic;
	private GraphicsWrapper m_Gameplay1Graphic;
	private GraphicsWrapper m_Gameplay2Graphic;
	private GraphicsWrapper m_Gameplay3Graphic;
	private GraphicsWrapper m_Gameplay4Graphic;
	private GraphicsWrapper m_AndroidControlsGraphic;
	private GraphicsWrapper m_DesktopControlsGraphic;
	private Button m_NextButton;
	private GRAPHIC m_CurrentGraphic;
	
	/**************************************************************************/
	/* Constructors
	/**************************************************************************/
	InstructionsScreen(Configuration config,
					   int width, int height) {
		super(config, width, height);
		Initialize(width, height);
	}
	
	InstructionsScreen(Configuration config,
					   int width, int height,
					   float r, float g, float b, float a){
		super(config, width, height, r, g, b, a);
		Initialize(width, height);
	}
	
	/**************************************************************************/
	/* Public methods
	/**************************************************************************/
	public void Initialize(int width, int height){
		m_BackgroundMarble = 
				new GraphicsWrapper(m_Configuration,
									m_Width/2,
									m_Height/2,
									m_Configuration.GetDisplayMarbleWidth()*30f,
									m_Configuration.GetDisplayMarbleHeight()*30f,
									Configuration.PATH_TO_BACKGROUND_MARBLE_IMAGE);
		m_BackgroundMarble.SetOrigin(m_BackgroundMarble.GetWidth()/2, m_BackgroundMarble.GetHeight()/2);
		m_BackgroundMarble.SetOpacity(0.25f);
		
		m_GoalGraphic = 
				new GraphicsWrapper(m_Configuration,
									m_Width/2-0.0f*m_Width,
									m_Height/2,
									m_Configuration.GetDisplayMarbleWidth()*10.2f,
									m_Configuration.GetDisplayMarbleHeight()*10.2f,
									Configuration.PATH_TO_GOAL_GRAPHIC);
		
		m_Gameplay1Graphic = 
				new GraphicsWrapper(m_Configuration,
									m_Width/2,
									m_Height/2,
									m_Configuration.GetDisplayMarbleWidth()*10.2f,
									m_Configuration.GetDisplayMarbleHeight()*10.2f,
									Configuration.PATH_TO_GAMEPLAY0_GRAPHIC);
		m_Gameplay1Graphic.Hide();
		
		m_Gameplay2Graphic = 
				new GraphicsWrapper(m_Configuration,
									m_Width/2,
									m_Height/2,
									m_Configuration.GetDisplayMarbleWidth()*10.2f,
									m_Configuration.GetDisplayMarbleHeight()*10.2f,
									Configuration.PATH_TO_GAMEPLAY1_GRAPHIC);
		m_Gameplay2Graphic.Hide();
		
		m_Gameplay3Graphic = 
				new GraphicsWrapper(m_Configuration,
									m_Width/2-0.01f*m_Width,
									m_Height/2,
									m_Configuration.GetDisplayMarbleWidth()*10.2f,
									m_Configuration.GetDisplayMarbleHeight()*10.2f,
									Configuration.PATH_TO_GAMEPLAY2_GRAPHIC);
		m_Gameplay3Graphic.Hide();
		
		m_Gameplay4Graphic = 
				new GraphicsWrapper(m_Configuration,
									m_Width/2,
									m_Height/2,
									m_Configuration.GetDisplayMarbleWidth()*10.2f,
									m_Configuration.GetDisplayMarbleHeight()*10.2f,
									Configuration.PATH_TO_GAMEPLAY3_GRAPHIC);
		m_Gameplay4Graphic.Hide();
		
		m_AndroidControlsGraphic = 
				new GraphicsWrapper(m_Configuration,
									m_Width/2,
									m_Height/2,
									m_Configuration.GetDisplayMarbleWidth()*10.2f,
									m_Configuration.GetDisplayMarbleHeight()*10.2f,
									Configuration.PATH_TO_ANDROID_CONTROLS_GRAPHIC);
		m_AndroidControlsGraphic.Hide();
		
		m_DesktopControlsGraphic = 
				new GraphicsWrapper(m_Configuration,
									m_Width/2,
									m_Height/2,
									m_Configuration.GetDisplayMarbleWidth()*10.2f,
									m_Configuration.GetDisplayMarbleHeight()*10.2f,
									Configuration.PATH_TO_DESKTOP_CONTROLS_GRAPHIC);
		m_DesktopControlsGraphic.Hide();
		
		m_NextButton = 
				new Button(m_Configuration,
				  		   m_Width/2,
				  		   m_Height/2-0.45f*m_Height,
				  		   m_Configuration.GetDisplayMarbleWidth()*2.5f,
				  		   m_Configuration.GetDisplayMarbleHeight()*1.025f,
				  		   1.2f,
				  		   Configuration.PATH_TO_NEXT_BUTTON,
				  		   0, 0, 256, 105);
		m_NextButton.SetScalingDeltaForAnimation(0.01f);
		m_NextButton.StartScaleChangeAnimation();
		
		m_CurrentGraphic = GRAPHIC.GOAL;
	}
	
	@Override
	public void Reinitialize(){
		Dispose();
		super.Reinitialize();
		Initialize(m_Width, m_Height);
	}
	
	@Override
	public void Update(){
		if(m_BackgroundMarble != null){
			m_BackgroundMarble.Rotate(0.025f);
		}
		if(m_NextButton != null){
			m_NextButton.Update();
		}
	}
	
	@Override
	public void Render(SpriteBatch batch){
		
		super.Render(batch);
		
		if(m_BackgroundMarble != null){
			m_BackgroundMarble.Render(batch);
		}
		if(m_GoalGraphic != null){
			m_GoalGraphic.Render(batch);
		}
		if(m_Gameplay1Graphic != null){
			m_Gameplay1Graphic.Render(batch);
		}
		if(m_Gameplay1Graphic != null){
			m_Gameplay1Graphic.Render(batch);
		}
		if(m_Gameplay2Graphic != null){
			m_Gameplay2Graphic.Render(batch);
		}
		if(m_Gameplay3Graphic != null){
			m_Gameplay3Graphic.Render(batch);
		}
		if(m_Gameplay4Graphic != null){
			m_Gameplay4Graphic.Render(batch);
		}
		if(m_AndroidControlsGraphic != null){
			m_AndroidControlsGraphic.Render(batch);
		}
		if(m_DesktopControlsGraphic != null){
			m_DesktopControlsGraphic.Render(batch);
		}
		if(m_NextButton != null){
			m_NextButton.Render(batch);
		}
	}
	
	@Override
	public void Dispose(){
		if(m_BackgroundMarble != null){
			m_BackgroundMarble.Dispose();
		}
		if(m_GoalGraphic != null){
			m_GoalGraphic.Dispose();
		}
		if(m_Gameplay1Graphic != null){
			m_Gameplay1Graphic.Dispose();
		}
		if(m_Gameplay2Graphic != null){
			m_Gameplay2Graphic.Dispose();
		}
		if(m_Gameplay3Graphic != null){
			m_Gameplay3Graphic.Dispose();
		}
		if(m_Gameplay4Graphic != null){
			m_Gameplay4Graphic.Dispose();
		}
		if(m_AndroidControlsGraphic != null){
			m_AndroidControlsGraphic.Dispose();
		}
		if(m_AndroidControlsGraphic != null){
			m_AndroidControlsGraphic.Dispose();
		}
		if(m_NextButton != null){
			m_NextButton.Dispose();
		}
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(Gdx.input.isKeyPressed(Keys.ENTER)){
			UserActionNextPage();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if( CheckForButtonPress(screenX, m_Height - screenY) ){
			UserActionNextPage();
			return true;
		}
		return false;
	}
	
	/**************************************************************************/
	/* Private methods
	/**************************************************************************/
	private void UserActionNextPage(){
		switch(m_CurrentGraphic){
			case GOAL:
				m_GoalGraphic.Hide();
				m_Gameplay1Graphic.Show();
				m_CurrentGraphic = GRAPHIC.GAMEPLAY1;
				break;
				
			case GAMEPLAY1:
				m_Gameplay1Graphic.Hide();
				m_Gameplay2Graphic.Show();
				m_CurrentGraphic = GRAPHIC.GAMEPLAY2;
				break;
				
			case GAMEPLAY2:
				m_Gameplay2Graphic.Hide();
				m_Gameplay3Graphic.Show();
				m_CurrentGraphic = GRAPHIC.GAMEPLAY3;
				break;
				
			case GAMEPLAY3:
				m_Gameplay3Graphic.Hide();
				m_Gameplay4Graphic.Show();
				m_CurrentGraphic = GRAPHIC.GAMEPLAY4;
				break;
				
			case GAMEPLAY4:
				m_Gameplay4Graphic.Hide();
				switch( Gdx.app.getType() ){
					case Desktop:
						m_DesktopControlsGraphic.Show();
						m_CurrentGraphic = GRAPHIC.DESKTOP_CONTROLS;
						break;
					case Android:
						m_AndroidControlsGraphic.Show();
						m_CurrentGraphic = GRAPHIC.ANDROID_CONTROLS;
						break;
					case WebGL:
						break;
					default:
						break;
				}
				break;
				
			case ANDROID_CONTROLS:
				m_AndroidControlsGraphic.Hide();
				m_IsScreenOver = true;
				break;
			case DESKTOP_CONTROLS:
				m_DesktopControlsGraphic.Hide();
				m_IsScreenOver = true;
				break;
		}
	}
	
	private boolean CheckForButtonPress(int x, int y){
		if( m_NextButton.IsCursorInside(x, y) ){
			return true;
		}
		return false;
	}
}
