package simplerace.e;
import simplerace.*;
import java.util.ArrayList;
import java.util.Objects;

public class AIController implements Controller, Constants {

  private double maxSpeed = 0;
  private double pi = Math.PI;
  private ArrayList<Boolean> isGetPoint = new ArrayList<Boolean>();
  private Vector2d lastPoint = new Vector2d(0,0);
  private Boolean changePoint;

  public void reset() {};

  public int control (SensorModel inputs) {
    int command=neutral;


    if(!inputs.otherVehicleIsPresent()){
      changeMaxSpeed(inputs.getDistanceToNextWaypoint(),inputs.getAngleToNextWaypoint());
      command = carStrategy(inputs,inputs.getAngleToNextWaypoint(), inputs.getDistanceToNextWaypoint());
    }else{
      command = VSCarStrategy(inputs);
    }

    //System.out.print(command);
    return command;
  }
  /**
  目標が前方にあるかを判定する。
  @angle 目標との角度
  **/
  private boolean forwardToNextPoint(double angle){
    return (angle > -pi/4 && angle<=pi/4);
  }
  /**
  目標がほぼ正面にあるかを判定する。
  @angle 目標との角度
  **/
  private boolean isAhead(double angle){
    return (angle > -pi/12 && angle<=pi/12);
  }
  /**
  目標が後方にあるかを判定する。
  @angle 目標との角度
  **/
  private boolean backwardToNextPoint(double angle){
    return ((angle > pi*3/4 && angle < pi)|| (angle <= -pi*3/4 && angle > -pi));
  }

  /**
  コマンドを決定するための判定をする
  @inputs センサーモデルの情報一式
  @angle 目標との角度
  @distance 目標との距離
  **/
  private int carStrategy(SensorModel inputs,double angle, double distance){
    int command= neutral;

    if(forwardToNextPoint(angle)){
      if(isAhead(angle)){
        command = forward;
      }
      if(angle<0){
        if(inputs.getSpeed() < maxSpeed){
          command = forwardright;
        }else{
          command = right;
        }
      }else if(angle >0){
        if(inputs.getSpeed() < maxSpeed){
          command = forwardleft;
        }else{
          command = left;
        }
      }

    }else if(backwardToNextPoint(angle) && (distance <0.3 || inputs.getSpeed() <1)){
      //if(backwardToNextPoint(angle) && distance <0.3 ){
      if(angle < 0){
        command = backwardright;
      }else{
        command = backwardleft;
      }
    }else if(angle <0){
      if(inputs.getSpeed() < 2){
        command = forwardright;
      }else{
        command = backwardright;
      }
    }else{
      if(inputs.getSpeed() < 2){
        command = forwardleft;
      }else{
        command = backwardleft;
      }
    }

    if(distance<0.08){
      if(isAhead(angle)){
        if(isAhead(inputs.getAngleToNextNextWaypoint())){
          command = forward;
        }
      }
    }
    if(distance < 0.04){
      if(!isAhead(angle)){
        if(angle <0){
          command = backwardleft;
        }else{
          command = backwardright;
        }
      }
    }
    if(distance < 0.05){
      if(inputs.getSpeed() > 1){
        if(inputs.getAngleToNextNextWaypoint()<0){
          command = right;
        }else{
          command = left;
        }
      }
      if(forwardToNextPoint(inputs.getAngleToNextNextWaypoint())){
        command = forward;
      }
    }
    if(Objects.equals(inputs.getDistanceToNextNextWaypoint(), distance)){
      if(distance < 0.05 && Math.abs(inputs.getSpeed())>1){
        command = brake(inputs.getSpeed());
      }
    }
    return command;
  }
  private void changeMaxSpeed(double distance, double angle){
    if( distance > 0.4){
      maxSpeed = 9;
    }else if(distance > 0.2){
      maxSpeed = 6;
    }else if(isAhead(angle)){
      maxSpeed=4;
    }else{
      maxSpeed = 2.3;
    }
    return ;
  }

  private int VSCarStrategy(SensorModel inputs){
    double distanceToNextPoint = inputs.getDistanceToNextWaypoint();
    double distanceToNextNextPoint = inputs.getDistanceToNextNextWaypoint();
    double angleToNextPoint = inputs.getAngleToNextWaypoint();
    double angleToNextNextPoint = inputs.getAngleToNextNextWaypoint();
    double distanceToOtheVehicle = inputs.getDistanceToOtherVehicle();
    double distanceFromOtherVehicle = getDinstance(inputs.getNextWaypointPosition(), inputs.getOtherVehiclePosition());
    double angleFromOtherVehicle = getAngle(getVector(inputs.getOtherVehiclePosition(), inputs.getNextWaypointPosition()),
                                            inputs.getOtherVehicleVelocity());
    int command=neutral;

    changePoint = changePosition(inputs.getNextWaypointPosition(), lastPoint);

    if(changePoint){
      if(distanceToNextPoint > distanceFromOtherVehicle){
        isGetPoint.add(changePoint);
      }
    }else{
      isGetPoint.clear();
      lastPoint = inputs.getNextWaypointPosition();
    }


    if(distanceToNextPoint < distanceFromOtherVehicle || isGetPoint.size() > 100){
      changeMaxSpeed(distanceToNextPoint,angleToNextPoint);
      command = carStrategy(inputs, angleToNextPoint, distanceToNextPoint);
    }
    else{
      changeMaxSpeed(distanceToNextNextPoint,angleToNextNextPoint);
      command = carStrategy(inputs, angleToNextNextPoint, distanceToNextNextPoint);
    }

    return command;
  }
  private double getDinstance(Vector2d firstPoint, Vector2d secondPoint){
    double delx = firstPoint.x - secondPoint.x;
    double dely = firstPoint.y - secondPoint.y;

    return Math.abs(Math.hypot(delx, dely))/(400*Math.sqrt(2.0));
  }

  private int brake (double speed){
    int command;
    if(speed > 0.1){
      command = backward;
    }else if(speed < -0.1){
      command = forward;
    }else {
      command = neutral;
    }

    return command;
  }

  private Boolean changePosition(Vector2d currentPosition, Vector2d lastPosition){
    return Objects.equals(currentPosition.x, lastPosition.x) && Objects.equals(currentPosition.y, lastPosition.y);
  }

  private double getAngle(Vector2d first, Vector2d second){
    double innerProduct = first.x*second.x + first.y*second.y;

    return Math.acos(innerProduct/(Math.hypot(first.x,first.y)*Math.hypot(second.x, second.y)));

  }

  private Vector2d getVector(Vector2d standardVector, Vector2d targetVector){
    Vector2d vector = new Vector2d(0,0);
    vector.x = targetVector.x - standardVector.x;
    vector.y = targetVector.y - standardVector.y;
    return vector;
  }

}
