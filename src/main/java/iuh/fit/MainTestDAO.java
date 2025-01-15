package iuh.fit;

import iuh.fit.dao.CustomerDAO;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.dao.RoomDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class MainTestDAO {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("mssql");
        EntityManager em = emf.createEntityManager();

//        EmployeeDAO.getData(em)
//                .forEach(System.out::println);
//        CustomerDAO.getData(em)
//                .forEach(System.out::println);
        RoomDAO.getData(em)
                .forEach(System.out::println);

        em.close();
    }
}
