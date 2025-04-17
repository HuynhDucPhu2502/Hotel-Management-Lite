package iuh.fit.devtools;

import iuh.fit.models.*;
import iuh.fit.models.enums.*;
import iuh.fit.security.PasswordHashing;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// =================================================================
// Lớp tạo dữ liệu cho bài
// =================================================================
public class InitSampleData {
    public static void main(String[] args) {
        Persistence.createEntityManagerFactory("drop-data-mssql").close();
        System.out.println("Dữ liệu cũ đã được xóa");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("init-data-mssql");
        EntityManager em = emf.createEntityManager();


        initCustomerData(em);
        initEmployeeAndAccountData(em);
        initServiceCategoryAndHotelService(em);
        initRoomCategoryAndRoomData(em);
        initGlobalSequenceData(em);

        emf.close();
    }

    // =================================================================
    // Hàm tạo dữ liệu khách hàng
    // =================================================================
    public static void initCustomerData(EntityManager em) {
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            List<Customer> customers = List.of(
                    new Customer("CUS-000001", "Nguyen Van A", "0912345678", "123 Duong ABC, Quan 1, TP HCM", Gender.MALE, "001099012333", LocalDate.of(1990, 5, 15), ObjectStatus.ACTIVE),
                    new Customer("CUS-000002", "Le Thi B", "0912345679", "456 Duong XYZ, Quan 3, TP HCM", Gender.FEMALE, "001099012323", LocalDate.of(1992, 7, 22), ObjectStatus.ACTIVE),
                    new Customer("CUS-000003", "Tran Van C", "0912345680", "789 Duong MNO, Quan 5, TP HCM", Gender.MALE, "001099012343", LocalDate.of(1988, 3, 30), ObjectStatus.ACTIVE),
                    new Customer("CUS-000004", "Pham Thi D", "0912345681", "321 Duong PQR, Quan 7, TP HCM", Gender.FEMALE, "001099012546", LocalDate.of(1995, 12, 1), ObjectStatus.ACTIVE),
                    new Customer("CUS-000005", "Hoang Van E", "0912345682", "987 Duong STU, Quan 10, TP HCM", Gender.MALE, "001099012764", LocalDate.of(1991, 11, 20), ObjectStatus.ACTIVE),
                    new Customer("CUS-000006", "Nguyen Van F", "0912345683", "111 Duong DEF, Quan 1, TP HCM", Gender.MALE, "001099012765", LocalDate.of(1989, 4, 18), ObjectStatus.ACTIVE),
                    new Customer("CUS-000007", "Le Thi G", "0912345684", "222 Duong GHI, Quan 2, TP HCM", Gender.FEMALE, "001099012776", LocalDate.of(1993, 8, 29), ObjectStatus.ACTIVE),
                    new Customer("CUS-000008", "Tran Van H", "0912345685", "333 Duong JKL, Quan 3, TP HCM", Gender.MALE, "001099012787", LocalDate.of(1985, 12, 12), ObjectStatus.ACTIVE),
                    new Customer("CUS-000009", "Pham Thi I", "0912345686", "444 Duong MNO, Quan 4, TP HCM", Gender.FEMALE, "001099012798", LocalDate.of(1990, 1, 1), ObjectStatus.ACTIVE),
                    new Customer("CUS-000010", "Hoang Van J", "0912345687", "555 Duong PQR, Quan 5, TP HCM", Gender.MALE, "001099012809", LocalDate.of(1987, 5, 5), ObjectStatus.ACTIVE),
                    new Customer("CUS-000011", "Nguyen Van K", "0912345688", "666 Duong STU, Quan 6, TP HCM", Gender.MALE, "001099012810", LocalDate.of(1992, 11, 11), ObjectStatus.ACTIVE),
                    new Customer("CUS-000012", "Le Thi L", "0912345689", "777 Duong VWX, Quan 7, TP HCM", Gender.FEMALE, "001099012821", LocalDate.of(1994, 2, 15), ObjectStatus.ACTIVE),
                    new Customer("CUS-000013", "Tran Van M", "0912345690", "888 Duong YZA, Quan 8, TP HCM", Gender.MALE, "001099012832", LocalDate.of(1986, 9, 9), ObjectStatus.ACTIVE),
                    new Customer("CUS-000014", "Pham Thi N", "0912345691", "999 Duong BCD, Quan 9, TP HCM", Gender.FEMALE, "001099012843", LocalDate.of(1991, 3, 3), ObjectStatus.ACTIVE),
                    new Customer("CUS-000015", "Hoang Van O", "0912345692", "000 Duong EFG, Quan 10, TP HCM", Gender.MALE, "001099012854", LocalDate.of(1993, 7, 21), ObjectStatus.ACTIVE),
                    new Customer("CUS-000016", "Nguyen Van P", "0912345693", "123 Duong HIJ, Quan 11, TP HCM", Gender.MALE, "001099012865", LocalDate.of(1990, 4, 4), ObjectStatus.ACTIVE),
                    new Customer("CUS-000017", "Le Thi Q", "0912345694", "234 Duong KLM, Quan 12, TP HCM", Gender.FEMALE, "001099012876", LocalDate.of(1988, 6, 6), ObjectStatus.ACTIVE),
                    new Customer("CUS-000018", "Tran Van R", "0912345695", "345 Duong NOP, Quan 1, TP HCM", Gender.MALE, "001099012887", LocalDate.of(1995, 8, 8), ObjectStatus.ACTIVE),
                    new Customer("CUS-000019", "Pham Thi S", "0912345696", "456 Duong QRS, Quan 2, TP HCM", Gender.FEMALE, "001099012898", LocalDate.of(1994, 5, 5), ObjectStatus.ACTIVE),
                    new Customer("CUS-000020", "Hoang Van T", "0912345697", "567 Duong TUV, Quan 3, TP HCM", Gender.MALE, "001099012909", LocalDate.of(1990, 2, 2), ObjectStatus.ACTIVE),
                    new Customer("CUS-000021", "Nguyen Van U", "0912345698", "678 Duong WXY, Quan 4, TP HCM", Gender.MALE, "001099012910", LocalDate.of(1989, 11, 11), ObjectStatus.ACTIVE),
                    new Customer("CUS-000022", "Le Thi V", "0912345699", "789 Duong ZAB, Quan 5, TP HCM", Gender.FEMALE, "001099012921", LocalDate.of(1992, 9, 9), ObjectStatus.ACTIVE),
                    new Customer("CUS-000023", "Tran Van W", "0912345700", "890 Duong CDE, Quan 6, TP HCM", Gender.MALE, "001099012932", LocalDate.of(1993, 10, 10), ObjectStatus.ACTIVE),
                    new Customer("CUS-000024", "Pham Thi X", "0912345701", "901 Duong FGHI, Quan 7, TP HCM", Gender.FEMALE, "001099012943", LocalDate.of(1987, 12, 12), ObjectStatus.ACTIVE),
                    new Customer("CUS-000025", "Hoang Van Y", "0912345702", "012 Duong JKL, Quan 8, TP HCM", Gender.MALE, "001099012954", LocalDate.of(1988, 1, 1), ObjectStatus.ACTIVE),
                    new Customer("CUS-000026", "Nguyen Van Z", "0912345703", "123 Duong MNO, Quan 9, TP HCM", Gender.MALE, "001099012965", LocalDate.of(1991, 4, 4), ObjectStatus.ACTIVE),
                    new Customer("CUS-000027", "Le Thi AA", "0912345704", "234 Duong PQR, Quan 10, TP HCM", Gender.FEMALE, "001099012976", LocalDate.of(1990, 12, 12), ObjectStatus.ACTIVE),
                    new Customer("CUS-000028", "Tran Van AB", "0912345705", "345 Duong STU, Quan 1, TP HCM", Gender.MALE, "001099012987", LocalDate.of(1986, 7, 7), ObjectStatus.ACTIVE),
                    new Customer("CUS-000029", "Pham Thi AC", "0912345706", "456 Duong VWX, Quan 2, TP HCM", Gender.FEMALE, "001099012998", LocalDate.of(1994, 3, 3), ObjectStatus.ACTIVE),
                    new Customer("CUS-000030", "Hoang Van AD", "0912345707", "567 Duong YZA, Quan 3, TP HCM", Gender.MALE, "001099013000", LocalDate.of(1992, 6, 6), ObjectStatus.ACTIVE)
            );

            for (Customer customer : customers) {
                em.persist(customer);
            }

            tx.commit();
            System.out.println("Dữ liệu Customer đã được tạo thành công");
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
    }


