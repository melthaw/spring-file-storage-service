package in.clouthink.daas.fss.mongodb.repository;

import java.util.Date;

/**
* @author dz on 16/4/5.
 */
public interface FileObjectQueryParameter {

	String getBizId();

	String getCode();

	String getCategory();

	String getUploadedBy();

	Date getUploadedAtFrom();

	Date getUploadedAtTo();

}
