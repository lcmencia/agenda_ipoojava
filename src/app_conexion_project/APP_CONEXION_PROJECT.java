
package app_conexion_project;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 *
 * @author lcmencia
 */
public class APP_CONEXION_PROJECT {

    public static void main(String[] args) {
       CONTACTO_CONTROLLER controller=new CONTACTO_CONTROLLER();
       Scanner keyReader=new Scanner(System.in);
       String Nombre, Telefono,Direccion;
       int ID,Edad;
       int selectedOption;
       String STR_OPTIONS="1 - INSERTAR REGISTRO\n"+
               "2 - ACTUALIZAR REGISTRO\n"+
               "3 - MOSTRAR TODOS LOS REGISTROS\n"+
               "4 - BORRAR REGISTRO\n"+
               "5 - SALIR\n"+
               "SELECCIONE UNA OPCION:";
        do {
            System.out.print(STR_OPTIONS);
            selectedOption=keyReader.nextInt();
            switch(selectedOption){
                case 5:
                  System.out.println("FIN DEL PROGRAMA");
                  break;
                case 1:
                    System.out.println("\n\n\n");
                  keyReader.nextLine();
                    System.err.print("\nNOMBRE: ");
                    Nombre=keyReader.nextLine();
                    System.err.print("\nTelefono: ");
                    Telefono=keyReader.nextLine();
                    System.err.print("\nDIRECCION: ");
                    Direccion=keyReader.nextLine();
                    System.err.print("\nEDAD: ");
                    Edad=keyReader.nextInt();
                    System.err.print("ID: ");
                    ID=keyReader.nextInt();
                    if(controller.INSERT_DATA(new CONTACTO(ID,Nombre,Edad, Telefono,Direccion))>0){
                        System.out.println("DATOS INSERTADOS CON EXITO\n\n\n");
                    }
                    break;
                case 2:
                    System.out.println("\n\n\n");
                    System.err.print("ID: ");
                    ID=keyReader.nextInt();
                    keyReader.nextLine();
                    System.err.print("\nNOMBRE: ");
                    Nombre=keyReader.nextLine();
                    System.err.print("\nTelefono: ");
                    Telefono=keyReader.nextLine();
                    System.err.print("\nDIRECCION: ");
                    Direccion=keyReader.nextLine();
                    System.err.print("\nEDAD: ");
                    Edad=keyReader.nextInt();
                 
                    if(controller.UPDATE_DATA(new CONTACTO(ID,Nombre,Edad,Telefono, Direccion))>0){
                        System.out.println("DATOS ACTUALIZADOS CON EXITO\n\n\n");
                    }
                     break;
                case 3:
                    System.out.println("\n\n\n");
                    for(CONTACTO p:controller.GetAllData())
                        System.out.println("ID:"+p.ID +"\nNOMBRE: "+p.NOMBRE+"\nEDAD:"+p.EDAD+"\nDIRECCION:"+p.DIRECCION+"\n------");
                    System.out.println("\n\n\n");
                    break;
                case 4:
                    System.out.println("\n\n\n");
                     System.err.print("ID: ");
                    ID=keyReader.nextInt();
                       if(controller.RemoveContacto(ID)>0){
                        System.out.println("DATOS REMOVIDOS CON EXITO\n\n\n");
                    }
                        break;
            }
        } while (selectedOption<1 || selectedOption<5);
        
    }
   static  class Conectar {
    public static final String URL = "jdbc:mysql://localhost:3306/jdbc_database";
    public static final String USER = "root";
    public static final String CLAVE = "12345";
     
    public Connection getConexion(){
        Connection con = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = (Connection) DriverManager.getConnection(URL, USER, CLAVE);
        }catch(Exception e){
            System.out.println("Error: ASD" + e.getMessage());
        }
        return con;
    }
}
static class CONTACTO{
    public int ID ;
    public String NOMBRE;
    public int EDAD;
    public String TELEFONO;
    public String DIRECCION;
    public CONTACTO(String NOMBRE,int EDAD, String TELEFONO, String DIRECCION){
        this.NOMBRE=NOMBRE;
        this.EDAD=EDAD;
        this.TELEFONO=TELEFONO;
        this.DIRECCION=DIRECCION;
    }
     public CONTACTO(int ID,String NOMBRE,int EDAD, String TELEFONO, String DIRECCION){
        this.NOMBRE=NOMBRE;
        this.EDAD=EDAD;
        this.TELEFONO=TELEFONO;
        this.DIRECCION=DIRECCION;
        this.ID=ID;
    }
}
static class CONTACTO_CONTROLLER extends Conectar{
    PreparedStatement statament;
    Statement resultstatement;
    Connection cn;
    
