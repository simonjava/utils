package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Main {

	String base = "/Users/chengsimin/dev/walilive/walilive的副本/app/src/main/";

	String from = "java/";
	String to = "java-mitalk/";

	ArrayList<File> list = new ArrayList<File>();

	public void find(File file, String name) {
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				find(child, name);
			}
		} else {
			String fn = file.getName();
			if (fn.equals(name + ".java")) {
				System.out.println("命中 path:" + file.getPath());
				list.add(file);
			}
		}
	}

	static class Holder {
		public String packName = "";
		public String className = "";

		@Override
		public String toString() {
			return "Holder [packName=" + packName + ", className=" + className + "]";
		}

	}

	public Holder getSubName(String path) {
		String basePath = "/Users/chengsimin/dev/walilive/walilive的副本/app/src/main/java/";

		String subPath = path.substring(basePath.length());
		String a[] = subPath.split("/");

		Holder holder = new Holder();

		for (int i = 0; i < a.length; i++) {
			if (!a[i].endsWith(".java")) {
				holder.packName += "." + a[i];
			} else {
				holder.className = a[i];
			}
		}
		holder.packName = holder.packName.substring(1);
		holder.packName = holder.packName.replace("com.wali.live", "com.xiaomi.channel");
		holder.packName = holder.packName.replace("com.mi.live", "com.xiaomi.channel");
		return holder;
	}

	public String getPath(Holder holder) {
		String[] b = holder.packName.split("\\.");
		String r = base + to;
		for (String b1 : b) {
			r = r + b1 + "/";
		}
		r += holder.className;
		return r;
	}

	public void copy(String fromFile, String toFile, String packName) throws IOException {
		Scanner sc = new Scanner(new FileInputStream(fromFile));
		File toF = new File(toFile);
		if (!toF.getParentFile().exists()) {
			toF.getParentFile().mkdirs();
		}

		if (toF.exists()) {
			System.err.println("文件已经存在");
			return;
		} else {
			toF.createNewFile();
		}
		FileWriter fos = new FileWriter(toF);
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			System.out.println(line);
			if (line.startsWith("package com.")) {
				line = "package " + packName + ";";
			}
			line += "\r\n";
			fos.write(line);
		}
		sc.close();
		fos.close();
	}

	public static double log(double value, double base) {
		return Math.log(value) / Math.log(base);
	}

	private static double myGetPower(double base, int exp) {
		if (exp == 0)
			return 1;
		if (exp % 2 == 0)
			return myGetPower((base * base), exp / 2);
		else
			return base * myGetPower((base * base), exp / 2);
	}

	public static void test1() {
		// for (int i = 1; i < 5000; i++) {
		// double x = i / 20.0;
		// double y = log(x, 0.1);
		// System.out.println("x:" + x + " y:" + y);
		// }
		// y = a sin(bx+c)+d;
		float sp = 100,ep =300;
		int t1 = 100;
		float chaochu = 60;
		double c = Math.PI / 2;
		double b = Math.PI / t1;
		double a = (ep + chaochu - sp) / 2;
		double d = ep + chaochu - a;
		
		float nowP = 0;
		
		float zhishu = 1f;
		int zhishuCount = 0;
		int ts = 0;
		while (ts < 1000) {
			ts += 10;
			if (ts < t1) {
				nowP = (float) (a * Math.sin(b * ts - c) + d);
			}
			System.out.println(nowP);
		}
	}

	public static void move(double sp, double ep) {
		double a = (ep + 80 - sp) / 22500;
		System.out.println("a:" + a);
		for (int i = 0; i < 150; i++) {
			double x = i;
			double y = -1 * a * (x - 150) * (x - 150) + ep + 80;
			System.out.println("x:" + x + " y:" + y);
		}
		for (int i = 150; i < 500; i++) {
			double x = i;
			double y = 80 * myGetPower(0.8, (int) x - 150) + ep;
			System.out.println("x:" + x + " y:" + y);
		}
	}

	public static void main(String[] args) {
		if (true) {
			test1();
			return;
		}
		final HashMap<Integer, String> map = new HashMap<>();
		final Random random = new Random();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					int a = random.nextInt() % 5;
					map.put(a, String.valueOf(a));
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					int a = random.nextInt() % 5;
					System.out.println(a + "-->" + map.get(a));
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					int a = random.nextInt() % 5;
					map.put(a, String.valueOf(a - 1));
				}
			}
		}).start();
	}

	// /Users/chengsimin/dev/walilive/walilive的副本/app/src/main/java/com/wali/live/pushtimestatistics/PushStatisticsDispatcher.java

	private void run() {
		File file = new File(base + from);
		System.out.println(file.listFiles().length);
		Scanner sc = new Scanner(System.in);

		while (sc.hasNext()) {
			String na = sc.next();
			System.out.println(na);
			if (na.equals("1") || na.equals("0")) {
				if (na.equals("1")) {
					ArrayList listA = new ArrayList<>();
					listA.add(list.get(1));
					list = listA;
				} else {
					ArrayList listA = new ArrayList<>();
					listA.add(list.get(0));
					list = listA;
				}
			} else {
				list.clear();
				find(file, na);
			}

			if (list.size() == 1) {
				File fromfile = list.get(0);
				String fromPath = fromfile.getPath();

				Holder holder = getSubName(fromPath);
				System.out.println("holder:" + holder);

				String toPath = getPath(holder);
				System.out.println("toPath:" + toPath);

				try {
					copy(fromPath, toPath, holder.packName);
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("holder:" + holder);
			} else if (list.size() == 0) {
				System.out.println("没找到");
			} else {
				System.out.println("找到多个");
			}
		}
	}

}
