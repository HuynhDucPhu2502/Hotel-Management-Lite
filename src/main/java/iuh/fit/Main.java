package iuh.fit;

import iuh.fit.dao.*;
import iuh.fit.models.*;
import iuh.fit.models.enums.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Admin 1/13/2025
 **/
public class Main {
    public static void main(String[] args) {
        Faker faker = new Faker();

        try (
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("mssql");
                EntityManager em = emf.createEntityManager()
        ) {
            em.getTransaction().begin();

            //generateFakeCustomerData(faker, em);
            //generateFakeEmployeeAndAccountData(faker, em);
            //generateFakeRoomAndRoomCategoryData(faker, em);
            //generateFakeHotelServiceAndServiceCategoryData(faker, em);
            //generateReservationFormData(faker,em);
            //generateFakerRoomUsageService(faker,em);
//            generateHistoryCheckinData(faker, em);
//            generateHistoryCheckoutData(faker, em);

            em.getTransaction().commit();

        } catch (Exception exception) {
            exception.printStackTrace();
        }


    }

    private static void generateHistoryCheckoutData(Faker faker, EntityManager em) {
        List<ReservationForm> rfs = ReservationFormDAO.getData(em);
        for(int i = 0; i < 10; i++){
            HistoryCheckOut hco = new HistoryCheckOut();
            hco.setRoomHistoryCheckOutID("HCO-" + String.format("%06d", (i + 1)));
            hco.setDateOfCheckingOut(
                    rfs.get(i).getApproxcheckOutTime().plusDays(1)
            );
            hco.setReservationForm(rfs.get(i));

            em.persist(hco);
        }
    }

    private static void generateHistoryCheckinData(Faker faker, EntityManager em) {
        List<ReservationForm> rfs = ReservationFormDAO.getData(em);
        for(int i = 0; i < 10; i++){
            HistoryCheckIn hci = new HistoryCheckIn();
            hci.setRoomHistoryCheckinID("HCI-" + String.format("%06d", (i + 1)));
            hci.setCheckInDate(
                    rfs.get(i).getApproxcheckInDate().plusDays(1)
            );
            hci.setReservationForm(rfs.get(i));

            em.persist(hci);
        }
    }

    private static void generateFakerRoomUsageService(Faker faker, EntityManager em) {
        List<ReservationForm> rfs = ReservationFormDAO.getData(em);
        List<HotelService> hs = HotelServiceDAO.getData(em);
        Random rd = new Random();
        for(int i = 0; i < 10; i++) {
            RoomUsageService rus = new RoomUsageService();

            rus.setRoomUsageServiceID("RUS-" + String.format("%06d", (i + 1)));
            rus.setQuantity(rd.nextInt(1, 10));
            rus.setDayAdded(
                    rfs.get(i).getReservationDate()
                            .plusDays(rd.nextInt(1, 5))
            );
            rus.setHotelService(hs.get(i));
            rus.setReservationForm(rfs.get(i));

            em.persist(rus);
        }
    }

    private static void generateReservationFormData(Faker faker, EntityManager em) {
        for (int i = 0; i < 10; i++) {
            List<Employee> emps = EmployeeDAO.getData(em);
            List<Customer> cus = CustomerDAO.getData(em);
            List<Room> rooms = RoomDAO.getData(em);

            ReservationForm rf = new ReservationForm();
            String id = ("RF-" + String.format("%06d", (i + 1)));
            LocalDateTime rfDate = LocalDateTime.now()
                    .minusDays(new Random().nextInt(1, 10));
            LocalDateTime rfCheckinDate = LocalDateTime.now()
                    .plusDays(new Random().nextInt(1, 10));
            LocalDateTime rfCheckoutDate = LocalDateTime.now()
                    .plusDays(new Random().nextInt(11, 20));

            rf.setReservationID(id);
            rf.setReservationDate(rfDate);
            rf.setApproxcheckInDate(rfCheckinDate);
            rf.setApproxcheckOutTime(rfCheckoutDate);
            rf.setReservationStatus(
                    faker.options().option(
                            ReservationStatus.RESERVATION,
                            ReservationStatus.IN_USE
                    )
            );
            rf.setCustomer(cus.get(i));
            rf.setEmployee(emps.get(i));
            rf.setRoom(rooms.get(i));

            em.persist(rf);
        }
    }

    // Tạo dữ liệu Customer
    private static void generateFakeCustomerData(Faker faker, EntityManager em) {
        for (int i = 1; i <= 10; i++) {
            Customer customer = new Customer();
            customer.setFullName(faker.name().fullName());
            customer.setPhoneNumber(faker.number().digits(10));
            customer.setAddress(faker.address().fullAddress());
            customer.setGender(faker.options().option(Gender.class));
            customer.setDob(LocalDate.now().minusYears(faker.number().numberBetween(18, 60)));
            customer.setIsActivate(faker.options().option(ObjectStatus.class));
            customer.setIdCardNumber(faker.number().digits(12));
            customer.setCustomerCode("CUS-" + String.format("%06d", (i + 1)));

            em.persist(customer);
        }
    }

