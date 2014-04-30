package com.hades.data;

import java.math.BigInteger;

public class Packet {
	BigInteger recvTime;
	BigInteger sendTime;
	BigInteger seqnr;
	BigInteger recvTimeNsec;
	BigInteger sendTimeNsec;
	double delay;
	double recvTimeMilli;
	double sendTimeMilli;
	
	public Packet(BigInteger recvTime, BigInteger sendTime, BigInteger seqnr,
			BigInteger recvTimeNsec, BigInteger sendTimeNsec, Double delay) {
		super();
		this.recvTime = recvTime;
		this.sendTime = sendTime;
		this.seqnr = seqnr;
		this.recvTimeNsec = recvTimeNsec;
		this.sendTimeNsec = sendTimeNsec;
		this.recvTimeMilli = this.recvTime.doubleValue() * 1000 + this.recvTimeNsec.doubleValue() * .000001;
		this.sendTimeMilli = this.sendTime.doubleValue() * 1000 + this.sendTimeNsec.doubleValue() * .000001;
		if(delay == null )this.delay = this.recvTimeMilli - this.sendTimeMilli;
		else this.delay = delay;
	}
	public BigInteger getRecvTime() {
		return recvTime;
	}
	public BigInteger getSendTime() {
		return sendTime;
	}
	public BigInteger getSeqnr() {
		return seqnr;
	}
	public BigInteger getRecvTimeNsec() {
		return recvTimeNsec;
	}
	public BigInteger getSendTimeNsec() {
		return sendTimeNsec;
	}
	public double getDelay() {
		return delay;
	}
	public void setDelay(double delay){
		this.delay = delay;
	}
	public double getRecvTimeMilli() {
		return recvTimeMilli;
	}
	public double getSendTimeMilli() {
		return sendTimeMilli;
	}
	
}
