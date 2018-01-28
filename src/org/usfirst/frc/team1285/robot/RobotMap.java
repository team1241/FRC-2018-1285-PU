package org.usfirst.frc.team1285.robot;
/**
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	//**************************************************************************
	//*****************************DRIVE MOTORS*********************************
	//**************************************************************************        
	
	public static final int RIGHT_DRIVE_FRONT                               = 2; 
	public static final int RIGHT_DRIVE_MIDDLE 								= 6;
	public static final int RIGHT_DRIVE_BACK                                = 3;
	
	public static final int LEFT_DRIVE_FRONT                                = 1;
	public static final int LEFT_DRIVE_MIDDLE                               = 5;
	public static final int LEFT_DRIVE_BACK                                 = 4;

	//**************************************************************************
	//******************************* INTAKE ***********************************
	//**************************************************************************        
		
	public static final int RIGHT_INTAKE                                    = 7;
	public static final int LEFT_INTAKE                                     = 8;
	
	//**************************************************************************
	//******************************* Elevator ***********************************
	//**************************************************************************        
		
	public static final int RIGHT_ELEVATOR                                   = 5; 
	public static final int LEFT_ELEVATOR                                    = 6;
	
	public static final int LEFT_BUMPER_SWITCH								 = 1;
	public static final int RIGHT_BUMPER_SWITCH								 = 2;
	
	//**************************************************************************
	//*************************** PNEUMATICS ***********************************
	//**************************************************************************
	//Clamps
	public static final int CLAMP_SOLENOID_A								= 1;
	public static final int CLAMP_SOLENOID_B							 	= 0;
	//Pivot
	public static final int PIVOT_SOLENOID_A							 	= 2;
	public static final int PIVOT_SOLENOID_B							 	= 3;
	
	//**************************************************************************
    //********************* DRIVE ENCODER CONSTANTS ****************************
	//**************************************************************************
	public static final int driveWheelRadius = 2;//wheel radius in inches
	public static final double driveGearRatio = 1/1; //ratio between wheel and encoder
	public static final double DRIVE_ROTATIONS_TO_INCHES 	= (2*Math.PI*driveWheelRadius*driveGearRatio);
	
	public static final boolean leftInverted = false;
	public static final boolean rightInverted = false;
	
	//**************************************************************************
    //********************* INTAKE ENCODER CONSTANTS ***************************
	//**************************************************************************
	
	public static final boolean leftIntakeInverted = false;
	public static final boolean rightIntakeInverted = true;
	
	//**************************************************************************
    //********************* Elevator ENCODER CONSTANTS ***************************
	//**************************************************************************
	
	public static final int pulsesPerRev = 128; //pulses per revolution of arm
	public static final double elevGearRatio = 1.4; //ratio between wheel and encoder
	
	public static final double ELEV_ROTATIONS_TO_INCHES 	= (2*Math.PI*elevGearRatio);
	
	
}