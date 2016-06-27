/**************************************************************************
 * Project : Lose Your Marbles Clone
 * File : MainMenuScreen.java
 * Author : j5backup
 * Purpose : Displays the main menu with buttons and title graphics.
 *************************************************************************/

package com.j5backup.loseyourmarblesclone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input.Keys;

public class MainMenuScreen extends Screen {

	/**************************************************************************/
	/* Private members
	/**************************************************************************/
	private GraphicsWrapper m_TitleOverlay;
	private GraphicsWrapper m_BackgroundMarble;
	private Button m_EasyButton;
	private Button m_HardButton;
	private Button m_InstructionsButton;
	private Button m_QuitButton;
	private Button m_MuteButton;
	private CheckBox m_CheckBox;
	private Button m_SelectedButton;
	private Configuration.DIFFICULTY m_SelectedDifficulty;
	private boolean m_IsDifficultySelected;
	private GraphicsWrapper m_TitleMarble1;
	private GraphicsWrapper m_TitleMarble2;
	private boolean m_IsScreenFadingToBlack;
	private boolean m_IsScreenFadingToBlackComplete;
	private GraphicsWrapper m_BlackPlate;
	private float m_BlackPlateOpacity;
	private GraphicsWrapper m_VisualSeparator;
	
	/**************************************************************************/
	/* Constructors
	/**************************************************************************/
	MainMenuScreen(Configuration config,
			       int width, int height) {
		super(config, width, height);
		Initialize(width, height);
	}
	
	MainMenuScreen(Configuration config,
			       int width, int height,
			       float r, float g, float b, float a){
		super(config, width, height, r, g, b, a);
		Initialize(width, height);
	}
	
