package com.catchmind.pro.util;

public class Catch_Protocol {
	//������������
	public static final int PROTOCOL_LOGIN = 1;
	public static final int PROTOCOL_JOIN = 2;
	public static final int PROTOCOL_IDFIND = 3;
	public static final int PROTOCOL_PWFIND = 4;
	
	//���� ��������
	public static final int PROTOCOL_WAITINGROOM_ACCEPT = 5;
	public static final int PROTOCOL_WAITINGROOM_MSG = 6;
	public static final int PROTOCOL_MAKEROOM = 7;
	public static final int PROTOCOL_ROOMREFRESH = 8;
	public static final int PROTOCOL_ROOMACCEPT = 9;
	
	//���ӷ� ��������
	public static final int PROTOCOL_MSG = 11;
	public static final int PROTOCOL_CHANGEAREA = 12;
	public static final int PROTOCOL_TIME = 13;
	public static final int PROTOCOL_QUIZANSWER = 14;
	public static final int PROTOCOL_TESTER = 15;

	//�׸� ��������
	public static final int PROTOCOL_DRAW = 101;
	
	//ä����������
	public static final int PROTOCOL_EXITROOM = 201;
	public static final int PROTOCOL_ROOMMEMBERFIIL = 202;
	public static final int PROTOCOL_USERINFO = 203;
	
	
	
	//----------���� 
	public static final int STATE_PASSWORD_EMPTY = 99;
}
