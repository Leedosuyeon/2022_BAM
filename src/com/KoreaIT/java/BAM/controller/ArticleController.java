package com.KoreaIT.java.BAM.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.KoreaIT.java.BAM.container.Container;
import com.KoreaIT.java.BAM.dto.Article;
import com.KoreaIT.java.BAM.dto.Member;
import com.KoreaIT.java.BAM.util.Util;

public class ArticleController extends Controller {
	private Scanner sc;
	private List<Article> articles;
	private String cmd;
	private String actionMethodName;

	public ArticleController(Scanner sc) {
		this.sc = sc;

		articles = Container.articleDao.articles;
	}

	public void doAction(String cmd, String actionMethodName) {
		this.cmd = cmd;
		this.actionMethodName = actionMethodName;

		switch (actionMethodName) {
		case "list":
			showList();
			break;
		case "write":
			doWrite();
			break;
		case "detail":
			showDetail();
			break;
		case "modify":
			doModify();
			break;
		case "delete":
			doDelete();
			break;
		default:
			System.out.println("�������� �ʴ� ��ɾ��Դϴ�");
			break;
		}
	}

	private void doWrite() {
		int id = Container.articleDao.getNewId();
		String regDate = Util.getNowDateStr();
		System.out.printf("���� : ");
		String title = sc.nextLine();
		System.out.printf("���� : ");
		String body = sc.nextLine();

		Article article = new Article(id, regDate, loginedMember.id, title, body);
		Container.articleDao.add(article);

		System.out.printf("%d�� ���� �����Ǿ����ϴ�\n", id);

	}

	private void showList() {
		if (articles.size() == 0) {
			System.out.println("�Խù��� �����ϴ�");
			return;
		}

		String searchKeyword = cmd.substring("article list".length()).trim();

		System.out.printf("�˻��� : %s\n", searchKeyword);

		List<Article> forPrintArticles = articles;

		if (searchKeyword.length() > 0) {
			forPrintArticles = new ArrayList<>();

			for (Article article : articles) {
				if (article.title.contains(searchKeyword)) {
					forPrintArticles.add(article);
				}
			}

			if (forPrintArticles.size() == 0) {
				System.out.println("�˻� ����� �����ϴ�");
				return;
			}
		}

		System.out.printf("��ȣ    |   ����     |     %7s        |    �ۼ���  |   ��ȸ\n", "��¥");
		for (int i = forPrintArticles.size() - 1; i >= 0; i--) {
			Article article = forPrintArticles.get(i);

			String writerName = null;

			List<Member> members = Container.memberDao.members;

			for (Member member : members) {
				if (article.memberId == member.id) {
					writerName = member.name;
					break;
				}
			}

			System.out.printf("%7d | %6s   | %5s  |   %7s  | %5d\n", article.id, article.title, article.regDate,
					writerName, article.hit);
		}

	}

	private void showDetail() {
		String[] cmdBits = cmd.split(" ");

		if (cmdBits.length == 2) {
			System.out.println("��ɾ Ȯ�����ּ���");
			return;
		}

		int id = Integer.parseInt(cmdBits[2]);

		Article foundArticle = getArticleById(id);

		if (foundArticle == null) {
			System.out.printf("%d�� �Խù��� �����ϴ�\n", id);
			return;
		}

		foundArticle.increaseHit();

		System.out.printf("��ȣ : %d\n", foundArticle.id);
		System.out.printf("��¥ : %s\n", foundArticle.regDate);
		System.out.printf("���� : %s\n", foundArticle.title);
		System.out.printf("���� : %s\n", foundArticle.body);
		System.out.printf("�ۼ��� : %s\n", foundArticle.memberId);
		System.out.printf("��ȸ : %d\n", foundArticle.hit);

	}

	private void doModify() {
		String[] cmdBits = cmd.split(" ");

		if (cmdBits.length == 2) {
			System.out.println("��ɾ Ȯ�����ּ���");
			return;
		}

		int id = Integer.parseInt(cmdBits[2]);

		Article foundArticle = getArticleById(id);

		if (foundArticle == null) {
			System.out.printf("%d�� �Խù��� �����ϴ�\n", id);
			return;
		}

		if (foundArticle.memberId != loginedMember.id) {
			System.out.printf("������ �����ϴ�\n");
			return;
		}

		System.out.printf("���� : ");
		String title = sc.nextLine();
		System.out.printf("���� : ");
		String body = sc.nextLine();

		foundArticle.title = title;
		foundArticle.body = body;

		System.out.printf("%d�� �Խù��� �����߽��ϴ�\n", id);

	}

	private void doDelete() {
		String[] cmdBits = cmd.split(" ");

		if (cmdBits.length == 2) {
			System.out.println("��ɾ Ȯ�����ּ���");
			return;
		}

		int id = Integer.parseInt(cmdBits[2]);

		Article foundArticle = getArticleById(id);

		if (foundArticle == null) {
			System.out.printf("%d�� �Խù��� �����ϴ�\n", id);
			return;
		}

		if (foundArticle.memberId != loginedMember.id) {
			System.out.printf("������ �����ϴ�\n");
			return;
		}

		articles.remove(foundArticle);
		System.out.printf("%d�� �Խù��� �����߽��ϴ�\n", id);

	}

	private int getArticleIndexById(int id) {
		int i = 0;
		for (Article article : articles) {

			if (article.id == id) {
				return i;
			}
			i++;
		}
		return -1;
	}

	private Article getArticleById(int id) {
		int index = getArticleIndexById(id);

		if (index != -1) {
			return articles.get(index);
		}

		return null;
	}

	public void makeTestData() {
		System.out.println("�׽�Ʈ�� ���� �Խù� �����͸� �����մϴ�.");

		Container.articleDao
				.add(new Article(Container.articleDao.getNewId(), Util.getNowDateStr(), 1, "����1", "����1", 11));
		Container.articleDao
				.add(new Article(Container.articleDao.getNewId(), Util.getNowDateStr(), 2, "����2", "����2", 22));
		Container.articleDao
				.add(new Article(Container.articleDao.getNewId(), Util.getNowDateStr(), 2, "����3", "����3", 33));
	}

}