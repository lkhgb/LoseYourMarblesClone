/**************************************************************************
 * Project : Lose Your Marbles Clone
 * File : Configuration.java
 * Author : j5backup
 * Purpose : Holds paths to assets and game parameter settings.
 *************************************************************************/

package com.j5backup.loseyourmarblesclone;

public class Configuration {
	
	/**************************************************************************/
	/* Enumerations
	/**************************************************************************/
	public enum DIFFICULTY{
		EASY, HARD
	}
	
	/**************************************************************************/
	/* Private members
	/* These are not constants because they are dependent directly or indirectly
	/* on the screen size.
	/**************************************************************************/
	private int m_DisplayMarbleWidth;
	private int m_DisplayMarbleHeight;
	private int m_GameBoardWidth;
	private int m_GameBoardHeight;
	private float m_AmountToShiftDroppedMarblesPerTimeSlice;
	private float m_AmountToShiftRollingMarblesPerTimeSlice;
	
	/**************************************************************************/
	/* Constructor
	/**************************************************************************/
	Configuration(int displayMarbleWidth, int displayMarbleHeight){
		m_DisplayMarbleWidth = displayMarbleWidth;
		m_DisplayMarbleHeight = displayMarbleHeight;
		m_GameBoardWidth = m_DisplayMarbleWidth*NUMBER_OF_MARBLE_COLUMNS;
		m_GameBoardHeight = m_DisplayMarbleHeight*NUMBER_OF_MARBLE_ROWS;
		m_AmountToShiftDroppedMarblesPerTimeSlice = (float) m_DisplayMarbleHeight /
													(float) NUMBER_OF_FRAMES_FOR_DROPPED_MARBLE_ANIMATION;
		m_AmountToShiftRollingMarblesPerTimeSlice = (float) m_DisplayMarbleHeight /
													(float) NUMBER_OF_FRAMES_FOR_ROLLING_MARBLE_ANIMATION;
	}
	
	/**************************************************************************/
	/* Public methods
	/**************************************************************************/
	public int GetDisplayMarbleWidth(){
		return m_DisplayMarbleWidth;
	}
	public int GetDisplayMarbleHeight(){
		return m_DisplayMarbleHeight;
	}
	public int GetGameBoardWidth(){
		return m_GameBoardWidth;
	}
	public int GetGameBoardHeight(){
		return m_GameBoardHeight;
	}
	public float GetAmountToShiftDroppedMarbles(){
		return m_AmountToShiftDroppedMarblesPerTimeSlice;
	}
	public float GetAmountToShiftRollingMarbles(){
		return m_AmountToShiftRollingMarblesPerTimeSlice;
	}
	
	/**************************************************************************/
	/* Game configuration parameters, all public constants
	/**************************************************************************/
	
	// Game board initialization
	///////////////////////////////////////////////////////////////////////////
	public static final int NUMBER_OF_MARBLE_COLUMNS = 5;
	public static final int NUMBER_OF_MARBLE_ROWS = 15;
	public static final int NUMBER_OF_ROWS_TO_NOT_POTENTIALLY_SPAWN_MARLBES = 4;
	public static final float GAME_BOARD_LINE_OPACITY = 0.85f;
	
	// Gameplay
	///////////////////////////////////////////////////////////////////////////
	public static final int LOW_COUNT_NUMBER_OF_MARBLES = 2;
	public static final int MAX_NUMBER_OF_RESPAWN_MARBLES = 2;
	public static final int MAX_NUMBER_OF_DROPPED_MARBLES = 1;
	public static final int NUMBER_OF_MARBLE_SPACES_OUTSIDE_OF_BOARD_FOR_NEW_DROPS = 6;
	public static final double THRESHOLD_FOR_MARBLE_DROP_EASY = 0.99;
	public static final double THRESHOLD_FOR_MARBLE_DROP_HARD = 0.91;
	
	// Animations
	///////////////////////////////////////////////////////////////////////////
	public static final int NUMBER_OF_FRAMES_FOR_RIGHT_SLIDE_ANIMATION = 6;
	public static final int NUMBER_OF_MARBLE_SPAWN_BLINKS = 2;
	
	// This is how many images are in the full render spin
	// NOTE: This needs to be consistent with the number of images in the 
	// rolling animation; Keep it an odd number
	public static final int NUMBER_OF_FRAMES_IN_FULL_ROLLING_ANIMATION = 33;
	// For better performance use fewer frames in the rolling animations
	public static final int NUMBER_OF_FRAMES_FOR_ROLLING_ANIMATION = 17;
	
	// These were just found by guestimating and seem to work okay
	public static final int NUMBER_OF_FRAMES_FOR_DROPPED_MARBLES_TO_CHANGE_CELLS = (int) Math.floor(NUMBER_OF_FRAMES_FOR_ROLLING_ANIMATION/3);
	public static final int NUMBER_OF_FRAMES_FOR_DROPPED_MARBLE_ANIMATION = 9;
	public static final int NUMBER_OF_FRAMES_FOR_ROLLING_MARBLE_ANIMATION = 15;
	
