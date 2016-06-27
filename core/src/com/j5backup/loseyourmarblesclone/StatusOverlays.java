/**************************************************************************
 * Project : Lose Your Marbles Clone
 * File : StatusOverlays.java
 * Author : j5backup
 * Purpose : Creates and manages the overlays and labels displayed on the
 * 			 game board.
 *************************************************************************/

package com.j5backup.loseyourmarblesclone;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StatusOverlays{
	
	/**************************************************************************/
	/* Private members
	/**************************************************************************/
	private float m_WindowSizeX;
	private float m_WindowSizeY;
	private TimerGraphic m_Timer;
	private ScoreGraphic m_Score;
	private GraphicsWrapper m_GameOverGraphic;
	private GraphicsWrapper m_GameWonGraphic;
	private GraphicsWrapper m_GamePausedGraphic;
	private boolean m_IsGamePaused;
	private boolean m_IsGameOver;
	private boolean m_IsGameWon;
	private float m_OverlayGraphicScale;
	private boolean m_IsOverlayGraphicGrowing;
	private Configuration m_Configuration;
	private String[] Digits = {Configuration.PATH_TO_ZERO_IMAGE, Configuration.PATH_TO_ONE_IMAGE,
							   Configuration.PATH_TO_TWO_IMAGE, Configuration.PATH_TO_THREE_IMAGE,
							   Configuration.PATH_TO_FOUR_IMAGE, Configuration.PATH_TO_FIVE_IMAGE,
							   Configuration.PATH_TO_SIX_IMAGE, Configuration.PATH_TO_SEVEN_IMAGE,
							   Configuration.PATH_TO_EIGHT_IMAGE, Configuration.PATH_TO_NINE_IMAGE};
	
	/**************************************************************************/
	/* Constructor
	/**************************************************************************/
	StatusOverlays(Configuration config, float windowSizeX, float windowSizeY){
		m_Configuration = config;
		m_WindowSizeX = windowSizeX;
		m_WindowSizeY = windowSizeY;
		Initialize();
	}
	
	/**************************************************************************/
	/* Public methods
	/**************************************************************************/
	public void Initialize(){
		m_IsGameOver = false;
		m_IsGameWon = false;
		m_IsGamePaused = false;
		m_OverlayGraphicScale = 1f;
		m_IsOverlayGraphicGrowing = true;
	}
	
	public void Reinitialize(){
		Dispose();
		Initialize();
	}
	
	public void CreateGameOverGraphic(float centerX, float centerY,
									  float displayWidth, float displayHeight,
									  String texturePathAndName){
		m_GameOverGraphic =
				new GraphicsWrapper(m_Configuration,
									centerX,
									centerY,
									displayWidth,
									displayHeight,
									texturePathAndName);
	}

	public void CreateGameWonGraphic(float centerX, float centerY,
			 						 float displayWidth, float displayHeight,
			 						 String texturePathAndName){
		m_GameWonGraphic =
				new GraphicsWrapper(m_Configuration,
									centerX,
									centerY,
									displayWidth,
									displayHeight,
									texturePathAndName);
	}
	
	public void CreatePausedGraphic(float centerX, float centerY,
									float displayWidth, float displayHeight,
									String texturePathAndName){
		m_GamePausedGraphic =
				new GraphicsWrapper(m_Configuration,
							 		centerX,
							 		centerY,
							 		displayWidth,
							 		displayHeight,
							 		texturePathAndName);
							 
	}
	
	public void CreateTimerGraphic(){
		m_Timer = new TimerGraphic(m_Configuration,
				 				   0.2f*m_WindowSizeX,
				 				   m_WindowSizeY - 0.07f*m_WindowSizeY,
				 				   0.75f*m_Configuration.GetDisplayMarbleWidth(), 
				   				   0.75f*m_Configuration.GetDisplayMarbleHeight(),
				   				   Digits);
		m_Timer.SetTime(00, 00);
	}
	
	public boolean ExistsTimerGraphic(){
		if(m_Timer != null){
			return true;
		}
		return false;
	}
	
	public void InitializeTimer(int minutes, int seconds){
		m_Timer.InitializeTimer(minutes, seconds);
	}
	
	public boolean UpdateTimer(double gameDeltaTime){
		return m_Timer.UpdateTimer(gameDeltaTime);
	}
	
	public void CreateScoreGraphic(Configuration.DIFFICULTY difficulty){
		m_Score = 
				new ScoreGraphic(m_Configuration,
								 0.8f*m_WindowSizeX,
								 m_WindowSizeY - 0.07f*m_WindowSizeY,
								 0.75f*m_Configuration.GetDisplayMarbleWidth(), 
				   				 0.75f*m_Configuration.GetDisplayMarbleHeight(),
				   				 Digits,
				   				 difficulty);
		m_Score.SetScore(0);
	}
	
	public boolean ExistsScoreGraphic(){
		if(m_Score != null){
			return true;
		}
		return false;
	}
	
	public void InitializeScore(int score){
		m_Score.SetScore(score);
	}
	
	public boolean SetScore(int score){
		return m_Score.SetScore(score);
	}
	
	public void SetGameOverStatus(boolean isGameOver){
		m_IsGameOver = isGameOver;
	}
	
	public void SetGameWonStatus(boolean isGameWon){
		m_IsGameWon = isGameWon;
	}
	
	public void SetGamePausedStatus(boolean isGamePaused){
		m_IsGamePaused = isGamePaused;
	}
	
	public void Update(){
		UpdateScaleOfOverlayGraphic();
	}
	
	public void Render(SpriteBatch batch){
		if(m_IsGamePaused){
			if(m_GamePausedGraphic != null){
				m_GamePausedGraphic.Render(batch);
			}
		}
		else{
			if(m_IsGameOver){
				if(m_GameOverGraphic != null){
					m_GameOverGraphic.Render(batch);
				}
			}
			if(m_IsGameWon){
				if(m_GameWonGraphic != null){
					m_GameWonGraphic.Render(batch);
				}
			}
		}
		if(m_Timer != null){
			m_Timer.Render(batch);
		}
		if(m_Score != null){
			m_Score.Render(batch);
		}
	}
	
	public void Dispose(){
		if(m_GamePausedGraphic != null){
			m_GameOverGraphic.Dispose();
		}
		if(m_GameOverGraphic != null){
			m_GameOverGraphic.Dispose();
		}
		if(m_GameWonGraphic != null){
			m_GameWonGraphic.Dispose();
		}
		if(m_Timer != null){
			m_Timer.Dispose();
		}
		if(m_Score != null){
			m_Score.Dispose();
		}
	}
	
	/**************************************************************************/
	/* Private methods
	/**************************************************************************/
	private void UpdateScaleOfOverlayGraphic(){
		if(m_IsOverlayGraphicGrowing){
			m_OverlayGraphicScale += 0.015;
			if(m_OverlayGraphicScale >= 1.5){
				m_IsOverlayGraphicGrowing = false;
			}
		}
		else{
			m_OverlayGraphicScale -= 0.015;
			if(m_OverlayGraphicScale <= 1f){
				m_IsOverlayGraphicGrowing = true;
			}
		}
		
		if(m_IsGamePaused){
			m_GamePausedGraphic.SetScale(m_OverlayGraphicScale, m_OverlayGraphicScale);
		}
		else if(m_IsGameOver){
			m_GameOverGraphic.SetScale(m_OverlayGraphicScale, m_OverlayGraphicScale);
		}
		else if(m_IsGameWon){
			m_GameWonGraphic.SetScale(m_OverlayGraphicScale, m_OverlayGraphicScale);
		}
	}
}
