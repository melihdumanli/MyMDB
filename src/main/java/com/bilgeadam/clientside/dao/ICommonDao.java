package com.bilgeadam.clientside.dao;

import com.bilgeadam.clientside.dbconnection.DatabaseConnection;

import java.sql.Connection;

public interface ICommonDao<T> {
    public void read(T t);

    public void write(T t);

    public void delete(T t);

    default Connection getInterfaceConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }

}
