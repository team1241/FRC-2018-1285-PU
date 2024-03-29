package org.usfirst.frc.team1285.robot.subsystems;

import org.usfirst.frc.team1285.robot.NumberConstants;
import org.usfirst.frc.team1285.robot.RobotMap;
import org.usfirst.frc.team1285.robot.commands.TankDrive;
import org.usfirst.frc.team1285.robot.utilities.PIDController;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * @author Julia Bui
 */
public class Drivetrain extends Subsystem {

	private WPI_VictorSPX leftBackFollower;
	private WPI_VictorSPX leftMiddleFollower;
	private WPI_TalonSRX leftMaster;

	private WPI_VictorSPX rightBackFollower;
	private WPI_VictorSPX rightMiddleFollower;
	private WPI_TalonSRX rightMaster;

	//	AHRS gyro;
	ADXRS450_Gyro gyro;

	public PIDController drivePID;
	public PIDController gyroPID;

	public Drivetrain() {

		try {
			/***********************************************************************
			 * navX-MXP: - Communication via RoboRIO MXP (SPI, I2C, TTL UART)
			 * and USB. - See
			 * http://navx-mxp.kauailabs.com/guidance/selecting-an-interface.
			 * 
			 * navX-Micro: - Communication via I2C (RoboRIO MXP or Onboard) and
			 * USB. - See
			 * http://navx-micro.kauailabs.com/guidance/selecting-an-interface.
			 * 
			 * Multiple navX-model devices on a single robot are supported.
			 ************************************************************************/
			//			gyro = new AHRS(SPI.Port.kMXP);
			gyro = new ADXRS450_Gyro();
		} catch (RuntimeException ex) {
			DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
		}

		// left front
		leftMaster = new WPI_TalonSRX(RobotMap.LEFT_DRIVE_FRONT);
		leftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		leftMaster.setInverted(RobotMap.leftInverted);

		// left middle
		leftMiddleFollower = new WPI_VictorSPX(RobotMap.LEFT_DRIVE_MIDDLE);
		leftMiddleFollower.setInverted(RobotMap.leftInverted);
		leftMiddleFollower.follow(leftMaster);

		// left back
		leftBackFollower = new WPI_VictorSPX(RobotMap.LEFT_DRIVE_BACK);
		leftBackFollower.setInverted(RobotMap.leftInverted);
		leftBackFollower.follow(leftMaster);

		// right back
		rightMaster = new WPI_TalonSRX(RobotMap.RIGHT_DRIVE_FRONT);
		rightMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		rightMaster.setInverted(RobotMap.rightInverted);

		// right middle
		rightMiddleFollower = new WPI_VictorSPX(RobotMap.RIGHT_DRIVE_MIDDLE);
		rightMiddleFollower.setInverted(RobotMap.rightInverted);
		rightMiddleFollower.follow(rightMaster);


		// right middle
		rightBackFollower = new WPI_VictorSPX(RobotMap.RIGHT_DRIVE_BACK);
		rightBackFollower.setInverted(RobotMap.rightInverted);
		rightBackFollower.follow(rightMaster);

		drivePID = new PIDController(NumberConstants.pDrive, NumberConstants.iDrive, NumberConstants.dDrive);
		gyroPID = new PIDController(NumberConstants.pGyro, NumberConstants.iGyro, NumberConstants.dGyro);

		leftMaster.config_kP(0, NumberConstants.pDrive, 0);
		leftMaster.config_kI(0, NumberConstants.iDrive, 0);
		leftMaster.config_kD(0, NumberConstants.dDrive, 0);

		rightMaster.config_kP(0, NumberConstants.pDrive, 0);
		rightMaster.config_kI(0, NumberConstants.iDrive, 0);
		rightMaster.config_kD(0, NumberConstants.dDrive, 0);

		leftMaster.setNeutralMode(NeutralMode.Coast);
		rightMaster.setNeutralMode(NeutralMode.Coast);

		gyro.calibrate();
		this.reset();
	}

	public void initDefaultCommand() {
		setDefaultCommand(new TankDrive());
	}

	public void updatePIDs() {
		drivePID.changePIDGains(NumberConstants.pDrive, NumberConstants.iDrive, NumberConstants.dDrive);
		gyroPID.changePIDGains(NumberConstants.pGyro, NumberConstants.iGyro, NumberConstants.dGyro);
	}

	public void runLeftDrive(double pwmVal) {
		leftMaster.set(ControlMode.PercentOutput, pwmVal);
	}

	public void runRightDrive(double pwmVal) {
		rightMaster.set(ControlMode.PercentOutput, pwmVal);
	}

