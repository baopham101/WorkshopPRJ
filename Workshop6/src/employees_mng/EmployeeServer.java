/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employees_mng;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.Vector;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;
/**
 *
 * @author Admin
 */
public class EmployeeServer extends UnicastRemoteObject implements EmployeeMngInterface{
    String filename;
    public EmployeeServer(String filename) throws RemoteException {
        super();
        this.filename = filename;
    }
    public Vector getInitialData() throws RemoteException {
        Vector data = new Vector(0);
        try {
            FileReader f = new FileReader(filename);
            BufferedReader br = new BufferedReader(f);
            String line;
            StringTokenizer stk;
            String code, name;
            int salary;
            while ((line = br.readLine())!= null) {
                stk = new StringTokenizer(line, ",");
                Vector v = new Vector();
                v.add(stk.nextToken());//code
                v.add(stk.nextToken());//name
                v.add(Integer.parseInt(stk.nextToken()));//salary
                data.add(v);
            }
            br.close();
            f.close();
        } catch (Exception e) {
        }
        return data;
    }
    
    public boolean saveList(Vector data) throws RemoteException {
        try {
            FileWriter f = new FileWriter(filename);
            PrintWriter pw = new PrintWriter(f);
            for (int i = 0; i < data.size(); i++) {
                Vector v = ((Vector)(data.get(i)));
                String S = "";
                S += v.get(0) + ","  + v.get(1) + "," + v.get(2);
                
                pw.println(S);
            }
            pw.close();
            f.close();
            return true;
        } catch (Exception e) {
        }
        return false;
    }
}
