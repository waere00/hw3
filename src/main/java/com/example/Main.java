package com.example;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        try (SessionFactory factory = new Configuration().configure().buildSessionFactory()) {
            Session session = factory.openSession();
            TreeDao dao = new TreeDao();
            TreeDao.populateDB(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
