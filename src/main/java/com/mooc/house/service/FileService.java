package com.mooc.house.service;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.io.Files;

@Service
public class FileService {
	@Value("${file.path}")
	private String filePath;

	public List<String> getImgPath(List<MultipartFile> files) {
		List<String> paths = Lists.newArrayList();
		files.forEach(file -> {
			// 首先判断上传的文件是否为空
			File localFile = null;
			if (!files.isEmpty()) {
				try {
					System.out.println("file-->" + file);
					System.out.println("filePath-->" + filePath);
					localFile = saveToLocal(file, filePath);
					System.out.println("localFile-->" + localFile);
					String path = StringUtils.substringAfterLast(localFile.getAbsolutePath(), filePath);
					System.out.println("path---->" + path);
					paths.add(path);
				} catch (Exception e) {
					throw new IllegalArgumentException(e);
				}
			}
		});
		return paths;
	}

	public static String getResourcePath() {
		File file = new File(".");
		String absolutePath = file.getAbsolutePath();
		return absolutePath;
	}

	private File saveToLocal(MultipartFile file, String filePath2) {
		try {
			// 路径+时间戳+文件原始名
			File newFile = new File(filePath + "/" + Instant.now().getEpochSecond() +"/"+file.getName());
			System.out.println("newFile1----" + newFile);
			if (!newFile.exists()) {
				// 创建上级目录
				newFile.getParentFile().mkdirs();
				// 创建临时文件
				newFile.createNewFile();
			}
			System.out.println("newFile2----" + newFile);
			Files.write(file.getBytes(), newFile);
			System.out.println("newFile3----" + newFile);
			return newFile;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
