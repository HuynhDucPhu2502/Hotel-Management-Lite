package iuh.fit;

import iuh.fit.dao.daointerface.ServiceCategoryDAO;
import iuh.fit.dao.daoimpl.ServiceCategoryDAOImpl;

import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args) throws RemoteException {
        final ServiceCategoryDAO serviceCategoryDAO = new ServiceCategoryDAOImpl();

        System.out.println(serviceCategoryDAO.findAll());;
    }
}