    // =================================================================
    // Hàm tạo dữ liệu nhân viên và tài khoản
    // =================================================================
    public static void initEmployeeAndAccountData(EntityManager em) {
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
            System.out.println("Dữ liệu Employee và Account đã được tạo thành công");
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
    }


    // =================================================================
    // Hàm tạo dữ liệu cho danh mục dịch vụ và dịch vụ khách sạn
    // =================================================================
    public static void initServiceCategoryAndHotelService(EntityManager em) {
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            List<ServiceCategory> categories = List.of(
                    new ServiceCategory("SC-000001", "Giải trí", ObjectStatus.ACTIVE, "karaoke"),
                    new ServiceCategory("SC-000002", "Ăn uống", ObjectStatus.ACTIVE, "food"),
                    new ServiceCategory("SC-000003", "Chăm sóc và sức khỏe", ObjectStatus.ACTIVE, "massage"),
                    new ServiceCategory("SC-000004", "Vận chuyển", ObjectStatus.ACTIVE, "car")
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
            System.out.println("Dữ liệu Service Category và Hotel Service đã được tạo thành công");
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
    }

    // =================================================================
    // Hàm tạo dữ liệu cho loại phòng
    // =================================================================
    public static void initRoomCategoryAndRoomData(EntityManager em) {
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Tạo danh sách RoomCategory
            RoomCategory rc1 = new RoomCategory("RC-000001", "Phòng Thường Giường Đơn", 1, 150000.0, 800000.0, ObjectStatus.ACTIVE, null);
            RoomCategory rc2 = new RoomCategory("RC-000002", "Phòng Thường Giường Đôi", 2, 200000.0, 850000.0, ObjectStatus.ACTIVE, null);
            RoomCategory rc3 = new RoomCategory("RC-000003", "Phòng VIP Giường Đơn", 1, 300000.0, 1600000.0, ObjectStatus.ACTIVE, null);
            RoomCategory rc4 = new RoomCategory("RC-000004", "Phòng VIP Giường Đôi", 2, 400000.0, 1800000.0, ObjectStatus.ACTIVE, null);

            em.persist(rc1);
            em.persist(rc2);
            em.persist(rc3);
            em.persist(rc4);

            // Tạo danh sách Room
            List<Room> rooms = List.of(
                    new Room("T1101", RoomStatus.AVAILABLE, LocalDateTime.of(2024, 9, 28, 10, 0), ObjectStatus.ACTIVE, rc1),
                    new Room("V2102", RoomStatus.AVAILABLE, LocalDateTime.of(2024, 9, 28, 10, 0), ObjectStatus.ACTIVE, rc4),
                    new Room("T1203", RoomStatus.AVAILABLE, LocalDateTime.of(2024, 9, 28, 10, 0), ObjectStatus.ACTIVE, rc1),
                    new Room("V2304", RoomStatus.AVAILABLE, LocalDateTime.of(2024, 9, 28, 10, 0), ObjectStatus.ACTIVE, rc4),
                    new Room("T1105", RoomStatus.AVAILABLE, LocalDateTime.of(2024, 9, 28, 10, 0), ObjectStatus.ACTIVE, rc1),
                    new Room("V2206", RoomStatus.AVAILABLE, LocalDateTime.of(2024, 9, 28, 10, 0), ObjectStatus.ACTIVE, rc4),
                    new Room("T1307", RoomStatus.AVAILABLE, LocalDateTime.of(2024, 9, 28, 10, 0), ObjectStatus.ACTIVE, rc1),
                    new Room("V2408", RoomStatus.AVAILABLE, LocalDateTime.of(2024, 9, 28, 10, 0), ObjectStatus.ACTIVE, rc4),
                    new Room("T1109", RoomStatus.AVAILABLE, LocalDateTime.of(2024, 9, 28, 10, 0), ObjectStatus.ACTIVE, rc1),
                    new Room("V2210", RoomStatus.AVAILABLE, LocalDateTime.of(2024, 9, 28, 10, 0), ObjectStatus.ACTIVE, rc4),
                    new Room("V2311", RoomStatus.AVAILABLE, LocalDateTime.of(2024, 9, 28, 10, 0), ObjectStatus.ACTIVE, rc2),
                    new Room("V2312", RoomStatus.AVAILABLE, LocalDateTime.of(2024, 9, 28, 10, 0), ObjectStatus.ACTIVE, rc2),
                    new Room("V2313", RoomStatus.AVAILABLE, LocalDateTime.of(2024, 9, 28, 10, 0), ObjectStatus.ACTIVE, rc2),
                    new Room("V2314", RoomStatus.AVAILABLE, LocalDateTime.of(2024, 9, 28, 10, 0), ObjectStatus.ACTIVE, rc1),
                    new Room("V2315", RoomStatus.AVAILABLE, LocalDateTime.of(2024, 9, 28, 10, 0), ObjectStatus.ACTIVE, rc1),
                    new Room("V2316", RoomStatus.AVAILABLE, LocalDateTime.of(2024, 9, 28, 10, 0), ObjectStatus.ACTIVE, rc1)
            );

            for (Room r : rooms) {
                em.persist(r);
            }

            tx.commit();
            System.out.println("Dữ liệu RoomCategory và Room đã được khởi tạo thành công!");
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
    }


    // =================================================================
    // Hàm tạo dữ liệu cho Global Sequence
    // =================================================================
    public static void initGlobalSequenceData(EntityManager em) {
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            List<GlobalSequence> globalSequences = List.of(
                    new GlobalSequence(0, "Employee", "EMP-000006"),
                    new GlobalSequence(0, "Account", "ACC-000006"),
                    new GlobalSequence(0, "ServiceCategory", "SC-000005"),
                    new GlobalSequence(0, "HotelService", "HS-000021"),
                    new GlobalSequence(0, "Customer", "CUS-000031"),
                    new GlobalSequence(0, "RoomCategory", "RC-000005"),
                    new GlobalSequence(0, "ReservationForm", "RF-000001"),
                    new GlobalSequence(0, "ReservationRoomDetail", "RRD-000001"),
                    new GlobalSequence(0, "HistoryCheckin", "HCI-000001"),
                    new GlobalSequence(0, "RoomUsageService", "RUS-000001")
            );

            for (GlobalSequence gs : globalSequences) {
                em.persist(gs);
            }

            tx.commit();
            System.out.println("Dữ liệu GlobalSequence đã được khởi tạo thành công!");
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
    }


}
