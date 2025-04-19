package iuh.fit.utils;

import iuh.fit.controller.MainController;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.dao.InvoiceDAO;
import iuh.fit.dao.RoomDAO;
import iuh.fit.dao.RoomWithReservationDAO;

import iuh.fit.models.Employee;
import iuh.fit.models.ReservationForm;
import iuh.fit.models.Room;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.models.wrapper.RoomWithReservation;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RoomManagementService {

    private static final ScheduledExecutorService SCHEDULER =
            Executors.newScheduledThreadPool(1);
    public static final Employee SYSTEM_EMPLOYEE =
            EmployeeDAO.getEmployeeByEmployeeCode("EMP-000000");

    public static void startAutoCheckoutScheduler(MainController mainController) {
        SCHEDULER.scheduleAtFixedRate(
                () -> RoomManagementService.autoCheckoutOverdueRooms(mainController),
                0,
                60,
                TimeUnit.SECONDS
        );
    }

    public static void autoCheckoutOverdueRooms(MainController mainController) {
        List<RoomWithReservation> overdueRooms =
                RoomWithReservationDAO.getRoomOverDueWithLatestReservation();

        for (RoomWithReservation roomWithReservation : overdueRooms) {
            checkAndUpdateRoomStatus(
                    roomWithReservation,
                    SYSTEM_EMPLOYEE
            );
        }

        List<RoomWithReservation> allRoomWithReservation =
                RoomWithReservationDAO.getRoomWithReservation();

        for (RoomWithReservation roomWithReservation : allRoomWithReservation) {
            checkAndUpdateRoomStatus(
                    roomWithReservation,
                    SYSTEM_EMPLOYEE
            );
        }
    }

    public static void checkAndUpdateRoomStatus(
            RoomWithReservation roomWithReservation,
            Employee employee
    ) {
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
                RoomDAO.updateRoomStatus(room.getRoomID(), RoomStatus.AVAILABLE);


            } else {
                RoomDAO.updateRoomStatus(room.getRoomID(), RoomStatus.OVER_DUE);
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


            InvoiceDAO.roomCheckingOut(
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


            InvoiceDAO.roomCheckingOutEarly(
                    roomWithReservation.getReservationForm().getReservationID(),
                    roomCharge,
                    serviceCharge
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




}