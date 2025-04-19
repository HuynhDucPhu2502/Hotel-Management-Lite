package iuh.fit.utils;

import iuh.fit.controller.MainController;
import iuh.fit.dao.daoimpl.*;

import iuh.fit.dao.daointerface.EmployeeDAO;
import iuh.fit.dao.daointerface.InvoiceDAO;
import iuh.fit.dao.daointerface.RoomDAO;
import iuh.fit.dao.daointerface.RoomWithReservationDAO;
import iuh.fit.models.Employee;
import iuh.fit.models.ReservationForm;
import iuh.fit.models.Room;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.models.wrapper.RoomWithReservation;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RoomManagementService {
    private static final EmployeeDAO employeeDAO;

    static {
        try {
            employeeDAO = new EmployeeDAOImpl();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private static final InvoiceDAO invoiceDAO;

    static {
        try {
            invoiceDAO = new InvoiceDAOImpl();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private static final RoomDAO roomDAO;

    static {
        try {
            roomDAO = new RoomDAOImpl();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private static final RoomWithReservationDAO roomWithReservationDAO;

    static {
        try {
            roomWithReservationDAO = new RoomWithReservationDAOImpl();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private static final ScheduledExecutorService SCHEDULER =
            Executors.newScheduledThreadPool(1);
    public static final Employee SYSTEM_EMPLOYEE;

    static {
        try {
            SYSTEM_EMPLOYEE = employeeDAO.getEmployeeByEmployeeCode("EMP-000000");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static void startAutoCheckoutScheduler(MainController mainController) {
        SCHEDULER.scheduleAtFixedRate(
                () -> {
                    try {
                        RoomManagementService.autoCheckoutOverdueRooms(mainController);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                },
                0,
                60,
                TimeUnit.SECONDS
        );
    }

    public static void autoCheckoutOverdueRooms(MainController mainController) throws RemoteException {
        List<RoomWithReservation> overdueRooms =
                roomWithReservationDAO.getRoomOverDueWithLatestReservation();

        for (RoomWithReservation roomWithReservation : overdueRooms) {
            checkAndUpdateRoomStatus(
                    roomWithReservation,
                    SYSTEM_EMPLOYEE,
                    mainController
            );
        }

        List<RoomWithReservation> allRoomWithReservation =
                roomWithReservationDAO.getRoomWithReservation();

        for (RoomWithReservation roomWithReservation : allRoomWithReservation) {
            checkAndUpdateRoomStatus(
                    roomWithReservation,
                    SYSTEM_EMPLOYEE,
                    mainController
            );
        }
    }

    public static void checkAndUpdateRoomStatus(
            RoomWithReservation roomWithReservation,
            Employee employee,
            MainController mainController
    ) throws RemoteException {
        ReservationForm reservationForm = roomWithReservation.getReservationForm();
        Room room = roomWithReservation.getRoom();

        if (reservationForm == null || room == null) return;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkOutDate = reservationForm.getApproxcheckOutTime();

        if (now.isAfter(checkOutDate)) {
            long hoursOverdue = ChronoUnit.HOURS.between(checkOutDate, now);

            if (hoursOverdue >= 2) {
                handleCheckOut(roomWithReservation, employee);
                room.setRoomStatus(RoomStatus.AVAILABLE);
                roomDAO.updateRoomStatus(room.getRoomID(), RoomStatus.AVAILABLE);


            } else {
                roomDAO.updateRoomStatus(room.getRoomID(), RoomStatus.OVER_DUE);
                room.setRoomStatus(RoomStatus.OVER_DUE);


            }
        }
    }

    public static void handleCheckOut(RoomWithReservation roomWithReservation, Employee employee) {
        try {
            double roomCharge = RoomChargesCalculate.calculateRoomCharges(
                    roomWithReservation.getReservationForm().getApproxcheckInDate(),
                    roomWithReservation.getReservationForm().getApproxcheckOutTime(),
                    roomWithReservation.getRoom()
            );
            double serviceCharge = RoomChargesCalculate.calculateTotalServiceCharge(
                    roomWithReservation.getReservationForm().getReservationID()
            );


            invoiceDAO.roomCheckingOut(
                    roomWithReservation.getReservationForm().getReservationID(),
                    roomCharge,
                    serviceCharge
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void handleCheckoutEarly(RoomWithReservation roomWithReservation, Employee employee) {
        try {
            double roomCharge = RoomChargesCalculate.calculateRoomCharges(
                    roomWithReservation.getReservationForm().getApproxcheckInDate(),
                    LocalDateTime.now(),
                    roomWithReservation.getRoom()
            );
            double serviceCharge = RoomChargesCalculate.calculateTotalServiceCharge(
                    roomWithReservation.getReservationForm().getReservationID()
            );

            System.out.println(">> ROOM CHARGE: " + roomCharge);
            System.out.println(">> SERVICE CHARGE: " + serviceCharge);

            invoiceDAO.roomCheckingOutEarly(
                    roomWithReservation.getReservationForm().getReservationID(),
                    roomCharge,
                    serviceCharge
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




}