package com.bilgeadam.clientside.mdbfactory;

import com.bilgeadam.clientside.dao.ActorDao;
import com.bilgeadam.clientside.dao.ICommonDao;
import com.bilgeadam.clientside.dao.MovieDao;

public class MovieDbFactory {
    public ICommonDao getDao (int choice) {
        switch (choice) {
            case 1:
                return new ActorDao();
            case 2:
                return new MovieDao();
            default:
                return null;
        }
    }
}
