package in.clouthink.daas.fss.mysql.repository.custom.impl;

import in.clouthink.daas.fss.domain.request.FileObjectSearchRequest;
import in.clouthink.daas.fss.mysql.model.FileObject;
import in.clouthink.daas.fss.mysql.repository.custom.FileObjectRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Repository
public class FileObjectRepositoryImpl implements FileObjectRepositoryCustom {

    @Autowired
    protected EntityManager entityManager;

    @Override
    public Page<FileObject> findPage(FileObjectSearchRequest searchRequest) {
        JHqlBuilder jhqlBuilder = createHqlBuilder(searchRequest);

        Pageable pageable = searchRequest.toPageable();

        String listJHql = jhqlBuilder.getQueryHql(null);
        String countJHql = jhqlBuilder.getCountJHql();

        Long total = queryCount(countJHql, jhqlBuilder.getParameters());
        PageableExecutionUtils.TotalSupplier totalSupplier = () -> total;

        List<FileObject> searchResult = queryPageList(listJHql, jhqlBuilder.getParameters(), pageable, total);

        return PageableExecutionUtils.getPage(searchResult, pageable, totalSupplier);
    }

    private JHqlBuilder createHqlBuilder(FileObjectSearchRequest searchRequest) {
        JHqlBuilder jhqlBuilder = new JHqlBuilder("select count(p.id) from FileObject p ",
                                                  "select p from FileObject p ");

        if (searchRequest instanceof in.clouthink.daas.fss.mysql.model.FileObjectSearchRequest) {
            in.clouthink.daas.fss.mysql.model.FileObjectSearchRequest mysqlSearchRequest = (in.clouthink.daas.fss.mysql.model.FileObjectSearchRequest) searchRequest;
            if (!StringUtils.isEmpty(mysqlSearchRequest.getAttachedId())) {
                jhqlBuilder.andEquals("p.attachedId", "pAttachedId", mysqlSearchRequest.getAttachedId());
            }
            if (!StringUtils.isEmpty(mysqlSearchRequest.getCategory())) {
                jhqlBuilder.andLike(new String[]{"p.category"},
                                    "nameLike",
                                    "%" + mysqlSearchRequest.getCategory() + "%");
            }
            if (!StringUtils.isEmpty(mysqlSearchRequest.getCode())) {
                jhqlBuilder.andLike(new String[]{"p.code"}, "codeLike", "%" + mysqlSearchRequest.getCode() + "%");
            }
        }

        if (!StringUtils.isEmpty(searchRequest.getStoredFilename())) {
            jhqlBuilder.andLike(new String[]{"p.storedFilename"},
                                "pStoredFilename",
                                "%" + searchRequest.getStoredFilename() + "%");
        }
        if (!StringUtils.isEmpty(searchRequest.getUploadedFilename())) {
            jhqlBuilder.andLike(new String[]{"p.uploadedFilename"},
                                "pUploadedFilename",
                                "%" + searchRequest.getUploadedFilename() + "%");
        }
        if (!StringUtils.isEmpty(searchRequest.getUploadedBy())) {
            jhqlBuilder.andLike(new String[]{"p.uploadedBy"}, "pUploadedBy", "%" + searchRequest.getUploadedBy() + "%");
        }
        if (null != searchRequest.getUploadedAtFrom()) {
            jhqlBuilder.andGreaterThan("p.uploadedAt", "pUploadedAtFrom", searchRequest.getUploadedAtFrom());
        }
        if (null != searchRequest.getUploadedAtTo()) {
            jhqlBuilder.andLessThan("p.uploadedAt", "pUploadedAtTo", searchRequest.getUploadedAtTo());
        }

        return jhqlBuilder;
    }

    private long queryCount(String jpql, Map<String, Object> parameters) {
        Query query = entityManager.createQuery(jpql);
        setParameters(query, parameters);
        return (Long) query.getSingleResult();
    }

    private List queryPageList(String listHql, Map param, Pageable pageable, Long total) {
        Query query = entityManager.createQuery(listHql);
        setParameters(query, param);
        int startIndex = pageable.getOffset();
        if (startIndex < 0) {
            startIndex = 0;
        }
        if (startIndex > total) {
            startIndex = total.intValue();
        }
        query.setFirstResult(startIndex);
        query.setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }

    protected void setParameters(Query query, Map<String, Object> parameters) {
        if (query == null || parameters == null) {
            return;
        }

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
    }

}
