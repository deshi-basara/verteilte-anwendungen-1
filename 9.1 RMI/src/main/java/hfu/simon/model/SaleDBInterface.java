package hfu.simon.model;

import hfu.simon.helper.TimedTask;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

/**
 * Created by simon on 11.06.15.
 */
public interface SaleDBInterface extends Remote {
    public abstract void sellTicket(int index) throws RemoteException;
    public abstract void bookTicket(int index, String owner) throws RemoteException;
    public abstract void unbookTicket(int index, String owner) throws RemoteException;
    public abstract void unsaleTicket(int index) throws RemoteException;
    public abstract void resetBookings() throws RemoteException;
    public abstract void toggleSaleEnabled() throws RemoteException;
    public abstract void setTimedTask(TimedTask task) throws RemoteException;
    public abstract boolean isSaleEnabled() throws RemoteException;
    public abstract int getTicketCount() throws RemoteException;
    public abstract TimedTask getTimedTask() throws RemoteException;
    public abstract Vector<Ticket> getAllTickets() throws RemoteException;
}
