/**************************************************************************
 * Project : Lose Your Marbles Clone
 * File : GameBoardScreen.java
 * Author : j5backup
 * Purpose : Implements the core game logic and displays the game board.
 *************************************************************************/

package com.j5backup.loseyourmarblesclone;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

public class GameBoardScreen extends Screen implements InputProcessor {

	/**************************************************************************/
	/* Enumerations
	/**************************************************************************/
	public enum  SIDE{TOP, BOTTOM, BOTH, NEITHER}
	
	/**************************************************************************/
	/* Private members
	/**************************************************************************/
	private Marble m_Cells[][];
	private Vector2 m_AnchorPoints[][];
	private float m_GameBoardCenterX;
	private float m_GameBoardCenterY;
	private GameBoardLines m_GameBoardLines;
	private boolean m_IsShiftingRight;
	private int m_CurrentFrameShiftingRight;
	private int m_SelectedColumn;
	private int m_CenterRow;
	private int[] m_CurrentFrameShiftingUp;
	private int[] m_CurrentFrameShiftingDown;
	private SIDE[] m_IsColumnRollingUp;
	private SIDE[] m_IsColumnRollingDown;
	private int[] m_ColumnFrameCount;
	private boolean[] m_LowMarbleCountColumns;
	private Marble[][] m_DroppedMarblesTop;
	private Marble[][] m_DroppedMarblesBottom;
	private boolean[] m_IsDroppingTop;
	private boolean[] m_IsDroppingBottom;
	private int[] m_CurrentFrameDropMarblesTop;
	private int[] m_CurrentFrameDropMarblesBottom;
	private int[] m_NumberOfBacklogUserUpRolls;
	private int[] m_NumberOfBacklogUserDownRolls;
	private ColumnEmptyData[] m_ColumnEmptyData;
	private boolean m_IsGameOver;
	private boolean m_IsGameWon;
	private boolean m_GoToMainMenu;
	private StatusOverlays m_StatusOverlays;
	private int m_Score;
	private float m_FrameLimiter;
	private Music m_GameCompleteMusic;
	private Sound m_Roll;
	private Sound m_Click;
	private Sound m_Despawn;
	private Configuration.DIFFICULTY m_Difficulty;
	private boolean m_HasPlayedGameCompleteMusic;
	private boolean m_IsGamePaused;
	private GraphicsWrapper m_Ring;
	private MarbleTextures m_MarbleTextures;
	private int m_CursorX;
	private int m_CursorY;
	private Button m_MainMenuButton;
	private Button m_PauseButton;
	private boolean m_IsTouchDown;
	
	/**************************************************************************/
	/* Constructor
	/**************************************************************************/
	GameBoardScreen(Configuration config,
					int screenWidth, int screenHeight,
					float gameBoardCenterX, float gameBoardCenterY){
		super(config, screenWidth, screenHeight);
		m_GameBoardCenterX = gameBoardCenterX;
		m_GameBoardCenterY = gameBoardCenterY;	
		Initialize();
	}
	
	/**************************************************************************/
	/* Public methods
	/**************************************************************************/
	public void Reinitialize(){
		Dispose();
		super.Reinitialize();
		Initialize();
	}
	
	public void Initialize(){
		// Calculate the index of the center row (the scoring row)
		m_CenterRow = (int) Math.floor(Configuration.NUMBER_OF_MARBLE_ROWS/2);
		
		// Create a 2d array of marbles representing the game board
		m_Cells = 
				new Marble[Configuration.NUMBER_OF_MARBLE_ROWS]
						  [Configuration.NUMBER_OF_MARBLE_COLUMNS];
		
		// Create a 2d array storing the locations of the marble centers in the
		// game board
		m_AnchorPoints =
				new Vector2[Configuration.NUMBER_OF_MARBLE_ROWS]
						   [Configuration.NUMBER_OF_MARBLE_COLUMNS];
		
		// Load all of the marble textures
		m_MarbleTextures = new MarbleTextures(m_Configuration);

		// Create the column selector ring
		m_Ring = 
				new GraphicsWrapper(m_Configuration,
									m_GameBoardCenterX, m_GameBoardCenterY,
									m_Configuration.GetDisplayMarbleWidth(),
									m_Configuration.GetDisplayMarbleHeight(),
									Configuration.PATH_TO_RING_IMAGE);
		// Create the game board lines
		m_GameBoardLines = 
				new GameBoardLines(m_Configuration, m_GameBoardCenterX, m_GameBoardCenterY);
		
		// Make all marble positions on the game board null
		InitializeMarblesTable();
		// Initialize the game board with a randomly generated marble layout
		InitializeStartingMarbles();
		// Find the centers of where the marbles will be in a full board
		SetMarbleCenterAnchors();
		
		// Start in the middle (0 index for 1st column)
		m_SelectedColumn = (int) Math.floor(Configuration.NUMBER_OF_MARBLE_COLUMNS/2);
	
		// Initialize variables keeping tracking of marble shifting and the animations
		m_IsShiftingRight = false;
		m_CurrentFrameShiftingRight = 0;
		m_CurrentFrameShiftingUp = new int[Configuration.NUMBER_OF_MARBLE_COLUMNS];
		Arrays.fill(m_CurrentFrameShiftingUp, 0);
		m_CurrentFrameShiftingDown = new int[Configuration.NUMBER_OF_MARBLE_COLUMNS];
		Arrays.fill(m_CurrentFrameShiftingDown, 0);
		
		// Initialize array keeping track of which columns have low marble counts
		m_LowMarbleCountColumns = new boolean[Configuration.NUMBER_OF_MARBLE_COLUMNS];
		Arrays.fill(m_LowMarbleCountColumns, false);
		
		// Initialize the 2d arrays keeping track of the marbles that roll externally onto the board 
		m_DroppedMarblesTop = new Marble[Configuration.MAX_NUMBER_OF_DROPPED_MARBLES][Configuration.NUMBER_OF_MARBLE_COLUMNS];
		for(Marble[] row: m_DroppedMarblesTop){
			Arrays.fill(row, null);
		}
		m_DroppedMarblesBottom = new Marble[Configuration.MAX_NUMBER_OF_DROPPED_MARBLES][Configuration.NUMBER_OF_MARBLE_COLUMNS];
		for(Marble[] row: m_DroppedMarblesBottom){
			Arrays.fill(row, null);
		}
		
		// Set the arrays to false indicating that marbles are dropping in columns
		m_IsDroppingTop = new boolean[Configuration.NUMBER_OF_MARBLE_COLUMNS];
		Arrays.fill(m_IsDroppingTop, false);
		m_IsDroppingBottom = new boolean [Configuration.NUMBER_OF_MARBLE_COLUMNS];
		Arrays.fill(m_IsDroppingBottom, false);
		m_CurrentFrameDropMarblesTop = new int[Configuration.NUMBER_OF_MARBLE_COLUMNS];
		Arrays.fill(m_CurrentFrameDropMarblesTop, 0);
		m_CurrentFrameDropMarblesBottom = new int[Configuration.NUMBER_OF_MARBLE_COLUMNS];
		Arrays.fill(m_CurrentFrameDropMarblesBottom, 0);
		
		// Initialize variables tracking how many times the user has requested a column roll
		// while an animation has not yet completed
		m_NumberOfBacklogUserUpRolls = new int[Configuration.NUMBER_OF_MARBLE_COLUMNS];
		Arrays.fill(m_NumberOfBacklogUserUpRolls, 0);
		m_NumberOfBacklogUserDownRolls = new int[Configuration.NUMBER_OF_MARBLE_COLUMNS];
		Arrays.fill(m_NumberOfBacklogUserDownRolls, 0);
		
		// Initialize array containing the empty locations in each column
		m_ColumnEmptyData = new ColumnEmptyData[Configuration.NUMBER_OF_MARBLE_COLUMNS];
		for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
			m_ColumnEmptyData[j] = new ColumnEmptyData(j);
		}
		
		m_IsGameOver = false;
		m_IsGameWon = false;
		m_GoToMainMenu = false;
		
		// Create the overlays for a game over, a game win, and a game pause
		m_StatusOverlays = new StatusOverlays(m_Configuration, m_Width, m_Height);
		m_StatusOverlays.CreateGameOverGraphic(m_CenterX,
											   m_CenterY,
											   m_Configuration.GetDisplayMarbleWidth()*6.5f,
											   m_Configuration.GetDisplayMarbleHeight()*6.5f,
											   Configuration.PATH_TO_GAME_OVER_OVERLAY);
		m_StatusOverlays.CreateGameWonGraphic(m_CenterX,
											  m_CenterY,
											  m_Configuration.GetDisplayMarbleWidth()*6.5f,
				   							  m_Configuration.GetDisplayMarbleHeight()*6.5f,
				   							  Configuration.PATH_TO_WIN_OVERLAY);
		m_StatusOverlays.CreatePausedGraphic(m_CenterX,
											 m_CenterY,
											 m_Configuration.GetDisplayMarbleWidth()*6.5f,
											 m_Configuration.GetDisplayMarbleWidth()*6.5f,
											 Configuration.PATH_TO_PAUSED_OVERLAY);
		
		m_Difficulty = Configuration.DIFFICULTY.EASY;
		
		m_Score = 0;
		