    // Tạo dữ liệu Employee và Account
    private static void generateFakeEmployeeAndAccountData(Faker faker, EntityManager em) {
        for (int i = 1; i <= 10; i++) {
            Employee employee = new Employee();
            employee.setFullName(faker.name().fullName());
            employee.setPhoneNumber(faker.number().digits(10));
            employee.setAddress(faker.address().fullAddress());
            employee.setGender(faker.options().option(Gender.class));
            employee.setDob(LocalDate.now().minusYears(faker.number().numberBetween(18, 60)));
            employee.setIsActivate(faker.options().option(ObjectStatus.class));
            employee.setIdCardNumber(faker.number().digits(12));
            employee.setEmployeeCode("EMP-" + String.format("%06d", (i + 1)));
            employee.setPosition(faker.options().option(Position.class));

            Account account = new Account();
            account.setAccountID("ACC-" + String.format("%06d", (i + 1)));
            account.setUserName(faker.name().username());
            account.setPassword(faker.internet().password());
            account.setStatus(faker.options().option(AccountStatus.class));

            account.setEmployee(employee);

            em.persist(account);
        }
    }

    // Tạo dữ liệu Room và RoomCategory
    private static void generateFakeRoomAndRoomCategoryData(Faker faker, EntityManager em) {
        Set<RoomCategory> roomCategories = new HashSet<>();
        for(int i = 0; i < 10; i++) {
            RoomCategory roomCategory = new RoomCategory();

            roomCategory.setRoomCategoryID("RC-" + String.format("%06d", (i + 1)));
            roomCategory.setRoomCategoryName(faker.team().name());
            roomCategory.setNumberOfBed(faker.number().numberBetween(1, 4));
            roomCategory.setHourlyPrice(faker.number().randomDouble(2, 100, 500));
            roomCategory.setDailyPrice(faker.number().randomDouble(2, 1000, 5000));
            roomCategory.setIsActivate(faker.options().option(ObjectStatus.class));

            em.persist(roomCategory);
            roomCategories.add(roomCategory);
        }

        for(int i = 0; i < 20; i++){
            Room room = new Room();

            room.setRoomID("R-" + String.format("%06d", (i + 1)));
            room.setRoomStatus(faker.options().option(RoomStatus.class));
            room.setIsActivate(faker.options().option(ObjectStatus.class));
            room.setDateOfCreation(LocalDateTime.now());
            room.setRoomCategory(roomCategories.stream().skip(faker.number().numberBetween(0, 10)).findFirst().get());

            em.persist(room);
        }
    }

    // Tạo dữ liệu HotelService, ServiceCategory
    private static void generateFakeHotelServiceAndServiceCategoryData(Faker faker, EntityManager em) {
        // mảng tạo để check xem Faker có
        // tạo ra dữ liệu trùng không.
        // Đừng xóa
        Set<String> uniqueHotelServiceNames = new HashSet<>();
        Set<String> uniqueServiceCategoryNames = new HashSet<>();


        for(int i = 0; i < 10; ++i) {
            ServiceCategory serviceCategory = new ServiceCategory();

            serviceCategory.setServiceCategoryID("SC-" + String.format("%06d", (i + 1)));
            serviceCategory.setIsActivate(faker.options().option(ObjectStatus.class));

            // Tìm service category name sao cho DataFaker generate ra không trùng
            String serviceCategoryName;
            do {
                serviceCategoryName = faker.commerce().department();
            } while (!uniqueServiceCategoryNames.add(serviceCategoryName));
            serviceCategory.setServiceCategoryName(serviceCategoryName);

            em.persist(serviceCategory);

            for (int y = 0; y < 10; y++) {
                HotelService hs = new HotelService();

                hs.setServiceID("HS-" + String.format("%06d", (i * 10 + y + 1)));
                hs.setDescription(faker.lorem().sentence());
                hs.setServicePrice(faker.number().randomDouble(2, 50, 300));
                hs.setIsActivate(faker.options().option(ObjectStatus.class));
                hs.setServiceCategory(serviceCategory);

                // Tìm hotel service name sao cho DataFaker generate ra không trùng
                String hotelServiceName;
                do {
                    hotelServiceName = faker.commerce().productName();
                } while (!uniqueHotelServiceNames.add(hotelServiceName));
                hs.setServiceName(hotelServiceName);

                em.persist(hs);
            }

        }
    }
}
