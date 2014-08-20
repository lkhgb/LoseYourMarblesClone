/**************************************************************************
 * Project : Lose Your Marbles Clone
 * File : GameBoardLines.java
 * Author : j5backup
 * Purpose : Loads and arranges the lines of the game board (scalable).
 *************************************************************************/

package com.j5backup.loseyourmarblesclone;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameBoardLines {

	/**************************************************************************/
	/* Private members
	/**************************************************************************/
	private float m_SizeLeftRightLinesX;
	private float m_SizeLeftRightLinesY;
	private float m_SizeTopBottomLinesX;
	private float m_SizeTopBottomLinesY;
	private float m_SizeMiddleLinesX;
	private float m_SizeMiddleLinesY;
	private float m_CenterX;
	private float m_CenterY;
	private GraphicsWrapper m_LeftLine;
	private GraphicsWrapper m_RightLine;
	private GraphicsWrapper m_TopLine;
	private GraphicsWrapper m_BottomLine;
	private GraphicsWrapper m_MiddleTopLine;
	private GraphicsWrapper m_MiddleBottomLine;
	private Configuration m_Configuration;
	
	/**************************************************************************/
	/* Constructor
	/**************************************************************************/
	GameBoardLines(Configuration config, float x, float y){
		m_Configuration = config;
		m_CenterX = x;
		m_CenterY = y;
		
		m_SizeLeftRightLinesX = (float) m_Configuration.GetDisplayMarbleWidth() * 0.35f;
		m_SizeLeftRightLinesY = (float) m_Configuration.GetGameBoardHeight() * 1.16f;
		
		m_SizeTopBottomLinesX = (float) m_Configuration.GetGameBoardWidth() * 1.3f;
		m_SizeTopBottomLinesY = (float) m_Configuration.GetDisplayMarbleHeight() * 0.35f;
		
		m_SizeMiddleLinesX = (float) m_Configuration.GetGameBoardWidth() * 1.3f;
		m_SizeMiddleLinesY = (float) m_Configuration.GetDisplayMarbleHeight() * 0.275f;
		
		// Left border line
		m_LeftLine = new GraphicsWrapper(m_Configuration, 
										 m_CenterX - m_Configuration.GetGameBoardWidth()/2 - m_SizeLeftRightLinesX/2,
				 						 m_CenterY,
				 						 m_SizeLeftRightLinesX, m_SizeLeftRightLinesY,
				 						 Configuration.GAME_BOARD_VERTICLE_LINE,
				 						 0, 0, 32, 512);
		m_LeftLine.SetOpacity(Configuration.GAME_BOARD_LINE_OPACITY);
		
		// Right border line
		m_RightLine = new GraphicsWrapper(m_Configuration,
										  m_CenterX + m_Configuration.GetGameBoardWidth()/2 + m_SizeLeftRightLinesX/2,
										  m_CenterY,
										  -m_SizeLeftRightLinesX, m_SizeLeftRightLinesY,
										  Configuration.GAME_BOARD_VERTICLE_LINE,
										  0, 0, 32, 512);
		m_RightLine.SetOpacity(Configuration.GAME_BOARD_LINE_OPACITY);
		
		// Top border line
		m_TopLine = new GraphicsWrapper(m_Configuration,
										m_CenterX,
										m_CenterY + m_Configuration.GetGameBoardHeight()/2 + m_SizeTopBottomLinesY/2,
									    m_SizeTopBottomLinesX, m_SizeTopBottomLinesY,
									    Configuration.GAME_BOARD_HORIZONTAL_LINE,
									    0, 0, 512, 32);
		m_TopLine.SetOpacity(Configuration.GAME_BOARD_LINE_OPACITY);
		
		// Bottom border line
		m_BottomLine = new GraphicsWrapper(m_Configuration,
										   m_CenterX,
										   m_CenterY - m_Configuration.GetGameBoardHeight()/2 - m_SizeTopBottomLinesY/2,
										   -m_SizeTopBottomLinesX, m_SizeTopBottomLinesY,
										   Configuration.GAME_BOARD_HORIZONTAL_LINE,
										   0, 0, 512, 32);
		m_BottomLine.SetOpacity(Configuration.GAME_BOARD_LINE_OPACITY);
		
		// Middle top border line
		m_MiddleTopLine = new GraphicsWrapper(m_Configuration,
											  m_CenterX,
				   							  m_CenterY + m_Configuration.GetDisplayMarbleHeight()/2 + m_SizeMiddleLinesY/2,
				   							  m_SizeMiddleLinesX, m_SizeMiddleLinesY,
				   							  Configuration.GAME_BOARD_HORIZONTAL_LINE,
				   							  0, 0, 512, 32);
		m_MiddleTopLine.SetOpacity(Configuration.GAME_BOARD_LINE_OPACITY);
		
		// Middle bottom border line
		m_MiddleBottomLine = new GraphicsWrapper(m_Configuration,
												 m_CenterX,
												 m_CenterY - m_Configuration.GetDisplayMarbleHeight()/2 - m_SizeMiddleLinesY/2,
												 -m_SizeMiddleLinesX, m_SizeMiddleLinesY,
												 Configuration.GAME_BOARD_HORIZONTAL_LINE,
												 0, 0, 512, 32);
		m_MiddleBottomLine.SetOpacity(Configuration.GAME_BOARD_LINE_OPACITY);
	}
	
	/**************************************************************************/
	/* Public methods
	/**************************************************************************/
	public void Render(SpriteBatch batch){
		if(m_LeftLine != null){
			m_LeftLine.Render(batch);
		}
		if(m_RightLine != null){
			m_RightLine.Render(batch);
		}
		if(m_TopLine != null){
			m_TopLine.Render(batch);
		}
		if(m_BottomLine != null){
			m_BottomLine.Render(batch);
		}
		if(m_MiddleTopLine != null){
			m_MiddleTopLine.Render(batch);
		}
		if(m_MiddleBottomLine != null){
			m_MiddleBottomLine.Render(batch);
		}
	}
	
	public void Dispose(){
		if(m_LeftLine != null){
			m_LeftLine.Dispose();
		}
		if(m_RightLine != null){
			m_RightLine.Dispose();
		}
		if(m_TopLine != null){
			m_TopLine.Dispose();
		}
		if(m_BottomLine != null){
			m_BottomLine.Dispose();
		}
		if(m_MiddleTopLine != null){
			m_MiddleTopLine.Dispose();
		}
		if(m_MiddleBottomLine != null){
			m_MiddleBottomLine.Dispose();
		}
	}
}
