package com.catchmind.pro.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.catchmind.pro.dao.UserDAO;
import com.catchmind.pro.gui.Window_ServerInfo;
import com.catchmind.pro.vo.UserVO;

/*
 * ���� ������Ʈ ��¥ :4�� 28�� 
 */
public class Server_Launcher {
	private ServerSocket ss;
	private Socket soc;
	private PrintWriter pw;
	private BufferedReader br;
	private UserDAO dao;

	// ���� ���ӷ�
	public Server_Launcher() throws Exception {
		// chm = new ConcurrentHashMap<String, Socket>();
		dao = UserDAO.getUserDAO();
		server_start();
	}

	private void server_start() {
		try {
			ss = new ServerSocket(20000);
		         new Window_ServerInfo(ss.getLocalPort(),InetAddress.getLocalHost().getHostAddress());
		        // ss.setSoTimeout(50000);
		         
		        
			if (ss != null) {
				while (true) {
					soc = ss.accept();
					ChatClient cc = new ChatClient(soc);
					cc.start();
					System.out.println("������ �����մϴ�.\n");
				}
			}
		} catch (IOException e) {
			System.out.println("������ �̹� ������Դϴ�.\n");
			System.exit(0);
		}
	}

	class ChatClient extends Thread {
		private Socket soc = null;
		private UserManagement um = new UserManagement();
		private UserVO user = null;

		public ChatClient(Socket soc) {
			this.soc = soc;
		}

