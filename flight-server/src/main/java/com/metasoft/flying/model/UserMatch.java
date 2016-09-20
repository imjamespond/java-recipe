package com.metasoft.flying.model;

import java.util.ArrayList;
import java.util.List;

import com.metasoft.flying.vo.ChessScoreVO;

/**
 * @author james
 *
 */
public class UserMatch {
	public UserMatch() {

	}

	private long mtime;
	private long duration;
	private List<ChessScoreVO> scores;
	private int win;

	public List<ChessScoreVO> getScores() {
		return scores;
	}

	public void setScores(List<ChessScoreVO> scores) {
		this.scores = scores;
	}
	
	public void addScore(long uid, int score) {
		if(null == scores)
			scores = new ArrayList<ChessScoreVO>(4);
		ChessScoreVO vo = new ChessScoreVO();
		vo.setScore(score);
		vo.setUserId(uid);
		scores.add(vo);
	}

	public long getMtime() {
		return mtime;
	}

	public void setMtime(long mtime) {
		this.mtime = mtime;
	}

	public int getWin() {
		return win;
	}

	public void setWin(int win) {
		this.win = win;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
}
