package iuh.fit.dao;

import iuh.fit.models.Employee;
import iuh.fit.models.ReservationForm;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;

public class EmployeeDAO {
    public static List<Employee> getData(EntityManager em){
        try{
            Query query = em.createQuery(
                    "select e from Employee e"
            );
            return query.getResultList();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