		public void run() {
			while (true) {
				try {
					br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
					// br.readLine();
					String msg = br.readLine();
					System.out.println(soc + " ����");
					System.out.println(msg);

					if (msg == null)
						break;
					if (msg.charAt(0) == '!') { // �α���
						UserVO user = um.login(soc, msg);
						if (user != null) {
							this.user = user;
							Server_ChatterAccepter chatterAccepter = new Server_ChatterAccepter(soc, user);
							Server_WaitingRoom.getWaitingRoom().addChatter(chatterAccepter);
							break;
						}
					} else if (msg.charAt(0) == '*') { // ȸ������
						um.join(msg);
					} else if (msg.charAt(0) == '#') { // ���̵� ã��
						um.idfind(msg);
					} else if (msg.charAt(0) == '$') { // �н����� ã��
						um.pwfind(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("����� �α���ȭ�鿡�� ����");
					return;
				}
			}
		}
	}

	class UserManagement {
		public UserVO login(Socket soc, String msg) throws Exception{
			UserVO vo = new UserVO();
			System.out.println("[login]");
			String msg2 = msg.substring(1);
			String msgArr1[] = msg2.split("!", 2);
			String id = msgArr1[0];
			String password = msgArr1[1];
			System.out.println("dao ������");
			vo.setUser_id(id);
			vo.setUser_password(password);
			System.out.println("dao ������2");
			UserVO user = dao.getUser(vo);
			System.out.println("[������ dao Ȯ��]");
			System.out.println(user);
			if (user != null) {
				pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())));
				System.out.println("���� �˻���");
				if (Server_WaitingRoom.getWaitingRoom().getChatters() != null
						&& !Server_WaitingRoom.getWaitingRoom().getChatters().isEmpty()) {
					for (Server_ChatterAccepter waitingChatter : Server_WaitingRoom.getWaitingRoom().getChatters()) {
						if (waitingChatter.getUser().getSeq() == user.getSeq()) {
							System.out.println("���� �޽�����������");
							pw.println("@accepting");
							pw.flush();
							System.out.println("���� �޽��������� �ٳ���");
							return null;
						}
					}
				}
				System.out.println("���ӹ� �˻���");
				if (Server_WaitingRoom.getWaitingRoom().getRoom_list() != null
						&& !Server_WaitingRoom.getWaitingRoom().getRoom_list().isEmpty())
					for (Server_GameRoom room : Server_WaitingRoom.getWaitingRoom().getRoom_list()) {

						if (room.getChatters() != null && !room.getChatters().isEmpty()) {
							for (Server_ChatterAccepter chatter : room.getChatters()) {
								if (chatter.getUser().getSeq() == user.getSeq()) {
									pw.println("@accepting");
									pw.flush();
									return null;
								}
							}
						}

					}

				pw.println("@loginOK"); // Ŭ���̾�Ʈ�� �α��� ���� ��ȣ
				pw.flush();
				return user;
			} else {
				pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())));
				pw.println("@loginFail"); // Ŭ���̾�Ʈ�� �α��� ���� ��ȣ
				pw.flush();
				return null;
			}
		} // �α���

		public void join(String msg) {
			try {
				UserVO vo = new UserVO();
				System.out.println("join()");
				String msg2 = msg.substring(1);
				System.out.println(msg2);
				String msgArr2[] = msg2.split("\\*", 9);

				String id = msgArr2[0];
				String password = msgArr2[1];
				String email = msgArr2[2];
				String nickname = msgArr2[3];
				String name = msgArr2[4];
				int year = Integer.parseInt(msgArr2[5]);
				int month = Integer.parseInt(msgArr2[6]);
				int day = Integer.parseInt(msgArr2[7]);
				int user_pokemon = Integer.parseInt(msgArr2[8]);

				System.out.println("������ �߶���");

				vo.setUser_id(id);
				vo.setUser_password(password);
				vo.setEmail(email);
				vo.setNickName(nickname);
				vo.setPersonName(name);
				vo.setBirth_year(year);
				vo.setBirth_month(month);
				vo.setBirth_day(day);
				vo.setUser_pokemon(user_pokemon);
				System.out.println("dao �ֱ��� : " + vo);
				boolean ok = dao.insertUser(vo);
				if (ok) {
					pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())));
					pw.println("@joinOK"); // Ŭ���̾�Ʈ�� �α��� ���� ��ȣ
					pw.flush();
					// enter(id, soc); // ����޽��� ����
					Thread.sleep(5000);
				} else {
					pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())));
					pw.println("@joinFail"); // Ŭ���̾�Ʈ�� �α��� ���� ��ȣ
					pw.flush();
					Thread.sleep(5000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} // ȸ������

		public void idfind(String msg) {
			try {
				UserVO vo = new UserVO();
				String msg2 = msg.substring(1);
				String msgArr3[] = msg2.split("#", 5);
				String email = msgArr3[0];
				String name = msgArr3[1];
				int year = Integer.parseInt(msgArr3[2]);
				int month = Integer.parseInt(msgArr3[3]);
				int day = Integer.parseInt(msgArr3[4]);

				vo.setEmail(email);
				vo.setPersonName(name);
				vo.setBirth_year(year);
				vo.setBirth_month(month);
				vo.setBirth_day(day);
				String response = dao.idFind(vo);

				if (response != null) {
					pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())));
					pw.println(response); // Ŭ���̾�Ʈ�� �α��� ���� ��ȣ
					pw.flush();
					// enter(id, soc); // ����޽��� ����

				} else {
					pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())));
					pw.println("@idFindFail"); // Ŭ���̾�Ʈ�� �α��� ���� ��ȣ
					pw.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} // ���̵� ã��

		public void pwfind(String msg) {
			try {
				UserVO vo = new UserVO();
				String msg2 = msg.substring(1);
				String msgArr4[] = msg2.split("\\$", 5);
				String id = msgArr4[0];
				String name = msgArr4[1];
				int year = Integer.parseInt(msgArr4[2]);
				int month = Integer.parseInt(msgArr4[3]);
				int day = Integer.parseInt(msgArr4[4]);

				vo.setUser_id(id);
				vo.setPersonName(name);
				vo.setBirth_year(year);
				vo.setBirth_month(month);
				vo.setBirth_day(day);

				String response = dao.pwFind(vo);
				System.out.println("response : " + response);

				if (response.equals("@pwFindOK")) {
					pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())));
					pw.println(response); // Ŭ���̾�Ʈ�� �α��� ���� ��ȣ
					pw.flush();
					// enter(id, soc); // ����޽��� ����

				} else {
					pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(soc.getOutputStream())));
					pw.println("@pwFindFail"); // Ŭ���̾�Ʈ�� �α��� ���� ��ȣ
					pw.flush();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) throws Exception {
		new Server_Launcher();
	}
}