package iuh.fit.dao.daoimpl;

import iuh.fit.dao.daointerface.ServiceCategoryDAO;
import iuh.fit.models.ServiceCategory;
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ServiceCategoryDAOImpl extends UnicastRemoteObject implements ServiceCategoryDAO {

    public ServiceCategoryDAOImpl() throws RemoteException {
    }

    // Tạo mới ServiceCategory với ID từ GlobalSequence
    @Override
    public void createData(ServiceCategory serviceCategory) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            em.getTransaction().begin();

            // Lấy ID kế tiếp từ GlobalSequence
            String jpqlSelect = "SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = :tableName";
            TypedQuery<String> querySelect = em.createQuery(jpqlSelect, String.class);
            querySelect.setParameter("tableName", "ServiceCategory");
            String currentNextID = querySelect.getSingleResult();

            // Gán ID vào serviceCategory
            serviceCategory.setServiceCategoryID(currentNextID);

            // Tăng ID lên 1 và cập nhật GlobalSequence
            String prefix = "SC-";
            int nextIDNum = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;
            String newNextID = prefix + String.format("%06d", nextIDNum);

            String jpqlUpdate = "UPDATE GlobalSequence gs SET gs.nextID = :newNextID WHERE gs.tableName = :tableName";
            em.createQuery(jpqlUpdate)
                    .setParameter("newNextID", newNextID)
                    .setParameter("tableName", "ServiceCategory")
                    .executeUpdate();

            // Lưu ServiceCategory
            em.persist(serviceCategory);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Cập nhật ServiceCategory
    @Override
    public void updateData(ServiceCategory serviceCategory) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            em.getTransaction().begin();
            em.merge(serviceCategory);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Xóa ServiceCategory (chuyển trạng thái INACTIVE)
    @Override
    public boolean deleteData(String serviceCategoryID) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            em.getTransaction().begin();
            ServiceCategory sc = em.find(ServiceCategory.class, serviceCategoryID);
            if (sc == null) return false;
            sc.setIsActivate(ObjectStatus.INACTIVE);
            em.merge(sc);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy tất cả các ServiceCategory đang hoạt động
    @Override
    public List<ServiceCategory> findAll() {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<ServiceCategory> query = em.createQuery(
                    "SELECT sc FROM ServiceCategory sc WHERE sc.isActivate = 'ACTIVE'", ServiceCategory.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Tìm ServiceCategory theo ID
    @Override
    public ServiceCategory findById(String serviceCategoryID) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            return em.find(ServiceCategory.class, serviceCategoryID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Kiểm tra ServiceCategory đang được sử dụng
    @Override
    public boolean isServiceCategoryInUse(String serviceCategoryID) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<Long> query = em.createQuery(
                    """
                    SELECT COUNT(r.id) FROM Room r 
                    JOIN r.roomCategory rc 
                    WHERE rc.roomCategoryID = :serviceCategoryID 
                    AND r.roomStatus IN ('ON_USE', 'OVERDUE')
                    """, Long.class);
            query.setParameter("serviceCategoryID", serviceCategoryID);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tìm kiếm ServiceCategory theo ID chứa từ khóa
    @Override
    public List<ServiceCategory> findDataByContainsId(String input) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<ServiceCategory> query = em.createQuery(
                    "SELECT sc FROM ServiceCategory sc WHERE LOWER(sc.serviceCategoryID) LIKE :input AND sc.isActivate = 'ACTIVE'",
                    ServiceCategory.class);
            query.setParameter("input", "%" + input.toLowerCase() + "%");
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Lấy top 3 ServiceCategory ID
    @Override
    public List<String> getTopThreeID() {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<String> query = em.createQuery(
                    "SELECT sc.serviceCategoryID FROM ServiceCategory sc WHERE sc.isActivate = 'ACTIVE' ORDER BY sc.serviceCategoryID DESC",
                    String.class);
            query.setMaxResults(3);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Lấy ID tiếp theo từ GlobalSequence
    @Override
    public String getNextServiceCategoryID() {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            String jpql = "SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = 'ServiceCategory'";
            TypedQuery<String> query = em.createQuery(jpql, String.class);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return "SC-000001";
        }
    }

    // Lấy danh sách tên ServiceCategory
    @Override
    public List<String> getServiceCategoryNames() {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<String> query = em.createQuery(
                    "SELECT sc.serviceCategoryName FROM ServiceCategory sc WHERE sc.isActivate = 'ACTIVE'", String.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
