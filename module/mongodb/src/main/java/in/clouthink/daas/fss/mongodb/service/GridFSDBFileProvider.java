package in.clouthink.daas.fss.mongodb.service;

import com.mongodb.gridfs.GridFSDBFile;

/**
 * Created by dz on 16/3/29.
 */
public interface GridFSDBFileProvider {

	GridFSDBFile getGridFSDBFile();

}
