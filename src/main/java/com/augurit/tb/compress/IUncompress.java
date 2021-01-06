package com.augurit.tb.compress;

import com.augurit.common.exception.AGException;

public interface IUncompress {
	default  void uncompress(String sourcePath) throws AGException {
		throw new AGException("不支持该方法调用");
	}

	default boolean isMatchedFile(String fileName) {
		return false;
	}

	void uncompress(String sourcePath, String destPath) throws AGException;
}