	/**************************************************************************/
	/* Public members
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
		
		m_TitleOverlay = 
				new GraphicsWrapper(m_Configuration,
									m_Width/2,
									m_Height/2+0.265f*m_Height,
									m_Configuration.GetDisplayMarbleWidth()*10f,
									m_Configuration.GetDisplayMarbleHeight()*10f,
									Configuration.PATH_TO_TITLE);
		
		m_EasyButton = 
				new Button(m_Configuration, 
						   m_Width/2,
						   m_Height/2-0.04f*m_Height,
						   m_Configuration.GetDisplayMarbleWidth()*2.5f,
						   m_Configuration.GetDisplayMarbleHeight()*1.196f,
						   1.1f,
						   Configuration.PATH_TO_EASY_BUTTON,
						   0,
						   0,
						   256,
						   122);
		m_EasyButton.SetScalingDeltaForAnimation(0.012f);
		m_EasyButton.StartScaleChangeAnimation();
		
		m_HardButton = 
				new Button(m_Configuration,
						   m_Width/2,
						   m_Height/2-0.17f*m_Height,
						   m_Configuration.GetDisplayMarbleWidth()*2.5f,
						   m_Configuration.GetDisplayMarbleHeight()*1.025f,
						   1.2f,
						   Configuration.PATH_TO_HARD_BUTTON,
						   0,
						   0,
						   256,
						   105);
		m_HardButton.SetScalingDeltaForAnimation(0.014f);
		
		m_InstructionsButton = 
				new Button(m_Configuration,
						   m_Width/2,
						   m_Height/2-0.32f*m_Height,
						   m_Configuration.GetDisplayMarbleWidth()*5f,
						   m_Configuration.GetDisplayMarbleHeight()*0.87f,
						   1.2f,
						   Configuration.PATH_TO_INSTRUCTIONS_BUTTON,
						   0,
						   0,
						   256,
						   45);
		m_InstructionsButton.SetScalingDeltaForAnimation(0.012f);
		
		m_QuitButton = 
				new Button(m_Configuration,
						   3*m_Width/4 + m_Width*0.06f,
						   m_Height*0.058f,
						   m_Configuration.GetDisplayMarbleWidth()*2.0f,
						   m_Configuration.GetDisplayMarbleHeight()*1.0f,
						   1.0f,
						   Configuration.PATH_TO_QUIT_BUTTON,
						   0,
						   0,
						   256,
						   128);
		m_QuitButton.SetScalingDeltaForAnimation(0.008f);
		
		m_MuteButton = 
				new Button(m_Configuration,
						   m_Width/4,
						   m_Height*0.056f,
						   m_Configuration.GetDisplayMarbleWidth()*2.0f,
						   m_Configuration.GetDisplayMarbleHeight()*1.0f,
						   1.0f,
						   Configuration.PATH_TO_MUTE_BUTTON,
						   0,
						   0,
						   256,
						   118);
		m_MuteButton.SetScalingDeltaForAnimation(0.008f);
		
		m_CheckBox = 
				new CheckBox(m_Configuration,
							 m_MuteButton.GetCenterX() - m_MuteButton.GetWidth()/2 - m_Configuration.GetDisplayMarbleWidth()*0.7f/1.5f,
							 m_Height*0.055f,
							 m_Configuration.GetDisplayMarbleWidth()*0.7f,
							 m_Configuration.GetDisplayMarbleHeight()*0.7f,
							 Configuration.PATH_TO_EMPTY_CHECKBOX_GRAPHIC,
							 Configuration.PATH_TO_FILLED_CHECKBOX_GRAPHIC);
		if(m_IsMusicOn){
			m_CheckBox.SetUnchecked();
		}
		else{
			m_CheckBox.SetChecked();
		}
		
		m_SelectedButton = m_EasyButton;
		m_IsDifficultySelected = false;
		m_SelectedDifficulty = Configuration.DIFFICULTY.EASY;
		
		m_TitleMarble1 = 
				new GraphicsWrapper(m_Configuration,
									m_Width/2-0.045f*m_TitleOverlay.GetWidth(),
									m_Height/2+0.25f*m_TitleOverlay.GetHeight()+0.265f*m_Height,
									0.14f*m_TitleOverlay.GetWidth(),
									0.14f*m_TitleOverlay.GetHeight(),
									Configuration.PATH_TO_TITLE_MARBLE1_IMAGE);
		
		m_TitleMarble2 = 
				new GraphicsWrapper(m_Configuration,
									m_Width/2-0.045f*m_TitleOverlay.GetWidth(),
									m_Height/2+0.025f*m_TitleOverlay.GetHeight()+0.265f*m_Height,
									0.14f*m_TitleOverlay.GetWidth(),
									0.14f*m_TitleOverlay.GetHeight(),
									Configuration.PATH_TO_TITLE_MARBLE2_IMAGE);
		
		m_BackgroundMusic = Gdx.audio.newMusic( Gdx.files.internal(Configuration.PATH_TO_MAIN_MENU_MUSIC) );
		m_BackgroundMusicVolume = 1f;
		
		m_IsScreenFadingToBlack = false;
		m_IsScreenFadingToBlackComplete = false;
		
		m_BlackPlate = 
				new GraphicsWrapper(m_Configuration,
				 					m_Width/2,
				 					m_Height/2,
				 					m_Width,
				 					m_Height,
				 					Configuration.PATH_TO_BLACK_BOX);
		m_BlackPlateOpacity = 0f;
		
		m_VisualSeparator = 
				new GraphicsWrapper(m_Configuration,
				 					m_Width/2,
				 					m_TitleOverlay.GetCenterY() - m_TitleOverlay.GetHeight()*0.367f,
				 					m_Width,
				 					0.04f*m_Height,
				 					Configuration.PATH_TO_WHITE_BOX);
		m_VisualSeparator.SetOpacity(0.25f);
		
		m_NextScreen = LoseYourMarblesCloneGame.SCREEN.MAIN_MENU;
	}
	
	public void Reinitialize(){
		Dispose();
		Initialize(m_Width, m_Height);
	}
	
	public void Update(){
		// TODO Move non-rendering things out of the rendering function to here
		if(m_EasyButton != null){
			m_EasyButton.Update();
		}
		if(m_HardButton != null){
			m_HardButton.Update();
		}
		if(m_InstructionsButton != null){
			m_InstructionsButton.Update();
		}
		if(m_QuitButton != null){
			m_QuitButton.Update();
		}
		if(m_MuteButton != null){
			m_MuteButton.Update();
		}
		if(m_CheckBox != null){
			if(m_IsMusicOn){
				m_CheckBox.SetUnchecked();
			}
			else{
				m_CheckBox.SetChecked();
			}
		}
	}
	
	public void FadeScreenToBlack(){
		m_IsScreenFadingToBlack = true;
	}
	
	@Override
	public void Render(SpriteBatch batch){
		
		Gdx.gl.glClearColor(m_R, m_G, m_B, m_A);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(m_BackgroundMarble != null){
			m_BackgroundMarble.Rotate(0.025f);
			m_BackgroundMarble.Render(batch);
		}
		if(m_VisualSeparator != null){
			m_VisualSeparator.Render(batch);
		}
		if(m_TitleOverlay != null){
			m_TitleOverlay.Render(batch);
		}
		if(m_EasyButton != null){
			m_EasyButton.Render(batch);
		}
		if(m_HardButton != null){
			m_HardButton.Render(batch);
		}	
		if(m_InstructionsButton != null){
			m_InstructionsButton.Render(batch);
		}
		if(m_QuitButton != null){
			m_QuitButton.Render(batch);
		}
		if(m_MuteButton != null){
			m_MuteButton.Render(batch);
		}
		if(m_CheckBox != null){
			m_CheckBox.Render(batch);
		}
		if(m_TitleMarble1 != null){
			m_TitleMarble1.Rotate(1f);
			m_TitleMarble1.Render(batch);
		}
		if(m_TitleMarble2 != null){
			m_TitleMarble2.Rotate(-1f);
			m_TitleMarble2.Render(batch);
		}
		if(m_IsScreenFadingToBlack){
			if(m_BlackPlateOpacity < 1f){
				m_BlackPlateOpacity += 0.015;
			}
			
			if(m_BlackPlateOpacity > 1f){
				m_BlackPlateOpacity = 1f;
				m_IsScreenFadingToBlackComplete = true;
			}
			
			if(m_BackgroundMusicVolume > 0f){
				m_BackgroundMusicVolume -= 0.05f;
			}
			
			if(m_BackgroundMusicVolume < 0f){
				m_BackgroundMusicVolume = 0f;
			}
			
			m_BackgroundMusic.setVolume(m_BackgroundMusicVolume);
			
			m_BlackPlate.SetOpacity(m_BlackPlateOpacity);
			m_BlackPlate.Render(batch);
		}
	}
	
	public void Dispose(){
		// Call the dispose method of the parent class
		// so the music objects are disposed
		super.Dispose();
		
		if(m_BackgroundMarble != null){
			m_BackgroundMarble.Dispose();
		}
		if(m_VisualSeparator != null){
			m_VisualSeparator.Dispose();
		}
		if(m_TitleOverlay != null){
			m_TitleOverlay.Dispose();
		}
		if(m_EasyButton != null){
			m_EasyButton.Dispose();
		}
		if(m_HardButton != null){
			m_HardButton.Dispose();
		}
		if(m_InstructionsButton != null){
			m_InstructionsButton.Dispose();
		}
		if(m_QuitButton != null){
			m_QuitButton.Dispose();
		}
		if(m_MuteButton != null){
			m_MuteButton.Dispose();
		}
		if(m_CheckBox != null){
			m_CheckBox.Dispose();
		}
		if(m_TitleMarble1 != null){
			m_TitleMarble1.Dispose();
		}
		if(m_TitleMarble2 != null){
			m_TitleMarble2.Dispose();
		}
		if(m_BlackPlate != null){
			m_BlackPlate.Dispose();
		}
	}

	public boolean IsMenuOver(){
		return m_IsScreenFadingToBlackComplete;
	}
	
	public Configuration.DIFFICULTY GetDifficulty(){
		return m_SelectedDifficulty;
	}

	/**************************************************************************/
	/* Private members
	/**************************************************************************/
	private void UserActionUpPress(){
		if(m_SelectedButton == m_EasyButton){
			m_SelectedButton = m_QuitButton;
			m_EasyButton.StopScaleChangeAnimation();
			m_HardButton.StopScaleChangeAnimation();
			m_InstructionsButton.StopScaleChangeAnimation();
			m_MuteButton.StopScaleChangeAnimation();
			m_QuitButton.StartScaleChangeAnimation();
		}
		else if(m_SelectedButton == m_HardButton){
			m_SelectedButton = m_EasyButton;
			m_EasyButton.StartScaleChangeAnimation();
			m_HardButton.StopScaleChangeAnimation();
			m_InstructionsButton.StopScaleChangeAnimation();
			m_MuteButton.StopScaleChangeAnimation();
			m_QuitButton.StopScaleChangeAnimation();
			m_SelectedDifficulty = Configuration.DIFFICULTY.EASY;
		}
		else if(m_SelectedButton == m_InstructionsButton){
			m_SelectedButton = m_HardButton;
			m_EasyButton.StopScaleChangeAnimation();
			m_HardButton.StartScaleChangeAnimation();
			m_InstructionsButton.StopScaleChangeAnimation();
			m_MuteButton.StopScaleChangeAnimation();
			m_QuitButton.StopScaleChangeAnimation();
			m_SelectedDifficulty = Configuration.DIFFICULTY.HARD;
		}
		else if(m_SelectedButton == m_MuteButton){
			m_SelectedButton = m_InstructionsButton;
			m_EasyButton.StopScaleChangeAnimation();
			m_HardButton.StopScaleChangeAnimation();
			m_InstructionsButton.StartScaleChangeAnimation();
			m_MuteButton.StopScaleChangeAnimation();
			m_QuitButton.StopScaleChangeAnimation();
		}
		else if(m_SelectedButton == m_QuitButton){
			m_SelectedButton = m_MuteButton;
			m_EasyButton.StopScaleChangeAnimation();
			m_HardButton.StopScaleChangeAnimation();
			m_InstructionsButton.StopScaleChangeAnimation();
			m_MuteButton.StartScaleChangeAnimation();
			m_QuitButton.StopScaleChangeAnimation();
		}
	}
	
