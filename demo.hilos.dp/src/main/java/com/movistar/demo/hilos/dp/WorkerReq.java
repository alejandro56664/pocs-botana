package com.movistar.demo.hilos.dp;

public class WorkerReq {

	private final long id;
	private final String content;
	private final int hilos;

	public WorkerReq(long id, String content, int hilos) {
		this.id = id;
		this.content = content;
		this.hilos = hilos;
	}

	
	public long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}


	public int getHilos() {
		return hilos;
	}
	
}