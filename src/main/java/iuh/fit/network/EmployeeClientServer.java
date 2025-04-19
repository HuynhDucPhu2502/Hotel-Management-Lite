package iuh.fit.network;

import iuh.fit.dao.daoimpl.*;
import iuh.fit.dao.daointerface.*;
import iuh.fit.models.HotelService;
import iuh.fit.models.RoomCategory;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.rmi.registry.LocateRegistry;

/**
 * @author Le Tran Gia Huy
 * @created 19/04/2025 - 6:29 PM
 * @project Hotel-Management-Lite
 * @package iuh.fit.network
 */
public class EmployeeClientServer {
    public static void main(String[] args) throws Exception {
        AllDAO allDAO = new AllDAOImpl();
        Context ct = new InitialContext();
        LocateRegistry.createRegistry(8702);

        ct.bind("rmi://localhost:8702/allDAO", allDAO);

        System.out.println("EmployeeClientServer ready!!!");
    }
}
