package iuh.fit;

import iuh.fit.models.*;
import iuh.fit.models.enums.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Admin 1/13/2025
 **/
public class Main {
    public static void main(String[] args) {
        Faker faker = new Faker();

        try (
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("mssql")
        ) {
            EntityManager em = emf.createEntityManager();
            try{
                em.getTransaction().begin();

                generateFakeCustomerData(faker, em);
                generateFakeEmployeeAndAccountData(faker, em);
                generateFakeRoomAndRoomCategoryData(faker, em);
                generateFakeHotelServiceAndServiceCategoryData(faker, em);
                generateFakeShiftAndShiftAssignmentData(faker, em);

                em.getTransaction().commit();
            }catch(Exception e){
                e.printStackTrace();
                em.getTransaction().rollback();
            }finally {
                em.close();
            }

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
            roomCategories.stream().skip(faker.number().numberBetween(0, 10)).findFirst().ifPresent(room::setRoomCategory);

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

    private static void generateFakeShiftAndShiftAssignmentData(Faker faker, EntityManager em){
        Random random = new Random();
        Set<Shift> shifts = new HashSet<>();
        List<Employee> employees = em.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();

        for(int i = 0; i < 10; i++){
            Shift shift = new Shift();

            shift.setShiftID("S-" + String.format("%06d", (i + 1)));
            shift.setStartTime(LocalTime.of(faker.number().numberBetween(0, 24), faker.number().numberBetween(0, 60)));
            shift.setEndTime(LocalTime.of(faker.number().numberBetween(0, 24), faker.number().numberBetween(0, 60)));
            shift.setShiftDaysSchedule(faker.options().option(ShiftDaysSchedule.class));
            shift.setModifiedDate(LocalDateTime.now());
            shift.setNumberOfHour(faker.number().numberBetween(1, 12));

            em.persist(shift);
            shifts.add(shift);
        }

        List<Employee> seletedEmployees = employees.stream()
                .filter(employee -> random.nextInt(2) == 1)
                .skip(random.nextInt(employees.size())).toList();

        seletedEmployees
                .forEach(employee->{
                    Set<Shift> selectedShifts = shifts.stream()
                            .skip(random.nextInt(shifts.size()))
                            .limit(random.nextInt(4))
                            .collect(Collectors.toSet());

                    System.out.println(employee);
                    selectedShifts.forEach(shift -> System.out.println(shift.toString()));

                    employee.setShifts(selectedShifts);

                    // Cập nhật hai chiều: Thêm Employee vào employees của Shift
                    selectedShifts.forEach(shift -> shift.getEmployees().add(employee));

                    // Persist những thay đổi (cập nhật quan hệ ManyToMany)
                    selectedShifts.forEach(em::merge);
                    em.merge(employee);
                });

    }
}
