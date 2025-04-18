package iuh.fit.dao;

import iuh.fit.models.Employee;
import iuh.fit.models.enums.Gender;
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.models.enums.Position;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;

public class EmployeeDAO {

    // Lấy danh sách tất cả nhân viên
    public static List<Employee> getEmployees() {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            String jpql = """
                SELECT e FROM Employee e
                WHERE e.employeeCode != 'EMP-000000' 
                AND e.isActivate = :status
                """;
            TypedQuery<Employee> query = em.createQuery(jpql, Employee.class);
            query.setParameter("status", ObjectStatus.ACTIVE);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Lấy thông tin nhân viên theo mã nhân viên
    public static Employee getEmployeeByEmployeeCode(String employeeCode) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            return em.createQuery("""
            SELECT e FROM Employee e
            WHERE e.employeeCode = :code
        """, Employee.class)
                    .setParameter("code", employeeCode)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // Lấy ID tiếp theo từ GlobalSequence
    public static String getNextEmployeeID() {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            String jpql = "SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = 'Employee'";
            TypedQuery<String> query = em.createQuery(jpql, String.class);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return "EMP-000001";
        }
    }

    // Cập nhật ID tiếp theo trong GlobalSequence
    public static void updateNextEmployeeID(String currentNextID) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            String prefix = "EMP-";
            int nextIDNum = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;
            String newNextID = prefix + String.format("%06d", nextIDNum);

            String jpql = """
                UPDATE GlobalSequence gs
                SET gs.nextID = :newNextID
                WHERE gs.tableName = 'Employee'
                """;
            em.getTransaction().begin();
            em.createQuery(jpql)
                    .setParameter("newNextID", newNextID)
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Tạo mới nhân viên
    public static void createData(Employee employee) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            // Lấy ID kế tiếp
            String nextID = getNextEmployeeID();
            employee.setEmployeeCode(nextID);

            // Cập nhật ID tiếp theo
            updateNextEmployeeID(nextID);

            em.persist(employee);
            tx.commit();
            System.out.println("Thêm mới nhân viên thành công với mã: " + nextID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Cập nhật thông tin nhân viên
    public static void updateData(Employee employee) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            // Sử dụng TypedQuery để tìm nhân viên dựa trên employeeCode
            String jpql = "SELECT e FROM Employee e WHERE e.employeeCode = :code";
            TypedQuery<Employee> query = em.createQuery(jpql, Employee.class);
            query.setParameter("code", employee.getEmployeeCode());

            // Lấy nhân viên từ kết quả truy vấn
            Employee existingEmployee = query.getResultStream().findFirst().orElse(null);
            if (existingEmployee == null) {
                throw new IllegalArgumentException("Nhân viên không tồn tại: " + employee.getEmployeeCode());
            }

            // Cập nhật thông tin từ đối tượng mới
            existingEmployee.setFullName(employee.getFullName());
            existingEmployee.setPhoneNumber(employee.getPhoneNumber());
            existingEmployee.setAddress(employee.getAddress());
            existingEmployee.setGender(employee.getGender());
            existingEmployee.setIdCardNumber(employee.getIdCardNumber());
            existingEmployee.setDob(employee.getDob());
            existingEmployee.setPosition(employee.getPosition());
            existingEmployee.setIsActivate(employee.getIsActivate());

            // Thực hiện merge để cập nhật
            em.merge(existingEmployee);
            tx.commit();
            System.out.println("Cập nhật nhân viên thành công: " + employee.getEmployeeCode());
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi cập nhật nhân viên: " + e.getMessage());
        } finally {
            if (em != null) em.close();
        }
    }


    // Xóa (ngưng hoạt động) nhân viên
    public static void deleteData(String employeeID) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            Employee employee = em.find(Employee.class, employeeID);
            if (employee != null) {
                employee.setIsActivate(ObjectStatus.INACTIVE);
                em.merge(employee);
            }
            tx.commit();
            System.out.println("Xóa nhân viên thành công: " + employeeID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lấy top 3 nhân viên có ID lớn nhất
    public static List<String> getTopThreeID() {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            String jpql = """
                SELECT e.employeeCode FROM Employee e
                WHERE e.isActivate = :status
                ORDER BY e.employeeCode DESC
                """;
            TypedQuery<String> query = em.createQuery(jpql, String.class);
            query.setParameter("status", ObjectStatus.ACTIVE);
            query.setMaxResults(3);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Tìm nhân viên theo từ khóa ID
    public static List<Employee> findDataByContainsId(String input) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            String jpql = """
                SELECT e FROM Employee e 
                WHERE LOWER(e.employeeCode) LIKE :input 
                AND e.isActivate = :status
                """;
            TypedQuery<Employee> query = em.createQuery(jpql, Employee.class);
            query.setParameter("input", "%" + input.toLowerCase() + "%");
            query.setParameter("status", ObjectStatus.ACTIVE);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Tìm kiếm nhân viên theo nhiều điều kiện
    public static List<Employee> searchEmployee(
            String employeeID, String fullName, String phoneNumber,
            String address, Gender gender, String idCardNumber,
            LocalDate dob, Position position
    ) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            String jpql = """
                SELECT e FROM Employee e 
                WHERE (:employeeID IS NULL OR e.employeeCode LIKE CONCAT('%', :employeeID, '%'))
                AND (:fullName IS NULL OR e.fullName LIKE CONCAT('%', :fullName, '%'))
                AND (:phoneNumber IS NULL OR e.phoneNumber LIKE CONCAT('%', :phoneNumber, '%'))
                AND (:address IS NULL OR e.address LIKE CONCAT('%', :address, '%'))
                AND (:idCardNumber IS NULL OR e.idCardNumber LIKE CONCAT('%', :idCardNumber, '%'))
                AND (:gender IS NULL OR e.gender = :gender)
                AND (:dob IS NULL OR e.dob = :dob)
                AND (:position IS NULL OR e.position = :position)
                AND e.isActivate = :status
                """;
            TypedQuery<Employee> query = em.createQuery(jpql, Employee.class);
            query.setParameter("employeeID", employeeID);
            query.setParameter("fullName", fullName);
            query.setParameter("phoneNumber", phoneNumber);
            query.setParameter("address", address);
            query.setParameter("idCardNumber", idCardNumber);
            query.setParameter("gender", gender);
            query.setParameter("dob", dob);
            query.setParameter("position", position);
            query.setParameter("status", ObjectStatus.ACTIVE);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
