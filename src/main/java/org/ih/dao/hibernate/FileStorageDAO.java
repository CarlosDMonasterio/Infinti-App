package org.ih.dao.hibernate;

import org.ih.dao.model.FileStorageModel;

public class FileStorageDAO extends HibernateRepository<FileStorageModel> {

    @Override
    public FileStorageModel get(long id) {
        return super.retrieve(FileStorageModel.class, id);
    }
}
