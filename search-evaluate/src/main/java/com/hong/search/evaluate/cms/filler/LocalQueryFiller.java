package com.hong.search.evaluate.cms.filler;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hong on 12/11/15.
 */
public class LocalQueryFiller implements Query {

    List loadStopWordsList() {
        List<String> list = new ArrayList<>();
        try {
            InputStream genericQueryStream = LocalQueryFiller.class.getClassLoader().getResourceAsStream("searchQuery.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(genericQueryStream, "UTF-8"));
            String line;
            while (StringUtils.isNotEmpty(line = in.readLine())) {
                if (StringUtils.isNotEmpty(line)) {
                    list.add(line.trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<String> fillImpl() {
        return loadStopWordsList();
    }
}
