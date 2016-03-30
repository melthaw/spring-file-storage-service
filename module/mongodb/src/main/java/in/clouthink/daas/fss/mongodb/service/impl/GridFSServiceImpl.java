package in.clouthink.daas.fss.mongodb.service.impl;

import com.mongodb.DB;
import com.mongodb.gridfs.GridFS;
import in.clouthink.daas.fss.mongodb.service.GridFSService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;

/**
 * Created by dz on 16/3/30.
 */
public class GridFSServiceImpl implements GridFSService, InitializingBean {

	public static final String GRIDFS_COLLECTION_NAME = "FileObjects";

	@Autowired
	private MongoDbFactory mongoDbFactory;

	private GridFS gridFS;

	@Override
	public GridFS getGridFS() {
		return gridFS;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		DB db = mongoDbFactory.getDb();
		this.gridFS = new GridFS(db, GRIDFS_COLLECTION_NAME);
	}

}
