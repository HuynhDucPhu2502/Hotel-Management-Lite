package iuh.fit.devtools;

import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

/**
 * Admin 3/28/2025
 **/
public class DropAllTables {
    public static void main(String[] args) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        // Thứ tự xóa đúng: bảng phụ → bảng cha
        String[] tables = {
                "reservation_room_details",
                "room_usage_services",
                "history_checkin",
                "history_checkout",
                "invoices",
                "reservation_forms",
                "shift_assignments",
                "shifts",
                "accounts",
                "employees",
                "customers",
                "persons",
                "rooms",
                "room_categories",
                "hotel_services",
                "service_categories"
        };

        try {
            tx.begin();
            for (String table : tables) {
                try {
                    em.createNativeQuery("DROP TABLE " + table).executeUpdate();
                } catch (Exception ignore) {
                    // Nếu bảng không tồn tại hoặc có lỗi → bỏ qua
                }
            }
            tx.commit();
            System.out.println("Đã drop tất cả bảng");
        } catch (Exception e) {
            tx.rollback();
            System.err.println("Lỗi khi drop bảng: " + e.getMessage());
        } finally {
            em.close();
            EntityManagerUtil.close();
        }
    }
}
