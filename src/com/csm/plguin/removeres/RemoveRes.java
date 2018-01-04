package com.csm.plguin.removeres;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveRes {

	static HashSet<String> layoutSet = new HashSet<>();
	static HashSet<String> drawableSet = new HashSet<>();

	public static void main(String[] args) {

		Processor layoutProcessor = new Processor() {
			// .java
			// 案例1
			// int resourceId = isForRelease ? R.layout.release_file_picker_item
			// : R.layout.file_picker_item; // 不能贪婪匹配，一行多次匹配
			// 案例2
			// return R.layout.release_file_picker_item ;
			Pattern pattern = Pattern.compile(".*?layout.(\\w+)(,|\\)|;| |)");

			@Override
			public String process(String line) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					matcher.reset();
					while (matcher.find()) {
						String v1 = matcher.group(1);
						if (v1 != null) {
							layoutSet.add(v1);
							System.out.println("layout:" + v1);
						}
					}
				}
				return line;
			}
		};

		Processor drawableProcessor = new Processor() {
			// .java
			/**
			 * 案例1
			 *     private static final int[] mCategoryImages=new int[]{
            R.drawable.message_chat_file_icon_memory,
            R.drawable.message_chat_file_icon_sd
    };

			 */
			Pattern pattern = Pattern.compile(".*?drawable.(\\w+)(,|\\)|;|)");

			@Override
			public String process(String line) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					matcher.reset();
					while (matcher.find()) {
						String v1 = matcher.group(1);
						if (v1 != null) {
							drawableSet.add(v1);
							System.out.println("drawable:" + v1);
						}
					}
				}
				return line;
			}
		};

		Processor layoutProcessor2 = new Processor() {
			// .layout
			Pattern pattern = Pattern.compile(".*=\\s*\"@layout/(\\w+)\"");

			@Override
			public String process(String line) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					matcher.reset();
					while (matcher.find()) {
						String v1 = matcher.group(1);
						if (v1 != null) {
							layoutSet.add(v1);
							System.out.println("layout:" + v1);
						}
					}
				}
				return line;
			}
		};

		Processor drawableProcessor2 = new Processor() {
			// .layout
			Pattern pattern = Pattern.compile(".*=\\s*\"@drawable/(\\w+)\"");

			@Override
			public String process(String line) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					matcher.reset();
					while (matcher.find()) {
						String v1 = matcher.group(1);
						if (v1 != null) {
							drawableSet.add(v1);
							System.out.println("drawable:" + v1);
						}
					}
				}
				return line;
			}
		};

		Processor drawableProcessor3 = new Processor() {
			// .style
			Pattern pattern = Pattern.compile(".*@drawable/(\\w+)(<?)");

			@Override
			public String process(String line) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					matcher.reset();
					while (matcher.find()) {
						String v1 = matcher.group(1);
						if (v1 != null) {
							drawableSet.add(v1);
							System.out.println("drawable:" + v1);
						}
					}
				}
				return line;
			}
		};

		Processor drawableProcessor4 = new Processor() {
			Pattern pattern = Pattern.compile(".*=\\s*\"@drawable/(\\w+)(<?)");

			@Override
			public String process(String line) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					matcher.reset();
					while (matcher.find()) {
						String v1 = matcher.group(1);
						if (v1 != null) {
							drawableSet.add(v1);
							System.out.println("drawable:" + v1);
						}
					}
				}
				return line;
			}
		};

		/************/
		File rootFile = new File("/Users/chengsimin/dev/walilive/walilive的副本");

		if (false) {
			// hot_live_icon_bg
			processorList.clear();
			processorList.add(layoutProcessor);
			processorList.add(drawableProcessor);
			// 得到java中的layout和drawable了
			digui(rootFile, new FF() {

				@Override
				public boolean accept(String path) {
					if (path.endsWith("FileCategoryAdapter.java")) {
						return true;
					}
					return false;
				}
			});

			return;
		}

		System.out.println("step1 beigin");
		processorList.clear();
		processorList.add(layoutProcessor);
		processorList.add(drawableProcessor);
		// 得到java中的layout和drawable了
		digui(rootFile, new FF() {

			@Override
			public boolean accept(String path) {
				if (path.endsWith(".java")) {
					return true;
				}
				return false;
			}
		});
		System.out.println("step1 over");
		System.out.println("step2 beigin");
		processorList.clear();
		processorList.add(layoutProcessor2);
		processorList.add(drawableProcessor2);
		int size = 0;
		do {
			size = layoutSet.size();
			System.out.println("layoutSet.size:" + layoutSet.size());
			// 得到以上layout中drawable,因为layout中可能含有layout，粗鲁些递归直到尺寸不变
			digui(rootFile, new FF() {
				Pattern pattern = Pattern.compile(".*res/layout/(\\w+)\\.xml");

				@Override
				public boolean accept(String path) {

					if (path.contains("res/layout")) {
						// System.out.println(path + " is layout");
						Matcher matcher = pattern.matcher(path);
						if (matcher.find()) {
							matcher.reset();
							while (matcher.find()) {
								String v1 = matcher.group(1);
								// System.out.println("layout name:" + v1);
								if (v1 != null) {
									boolean has = layoutSet.contains(v1);
									if (has)
										System.out.println("has=" + has);
									return has;
								}
							}
						}
					}
					return false;
				}
			});
		} while (size != layoutSet.size());
		System.out.println("step2 over");
		// 得到所有styles中的drawable
		System.out.println("step3 beigin");
		processorList.clear();
		processorList.add(drawableProcessor3);
		digui(rootFile, new FF() {
			@Override
			public boolean accept(String path) {
				if (path.endsWith("/styles.xml")) {
					return true;
				}
				return false;
			}
		});
		System.out.println("step3 over");
		// 得到 drawable.xml 中的 drawable
		System.out.println("step4 begin");
		processorList.clear();
		processorList.add(drawableProcessor4);
		digui(rootFile, new FF() {
			Pattern pattern = Pattern.compile(".*/(\\w+)\\.xml");

			@Override
			public boolean accept(String path) {
				if (path.contains("/res/drawable")) {
					// System.out.println(path + " is layout");
					Matcher matcher = pattern.matcher(path);
					if (matcher.find()) {
						matcher.reset();
						while (matcher.find()) {
							String v1 = matcher.group(1);
							// System.out.println("layout name:" + v1);
							if (v1 != null) {
								boolean has = drawableSet.contains(v1);
								if (has)
									System.out.println("has=" + has);
								return has;
							}
						}
					}
				}
				return false;
			}
		});
		System.out.println("step4 over");
		// System.out.println("drawableSet:" + drawableSet);

		// 完整的 drawable 不在里面的都可以删除了
		System.out.println("step5 beigin");
		delete(rootFile, new FF() {
			Pattern pattern = Pattern.compile(".*/(\\w+)\\.(xml|png|webp|9\\.png|jpg|jepg|gif)");
			Pattern pattern2 = Pattern.compile(".*res/layout/(\\w+)\\.xml");

			@Override
			public boolean accept(String path) {
				// return true 就是该被删除的文件
				if (path.contains("/res/drawable")) {
					// System.out.println(path + " is layout");
					Matcher matcher = pattern.matcher(path);
					if (matcher.find()) {
						matcher.reset();
						while (matcher.find()) {
							String v1 = matcher.group(1);
							// System.out.println("layout name:" + v1);
							if (v1 != null) {
								boolean has = drawableSet.contains(v1);
								if (has) {
									return false;
								} else {
									return true;
								}
							}
						}
					}
					return true;
				}

				if (path.contains("res/layout")) {
					// System.out.println(path + " is layout");
					Matcher matcher = pattern2.matcher(path);
					if (matcher.find()) {
						matcher.reset();
						while (matcher.find()) {
							String v1 = matcher.group(1);
							// System.out.println("layout name:" + v1);
							if (v1 != null) {
								boolean has = layoutSet.contains(v1);
								if (has) {
									return false;
								} else {
									return true;
								}
							}
						}
					}
					return true;
				}
				return false;
			}
		});
		System.out.println("step5 over");
	}

	static void digui(File root, FF f) {

		for (File file : root.listFiles()) {
			String path = file.getPath();
			// System.out.println("digui:" + path);
			if (file.isFile()) {
				if (f.accept(path)) {
					// 是java文件
					read(path);
				}
			} else {
				if (!path.contains("/build/")) {
					digui(file, f);
				}
			}
		}
	}

	static void delete(File root, FF f) {

		for (File file : root.listFiles()) {
			String path = file.getPath();
			// System.out.println("digui:" + path);
			if (file.isFile()) {
				if (f.accept(path)) {
					// 是java文件
					System.out.println("删除" + path);
					file.delete();
				}
			} else {
				if (!path.contains("/build/")) {
					delete(file, f);
				}
			}
		}
	}

	private static void read(String filePath) {
		File file = new File(filePath);
		try {
			Scanner scan = new Scanner(file);
			StringBuilder sb = new StringBuilder();
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				for (Processor p : processorList) {
					p.process(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static ArrayList<Processor> processorList = new ArrayList<Processor>();

	interface Processor {
		String process(String path);
	}

	interface FF {
		boolean accept(String path);
	}
}
