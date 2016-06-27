/**************************************************************************
 * Project : Lose Your Marbles Clone
 * File : Screen.java
 * Author : j5backup
 * Purpose : Base class for the game screens.
 *************************************************************************/

package com.j5backup.loseyourmarblesclone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;

public abstract class Screen implements InputProcessor{
	
	/**************************************************************************/
	/* Protected members
	/**************************************************************************/
	protected int m_Width;
	protected int m_Height;
	protected float m_CenterX;
	protected float m_CenterY;
	protected String m_BackgroundImagePathAndName;
	protected float m_R;
	protected float m_G;
	protected float m_B;
	protected float m_A;
	protected GraphicsWrapper m_Background;
	protected boolean m_IsQuitting;
	protected Music m_BackgroundMusic;
	protected float m_BackgroundMusicVolume;
	protected boolean m_IsMusicOn;
	protected Configuration m_Configuration;
	protected LoseYourMarblesCloneGame.SCREEN m_NextScreen;
	protected boolean m_IsScreenOver;
	
	/**************************************************************************/
	/* Constructors
	/**************************************************************************/
	Screen(Configuration config,
		   int width, int height){
		m_R = 0;
		m_G = 0;
		m_B = 0;
		m_A = 1;
		Initialize(config, width, height);
	}
	
	Screen(Configuration config,
		   int width, int height,
		   float r, float g, float b, float a){
		m_R = r;
		m_G = g;
		m_B = b;
		m_A = a;
		Initialize(config, width, height);
	}
	
	/**************************************************************************/
	/* Public methods
	/**************************************************************************/
	public void Reinitialize(){
		Dispose();
		Initialize(m_Configuration, m_Width, m_Height);
	}
	
	public boolean IsScreenOver(){
		return m_IsScreenOver;
	}
	
	public void SetBackgroundImage(float spriteOriginX, float spriteOriginY, float spriteWidth, float spriteHeight, String backgroundImagePathAndName, 
								   int textureOriginX, int textureOriginY, int textureWidth, int textureHeight){
		m_BackgroundImagePathAndName = backgroundImagePathAndName;
		m_Background = 
				new GraphicsWrapper(m_Configuration,
									spriteOriginX,
									spriteOriginY,
									spriteWidth,
									spriteHeight,
									backgroundImagePathAndName,
									textureOriginX,
									textureOriginY,
									textureWidth,
									textureHeight);
	}
	
	public void ToggleMusic(boolean toggle){
		m_IsMusicOn = toggle;
		if(toggle){
			if(m_BackgroundMusic != null){
				m_BackgroundMusic.setLooping(true);
				m_BackgroundMusic.play();
				m_BackgroundMusic.setVolume(m_BackgroundMusicVolume);
			}
		}
		else{
			if(m_BackgroundMusic != null){
				m_BackgroundMusic.stop();
			}
		}
	}
	
	public void SetBackgroundMusicVolume(float vol){
		m_BackgroundMusicVolume = vol;
	}
	
	public boolean IsMusicOn(){
		return m_IsMusicOn;
	}
	
	public boolean IsQuitting(){
		return m_IsQuitting;
	}
	
	public LoseYourMarblesCloneGame.SCREEN GetNextScreen(){
		return m_NextScreen;
	}
	
	public void Update(){
		// Override and provide updates as required
	}
	
	public void Render(SpriteBatch batch){		
		Gdx.gl.glClearColor(m_R, m_G, m_B, m_A);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(m_Background != null){
			m_Background.Render(batch);
		}
	}
	
	public void Dispose(){
		if(m_Background != null){
			m_Background.Dispose();
		}
		if(m_BackgroundMusic != null){
			m_BackgroundMusic.dispose();
		}
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if( Gdx.input.isKeyPressed(Keys.Q) ){
			m_IsQuitting = true;
		}
		else if( Gdx.input.isKeyPressed(Keys.M) ){
			m_IsMusicOn = !m_IsMusicOn;
		}
		return false;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**************************************************************************/
	/* Private methods
	/**************************************************************************/
	private void Initialize(Configuration config, int width, int height){
		m_Configuration = config;
		m_Width = width;
		m_Height = height;
		m_CenterX = (float) width / 2f;
		m_CenterY = (float) height / 2f;
		m_IsQuitting = false;
		m_IsMusicOn = true;
		m_BackgroundMusic = null;
		m_BackgroundMusicVolume = 1f;
		m_NextScreen = LoseYourMarblesCloneGame.SCREEN.MAIN_MENU;
		m_IsScreenOver = false;
	}
}
