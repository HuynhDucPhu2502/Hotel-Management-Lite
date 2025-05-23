package iuh.fit.dao.daoimpl;

import iuh.fit.dao.daointerface.InvoiceDAO;
import iuh.fit.models.*;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.List;

public class InvoiceDAOImpl extends UnicastRemoteObject implements InvoiceDAO {

    public InvoiceDAOImpl() throws RemoteException {
    }

    @Override
    public List<Invoice> getAllInvoices() {
        EntityManager em = EntityManagerUtil.getEntityManager();

        String jpql = """
            SELECT i FROM Invoice i
        """;

        return em.createQuery(jpql, Invoice.class).getResultList();
    }

    @Override
    public Invoice getInvoiceByInvoiceID(String invoiceID) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        return em.find(Invoice.class, invoiceID);
    }

    @Override
    public Invoice getInvoiceByReservationFormID(String reservationFormID) {
        EntityManager em = EntityManagerUtil.getEntityManager();

        String jpql = """
            SELECT i FROM Invoice i
            WHERE i.reservationForm.reservationID = :reservationFormID
              AND i.reservationForm.historyCheckIn IS NOT NULL
              AND i.reservationForm.historyCheckOut IS NOT NULL
        """;

        TypedQuery<Invoice> query = em.createQuery(jpql, Invoice.class);
        query.setParameter("reservationFormID", reservationFormID);
        return query.getResultStream().findFirst().orElse(null);
    }

    @Override
    public String roomCheckingOut(String reservationFormID, double roomCharge, double serviceCharge) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        LocalDateTime now = LocalDateTime.now();

        ReservationForm form = em.find(ReservationForm.class, reservationFormID);
        if (form == null) return "RESERVATION_FORM_NOT_FOUND";

        Room room = form.getRoom();

        String hcoID = em.createQuery("SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = 'HistoryCheckOut'", String.class).getSingleResult();
        int hcoNext = Integer.parseInt(hcoID.substring(4)) + 1;
        String newHcoID = "HCO-" + String.format("%06d", hcoNext);

        String invoiceID = em.createQuery("SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = 'Invoice'", String.class).getSingleResult();
        int invoiceNext = Integer.parseInt(invoiceID.substring(4)) + 1;
        String newInvoiceID = "INV-" + String.format("%06d", invoiceNext);

        em.getTransaction().begin();

        HistoryCheckOut hco = new HistoryCheckOut();
        hco.setRoomHistoryCheckOutID(hcoID);
        hco.setDateOfCheckingOut(now);
        hco.setReservationForm(form);
        em.persist(hco);

        Invoice invoice = new Invoice();
        invoice.setInvoiceID(invoiceID);
        invoice.setInvoiceDate(now);
        invoice.setRoomCharges(roomCharge);
        invoice.setServiceCharges(serviceCharge);

        double subTotal = roomCharge + serviceCharge;
        double taxCharge = subTotal * 0.1;
        double totalDue = subTotal + taxCharge;

        invoice.setSubTotal(subTotal);
        invoice.setTaxCharge(taxCharge);
        invoice.setTotalDue(totalDue);

        invoice.setReservationForm(form);
        em.persist(invoice);

        room.setRoomStatus(RoomStatus.AVAILABLE);

        em.createQuery("UPDATE GlobalSequence gs SET gs.nextID = :newID WHERE gs.tableName = 'HistoryCheckOut'")
                .setParameter("newID", newHcoID).executeUpdate();

        em.createQuery("UPDATE GlobalSequence gs SET gs.nextID = :newID WHERE gs.tableName = 'Invoice'")
                .setParameter("newID", newInvoiceID).executeUpdate();

        em.getTransaction().commit();

        return "ROOM_CHECKOUT_SUCCESS";
    }

    @Override
    public String roomCheckingOutEarly(String reservationFormID, double roomCharge, double serviceCharge) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        LocalDateTime now = LocalDateTime.now();

        ReservationForm form = em.find(ReservationForm.class, reservationFormID);
        if (form == null) return "RESERVATION_FORM_NOT_FOUND";


        form.setBookingDeposit(0);
        em.merge(form);
        Room room = form.getRoom();

        String hcoID = em.createQuery("SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = 'HistoryCheckOut'", String.class).getSingleResult();
        int hcoNext = Integer.parseInt(hcoID.substring(4)) + 1;
        String newHcoID = "HCO-" + String.format("%06d", hcoNext);

        String invoiceID = em.createQuery("SELECT gs.nextID FROM GlobalSequence gs WHERE gs.tableName = 'Invoice'", String.class).getSingleResult();
        int invoiceNext = Integer.parseInt(invoiceID.substring(4)) + 1;
        String newInvoiceID = "INV-" + String.format("%06d", invoiceNext);

        em.getTransaction().begin();

        HistoryCheckOut hco = new HistoryCheckOut();
        hco.setRoomHistoryCheckOutID(hcoID);
        hco.setDateOfCheckingOut(now);
        hco.setReservationForm(form);
        em.persist(hco);

        Invoice invoice = new Invoice();
        invoice.setInvoiceID(invoiceID);
        invoice.setInvoiceDate(now);
        invoice.setRoomCharges(roomCharge);
        invoice.setServiceCharges(serviceCharge);

        double subTotal = roomCharge + serviceCharge;
        double taxCharge = subTotal * 0.1;
        double totalDue = subTotal + taxCharge;

        invoice.setSubTotal(subTotal);
        invoice.setTaxCharge(taxCharge);
        invoice.setTotalDue(totalDue);

        invoice.setReservationForm(form);
        em.persist(invoice);

        room.setRoomStatus(RoomStatus.AVAILABLE);

        form.setBookingDeposit(form.getBookingDeposit() - roomCharge * 0.1);
        form.setApproxcheckOutTime(now);

        em.createQuery("UPDATE GlobalSequence gs SET gs.nextID = :newID WHERE gs.tableName = 'HistoryCheckOut'")
                .setParameter("newID", newHcoID).executeUpdate();

        em.createQuery("UPDATE GlobalSequence gs SET gs.nextID = :newID WHERE gs.tableName = 'Invoice'")
                .setParameter("newID", newInvoiceID).executeUpdate();

        em.getTransaction().commit();

        return "ROOM_CHECKOUT_SUCCESS";
    }
}
