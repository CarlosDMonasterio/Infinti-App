package org.ih.common.file;

import org.ih.common.logging.Logger;
import org.ih.dao.DAOFactory;
import org.ih.dao.hibernate.FileStorageDAO;
import org.ih.dao.model.AccountModel;
import org.ih.dao.model.FileStorageModel;
import org.ih.dto.FileStorage;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

public class Files {

    private final String userId;
    private final FileStorageDAO dao;

    public Files(String userId) {
        this.userId = userId;
        this.dao = DAOFactory.getFileStorageDAO();
    }

    public FileStorage add(String name, InputStream stream) {
        byte[] bytes;
        try {
            bytes = stream.readAllBytes();
        } catch (Exception e) {
            Logger.error(e);
            return null;
        }

        AccountModel accountModel = DAOFactory.getAccountDAO().getByEmail(userId);
        FileStorageModel model = new FileStorageModel();
        model.setData(bytes);
        model.setName(name);
        model.setIdentifier(UUID.randomUUID().toString());
        model.setCreated(new Date());
        model.setAccount(accountModel);

        model = this.dao.create(model);
        return model.toDataObject();
    }
}