		// Initialize the variables tracking whether a column is currently in motion
		// up or down
		m_IsColumnRollingUp = new SIDE[Configuration.NUMBER_OF_MARBLE_COLUMNS];
		Arrays.fill(m_IsColumnRollingUp, SIDE.NEITHER);
		m_IsColumnRollingDown = new SIDE[Configuration.NUMBER_OF_MARBLE_COLUMNS];
		Arrays.fill(m_IsColumnRollingDown, SIDE.NEITHER);
		
		// Initialize the variable tracking the frame counts for each column
		m_ColumnFrameCount = new int[Configuration.NUMBER_OF_MARBLE_COLUMNS];
		Arrays.fill(m_ColumnFrameCount, 0);
		
		// Initialize the variable holding the time passed between each game loop update
		m_FrameLimiter = 0;
		
		// Load the sounds
		m_Roll = Gdx.audio.newSound( Gdx.files.internal(Configuration.PATH_TO_ROLL0_SOUND) );
		m_Click = Gdx.audio.newSound( Gdx.files.internal(Configuration.PATH_TO_CLICK0_SOUND) );
		m_Despawn = Gdx.audio.newSound( Gdx.files.internal(Configuration.PATH_TO_DESPAWN0_SOUND) );
		
		m_NextScreen = LoseYourMarblesCloneGame.SCREEN.GAME_BOARD;
		
		m_HasPlayedGameCompleteMusic = false;
		
		m_IsGamePaused = false;
		
		// Create background and buttons
		// NOTE: The locations for the graphics are currently hard coded here and were tweaked manually
		SetBackgroundImage(m_Width/2, m_Height/2,
						   m_Width, m_Height,
						   GetRandomBackgroundImage(),
						   0, 0,
						   Configuration.BACKGROUND_IMAGE_WIDTH, Configuration.BACKGROUND_IMAGE_HEIGHT);
		
		m_MainMenuButton =
				new Button(m_Configuration, 
						   m_Width/4-m_Width*0.06f,
						   m_Height*0.02f,
						   m_Configuration.GetDisplayMarbleWidth()*3.2f,
						   m_Configuration.GetDisplayMarbleHeight()*0.6f,
						   1f,
						   Configuration.PATH_TO_MAIN_MENU_BUTTON,
						   0, 0,
						   256, 48);
		
		m_PauseButton =
				new Button(m_Configuration, 
						   3*m_Width/4+m_Width*0.11f,
						   m_Height*0.022f,
						   m_Configuration.GetDisplayMarbleWidth()*1.95f,
						   m_Configuration.GetDisplayMarbleHeight()*0.6474f,
						   1f,
						   Configuration.PATH_TO_PAUSE_BUTTON,
						   0, 0,
						   256, 85);
		
