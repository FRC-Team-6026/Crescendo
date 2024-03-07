// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
/** Add your docs here. */
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Pivot;

public class SetPivotCommand extends Command{
    private Pivot s_Pivot;
    private Double targetAngle;
    private DoubleSupplier JoystickInput;

    public SetPivotCommand(Pivot s_Pivot, Double targetAngle, DoubleSupplier JoystickInput) {
        this.s_Pivot = s_Pivot;
        addRequirements(s_Pivot);
        
        this.targetAngle = targetAngle;
        this.JoystickInput = JoystickInput; // Strictly for interrupting
    }
    public SetPivotCommand(Pivot s_Pivot, int targetAngle, DoubleSupplier JoystickInput) {
        this(s_Pivot, (double)targetAngle, JoystickInput);
    }

    @Override
    public void initialize() {
        s_Pivot.isTrackingAngle = true;
    }

    @Override
    public void execute() {
        double attemptVoltage = s_Pivot.pivotPID.calculate(s_Pivot.PivotEncoder.getPosition(), targetAngle);
        s_Pivot.PivotMotor.spark.setVoltage(MathUtil.clamp(attemptVoltage, -Constants.Pivot.maxVoltage, Constants.Pivot.maxVoltage)); // TODO - add feedforward afterall?  (M4 push)
    }

    @Override
    public void end(boolean interrupted) {
        s_Pivot.isTrackingAngle = false;
    }

    @Override
    public boolean isFinished() {
        return (
            Math.abs(s_Pivot.PivotEncoder.getPosition() - targetAngle) <= Constants.Pivot.angleTolerance ||
            Math.abs(JoystickInput.getAsDouble()) > .1
        );
    }
}

