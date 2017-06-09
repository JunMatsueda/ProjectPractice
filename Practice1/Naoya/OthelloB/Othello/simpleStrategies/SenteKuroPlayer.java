package simpleStrategies;

import game.GameState;
import game.Move;
import game.OthelloMoveException;
import game.Player;
import game.Strategy;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class SenteKuroPlayer extends Strategy {

	private int[][] check_state = new int[SIZE][SIZE]; //int[x][y]
	private int[][] next_state = new int[SIZE][SIZE];
	private int count =0;

	// コンストラクタは改造せずこのまま使うこと
	public SenteKuroPlayer(Player _thisPlayer, int size) {
		super(_thisPlayer, size);
	}

	//	Override
	public Move nextMove(GameState currentState, int remainingTime) {

		// 8*8マスの石の状態をcheck_state配列に格納
		check(currentState);

		// check_state配列の表示用
		// 無0　黒1　白2
		for(int i=0; i<SIZE; i++){
			for(int j=0; j<SIZE; j++){
				if(currentState.isLegal(thisPlayer, i,j)){
					check_state[i][j] = 3;
				}
			}
		}
		for(int j=0; j<SIZE; j++){
			for(int i=0; i<SIZE; i++){
				System.out.print(check_state[i][j] + " ");
			}
			System.out.println();
		}


		Move m = new Move();
		int yoko, tate;

		/*
		8*8マスの中からランダムに1箇所選んで，
		その箇所に石を置けるかどうかを
		currentState.isLegalメソッドでチェック
		置けるならそこに置く
		置けないなら，置けるところが見つかるまで繰り返す
		*/
		if(count<4){
			m=openningStrategy(currentState);
			this.count++;
		}else if(count>3 && count<25){
			m=commonStrategy(currentState);
		}else{
			m=lastStrategy(currentState);
		}

		return m;
	}


	private void check(GameState currentState) {
		for(int i=0; i<SIZE; i++){
			for(int j=0; j<SIZE; j++){
				if( (""+currentState.getState(i,j)).equals("Empty")){ check_state[i][j] = 0; }
				else if( (""+currentState.getState(i,j)).equals("Black")){ check_state[i][j] = 1; }
				else { check_state[i][j] = 2; }
			}
		}
	}
	private void checkNext(GameState currentState,int x,int y) {
		GameState nextState =currentState;
		try{
			nextState = currentState.nextState(thisPlayer, x, y);

		}
		catch(OthelloMoveException e){
			System.out.println(e);
		}
		for(int i=0; i<SIZE; i++){
			for(int j=0; j<SIZE; j++){
				if( (""+nextState.getState(i,j)).equals("Empty")){ next_state[i][j] = 0; }
				else if( (""+nextState.getState(i,j)).equals("Black")){ next_state[i][j] = 1; }
				else { next_state[i][j] = 2; }
			}
		}
		return ;
	}

	private Move random(GameState currentState){
		Move move = new Move();
		Integer tate=0, yoko=0;
		do {
			yoko = (int)(Math.random()*SIZE);
			tate = (int)(Math.random()*SIZE);
		} while (!currentState.isLegal(thisPlayer,yoko,tate));
		move.x = yoko;
		move.y = tate;
		return move;
	}


	private Move openningStrategy(GameState currentState){
		Move move = new Move();
		Integer tate=0, yoko=0;

		if(count == 0){
			tate=4;
			yoko=5;
		}else if(count ==1){
			if(check_state[5][3]==2){
				tate = 2;
				yoko = 4;
			}else if(check_state[3][5]==2){
				tate = 4;
				yoko = 2;
			}else if(check_state[5][5]==2){
				yoko=4;
				tate=5;
			}else{
				tate = 2;
				yoko = 3;
			}
		}else if (count == 2){
			if(check_state[4][5]==3){
				yoko=4;
				tate=5;
			}else if(check_state[4][2]==3){
				yoko=4;
				tate=2;
			}else if(check_state[4][6]==3){
				yoko=4;
				tate=6;
			}else{
				yoko=3;
				tate=2;
			}
		}else if(count == 3){
			if (check_state[3][2]==3){
				yoko=3;
				tate=2;
			}else if(check_state[2][3]==3){
				yoko=2;
				tate=3;
			}else if(check_state[2][2]==3){
				yoko=2;
				tate=2;
			}else {
				move=commonStrategy(currentState);
				return move;
			}
		}
		move.x = yoko;
		move.y = tate;

		return move;
	}

	private Move commonStrategy(GameState currentState){
		Move move = new Move();
		int whiteCount=0;

		if(check_state[0][0]==3){ //角
			move.x=0;
			move.y=0;
			return move;

		}else if(check_state[0][7]==3){
			move.x=0;
			move.y=7;
			return move;
		}else if(check_state[7][0]==3){
			move.x=7;
			move.y=0;
			return move;
		}else if(check_state[7][7]==3){
			move.x=7;
			move.y=7;
			return move;
		}else if(check_state[2][0]==3 && !(check_state[1][1]==2 && check_state[0][2]==1)){
			move.x=2;
			move.y=0;
			return move;
		}else if(check_state[5][0]==3 && !(check_state[6][1]==2 && check_state[7][2]==1)){
			move.x=5;
			move.y=0;
			return move;
		}else if(check_state[0][2]==3 && !(check_state[1][1]==2 && check_state[2][0]==1)){
			move.x=0;
			move.y=2;
			return move;
		}else if(check_state[7][2]==3 && !(check_state[6][1]==2 && check_state[5][0]==1)){
			move.x=7;
			move.y=2;
			return move;
		}else if(check_state[0][5]==3 && !(check_state[1][6]==2 && check_state[2][7]==1)){
			move.x=0;
			move.y=5;
			return move;
		}else if(check_state[7][5]==3 && !(check_state[6][6]==2 && check_state[5][7]==1)){
			move.x=7;
			move.y=5;
			return move;
		}else if(check_state[2][7]==3 && !(check_state[1][6]==2 && check_state[0][5]==1)){
			move.x=2;
			move.y=7;
			return move;
		}else if(check_state[5][7]==3 && !(check_state[6][6]==2 && check_state[7][5]==1)){
			move.x=5;
			move.y=7;
			return move;
		}

		int minCount=20;
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE;j++){
				if(check_state[i][j]==3){
					if((i>1&&i<6)||(j>1&&j<6)){
						Integer openningCount=0;
						List<Move> changeLocation = diffState(currentState, i, j);
						for(Move location:changeLocation){
							Integer x=new Integer(0);
							Integer y=new Integer(0);
							if(Objects.equals(location.x, 0) && Objects.equals(location.y, 0)){
								for(int k=0;k<2;k++){
									for(int l=0;l<2;l++){
										x=location.x+k;
										y=location.y+l;
										if(Objects.equals(check_state[x][y], 0)||Objects.equals(check_state[x][y], 3)){
											openningCount++;
										}
									}
								}
							}else if(Objects.equals(location.x, 7) && Objects.equals(location.y, 0)){
								for(int k=-1;k<1;k++){
									for(int l=0;l<2;l++){
										x=location.x+k;
										y=location.y+l;
										if(Objects.equals(check_state[x][y], 0)||Objects.equals(check_state[x][y], 3)){
											openningCount++;
										}
									}
								}
							}else if(Objects.equals(location.x, 0) && Objects.equals(location.y, 7)){
								for(int k=0;k<2;k++){
									for(int l=-1;l<1;l++){
										x=location.x+k;
										y=location.y+l;
										if(Objects.equals(check_state[x][y], 0)||Objects.equals(check_state[x][y], 3)){
											openningCount++;
										}
									}
								}
							}else if(Objects.equals(location.x, 7) && Objects.equals(location.y, 7)){
								for(int k=-1;k<1;k++){
									for(int l=-1;l<1;l++){
										x=location.x+k;
										y=location.y+l;
										if(Objects.equals(check_state[x][y], 0)||Objects.equals(check_state[x][y], 3)){
											openningCount++;
										}
									}
								}
							}else if(Objects.equals(location.x, 0)){
								for(int k=0;k<2;k++){
									for(int l=-1;l<2;l++){
										x=location.x+k;
										y=location.y+l;
										if(Objects.equals(check_state[x][y], 0)||Objects.equals(check_state[x][y], 3)){
											openningCount++;
										}
									}
								}
							}else if(Objects.equals(location.x, 7)){
								for(int k=-1;k<1;k++){
									for(int l=-1;l<2;l++){
										x=location.x+k;
										y=location.y+l;
										if(Objects.equals(check_state[x][y], 0)||Objects.equals(check_state[x][y], 3)){
											openningCount++;
										}
									}
								}
							}else if(Objects.equals(location.y, 0)){
								for(int k=-1;k<2;k++){
									for(int l=0;l<2;l++){
										x=location.x+k;
										y=location.y+l;
										if(Objects.equals(check_state[x][y], 0)||Objects.equals(check_state[x][y], 3)){
											openningCount++;
										}
									}
								}
							}else if(Objects.equals(location.y, 7)){
								for(int k=-1;k<2;k++){
									for(int l=-1;l<1;l++){
										x=location.x+k;
										y=location.y+l;
										if(Objects.equals(check_state[x][y], 0)||Objects.equals(check_state[x][y], 3)){
											openningCount++;
										}
									}
								}
							}else{
								for(int k=-1;k<2;k++){
									for(int l=-1;l<2;l++){
										x=location.x+k;
										y=location.y+l;
										if(Objects.equals(check_state[x][y], 0)||Objects.equals(check_state[x][y], 3)){
											openningCount++;
										}
									}
								}
							}
						}
						if(openningCount<minCount){
							minCount=openningCount;
							move.x=i;
							move.y=j;
						}
						/*
					}else if(check_state[0][1]==3 && (check_state[0][0]==1 || (check_state[0][0]==2 && check_state[0][2]==2))){
						move.x=0;
						move.y=1;
						return move;
					}else if(check_state[1][0]==3 && (check_state[0][0]==1|| (check_state[0][0]==2 && check_state[2][0]==2))){
						move.x=1;
						move.y=0;
						return move;
					}else if(check_state[6][0]==3 && (check_state[7][0]==1 || (check_state[7][0]==2 && check_state[5][0]==2))){
						move.x=6;
						move.y=0;
						return move;
					}else if(check_state[7][1]==3 && (check_state[7][0]==1 || (check_state[7][0]==2 && check_state[7][2]==2))){
						move.x=7;
						move.y=1;
						return move;
					}else if(check_state[0][6]==3 && (check_state[0][7]==1 || (check_state[0][7]==2 && check_state[0][5]==2))){
						move.x=0;
						move.y=6;
						return move;
					}else if(check_state[1][7]==3 && (check_state[0][7]==1 || (check_state[0][7]==2 && check_state[2][7]==2))){
						move.x=1;
						move.y=7;
						return move;
					}else if(check_state[6][7]==3 && (check_state[7][7]==1 || (check_state[7][7]==2 && check_state[6][7]==2))){
						move.x=6;
						move.y=7;
						return move;
					}else if(check_state[7][6]==3 && (check_state[7][7]==1 || (check_state[7][7]==2 && check_state[7][5]==2))){
						move.x=7;
						move.y=6;
						return move;
					}
*/
}

				}
			}
		}
		if(!currentState.isLegal(thisPlayer,move.x,move.y)){
			return lastStrategy(currentState);
		}
		return move;
	}

	private List<Move> diffState(GameState currentState, int x, int y){
		List<Move> diffList=new ArrayList<Move>();

		checkNext(currentState, x, y);

		for(int i=0;i<SIZE; i++){
			for(int j=0; j<SIZE; j++){
				if(check_state[i][j]==2 && next_state[i][j]==1){
					Move move = new Move();
					move.x=i;
					move.y=j;
					diffList.add(move);
				}
			}
		}
		return diffList;
	}
	private int canGet(List<Move> changeLocation){
		return changeLocation.size();
	}
	private Move lastStrategy(GameState currentState){
		Move move=new Move();
		int getPieces=0;
		int maxGet=0;

		if(check_state[0][0]==3){ //角
			move.x=0;
			move.y=0;
			return move;

		}else if(check_state[0][7]==3){
			move.x=0;
			move.y=7;
			return move;
		}else if(check_state[7][0]==3){
			move.x=7;
			move.y=0;
			return move;
		}else if(check_state[7][7]==3){
			move.x=7;
			move.y=7;
			return move;
		}
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE;j++){
				if(check_state[i][j]==3){
					getPieces=canGet(diffState(currentState, i, j));
					if(maxGet<getPieces){
						move.x=i;
						move.y=j;
						maxGet=getPieces;
					}
				}
			}
		}
		return move;

	}

}
