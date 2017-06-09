package simpleStrategies;

import game.GameState;
import game.Move;
import game.OthelloMoveException;
import game.Player;
import game.Strategy;

public class GoteShiroPlayer extends Strategy {

	private int[][] check_state = new int[SIZE][SIZE];

	int count = 0;

	// コンストラクタは改造せずこのまま使うこと
	public GoteShiroPlayer(Player _thisPlayer, int size) {
		super(_thisPlayer, size);
	}

	//	Override
	public Move nextMove(GameState currentState, int remainingTime) {

		// 8*8マスの石の状態をcheck_state配列に格納
		check(currentState);

		// check_state配列の表示用
		// 無0　黒1　白2

		for(int j=0; j<SIZE; j++){
			for(int i=0; i<SIZE; i++){
				if(currentState.isLegal(thisPlayer, i,j)){
					check_state[i][j] = 4;
				}
				//System.out.print(check_state[i][j] + " ");
			}
			//System.out.println();
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

		if(count<2){
			m=openningStrategy(currentState);
		}else{
			m=commonStrategy(currentState);
		}

		count++;
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
	private Move random(GameState currentState){
		int yoko,tate;
		Move move = new Move();
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
		int yoko=0,tate =0;
		if(count == 0){
			if(check_state[3][2]==1){
				yoko=2;
				tate=4;
			}else if(check_state[2][3] == 1){
				yoko=4;
				tate=2;
			}else if(check_state[5][4]==1){
				yoko=3;
				tate=5;
			}else if(check_state[4][5]==1){
				yoko=5;
				tate=3;
			}
		}else if(count == 1){
			if(check_state[5][3]==4){
				yoko=5;
				tate=3;
			}else if(check_state[3][5]==4){
				yoko=3;
				tate=5;
			}else if(check_state[2][4]==4){
				yoko=2;
				tate=4;
			}else if(check_state[4][2]==4){
				yoko=4;
				tate=2;
			}else{
				move=random(currentState);
				return move;
			}
		}
		move.x = yoko;
		move.y = tate;

		return move;
	}

	private Move commonStrategy(GameState currentState){
		int tate=0, yoko=0;
		Move move = new Move();

		if(check_state[0][0]==4){ //角
			tate=0;
			yoko=0;
		}else if(check_state[0][7]==4){
			tate=7;
			yoko=0;
		}else if(check_state[7][0]==4){
			tate=0;
			yoko=7;
		}else if(check_state[7][7]==4){
			tate=7;
			yoko=7;
		}else if(check_state[2][2]==4){ //中四隅
			yoko=2;
			tate=2;
		}else if(check_state[5][2]==4){
			yoko=5;
			tate=2;
		}else if(check_state[2][5]==4){
			yoko=2;
			tate=5;
		}else if(check_state[5][5]==4){
			yoko=5;
			tate=5;
		}else if(check_state[2][0]==4 && !(check_state[1][1]==1 && check_state[0][2]==2)){
			yoko=2;
			tate=0;
		}else if(check_state[5][0]==4 && !(check_state[6][1]==1 && check_state[7][2]==2)){
			yoko=5;
			tate=0;
		}else if(check_state[0][2]==4 && !(check_state[1][1]==1 && check_state[2][0]==2)){
			yoko=0;
			tate=2;
		}else if(check_state[7][2]==4 && !(check_state[6][1]==1 && check_state[5][0]==2)){
			yoko=7;
			tate=2;
		}else if(check_state[0][5]==4 && !(check_state[1][6]==1 && check_state[2][7]==2)){
			yoko=0;
			tate=5;
		}else if(check_state[7][5]==4 && !(check_state[6][6]==1 && check_state[5][7]==2)){
			yoko=7;
			tate=5;
		}else if(check_state[2][7]==4 && !(check_state[1][6]==1 && check_state[0][5]==2)){
			yoko=2;
			tate=7;
		}else if(check_state[5][7]==4 && !(check_state[6][6]==1 && check_state[7][5]==2)){
			yoko=5;
			tate=7;
		}else if(check_state[0][1]==4 && check_state[0][0]==2){
			yoko=0;
			tate=1;
		}else if(check_state[1][0]==4 && check_state[0][0]==2){
			yoko=1;
			tate=0;
		}else if(check_state[6][0]==4 && check_state[7][0]==2){
			yoko=6;
			tate=0;
		}else if(check_state[7][1]==4 && check_state[7][0]==2){
			yoko=7;
			tate=1;
		}else if(check_state[0][6]==4 && check_state[0][7]==2){
			yoko=0;
			tate=6;
		}else if(check_state[1][7]==4 && check_state[0][7]==2){
			yoko=1;
			tate=7;
		}else if(check_state[6][7]==4 && check_state[7][7]==2){
			yoko=6;
			tate=7;
		}else if(check_state[7][6]==4 && check_state[7][7]==2){
			yoko=7;
			tate=6;
		}else {
			for(int i=2;i<6;i++){
				if(check_state[i][0]==4){
					if(check_state[i-1][0]==2 || check_state[i+1][0]==2){ //　相手のマスを取るようにする
						move.x=i;
						move.y=0;
						return move;
					}
				}else if(check_state[i][7]==4){
					if(check_state[i-1][7]==2 || check_state[i+1][7]==2){
						move.x=i;
						move.y=7;
						return move;
					}
				}else if(check_state[0][i]==4){
					if(check_state[0][i-1]==2 || check_state[0][i+1]==2){
						move.x=0;
						move.y=i;
						return move;
					}
				}else if(check_state[7][i]==4){
					if(check_state[7][i-1]==2 || check_state[7][i+1]==2){
						move.x=7;
						move.y=i;
						return move;
					}
				}
			}
			for(int i=2; i<6; i++){
				for(int j=2; j<6;j++){
					if(check_state[i][j]==4){
						move.x=i;
						move.y=j;
						return move;
					}
				}
			}
			for(int i=3; i<5; i++){
				for(int j=0; j<SIZE;j++){
					if(check_state[i][j]==4){
						move.x=i;
						move.y=j;
						return move;
					}
				}
			}
			for(int i=0; i<SIZE; i++){
				for(int j=3; j<5;j++){
					if(check_state[i][j]==4){
						move.x=i;
						move.y=j;
						return move;
					}
				}
			}
			for(int i=0; i<SIZE; i++){
				for(int j=2; j<6;j++){
					if(check_state[i][j]==4){
						move.x=i;
						move.y=j;
						return move;
					}
				}
			}
			for(int i=2; i<6; i++){
				for(int j=0; j<SIZE;j++){
					if(check_state[i][j]==4){
						move.x=i;
						move.y=j;
						return move;
					}
				}
			}
			move = random(currentState);
			return move;
		}

		move.x = yoko;
		move.y = tate;

		return move;
	}
}
