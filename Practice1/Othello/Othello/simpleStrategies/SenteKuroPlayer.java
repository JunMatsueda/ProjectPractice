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
		/*
		if(count<4){
			m=openningStrategy(currentState);
			this.count++;
		}else if(count>3 ){
			m=commonStrategy(currentState);
		}
		if(!currentState.isLegal(thisPlayer, m.x, m.y)){
			m=lastStrategy(currentState);
		}
		*/
		m=checkCorner(currentState,m);
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE;j++){
				if(check_state[i][j]==3){
					checkNext(currentState,i,j);
					if((next_state[0][0]!=4&&next_state[0][7]!=4)&&(next_state[7][0]!=4&&next_state[7][7]!=4)){
						m.x=i;
						m.y=j;
					}
				}
			}
		}
		if(!currentState.isLegal(thisPlayer, m.x, m.y)){
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
		int tate=0, yoko=0;
		Move move = new Move();
		/*
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
		}else
		*/ if(check_state[2][2]==3){ //中四隅
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
		}else if(check_state[2][0]==3 && !(check_state[1][1]==2 && check_state[0][2]==1)){
			yoko=2;
			tate=0;
		}else if(check_state[5][0]==3 && !(check_state[6][1]==2 && check_state[7][2]==1)){
			yoko=5;
			tate=0;
		}else if(check_state[0][2]==3 && !(check_state[1][1]==2 && check_state[2][0]==1)){
			yoko=0;
			tate=2;
		}else if(check_state[7][2]==3 && !(check_state[6][1]==2 && check_state[5][0]==1)){
			yoko=7;
			tate=2;
		}else if(check_state[0][5]==3 && !(check_state[1][6]==2 && check_state[2][7]==1)){
			yoko=0;
			tate=5;
		}else if(check_state[7][5]==3 && !(check_state[6][6]==2 && check_state[5][7]==1)){
			yoko=7;
			tate=5;
		}else if(check_state[2][7]==3 && !(check_state[1][6]==2 && check_state[0][5]==1)){
			yoko=2;
			tate=7;
		}else if(check_state[5][7]==3 && !(check_state[6][6]==2 && check_state[7][5]==1)){
			yoko=5;
			tate=7;
		}
		/*else if(check_state[0][1]==3 && (check_state[0][0]==1 || (check_state[0][0]==2 && check_state[0][2]==2))){
			yoko=0;
			tate=1;
		}else if(check_state[1][0]==3 && (check_state[0][0]==1 || (check_state[0][0]==2 &&check_state[2][0]==2))){
			yoko=1;
			tate=0;
		}else if(check_state[6][0]==3 && (check_state[7][0]==1|| (check_state[7][0]==2 && check_state[5][0]==2))){
			yoko=6;
			tate=0;
		}else if(check_state[7][1]==3 && (check_state[7][0]==1 || (check_state[7][0]==2 && check_state[7][2]==2))){
			yoko=7;
			tate=1;
		}else if(check_state[0][6]==3 && (check_state[0][7]==1|| (check_state[0][7]==2 && check_state[0][5]==2))){
			yoko=0;
			tate=6;
		}else if(check_state[1][7]==3 && (check_state[0][7]==1 || (check_state[0][7]==2 && check_state[2][7]==2))){
			yoko=1;
			tate=7;
		}else if(check_state[6][7]==3 && (check_state[7][7]==1|| (check_state[7][7]==2 && check_state[6][7]==2))){
			yoko=6;
			tate=7;
		}else if(check_state[7][6]==3 && (check_state[7][7]==1 || (check_state[7][7]==2 && check_state[7][5]==2))){
			yoko=7;
			tate=6;
		}
		*/else {

			if(count>15){
				move=sideStrategy(currentState);
				if(currentState.isLegal(thisPlayer,move.x, move.y)){
					return move;
				}
			}

			int minCount=20;
			for(int i=1;i<7;i++){
				for(int j=1;j<7;j++){
					if(!(((i==1&&j==1)||(i==1&&j==6))||((i==6&&j==1)||(i==6&&j==6)))){
						if(check_state[i][j]==3){
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
						}
					}
				}
			}
			if(!currentState.isLegal(thisPlayer,move.x,move.y)){
				return otherCase(currentState);
			}
			return move;
		}

		move.x = yoko;
		move.y = tate;

		return move;
	}
	private void checkNext(GameState currentState,int x,int y) {
		GameState nextState =currentState;
		try{
			nextState = currentState.nextState(thisPlayer, x, y);

		}
		catch(OthelloMoveException e){
			//System.out.println(e);
		}
		for(int i=0; i<SIZE; i++){
			for(int j=0; j<SIZE; j++){
				if( (""+nextState.getState(i,j)).equals("Empty")){ next_state[i][j] = 0; }
				else if( (""+nextState.getState(i,j)).equals("Black")){ next_state[i][j] = 1; }
				else { next_state[i][j] = 2; }
			}
		}
		for(int i=0; i<SIZE; i++){
			for(int j=0; j<SIZE; j++){
				if(nextState.isLegal(thisPlayer.White, i,j)){
					next_state[i][j] = 4;
				}
			}
		}

		return ;
	}

	private Move lastStrategy(GameState currentState){
		Move move=new Move();
		int getPieces=0;
		int maxGet=0;
/*
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
		*/
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

	private int canGet(List<Move> changeLocation){
		return changeLocation.size();
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

	private Move otherCase(GameState currentState){
		Move move = new Move();
		for(int k=1;k<SIZE-1;k++){
			boolean notBlack=true;
			if(check_state[0][k]==3){
				checkNext(currentState,0,k);
				for(int l=1;l<SIZE-1;l++){
					if(next_state[0][l]==2){
						notBlack=false;
					}else if(next_state[0][k+1]==4||next_state[0][k-1]==4){
						notBlack=false;
					}else if(next_state[1][1]==2||next_state[1][6]==2){
						notBlack=false;
					}else if(next_state[0][l]==4&&(next_state[0][l-1]==1&&next_state[0][l+1]==1)){
						notBlack=false;
					}
				}
				if(notBlack){
					move.x=0;
					move.y=k;
					return move;
				}
			}else if(check_state[7][k]==3){
				checkNext(currentState,7,k);
				for(int l=1;l<SIZE-1;l++){
					if(next_state[7][l]==2){
						notBlack=false;
					}else if(next_state[7][k+1]==4||next_state[7][k-1]==4){
						notBlack=false;
					}else if(next_state[6][1]==2||next_state[6][6]==2){
						notBlack=false;
					}else if(next_state[7][l]==4&&(next_state[7][l-1]==1&&next_state[7][l+1]==1)){
						notBlack=false;
					}
				}
				if(notBlack){
					move.x=7;
					move.y=k;
					return move;
				}
			}else if(check_state[k][0]==3){
				checkNext(currentState,k,0);
				for(int l=1;l<SIZE-1;l++){
					if(next_state[l][0]==2){
						notBlack=false;
					}else if(next_state[k+1][0]==4||next_state[k-1][0]==4){
						notBlack=false;
					}else if(next_state[1][1]==2||next_state[6][1]==2){
						notBlack=false;
					}else if(next_state[l][0]==4&&(next_state[l-1][0]==1&&next_state[l+1][0]==1)){
						notBlack=false;
					}
				}
				if(notBlack){
					move.x=k;
					move.y=0;
					return move;
				}
			}else if(check_state[k][7]==3){
				checkNext(currentState,k,7);
				for(int l=1;l<SIZE-1;l++){
					if(next_state[l][7]==2){
						notBlack=false;
					}else if(next_state[k+1][7]==4||next_state[k-1][7]==4){
						notBlack=false;
					}else if(next_state[1][6]==2||next_state[6][6]==2){
						notBlack=false;
					}else if(next_state[l][7]==4&&(next_state[l-1][7]==1&&next_state[l+1][7]==1)){
						notBlack=false;
					}
				}
				if(notBlack){
					move.x=k;
					move.y=7;
					return move;
				}
			}
		}
		if(check_state[1][1]==3){
			boolean notBlack = true;
			checkNext(currentState, 1, 1);
			if(next_state[0][0]==4||next_state[7][7]==4){
				notBlack=false;
			}

			if(notBlack){
				move.x=1;
				move.y=1;
				return move;

			}
		}
		if(check_state[6][6]==3){
			boolean notBlack = true;
			checkNext(currentState, 6, 6);
			if(next_state[0][0]==4||next_state[7][7]==4){
				notBlack=false;
			}
			if(notBlack){
				move.x=6;
				move.y=6;
				return move;

			}
		}
		if(check_state[1][6]==3){
			boolean notBlack = true;
			checkNext(currentState, 1, 6);
			if(next_state[0][7]==4||next_state[7][0]==4){
				notBlack=false;
			}
			if(notBlack){
				move.x=1;
				move.y=6;
				return move;

			}
		}
		if(check_state[6][1]==3){
			boolean notBlack = true;
			checkNext(currentState, 6, 1);
			if(next_state[0][7]==4||next_state[7][0]==4){
				notBlack=false;
			}
			if(notBlack){
				move.x=6;
				move.y=1;
				return move;

			}
		}
		if(check_state[1][1]==3 && (check_state[0][0]==2 || check_state[0][0]==1) ){
			move.x=1;
			move.y=1;
		}else if(check_state[6][1]==3 && (check_state[7][0]==2||check_state[7][0]==1)){
			move.x=6;
			move.y=1;
		}else if(check_state[0][6]==3 && (check_state[0][7]==2||check_state[0][7]==1)){
			move.x=1;
			move.y=6;
		}else if(check_state[6][6]==3 && (check_state[7][7]==2||check_state[7][7]==1)){
			move.x=6;
			move.y=6;
		}
		return move;
	}

	private Move sideStrategy(GameState currentState){
		Move move =new Move();
		if(check_state[0][0]==3){ //角
			move.x=0;
			move.y=0;
		}else if(check_state[0][7]==3){
			move.x=7;
			move.y=0;
		}else if(check_state[7][0]==3){
			move.x=0;
			move.y=7;
		}else if(check_state[7][7]==3){
			move.x=7;
			move.y=7;
		}
		for(int k=1;k<SIZE-1;k++){
			boolean notBlack=true;
			boolean notPieces=true;
			if(check_state[0][k]==3){
				checkNext(currentState,0,k);
				for(int l=1;l<SIZE-1;l++){
					if(next_state[0][l]==2){
						notBlack=false;
					}else if(next_state[0][k+1]==4||next_state[0][k-1]==4){
						notBlack=false;
					}else if(next_state[1][1]==2||next_state[1][6]==2){
						notBlack=false;
					}else if(next_state[0][l]==4&&(next_state[0][l-1]==1&&next_state[0][l+1]==1)){
						notBlack=false;
					}else if((next_state[0][l]==0||next_state[0][l]==4)&&!(check_state[0][0]==1&&check_state[0][7]==1)){
						notPieces=false;
					}
				}
				if((notBlack || notPieces)&&!(next_state[0][0]==4||next_state[0][7]==4)){
					move.x=0;
					move.y=k;
					//return move;
				}
			}else if(check_state[7][k]==3){
				checkNext(currentState,7,k);
				for(int l=1;l<SIZE-1;l++){
					if(next_state[7][l]==2){
						notBlack=false;
					}else if(next_state[7][k+1]==4||next_state[7][k-1]==4){
						notBlack=false;
					}else if(next_state[6][1]==2||next_state[6][6]==2){
						notBlack=false;
					}else if(next_state[7][l]==4&&(next_state[7][l-1]==1&&next_state[7][l+1]==1)){
						notBlack=false;
					}else if((next_state[7][l]==0||next_state[7][l]==4)&&!(check_state[7][0]==1&&check_state[7][7]==1)){
						notPieces=false;
					}
				}
				if((notBlack || notPieces)&&!(next_state[7][0]==4||next_state[7][7]==4)){
					move.x=7;
					move.y=k;
					//return move;
				}
			}else if(check_state[k][0]==3){
				checkNext(currentState,k,0);
				for(int l=1;l<SIZE-1;l++){
					if(next_state[l][0]==2){
						notBlack=false;
					}else if(next_state[k+1][0]==4||next_state[k-1][0]==4){
						notBlack=false;
					}else if(next_state[1][1]==2||next_state[6][1]==2){
						notBlack=false;
					}else if(next_state[l][0]==4&&(next_state[l-1][0]==1&&next_state[l+1][0]==1)){
						notBlack=false;
					}else if((next_state[l][0]==0||next_state[l][0]==4)&&!(check_state[0][0]==1&&check_state[7][0]==1)){
						notPieces=false;
					}
				}
				if((notBlack || notPieces)&&!(next_state[0][0]==4||next_state[7][0]==4)){
					move.x=k;
					move.y=0;
					//return move;
				}
			}else if(check_state[k][7]==3){
				checkNext(currentState,k,7);
				for(int l=1;l<SIZE-1;l++){
					if(next_state[l][7]==2){
						notBlack=false;
					}else if(next_state[k+1][7]==4||next_state[k-1][7]==4){
						notBlack=false;
					}else if(next_state[1][6]==2||next_state[6][6]==2){
						notBlack=false;
					}else if(next_state[l][7]==4&&(next_state[l-1][7]==1&&next_state[l+1][7]==1)){
						notBlack=false;
					}else if((next_state[l][7]==0||next_state[l][7]==4)&&!(check_state[0][7]==1&&check_state[7][7]==1)){
						notPieces=false;
					}
				}
				if((notBlack || notPieces)&&!(next_state[0][7]==4||next_state[7][7]==4)){
					move.x=k;
					move.y=7;
					//return move;
				}
			}
		}
		if(check_state[0][0]==3){ //角
			move.x=0;
			move.y=0;
		}else if(check_state[0][7]==3){
			move.x=7;
			move.y=0;
		}else if(check_state[7][0]==3){
			move.x=0;
			move.y=7;
		}else if(check_state[7][7]==3){
			move.x=7;
			move.y=7;
		}
		return move;
	}
	private Move checkCorner(GameState currentState, Move move){
		checkNext(currentState, move.x,move.y);
		if(check_state[0][0]==3){
			if(next_state[0][0]==4||next_state[0][0]==0){
				move.x=0;
				move.y=0;
				return move;
			}
		}else if(check_state[0][7]==3){
			if(next_state[0][7]==4||next_state[0][7]==0){
				move.x=0;
				move.y=7;
				return move;
			}
		}else if(check_state[7][0]==3){
			if(next_state[7][0]==4||next_state[7][0]==0){
				move.x=7;
				move.y=0;
				return move;
			}
		}else if(check_state[7][7]==3){
			if(next_state[7][7]==4||next_state[7][7]==0){
				move.x=7;
				move.y=7;
				return move;
			}
		}
		return move;
	}
}
