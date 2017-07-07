package simplerace.e2;
import simplerace.*;
import java.util.ArrayList;
import java.util.Objects;

public class AIController implements Controller, Constants {

  private double maxSpeed = 0; //最高速度
  private double pi = Math.PI; //円周率
  private Vector2d lastPosition = new Vector2d(0,0); // 前回の処理の目標の座標
  private Boolean changeWayPoint; //目標の座標が変わったかを記憶
  private int notGetCount=0; //相手が側を取らない時の条件分岐のためののカウンタ
  private int closeDistanceCount=0;

  public void reset() {};

  public int control (SensorModel inputs) {
    int command=neutral;

  // 相手がいるかどうかで戦略を変更
    if(!inputs.otherVehicleIsPresent()){
      changeMaxSpeed(inputs.getDistanceToNextWaypoint(),inputs.getAngleToNextWaypoint());
      command = carStrategy(inputs,inputs.getAngleToNextWaypoint(), inputs.getDistanceToNextWaypoint());
    }else{
      command = VSCarStrategy(inputs);
    }

    return command;
  }
  /**
  目標が前方にあるかを判定する。
  @param angle 目標との角度
  @return -π/4 < angle <= π/4 の時、真を返す.
  **/
  private boolean forwardToNextPoint(double angle){
    return (angle > -pi/4 && angle<=pi/4);
  }

  /**
  目標がほぼ正面にあるかを判定する。
  @param angle 目標との角度
  @return -π/12 < angle <= π/12の時、真を返す.
  **/
  private boolean isAhead(double angle){
    return (angle > -pi/12 && angle<=pi/12);
  }

  /**
  目標が後方にあるかを判定する。
  @param angle 目標との角度
  @return angle > 2π/3 または angle < -2π/3 の時、真を返す.
  **/
  private boolean backwardToNextPoint(double angle){
    return ((angle > pi*2/3 && angle < pi)|| (angle <= -pi*2/3 && angle > -pi));
  }

  /**
  コマンドを決定するための判定をする
  @param inputs センサーモデルの情報一式
  @param angle 目標との角度
  @param distance 目標との距離
  @return コマンド(0~9の整数値)を返す
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
    }else if(backwardToNextPoint(angle) && (distance <0.3 || inputs.getSpeed() < 1.3 )){
      if(angle < 0){
        command = backwardright;
      }else if(angle > 0){
        command = backwardleft;
      }else {
        command = backward;
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
  //以下、目標との距離が近い時の処理
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
      if(forwardToNextPoint(inputs.getAngleToNextNextWaypoint()) && forwardToNextPoint(inputs.getAngleToNextWaypoint())){
        command = forward;
      }
    }
    if(Objects.equals(inputs.getDistanceToNextNextWaypoint(), distance)){
      if(Math.abs(inputs.getSpeed()) < 1){
        if(distance < 0.04 ){
          command = brake(inputs.getSpeed());
        }
      }else {
        if(distance < 0.1){
          command = brake(inputs.getSpeed());
        }
      }
    }
    return command;
  }
  /**
    目標との距離によって最高速度を変更する。
    @distance 目標との距離
    @angle 目標に対する角度
  **/
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

  /**
  相手がいる場合の戦略を決定する。
  @param inputs センサー情報
  @return コマンド(0~9の整数値)を返す
  **/
  private int VSCarStrategy(SensorModel inputs){
    double distanceToNextPoint = inputs.getDistanceToNextWaypoint();
    double distanceToNextNextPoint = inputs.getDistanceToNextNextWaypoint();
    double angleToNextPoint = inputs.getAngleToNextWaypoint();
    double angleToNextNextPoint = inputs.getAngleToNextNextWaypoint();
    double distanceToOtheVehicle = inputs.getDistanceToOtherVehicle();
    double distanceFromOtherVehicle = getDinstance(inputs.getNextWaypointPosition(), inputs.getOtherVehiclePosition());
    double angleFromOtherVehicle = getAngle(getVector(inputs.getOtherVehiclePosition(), inputs.getNextWaypointPosition()),
    inputs.getOtherVehicleVelocity());
    double speedOfOtherVehucle = Math.hypot(inputs.getOtherVehicleVelocity().x, inputs.getOtherVehicleVelocity().y);
    double averageMySpeed = (9+6+4+2.3)/4;
    int command=neutral;

    changeWayPoint = changePosition(inputs.getNextWaypointPosition(), lastPosition);

    if(changeWayPoint){
      if(distanceFromOtherVehicle < distanceToNextPoint){
        notGetCount++;
      }
    }else{
      notGetCount = 0;
      lastPosition = inputs.getNextWaypointPosition();
    }

    if(distanceToOtheVehicle < 0.05){
      closeDistanceCount++;
    }else{
      closeDistanceCount = 0;
    }

     if(closeDistanceCount > 30){
      command = neutral;
      return command;
    }


    if(distanceToNextPoint < distanceFromOtherVehicle|| notGetCount > 100 ||(isAhead(angleToNextPoint)&&!forwardToNextPoint(angleFromOtherVehicle)&& distanceToNextPoint/averageMySpeed < distanceFromOtherVehicle/speedOfOtherVehucle)){
      changeMaxSpeed(distanceToNextPoint,angleToNextPoint);
      command = carStrategy(inputs, angleToNextPoint, distanceToNextPoint);
    }
    else{
      changeMaxSpeed(distanceToNextNextPoint,angleToNextNextPoint);
      command = carStrategy(inputs, angleToNextNextPoint, distanceToNextNextPoint);
    }

    return command;
  }

  /**
    ２点間の距離を計算する。
    @param firstPoint 座標１
    @param secondPoint　座標２
    @return ２点間の距離
  **/
  private double getDinstance(Vector2d firstPoint, Vector2d secondPoint){
    double delx = firstPoint.x - secondPoint.x;
    double dely = firstPoint.y - secondPoint.y;

    return Math.abs(Math.hypot(delx, dely))/(400*Math.sqrt(2.0));
  }

  /**
    ブレーキ操作をする
    @param speed 現在の速度
    @return コマンド(1 || 5 || 7)を返す
  **/
  private int brake (double speed){
    int command;
    if(speed > 0.3){
      command = backward;
    }else if(speed < -0.3){
      command = forward;
    }else {
      command = neutral;
    }

    return command;
  }

  /**
    現在の目標の座標と前回の目標の座標が同値かを調べる。
    @param currentPosition 現在の座標
    @param lastPosition 過去の座標
    @return 目標の座標が変わっている時、真を返す
  **/
  private Boolean changePosition(Vector2d currentPosition, Vector2d lastPosition){
    return Objects.equals(currentPosition.x, lastPosition.x) && Objects.equals(currentPosition.y, lastPosition.y);
  }

  /**
  2つの座標から角度を算出する.
  @param first 座標１
  @param second 座標２
  @return 自身からの２点のなす角度
  **/
  public static double getAngle(Vector2d first, Vector2d second){
    double innerProduct = first.x*second.x + first.y*second.y;

    return Math.acos(innerProduct/(Math.hypot(first.x,first.y)*Math.hypot(second.x, second.y)));
  }

  /**
    基準となる座標と目的の座標からベクトルを算出する.
    @param standardVector 基準の座標
    @param targetVector 目的の座標
    @return 基準座標からみた目的座標へのベクトル
  **/
  public static Vector2d getVector(Vector2d standardVector, Vector2d targetVector){
    Vector2d vector = new Vector2d(0,0);
    vector.x = targetVector.x - standardVector.x;
    vector.y = targetVector.y - standardVector.y;
    return vector;
  }

}
