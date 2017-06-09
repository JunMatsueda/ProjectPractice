package simpleStrategies;

import game.GameState;
import game.Move;
import game.OthelloMoveException;
import game.Player;
import game.Strategy;

public class SenteKuroPlayer extends Strategy {

	private int[][] check_state = new int[SIZE][SIZE]; //int[x][y]

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
		if(count<3){
			m=openningStrategy(currentState);
		}else{
			m=commonStrategy(currentState);
		}

		this.count++;

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
		Move move = new Move();
		int tate=0, yoko=0;
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
		int tate=0, yoko=0;

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
			}else{
				yoko=3;
				tate=2;
			}
		}
		move.x = yoko;
		move.y = tate;

		return move;
	}

	private Move commonStrategy(GameState currentState){
		int tate=0, yoko=0;
		Move move = new Move();

		if(check_state[0][0]==3){ //角
			tate=0;
			yoko=0;
		}else if(check_state[0][7]==3){
			tate=7;
			yoko=0;
		}else if(check_state[7][0]==3){
			tate=0;
			yoko=7;
		}else if(check_state[7][7]==3){
			tate=7;
			yoko=7;
		}else if(check_state[2][2]==3){ //中四隅
			yoko=2;
			tate=2;
		}else if(check_state[5][2]==3){
			yoko=5;
			tate=2;
		}else if(check_state[2][5]==3){
			yoko=2;
			tate=5;
		}else if(check_state[5][5]==3){
			yoko=5;
			tate=5;
		}else if(check_state[2][0]==3){
			yoko=2;
			tate=0;
		}else if(check_state[5][0]==3){
			yoko=5;
			tate=0;
		}else if(check_state[0][2]==3){
			yoko=0;
			tate=2;
		}else if(check_state[7][2]==3){
			yoko=7;
			tate=2;
		}else if(check_state[0][5]==3){
			yoko=0;
			tate=5;
		}else if(check_state[7][5]==3){
			yoko=7;
			tate=5;
		}else if(check_state[2][7]==3){
			yoko=2;
			tate=7;
		}else if(check_state[5][7]==3){
			yoko=5;
			tate=7;
		}else if(check_state[0][0]==1 && check_state[1][0]==3){
			yoko=1;
			tate=0;
		}else if(check_state[0][0]==1 && check_state[0][1]==3){
			yoko=0;
			tate=1;
		}else if(check_state[7][0]==1 && check_state[6][0]==3){
			yoko=6;
			tate=0;
		}else if(check_state[7][0]==1 &&check_state[7][1]==3){
			yoko=7;
			tate=1;
		}else if(check_state[0][7]==1 && check_state[1][7]==3){
			yoko=1;
			tate=7;
		}else if(check_state[0][7]==1 && check_state[0][6]==3){
			yoko=0;
			tate=6;
		}else if(check_state[7][7]==1 && check_state[7][6]==3){
			yoko=7;
			tate=6;
		}else if(check_state[7][7]==1 &&check_state[6][7]==3){
			yoko=6;
			tate=7;
		}else {
			for(int i=2; i<6; i++){
				for(int j=2; j<6;j++){
					if(check_state[i][j]==3){
						move.x=i;
						move.y=j;
						return move;
					}
				}
			}
			for(int i=2; i<6; i++){
				for(int j=2; j<SIZE;j++){
					if(check_state[i][j]==3){
						move.x=i;
						move.y=j;
						return move;
					}
				}
			}
			for(int i=0; i<SIZE; i++){
				for(int j=2; j<6;j++){
					if(check_state[i][j]==3){
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
