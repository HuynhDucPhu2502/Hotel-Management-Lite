package iuh.fit;

import iuh.fit.dao.*;
import iuh.fit.models.*;
import iuh.fit.models.enums.*;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Admin 1/13/2025
 **/
public class Main {
    public static void main(String[] args) {
        Faker faker = new Faker();

        //generateFakeData(faker);
        testCRUD(faker);


        EntityManagerUtil.close();
    }

    // ==================================================================================================================
    // Tạo Dữ Liệu
    // ==================================================================================================================
    private static void generateFakeData(Faker faker) {

        try (
                EntityManager em = EntityManagerUtil.getEntityManager()
        ) {

            try {
                em.getTransaction().begin();
                generateFakeCustomerData(faker, em);
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
                e.printStackTrace();
            }

            try {
                em.getTransaction().begin();
                generateFakeEmployeeAndAccountData(faker, em);
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
                e.printStackTrace();
            }

            try {
                em.getTransaction().begin();
                generateFakeRoomAndRoomCategoryData(faker, em);
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
                e.printStackTrace();
            }

            try {
                em.getTransaction().begin();
                generateFakeHotelServiceAndServiceCategoryData(faker, em);
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
                e.printStackTrace();
            }

            try {
                em.getTransaction().begin();
                generateReservationFormData(faker, em);
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
                e.printStackTrace();
            }

            try {
                em.getTransaction().begin();
                generateFakerRoomUsageService(faker, em);
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
                e.printStackTrace();
            }

            try {
                em.getTransaction().begin();
                generateHistoryCheckinAndRoomReservationDetailData(faker, em);
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
                e.printStackTrace();
            }

            try {
                em.getTransaction().begin();
                generateHistoryCheckoutData(faker, em);
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
                e.printStackTrace();
            }

            try {
                em.getTransaction().begin();
                generateFakeShiftAndShiftAssignmentData(faker, em);
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
                e.printStackTrace();
            }



        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    private static void generateHistoryCheckoutData(Faker faker, EntityManager em) {
        List<ReservationForm> rfs = ReservationFormDAO.findAll();

        int historyCheckOuntCount = 0;
        for(int i = 0; i < rfs.size(); i++) {

            ReservationForm rf = rfs.get(i);
            ReservationStatus rfStatus = rf.getReservationStatus();

            if (rfStatus.equals(ReservationStatus.CHECKED_OUT)) {
                HistoryCheckOut hco = new HistoryCheckOut();
                hco.setRoomHistoryCheckOutID("HCO-" + String.format("%06d", ++historyCheckOuntCount));
                hco.setDateOfCheckingOut(
                        rf.getApproxcheckOutTime().plusDays(1)
                );
                hco.setReservationForm(rf);

                em.persist(hco);

                Invoice invoice = new Invoice();
                invoice.setInvoiceID("INV-" + String.format("%06d", ++historyCheckOuntCount));
                invoice.setInvoiceDate(rf.getApproxcheckOutTime());
                invoice.setReservationForm(rf);

                double roomCharges = faker.number().numberBetween(1000, 5000);
                double serviceCharges = faker.number().numberBetween(300, 800);
                double total = roomCharges + serviceCharges;

                invoice.setSubTotal(total);
                invoice.setRoomCharges(roomCharges);
                invoice.setServiceCharges(serviceCharges);
                invoice.setTaxCharge(total * 0.1);
                invoice.setTotalDue(total * 1.1);


                em.persist(invoice);
            }
        }
    }

    // Tạo dữ liệu HistoryCheckIn
    private static void generateHistoryCheckinAndRoomReservationDetailData(Faker faker, EntityManager em) {
        List<ReservationForm> rfs = ReservationFormDAO.findAll();

        int historyCheckOutCount = 0;
        int reservationFormCount = 0;
        for(int i = 0; i < rfs.size(); i++){

            ReservationForm rf = rfs.get(i);
            ReservationStatus rfStatus = rf.getReservationStatus();

            if (
                    rfStatus.equals(ReservationStatus.CHECKED_IN)
                            || rfStatus.equals(ReservationStatus.CHECKED_OUT)
            ) {
                HistoryCheckIn hci = new HistoryCheckIn();

                hci.setRoomHistoryCheckinID("HCI-" + String.format("%06d", ++historyCheckOutCount));
                hci.setCheckInDate(
                        rf.getApproxcheckInDate().plusHours(faker.number().numberBetween(0, 1))
                );
                hci.setReservationForm(rf);

                if (rfStatus.equals(ReservationStatus.CHECKED_IN))
                    rf.getRoom().setRoomStatus(RoomStatus.IN_USE);

                ReservationRoomDetail reservationRoomDetail = new ReservationRoomDetail();
                reservationRoomDetail.setRoom(rf.getRoom());
                reservationRoomDetail.setReservationForm(rf);
                reservationRoomDetail.setDateChanged(hci.getCheckInDate());
                reservationRoomDetail.setReservationRoomDetailID("RRD-" + String.format("%06d", ++reservationFormCount));

                em.persist(hci);
                em.persist(reservationRoomDetail);
            }
        }
    }

    // Tạo dữ liệu RoomUsageService
    private static void generateFakerRoomUsageService(Faker faker, EntityManager em) {
        List<ReservationForm> rfs = ReservationFormDAO.findAll();
        List<HotelService> hs = HotelServiceDAO.findAll();

        int count = 0;
        for(int i = 0; i < rfs.size(); i++) {

            ReservationForm rf = rfs.get(i);
            ReservationStatus rfStatus = rf.getReservationStatus();

            if (
                    rfStatus.equals(ReservationStatus.CHECKED_IN)
                            || rfStatus.equals(ReservationStatus.CHECKED_OUT)
            ) {
                int numberOfService = faker.number().numberBetween(1, 5);

                for (int j = 0; j < numberOfService; j++) {
                    HotelService hotelService = hs.get(faker.number().numberBetween(0, hs.size() - 1));
                    LocalDateTime dayAdded = rfStatus.equals(ReservationStatus.CHECKED_IN)
                            ? LocalDateTime.now()
                            : rf.getApproxcheckOutTime().minusDays(faker.number().numberBetween(1, 2));

                    RoomUsageService rus = new RoomUsageService();

                    rus.setRoomUsageServiceID("RUS-" + String.format("%06d", ++count));
                    rus.setQuantity(faker.number().numberBetween(1, 10));
                    rus.setDayAdded(dayAdded);
                    rus.setHotelService(hotelService);
                    rus.setReservationForm(rf);
                    rus.setUnitPrice(hotelService.getServicePrice());

                    em.persist(rus);
                }
            }

        }
    }

    // Tạo dữ liệu ReservationForm
    private static void generateReservationFormData(Faker faker, EntityManager em) {
        List<Employee> emps = EmployeeDAO.findAll();
        List<Customer> cus = CustomerDAO.findAll();
        List<Room> rooms = RoomDAO.findAll();


        // Tạo phiếu cho trường hợp IN_USE, RESERVATION
        for (int i = 0; i < 10; i++) {
            ReservationForm rf = new ReservationForm();

            LocalDateTime now = LocalDateTime.now();

            LocalDateTime rfDate = now.minusDays(new Random().nextInt(1, 4));
            LocalDateTime rfCheckinDate = now.plusDays(new Random().nextInt(-5, 5));
            LocalDateTime rfCheckoutDate = now.plusDays(new Random().nextInt(6, 10));


            ReservationStatus reservationStatus = rfCheckinDate.isAfter(now)
                    ? ReservationStatus.RESERVATION : faker.options().option(ReservationStatus.CANCEL, ReservationStatus.CHECKED_IN);

            rf.setReservationID("RF-" + String.format("%06d", (i + 1)));
            rf.setReservationStatus(reservationStatus);

            rf.setReservationDate(rfDate);
            rf.setApproxcheckInDate(rfCheckinDate);
            rf.setApproxcheckOutTime(rfCheckoutDate);


            rf.setCustomer(cus.get(i));
            rf.setEmployee(emps.get(i));
            rf.setRoom(rooms.get(i));

            em.persist(rf);
        }

        //Tạo phiếu cho trường hợp OVER_DUE
        for (int i = 11; i < 15; i++) {
            ReservationForm rf = new ReservationForm();


            LocalDateTime now = LocalDateTime.now();

            LocalDateTime rfDate = now.minusDays(new Random().nextInt(12,18));
            LocalDateTime rfCheckinDate = rfDate.plusDays(new Random().nextInt(1, 2));
            LocalDateTime rfCheckoutDate = rfDate.plusDays(new Random().nextInt(5, 8));

            rf.setReservationID("RF-" + String.format("%06d", i));
            rf.setReservationStatus(ReservationStatus.CHECKED_OUT);

            rf.setReservationDate(rfDate);
            rf.setApproxcheckInDate(rfCheckinDate);
            rf.setApproxcheckOutTime(rfCheckoutDate);


            rf.setCustomer(cus.get(i-11));
            rf.setEmployee(emps.get(i-11));
            rf.setRoom(rooms.get(i-11));

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
            customer.setCustomerCode("CUS-" + String.format("%06d", i));

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
            employee.setEmployeeCode("EMP-" + String.format("%06d", i));
            employee.setPosition(faker.options().option(Position.class));

            Account account = new Account();
            account.setAccountID("ACC-" + String.format("%06d", i));
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
            room.setRoomStatus(RoomStatus.AVAILABLE);
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

    // Tạo dữ liệu Shift, ShiftAssignment
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

        List<Employee> selectedEmployees = new ArrayList<>();

        for (Employee e : employees) {
            if (random.nextInt(2) == 1) {
                selectedEmployees.add(e);
            }
        }

        selectedEmployees.forEach(employee -> {
            // Chọn một số ca làm việc ngẫu nhiên
            Set<Shift> selectedShifts = shifts.stream()
                    .skip(random.nextInt(shifts.size()))
                    .collect(Collectors.toSet());


            final int[] counter = {0};

            // Gán shifts vào employee thông qua ShiftAssignment
            selectedShifts.forEach(shift -> {
                ShiftAssignment shiftAssignment = new ShiftAssignment();
                shiftAssignment.setShiftAssignmentID("SA-" + String.format("%06d", (counter[0] + 1)));
                shiftAssignment.setShift(shift);
                shiftAssignment.setEmployee(employee);
                shiftAssignment.setDescription(faker.lorem().sentence());
                counter[0]++;

                // Persist đối tượng ShiftAssignment
                shift.getShiftAssignments().add(shiftAssignment);
                employee.getShiftAssignments().add(shiftAssignment);

                em.merge(shiftAssignment);

                em.merge(shift);
                em.merge(employee);
            });
        });
    }

    // ==================================================================================================================
    // CRUD
    // ==================================================================================================================
    private static void testCRUD(Faker faker) {

        testCRUDCustomer(faker);
        testCRUDEmployee(faker);
        testCRUDAccount(faker);
        testCRUDReservationForm(faker);
        testCRUDServiceCategory(faker);
        testCRUDHistoryCheckin(faker);
        testCRUDRoomAndRoomCategory(faker);
        testCRUDShiftAndShiftAssignment(faker);

    }

    private static void testCRUDCustomer(Faker faker) {
        System.out.println("\n\n\nCRUD bảng Customer");

        // Create
        Customer newCustomer = new Customer();
        newCustomer.setFullName(faker.name().fullName());
        newCustomer.setPhoneNumber(faker.number().digits(10));
        newCustomer.setAddress(faker.address().fullAddress());
        newCustomer.setGender(faker.options().option(Gender.class));
        newCustomer.setDob(LocalDate.now().minusYears(faker.number().numberBetween(18, 60)));
        newCustomer.setIsActivate(faker.options().option(ObjectStatus.class));
        newCustomer.setIdCardNumber(faker.number().digits(12));
        newCustomer.setCustomerCode("CUS-" + String.format("%06d", 11));
        CustomerDAO.create(newCustomer);

        System.out.println("Tạo Customer: " + newCustomer.getCustomerCode());

        // Read
        System.out.println("Đọc Customer: " + newCustomer.getCustomerCode());
        Customer customer = CustomerDAO.findById("CUS-000011");
        System.out.println(customer);

        // Update

        customer.setFullName("Test");
        CustomerDAO.update(customer);

        System.out.println("Đọc lại Customer khi đổi tên: " + newCustomer.getCustomerCode());
        Customer updatedCustomer = CustomerDAO.findById("CUS-000011");
        System.out.println(updatedCustomer);

        // Delete
        CustomerDAO.delete("CUS-000011");

        System.out.println("Xóa customer: " + customer.getCustomerCode());
        Customer deletedCustomer = CustomerDAO.findById("CUS-000011");
        System.out.println(deletedCustomer);

    }

    private static void testCRUDEmployee(Faker faker) {
        System.out.println("\n\n\nCRUD bảng Employee");

        // Create Employee
        Employee newEmployee = new Employee();
        newEmployee.setFullName(faker.name().fullName());
        newEmployee.setPhoneNumber(faker.number().digits(10));
        newEmployee.setAddress(faker.address().fullAddress());
        newEmployee.setGender(faker.options().option(Gender.class));
        newEmployee.setDob(LocalDate.now().minusYears(faker.number().numberBetween(18, 60)));
        newEmployee.setIsActivate(faker.options().option(ObjectStatus.class));
        newEmployee.setIdCardNumber(faker.number().digits(12));
        newEmployee.setEmployeeCode("EMP-" + String.format("%06d", 11));
        newEmployee.setPosition(faker.options().option(Position.class));
        EmployeeDAO.create(newEmployee);

        System.out.println("Tạo Employee: " + newEmployee.getEmployeeCode());

        // Read
        System.out.println("Đọc Employee: " + newEmployee.getEmployeeCode());
        Employee employee = EmployeeDAO.findById("EMP-000011");
        System.out.println(employee);

        // Update
        employee.setFullName("Test");
        EmployeeDAO.update(employee);

        System.out.println("Đọc lại Employee khi đổi tên: " + employee.getEmployeeCode());
        Employee updatedEmployee = EmployeeDAO.findById("EMP-000011");
        System.out.println(updatedEmployee);

        // Delete
        EmployeeDAO.delete("EMP-000011");

        System.out.println("Xóa Employee: " +employee.getEmployeeCode());
        Employee deletedEmployee = EmployeeDAO.findById("EMP-000011");
        System.out.println(deletedEmployee);

    }

    private static void testCRUDAccount(Faker faker) {
        System.out.println("\n\n\nCRUD bảng Account");

        // Create Employee
        Employee newEmployee = new Employee();
        newEmployee.setFullName(faker.name().fullName());
        newEmployee.setPhoneNumber(faker.number().digits(10));
        newEmployee.setAddress(faker.address().fullAddress());
        newEmployee.setGender(faker.options().option(Gender.class));
        newEmployee.setDob(LocalDate.now().minusYears(faker.number().numberBetween(18, 60)));
        newEmployee.setIsActivate(faker.options().option(ObjectStatus.class));
        newEmployee.setIdCardNumber(faker.number().digits(12));
        newEmployee.setEmployeeCode("EMP-" + String.format("%06d", 12));
        newEmployee.setPosition(faker.options().option(Position.class));

        // Create Account
        Account newAccount = new Account();
        newAccount.setAccountID("ACC-" + String.format("%06d", 12));
        newAccount.setUserName(faker.name().username());
        newAccount.setPassword(faker.internet().password());
        newAccount.setStatus(AccountStatus.ACTIVE);
        newAccount.setEmployee(newEmployee);

        AccountDAO.create(newAccount);

        System.out.println("Tạo Account: " + newAccount.getAccountID());

        // Read
        System.out.println("Đọc Account: " + newAccount.getAccountID());
        Account account = AccountDAO.findById("ACC-000012");
        System.out.println(account);

        // Update
        account.setStatus(AccountStatus.LOCKED);
        AccountDAO.update(account);

        System.out.println("Đọc lại Account khi đổi status: " + account.getAccountID());
        Account updatedAccount = AccountDAO.findById("ACC-000012");
        System.out.println(updatedAccount);

        // Delete
        AccountDAO.delete("ACC-000012");

        System.out.println("Xóa Employee: " + account.getAccountID());
        Account deletedAccount = AccountDAO.findById("ACC-000012");
        System.out.println(deletedAccount);

    }

    private static void testCRUDReservationForm(Faker faker) {
        System.out.println("\n\n\nCRUD bảng ReservationForm");


        // Create Customer
        Customer newCustomer = new Customer();
        newCustomer.setFullName(faker.name().fullName());
        newCustomer.setPhoneNumber(faker.number().digits(10));
        newCustomer.setAddress(faker.address().fullAddress());
        newCustomer.setGender(faker.options().option(Gender.class));
        newCustomer.setDob(LocalDate.now().minusYears(faker.number().numberBetween(18, 60)));
        newCustomer.setIsActivate(faker.options().option(ObjectStatus.class));
        newCustomer.setIdCardNumber(faker.number().digits(12));
        newCustomer.setCustomerCode("CUS-" + String.format("%06d", 12));
        CustomerDAO.create(newCustomer);

        // Create Employee
        Employee newEmployee = new Employee();
        newEmployee.setFullName(faker.name().fullName());
        newEmployee.setPhoneNumber(faker.number().digits(10));
        newEmployee.setAddress(faker.address().fullAddress());
        newEmployee.setGender(faker.options().option(Gender.class));
        newEmployee.setDob(LocalDate.now().minusYears(faker.number().numberBetween(18, 60)));
        newEmployee.setIsActivate(faker.options().option(ObjectStatus.class));
        newEmployee.setIdCardNumber(faker.number().digits(12));
        newEmployee.setEmployeeCode("EMP-" + String.format("%06d", 13));
        newEmployee.setPosition(faker.options().option(Position.class));
        EmployeeDAO.create(newEmployee);

        Room room = RoomDAO.findById("R-000016");

        ReservationForm newReservationForm = new ReservationForm();

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime rfDate = now.minusDays(new Random().nextInt(1, 4));
        LocalDateTime rfCheckinDate = now.plusDays(new Random().nextInt(1, 5));
        LocalDateTime rfCheckoutDate = now.plusDays(new Random().nextInt(6, 10));


        ReservationStatus reservationStatus = rfCheckinDate.isAfter(now)
                ? ReservationStatus.RESERVATION : faker.options().option(ReservationStatus.CANCEL, ReservationStatus.CHECKED_IN);

        newReservationForm.setReservationID("RF-" + String.format("%06d", 16));
        newReservationForm.setReservationStatus(reservationStatus);

        newReservationForm.setReservationDate(rfDate);
        newReservationForm.setApproxcheckInDate(rfCheckinDate);
        newReservationForm.setApproxcheckOutTime(rfCheckoutDate);


        newReservationForm.setCustomer(newCustomer);
        newReservationForm.setEmployee(newEmployee);
        newReservationForm.setRoom(room);

        // CREATE
        ReservationFormDAO.create(newReservationForm);
        System.out.println("Tạo ReservationForm: " + newReservationForm.getReservationID());

        // READ
        System.out.println("Đọc ReservationForm: " + newReservationForm.getReservationID());
        ReservationForm reservationForm = ReservationFormDAO.findById("RF-000016");
        System.out.println(reservationForm);

        // UPDATE
        reservationForm.setReservationStatus(ReservationStatus.CANCEL);
        ReservationFormDAO.update(reservationForm);

        System.out.println("Đọc lại ReservationForm khi đổi status: " + reservationForm.getReservationID());
        ReservationForm updatedReservationForm = ReservationFormDAO.findById("RF-000016");
        System.out.println(updatedReservationForm);

        // DELETE
        ReservationFormDAO.delete("RF-000016");
        System.out.println("Xóa ReservationForm: " + reservationForm.getReservationID());
        ReservationForm deletedReservationForm = ReservationFormDAO.findById("RF-000016");
        System.out.println(deletedReservationForm);
    }

    private static void testCRUDServiceCategory(Faker faker) {
        System.out.println("\n\n\nCRUD bảng ServiceCategory");

        // Create ServiceCategory
        ServiceCategory serviceCategory = new ServiceCategory();

        serviceCategory.setServiceCategoryID("SC-" + String.format(
                "%06d",
                new Random().nextInt(100, 200))
        );
        serviceCategory.setServiceCategoryName(faker.name().fullName());
        serviceCategory.setIsActivate(ObjectStatus.ACTIVE);

        ServiceCategoryDAO.create(serviceCategory);

        System.out.println("Tạo ServiceCategory: " + ServiceCategoryDAO.findById(serviceCategory.getServiceCategoryID()));

        // READ
        System.out.println("Đọc ServiceCategory: " + serviceCategory.getServiceCategoryID());
        System.out.println(ServiceCategoryDAO.findById(serviceCategory.getServiceCategoryID()));

        // delete
        if(ServiceCategoryDAO.delete(serviceCategory.getServiceCategoryID()))
            System.out.println("Xóa ServiceCategory thanh cong");

        // UPDATE
        serviceCategory.setIsActivate(ObjectStatus.INACTIVE);
        ServiceCategoryDAO.update(serviceCategory);

        System.out.println("Đọc lại ServiceCategory khi đổi status: " + serviceCategory.getServiceCategoryID());
        ServiceCategory updatedServiceCategory = ServiceCategoryDAO.findById(serviceCategory.getServiceCategoryID());
        System.out.println(updatedServiceCategory);
    }

    private static void testCRUDHistoryCheckin(Faker faker) {
        List<Employee> emps = EmployeeDAO.findAll();
        List<Customer> cus = CustomerDAO.findAll();
        List<Room> rooms = RoomDAO.findAll();

        System.out.println("\n\n\nCRUD bảng HistoryCheckin");

        // Create HistoryCheckin

        ReservationForm rf = new ReservationForm();
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime rfDate = now.minusDays(new Random().nextInt(12,18));
        LocalDateTime rfCheckinDate = rfDate.plusDays(new Random().nextInt(1, 2));
        LocalDateTime rfCheckoutDate = rfDate.plusDays(new Random().nextInt(5, 8));

        rf.setReservationID("RF-" + String.format(
                "%06d",
                new Random().nextInt(100, 200))
        );
        rf.setReservationStatus(ReservationStatus.CHECKED_OUT);

        rf.setReservationDate(rfDate);
        rf.setApproxcheckInDate(rfCheckinDate);
        rf.setApproxcheckOutTime(rfCheckoutDate);


        rf.setCustomer(cus.get(1));
        rf.setEmployee(emps.get(1));
        rf.setRoom(rooms.get(1));

        ReservationFormDAO.create(rf);

        HistoryCheckIn hci = new HistoryCheckIn();

        hci.setRoomHistoryCheckinID("HCI-" + String.format(
                "%06d",
                new Random().nextInt(100, 200)));
        hci.setCheckInDate(
                rf.getApproxcheckInDate().plusHours(faker.number().numberBetween(0, 1))
        );
        hci.setReservationForm(rf);

        HistoryCheckInDAO.create(hci);

        System.out.println("Tạo HistoryCheckin: " + HistoryCheckInDAO.findById(hci.getRoomHistoryCheckinID()));

        // READ
        System.out.println("Đọc HistoryCheckin: " + hci.getRoomHistoryCheckinID());
        System.out.println(HistoryCheckInDAO.findById(hci.getRoomHistoryCheckinID()));

        // delete
        if(HistoryCheckInDAO.delete(hci.getRoomHistoryCheckinID()))
            System.out.println("Xóa HistoryCheckin thanh cong");

        // UPDATE
        hci.setCheckInDate(LocalDateTime.now());
        HistoryCheckInDAO.update(hci);

        System.out.println("Đọc lại HistoryCheckin khi đổi status: " + hci.getRoomHistoryCheckinID());
        HistoryCheckIn updatedHistoryCheckIn = HistoryCheckInDAO.findById(hci.getRoomHistoryCheckinID());
        System.out.println(updatedHistoryCheckIn);
    }
  
  private static void testCRUDRoomAndRoomCategory(Faker faker) {
        //TEST CREATE FOR ROOM AND ROOM CATEGORY
        // Create RoomCategory
        RoomCategory roomCategory = new RoomCategory();
        roomCategory.setRoomCategoryID("RC-" + String.format("%06d", 11));
        roomCategory.setRoomCategoryName(faker.team().name());
        roomCategory.setNumberOfBed(faker.number().numberBetween(1, 4));
        roomCategory.setHourlyPrice(faker.number().randomDouble(2, 100, 500));
        roomCategory.setDailyPrice(faker.number().randomDouble(2, 1000, 5000));
        roomCategory.setIsActivate(ObjectStatus.ACTIVE);
        RoomCategoryDAO.create(roomCategory);

        // Create Room
        Room newRoom = new Room();
        newRoom.setRoomID("R-" + String.format("%06d", 21));
        newRoom.setRoomStatus(RoomStatus.AVAILABLE);
        newRoom.setIsActivate(faker.options().option(ObjectStatus.class));
        newRoom.setDateOfCreation(LocalDateTime.now());
        newRoom.setRoomCategory(roomCategory);
        RoomDAO.create(newRoom);

        System.out.println("Tạo Room: " + newRoom.getRoomID());

        //TEST CRUD FOR ROOM
        // Read
        System.out.println("Đọc Room: " + newRoom.getRoomID());
        Room room = RoomDAO.findById("R-000021");
        System.out.println(room);

        // Update
        room.setRoomStatus(RoomStatus.IN_USE);
        RoomDAO.update(room);

        System.out.println("Đọc lại Room khi đổi status: " + room.getRoomID());
        Room updatedRoom = RoomDAO.findById("R-000021");
        System.out.println(updatedRoom);

        // Delete
        RoomDAO.delete("R-000021");
        System.out.println("Xóa Room: " + room.getRoomID());
        Room deletedRoom = RoomDAO.findById("R-000021");
        System.out.println(deletedRoom);


        //TEST CRUD FOR ROOM CATEGORY
        // Read
        System.out.println("Đọc RoomCategory: " + roomCategory.getRoomCategoryID());
        RoomCategory readRoomCategory = RoomCategoryDAO.getById("RC-000011");
        System.out.println(readRoomCategory);

        // Update
        readRoomCategory.setRoomCategoryName("Test");
        RoomCategoryDAO.update(readRoomCategory);

        System.out.println("Đọc lại Room khi đổi status: " + readRoomCategory.getRoomCategoryID());
        RoomCategory updateRoomCategory = RoomCategoryDAO.getById("RC-000011");
        System.out.println(updateRoomCategory);

        // Delete
        RoomDAO.delete(updateRoomCategory.getRoomCategoryID());
        System.out.println("Xóa Room: " + readRoomCategory.getRoomCategoryID());
        RoomCategory deletedRoomCategory = RoomCategoryDAO.getById("R-000021");
        System.out.println(deletedRoomCategory);
    }

    private static void testCRUDShiftAndShiftAssignment(Faker faker){
        Random random = new Random();
        //TEST CREATE FOR SHIFT AND SHIFT ASSIGNMENT
        // Create Shift
        Shift newShift = new Shift();
        newShift.setShiftID("S-" + String.format("%06d", 11));
        newShift.setStartTime(LocalTime.of(faker.number().numberBetween(0, 24), faker.number().numberBetween(0, 60)));
        newShift.setEndTime(LocalTime.of(faker.number().numberBetween(0, 24), faker.number().numberBetween(0, 60)));
        newShift.setShiftDaysSchedule(faker.options().option(ShiftDaysSchedule.class));
        newShift.setModifiedDate(LocalDateTime.now());
        newShift.setNumberOfHour(faker.number().numberBetween(1, 12));
        ShiftDAO.create(newShift);

        System.out.println("Tạo Shift: " + newShift.getShiftID());

        //TEST CRUD FOR SHIFT
        // Read
        System.out.println("Đọc Shift: " + newShift.getShiftID());
        Shift shift = ShiftDAO.findById("S-000011");
        System.out.println(shift);

        // Update
        shift.setNumberOfHour(10);
        ShiftDAO.update(shift);

        System.out.println("Đọc lại Shift khi đổi số giờ: " + shift.getShiftID());
        Shift updatedShift = ShiftDAO.findById("S-000011");
        System.out.println(updatedShift);


        //TEST CREATE FOR SHIFT ASSIGNMENT
        // Create ShiftAssignment
        List<Employee> employees = EmployeeDAO.findAll();
        List<Employee> selectedEmployees = new ArrayList<>();
        for (Employee e : employees) {
            if (random.nextInt(2) == 1) {
                selectedEmployees.add(e);
            }
        }

        final int[] counter = {1};

        selectedEmployees.forEach(employee -> {

            ShiftAssignment shiftAssignment = new ShiftAssignment();
            shiftAssignment.setShiftAssignmentID("SATEST-" + String.format("%06d", (counter[0])));
            shiftAssignment.setShift(shift);
            shiftAssignment.setEmployee(employee);
            shiftAssignment.setDescription(faker.lorem().sentence());
            counter[0]++;

            ShiftAssignmentDAO.create(shiftAssignment, shift, employee);
        });

        //TEST READ AND UPDATE FOR SHIFT ASSIGNMENT
        // Read
        System.out.println("Đọc ShiftAssignment: " + "SATEST-000001");
        ShiftAssignment shiftAssignment = ShiftAssignmentDAO.findById("SATEST-000001");
        System.out.println(shiftAssignment);

        // Update
        shiftAssignment.setDescription("Test");
        ShiftAssignmentDAO.update(shiftAssignment);
        System.out.println("Đọc ShiftAssignment sau khi update: " + shiftAssignment.getShiftAssignmentID());
        System.out.println(shiftAssignment);

        //TEST DELETE FOR SHIFT AND SHIFT ASSIGNMENT

        // Delete ShiftAssignment
        ShiftAssignmentDAO.delete(shiftAssignment.getShiftAssignmentID());
        System.out.println("Đọc ShiftAssignment sau khi delete: " + shiftAssignment.getShiftAssignmentID());
        System.out.println(shiftAssignment);

        // Delete
        ShiftAssignmentDAO.deleteByShift(ShiftDAO.findById("S-000011"));
        ShiftDAO.delete("S-000011");
        System.out.println("Xóa Shift: " + shift.getShiftID());
        Shift deletedShift = ShiftDAO.findById("S-000011");
        System.out.println(deletedShift);
}