	public void turnDrive(double setAngle, double speed) {
		turnDrive(setAngle, speed, 1);
	}

	public void turnDrive(double setAngle, double speed, double tolerance) {
		double angle = gyroPID.calcPID(setAngle, getYaw(), 1);
		double min = 0.05;
		if(Math.abs(setAngle-getYaw()) < tolerance){ 
			runLeftDrive(0); 
			runRightDrive(0);
		}
		else if(angle > -min && angle < 0){
			runLeftDrive(-min);
			runRightDrive(min);
		} 
		else if(angle < min && angle > 0){ 
			runLeftDrive(min);
			runRightDrive(min); 
		} else{ 
			runLeftDrive(-angle * speed); //-removed at
			runRightDrive(angle * speed); 
		}
	}

	public void driveSetpoint(double setPoint, double speed, double setAngle) {
		driveSetpoint(setPoint, speed, setAngle, 1);
	}

	public void driveSetpoint(double setPoint, double speed, double setAngle, double tolerance) {
		double output = drivePID.calcPID(setPoint, getAverageDistance(), tolerance);
		double angle = gyroPID.calcPID(setAngle, getYaw(), tolerance);
		// System.out.println("output:" + output + "|angle:" + angle + " total:"
		// +
		// ((-output - angle) * speed));
		runLeftDrive(-(output + angle) * speed);
		runRightDrive((-output + angle) * speed);
		// SmartDashboard.putNumber("output", output);
	}

	public boolean drivePIDDone() {
		return drivePID.isDone();
	}

	public void coastMode() {
		leftMaster.setNeutralMode(NeutralMode.Coast);
		leftMiddleFollower.setNeutralMode(NeutralMode.Coast);
		leftBackFollower.setNeutralMode(NeutralMode.Coast);
		rightMaster.setNeutralMode(NeutralMode.Coast);
		rightMiddleFollower.setNeutralMode(NeutralMode.Coast);
		rightBackFollower.setNeutralMode(NeutralMode.Coast);
	}

	public void brakeMode() {
		leftMaster.setNeutralMode(NeutralMode.Brake);
		leftMiddleFollower.setNeutralMode(NeutralMode.Brake);
		leftBackFollower.setNeutralMode(NeutralMode.Brake);
		rightMaster.setNeutralMode(NeutralMode.Brake);
		rightMiddleFollower.setNeutralMode(NeutralMode.Brake);
		rightBackFollower.setNeutralMode(NeutralMode.Brake);
	}

	// ************************Encoder Functions************************

	public boolean isLeftAlive() {
		return leftMaster.isAlive();
	}

	public boolean isRightAlive() {
		return rightMaster.isAlive();
	}

	public double getLeftEncoderDist() {
		return leftMaster.getSelectedSensorPosition(0) * RobotMap.DRIVE_RAW_TO_INCHES;
	}

	public double getRightEncoderDist() {
		return rightMaster.getSelectedSensorPosition(0) * RobotMap.DRIVE_RAW_TO_INCHES;
	}

	public double getLeftEncoderRaw() {
		return leftMaster.getSelectedSensorPosition(0);
	}

	public double getRightEncoderRaw() {
		return rightMaster.getSelectedSensorPosition(0);
	}

	public double getAverageDistance() {
		return (getLeftEncoderDist() + getRightEncoderDist()) / 2;
	}

	public void resetEncoders() {
		leftMaster.setSelectedSensorPosition(0, 0, 0);
		rightMaster.setSelectedSensorPosition(0, 0, 0);
	}

	/************************ GYRO FUNCTIONS ************************/

	//	public boolean gyroConnected() {
	//		return gyro.isConnected();
	//	}
	//
	//	public boolean gyroCalibrating() {
	//		return gyro.isCalibrating();
	//	}

	public double getYaw() {
		return gyro.getAngle();
	}

	//	public double getRoll() {
	//		return gyro.getRoll();
	//	}

	//	public double getPitch() {
	//		return gyro.getPitch();
	//	}

	public void resetGyro() {
		gyro.reset();
	}

	//	public double getCompassHeading() {
	//		return gyro.getCompassHeading();
	//	}

	public void resetPID() {
		drivePID.resetPID();
		gyroPID.resetPID();
	}

	public void reset() {
		resetEncoders();
		resetGyro();
	}

	public void changeDriveGains(double pDrive, double iDrive, double dDrive) {
		drivePID.changePIDGains(pDrive, iDrive, dDrive);
	}

	public void changeGyroGains(double pGyro, double iGyro, double dGyro) {
		gyroPID.changePIDGains(pGyro, iGyro, dGyro);
	}
}