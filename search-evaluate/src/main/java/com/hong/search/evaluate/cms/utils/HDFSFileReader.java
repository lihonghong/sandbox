package com.hong.search.evaluate.cms.utils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by dengguo on 15-10-20.
 */
public class HDFSFileReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(HDFSFileReader.class);
    private FileSystem fs;
    private Configuration conf = new Configuration();

    public HDFSFileReader() throws IOException {
        this.fs = FileSystem.get(conf);
    }

    public List<String> readTextFileLinesByPath(String path) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(this.fs.open(new Path(path))));

        List<String> result = Lists.newArrayList();
        String line = null;
        while ((line = br.readLine()) != null) {
            result.add(line);
        }
        return result;
    }

    public String readTextFileByPath(String path) throws IOException {
        List<String> lines = readTextFileLinesByPath(path);
        return Joiner.on('\n').skipNulls().join(lines);
    }

    public void readSequenceFile(String path) throws IOException {
        Path seqFilePath = new Path(path);

        System.out.println("start");
        SequenceFile.Reader reader = new SequenceFile.Reader(conf,
            SequenceFile.Reader.file(seqFilePath));

        Text key = new Text();
        Text val = new Text();

        while (reader.next(key, val)) {
            System.out.println(key + "\t" + val);
        }

        reader.close();
    }
}
