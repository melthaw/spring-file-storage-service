package in.clouthink.daas.fss.mongodb.repository;

import java.util.Date;

/**
 * Created by dz on 16/4/5.
 */
public interface FileObjectQueryParameter {

	int getStart();

	int getLimit();

	String getBizId();

	String getCode();

	String getCategory();

	String getUploadedBy();

	Date getUploadedAtFrom();

	Date getUploadedAtTo();

}
