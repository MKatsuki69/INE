/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ine;
import java.sql.Connection;
import java.sql.DriverManager;
/**
 *
 * @author randy
 */
public class Conexion {

    public static Connection getConnection() {
        String server, user, database, port, password;
        server = "RANDYC\\SQLEXPRESS";
        user = "Randy";
        password = "123456789";
        database = "CITAS_INE";
        port = "1433";
        String connectionUrl
                    = "jdbc:sqlserver://"+server+":"+port+";"
                    + "database="+database+";"
                   // + "user="+user+";"
                    //+ "password="+password+";"
                    + "encrypt=true;"
                    + "trustServerCertificate=true;"
                    + "loginTimeout=30;";
        try {
            Connection c = DriverManager.getConnection(connectionUrl, user, password);
            System.out.println(connectionUrl+"Conexión exitosa =D");
            return c;
        } catch (Exception e) {
            System.out.println("ERROR, NO SE PUDO CONECTAR"+e.getMessage());
        }
        return null;
} 
}
