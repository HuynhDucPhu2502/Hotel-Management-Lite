package iuh.fit.devtools;

import iuh.fit.models.Account;
import iuh.fit.models.Employee;
import iuh.fit.models.HotelService;
import iuh.fit.models.ServiceCategory;
import iuh.fit.models.enums.AccountStatus;
import iuh.fit.models.enums.Gender;
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.models.enums.Position;
import iuh.fit.security.PasswordHashing;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDate;
import java.util.List;

// =================================================================
// Lớp tạo dữ liệu cho bài
// =================================================================
public class InitSampleData {
    public static void main(String[] args) {
        initEmployeeAndAccountData();
        initServiceCategoryAndHotelService();
    }

    // =================================================================
    // Hàm tạo dữ liệu nhân viên và tài khoản
    // =================================================================
    public static void initEmployeeAndAccountData() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            List<Employee> employees = List.of(
                    new Employee("EMP-000001", "Huynh Duc Phu", "0912345678", "123 Ho Chi Minh", Gender.MALE, "001099012345", LocalDate.of(1985, 6, 15), ObjectStatus.ACTIVE, Position.MANAGER),
                    new Employee("EMP-000002", "Nguyen Xuan Chuc", "0908765432", "456 Hue", Gender.MALE, "002199012346", LocalDate.of(1990, 4, 22), ObjectStatus.ACTIVE, Position.RECEPTIONIST),
                    new Employee("EMP-000003", "Le Tran Gia Huy", "0987654321", "789 Ho Chi Minh", Gender.MALE, "003299012347", LocalDate.of(1992, 8, 19), ObjectStatus.ACTIVE, Position.MANAGER),
                    new Employee("EMP-000004", "Dang Nguyen Tien Phat", "0934567890", "321 Binh Phuoc", Gender.MALE, "004399012348", LocalDate.of(1987, 12, 5), ObjectStatus.ACTIVE, Position.RECEPTIONIST),
                    new Employee("EMP-000005", "Vu Ba Hai", "0923456789", "654 Long An", Gender.MALE, "004399012349", LocalDate.of(1995, 3, 30), ObjectStatus.ACTIVE, Position.MANAGER)
            );

            for (Employee e : employees) {
                em.persist(e);
                Account acc = new Account(
                        "ACC-" + e.getEmployeeCode().substring(4),
                        e.getFullName().toLowerCase().replace(" ", ""),
                        PasswordHashing.hashPassword("test123@"),
                        AccountStatus.ACTIVE,
                        e
                );
                em.persist(acc);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
            EntityManagerUtil.close();
        }
    }

    // =================================================================
    // Hàm tạo dữ liệu cho danh mục dịch vụ và dịch vụ khách sạn
    // =================================================================
    public static void initServiceCategoryAndHotelService() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            List<ServiceCategory> categories = List.of(
                    new ServiceCategory("SC-000001", "Giải trí", ObjectStatus.ACTIVE),
                    new ServiceCategory("SC-000002", "Ăn uống", ObjectStatus.ACTIVE),
                    new ServiceCategory("SC-000003", "Chăm sóc và sức khỏe", ObjectStatus.ACTIVE),
                    new ServiceCategory("SC-000004", "Vận chuyển", ObjectStatus.ACTIVE)
            );

            for (ServiceCategory category : categories) {
                em.persist(category);
            }

            List<HotelService> services = List.of(
                    new HotelService("HS-000001", "Dịch vụ Karaoke", "Phòng karaoke chất lượng cao, tiêu chuẩn giải trí gia đình", 100.00, ObjectStatus.ACTIVE, categories.get(0)),
                    new HotelService("HS-000002", "Hồ bơi", "Sử dụng hồ bơi ngoài trời cho khách nghỉ", 50.00, ObjectStatus.ACTIVE, categories.get(0)),
                    new HotelService("HS-000003", "Bữa sáng tự chọn", "Bữa sáng buffet với đa dạng món ăn", 30.00, ObjectStatus.ACTIVE, categories.get(1)),
                    new HotelService("HS-000004", "Thức uống tại phòng", "Phục vụ thức uống tại phòng", 20.00, ObjectStatus.ACTIVE, categories.get(1)),
                    new HotelService("HS-000005", "Dịch vụ Spa", "Massage toàn thân và liệu trình chăm sóc da", 120.00, ObjectStatus.ACTIVE, categories.get(2)),
                    new HotelService("HS-000006", "Chăm sóc trẻ em", "Chăm sóc trẻ dưới 10 tuổi", 80.00, ObjectStatus.ACTIVE, categories.get(2)),
                    new HotelService("HS-000007", "Thuê xe", "Thuê xe di chuyển trong thành phố", 150.00, ObjectStatus.ACTIVE, categories.get(3)),
                    new HotelService("HS-000008", "Dịch vụ Xông hơi", "Xông hơi thư giãn cơ thể và tâm trí", 900000, ObjectStatus.ACTIVE, categories.get(0)),
                    new HotelService("HS-000009", "Phòng Gym", "Trung tâm thể hình với trang thiết bị hiện đại", 700000, ObjectStatus.ACTIVE, categories.get(0)),
                    new HotelService("HS-000010", "Trò chơi điện tử", "Khu vực giải trí với các trò chơi điện tử", 500000, ObjectStatus.ACTIVE, categories.get(0)),
                    new HotelService("HS-000011", "Buffet tối", "Thực đơn buffet với đa dạng món ăn", 2000000, ObjectStatus.ACTIVE, categories.get(1)),
                    new HotelService("HS-000012", "Quầy bar", "Thưởng thức cocktail và các loại rượu tại quầy", 800000, ObjectStatus.ACTIVE, categories.get(1)),
                    new HotelService("HS-000013", "Dịch vụ Cà phê", "Cà phê và đồ uống nóng phục vụ cả ngày", 300000, ObjectStatus.ACTIVE, categories.get(1)),
                    new HotelService("HS-000014", "Dịch vụ Tóc", "Tạo kiểu và chăm sóc tóc chuyên nghiệp", 600000, ObjectStatus.ACTIVE, categories.get(2)),
                    new HotelService("HS-000015", "Tắm trắng", "Liệu trình tắm trắng da toàn thân", 1500000, ObjectStatus.ACTIVE, categories.get(2)),
                    new HotelService("HS-000016", "Yoga & Thiền", "Lớp yoga và thiền hàng ngày", 1000000, ObjectStatus.ACTIVE, categories.get(2)),
                    new HotelService("HS-000017", "Xe đưa đón sân bay", "Dịch vụ đưa đón từ sân bay về khách sạn", 1200000, ObjectStatus.ACTIVE, categories.get(3)),
                    new HotelService("HS-000018", "Thuê xe đạp", "Thuê xe đạp tham quan quanh thành phố", 400000, ObjectStatus.ACTIVE, categories.get(3)),
                    new HotelService("HS-000019", "Thuê xe điện", "Thuê xe điện cho các chuyến đi ngắn", 600000, ObjectStatus.ACTIVE, categories.get(3)),
                    new HotelService("HS-000020", "Dịch vụ Thư ký", "Hỗ trợ thư ký và in ấn tài liệu", 1000000, ObjectStatus.ACTIVE, categories.get(3))
            );

            for (HotelService service : services) {
                em.persist(service);
            }

            tx.commit();
            System.out.println("Dữ liệu đã được khởi tạo thành công!");
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
            EntityManagerUtil.close();
        }
    }


}
