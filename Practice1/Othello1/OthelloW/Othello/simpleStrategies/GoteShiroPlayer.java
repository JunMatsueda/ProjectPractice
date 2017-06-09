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

		m=openningStrategy(currentState);


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
		}else {
			if(check_state[0][0]==4){
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
			}else{
				move=random(currentState);
				return move;
			}
		}
		move.x = yoko;
		move.y = tate;

		return move;
	}
}
