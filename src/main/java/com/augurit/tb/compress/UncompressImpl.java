package com.augurit.tb.compress;

import com.augurit.common.exception.AGException;
import com.google.common.collect.Lists;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.List;

public class UncompressImpl implements IUncompress {

	private String defaultUncompressDestPath = "D:/awater/compress/";

	private static final List<IUncompress> uncompressList = Lists.newArrayList(
			UnzipStrategy.getInstance(),
			UnrarStrategy.getInstance()
	);

	@Override
	public void uncompress(String sourcePath) throws AGException {
		uncompress(sourcePath, defaultUncompressDestPath);
	}

	@Override
	public void uncompress(String sourcePath, String destPath) throws AGException {
		File sourceFile = new File(sourcePath);
		if(!sourceFile.exists() && !sourceFile.isFile()) {
			throw new AGException("原文件不存在");
		}

		File destDir = new File(destPath);
		if(destDir.exists()) {
			destDir.delete();
		}

		destDir.mkdirs();

		String extension = FilenameUtils.getExtension(sourcePath);
		uncompressList.forEach(uncompress -> {
			if(uncompress.isMatchedFile(extension)) {
				uncompress.uncompress(sourcePath, destPath);
			}
		});
	}
}
