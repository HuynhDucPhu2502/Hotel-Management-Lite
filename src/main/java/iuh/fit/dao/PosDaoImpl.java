package iuh.fit.dao;

import iuh.fit.dto.InvoiceInfoDTO;
import iuh.fit.models.Customer;
import iuh.fit.models.Room;
import iuh.fit.models.RoomCategory;
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 17/04/2025 - 2:47 PM
 * @project Hotel-Management-Lite
 * @package iuh.fit.dao
 */
public class PosDaoImpl extends UnicastRemoteObject implements PosDAO {

    public PosDaoImpl() throws RemoteException {
    }

    public List<Customer> searchCustomer (
            String info
    ) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {
            String jpql = """
            SELECT c FROM Customer c
            WHERE (c.phoneNumber = :info) OR (c.idCardNumber = :info)
            AND c.isActivate = :status
            """;

            TypedQuery<Customer> query = em.createQuery(jpql, Customer.class);

            query.setParameter("info", info);
            query.setParameter("info", info);
            query.setParameter("status", ObjectStatus.ACTIVE);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<InvoiceInfoDTO> searchCustomerInvoice (
            String info
    ) {
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {

            String jpql = """
                SELECT new iuh.fit.dto.InvoiceInfoDTO(
                        c,
                        i.invoiceID,
                        r.roomID,
                        r.roomCategory.roomCategoryName,
                        i.totalDue,
                        i.invoiceDate
                    )
                    FROM Invoice i
                    JOIN i.reservationForm rf
                    JOIN rf.room r
                    JOIN rf.customer c
                    WHERE c.phoneNumber = :info OR c.idCardNumber = :info AND c.isActivate = :status
            """;

            TypedQuery<InvoiceInfoDTO> query = em.createQuery(jpql, InvoiceInfoDTO.class);

            query.setParameter("info", info);
            query.setParameter("info", info);
            query.setParameter("status", ObjectStatus.ACTIVE);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<RoomCategory> getCategoryForPOS (){
        return RoomCategoryDAO.getRoomCategory();
    }

    public List<Room> getAvailableRoomByRoomCategoryForPOS (RoomCategory category){
        try (EntityManager em = EntityManagerUtil.getEntityManager()) {

            String jpql = """
                SELECT r
                    FROM Room r
                    WHERE r.roomCategory = :category AND r.roomStatus = :status
            """;

            TypedQuery<Room> query = em.createQuery(jpql, Room.class);

            query.setParameter("category", category);
            query.setParameter("status", RoomStatus.AVAILABLE);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
