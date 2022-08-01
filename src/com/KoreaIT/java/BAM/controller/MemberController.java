package com.KoreaIT.java.BAM.controller;


import java.util.List;
import java.util.Scanner;

import com.KoreaIT.java.BAM.container.Container;
import com.KoreaIT.java.BAM.dto.Member;
import com.KoreaIT.java.BAM.util.Util;

public class MemberController extends Controller {
	private Scanner sc;
	private List<Member> members;
	private String cmd;
	private String actionMethodName;

	public MemberController(Scanner sc) {
		this.sc = sc;

		members = Container.memberDao.members;
	}

	public void doAction(String cmd, String actionMethodName) {
		this.cmd = cmd;
		this.actionMethodName = actionMethodName;

		switch (actionMethodName) {
		case "join":
			doJoin();
			break;
		case "login":
			doLogin();
			break;
		case "logout":
			doLogout();
			break;
		case "profile":
			showProfile();
			break;
		default:
			System.out.println("�������� �ʴ� ��ɾ��Դϴ�");
			break;
		}
	}

	private void doLogout() {

		loginedMember = null;
		System.out.println("�α׾ƿ� �Ǿ����ϴ�");
	}

	private void doLogin() {

		System.out.printf("�α��� ���̵� : "); String loginId = sc.nextLine();
		String msg = "�������� �ʴ� ���̵� �Դϴ�.";

		System.out.printf("�α��� ��й�ȣ : "); String loginPw = sc.nextLine();
		

		Member member = getMemberByLoginId(loginId);

		if (member == null) {
			System.out.println("��ġ�ϴ� ȸ���� �����ϴ�");
			return;
		}

		if (member.loginPw.equals(loginPw) == false) {
			System.out.println("��й�ȣ�� �ٽ� �Է����ּ���");
			return;
		}

		loginedMember = member;
		System.out.printf("�α��� ����! %s�� ȯ���մϴ�.\n", loginedMember.name);

	}

	private void showProfile() {

		System.out.printf("�α��� ���̵� : %s\n", loginedMember.loginId);
		System.out.printf("�̸� : %s\n", loginedMember.name);
	}

	private void doJoin() {
		int id = Container.memberDao.getNewId();
		String regDate = Util.getNowDateStr();
		String loginId = null;

		while (true) {

			System.out.printf("�α��� ���̵� : ");
			loginId = sc.nextLine();

			if (isJoinableLoginId(loginId) == false) {
				System.out.printf("%s��(��) �̹� ������� ���̵��Դϴ�\n", loginId);
				continue;
			}
			break;
		}

		String loginPw = null;
		String loginPwConfirm = null;

		while (true) {

			System.out.printf("�α��� ��й�ȣ : ");
			loginPw = sc.nextLine();
			System.out.printf("�α��� ��й�ȣ Ȯ�� : ");
			loginPwConfirm = sc.nextLine();

			if (loginPw.equals(loginPwConfirm) == false) {
				System.out.println("��й�ȣ�� �ٽ� �Է����ּ���");
				continue;
			}
			break;
		}

		System.out.printf("�̸� : ");
		String name = sc.nextLine();

		Member member = new Member(id, regDate, loginId, loginPw, name);
		Container.memberDao.add(member);

		System.out.printf("%d�� ȸ���� ȯ���մϴ�\n", id);
	}

	private Member getMemberByLoginId(String loginId) {
		int index = getMemberIndexByLoginId(loginId);

		if (index == -1) {
			return null;
		}

		return members.get(index);
	}

	private boolean isJoinableLoginId(String loginId) {
		int index = getMemberIndexByLoginId(loginId);

		if (index == -1) {
			return true;
		}

		return false;
	}

	private int getMemberIndexByLoginId(String loginId) {
		int i = 0;
		for (Member member : members) {
			if (member.loginId.equals(loginId)) {
				return i;
			}
			i++;
		}

		return -1;
	}

	public void makeTestData() {
		System.out.println("�׽�Ʈ�� ���� ȸ�� �����͸� �����մϴ�.");

		Container.memberDao
				.add(new Member(Container.memberDao.getNewId(), Util.getNowDateStr(), "test1", "test1", "ȫ�浿"));
		Container.memberDao
				.add(new Member(Container.memberDao.getNewId(), Util.getNowDateStr(), "test2", "test2", "��ö��"));
		Container.memberDao
				.add(new Member(Container.memberDao.getNewId(), Util.getNowDateStr(), "test3", "test3", "�ڿ���"));
	}
}