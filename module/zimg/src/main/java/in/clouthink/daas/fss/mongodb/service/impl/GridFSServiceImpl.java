package in.clouthink.daas.fss.mongodb.service.impl;

import com.mongodb.DB;
import com.mongodb.gridfs.GridFS;
import in.clouthink.daas.fss.mongodb.service.GridFSService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.util.StringUtils;

/**
* @author dz on 16/3/30.
 */
public class GridFSServiceImpl implements GridFSService, InitializingBean {

	public static final String GRIDFS_COLLECTION_NAME = "FileObjects";

	private String collectionName = GRIDFS_COLLECTION_NAME;

	@Autowired
	private MongoDbFactory mongoDbFactory;

	private GridFS gridFS;

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		if (StringUtils.isEmpty(collectionName)) {
			throw new IllegalArgumentException("The collection name can't be null or empty");
		}
		this.collectionName = collectionName;
	}

	@Override
	public GridFS getGridFS() {
		return gridFS;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		DB db = mongoDbFactory.getDb();
		this.gridFS = new GridFS(db, collectionName);
	}

}
