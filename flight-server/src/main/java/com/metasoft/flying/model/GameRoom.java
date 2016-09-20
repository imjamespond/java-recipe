package com.metasoft.flying.model;

import java.lang.ref.WeakReference;

import com.metasoft.flying.net.BaseGroup;

/**
 * @author james
 * 
 */
public class GameRoom extends BaseGroup {
	//private static final Logger logger = LoggerFactory.getLogger(GameRoom.class);

	private long masterId = 0;// 房主id
	private Flight flight;
	private ChessApplication chessApp;
	private ChessGuess guess;
	private WeakReference<PkArena> arena;// 竞技场位置
	private WeakReference<FlightMatch> match;// 比赛
	private WeakReference<FlightNpc> npc;
	private WeakReference<FlightRp> rp;
	private WeakReference<FlightPve> pve;
	private WeakReference<FlightPvp> pvp;

	public GameRoom(String name) {
		super(name);
		flight = new Flight(this);
		chessApp = new ChessApplication("");
		guess = new ChessGuess("");
	}

	public PkArena getArena() {
		if(null == arena){
			return null;
		}
		return arena.get();
	}

	public void setArena(WeakReference<PkArena> arena) {
		this.arena = arena;
	}
	

	public long getMasterId() {
		return masterId;
	}

	public void setMasterId(long masterId) {
		this.masterId = masterId;
	}

	public Flight getFlight() {
		if(match!=null){
			Flight ref = match.get();
			if(ref!=null){
				return ref;
			}
		}else if(npc!=null){
			Flight ref = npc.get();
			if(ref!=null){
				return ref;
			}
		}else if(rp!=null){
			Flight ref = rp.get();
			if(ref!=null){
				return ref;
			}
		}else if(pve!=null){
			Flight ref = pve.get();
			if(ref!=null){
				return ref;
			}
		}else if(pvp!=null){
			Flight ref = pvp.get();
			if(ref!=null){
				return ref;
			}
		}
		
		return flight;
	}

	public ChessApplication getChessApp() {
		return chessApp;
	}

	public void setChessApp(ChessApplication chessApp) {
		this.chessApp = chessApp;
	}

	public ChessGuess getGuess() {
		return guess;
	}

	public void setGuess(ChessGuess guess) {
		this.guess = guess;
	}
	public FlightMatch getFlightMatch() {
		return match.get();
	}

	public void setFlightMatch(FlightMatch m) {
		if(match!=null){
			FlightMatch ref = match.get();
			if(ref!=null){
				ref.reset();
			}
		}
		
		if(null == m){
			match = null;
		}else{
			match = new WeakReference<FlightMatch>(m);
		}
	}
	public void setFlightNpc(FlightNpc n) {
		if(npc!=null){
			FlightNpc ref = npc.get();
			if(ref!=null){
				ref.reset();
			}
		}
		
		if(null == n){
			npc = null;
		}else{
			npc = new WeakReference<FlightNpc>(n);
		}
	}
	public void setFlightRp(FlightRp n) {
		if(rp!=null){
			FlightRp ref = rp.get();
			if(ref!=null){
				ref.reset();
			}
		}
		
		if(null == n){
			rp = null;
		}else{
			rp = new WeakReference<FlightRp>(n);
		}
	}
	public void setFlightPve(FlightPve n) {
		if(pve!=null){
			FlightPve ref = pve.get();
			if(ref!=null){
				ref.reset();
			}
		}
		
		if(null == n){
			pve = null;
		}else{
			pve = new WeakReference<FlightPve>(n);
		}
	}

	public FlightMatch getMatch() {
		if(match!=null){
			FlightMatch ref = match.get();
			if(ref!=null){
				return ref;
			}
		}
		return null;
	}

	public FlightNpc getNpc() {
		if(npc!=null){
			FlightNpc ref = npc.get();
			if(ref!=null){
				return ref;
			}
		}
		return null;
	}

	public FlightRp getRp() {
		if(rp!=null){
			FlightRp ref = rp.get();
			if(ref!=null){
				return ref;
			}
		}
		return null;
	}
	
	public FlightPve getPve() {
		if(pve!=null){
			FlightPve ref = pve.get();
			if(ref!=null){
				return ref;
			}
		}
		return null;
	}
	
	public void setFlightPvp(FlightPvp f) {
		if(pvp!=null){
			FlightPvp ref = pvp.get();
			if(ref!=null){
				ref.reset();
			}
		}
		
		if(null == f){
			pvp = null;
		}else{
			pvp = new WeakReference<FlightPvp>(f);
		}
	}
	public FlightPvp getPvp() {
		if(pvp!=null){
			FlightPvp ref = pvp.get();
			if(ref!=null){
				return ref;
			}
		}
		return null;
	}
	public void clean() {
		flight.end();
		setFlightRp(null);
		setFlightNpc(null);
		setFlightMatch(null);
		setFlightPve(null);
		setFlightPvp(null);
	}

}
