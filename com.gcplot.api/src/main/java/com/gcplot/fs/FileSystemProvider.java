package com.gcplot.fs;

import com.gcplot.model.gc.SourceType;

import java.util.Properties;

/**
 * @author <a href="mailto:art.dm.ser@gmail.com">Artem Dmitriev</a>
 *         3/6/17
 */
public interface FileSystemProvider {

    FileSystem internal();

    FileSystem get(SourceType type, Properties config);

}