	// Graphics paths
	///////////////////////////////////////////////////////////////////////////
	public static final String PATH_TO_ASSETS_DIR = "data";
	public static final String PATH_TO_IMAGES_DIR = PATH_TO_ASSETS_DIR + "/images";
	
	// Background assets
	public static final int BACKGROUND_IMAGE_WIDTH = 960;
	public static final int BACKGROUND_IMAGE_HEIGHT = 1080;
	
	public static final String BACKGROUND_IMAGES_DIR = "backgrounds";
	public static final String[] BACKGROUND_IMAGES = {PATH_TO_IMAGES_DIR + "/" + BACKGROUND_IMAGES_DIR + "/background-000.png",
											   		  PATH_TO_IMAGES_DIR + "/" + BACKGROUND_IMAGES_DIR + "/background-001.png",
											   		  PATH_TO_IMAGES_DIR + "/" + BACKGROUND_IMAGES_DIR + "/background-002.png",
											   		  PATH_TO_IMAGES_DIR + "/" + BACKGROUND_IMAGES_DIR + "/background-003.png",
											   		  PATH_TO_IMAGES_DIR + "/" + BACKGROUND_IMAGES_DIR + "/background-004.png"};
	
	public static final String PATH_TO_BACKGROUND_MARBLE_IMAGE = PATH_TO_IMAGES_DIR + "/background_marble-000.png";
	
	// Game board assets
	public static final String GAME_BOARD_HORIZONTAL_LINE = PATH_TO_IMAGES_DIR + "/horizontal_line-000.png";
	public static final String GAME_BOARD_VERTICLE_LINE = PATH_TO_IMAGES_DIR + "/verticle_line-000.png";
	public static final String PATH_TO_RING_IMAGE = PATH_TO_IMAGES_DIR + "/ring-000.png";
	
	// Marble assets
	public static final String RED_MARBLE_DIR = PATH_TO_IMAGES_DIR + "/red_marble";
	public static final String BLUE_MARBLE_DIR = PATH_TO_IMAGES_DIR + "/blue_marble";
	public static final String GREEN_MARBLE_DIR = PATH_TO_IMAGES_DIR + "/green_marble";
	public static final String YELLOW_MARBLE_DIR = PATH_TO_IMAGES_DIR + "/yellow_marble";
	public static final String BLACK_MARBLE_DIR = PATH_TO_IMAGES_DIR + "/black_marble";
	public static final String PURPLE_MARBLE_DIR = PATH_TO_IMAGES_DIR + "/purple_marble";
	public static final String MARBLE_IMAGES_EXT = ".png";
	
	public static final String RED_MARBLE_IMAGE_0000 = RED_MARBLE_DIR + "/0000.png";
	public static final String BLUE_MARBLE_IMAGE_0000 = BLUE_MARBLE_DIR + "/0000.png";
	public static final String GREEN_MARBLE_IMAGE_0000 = GREEN_MARBLE_DIR + "/0000.png";
	public static final String YELLOW_MARBLE_IMAGE_0000 = YELLOW_MARBLE_DIR + "/0000.png";
	public static final String BLACK_MARBLE_IMAGE_0000 = BLACK_MARBLE_DIR + "/0000.png";
	public static final String PURPLE_MARBLE_IMAGE_0000 = PURPLE_MARBLE_DIR + "/0000.png";
	
	public static final String PATH_TO_TITLE_MARBLE1_IMAGE = RED_MARBLE_DIR + "/0000.png";
	public static final String PATH_TO_TITLE_MARBLE2_IMAGE = BLUE_MARBLE_DIR + "/0000.png";
	
	// Status overlays
	public static final String PATH_TO_GAME_OVER_OVERLAY = PATH_TO_IMAGES_DIR + "/game_over_overlay-000.png";
	public static final String PATH_TO_WIN_OVERLAY = PATH_TO_IMAGES_DIR + "/you_win_overlay-000.png";
	public static final String PATH_TO_PAUSED_OVERLAY = PATH_TO_IMAGES_DIR + "/paused_overlay-000.png";
	public static final String PATH_TO_SCORE_OVERLAY = PATH_TO_IMAGES_DIR + "/score_overlay-000.png";
	public static final String PATH_TO_TIME_OVERLAY = PATH_TO_IMAGES_DIR + "/time_overlay-000.png";
	
