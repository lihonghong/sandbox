package com.hong.search.evaluate.cms.filler;

import com.hong.search.evaluate.cms.utils.HDFSFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hong on 12/11/15.
 */
public class GlobalSearchQueryFiller implements Query {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalSearchQueryFiller.class);
    private static final String FILE_FORMAT = "hdfs://lgprc-xiaomi/user/h_sns/global_search/log_analysis/zixun_yidian/part-r-00000";

    @Override
    public List<String> fillImpl() {
        HDFSFileReader hdfsFileReader;
        try {
            hdfsFileReader = new HDFSFileReader();
        } catch (IOException e) {
            return null;
        }

        LOGGER.debug("try read file {}", FILE_FORMAT);
        try {
            return hdfsFileReader.readTextFileLinesByPath(FILE_FORMAT);
        } catch (IOException e) {
            LOGGER.error("try reading today file {} failed", FILE_FORMAT);
        }

        return null;
    }

    public static void main(String[] args) throws IOException {
        GlobalSearchQueryFiller globalSearchResultFiller = new GlobalSearchQueryFiller();
        List<String> data = globalSearchResultFiller.fillImpl();
        System.out.println(data.size());
 //       List<String> result = filter(data);
        for (String query : data) {
            System.out.println(query);
        }

    }

    private static List filter(List<String> data) {
        Map<String, Integer> map = new TreeMap<>();
        for (String line : data) {
            String[] cols = line.split("\t");
            String query = cols[0];
            if (stringFilter(query).length() > 1) {
                map.put(query, Integer.parseInt(cols[1]));
            }
        }

        List<Map.Entry<String, Integer>> mapList = new ArrayList<>(map.entrySet());
        Collections.sort(mapList, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> map1, Map.Entry<String, Integer> map2) {
                return map2.getValue().compareTo(map1.getValue());
            }
        });

        List<String> list = new ArrayList<>();
        for (Map.Entry<String, Integer> mapping : mapList) {
            list.add(mapping.getKey());
        }
        return list;
    }

    public static String stringFilter(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？ ]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

}
