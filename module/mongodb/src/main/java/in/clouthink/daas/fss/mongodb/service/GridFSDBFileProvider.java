package in.clouthink.daas.fss.mongodb.service;

import com.mongodb.gridfs.GridFSDBFile;

/**
* @author dz
 */
public interface GridFSDBFileProvider {

	GridFSDBFile getGridFSDBFile();

}
