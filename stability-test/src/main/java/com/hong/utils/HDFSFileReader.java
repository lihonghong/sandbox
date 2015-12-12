package com.hong.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.google.api.client.util.Lists;
import com.google.common.base.Joiner;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by dengguo on 15-10-20.
 */
public class HDFSFileReader {
  private static final Logger LOGGER = LoggerFactory.getLogger(HDFSFileReader.class);
  private FileSystem fs;

  public HDFSFileReader() throws IOException {
    Configuration conf = new Configuration();
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

  public Path getLatestUpdateFilePath(String dir, final String pattern) throws IOException {
    FileStatus[] statuses = this.fs.listStatus(new Path(dir), new PathFilter() {
      @Override
      public boolean accept(Path path) {
        LOGGER.debug("check name of {}, {}", path, path.getName());
        return path.getName().matches(pattern);
      }
    });

    Long mostRecentModifedTime = 0l;
    Path mostLatestPath = null;
    for (FileStatus status : statuses) {
      if (mostRecentModifedTime < status.getModificationTime()) {
        mostLatestPath = status.getPath();
        mostRecentModifedTime = status.getModificationTime();
      }
    }

    return mostLatestPath;
  }
}
