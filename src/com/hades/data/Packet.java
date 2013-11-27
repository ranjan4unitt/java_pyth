package com.hades.data;

import java.math.BigInteger;

public class Packet {
	BigInteger recvTime;
	BigInteger sendTime;
	BigInteger seqnr;
	BigInteger recvTimeNsec;
	BigInteger sendTimeNsec;
	double delay;
	
	public Packet(BigInteger recvTime, BigInteger sendTime, BigInteger seqnr,
			BigInteger recvTimeNsec, BigInteger sendTimeNsec) {
		super();
		this.recvTime = recvTime;
		this.sendTime = sendTime;
		this.seqnr = seqnr;
		this.recvTimeNsec = recvTimeNsec;
		this.sendTimeNsec = sendTimeNsec;
		double recvTimeMilli = this.recvTime.doubleValue() * 1000 + this.recvTimeNsec.doubleValue() * .000001;
		double sendTimeMilli = this.sendTime.doubleValue() * 1000 + this.sendTimeNsec.doubleValue() * .000001;
		this.delay = recvTimeMilli - sendTimeMilli;
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
	
}
