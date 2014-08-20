/**************************************************************************
 * Project : Lose Your Marbles Clone
 * File : MarbleTextures.java
 * Author : j5backup
 * Purpose : Loads the images used for the marble rolling animations.
 *************************************************************************/

package com.j5backup.loseyourmarblesclone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class MarbleTextures {
	
	/**************************************************************************/
	/* Private members
	/**************************************************************************/
	private Texture[][] m_RollingAnimationTextures;
	private int m_NumberOfFramesToUse;
	private int[] m_FrameNumbers;
	private Configuration m_Configuration;
	
	/**************************************************************************/
	/* Constructor
	/**************************************************************************/
	MarbleTextures(Configuration config){
		m_Configuration = config;
		m_NumberOfFramesToUse = Configuration.NUMBER_OF_FRAMES_FOR_ROLLING_ANIMATION;
		m_FrameNumbers = new int[m_NumberOfFramesToUse];
		CalculateFrameNumbersToUse();
		
		m_RollingAnimationTextures = new Texture[Marble.COLOR.values().length][m_NumberOfFramesToUse];
		LoadRollingAnimationTextures();
	}
	
	/**************************************************************************/
	/* Public methods
	/**************************************************************************/
	public void LoadRollingAnimationTextures(){
		int itr = 0;
		// Load all of the marble images just once
		for( Marble.COLOR color : Marble.COLOR.values() ){
			for(int k=0; k<m_NumberOfFramesToUse; k++){
				switch(color){
					case RED:
						m_RollingAnimationTextures[itr][k] = new Texture( Gdx.files.internal(Configuration.RED_MARBLE_DIR + "/" +
																	      GetImageName(m_FrameNumbers[k]) + Configuration.MARBLE_IMAGES_EXT) );
						m_RollingAnimationTextures[itr][k].setFilter(TextureFilter.Linear, TextureFilter.Linear);
						break;
					case BLUE:
						m_RollingAnimationTextures[itr][k] = new Texture( Gdx.files.internal(Configuration.BLUE_MARBLE_DIR + "/" +
							      										  GetImageName(m_FrameNumbers[k]) + Configuration.MARBLE_IMAGES_EXT) );
						m_RollingAnimationTextures[itr][k].setFilter(TextureFilter.Linear, TextureFilter.Linear);
						break;
					case GREEN:
						m_RollingAnimationTextures[itr][k] = new Texture( Gdx.files.internal(Configuration.GREEN_MARBLE_DIR + "/" +
							      										  GetImageName(m_FrameNumbers[k]) + Configuration.MARBLE_IMAGES_EXT) );
						m_RollingAnimationTextures[itr][k].setFilter(TextureFilter.Linear, TextureFilter.Linear);
						break;
					case YELLOW:
						m_RollingAnimationTextures[itr][k] = new Texture( Gdx.files.internal(Configuration.YELLOW_MARBLE_DIR + "/" +
							      										  GetImageName(m_FrameNumbers[k]) + Configuration.MARBLE_IMAGES_EXT) );
						m_RollingAnimationTextures[itr][k].setFilter(TextureFilter.Linear, TextureFilter.Linear);
						break;
					case BLACK:
						m_RollingAnimationTextures[itr][k] = new Texture( Gdx.files.internal(Configuration.BLACK_MARBLE_DIR + "/" +
							      										  GetImageName(m_FrameNumbers[k]) + Configuration.MARBLE_IMAGES_EXT) );
						m_RollingAnimationTextures[itr][k].setFilter(TextureFilter.Linear, TextureFilter.Linear);
						break;
					case PURPLE:
						m_RollingAnimationTextures[itr][k] = new Texture( Gdx.files.internal(Configuration.PURPLE_MARBLE_DIR + "/" +
							      										  GetImageName(m_FrameNumbers[k]) + Configuration.MARBLE_IMAGES_EXT) );
						m_RollingAnimationTextures[itr][k].setFilter(TextureFilter.Linear, TextureFilter.Linear);
						break;
					default:
						break;
				}
			}
			itr += 1;
		}
	}
	
	public Texture GetTexture(Marble.COLOR color, int k){
		return m_RollingAnimationTextures[color.ordinal()][k];
	}
	
	public int GetNumberOfFramesUsed(){
		return m_NumberOfFramesToUse;
	}
	
	public void Dispose(){
		for(int m=0; m<Marble.COLOR.values().length; m++){
			for(int n=0; n<m_NumberOfFramesToUse; n++){
				if(m_RollingAnimationTextures[m][n] != null){
					m_RollingAnimationTextures[m][n].dispose();
				}
			}
		}
	}
	
	/**************************************************************************/
	/* Private members
	/**************************************************************************/
	private void CalculateFrameNumbersToUse(){
		int skipFactor = (int) Math.floor(Configuration.NUMBER_OF_FRAMES_IN_FULL_ROLLING_ANIMATION / Configuration.NUMBER_OF_FRAMES_FOR_ROLLING_ANIMATION);
		int index = 0;
		for(int k=0; k<m_NumberOfFramesToUse; k++){
			m_FrameNumbers[k] = index;
			index += skipFactor;
		}
	}
	
	// The image sequence cannot have greater than 9999 frames...
	private String GetImageName(int i){
		if(i < 10){
			return "000" + Integer.toString(i);
		}
		else if(i < 100){
			return "00" + Integer.toString(i);
		}
		else if(i < 1000){
			return "0" + Integer.toString(i);
		}
		else if(i < 10000){
			return Integer.toString(i);
		}
		return "\0";
	}
}
