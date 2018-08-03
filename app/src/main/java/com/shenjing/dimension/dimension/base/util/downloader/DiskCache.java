package com.shenjing.dimension.dimension.base.util.downloader;

import java.io.File;

/**
 * Created by Tiny  on 2016/7/27 17:34.
 * Desc:
 */
public interface DiskCache {



    File get(String url, FilenameGenerator generator);

    File getBaseDir();

    String createDiskFilePath(String filename);

}
