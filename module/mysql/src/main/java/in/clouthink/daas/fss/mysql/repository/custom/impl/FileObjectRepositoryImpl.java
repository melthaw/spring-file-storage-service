package in.clouthink.daas.fss.mysql.repository.custom.impl;

import in.clouthink.daas.fss.mysql.model.FileObject;
import in.clouthink.daas.fss.mysql.model.FileObjectSearchRequest;
import in.clouthink.daas.fss.mysql.repository.custom.FileObjectRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class FileObjectRepositoryImpl implements FileObjectRepositoryCustom {

    @Autowired
    protected EntityManager entityManager;

    @Override
    public Page<FileObject> findPage(FileObjectSearchRequest searchRequest) {
        Pageable pageable = searchRequest.toPageable();

        //FIXME
        return null;
    }

}
