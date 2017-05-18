package com.catchmind.pro.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.catchmind.pro.gui.Window_Login;
import com.catchmind.pro.gui.Window_Room;
import com.catchmind.pro.gui.Window_UserSearch;

/*
 * ���� ������Ʈ ��¥ :4�� 28��
 */
public class Client_Launcher {

	private InetAddress ia;
	private Socket soc;
	private PrintWriter pw;
	private BufferedReader br;

	private Window_Login login_frame;
	
	private Client_Telecom room_protocol;
	public Client_Launcher() {
		login_frame = new Window_Login(this);
		login_frame.setVisible(true);

		try {
			ia = InetAddress.getByName("localhost");
			soc = new Socket(ia, 20000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Thread th = new Thread(this);
	}
	public Socket getSoc() {
		return soc;
	}
	public void setSoc(Socket soc) {
		this.soc = soc;
	}
	public Window_Login getWindow_Login() {
		return login_frame;
	}
	public void setWindow_Login(Window_Login window_Login) {
		this.login_frame = window_Login;
	}

	public void login(String id, String password) {// �α���
		try {
			br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			pw = new PrintWriter(soc.getOutputStream());
			System.out.println("id : " + id + " , pw : " + password);
			pw.println("!" + id + "!" + password);
			pw.flush();
			String msg = br.readLine();
			System.out.println("msg : ");
			System.out.println(msg);
			if (msg != null && msg.equals("@loginOK")) {
				login_frame.dispose();
				JOptionPane.showMessageDialog(null, "�α��� ����!!");
				this.room_protocol = new Client_Telecom(this);
			}else if(msg != null && msg.equals("@accepting")){
				System.out.println("[������]");
				JOptionPane.showMessageDialog(null, "�̹� �������� �����Դϴ�.!!");
			}else {
				JOptionPane.showMessageDialog(null, "�α��ν��� !!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void join(String id, String password, String email, String nickname, String personalname, String year,
			String month, String day, int user_pokemon) {// ȸ������
		try {
			br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			pw = new PrintWriter(soc.getOutputStream());
			System.out.println("*" + id + "*" + password + "*" + email + "*" + nickname + "*" + personalname + "*"
					+ year + "*" + month + "*" + day);
			pw.println("*" + id + "*" + password + "*" + email + "*" + nickname + "*" + personalname + "*" + year + "*"
					+ month + "*" + day + "*" + user_pokemon );
			pw.flush();
			String msg = br.readLine();
			System.out.println("msg : ");
			System.out.println(msg);
			if (msg != null && msg.equals("@joinOK")) {
				JOptionPane.showMessageDialog(null, "ȸ������ ����!!");
				login_frame.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(null, "ȸ������ ���� !!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void idfind(String email, String personalname, String year, String month, String day,
			Window_UserSearch search_frame) {// ���̵�ã��
		try {
			br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			pw = new PrintWriter(soc.getOutputStream());
			System.out.println("#" + email + "#" + personalname + "#" + year + "#" + month + "#" + day);

			pw.println("#" + email + "#" + personalname + "#" + year + "#" + month + "#" + day);
			pw.flush();

			String msg = br.readLine();
			System.out.println("msg : ");
			System.out.println(msg);
			if (msg != null && msg.startsWith("@id?")) {
				String usersStr = msg.substring(4);
				String[] userIds = usersStr.split("\\?");
				String[] hiddenId = new String[userIds.length];
				String resultMsg = "";
				for (int j = 0 ; j < userIds.length ; j++) {
					hiddenId[j] = "";
					for (int i = 0; i < userIds[j].length(); i++) {
						if (i > 2 && i < userIds[j].length() - 2) {
							System.out.println("����");
							hiddenId[j] += "*";
						} else {
							System.out.println("�߰�");
							hiddenId[j] += userIds[j].charAt(i);
						}
					}
					resultMsg += hiddenId[j]+"  ";
				}
				JOptionPane.showMessageDialog(null, "�ش�Ǵ� ȸ���� ���̵�� :" + resultMsg + " �Դϴ�.");
				login_frame.setVisible(true);
				search_frame.dispose();
			} else {
				JOptionPane.showMessageDialog(null, "�Է��� ������ ��ġ�ϴ� ȸ���� �������� �ʽ��ϴ�.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void pwfind(String id, String personalname, String year, String month, String day, Window_UserSearch search_frame) {// ��й�ȣã��
		try {
			br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			pw = new PrintWriter(soc.getOutputStream());
			System.out.println("$" + id + "$" + personalname + "$" + year + "$" + month + "$" + day);
			
			pw.println("$" + id + "$" + personalname + "$" + year + "$" + month + "$" + day);
			pw.flush();
			
			String msg = br.readLine();
			System.out.println("msg : ");
			System.out.println(msg);
			if (msg != null && msg.equals("@pwFindOK")) {
				JOptionPane.showMessageDialog(null, "�ش������� ��ϵǾ��ִ� �̸��Ϸ� �ӽú�й�ȣ�� �����߽��ϴ�.");
				login_frame.setVisible(true);
				search_frame.dispose();
			} else {
				JOptionPane.showMessageDialog(null, "�Է��� ������ ��ġ�ϴ� ȸ���� �������� �ʽ��ϴ�.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//------------------------------�׸�ȭ��------------------------
	
	public static void main(String[] args) {
		new Client_Launcher();
	}
}