	private void UserActionDownPress(){
		if(m_SelectedButton == m_EasyButton){
			m_SelectedButton = m_HardButton;
			m_EasyButton.StopScaleChangeAnimation();
			m_HardButton.StartScaleChangeAnimation();
			m_InstructionsButton.StopScaleChangeAnimation();
			m_MuteButton.StopScaleChangeAnimation();
			m_QuitButton.StopScaleChangeAnimation();
			m_SelectedDifficulty = Configuration.DIFFICULTY.HARD;
		}
		else if(m_SelectedButton == m_HardButton){
			m_SelectedButton = m_InstructionsButton;
			m_EasyButton.StopScaleChangeAnimation();
			m_HardButton.StopScaleChangeAnimation();
			m_InstructionsButton.StartScaleChangeAnimation();
			m_MuteButton.StopScaleChangeAnimation();
			m_QuitButton.StopScaleChangeAnimation();
		}
		else if(m_SelectedButton == m_InstructionsButton){
			m_SelectedButton = m_MuteButton;
			m_EasyButton.StopScaleChangeAnimation();
			m_HardButton.StopScaleChangeAnimation();
			m_InstructionsButton.StopScaleChangeAnimation();
			m_MuteButton.StartScaleChangeAnimation();
			m_QuitButton.StopScaleChangeAnimation();
		}
		else if(m_SelectedButton == m_MuteButton){
			m_SelectedButton = m_QuitButton;
			m_EasyButton.StopScaleChangeAnimation();
			m_HardButton.StopScaleChangeAnimation();
			m_InstructionsButton.StopScaleChangeAnimation();
			m_MuteButton.StopScaleChangeAnimation();
			m_QuitButton.StartScaleChangeAnimation();
		}
		else if(m_SelectedButton == m_QuitButton){
			m_SelectedButton = m_EasyButton;
			m_EasyButton.StartScaleChangeAnimation();
			m_HardButton.StopScaleChangeAnimation();
			m_InstructionsButton.StopScaleChangeAnimation();
			m_MuteButton.StopScaleChangeAnimation();
			m_QuitButton.StopScaleChangeAnimation();
			m_SelectedDifficulty = Configuration.DIFFICULTY.EASY;
		}
	}
	