    public int INSERT_DATA(CONTACTO Entidad){
        int RESULT=-1;
        String SQL_QUERY="INSERT INTO CONTACTOS VALUES(?,?,?,?,?);";
         cn=getConexion();
         try {
           statament=cn.prepareStatement(SQL_QUERY);
           statament.setInt(1, Entidad.ID);
           statament.setString(2, Entidad.NOMBRE);
           statament.setInt(3,Entidad.EDAD);
           statament.setString(4,Entidad.TELEFONO);
           statament.setString(5,Entidad.DIRECCION);
           RESULT=statament.executeUpdate();
           cn.close();
           statament.close();
           statament=null; cn=null;
        } catch (Exception e) {
         e.printStackTrace();
        }
        return RESULT;
    }
    public int RemoveContacto(int ID){
        int RESULT=-1;
        String SQL_QUERY="DELETE FROM CONTACTOS WHERE ID=?";
         cn=getConexion();
         try {
           statament=cn.prepareStatement(SQL_QUERY);
           statament.setInt(1, ID);
           RESULT=statament.executeUpdate();
           cn.close();
           statament.close();
           statament=null; cn=null;
        } catch (Exception e) {
         e.printStackTrace();
        }
        return RESULT;
    }
    public CONTACTO GetPersona(int ID){
        CONTACTO tempData=null;
         String SQL_QUERY="SELECT *FROM CONTACTOS WHERE ID="+ID;
         cn=getConexion();
         try {
           resultstatement=cn.createStatement();
             ResultSet rs=resultstatement.executeQuery(SQL_QUERY);
             
             if(rs.next()){
                 tempData=new CONTACTO(rs.getInt("ID"),rs.getString("NOMBRE"),rs.getInt("EDAD"),rs.getString("TELEFONO"),rs.getString("DIRECCION"));
             }else{
                 tempData=null;
             }
           cn.close();
           resultstatement.close();
           resultstatement=null; cn=null;
        } catch (Exception e) {
         e.printStackTrace();
        }
         return tempData;
    }
     public List<CONTACTO> GetAllData(){
       ArrayList<CONTACTO> ListadoPersonas=new ArrayList<>();
         String SQL_QUERY="SELECT *FROM CONTACTOS;";
         cn=getConexion();
         try {
           resultstatement=cn.createStatement();
             ResultSet rs=resultstatement.executeQuery(SQL_QUERY);
             
             while(rs.next()){
               ListadoPersonas.add(new CONTACTO(rs.getInt("ID"),rs.getString("NOMBRE"),rs.getInt("EDAD"),rs.getString("TELEFONO") ,rs.getString("DIRECCION")));
             }
           cn.close();
           resultstatement.close();
           resultstatement=null; cn=null;
        } catch (Exception e) {
         e.printStackTrace();
        }
         return ListadoPersonas;
    }
     public int UPDATE_DATA(CONTACTO Entidad){
        int RESULT=-1;
        String SQL_QUERY="UPDATE CONTACTOS SET NOMBRE=?,EDAD=?,TELEFONO=? ,DIRECCION=? WHERE ID=?;";
         cn=getConexion();
         try {
           statament=cn.prepareStatement(SQL_QUERY);
        
           statament.setString(1, Entidad.NOMBRE);
           statament.setInt(2,Entidad.EDAD);
           statament.setString(3,Entidad.TELEFONO);
           statament.setString(4,Entidad.DIRECCION);
           statament.setInt(5,Entidad.ID);
           RESULT=statament.executeUpdate();
           cn.close();
           statament.close();
           statament=null; cn=null;
        } catch (Exception e) {
         e.printStackTrace();
        }
        return RESULT;
    }
}
}
