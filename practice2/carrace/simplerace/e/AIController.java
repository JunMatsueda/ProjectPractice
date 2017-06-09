package simplerace.e;
import simplerace.*;

public class AIController implements Controller, Constants {

  public void reset() {};

  public int control (SensorModel inputs) {
    int command=neutral;

    if(inputs.getDistanceToNextWaypoint() > 0.05){
      command = carMove(inputs,inputs.getAngleToNextWaypoint(), inputs.getDistanceToNextWaypoint());
    }
    else {
      command = carMove(inputs,inputs.getAngleToNextNextWaypoint(), inputs.getDistanceToNextNextWaypoint());
    }

    //System.out.print(command);

    return command;
  }
  /**
  進行方向を反転する
  **/
  private int reversalCommand(int command){
    switch(command){
      case 0:
      command = forwardright;
      break;
      case 1 :
      command = forward;
      break;
      case 2 :
      command = forwardleft;
      break;
      case 3:
      command = left;
      break;
      case 4 :
      command = neutral;
      break;
      case 5 :
      command = right;
      break;
      case 6 :
      command = backwardright;
      break;
      case 7 :
      command = backward;
      break;
      case 8 :
      command = backwardleft;
      break;
    }
    return command;
  }
  /**
  目標が前方にあるかを判定する。
  **/
  private boolean fowardToNextPoint(double angle){
    return (angle > -Math.PI/12 && angle<=Math.PI/12);
  }
  /**
  目標が後方にあるかを判定する。
  **/
  private boolean backwardToNextPoint(double angle){
    return ((angle > Math.PI*11/12 && angle < Math.PI)|| (angle <= -Math.PI*11/12 && angle > -Math.PI));
  }

  private int carMove(SensorModel inputs,double angle, double distance){
    int command= neutral;

    if(angle > 0 && angle <=Math.PI/2) {
      command=forwardleft;
    }
    else if(angle < 0&&angle >= -Math.PI/2){
      command=forwardright;
    }
    else if(angle < -Math.PI/2&&angle > -Math.PI){
      command=backwardright;
    }
    else if(angle > Math.PI/2&&angle < Math.PI){
      command=backwardleft;
    }
    else if(angle == 0){
      command = forward;
    }
    else{
      command = backward;
    }


    if(inputs.getSpeed()>7){ //速度の値が大きい時

      if(angle > 0&& angle <= Math.PI/2.0) {
        command=left;
      }
      else if(angle< 0&&angle >= -Math.PI/2.0){
        command=right;
      }
      else if(angle < -Math.PI/2.0&&angle > -Math.PI){
        command=backwardleft;
      }
      else if(angle > Math.PI/2.0&&angle < Math.PI){
        command=backwardright;
      }
      else {
        command=neutral;
      }
      if(inputs.getDistanceToNextWaypoint() < 0.05){
        command = reversalCommand(command);
      }

    }
    if(inputs.getSpeed()< -7){ //速度の値が小さい時
      if(angle > 0&& angle <= Math.PI/2.0) {
        command=forwardleft;
      }
      else if(angle< 0&&angle >= -Math.PI/2.0){
        command=forwardright;
      }
      else if(angle < -Math.PI/2.0&&angle > -Math.PI){
        command=left;
      }
      else if(angle > Math.PI/2.0&&angle < Math.PI){
        command=right;
      }
      else {
        command=neutral;
      }
      if(inputs.getDistanceToNextWaypoint() < 0.05){
        command = reversalCommand(command);
      }
    }

    if(distance< 0.08 ){
      if(inputs.getSpeed() > 2 || inputs.getSpeed() < -2){
        if(fowardToNextPoint(inputs.getAngleToNextWaypoint())) {
          command=forward;
        }
        else if(backwardToNextPoint(inputs.getAngleToNextWaypoint())){
          command=backward;
        }
        else if(inputs.getSpeed() < 0){
          command = forward;
        }else {
          command = backward;
        }

      }
    }

    return command;
  }

  private int brake(double speed){
    int command= neutral;
    if(speed >= 0){
      command = backward;
    }
    else {
      command = forward;
    }
    return command;
  }
}
