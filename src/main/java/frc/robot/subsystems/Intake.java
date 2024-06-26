package frc.robot.subsystems;

import com.revrobotics.CANSparkBase;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.Items.SparkMax.SparkController;
import frc.lib.configs.Sparkmax.SparkControllerInfo;
import frc.robot.Constants;

public class Intake extends SubsystemBase {

    private SparkController topRoller;
    private SparkController bottomRoller;
    
    private RelativeEncoder topEncoder;
    private RelativeEncoder bottomEncoder; //TODO - Use this somehow since java is being weird 

    private SparkPIDController topController;
    private SparkPIDController bottomController;

    public Intake(){

        this.topRoller = new SparkController(Constants.Setup.topRoller, new SparkControllerInfo().intake());
        this.bottomRoller = new SparkController(Constants.Setup.bottomRoller, new SparkControllerInfo().intake());
        
        this.topEncoder = topRoller.sparkEncode;
        this.bottomEncoder = bottomRoller.sparkEncode;

        this.topController = topRoller.sparkControl;
        this.bottomController = bottomRoller.sparkControl;
    }

    @Override
    public void periodic(){
        SmartDashboard.putNumber("RollerVelocity", topEncoder.getVelocity());
        SmartDashboard.putNumber("Using bottom Enconder", bottomEncoder.getVelocity());
    }

    public void setVoltage(double voltage){
        if(voltage < -Constants.Intake.maxVoltage){
            voltage = -Constants.Intake.maxVoltage;
        } else if (voltage > Constants.Intake.maxVoltage){
            voltage = Constants.Intake.maxVoltage;
        }
        topController.setReference(voltage, CANSparkBase.ControlType.kVoltage, 0);
        bottomController.setReference(voltage, CANSparkBase.ControlType.kVoltage, 0);
    }

    public void setDutyCylce(double percent){
        percent = percent/100;
        topController.setReference(percent, CANSparkBase.ControlType.kDutyCycle);
        bottomController.setReference(percent, CANSparkBase.ControlType.kDutyCycle);
    }

    
}