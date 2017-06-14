package simplerace.e;
import simplerace.*;

public class AIController implements Controller, Constants {

  double maxSpeed = 0;
  double pi = Math.PI;

  public void reset() {};

  public int control (SensorModel inputs) {
    int command=neutral;
    if(inputs.getDistanceToNextWaypoint() >0.4){
      maxSpeed = 9;
    }else if(inputs.getDistanceToNextWaypoint() >0.2){
      maxSpeed = 6;
    }else if(isAhead(inputs.getAngleToNextWaypoint())){
      maxSpeed=4;
    }else{
      maxSpeed = 2.3;
    }


    command = carMove(inputs,inputs.getAngleToNextWaypoint(), inputs.getDistanceToNextWaypoint());

    //System.out.print(command);

    return command;
  }
  /**
  目標が前方にあるかを判定する。
  **/
  private boolean forwardToNextPoint(double angle){
    return (angle > -pi/4 && angle<=pi/4);
  }

  private boolean isAhead(double angle){
    return (angle > -pi/12 && angle<=pi/12);
  }
  /**
  目標が後方にあるかを判定する。
  **/
  private boolean backwardToNextPoint(double angle){
    return ((angle > pi*3/4 && angle < pi)|| (angle <= -pi*3/4 && angle > -pi));
  }

  private int carMove(SensorModel inputs,double angle, double distance){
    int command= neutral;

    if(forwardToNextPoint(angle)){
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
      }else {
        command = forward;
      }

    }else if(backwardToNextPoint(angle) && (distance <0.3 || inputs.getSpeed() <1)){
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

    return command;
  }
}
