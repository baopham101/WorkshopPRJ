/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employees_mng;
import java.lang.Runtime;
import java.rmi.Naming;

/**
 *
 * @author Admin
 */
public class ManagerServerProgram {
    public static void main(String[] args) {
        String serviceName = "127.0.0.1/EmployeeService";
        String filename = "employees.txt";
        EmployeeServer server = null;
        try {
            server = new EmployeeServer(filename);
            
            Runtime rt = Runtime.getRuntime();
            rt.exec("rmiregistry.exe");
            
            Naming.rebind(serviceName, server);
            System.out.println("Service " + serviceName + " is running.");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
