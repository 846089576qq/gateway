package com.gateway.payment.service.util;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

public class IDGenerator {

	private static int ServerNum = 0;
	private static final String pid = "RZF";

	private static InnerIDGenerator _innerIDGenerator = null;

	static {
		_innerIDGenerator = new InnerIDGenerator((byte) ServerNum);
	}

	public static String newPid() {
		return pid + _innerIDGenerator.newDataId();
	}

	private static class InnerIDGenerator {

		private byte _serverNum;

		public InnerIDGenerator(byte serverNum) {
			if (serverNum >= 16) {
				throw new RuntimeException("ServerNum must >= 0 and < 16");
			}

			_serverNum = serverNum;
		}

		private Object _LockForDataId = new Object();

		private volatile long _lastTime = 0;

		private final static long _timeZero = (new GregorianCalendar(2000, 0, 1, 0, 0, 0)).getTimeInMillis();

		public String newDataId() {
			long curTime;

			synchronized (_LockForDataId) {
				curTime = System.currentTimeMillis();
				if (curTime == _lastTime) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					curTime = System.currentTimeMillis();
				}

				_lastTime = curTime;
			}

			long newId = ((curTime - _timeZero) << 4) & 0xfffffffffffffff0L | _serverNum;

			return toHexString(newId).substring(10);
		}

		public static String toHexString(long val) {
			StringBuilder hexStr = new StringBuilder(Long.toHexString(val));

			for (int i = hexStr.length(); i < 16; ++i) {
				hexStr.insert(0, '0');
			}

			return hexStr.toString();
		}
	}

	public static void main(String[] args) {
		Set<String> s = new HashSet<String>();
		for (int i = 0; i < 100; i++) {
			s.add(IDGenerator.newPid());
		}
		System.out.println(s.size());
	}

}
