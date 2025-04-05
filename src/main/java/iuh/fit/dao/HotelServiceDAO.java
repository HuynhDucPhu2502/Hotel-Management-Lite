package iuh.fit.dao;

import iuh.fit.models.HotelService;
import iuh.fit.models.ServiceCategory;
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class HotelServiceDAO {

    // Lấy tất cả HotelService đang hoạt động
    public static List<HotelService> getHotelService() {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<HotelService> query = em.createQuery(
                    """
                    SELECT hs FROM HotelService hs
                    JOIN FETCH hs.serviceCategory sc
                    WHERE hs.isActivate = :status
                    """, HotelService.class);
            query.setParameter("status", ObjectStatus.ACTIVE); // Truyền enum thay vì string
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Lấy HotelService theo ID
    public static HotelService getDataByID(String hotelServiceId) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<HotelService> query = em.createQuery(
                    """
                    SELECT hs FROM HotelService hs
                    JOIN FETCH hs.serviceCategory sc
                    WHERE hs.serviceID = :hotelServiceId 
                    AND hs.isActivate = :status
                    """, HotelService.class);
            query.setParameter("hotelServiceId", hotelServiceId);
            query.setParameter("status", ObjectStatus.ACTIVE); // Truyền enum thay vì string
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Tạo mới HotelService với ID từ GlobalSequence
    public static boolean createData(HotelService hotelService) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            em.getTransaction().begin();

            // Kiểm tra xem ServiceCategory đã tồn tại chưa
            ServiceCategory existingCategory = em.find(ServiceCategory.class, hotelService.getServiceCategory().getServiceCategoryID());
            if (existingCategory == null) {
                // Nếu chưa tồn tại, lưu ServiceCategory trước
                em.persist(hotelService.getServiceCategory());
            } else {
                // Nếu đã tồn tại, set lại category để tránh Hibernate cố tạo mới
                hotelService.setServiceCategory(existingCategory);
            }

            // Lấy ID kế tiếp từ GlobalSequence
            String jpqlSelect = "SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = :tableName";
            TypedQuery<String> querySelect = em.createQuery(jpqlSelect, String.class);
            querySelect.setParameter("tableName", "HotelService");
            String currentNextID = querySelect.getSingleResult();

            // Gán ID vào HotelService
            hotelService.setServiceID(currentNextID);

            // Tăng ID lên 1 và cập nhật GlobalSequence
            String prefix = "HS-";
            try {
                int nextIDNum = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;
                String newNextID = prefix + String.format("%06d", nextIDNum);

                String jpqlUpdate = "UPDATE GlobalSequence gs SET gs.nextID = :newNextID WHERE gs.tableName = :tableName";
                em.createQuery(jpqlUpdate)
                        .setParameter("newNextID", newNextID)
                        .setParameter("tableName", "HotelService")
                        .executeUpdate();
            } catch (NumberFormatException ex) {
                System.err.println("Lỗi khi chuyển đổi ID: " + currentNextID);
                em.getTransaction().rollback();
                return false;
            }

            // Lưu HotelService
            em.persist(hotelService);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    // Xóa HotelService (chuyển trạng thái INACTIVE)
    public static boolean deleteData(String hotelServiceId) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            em.getTransaction().begin();
            HotelService hs = em.find(HotelService.class, hotelServiceId);
            if (hs == null) return false;
            hs.setIsActivate(ObjectStatus.INACTIVE);
            em.merge(hs);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật HotelService
    public static boolean updateData(HotelService hotelService) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            em.getTransaction().begin();
            em.merge(hotelService);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tìm kiếm HotelService theo ID chứa từ khóa
    public static List<HotelService> findDataByContainsId(String input) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<HotelService> query = em.createQuery(
                    """
                    SELECT hs FROM HotelService hs 
                    WHERE LOWER(hs.serviceID) LIKE :input 
                    AND hs.isActivate = :status
                    """, HotelService.class);
            query.setParameter("input", "%" + input.toLowerCase() + "%");
            query.setParameter("status", ObjectStatus.ACTIVE); // Truyền enum thay vì string
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Lấy top 3 HotelService ID
    public static List<String> getTopThreeID() {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            TypedQuery<String> query = em.createQuery(
                    """
                    SELECT hs.serviceID FROM HotelService hs 
                    WHERE hs.isActivate = :status
                    ORDER BY hs.serviceID DESC
                    """, String.class);
            query.setParameter("status", ObjectStatus.ACTIVE); // Truyền Enum thay vì String
            query.setMaxResults(3);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Lấy ID tiếp theo từ GlobalSequence
    public static String getNextHotelServiceID() {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            String jpql = "SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = 'HotelService'";
            TypedQuery<String> query = em.createQuery(jpql, String.class);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return "HS-000001";
        }
    }

    public static List<HotelService> searchHotelServices(
            String hotelServiceID, String serviceName,
            Double minPrice, Double maxPrice, String serviceCategory) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            String jpql = """
                SELECT hs FROM HotelService hs 
                WHERE (:hotelServiceID IS NULL OR hs.serviceID LIKE CONCAT('%', :hotelServiceID, '%'))
                AND (:serviceName IS NULL OR hs.serviceName LIKE CONCAT('%', :serviceName, '%'))
                AND (:minPrice IS NULL OR hs.servicePrice >= :minPrice)
                AND (:maxPrice IS NULL OR hs.servicePrice <= :maxPrice)
                AND (:serviceCategory = 'ALL' OR hs.serviceCategory.serviceCategoryID = :serviceCategory 
                OR (:serviceCategory = 'NULL' AND hs.serviceCategory IS NULL))
                AND hs.isActivate = :status
                """;

            TypedQuery<HotelService> query = em.createQuery(jpql, HotelService.class);

            query.setParameter("hotelServiceID", hotelServiceID);
            query.setParameter("serviceName", serviceName);
            query.setParameter("minPrice", minPrice);
            query.setParameter("maxPrice", maxPrice);
            query.setParameter("serviceCategory", serviceCategory);
            query.setParameter("status", ObjectStatus.ACTIVE);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    // Kiểm tra HotelService đang được sử dụng
//    public static boolean isHotelServiceInUse(String hotelServiceId) {
//        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
//            TypedQuery<Long> query = em.createQuery(
//                    """
//                    SELECT COUNT(r.id) FROM Room r
//                    JOIN r.roomUsageService rus
//                    WHERE rus.hotelService.serviceID = :hotelServiceId
//                    """, Long.class);
//            query.setParameter("hotelServiceId", hotelServiceId);
//            return query.getSingleResult() > 0;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
}
