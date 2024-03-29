package org.usfirst.frc.team1285.robot.commands.auto;

import org.usfirst.frc.team1285.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class OpenIntakeClamp extends Command {
	private boolean open;
    public OpenIntakeClamp(boolean open) {
    	this.open = open;
//    	requires(Robot.intake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	if(open){
    		Robot.intake.openClamp();
    	}
    	else{
    		Robot.intake.closeClamp();
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
