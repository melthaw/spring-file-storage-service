package in.clouthink.daas.fss.mongodb.repository.custom.impl;

import in.clouthink.daas.fss.mongodb.model.FileObject;
import in.clouthink.daas.fss.mongodb.repository.FileObjectQueryParameter;
import in.clouthink.daas.fss.mongodb.repository.custom.FileObjectRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Repository
public class FileObjectRepositoryImpl implements FileObjectRepositoryCustom {

	@Autowired
	protected MongoTemplate mongoTemplate;

	@Override
	public Page<FileObject> findPage(FileObjectQueryParameter queryParameter) {
		int start = getStart(queryParameter);
		int limit = getLimit(queryParameter);

		Query query = buildQuery(queryParameter);

		PageRequest pageable = new PageRequest(start, limit);
		query.with(pageable).with(new Sort(Sort.Direction.DESC, "uploadedAt"));
		long count = mongoTemplate.count(query, FileObject.class);
		List<FileObject> list = mongoTemplate.find(query, FileObject.class);

		return new PageImpl<FileObject>(list, pageable, count);
	}

	int getStart(FileObjectQueryParameter queryParameter) {
		return (queryParameter.getStart() < 0) ? 0 : queryParameter.getStart();
	}

	int getLimit(FileObjectQueryParameter queryParameter) {
		return (queryParameter.getLimit() <= 0) ? 10 : queryParameter.getLimit();
	}

	private Query buildQuery(FileObjectQueryParameter queryParameter) {
		Query query = new Query();

		String bizId = queryParameter.getBizId();
		String code = queryParameter.getCode();
		String category = queryParameter.getCategory();
		String uploadedBy = queryParameter.getUploadedBy();
		Date uploadedAtFrom = queryParameter.getUploadedAtFrom();
		Date uploadedAtTo = queryParameter.getUploadedAtTo();

		if (!StringUtils.isEmpty(bizId)) {
			query.addCriteria(Criteria.where("bizId").regex(bizId));
		}

		if (!StringUtils.isEmpty(code)) {
			query.addCriteria(Criteria.where("code").regex(code));
		}
		if (!StringUtils.isEmpty(category)) {
			query.addCriteria(Criteria.where("category").regex(category));
		}
		if (!StringUtils.isEmpty(uploadedBy)) {
			query.addCriteria(Criteria.where("uploadedBy").regex(uploadedBy));
		}

		if (uploadedAtFrom != null && uploadedAtTo != null) {
			Criteria criteria = new Criteria().andOperator(Criteria.where("uploadedAt").gte(uploadedAtFrom),
														   Criteria.where("uploadedAt").lte(uploadedAtTo));
			query.addCriteria(criteria);
		} else if (uploadedAtFrom != null) {
			query.addCriteria(Criteria.where("uploadedAt").gte(uploadedAtFrom));
		} else if (uploadedAtTo != null) {
			query.addCriteria(Criteria.where("uploadedAt").lte(uploadedAtTo));
		}

		return query;
	}

}
