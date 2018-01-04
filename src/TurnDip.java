import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TurnDip {

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

	public static void run() {
		digui(new File("/Users/chengsimin/dev/walilive/walilive的副本"));
	}

	static void digui(File root) {

		for (File file : root.listFiles()) {
			String path = file.getPath();
			System.out.println("digui:" + path);
			if (file.isFile()) {
				if (path.endsWith("dimens.xml")) {
					read(path);
				}
//				if (path.endsWith(".xml")) {
//					read(path);
//				}
			} else {
				if (!path.contains("/build/")) {
					digui(file);
				}
			}
		}
	}

	private static void read(String filePath) {
		System.out.println("read:" + filePath);
		File file = new File(filePath);
		try {
			Scanner scan = new Scanner(file);
			StringBuilder sb = new StringBuilder();
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				String line2 = replace2(line);
				sb.append(line2).append("\r\n");
			}
			FileWriter fos = new FileWriter(file);
			fos.write(sb.toString());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static Pattern pattern = Pattern.compile(".*=\"(-?[0-9]+.?[0-9]*)(dp|dip)\"");
	static Pattern pattern2 = Pattern.compile(".*>(-?[0-9]+.?[0-9]*)(dp|dip)<");

	// static Pattern pattern = Pattern.compile(".*=");
	private static String replace(String line) {
		Matcher matcher = pattern.matcher(line);
		if (matcher.find()) {
			matcher.reset();
			while (matcher.find()) {
				String v1 = matcher.group(1);
				float v2 = Float.parseFloat(v1);
				int v3;
				if (v2 > 0) {
					v3 = (int) (v2 * 3 + 0.5f);
				} else {
					v3 = (int) (v2 * 3 - 0.5f);
				}
				String d;
				if (v2 > 0) {
					d = String.format("=\"@dimen/view_dimen_%s\"", v3);
				} else {
					d = String.format("=\"@dimen/view_negative_dimen_%s\"", -v3);
				}
				return line.replaceFirst("=\"(-?[0-9]+.?[0-9]*)(dp|dip)\"", d);

			}
		}
		return line;
	}

	private static String replace2(String line) {
		Matcher matcher = pattern2.matcher(line);
		if (line.contains("view_negative_dimen") || line.contains("view_dimen")) {
			return line;
		}
		if (matcher.find()) {
			matcher.reset();
			while (matcher.find()) {
				String v1 = matcher.group(1);
				float v2 = Float.parseFloat(v1);
				int v3;
				if (v2 > 0) {
					v3 = (int) (v2 * 3 + 0.5f);
				} else {
					v3 = (int) (v2 * 3 - 0.5f);
				}
				String d;
				if (v2 > 0) {
					d = String.format(">@dimen/view_dimen_%s<", v3);
				} else {
					d = String.format(">@dimen/view_negative_dimen_%s<", -v3);
				}
				return line.replaceFirst(">(-?[0-9]+.?[0-9]*)(dp|dip)<", d);

			}
		}
		return line;
	}

	public static void main(String[] args) {
		run();
	}
}
