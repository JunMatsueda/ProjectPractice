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

	private Integer[][] check_state = new Integer[SIZE][SIZE]; //int[x][y]
	private Integer count =0;
	public static final int zero = 0;
	public static final int one = 1;
	public static final int two = 2;
	public static final int three = 3;

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
		int count3 = 0;
		for(int i=0; i<SIZE; i++){
			for(int j=0; j<SIZE; j++){
				if(currentState.isLegal(thisPlayer, i,j)){
					check_state[i][j] = three;
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
		}else{
			m=commonStrategy(currentState);
		}


		return m;
	}


	private void check(GameState currentState) {
		for(int i=0; i<SIZE; i++){
			for(int j=0; j<SIZE; j++){
				if( (""+currentState.getState(i,j)).equals("Empty")){ check_state[i][j] = zero; }
				else if( (""+currentState.getState(i,j)).equals("Black")){ check_state[i][j] = one; }
				else { check_state[i][j] = two; }
			}
		}
	}
	private Integer[][] checkNext(GameState currentState,Integer[][] next_state) {
		for(int i=0; i<SIZE; i++){
			for(int j=0; j<SIZE; j++){
				if( (""+currentState.getState(i,j)).equals("Empty")){ next_state[i][j] = zero; }
				else if( (""+currentState.getState(i,j)).equals("Black")){ next_state[i][j] = one; }
				else { next_state[i][j] = two; }
			}
		}
		return next_state;
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

		int minCount=20;
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE;j++){
				if(check_state[i][j]==3){
					Integer openningCount=0;
					List<Move> chengeLocation = diffState(currentState, i, j);
					for(Move location:chengeLocation){
						if(Objects.equals(location.x, 0) && Objects.equals(location.y, 0)){
							for(int k=0;k<2;k++){
								for(int l=0;l<2;l++){
									Integer x=location.x+k;
									Integer y=location.y+l;
									if(check_state[x][y]==0||check_state[x][y]==3){
										openningCount++;
									}
								}
							}
						}else if(Objects.equals(location.x, 7) && Objects.equals(location.y, 0)){
							for(int k=-1;k<1;k++){
								for(int l=0;l<2;l++){
									Integer x=location.x+k;
									Integer y=location.y+l;
									if(check_state[x][y]==0||check_state[x][y]==3){
										openningCount++;
									}
								}
							}
						}else if(Objects.equals(location.x, 0) && Objects.equals(location.y, 7)){
							for(int k=0;k<2;k++){
								for(int l=-1;l<1;l++){
									Integer x=location.x+k;
									Integer y=location.y+l;
									if(check_state[x][y]==0||check_state[x][y]==3){
										openningCount++;
									}
								}
							}
						}else if(Objects.equals(location.x, 7) && Objects.equals(location.y, 7)){
							for(int k=-1;k<1;k++){
								for(int l=-1;l<1;l++){
									Integer x=location.x+k;
									Integer y=location.y+l;
									if(check_state[x][y]==0||check_state[x][y]==3){
										openningCount++;
									}
								}
							}
						}else{
							for(int k=-1;k<2;k++){
								for(int l=-1;l<2;l++){
									Integer x=location.x+k;
									Integer y=location.y+l;
									if(check_state[x][y]==0||check_state[x][y]==3){
										openningCount++;
									}
								}
							}
						}
						if(openningCount<minCount){
							minCount=openningCount;
							move.x=i;
							move.y=j;
						}
					}
				}
			}
		}
		System.out.println("x="+move.x+" y="+move.y+"Count="+minCount);
		return move;
	}

	private List<Move> diffState(GameState currentState, int x, int y){
		List<Move> diffList=new ArrayList<Move>();
		Integer[][] next_state = new Integer[SIZE][SIZE];

		try{
			//GameState nextState = currentState.nextState(thisPlayer, x, y);
			next_state=checkNext(currentState.nextState(thisPlayer, x, y), next_state);
		}
		catch(OthelloMoveException e){
			System.out.println(e);
		}

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
}
