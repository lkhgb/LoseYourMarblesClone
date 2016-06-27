/**************************************************************************
 * Project : Lose Your Marbles Clone
 * File : LoseYourMarblesCloneGame.java
 * Author : j5backup
 * Purpose : Implements the main functions libGDX provides. Controls
 * 	         which game screen is displayed.
 *************************************************************************/

package com.j5backup.loseyourmarblesclone;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LoseYourMarblesCloneGame implements ApplicationListener{

	/**************************************************************************/
	/* Enumerations
	/**************************************************************************/
	public enum SCREEN{
		MAIN_MENU,
		GAME_BOARD,
		INSTRUCTIONS
	}
	
	/**************************************************************************/
	/* Private members
	/**************************************************************************/
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private MainMenuScreen m_MainMenuScreen;
	private GameBoardScreen m_GameBoardScreen;
	private InstructionsScreen m_InstructionsScreen;
	private int m_MainWindowWidth;
	private int m_MainWindowHeight;
	private float m_CenterX;
	private float m_CenterY;
	private int m_DisplayMarbleWidth;
	private int m_DisplayMarbleHeight;
	private Configuration m_Configuration;
	private SCREEN m_CurrentScreen;
	private boolean m_IsMusicOn;
	
	/**************************************************************************/
	/* Constructors
	/**************************************************************************/
	public LoseYourMarblesCloneGame(){}
	
	public LoseYourMarblesCloneGame(int width, int height){
		m_MainWindowWidth = width;
		m_MainWindowHeight = height;
	}
	
	/**************************************************************************/
	/* Public methods
	/**************************************************************************/
	@Override
	public void create() {
		
		switch( Gdx.app.getType() ){
			case Desktop:
				m_DisplayMarbleHeight = (int) Math.floor(m_MainWindowHeight*0.051);
				m_DisplayMarbleWidth = m_DisplayMarbleHeight;
				break;
			case Android:
				m_MainWindowWidth = Gdx.graphics.getWidth();
				m_MainWindowHeight = Gdx.graphics.getHeight();
				m_DisplayMarbleHeight = (int) Math.floor(m_MainWindowHeight*0.051f);
				m_DisplayMarbleWidth = m_DisplayMarbleHeight;
				break;
			case WebGL:
				break;
			default:
				break;
		}
		
		m_CenterX = m_MainWindowWidth/2f;
		m_CenterY = m_MainWindowHeight/2f;

		m_Configuration = new Configuration(m_DisplayMarbleWidth, m_DisplayMarbleHeight);
		
		camera = new OrthographicCamera(1, m_MainWindowHeight/m_MainWindowWidth);
		batch = new SpriteBatch();
		
		m_CurrentScreen = SCREEN.MAIN_MENU;
		
		m_IsMusicOn = true;
		
		// Give the main menu a light blue background
		m_MainMenuScreen = new MainMenuScreen(m_Configuration, m_MainWindowWidth, m_MainWindowHeight, 0.3f, 0.75f, 1f, 1f);
		m_MainMenuScreen.ToggleMusic(m_IsMusicOn);
		
		// Center the game board in the x direction, but shift down slightly to make room for the score graphic
		m_GameBoardScreen = new GameBoardScreen(m_Configuration, m_MainWindowWidth, m_MainWindowHeight, m_CenterX, m_CenterY-50);
		m_GameBoardScreen.ToggleMusic(false);
		
		m_InstructionsScreen = new InstructionsScreen(m_Configuration, m_MainWindowWidth, m_MainWindowHeight, 0.3f, 0.75f, 1f, 1f);
		m_InstructionsScreen.ToggleMusic(false);
	}

	@Override
	public void dispose() {
		if(batch != null){
			batch.dispose();
		}
		if(m_MainMenuScreen != null){
			m_MainMenuScreen.Dispose();
		}
		if(m_GameBoardScreen != null){
			m_GameBoardScreen.Dispose();
		}
		if(m_InstructionsScreen != null){
			m_InstructionsScreen.Dispose();
		}
	}

	@Override
	public void render() {		
		switch(m_CurrentScreen){
			case MAIN_MENU:
				// Allow the main menu to handle key presses
				Gdx.input.setInputProcessor(m_MainMenuScreen);
				
				if( m_MainMenuScreen.IsQuitting() ){
					// Exit the game
					Gdx.app.exit();
				}
				else if( m_MainMenuScreen.IsMenuOver() ){
					m_CurrentScreen = m_MainMenuScreen.GetNextScreen();
					
					// Note: The difficulty needs to be set before the game board screen shows
					//       and after it is initialized
					m_GameBoardScreen.SetDifficulty( m_MainMenuScreen.GetDifficulty() );
					
					if(m_CurrentScreen == SCREEN.GAME_BOARD){
						m_GameBoardScreen.ToggleMusic( m_MainMenuScreen.IsMusicOn() );
					}
					
					m_IsMusicOn = m_MainMenuScreen.IsMusicOn();
					m_MainMenuScreen.Reinitialize();
					m_MainMenuScreen.ToggleMusic(false);
				}
				else{
					m_MainMenuScreen.Update();
					
					// Render objects
					batch.begin();
					m_MainMenuScreen.Render(batch);
					batch.end();
				}
				
				break;
				
			case GAME_BOARD:
				// Allow the game board screen to handle key presses
				Gdx.input.setInputProcessor(m_GameBoardScreen);
				
				if( m_GameBoardScreen.IsQuitting() ){
					// Exit the game
					Gdx.app.exit();
				}
				else if( m_GameBoardScreen.IsGoToMainMenu() ){
					m_CurrentScreen = SCREEN.MAIN_MENU;
					
					m_MainMenuScreen.ToggleMusic( m_GameBoardScreen.IsMusicOn() );
					m_GameBoardScreen.Reinitialize();
					m_GameBoardScreen.ToggleMusic(false);
				}
				else{
					m_GameBoardScreen.Update();
					// Render the game board
					batch.begin();
			        m_GameBoardScreen.Render(batch);
					batch.end();
				}
				
				break;
			
			case INSTRUCTIONS:
				// Allow the game board screen to handle key presses
				Gdx.input.setInputProcessor(m_InstructionsScreen);
				
				if( m_InstructionsScreen.IsScreenOver() ){
					m_CurrentScreen = SCREEN.MAIN_MENU;
					m_InstructionsScreen.Reinitialize();
					m_MainMenuScreen.ToggleMusic(m_IsMusicOn);
				}
				else{
					m_InstructionsScreen.Update();		
					batch.begin();
					m_InstructionsScreen.Render(batch);
					batch.end();
				}
				
				break;
				
			default:
				break;
		}	
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void pause() {
		switch(m_CurrentScreen){
			case MAIN_MENU:
				m_IsMusicOn = m_MainMenuScreen.IsMusicOn();
				m_MainMenuScreen.ToggleMusic(false);
				break;
			case GAME_BOARD:
				m_GameBoardScreen.PauseGame();
				break;
			case INSTRUCTIONS:
				break;
			default:
				break;
		}
	}

	@Override
	public void resume() {
		switch(m_CurrentScreen){
			case MAIN_MENU:
				m_MainMenuScreen.ToggleMusic(m_IsMusicOn);
				break;
			case GAME_BOARD:
				m_GameBoardScreen.ResumeGame();
				break;
			case INSTRUCTIONS:
				break;
			default:
				break;
		}
	}
}
