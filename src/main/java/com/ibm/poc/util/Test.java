package com.ibm.poc.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		String fPath = "C:/bcp-testrelt-saveasdraft/";
		File folder = new File(fPath);
		List<String> list = new ArrayList();
		if (folder.exists()) {
			for (File file : folder.listFiles()) {
				if (file.isFile()) {
					list.add(file.getName().split("-")[2].split(".txt")[0]);
				}
			}
		}
		System.out.println(list);
	}

}
