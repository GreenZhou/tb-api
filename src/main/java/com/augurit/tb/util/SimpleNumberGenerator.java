package com.augurit.tb.util;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 简单流水号生成器
 */
public class SimpleNumberGenerator {
    private static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyyMMdd");

    private static int seq_number = 0;

    private static final ConcurrentMap<String, List<String>> SEQ_MAP = new ConcurrentHashMap<String, List<String>>();

    static {
        List<String> seqs = new ArrayList<String>(99);
        for(int i=1; i<100; i++) {
            seqs.add("00".substring(0, "00".length() - ("" + i).length()) + i);
        }

        SEQ_MAP.put(dateFormatter.print(LocalDateTime.now()), seqs);
    }

    public static String generate() {
        String currentDateStr = dateFormatter.print(LocalDateTime.now());
        return currentDateStr + getSeq(currentDateStr);
    }

    public static void main(String[] args) {
        for(int i=1; i<100; i++) {
            System.out.println(generate());
        }
    }

    public synchronized static String getSeq(String dateFormat) {
        List<String> seqs = SEQ_MAP.get(dateFormat);

        if(seqs == null) {
            // 获取序列对象并清空map数据
            for(Map.Entry<String, List<String>> entry : SEQ_MAP.entrySet()) {
                seqs = SEQ_MAP.get(entry.getKey());
                SEQ_MAP.remove(entry.getKey());

                seq_number = 0;
            }

            SEQ_MAP.put(dateFormat, seqs);
        }

        return seqs.get(seq_number++);
    }
}
