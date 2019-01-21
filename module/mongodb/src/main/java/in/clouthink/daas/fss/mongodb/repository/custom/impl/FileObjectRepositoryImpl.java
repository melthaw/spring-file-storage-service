package in.clouthink.daas.fss.mongodb.repository.custom.impl;

import in.clouthink.daas.fss.mongodb.model.FileObject;
import in.clouthink.daas.fss.mongodb.model.FileObjectSearchRequest;
import in.clouthink.daas.fss.mongodb.repository.custom.FileObjectRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Date;

@Repository
public class FileObjectRepositoryImpl implements FileObjectRepositoryCustom {

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Override
    public Page<FileObject> findPage(FileObjectSearchRequest searchRequest) {
        Pageable pageable = searchRequest.toPageable();

        Query query = buildQuery(searchRequest);
        query.with(pageable);

        return PageableExecutionUtils.getPage(mongoTemplate.find(query, FileObject.class),
                                              pageable,
                                              () -> mongoTemplate.count(query, FileObject.class));
    }

    private Query buildQuery(FileObjectSearchRequest searchRequest) {
        Query query = new Query();

        String bizId = searchRequest.getBizId();
        String code = searchRequest.getCode();
        String category = searchRequest.getCategory();
        String uploadedBy = searchRequest.getUploadedBy();
        Date uploadedAtFrom = searchRequest.getUploadedAtFrom();
        Date uploadedAtTo = searchRequest.getUploadedAtTo();

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
        }
        else if (uploadedAtFrom != null) {
            query.addCriteria(Criteria.where("uploadedAt").gte(uploadedAtFrom));
        }
        else if (uploadedAtTo != null) {
            query.addCriteria(Criteria.where("uploadedAt").lte(uploadedAtTo));
        }

        return query;
    }

}
