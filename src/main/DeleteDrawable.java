package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import main.Main.Holder;

public class DeleteDrawable {
	String base = "/Users/chengsimin/dev/walilive/walilive的副本/app/src/main/";

	String from = "java/";
	String to = "java-mitalk/";
	String layout = "res/layout";

	HashSet<String> list = new HashSet<String>();
	int step = 0;

	public void find(File file) {
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				find(child);
			}
		} else {
			getLayoutString(file);
		}
	}

	private void getLayoutString(File file) {
		Scanner sc = null;
		try {
			sc = new Scanner(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (judge(line)) {
				proc(line);
			}
		}
		sc.close();
	}

	private void proc(String line) {
		switch (step) {
		case 1: {
			int b = line.indexOf("R.drawable");
			int b2 = line.lastIndexOf("R.drawable");
			System.out.println(b+" "+b2+" "+line);
			if (b == b2) {
				int e = line.length();
				for (int i = b; i < line.length(); i++) {
					if (line.charAt(i) == '}' || line.charAt(i) == ',' || line.charAt(i) == ')'
							|| line.charAt(i) == ';') {
						e = i;
						break;
					}
				}
				if (e != -1) {
					line = line.substring(b + "R.drawable.".length(), e);
				}
				list.add(line);
			} else {
				for (String ss : line.split("[,:]")) {
					proc(ss);
				}
			}
			return;
		}
		case 3:
		case 2: {
			int b = line.indexOf("@drawable/");
			int e = line.length();
			for (int i = b; i < line.length(); i++) {
				char c = line.charAt(i);
				if (c == '"' || c == '\'' || c == '<') {
					e = i;
					break;
				}
			}
			if (e != -1) {
				line = line.substring(b + "@drawable/".length(), e);
			}
			list.add(line);
			return;
		}
		default:
			break;
		}
		return;
	}

	private boolean judge(String line) {
		switch (step) {
		case 1: {
			if (line.contains("R.drawable")) {
				return true;
			}
		}
		case 3:
		case 2: {
			if (line.contains("@drawable")) {
				return true;
			}
		}
		default:
			break;
		}
		return false;
	}

	public static void main(String[] args) {
		new DeleteDrawable().run();
	}

	// /Users/chengsimin/dev/walilive/walilive的副本/app/src/main/java/com/wali/live/pushtimestatistics/PushStatisticsDispatcher.java

	private void run() {
		step = 1;
		find(new File(base + to));
		for (String s : list) {
			System.out.println(s);
		}

		System.out.println("开始步骤2 " + list.size());
		step = 2;
		find(new File(base + layout));
		for (String s : list) {
			System.out.println(s);
		}
		System.out.println("开始步骤3 " + list.size());
		find(new File(base + "res/values/styles.xml"));
		step = 3;
		int i = 0;
		while (true) {
			i++;
			System.out.println("开始步骤3 " + list.size());
			int beginsize = list.size();
			step = 3;
			find(new File(base + "res/drawable"));
			for (String s : list) {
				System.out.println(s);
			}
			if (list.size() == beginsize) {
				System.out.println("工进行了" + i + "轮次");
				break;
			}
		}

		delete(new File(base + "res"));
	}

	private void delete(File file) {
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				delete(child);
			}
		} else {
			if (!file.getParentFile().getName().contains("drawable")) {
				return;
			}
			String name = file.getName();
			if (name.endsWith("png") || name.endsWith("jpg") || name.endsWith("webp")) {
				int e = name.indexOf(".");
				name = name.substring(0, e);
				System.out.println(name);
				if (!list.contains(name)) {
					System.err.println("delete " + name);
					file.delete();
				}
			}
		}
	}
}
