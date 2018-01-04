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

public class DeleteLayout {
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
			if (step == 1) {
				getLayoutString(file);
			} else if (step == 2) {
				String name = file.getName();
				name = name.substring(0, name.length() - 4);
				if (list.contains(name)) {
					getLayoutString(file);
				}
			}
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
				list.add(proc(line));
			}
		}
		sc.close();
	}

	private String proc(String line) {
//		if(line.contains("activity_block_list")){
//			System.out.println(line);
//			return "";
//		}
		switch (step) {
		case 1: {
			int b = line.indexOf("R.layout");
			int e = -1;
			for (int i = b; i < line.length(); i++) {
				if (line.charAt(i) == ',' || line.charAt(i) == ')' || line.charAt(i) == ';') {
					e = i;
					break;
				}
			}
			if (e != -1) {
				line = line.substring(b + "R.layout.".length(), e);
			}
			return line;
		}
		case 2: {
			int b = line.indexOf("@layout/");
			int e = -1;
			for (int i = b; i < line.length(); i++) {
				char c = line.charAt(i);
				if (c == '"' || c == '\'') {
					e = i;
					break;
				}
			}
			if (e != -1) {
				line = line.substring(b + "@layout/".length(), e);
			}
			return line;
		}
		default:
			break;
		}
		return null;
	}

	private boolean judge(String line) {
		if (line.contains("R.layout") || line.contains("@layout")) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		new DeleteLayout().run();
	}

	// /Users/chengsimin/dev/walilive/walilive的副本/app/src/main/java/com/wali/live/pushtimestatistics/PushStatisticsDispatcher.java

	private void run() {
		step = 1;
		find(new File(base + to));
		for (String s : list) {
			System.out.println(s);
		}
		
		int i = 0;
		while (true) {
			i++;
			System.out.println("开始步骤2 " + list.size());
			int beginsize = list.size();
			step = 2;
			find(new File(base + layout));
			for (String s : list) {
				System.out.println(s);
			}
			if(list.size()==beginsize){
				System.out.println("工进行了"+i+"轮次");
				break;
			}
		}
		System.out.println("开始步骤3 " + list.size());

		 delete(new File(base+layout));
	}

	private void delete(File file) {
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				delete(child);
			}
		} else {
			String name = file.getName();
			name = name.substring(0, name.length() - 4);
			System.out.println(name);
			if (!list.contains(name)) {
				System.err.println("delete " + name);
				file.delete();
			} else {

			}
		}
	}
}
