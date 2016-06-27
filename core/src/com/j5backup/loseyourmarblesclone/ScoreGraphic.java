/**************************************************************************
 * Project : Lose Your Marbles Clone
 * File : ScoreGraphic.java
 * Author : j5backup
 * Purpose : Implements a mutable score label.
 *************************************************************************/

package com.j5backup.loseyourmarblesclone;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScoreGraphic{
	
	/**************************************************************************/
	/* Private members
	/**************************************************************************/
	private float m_CenterX;
	private float m_CenterY;
	private float m_DisplayWidth;
	private float m_DisplayHeight;
	private float m_Score;
	private GraphicsWrapper[] m_Digits;
	private String[] m_DigitTexturesNamesAndPaths;
	private GraphicsWrapper m_ScoreLabel;
	private int m_NumberOfScoreDigits;
	private int m_MaxScore;
	private Configuration m_Configuration;
	
	/**************************************************************************/
	/* Constructor
	/**************************************************************************/
	ScoreGraphic(Configuration config,
				 float centerX,
				 float centerY,
				 float displayWidth,
				 float displayHeight,
				 String[] digitTexturesNamesAndPaths,
				 Configuration.DIFFICULTY difficulty){
		m_Configuration = config;
		m_CenterX = centerX;
		m_CenterY = centerY;
		m_DisplayWidth = displayWidth;
		m_DisplayHeight = displayHeight;
		m_DigitTexturesNamesAndPaths = digitTexturesNamesAndPaths;
		
		switch(difficulty){
			case EASY:
				m_NumberOfScoreDigits = 2;
				break;
			case HARD:
				m_NumberOfScoreDigits = 3;
				break;
			default:
				m_NumberOfScoreDigits = 2;
				break;
		}
		m_MaxScore = (int) Math.pow(10, m_NumberOfScoreDigits) - 1;
		
		Initialize();
	}
	
	/**************************************************************************/
	/* Public methods
	/**************************************************************************/
	public boolean SetScore(int score){
		/* 
		 * The score is determined using the knowledge that each digit is a constant times a power of ten.
		 * Each iteration the previously determined order of magnitude is subtracted out.
		 * The function returns true if there is an overflow.
		 */
		int[] scoreDigits = new int[m_NumberOfScoreDigits];
		int previousDigit = 0;
		int modifiedScore = score;
		for(int i=0; i<m_NumberOfScoreDigits; i++){
			scoreDigits[i] = (int) Math.floor( (float)( modifiedScore - previousDigit*Math.pow(10f, m_NumberOfScoreDigits-i) ) /
					                           ( Math.pow(10f, m_NumberOfScoreDigits-i-1) ) );
			// Check for an overflow case
			if( scoreDigits[i] >= 10 ){
				SetScore(m_MaxScore);
				return true;
				// Wrap around, but only if the score being input wraps as well or there will be an array out of bounds exception
				//scoreDigits[i] = score - 10;
			}
			modifiedScore -= previousDigit*Math.pow(10f, m_NumberOfScoreDigits-i);
			previousDigit = scoreDigits[i];
		}
		
		// Use scoreDigits[] as indices
		float xCoord = -1f*m_NumberOfScoreDigits/2f*m_DisplayWidth;
		for(int i=0; i<m_NumberOfScoreDigits; i++){
			if(m_Digits[i] != null){
				m_Digits[i].Dispose();
			}
			m_Digits[i] = new GraphicsWrapper(m_Configuration,
											  m_CenterX+xCoord,
											  m_CenterY,
											  m_DisplayWidth,
											  m_DisplayHeight,
											  m_DigitTexturesNamesAndPaths[ scoreDigits[i] ]);
			xCoord += m_DisplayWidth;
		}
		return false;
	}
	
	public void InitializeScore(int score){
		m_Score = score;
		SetScore(score);
	}
	
	public void Reinitialize(){
		Dispose();
		Initialize();
	}
	
	public void Render(SpriteBatch batch){
		for(int i=0; i<m_NumberOfScoreDigits; i++){
			if(m_Digits[i] != null){
				m_Digits[i].Render(batch);
			}
		}
		if(m_ScoreLabel != null){
			m_ScoreLabel.Render(batch);
		}
	}
	
	public void Dispose(){
		for(int i=0; i<m_NumberOfScoreDigits; i++){
			if(m_Digits[i] != null){
				m_Digits[i].Dispose();
			}
		}
		if(m_ScoreLabel != null){
			m_ScoreLabel.Dispose();
		}
	}
	
	/**************************************************************************/
	/* Private methods
	/**************************************************************************/
	private void Initialize(){
		m_Digits = new GraphicsWrapper[m_NumberOfScoreDigits];
		m_ScoreLabel = new GraphicsWrapper(m_Configuration,
										   m_CenterX - m_NumberOfScoreDigits/2f*m_DisplayWidth + 3.5f*m_DisplayWidth/2f - m_DisplayWidth/2, 
				                           m_CenterY+m_DisplayHeight,
				                           3.5f*m_DisplayWidth,
				                           3.5f*m_DisplayHeight,
				                           Configuration.PATH_TO_SCORE_OVERLAY);
	}
}