		m_IsTouchDown = false;
	}
	
	@Override
	public void ToggleMusic(boolean toggle){
		m_IsMusicOn = toggle;
		if(toggle){
			if(m_Difficulty == Configuration.DIFFICULTY.EASY){
				m_BackgroundMusic = Gdx.audio.newMusic( Gdx.files.internal(Configuration.PATH_TO_TRACK0_MUSIC) );
			}
			else if(m_Difficulty == Configuration.DIFFICULTY.HARD){
				m_BackgroundMusic = Gdx.audio.newMusic( Gdx.files.internal(Configuration.PATH_TO_TRACK1_MUSIC) );
			}
			if(m_BackgroundMusic != null){
				m_BackgroundMusic.setLooping(true);
				m_BackgroundMusic.play();
				m_BackgroundMusic.setVolume(1f);
			}
		}
		else{
			if(m_BackgroundMusic != null){
				m_BackgroundMusic.stop();
			}
		}
	}
	
	public void SetDifficulty(Configuration.DIFFICULTY difficulty){
		m_Difficulty = difficulty;
		m_StatusOverlays.CreateScoreGraphic(difficulty);
		m_StatusOverlays.InitializeScore(0);
	}
	
	public void PauseGame(){
		m_IsGamePaused = false;
		UserActionPause();
	}
	
	public void ResumeGame(){
		m_IsGamePaused = true;
		UserActionPause();
	}
	
	public boolean IsGoToMainMenu(){
		return m_GoToMainMenu;
	}
	
	@Override
	public void Render(SpriteBatch batch){
		
		// Render the background
		m_Background.Render(batch);
		
		// Render the lines of the game board
		m_GameBoardLines.Render(batch);
		
		// Render the existing grid of marbles
		for(int i=0; i<Configuration.NUMBER_OF_MARBLE_ROWS; i++){
			for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
				if(m_Cells[i][j] != null){
					m_Cells[i][j].Render(batch);
				}
			}
		}
		
		// Render dropped-in marbles
		for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
			for(int k=0; k<Configuration.MAX_NUMBER_OF_DROPPED_MARBLES; k++){
				if(m_DroppedMarblesTop[k][j] != null){
					m_DroppedMarblesTop[k][j].Render(batch);
				}
				if(m_DroppedMarblesBottom[k][j] != null){
					m_DroppedMarblesBottom[k][j].Render(batch);
				}
			}
		}

		// Render the column selector ring
		m_Ring.Render(batch);
		
		// Render the buttons
		m_MainMenuButton.Render(batch);
		m_PauseButton.Render(batch);
		
		// Render and status overlays
		m_StatusOverlays.Render(batch);
	}
	
	@Override
	public void Update(){
		
		if(!m_IsGamePaused){
		
			m_FrameLimiter += Gdx.graphics.getDeltaTime();
			// Prevent the game from playing too fast by making sure 0.01 seconds have
			// passed before updating the game states
			if(m_FrameLimiter > 0.01){
				m_FrameLimiter = 0;
				
				boolean timerOverStatus = false;
				if( m_StatusOverlays.ExistsTimerGraphic() ){
					timerOverStatus = m_StatusOverlays.UpdateTimer( Gdx.graphics.getDeltaTime() );
				}
				m_IsGameOver = IsBoardFull() || timerOverStatus;
				
				if(!m_IsGameOver && !m_IsGameWon){
					
					if( !IsUpdatingScorePositions() && !IsUpdatingKeyDownPositions() && !IsUpdatingDroppedMarblePositions()){
						
						double threshold = Configuration.THRESHOLD_FOR_MARBLE_DROP_EASY;
						if(m_Difficulty == Configuration.DIFFICULTY.EASY){
							threshold = Configuration.THRESHOLD_FOR_MARBLE_DROP_EASY;
						}
						else if(m_Difficulty == Configuration.DIFFICULTY.HARD){
							threshold = Configuration.THRESHOLD_FOR_MARBLE_DROP_HARD;
						}
						
						if(Math.random()>threshold){
							RandomlyDropMarblesIntoOpenCells();
						}
					}
					
					// Check for scoring
					if( !IsUpdatingScorePositions() && !IsUpdatingKeyDownPositions() ){
						float tmpScore = CheckScoring();
						if( m_StatusOverlays.ExistsScoreGraphic() && tmpScore > 0 ){
							m_Score += tmpScore;
							m_IsGameWon = m_StatusOverlays.SetScore(m_Score);
						}
					}
					
					// Update marble positions
					if( !IsUpdatingKeyDownPositions() && !IsUpdatingDespawningMarbles() ){
						UpdateScorePositions();
					}
					
					// The reason for the hack below is a race condition that seems to occur if a dropped marble approaches the middle row as
					// a score is occurring, when there are no board marbles between the dropped marbles and the center row. Not doing anything in the catch 
					// for right now just allows the marbles from the other side of the board to roll into place and re-occupy the center row, thus
					// making the center row not be null.
					try{
						UpdateDroppedMarblePositions();
					}
					catch(NullPointerException e){
						//System.out.println("There was a null pointer while updating dropped marble positions. Skipping update this frame.");
					}
					
					if( !IsUpdatingScorePositions() ){
						UpdateKeyDownPositions();
					}
					
					// Make marbles that have finished their despawn animations null
					CleanupDespawnedMarbles();
					
					if( !IsUpdatingScorePositions() && !IsUpdatingKeyDownPositions() ){
						AddMarblesToLowCountColumns();
					}
				}
				else{
					if(m_BackgroundMusic != null){
						m_BackgroundMusic.stop();
					}
					
					if(m_IsGameOver && !m_HasPlayedGameCompleteMusic && m_IsMusicOn){
						
						m_GameCompleteMusic = Gdx.audio.newMusic( Gdx.files.internal(Configuration.PATH_TO_TRACK_GAME_OVER_MUSIC) );
						
						if(m_GameCompleteMusic != null){
							m_GameCompleteMusic.setVolume(0.6f);
							m_GameCompleteMusic.setLooping(false);
							m_GameCompleteMusic.play();
							m_HasPlayedGameCompleteMusic = true;
						}
					}
					else if(m_IsGameWon && !m_HasPlayedGameCompleteMusic && m_IsMusicOn){
						
						m_GameCompleteMusic = Gdx.audio.newMusic( Gdx.files.internal(Configuration.PATH_TO_TRACK_GAME_WIN_MUSIC) );
						
						if(m_GameCompleteMusic != null){
							m_GameCompleteMusic.setVolume(0.6f);
							m_GameCompleteMusic.setLooping(false);
							m_GameCompleteMusic.play();
							m_HasPlayedGameCompleteMusic = true;
						} 
					}
					
					// Show game over overlay and go back to main menu after a button press/mouse click
					m_StatusOverlays.SetGameOverStatus(m_IsGameOver);
					m_StatusOverlays.SetGameWonStatus(m_IsGameWon);
					m_StatusOverlays.Update();
				}
			}
		}
		else{
			m_StatusOverlays.Update();
		}
	}
	
	public void Dispose(){

		super.Dispose();
		
		m_StatusOverlays.Dispose();
		
		m_Ring.Dispose();
		
		m_GameBoardLines.Dispose();
		
		if(m_Roll != null){
			m_Roll.dispose();
		}
		if(m_Click != null){
			m_Click.dispose();
		}
		if(m_Despawn != null){
			m_Despawn.dispose();
		}
		if(m_GameCompleteMusic != null){
			m_GameCompleteMusic.dispose();
		}
		if(m_MainMenuButton != null){
			m_MainMenuButton.Dispose();
		}
		if(m_PauseButton != null){
			m_PauseButton.Dispose();
		}
		if(m_Roll != null){
			m_Roll.dispose();
		}
		if(m_Click != null){
			m_Click.dispose();
		}
		if(m_Despawn != null){
			m_Despawn.dispose();
		}
		
		for(int i=0; i<Configuration.NUMBER_OF_MARBLE_ROWS; i++){
			for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
				if(m_Cells[i][j] != null){
					m_Cells[i][j].Dispose();
				}
			}
		}
		
		for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
			for(int k=0; k<Configuration.MAX_NUMBER_OF_DROPPED_MARBLES; k++){
				if(m_DroppedMarblesTop[k][j] != null){
					m_DroppedMarblesTop[k][j].Dispose();
				}
				if(m_DroppedMarblesBottom[k][j] != null){
					m_DroppedMarblesBottom[k][j].Dispose();
				}
			}
		}
		
		m_MarbleTextures.Dispose();
	}
	
	@Override
	public boolean keyDown(int keycode) {

		if(Gdx.input.isKeyPressed(Keys.DPAD_LEFT) && !m_IsGamePaused){
			UserActionMoveRingLeft();
			return true;
		}
		else if( Gdx.input.isKeyPressed(Keys.DPAD_RIGHT) && !m_IsGamePaused){
			UserActionMoveRingRight();
			return true;
		}
		else if(Gdx.input.isKeyPressed(Keys.DPAD_UP) && !m_IsGamePaused){
			UserActionRollMarblesUp();
			return true;
		}
		else if(Gdx.input.isKeyPressed(Keys.DPAD_DOWN) && !m_IsGamePaused){
			UserActionRollMarblesDown();
			return true;
		}
		else if(Gdx.input.isKeyPressed(Keys.SPACE)  && !m_IsGamePaused){
			UserActionSlideMarblesRight();
			return true;
		}
		else if(Gdx.input.isKeyPressed(Keys.ENTER) && !m_IsGamePaused){
			UserActionContinue();
			return true;
		}
		else if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
			UserActionPause();
			return true;
		}
		else if(Gdx.input.isKeyPressed(Keys.R) ){
			Reinitialize();
			return true;
		}
		else if(Gdx.input.isKeyPressed(Keys.Q) ){
			m_IsQuitting = true;
			return true;
		}
		else if(Gdx.input.isKeyPressed(Keys.M) ){
			m_IsMusicOn = !m_IsMusicOn;
			ToggleMusic(m_IsMusicOn);
			return true;
		}

		return false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		m_CursorX = screenX;
		m_CursorY = screenY;
		m_IsTouchDown = true;
		return true;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if( (m_CursorX < screenX+m_Width*0.012f && m_CursorX > screenX-m_Width*0.012f) &&
			(m_CursorY < screenY+m_Width*0.012f && m_CursorY > screenY-m_Width*0.012f) ){
			if( m_PauseButton.IsCursorInside(m_CursorX, m_Height - m_CursorY) ){
				UserActionPause();
			}
			else if( m_MainMenuButton.IsCursorInside(m_CursorX, m_Height - m_CursorY) ){
				m_GoToMainMenu = true;
			}
			else{
				UserActionSlideMarblesRight();
			}
			return true;
		}
		m_IsTouchDown = false;
		return false;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(screenY > m_CursorY+m_Height*0.1 && m_IsTouchDown){
			m_CursorY = screenY;
			UserActionRollMarblesDown();
			m_IsTouchDown = false;
			return true;
		}
		else if(screenY < m_CursorY-m_Height*0.1 && m_IsTouchDown){
			m_CursorY = screenY;
			UserActionRollMarblesUp();
			m_IsTouchDown = false;
			return true;
		}
		
		if(screenX > m_CursorX+m_Width*0.1 && m_IsTouchDown){
			m_CursorX = screenX;
			UserActionMoveRingRight();
			m_IsTouchDown = false;
			return true;
		}
		else if(screenX < m_CursorX-m_Width*0.1 && m_IsTouchDown){
			m_CursorX = screenX;
			UserActionMoveRingLeft();
			m_IsTouchDown = false;
			return true;
		}
		return false;
	}
	
	/**************************************************************************/
	/* Private methods
	/**************************************************************************/
	private void InitializeMarblesTable(){
		for(int i=0; i<Configuration.NUMBER_OF_MARBLE_ROWS; i++){
			for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
				m_Cells[i][j] = null;
			}
		}
	}
	
	private void SetMarbleCenterAnchors(){
		float x;
		float y;
		for(int i=0; i<Configuration.NUMBER_OF_MARBLE_ROWS; i++){
			y = (float)m_GameBoardCenterY -
				(float)m_Configuration.GetDisplayMarbleHeight()*(float)Configuration.NUMBER_OF_MARBLE_ROWS/2f +
				(float)m_Configuration.GetDisplayMarbleHeight()*(float)i +
				(float)m_Configuration.GetDisplayMarbleHeight()/2f;
			for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
				x = (float)m_GameBoardCenterX -
					(float)m_Configuration.GetDisplayMarbleWidth()*(float)Configuration.NUMBER_OF_MARBLE_COLUMNS/2f +
					(float)m_Configuration.GetDisplayMarbleWidth()*(float)j +
					(float)m_Configuration.GetDisplayMarbleWidth()/2f;
				m_AnchorPoints[i][j] = new Vector2(x,y);
			}
		}
	}
	
	private void InitializeStartingMarbles(){
		float x;
		float y;
		// Set center row to random marbles (none null)
		for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
			x = (float)m_Configuration.GetDisplayMarbleWidth()*(float)j +
				(float)m_GameBoardCenterX -
				(float)m_Configuration.GetDisplayMarbleWidth() * (float)Configuration.NUMBER_OF_MARBLE_COLUMNS/2f + 
				(float)m_Configuration.GetDisplayMarbleWidth()/2f;
			
			m_Cells[m_CenterRow][j] = 
					new Marble(m_Configuration,
							   Marble.GenerateRandomColor(),
							   x,
							   m_GameBoardCenterY,
							   m_Configuration.GetDisplayMarbleWidth(),
							   m_Configuration.GetDisplayMarbleHeight(),
							   m_MarbleTextures,
							   true);
		}
		
		// Set rows 1 ... and -1 ... to random marbles. However, if the marble
		// in the previous row is null, don't generate anymore marbles for the
		// column.
		
		// Above the midpoint potentially spawn marbles
		for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
			for(int i=m_CenterRow; i<Configuration.NUMBER_OF_MARBLE_ROWS-Configuration.NUMBER_OF_ROWS_TO_NOT_POTENTIALLY_SPAWN_MARLBES; i++){
				y = (float)m_Configuration.GetDisplayMarbleHeight()*(float)i +
					(float)m_GameBoardCenterY -
					(float)m_Configuration.GetDisplayMarbleHeight() * (float)Configuration.NUMBER_OF_MARBLE_ROWS/2f + 
					(float)m_Configuration.GetDisplayMarbleHeight()/2f;
				
				if(m_Cells[i-1][j] != null){
					x = (float)m_Configuration.GetDisplayMarbleWidth()*(float)j +
						(float)m_GameBoardCenterX -
						(float)m_Configuration.GetDisplayMarbleWidth() * (float)Configuration.NUMBER_OF_MARBLE_COLUMNS/2f + 
						(float)m_Configuration.GetDisplayMarbleWidth()/2f;
					
					Marble.COLOR color = Marble.GenerateRandomColorOrNull();
					if(color != null){
						m_Cells[i][j] =
								new Marble(m_Configuration,
										   color,
										   x,
										   y,
										   m_Configuration.GetDisplayMarbleWidth(),
										   m_Configuration.GetDisplayMarbleHeight(),
										   m_MarbleTextures,
										   true);
					}
				}
			}
		}
		
		// Below the midpoint potentially spawn marbles
		for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
			for(int i=(int) Math.floor(Configuration.NUMBER_OF_MARBLE_ROWS/2)-1; i>=Configuration.NUMBER_OF_ROWS_TO_NOT_POTENTIALLY_SPAWN_MARLBES; i--){
				y = (float)m_Configuration.GetDisplayMarbleHeight()*(float)i +
					(float)m_GameBoardCenterY - (float)m_Configuration.GetDisplayMarbleHeight() * (float)Configuration.NUMBER_OF_MARBLE_ROWS/2f +
					(float)m_Configuration.GetDisplayMarbleHeight()/2f;
				
				if(m_Cells[i+1][j] != null){
					x = (float)m_Configuration.GetDisplayMarbleWidth()*(float)j +
						(float)m_GameBoardCenterX - (float)m_Configuration.GetDisplayMarbleWidth()*
						(float)Configuration.NUMBER_OF_MARBLE_COLUMNS/2f +
						(float)m_Configuration.GetDisplayMarbleWidth()/2f;
					
					Marble.COLOR color = Marble.GenerateRandomColorOrNull();
					if(color != null){
						m_Cells[i][j] = new Marble(m_Configuration, color, x, y, m_Configuration.GetDisplayMarbleWidth(), m_Configuration.GetDisplayMarbleHeight(), m_MarbleTextures, true);
					}
				}
			}
		}
	}
	
	private void RecenterMarbleColumn(int column){
		for(int i=0; i<Configuration.NUMBER_OF_MARBLE_ROWS; i++){
			if(m_Cells[i][column] != null){
				m_Cells[i][column].SetPositionCenter(m_AnchorPoints[i][column].x, m_AnchorPoints[i][column].y);
			}
		}
	}
	
	private void RecenterMarbleRow(int row){
		for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
			if(m_Cells[row][j] != null){
				m_Cells[row][j].SetPositionCenter(m_AnchorPoints[row][j].x, m_AnchorPoints[row][j].y);
			}
		}
	}
	
	private void UpdateKeyDownPositions(){
		if(m_IsShiftingRight){
			SlideMarblesRight();
		}
		
		for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
			if(m_IsColumnRollingUp[j] == SIDE.BOTH){
				RollMarblesUp(SIDE.BOTH, j);
			}
			if(m_IsColumnRollingDown[j] == SIDE.BOTH){
				RollMarblesDown(SIDE.BOTH, j);
			}
		}
	}
	
	private void SlideMarblesRight(){
		
		m_Roll.play(2f);
		
		float amountToShift = (float) m_Configuration.GetDisplayMarbleWidth() / (float) Configuration.NUMBER_OF_FRAMES_FOR_RIGHT_SLIDE_ANIMATION;
		
		Marble currentMarble;
		for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
			currentMarble = m_Cells[m_CenterRow][j];
			
			if(currentMarble.GetCenterX() + amountToShift > m_GameBoardCenterX + m_Configuration.GetGameBoardWidth()/2){
				currentMarble.SetPositionCenter( m_Cells[m_CenterRow][0].GetLeftBorderX()-currentMarble.GetWidth()/2, currentMarble.GetCenterY() );
			}
			else{
				currentMarble.SetPositionCenter( currentMarble.GetCenterX() + amountToShift, currentMarble.GetCenterY() );
			}
		}
		
		// Increment the frame number and check to see if the shift has finished
		m_CurrentFrameShiftingRight++;	
		if(m_CurrentFrameShiftingRight == Configuration.NUMBER_OF_FRAMES_FOR_RIGHT_SLIDE_ANIMATION){
			m_CurrentFrameShiftingRight = 0;
			m_IsShiftingRight = false;
			
			Marble tmpMarble1 = m_Cells[m_CenterRow][0];
			Marble tmpMarble2 = m_Cells[m_CenterRow][0];
			for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
				currentMarble = m_Cells[m_CenterRow][j];
				
				if(j==0){
					tmpMarble1 = m_Cells[m_CenterRow][0];
					m_Cells[m_CenterRow][0] = m_Cells[m_CenterRow][Configuration.NUMBER_OF_MARBLE_COLUMNS-1];
				}
				else{
					tmpMarble2 = currentMarble;
					m_Cells[m_CenterRow][j] = tmpMarble1;
					tmpMarble1 = tmpMarble2;
				}
			}
			RecenterMarbleRow(m_CenterRow);
		}
	}
	
	private void RollMarblesUp(SIDE side, int column){
		
		m_Roll.play(0.2f);
		
		Marble currentMarble;
		switch(side){
		
			case BOTTOM:
				for(int i=0; i<m_CenterRow; i++){
					currentMarble = m_Cells[i][column];
					
					if(currentMarble != null){
						currentMarble.SetPositionCenter( currentMarble.GetCenterX(),
														 currentMarble.GetCenterY() + m_Configuration.GetAmountToShiftRollingMarbles() );
						
						currentMarble.DecrementNextRollingUpAnimationFrame();
					}
				}
				
				// Increment the frame number and check to see if the shift has finished
				m_CurrentFrameShiftingUp[column]++;		
				if(m_CurrentFrameShiftingUp[column] == Configuration.NUMBER_OF_FRAMES_FOR_ROLLING_MARBLE_ANIMATION){
					m_CurrentFrameShiftingUp[column] = 0;
					m_IsColumnRollingUp[column] = SIDE.NEITHER;
					
					Marble tmpMarble1 = m_Cells[m_CenterRow][column];
					Marble tmpMarble2 = m_Cells[m_CenterRow][column];
					boolean firstMarble = false;
					for(int i=0; i<=m_CenterRow; i++){
						currentMarble = m_Cells[i][column];
						
						if(currentMarble != null){
							if(firstMarble == false){
								firstMarble = true;
								tmpMarble1 = currentMarble;
								//m_Cells[i][column].Dispose();
								m_Cells[i][column] = null;
							}
							else{
								tmpMarble2 = currentMarble;
								m_Cells[i][column] = tmpMarble1;
								tmpMarble1 = tmpMarble2;
							}
						}
						else if(firstMarble == true){
							tmpMarble2 = currentMarble;
							m_Cells[i][column] = tmpMarble1;
							tmpMarble1 = tmpMarble2;
							break;
						}
					}
					m_ColumnEmptyData[column].UpdateFirstEmptyCellIndexBottom(1);
					RecenterMarbleColumn(column);
				}
				break;
			
			case BOTH:
				// Don't allow movements that empty the center row or push marbles
				// off of the board
				if(m_Cells[Configuration.NUMBER_OF_MARBLE_ROWS-1][column] != null || 
				   m_Cells[m_CenterRow-1][column] == null){
					m_IsColumnRollingUp[column] = SIDE.NEITHER;
					m_NumberOfBacklogUserUpRolls[column] = 0;
					return;
				}
	
				for(int i=0; i<Configuration.NUMBER_OF_MARBLE_ROWS; i++){
					currentMarble = m_Cells[i][column];
					
					if(currentMarble != null){
						currentMarble.SetPositionCenter( currentMarble.GetCenterX(),
														 currentMarble.GetCenterY() + m_Configuration.GetAmountToShiftRollingMarbles() );
						
						currentMarble.DecrementNextRollingUpAnimationFrame();
					}
				}
				
				// Increment the frame number and check to see if the shift has finished
				m_ColumnFrameCount[column]++;
				if(m_ColumnFrameCount[column] == Configuration.NUMBER_OF_FRAMES_FOR_ROLLING_MARBLE_ANIMATION){
					m_ColumnFrameCount[column] = 0;
					
					m_NumberOfBacklogUserUpRolls[column]--;
					if(m_NumberOfBacklogUserUpRolls[column] == 0){
						m_IsColumnRollingUp[column] = SIDE.NEITHER;
					}
					
					Marble tmpMarble1 = m_Cells[m_CenterRow][column];
					Marble tmpMarble2 = m_Cells[m_CenterRow][column];
					boolean firstMarble = false;
					for(int i=0; i<Configuration.NUMBER_OF_MARBLE_ROWS; i++){
						currentMarble = m_Cells[i][column];
						
						if(currentMarble != null){
							if(firstMarble == false){
								firstMarble = true;
								tmpMarble1 = currentMarble;
								//m_Cells[i][column].Dispose();
								m_Cells[i][column] = null;
							}
							else{
								tmpMarble2 = currentMarble;
								m_Cells[i][column] = tmpMarble1;
								tmpMarble1 = tmpMarble2;
							}
						}
						else if(firstMarble == true){
							tmpMarble2 = currentMarble;
							m_Cells[i][column] = tmpMarble1;
							tmpMarble1 = tmpMarble2;
							break;
						}
					}
					m_ColumnEmptyData[column].UpdateFirstEmptyCellIndices(1);
					RecenterMarbleColumn(column);
				}
				break;
				
			default:
				break;
		}
	}
	
	private void RollMarblesDown(SIDE side, int column){
		
		m_Roll.play(0.2f);
		
		Marble currentMarble;
		switch(side){
		
			case TOP:
				for(int i=Configuration.NUMBER_OF_MARBLE_ROWS-1; i>m_CenterRow; i--){
					currentMarble = m_Cells[i][column];
	
					if(currentMarble != null){
						currentMarble.SetPositionCenter( currentMarble.GetCenterX(),
														 currentMarble.GetCenterY() - m_Configuration.GetAmountToShiftRollingMarbles() );
						
						currentMarble.IncrementNextRollingUpAnimationFrame();
					}
				}
				
				// Increment the frame number and check to see if the shift has finished
				m_CurrentFrameShiftingDown[column]++;		
				if(m_CurrentFrameShiftingDown[column] == Configuration.NUMBER_OF_FRAMES_FOR_ROLLING_MARBLE_ANIMATION){
					m_CurrentFrameShiftingDown[column] = 0;
					m_IsColumnRollingDown[column] = SIDE.NEITHER;
					
					Marble tmpMarble1 = m_Cells[m_CenterRow][column];
					Marble tmpMarble2 = m_Cells[m_CenterRow][column];
					boolean firstMarble = false;
					for(int i=Configuration.NUMBER_OF_MARBLE_ROWS-1; i>=m_CenterRow; i--){
						currentMarble = m_Cells[i][column];
						
						if(currentMarble != null){
							if(firstMarble == false){
								firstMarble = true;
								tmpMarble1 = currentMarble;
								//m_Cells[i][column].Dispose();
								m_Cells[i][column] = null;
							}
							else{
								tmpMarble2 = currentMarble;
								m_Cells[i][column] = tmpMarble1;
								tmpMarble1 = tmpMarble2;
							}
						}
						else if(firstMarble == true){
							tmpMarble2 = currentMarble;
							m_Cells[i][column] = tmpMarble1;
							tmpMarble1 = tmpMarble2;
							break;
						}
					}
					m_ColumnEmptyData[column].UpdateFirstEmptyCellIndexTop(-1);
					RecenterMarbleColumn(column);
				}
				break;
			
			case BOTH:
				// Don't allow movements that empty the center row or push marbles
				// off of the board
				if(m_Cells[0][column] != null || 
				   m_Cells[m_CenterRow+1][column] == null){
					m_IsColumnRollingDown[column] = SIDE.NEITHER;
					m_NumberOfBacklogUserDownRolls[column] = 0;
					return;
				}
	
				for(int i=0; i<Configuration.NUMBER_OF_MARBLE_ROWS; i++){
				currentMarble = m_Cells[i][column];
	
					if(currentMarble != null){
						currentMarble.SetPositionCenter( currentMarble.GetCenterX(),
														 currentMarble.GetCenterY() - m_Configuration.GetAmountToShiftRollingMarbles() );
						
						currentMarble.IncrementNextRollingUpAnimationFrame();
						
					}
				}
				
				// Increment the frame number and check to see if the shift has finished
				m_ColumnFrameCount[column]++;
				if(m_ColumnFrameCount[column] == Configuration.NUMBER_OF_FRAMES_FOR_ROLLING_MARBLE_ANIMATION){
					m_ColumnFrameCount[column] = 0;
					
					m_NumberOfBacklogUserDownRolls[column]--;
					if(m_NumberOfBacklogUserDownRolls[column] == 0){
						m_IsColumnRollingDown[column] = SIDE.NEITHER;
					}
					
					Marble tmpMarble1 = m_Cells[m_CenterRow][column];
					Marble tmpMarble2 = m_Cells[m_CenterRow][column];
					boolean firstMarble = false;
					for(int i=Configuration.NUMBER_OF_MARBLE_ROWS-1; i>=0; i--){
						currentMarble = m_Cells[i][column];
						
						if(currentMarble != null){
							if(firstMarble == false){
								firstMarble = true;
								tmpMarble1 = currentMarble;
								//m_Cells[i][column].Dispose();
								m_Cells[i][column] = null;
							}
							else{
								tmpMarble2 = currentMarble;
								m_Cells[i][column] = tmpMarble1;
								tmpMarble1 = tmpMarble2;
							}
						}
						else if(firstMarble == true){
							tmpMarble2 = currentMarble;
							m_Cells[i][column] = tmpMarble1;
							tmpMarble1 = tmpMarble2;
							break;
						}
					}
					m_ColumnEmptyData[column].UpdateFirstEmptyCellIndices(-1);
					RecenterMarbleColumn(column);
				}
				break;
				
			default:
				break;
		}
	}
	
	// TODO Incorporate chain bonuses
	private int CheckScoring(){
		// TODO Use a for loop here based on the game board width to generalize
		// 		the scoring detection.
		int startIndex = CheckForMarblesInARow(5);
		if(startIndex != -1){
			// Unless the roll sound is stopped, sometimes the scoring sound won't play
			// However, the scoring/despawning sound needs to play every time there is a score
			m_Roll.stop();
			m_Despawn.play();
			
			DetermineMarbleColumnShiftDirections(startIndex, 5);
			return 5;
		}
		
		startIndex = CheckForMarblesInARow(4);
		if(startIndex != -1){
			m_Roll.stop();
			m_Despawn.play();

			DetermineMarbleColumnShiftDirections(startIndex, 4);
			return 4;
		}
		
		startIndex = CheckForMarblesInARow(3);
		if(startIndex != -1){
			m_Roll.stop();
			m_Despawn.play();
			
			DetermineMarbleColumnShiftDirections(startIndex, 3);
			return 3;
		}
		
		return 0;
	}
	
	private void DetermineMarbleColumnShiftDirections(int startIndex, int numberInARow){
		for(int j=startIndex; j<numberInARow+startIndex; j++){
			// Remove the current center row since the shifting will be 
			// occurring now. 
			m_Cells[m_CenterRow][j].StartDespawnAnimation();
			
			if(m_Cells[m_CenterRow-1][j] == null){
				m_IsColumnRollingDown[j] = SIDE.TOP;
			}
			else if(m_Cells[m_CenterRow+1][j] == null){
				m_IsColumnRollingUp[j] = SIDE.BOTTOM;
			}
			else if(Math.random()>0.5){
				m_IsColumnRollingUp[j] = SIDE.BOTTOM;
			}
			else{
				m_IsColumnRollingDown[j] = SIDE.TOP;
			}
		}
		return;
	}
	
	private void UpdateScorePositions(){
		for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
			if(m_IsColumnRollingUp[j] == SIDE.BOTTOM){
				RollMarblesUp(SIDE.BOTTOM, j);
			}
			else if(m_IsColumnRollingDown[j] == SIDE.TOP){
				RollMarblesDown(SIDE.TOP, j);
			}
		}
	}
	
	// TODO Be able to return multiple indices. For example, for a board
	// 		width of 7, two groups of three should be able to be detected.
	private int CheckForMarblesInARow(int numberInARowCheck){
		int startIndex = -1;
		for(int i=0, end = Configuration.NUMBER_OF_MARBLE_COLUMNS-numberInARowCheck; i<=end; i++){
			startIndex++;
			Marble.COLOR firstColor = m_Cells[m_CenterRow][startIndex].GetColor();
			Marble.COLOR currentColor;
			for(int j=startIndex+1; j<numberInARowCheck+startIndex; j++){
				currentColor = m_Cells[m_CenterRow][j].GetColor();
				if(currentColor != firstColor){
					break;
				}
				// Returns here once the first chain is found. Fine for
				// 5 columns, but not for 7, etc
				else if(j == numberInARowCheck+startIndex-1){
					return startIndex;
				}
			}
		}
		return -1;
	}
	
	private boolean IsUpdatingKeyDownPositions(){
		if(m_IsShiftingRight){
			return true;
		}
		for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
			if(m_IsColumnRollingUp[j] == SIDE.BOTH || m_IsColumnRollingDown[j] == SIDE.BOTH){
				return true;
			}
		}
		return false;
	}
	
	private boolean IsUpdatingDespawningMarbles(){
		for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
			if(m_Cells[m_CenterRow][j] != null){
				if( m_Cells[m_CenterRow][j].IsDespawning() ){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean IsUpdatingScorePositions(){
		for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
			if(m_IsColumnRollingUp[j] == SIDE.BOTTOM || m_IsColumnRollingDown[j] == SIDE.TOP){
				return true;
			}
		}
		return false;
	}
	
	private void CheckForLowMarbleCount(){		
		Arrays.fill(m_LowMarbleCountColumns, false);
		int marbleCount = 0;
		for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
			for(int i=0; i<Configuration.NUMBER_OF_MARBLE_ROWS; i++){
				if(m_Cells[i][j] != null){
					marbleCount++;
				}
			}
			if(marbleCount <= Configuration.LOW_COUNT_NUMBER_OF_MARBLES){
				m_LowMarbleCountColumns[j] = true;
			}
			marbleCount = 0;
		}
		return;
	}
	
	private boolean IsUpdatingDroppedMarblePositions(){
		for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
			if(m_IsDroppingTop[j] || m_IsDroppingBottom[j]){
				return true;
			}
		}
		return false;
	}
	
	private void AddDroppedMarbles(SIDE side, int column, int numberOfMarblesToDrop){
		for(int i=0; i<numberOfMarblesToDrop; i++){
			if(side == SIDE.TOP){
				m_DroppedMarblesTop[i][column] = new Marble(m_Configuration,
															Marble.GenerateRandomColor(), m_Cells[m_CenterRow][column].GetCenterX(), 
										  				    m_Cells[m_CenterRow][column].GetCenterY() +
										  				    m_Configuration.GetDisplayMarbleHeight()*(int) Math.floor(Configuration.NUMBER_OF_MARBLE_ROWS/2) +
										  				    (i+Configuration.NUMBER_OF_MARBLE_SPACES_OUTSIDE_OF_BOARD_FOR_NEW_DROPS)*m_Configuration.GetDisplayMarbleHeight(),
										  				    m_Configuration.GetDisplayMarbleHeight(),
										  				    m_Configuration.GetDisplayMarbleWidth(),
										  				    m_MarbleTextures,
										  				    true);
				m_IsDroppingTop[column] = true;
			}
			else if(side == SIDE.BOTTOM){
				m_DroppedMarblesBottom[i][column] = new Marble(m_Configuration,
															   Marble.GenerateRandomColor(), m_Cells[m_CenterRow][column].GetCenterX(), 
										     				   m_Cells[m_CenterRow][column].GetCenterY() -
										     				   m_Configuration.GetDisplayMarbleHeight()*(int) Math.floor(Configuration.NUMBER_OF_MARBLE_ROWS/2) -
										     				   (i+Configuration.NUMBER_OF_MARBLE_SPACES_OUTSIDE_OF_BOARD_FOR_NEW_DROPS)*m_Configuration.GetDisplayMarbleHeight(),
										     				   m_Configuration.GetDisplayMarbleHeight(),
											  				   m_Configuration.GetDisplayMarbleWidth(),
											  				   m_MarbleTextures,
										     				   true);
				m_IsDroppingBottom[column] = true;
			}
		}
	}
	
	private void UpdateDroppedMarblePositions(){
		for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
			if(m_IsDroppingTop[j] == true){
				UpdateDroppedMarblePositionForColumn(SIDE.TOP, j);
			}
			
			if(m_IsDroppingBottom[j] == true){
				UpdateDroppedMarblePositionForColumn(SIDE.BOTTOM, j);
			}
		}
	}
	
	private boolean IsCollision(SIDE side, int column){
		switch(side){
			case TOP:
				// NOTE: The extra calculation in the comparison is needed because of rounding errors in how far a marble moves each frame; without it marbles
				// can overlap
				if(FindDroppedMarbleBoundaryInner(side, column)-m_Configuration.GetAmountToShiftDroppedMarbles() <= FindGameBoardMarbleBoundaryOuter(side, column) ){
					return true;
				}
				break;
				
			case BOTTOM:
				// NOTE: The extra calculation in the comparison is needed because of rounding errors in how far a marble moves each frame; without it marbles
				// can overlap
				if(FindDroppedMarbleBoundaryInner(side, column)+m_Configuration.GetAmountToShiftDroppedMarbles() >= FindGameBoardMarbleBoundaryOuter(side, column) ){
					return true;
				}
				break;
				
			default:
				break;
		}
		return false;
	}
	
	private float FindDroppedMarbleBoundaryInner(SIDE side, int column){
		float top = -1;	
		switch(side){
			case TOP:
				if(m_DroppedMarblesTop[0][column] != null){
					top = m_DroppedMarblesTop[0][column].GetBottomBorderY();
				}
				break;
				
			case BOTTOM:
				if(m_DroppedMarblesBottom[0][column] != null){
					top = m_DroppedMarblesBottom[0][column].GetTopBorderY();
				}
				break;
				
			default:
				break;
		}
		return top;
	}
	
	private float FindGameBoardMarbleBoundaryOuter(SIDE side, int column){
		float top = -1;
		switch(side){
			case TOP:
				for(int i=m_CenterRow-1; i<Configuration.NUMBER_OF_MARBLE_ROWS; i++){
					if(m_Cells[i][column] != null){
						top = m_Cells[i][column].GetTopBorderY();
					}
				}
				break;
				
			case BOTTOM:
				for(int i=m_CenterRow+1; i>=0; i--){
					if(m_Cells[i][column] != null){
						top = m_Cells[i][column].GetBottomBorderY();
					}
				}
				break;
				
			default:
				break;
		}
		return top;
	}
	
	private void UpdateDroppedMarblePositionForColumn(SIDE side, int column){
		if( !IsCollision(side, column) ){
			for(int i=0; i<Configuration.MAX_NUMBER_OF_DROPPED_MARBLES; i++){
				switch(side){
					
					case TOP:
						if(m_DroppedMarblesTop[i][column] != null){
						   m_DroppedMarblesTop[i][column].IncrementNextRollingUpAnimationFrame();
						   m_DroppedMarblesTop[i][column].SetPositionCenter(m_DroppedMarblesTop[i][column].GetCenterX(),
								   											m_DroppedMarblesTop[i][column].GetCenterY() -
								   											m_Configuration.GetAmountToShiftDroppedMarbles());
						}
						break;
				
					case BOTTOM:
						if(m_DroppedMarblesBottom[i][column] != null){
						   m_DroppedMarblesBottom[i][column].DecrementNextRollingUpAnimationFrame();
						   m_DroppedMarblesBottom[i][column].SetPositionCenter(m_DroppedMarblesBottom[i][column].GetCenterX(),
																	 	       m_DroppedMarblesBottom[i][column].GetCenterY() +
																	 	       m_Configuration.GetAmountToShiftDroppedMarbles());
						}
						break;
						
					default:
						break;
				}
			}
		}
		else{
			if(m_IsColumnRollingUp[column] == SIDE.BOTH){
				for(int i=0; i<Configuration.MAX_NUMBER_OF_DROPPED_MARBLES; i++){
					switch(side){
					
						case TOP:
							if(m_DroppedMarblesTop[i][column] != null){
							   m_DroppedMarblesTop[i][column].DecrementNextRollingUpAnimationFrame();
							   m_DroppedMarblesTop[i][column].SetPositionCenter( m_DroppedMarblesTop[i][column].GetCenterX(),
									   											 m_Cells[m_ColumnEmptyData[column].GetFirstEmptyCellIndexTop()-1][column].GetTopBorderY() + 
									   											 m_Configuration.GetDisplayMarbleHeight()/2 + m_Configuration.GetDisplayMarbleHeight()*i +
									   											 m_Configuration.GetAmountToShiftRollingMarbles() );
							}
							break;
							
						case BOTTOM:
							if(m_DroppedMarblesBottom[i][column] != null){
							   m_DroppedMarblesBottom[i][column].DecrementNextRollingUpAnimationFrame();
							   m_DroppedMarblesBottom[i][column].SetPositionCenter(m_DroppedMarblesBottom[i][column].GetCenterX(),
																				   m_Cells[m_ColumnEmptyData[column].GetFirstEmptyCellIndexBottom()+1][column].GetBottomBorderY() - 
																				   m_Configuration.GetDisplayMarbleHeight()/2 - m_Configuration.GetDisplayMarbleHeight()*i);
							}
							break;
							
						default:
							break;
					}
				}
			}
			else if(m_IsColumnRollingDown[column] == SIDE.BOTH){
				for(int i=0; i<Configuration.MAX_NUMBER_OF_DROPPED_MARBLES; i++){
					switch(side){
						
						case TOP:
							if(m_DroppedMarblesTop[i][column] != null){
							   m_DroppedMarblesTop[i][column].IncrementNextRollingUpAnimationFrame();
							   m_DroppedMarblesTop[i][column].SetPositionCenter(m_DroppedMarblesTop[i][column].GetCenterX(),
																			    m_Cells[m_ColumnEmptyData[column].GetFirstEmptyCellIndexTop()-1][column].GetTopBorderY() + 
																				m_Configuration.GetDisplayMarbleHeight()/2 + m_Configuration.GetDisplayMarbleHeight()*i);
							}
							break;
							
						case BOTTOM:
							if(m_DroppedMarblesBottom[i][column] != null){
							   m_DroppedMarblesBottom[i][column].IncrementNextRollingUpAnimationFrame();
							   m_DroppedMarblesBottom[i][column].SetPositionCenter( m_DroppedMarblesBottom[i][column].GetCenterX(),
																				    m_Cells[m_ColumnEmptyData[column].GetFirstEmptyCellIndexBottom()+1][column].GetBottomBorderY() - 
																				    m_Configuration.GetDisplayMarbleHeight()/2 - m_Configuration.GetDisplayMarbleHeight()*i - 
																				    m_Configuration.GetAmountToShiftRollingMarbles() );
							}
							break;
							
						default:
							break;
					}
				}
			}
			else{
				m_Click.play(3f);
				
				for(int i=0; i<Configuration.MAX_NUMBER_OF_DROPPED_MARBLES; i++){
					switch(side){
						
						case TOP:
							if(m_DroppedMarblesTop[i][column] != null){
							   //m_DroppedMarblesTop[i][column].DecrementNextRollingUpAnimationFrame();
							   m_DroppedMarblesTop[i][column].SetPositionCenter(m_DroppedMarblesTop[i][column].GetCenterX(),
									   											m_Cells[m_ColumnEmptyData[column].GetFirstEmptyCellIndexTop()-1][column].GetTopBorderY() + 
									   											m_Configuration.GetDisplayMarbleHeight()/2 + m_Configuration.GetDisplayMarbleHeight()*i);
							}
							break;
							
						case BOTTOM:
							if(m_DroppedMarblesBottom[i][column] != null){
							   //m_DroppedMarblesBottom[i][column].IncrementNextRollingUpAnimationFrame();
							   m_DroppedMarblesBottom[i][column].SetPositionCenter(m_DroppedMarblesBottom[i][column].GetCenterX(),
																				   m_Cells[m_ColumnEmptyData[column].GetFirstEmptyCellIndexBottom()+1][column].GetBottomBorderY() - 
																				   m_Configuration.GetDisplayMarbleHeight()/2 - m_Configuration.GetDisplayMarbleHeight()*i);
							}
							break;
							
						default:
							break;
					}
				}
				
				MergeDroppedMarblesIntoCells(side, column);
				m_IsDroppingTop[column] = false;
				m_IsDroppingBottom[column] = false;
			}
		}
	}
	
	private void MergeDroppedMarblesIntoCells(SIDE side, int column){
		int cntr = 0;
		switch(side){
		
			case TOP:	
				for(int i=m_CenterRow+1; i<Configuration.NUMBER_OF_MARBLE_ROWS; i++){
					if(m_Cells[i][column] == null && m_DroppedMarblesTop[cntr][column] != null){
						m_Cells[i][column] = m_DroppedMarblesTop[cntr][column];
						m_ColumnEmptyData[column].UpdateFirstEmptyCellIndexTop(1);
						m_DroppedMarblesTop[cntr][column] = null;
						cntr++;
						if(cntr == Configuration.MAX_NUMBER_OF_DROPPED_MARBLES){
							return;
						}
					}
				}
				// Erase any dropped marbles that have been pushed off of the game board
				if(cntr<Configuration.MAX_NUMBER_OF_DROPPED_MARBLES){
					for(int i=cntr; i<Configuration.MAX_NUMBER_OF_DROPPED_MARBLES; i++){
						if(m_DroppedMarblesTop[cntr][column] != null){
							m_DroppedMarblesTop[cntr][column].StartDespawnAnimation();
							cntr++;
						}
					}
				}
				break;
				
			case BOTTOM:
				for(int i=m_CenterRow-1; i>=0; i--){
					if(m_Cells[i][column] == null && m_DroppedMarblesBottom[cntr][column] != null){
						m_Cells[i][column] = m_DroppedMarblesBottom[cntr][column];
						m_ColumnEmptyData[column].UpdateFirstEmptyCellIndexBottom(-1);
						m_DroppedMarblesBottom[cntr][column] = null;
						cntr++;
						if(cntr == Configuration.MAX_NUMBER_OF_DROPPED_MARBLES){
							return;
						}
					}
				}
				// Erase any dropped marbles that have been pushed off of the game board
				if(cntr<Configuration.MAX_NUMBER_OF_DROPPED_MARBLES){
					for(int i=cntr; i<Configuration.MAX_NUMBER_OF_DROPPED_MARBLES; i++){
						if(m_DroppedMarblesBottom[cntr][column] != null){
							m_DroppedMarblesBottom[cntr][column].StartDespawnAnimation();
							cntr++;
						}
					}
				}
				break;
				
			default:
				break;
		}
		return;
	}
	
	private void AddMarbles(SIDE side, int column, int numberOfMarblesToAdd){
		int count = 0;
		if(side == SIDE.TOP){
			for(int i=m_CenterRow+1; i<Configuration.NUMBER_OF_MARBLE_ROWS; i++){
				if(m_Cells[i][column] == null){
					m_Cells[i][column] = new Marble(m_Configuration,
													Marble.GenerateRandomColor(), m_Cells[i-1][column].GetCenterX(), 
													m_Cells[i-1][column].GetCenterY()+m_Configuration.GetDisplayMarbleHeight(), 
													m_Configuration.GetDisplayMarbleHeight(),
								  				    m_Configuration.GetDisplayMarbleWidth(),
								  				    m_MarbleTextures,
													true);
					m_ColumnEmptyData[column].UpdateFirstEmptyCellIndexTop(1);
					count++;
					if(count == numberOfMarblesToAdd){
						break;
					}
				}
			}
		}
		else if(side == SIDE.BOTTOM){
			for(int i=m_CenterRow-1; i>=0; i--){
				if(m_Cells[i][column] == null){
					m_Cells[i][column] = new Marble(m_Configuration,
													Marble.GenerateRandomColor(), m_Cells[i+1][column].GetCenterX(), 
													m_Cells[i+1][column].GetCenterY()-m_Configuration.GetDisplayMarbleHeight(),
													m_Configuration.GetDisplayMarbleHeight(),
								  				    m_Configuration.GetDisplayMarbleWidth(),
								  				    m_MarbleTextures,
								  				    true);
					m_ColumnEmptyData[column].UpdateFirstEmptyCellIndexBottom(-1);
					count++;
					if(count == numberOfMarblesToAdd){
						break;
					}
				}
			}
		}
		return;
	}
	
	private void AddMarblesToLowCountColumns(){
		// Check which columns need marbles
		CheckForLowMarbleCount();
		
		for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
			if(m_LowMarbleCountColumns[j] == true){
				SIDE side = RandomlyDetermineMarbleAddSide();
				int numberOfMarblesToAdd = 1 + (int) Math.floor(Math.random()*Configuration.MAX_NUMBER_OF_RESPAWN_MARBLES);
				if(numberOfMarblesToAdd > Configuration.MAX_NUMBER_OF_RESPAWN_MARBLES){
					numberOfMarblesToAdd = Configuration.MAX_NUMBER_OF_RESPAWN_MARBLES;
				}
				if(side == SIDE.TOP){
					AddMarbles(side, j, numberOfMarblesToAdd);
				}
				else if(side == SIDE.BOTTOM){
					AddMarbles(side, j, numberOfMarblesToAdd);
				}
			}
		}
	}
	
	private SIDE RandomlyDetermineMarbleAddSide(){
		if(Math.random() > 0.5){
			return SIDE.TOP;
		}
		else{
			return SIDE.BOTTOM;
		}
	}
	
	private int RandomlyDetermineMarbleColumn(){
		int val = (int) Math.floor(Math.random()*(Configuration.NUMBER_OF_MARBLE_COLUMNS));
		if(val == Configuration.NUMBER_OF_MARBLE_COLUMNS){
			// Choose center column for border case
			return (int) Math.floor(Configuration.NUMBER_OF_MARBLE_COLUMNS/2);
		}
		else{
			return val;
		}
	}
	
	private void RandomlyDropMarblesIntoOpenCells(){
		
		GetNumberOfEmptyColumnCells();
		
		int column = RandomlyDetermineMarbleColumn();
		while( m_ColumnEmptyData[column].IsColumnFull() ){
			column++;
			column %= Configuration.NUMBER_OF_MARBLE_COLUMNS;
		}
		
		SIDE side = RandomlyDetermineMarbleAddSide();
		int numberOfEmptyCells = 0;
		switch(side){
		
			case TOP:
				numberOfEmptyCells = m_ColumnEmptyData[column].GetNumberOfEmptyCellsTop();
				if(numberOfEmptyCells == 0){
					// Switch to bottom
					numberOfEmptyCells = m_ColumnEmptyData[column].GetNumberOfEmptyCellsBottom();
					int numberOfMarblesToDrop = RandomlyDetermineNumberOfMarblesToDrop();
					while(numberOfMarblesToDrop > numberOfEmptyCells){
						numberOfMarblesToDrop--;
					}
					AddDroppedMarbles(SIDE.BOTTOM, column, numberOfMarblesToDrop);
				}
				else{
					int numberOfMarblesToDrop = RandomlyDetermineNumberOfMarblesToDrop();
					while(numberOfMarblesToDrop > numberOfEmptyCells){
						numberOfMarblesToDrop--;
					}
					AddDroppedMarbles(SIDE.TOP, column, numberOfMarblesToDrop);
				}
				break;
				
			case BOTTOM:
				numberOfEmptyCells = m_ColumnEmptyData[column].GetNumberOfEmptyCellsBottom();
				if(numberOfEmptyCells == 0){
					// Switch to top
					numberOfEmptyCells = m_ColumnEmptyData[column].GetNumberOfEmptyCellsTop();
					int numberOfMarblesToDrop = RandomlyDetermineNumberOfMarblesToDrop();
					while(numberOfMarblesToDrop > numberOfEmptyCells){
						numberOfMarblesToDrop--;
					}
					AddDroppedMarbles(SIDE.TOP, column, numberOfMarblesToDrop);
				}
				else{
					int numberOfMarblesToDrop = RandomlyDetermineNumberOfMarblesToDrop();
					while(numberOfMarblesToDrop > numberOfEmptyCells){
						numberOfMarblesToDrop--;
					}
					AddDroppedMarbles(SIDE.BOTTOM, column, numberOfMarblesToDrop);
				}
				break;
				
			default:
				break;
		}
		return;
	}
	
	private void CleanupDespawnedMarbles(){
		for(int i=0; i<Configuration.NUMBER_OF_MARBLE_ROWS; i++){
			for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
				if(m_Cells[i][j] != null){
					if( m_Cells[i][j].IsDespawned() ){
						m_Cells[i][j].Dispose();
						m_Cells[i][j] = null;
					}
				}
			}
		}
		
		for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
			for(int i=0; i<Configuration.MAX_NUMBER_OF_DROPPED_MARBLES; i++){
				if(m_DroppedMarblesTop[i][j] != null){
					if( m_DroppedMarblesTop[i][j].IsDespawned() ){
						m_DroppedMarblesTop[i][j].Dispose();
						m_DroppedMarblesTop[i][j] = null;
					}
				}
				
				if(m_DroppedMarblesBottom[i][j] != null){
					if( m_DroppedMarblesBottom[i][j].IsDespawned() ){
						m_DroppedMarblesBottom[i][j].Dispose();
						m_DroppedMarblesBottom[i][j] = null;
					}
				}
			}
		}
	}
	
	private void UserActionRollMarblesUp(){
		if( !m_IsShiftingRight && !IsUpdatingScorePositions() ){
			// Don't allow a rolling backlog to form in the opposite direction of what is currently being processed
			if(m_NumberOfBacklogUserDownRolls[m_SelectedColumn] == 0){
				m_NumberOfBacklogUserUpRolls[m_SelectedColumn]++;
				LimitUserBacklogRollsUp(m_SelectedColumn);
				m_IsColumnRollingUp[m_SelectedColumn] = SIDE.BOTH;
			}
		}
	}
	
	private void UserActionRollMarblesDown(){
		if( !m_IsShiftingRight && !IsUpdatingScorePositions() ){
			// Don't allow a rolling backlog to form in the opposite direction of what is currently being processed
			if(m_NumberOfBacklogUserUpRolls[m_SelectedColumn] == 0){
				m_NumberOfBacklogUserDownRolls[m_SelectedColumn]++;
				LimitUserBacklogRollsDown(m_SelectedColumn);
				m_IsColumnRollingDown[m_SelectedColumn] = SIDE.BOTH;
			}
		}
	}
	
	private void UserActionMoveRingLeft(){
		if( !m_IsShiftingRight && !IsUpdatingScorePositions() ){
			if( m_Ring.GetLeftBorderX() > m_Cells[m_CenterRow][0].GetLeftBorderX() && !m_IsShiftingRight ){
				m_Ring.SetPositionCenter( m_Ring.GetCenterX() - m_Configuration.GetDisplayMarbleWidth(), m_Ring.GetCenterY() );
			}
			else{
				m_Ring.SetPositionCenter( m_Cells[m_CenterRow][Configuration.NUMBER_OF_MARBLE_COLUMNS-1].GetCenterX(), m_Ring.GetCenterY() );
			}
			
			m_SelectedColumn--;
			if(m_SelectedColumn < 0){
				m_SelectedColumn = Configuration.NUMBER_OF_MARBLE_COLUMNS-1;
			}
		}
	}
	
	private void UserActionMoveRingRight(){
		if( !m_IsShiftingRight && !IsUpdatingScorePositions() ){
			if( m_Ring.GetRightBorderX() < m_Cells[m_CenterRow][Configuration.NUMBER_OF_MARBLE_COLUMNS-1].GetRightBorderX() ){
				m_Ring.SetPositionCenter( m_Ring.GetCenterX() + m_Configuration.GetDisplayMarbleWidth(), m_Ring.GetCenterY() );
			}
			else{
				m_Ring.SetPositionCenter( m_Cells[m_CenterRow][0].GetCenterX(), m_Ring.GetCenterY() );
			}
			
			m_SelectedColumn++;
			if(m_SelectedColumn > Configuration.NUMBER_OF_MARBLE_COLUMNS-1){
				m_SelectedColumn = 0;
			}
		}
	}
	
	private void UserActionSlideMarblesRight(){
		if( !IsUpdatingKeyDownPositions() && !IsUpdatingScorePositions() ){
			m_IsShiftingRight = true;
		}
	}
	
	private void UserActionContinue(){
		if(m_IsGameOver || m_IsGameWon){
			if(m_GameCompleteMusic != null){
				m_GameCompleteMusic.stop();
			}
			m_GoToMainMenu = true;
			m_NextScreen = LoseYourMarblesCloneGame.SCREEN.MAIN_MENU;
		}
	}
	
	private void UserActionPause(){
		m_IsGamePaused = !m_IsGamePaused;
		m_StatusOverlays.SetGamePausedStatus(m_IsGamePaused);
		if(m_IsGamePaused){
			if(m_BackgroundMusic != null){
				m_BackgroundMusic.pause();
			}
		}
		else{
			if(m_BackgroundMusic != null){
				m_BackgroundMusic.play();
			}
		}
	}
	
	private void LimitUserBacklogRollsUp(int column){
		for(int i=m_CenterRow+1; i<Configuration.NUMBER_OF_MARBLE_ROWS; i++){
			if(m_Cells[i][column] == null){
				if(m_NumberOfBacklogUserUpRolls[column] > Configuration.NUMBER_OF_MARBLE_ROWS - i){
					m_NumberOfBacklogUserUpRolls[column] = Configuration.NUMBER_OF_MARBLE_ROWS - i;
					return;
				}
				break;
			}
		}
	}
	
	private void LimitUserBacklogRollsDown(int column){
		for(int i=m_CenterRow-1; i>=0; i--){
			if(m_Cells[i][column] == null){
				if(m_NumberOfBacklogUserDownRolls[column] > i){
					m_NumberOfBacklogUserDownRolls[column] = i;
					return;
				}
				break;
			}
		}
	}
	
	private void GetNumberOfEmptyColumnCells(){
		for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
			m_ColumnEmptyData[j].Reset();
			for(int i=0; i<Configuration.NUMBER_OF_MARBLE_ROWS; i++){
				if(m_Cells[i][j] == null){
					if( i>m_CenterRow ){
						m_ColumnEmptyData[j].IncrementNumberOfEmptyCellsTop();
					}
					else if(i<m_CenterRow){
						m_ColumnEmptyData[j].IncrementNumberOfEmptyCellsBottom();
					}
				}
			}
		}
	}
	
	private int RandomlyDetermineNumberOfMarblesToDrop(){
		int val = 1 + (int) Math.floor(Math.random()*Configuration.MAX_NUMBER_OF_DROPPED_MARBLES);
		if(val > Configuration.MAX_NUMBER_OF_DROPPED_MARBLES){
			return Configuration.MAX_NUMBER_OF_DROPPED_MARBLES;
		}
		else{
			return val;
		}
	}
	
	private boolean IsBoardFull(){
		for(int i=0; i<Configuration.NUMBER_OF_MARBLE_ROWS; i++){
			for(int j=0; j<Configuration.NUMBER_OF_MARBLE_COLUMNS; j++){
				if(m_Cells[i][j] == null){
					return false;
				}
			}
		}
		//System.out.println("Game Over!");
		return true;
	}
	
	private String GetRandomBackgroundImage(){
		return Configuration.BACKGROUND_IMAGES[GenerateRandomInteger(0, Configuration.BACKGROUND_IMAGES.length-1)];
	}
	
	private int GenerateRandomInteger(int lowThreshold, int highThreshold){
		int val = highThreshold+1;
		while(val > highThreshold){
			val = (int) Math.floor(Math.random()*((highThreshold-lowThreshold)+1)) + lowThreshold;
		}
		return val;
	}
	
	// Structure to hold empty column data
	private class ColumnEmptyData{
		private int m_NumberOfEmptyCellsTop;
		private int m_NumberOfEmptyCellsBottom;
		private int m_IndexOfFirstEmptyCellTop;
		private int m_IndexOfFirstEmptyCellBottom;
		private int m_Column;
		ColumnEmptyData(int column){
			m_NumberOfEmptyCellsTop = 0;
			m_NumberOfEmptyCellsBottom = 0;
			m_Column = column;
			UpdateFirstEmptyCellIndexTop();
			UpdateFirstEmptyCellIndexBottom();
		}
		public void Reset(){
			m_NumberOfEmptyCellsTop = 0;
			m_NumberOfEmptyCellsBottom = 0;
		}
		public int GetNumberOfEmptyCellsTop(){
			return m_NumberOfEmptyCellsTop;
		}
		public int GetNumberOfEmptyCellsBottom(){
			return m_NumberOfEmptyCellsBottom;
		}
		public void IncrementNumberOfEmptyCellsTop(){
			m_NumberOfEmptyCellsTop++;
		}
		public void IncrementNumberOfEmptyCellsBottom(){
			m_NumberOfEmptyCellsBottom++;
		}
		public boolean IsColumnFull(){
			if(m_NumberOfEmptyCellsTop == 0 && m_NumberOfEmptyCellsBottom == 0){
				return true;
			}
			return false;
		}
		/*public void SetColumn(int index){
			m_Column = index;
		}*/
		public int GetFirstEmptyCellIndexTop(){
			return m_IndexOfFirstEmptyCellTop;
		}
		public int GetFirstEmptyCellIndexBottom(){
			return m_IndexOfFirstEmptyCellBottom;
		}
		public void UpdateFirstEmptyCellIndexTop(){
			for(int i=m_CenterRow+1; i<Configuration.NUMBER_OF_MARBLE_ROWS; i++){
				if(m_Cells[i][m_Column] == null){
					m_IndexOfFirstEmptyCellTop = i;
					break;
				}
			}
		}
		
		public void UpdateFirstEmptyCellIndexBottom(){
			for(int i=m_CenterRow-1; i>=0; i--){
				if(m_Cells[i][m_Column] == null){
					m_IndexOfFirstEmptyCellBottom = i;
					break;
				}
			}
		}
		
		public void UpdateFirstEmptyCellIndexTop(int additionalMoves){
			m_IndexOfFirstEmptyCellTop += additionalMoves;
		}
		
		public void UpdateFirstEmptyCellIndexBottom(int additionalMoves){
			m_IndexOfFirstEmptyCellBottom += additionalMoves;
		}
		
		public void UpdateFirstEmptyCellIndices(int additionalMoves){
			m_IndexOfFirstEmptyCellTop += additionalMoves;
			m_IndexOfFirstEmptyCellBottom += additionalMoves;
		}
	}
}
