package iuh.fit;

import iuh.fit.dao.ServiceCategoryDAO;

public class Main {
    public static void main(String[] args) {
        System.out.println(ServiceCategoryDAO.findAll());;
    }
}
