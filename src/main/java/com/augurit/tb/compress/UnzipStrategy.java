package com.augurit.tb.compress;

import com.augurit.common.exception.AGException;
import com.google.common.base.Strings;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

class UnzipStrategy implements IUncompress {
	private static final UnzipStrategy instance = new UnzipStrategy();

	private UnzipStrategy() {}

	@Override
	public boolean isMatchedFile(String extension) {
		return !Strings.isNullOrEmpty(extension) && extension.endsWith("zip");
	}

	@Override
	public void uncompress(String sourcePath, String destPath) throws AGException {
		try {
			ZipFile zFile = new ZipFile(sourcePath);
			zFile.setFileNameCharset("GBK");
			zFile.extractAll(destPath);
		} catch (ZipException e) {
			throw new AGException("压缩文件解压失败");
		}

	}

	protected static UnzipStrategy getInstance() {
		return instance;
	}
}
