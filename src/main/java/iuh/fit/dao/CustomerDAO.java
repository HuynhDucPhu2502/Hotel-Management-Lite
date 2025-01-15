package iuh.fit.dao;

import iuh.fit.models.Customer;
import iuh.fit.models.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;

public class CustomerDAO {
    public static List<Customer> getData(EntityManager em){
        try{
            Query query = em.createQuery(
                    "select c from Customer c"
            );
            return query.getResultList();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