	public static final String DIGITS_DIR = PATH_TO_IMAGES_DIR + "/digits";
	public static final String PATH_TO_ZERO_IMAGE = DIGITS_DIR + "/digit_zero-000.png";
	public static final String PATH_TO_ONE_IMAGE = DIGITS_DIR + "/digit_one-000.png";
	public static final String PATH_TO_TWO_IMAGE = DIGITS_DIR + "/digit_two-000.png";
	public static final String PATH_TO_THREE_IMAGE = DIGITS_DIR + "/digit_three-000.png";
	public static final String PATH_TO_FOUR_IMAGE = DIGITS_DIR + "/digit_four-000.png";
	public static final String PATH_TO_FIVE_IMAGE = DIGITS_DIR + "/digit_five-000.png";
	public static final String PATH_TO_SIX_IMAGE = DIGITS_DIR + "/digit_six-000.png";
	public static final String PATH_TO_SEVEN_IMAGE = DIGITS_DIR + "/digit_seven-000.png";
	public static final String PATH_TO_EIGHT_IMAGE = DIGITS_DIR + "/digit_eight-000.png";
	public static final String PATH_TO_NINE_IMAGE = DIGITS_DIR + "/digit_nine-000.png";
	public static final String PATH_TO_COLON_IMAGE = DIGITS_DIR + "/digit_colon-000.png";
	
	// Game board buttons
	public static final String PATH_TO_MAIN_MENU_BUTTON = PATH_TO_IMAGES_DIR + "/main_menu_text-000.png";
	public static final String PATH_TO_PAUSE_BUTTON = PATH_TO_IMAGES_DIR + "/pause_text-000.png";
	
	// Instructions
	public static final String PATH_TO_GOAL_GRAPHIC = PATH_TO_IMAGES_DIR + "/instructions_overlay-000.png";
	public static final String PATH_TO_GAMEPLAY0_GRAPHIC = PATH_TO_IMAGES_DIR + "/instructions_overlay-001.png";
	public static final String PATH_TO_GAMEPLAY1_GRAPHIC = PATH_TO_IMAGES_DIR + "/instructions_overlay-002.png";
	public static final String PATH_TO_GAMEPLAY2_GRAPHIC = PATH_TO_IMAGES_DIR + "/instructions_overlay-003.png";
	public static final String PATH_TO_GAMEPLAY3_GRAPHIC = PATH_TO_IMAGES_DIR + "/instructions_overlay-004.png";
	public static final String PATH_TO_ANDROID_CONTROLS_GRAPHIC = PATH_TO_IMAGES_DIR + "/instructions_overlay-005.png";
	public static final String PATH_TO_DESKTOP_CONTROLS_GRAPHIC = PATH_TO_IMAGES_DIR + "/instructions_overlay-006.png";
	public static final String PATH_TO_NEXT_BUTTON = PATH_TO_IMAGES_DIR + "/next_text-000.png";
	
	// Main menu
	public static final String PATH_TO_TITLE = PATH_TO_IMAGES_DIR + "/title_text-000.png";
	public static final String PATH_TO_EASY_BUTTON = PATH_TO_IMAGES_DIR + "/easy_text-000.png";
	public static final String PATH_TO_HARD_BUTTON = PATH_TO_IMAGES_DIR + "/hard_text-000.png";
	public static final String PATH_TO_INSTRUCTIONS_BUTTON = PATH_TO_IMAGES_DIR + "/instructions_text-000.png";
	public static final String PATH_TO_QUIT_BUTTON = PATH_TO_IMAGES_DIR + "/quit_text-000.png";
	public static final String PATH_TO_MUTE_BUTTON = PATH_TO_IMAGES_DIR + "/mute_text-000.png";
	public static final String PATH_TO_EMPTY_CHECKBOX_GRAPHIC = PATH_TO_IMAGES_DIR + "/check_box_empty-000.png";
	public static final String PATH_TO_FILLED_CHECKBOX_GRAPHIC = PATH_TO_IMAGES_DIR + "/check_box_filled-000.png";
	public static final String PATH_TO_BLACK_BOX = PATH_TO_IMAGES_DIR + "/black_box-000.png";
	public static final String PATH_TO_WHITE_BOX = PATH_TO_IMAGES_DIR + "/white_box-000.png";
	
	// Music
	public static final String MUSIC_DIR = PATH_TO_ASSETS_DIR + "/music";
	public static final String PATH_TO_MAIN_MENU_MUSIC = MUSIC_DIR + "/track_menu-000.mp3";
	public static final String PATH_TO_TRACK0_MUSIC = MUSIC_DIR + "/track_background-000.mp3";
	public static final String PATH_TO_TRACK1_MUSIC = MUSIC_DIR + "/track_background-001.mp3";
	public static final String PATH_TO_TRACK_GAME_OVER_MUSIC = MUSIC_DIR + "/track_game_over-000.mp3";
	public static final String PATH_TO_TRACK_GAME_WIN_MUSIC = MUSIC_DIR + "/track_game_win-000.mp3";
	
	// Sound
	public static final String SOUNDS_DIR = PATH_TO_ASSETS_DIR + "/sounds";
	public static final String PATH_TO_ROLL0_SOUND = SOUNDS_DIR + "/roll-000.mp3";
	public static final String PATH_TO_CLICK0_SOUND = SOUNDS_DIR + "/click-000.mp3";
	public static final String PATH_TO_DESPAWN0_SOUND = SOUNDS_DIR + "/despawn-000.mp3";
}