	private void UserActionOptionChosen(){
		if(m_SelectedButton == m_EasyButton ||
		   m_SelectedButton == m_HardButton){
			m_IsDifficultySelected = true;
			m_NextScreen = LoseYourMarblesCloneGame.SCREEN.GAME_BOARD;
			FadeScreenToBlack();
		}
		else if(m_SelectedButton == m_InstructionsButton){
			m_NextScreen = LoseYourMarblesCloneGame.SCREEN.INSTRUCTIONS;
			FadeScreenToBlack();
		}
		else if(m_SelectedButton == m_MuteButton){
			m_IsMusicOn = !m_IsMusicOn;
			ToggleMusic(m_IsMusicOn);
		}
		else if(m_SelectedButton == m_QuitButton){
			m_IsQuitting = true;
		}
		
	}
	
	private boolean CheckForButtonPress(int x, int y){
		if( m_EasyButton.IsCursorInside(x, y) ){
			m_SelectedButton = m_EasyButton;
			m_SelectedDifficulty = Configuration.DIFFICULTY.EASY;
			m_EasyButton.StartScaleChangeAnimation();
			m_HardButton.StopScaleChangeAnimation();
			m_InstructionsButton.StopScaleChangeAnimation();
			m_MuteButton.StopScaleChangeAnimation();
			m_QuitButton.StopScaleChangeAnimation();
			return true;
		}
		else if( m_HardButton.IsCursorInside(x, y) ){
			m_SelectedButton = m_HardButton;
			m_SelectedDifficulty = Configuration.DIFFICULTY.HARD;
			m_EasyButton.StopScaleChangeAnimation();
			m_HardButton.StartScaleChangeAnimation();
			m_InstructionsButton.StopScaleChangeAnimation();
			m_MuteButton.StopScaleChangeAnimation();
			m_QuitButton.StopScaleChangeAnimation();
			return true;
		}
		else if( m_InstructionsButton.IsCursorInside(x, y) ){
			m_SelectedButton = m_InstructionsButton;
			m_EasyButton.StopScaleChangeAnimation();
			m_HardButton.StopScaleChangeAnimation();
			m_InstructionsButton.StartScaleChangeAnimation();
			m_MuteButton.StopScaleChangeAnimation();
			m_QuitButton.StopScaleChangeAnimation();
			return true;
		}
		else if( m_MuteButton.IsCursorInside(x, y) ){
			m_SelectedButton = m_MuteButton;
			m_EasyButton.StopScaleChangeAnimation();
			m_HardButton.StopScaleChangeAnimation();
			m_InstructionsButton.StopScaleChangeAnimation();
			m_MuteButton.StartScaleChangeAnimation();
			m_QuitButton.StopScaleChangeAnimation();
			return true;
		}
		else if( m_QuitButton.IsCursorInside(x, y) ){
			m_SelectedButton = m_QuitButton;
			m_EasyButton.StopScaleChangeAnimation();
			m_HardButton.StopScaleChangeAnimation();
			m_InstructionsButton.StopScaleChangeAnimation();
			m_MuteButton.StopScaleChangeAnimation();
			m_QuitButton.StartScaleChangeAnimation();
			return true;
		}		
		return false;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(Gdx.input.isKeyPressed(Keys.DPAD_UP)){
			UserActionUpPress();
			return true;
		}
		else if(Gdx.input.isKeyPressed(Keys.DPAD_DOWN)){
			UserActionDownPress();
			return true;
		}
		else if(Gdx.input.isKeyPressed(Keys.ENTER)){
			UserActionOptionChosen();
			return true;
		}
		else if(Gdx.input.isKeyPressed(Keys.M)){
			m_IsMusicOn = !m_IsMusicOn;
			ToggleMusic(m_IsMusicOn);
			return true;
		}
		else if(Gdx.input.isKeyPressed(Keys.Q)){
			m_IsQuitting = true;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if( CheckForButtonPress(screenX, m_Height - screenY) ){
			UserActionOptionChosen();
			return true;
		}
		return false;
	}
}
