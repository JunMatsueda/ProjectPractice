package simpleStrategies;

import game.GameState;
import game.Move;
import game.OthelloMoveException;
import game.Player;
import game.Strategy;

public class SenteKuroPlayer extends Strategy {

	private int[][] check_state = new int[SIZE][SIZE];

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

		for(int j=0; j<SIZE; j++){
			for(int i=0; i<SIZE; i++){
			//	System.out.print(check_state[i][j] + " ");
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
		do {
			yoko = (int)(Math.random()*SIZE);
			tate = (int)(Math.random()*SIZE);
		} while (!currentState.isLegal(thisPlayer,yoko,tate));

		m.x = yoko;
		m.y = tate;

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

}
