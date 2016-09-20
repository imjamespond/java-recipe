package com.metasoft.flying.vo;

import java.util.List;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

@DescAnno("比赛结果")
@EventAnno(desc = "", name = "event.match.finish")
public class MatchResultVO {
	@DescAnno("比赛时间")
	private Long duration;
	@DescAnno("结束时间")
	private Long finishtime;
	@DescAnno("结果<ChessScoreVO>")
	private List<ChessScoreVO> players;
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public List<ChessScoreVO> getPlayers() {
		return players;
	}
	public void setPlayers(List<ChessScoreVO> players) {
		this.players = players;
	}
	public Long getFinishtime() {
		return finishtime;
	}
	public void setFinishtime(Long finishtime) {
		this.finishtime = finishtime;
	}

}
