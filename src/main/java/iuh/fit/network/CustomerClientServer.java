package iuh.fit.network;

import iuh.fit.dao.daointerface.PosDAO;
import iuh.fit.dao.daoimpl.PosDaoImpl;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.rmi.registry.LocateRegistry;

/**
 * @author Le Tran Gia Huy
 * @created 17/04/2025 - 2:44 PM
 * @project Hotel-Management-Lite
 * @package iuh.fit.network
 */

public class CustomerClientServer {
    public static void main(String[] args) throws Exception {
        PosDAO obj = new PosDaoImpl();

        Context ct = new InitialContext();
        LocateRegistry.createRegistry(8701);

        ct.bind("rmi://localhost:8701/pos", obj);

        System.out.println("CustomerClientServer ready!!!");
    }
}
