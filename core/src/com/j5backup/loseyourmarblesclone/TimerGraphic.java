/**************************************************************************
 * Project : Lose Your Marbles Clone
 * File : TimerGraphic.java
 * Author : j5backup
 * Purpose : Implements a game timer that counts down from the initialized
 *           time.
 *************************************************************************/

package com.j5backup.loseyourmarblesclone;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TimerGraphic{
	
	/**************************************************************************/
	/* Private members
	/**************************************************************************/
	private float m_CenterX;
	private float m_CenterY;
	private float m_SizeX;
	private float m_SizeY;
	private GraphicsWrapper m_Colon;
	private GraphicsWrapper[] m_Digits;
	private String[] m_DigitTexturesNamesAndPaths;
	private GraphicsWrapper m_TimerLabel;
	private final int NUMBER_OF_TIME_DIGITS = 4;
	private double m_AccumulatedTime;
	private int m_TimeMinutes;
	private int m_TimeSeconds;
	private Configuration m_Configuration;
	
	/**************************************************************************/
	/* Constructor
	/**************************************************************************/
	TimerGraphic(Configuration config,
				float centerX, float centerY,
				float displayWidth, float displayHeight,
				String[] digitTexturesNamesAndPaths){
		m_Configuration = config;
		m_CenterX = centerX;
		m_CenterY = centerY;
		m_SizeX = displayWidth;
		m_SizeY = displayHeight;
		m_DigitTexturesNamesAndPaths = digitTexturesNamesAndPaths;
		Initialize();
	}
	
	/**************************************************************************/
	/* Public methods
	/**************************************************************************/
	public void SetTime(int minutes, int seconds){
		
		int[] timeDigits = new int[NUMBER_OF_TIME_DIGITS];
		int previousDigit = 0;
		int modifiedScore = minutes;
		for(int i=0; i<NUMBER_OF_TIME_DIGITS; i++){
			timeDigits[i] = (int) Math.floor((float)(modifiedScore - previousDigit*Math.pow(10f, NUMBER_OF_TIME_DIGITS-i)) /
					                          (Math.pow(10f, NUMBER_OF_TIME_DIGITS-i-1)));
			modifiedScore -= previousDigit*Math.pow(10f, NUMBER_OF_TIME_DIGITS-i);
			previousDigit = timeDigits[i];
			// TODO Fix this hack
			if(i == 1){
				previousDigit = 0;
				modifiedScore = seconds;
			}
		}
		
		// Use timeDigits[] as indices
		float xCoord = -1f*NUMBER_OF_TIME_DIGITS/2f*m_SizeX;
		for(int i=0; i<NUMBER_OF_TIME_DIGITS; i++){
			if(m_Digits[i] != null){
				m_Digits[i].Dispose();
			}
			m_Digits[i] = new GraphicsWrapper(m_Configuration, m_CenterX+xCoord, m_CenterY, m_SizeX, m_SizeY, m_DigitTexturesNamesAndPaths[timeDigits[i]]);
			// TODO Fix this hack for shifting over the seconds for the :
			if(i == NUMBER_OF_TIME_DIGITS/2 - 1){
				xCoord += 2*m_SizeX;
			}
			else{
				xCoord += m_SizeX;
			}
		}
	}
	
	public void InitializeTimer(int minutes, int seconds){
		m_TimeMinutes = minutes;
		m_TimeSeconds = seconds;
		return;
	}
	
	// Returns true if timer reaches zero
	public boolean UpdateTimer(double gameDeltaTime){
		if(m_TimeMinutes == 0 && m_TimeSeconds == 0){
			return true;
		}
		else{
			m_AccumulatedTime += gameDeltaTime;
			if(m_AccumulatedTime >= 1f){
				m_AccumulatedTime = m_AccumulatedTime - 1;
				m_TimeSeconds--;
				if(m_TimeSeconds < 0){
					m_TimeSeconds = 59;
					m_TimeMinutes--;
					if(m_TimeMinutes < 0){
						m_TimeMinutes = 0;
						return true;
					}
				}
			}
			SetTime(m_TimeMinutes, m_TimeSeconds);
		}
		return false;
	}
	
	public void Reinitialize(){
		Dispose();
		Initialize();
	}
	
	public void Render(SpriteBatch batch){
		if(m_Colon != null){
			m_Colon.Render(batch);
		}
		for(int i=0; i<NUMBER_OF_TIME_DIGITS; i++){
			if(m_Digits[i] != null){
				m_Digits[i].Render(batch);
			}
		}
		if(m_TimerLabel != null){
			m_TimerLabel.Render(batch);
		}
	}
	
	public void Dispose(){
		if(m_Colon != null){
			m_Colon.Dispose();
		}
		for(int i=0; i<NUMBER_OF_TIME_DIGITS; i++){
			if(m_Digits[i] != null){
				m_Digits[i].Dispose();
			}
		}
		if(m_TimerLabel != null){
			m_TimerLabel.Dispose();
		}
	}
	
	/**************************************************************************/
	/* Private methods
	/**************************************************************************/
	private void Initialize(){
		m_AccumulatedTime = 0;
		m_TimeMinutes = 5;
		m_TimeSeconds = 0;
		m_Digits = new GraphicsWrapper[NUMBER_OF_TIME_DIGITS];
		m_TimerLabel = new GraphicsWrapper(m_Configuration,
										   m_CenterX - NUMBER_OF_TIME_DIGITS/2f*m_SizeX + 3.5f*m_SizeX/2f - m_SizeX/2, 
               							   m_CenterY+m_SizeY,
               							   3.3f*m_SizeX,
               							   3.3f*m_SizeY,
               							   Configuration.PATH_TO_TIME_OVERLAY);
		m_Colon = new GraphicsWrapper(m_Configuration,
									  m_CenterX, m_CenterY,
									  m_SizeX, m_SizeY,
									  Configuration.PATH_TO_COLON_IMAGE);
	}
}
