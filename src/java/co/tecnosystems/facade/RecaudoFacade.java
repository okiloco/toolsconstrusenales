/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.tecnosystems.facade;

import co.tecnosystems.bean.DBEntity;

import co.tecnosystems.manager.DBManager;
import java.io.FileReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 *
 */
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Stateless
public class RecaudoFacade {

//    @PersistenceContext(unitName = "movilidadPU")
//    private EntityManager em;

    public HashMap<String, Object> ListarRecaudoDiario(String fechaInicio, String fechaFin) throws SQLException, IOException, ParseException {
        List<DBEntity> lecturaJson = lecturaJson();
        List<HashMap<String, Object>> result = new ArrayList();
        HashMap<String, Object> hmResult;
        hmResult = new HashMap<String, Object>();

        for (DBEntity d : lecturaJson) {
            DBManager dbManager = new DBManager(d.getDbServer(), d.getDbName(), d.getDbUser(), d.getDbPassword(), d.getOrganismo());
            Connection conn = dbManager.getConnection();
            
              String query="SELECT T.recaudo_total,O.\"ORTRNOMB\" organismo FROM \n"
                    + "(SELECT  round(SUM(\"PAGOVATO\" + \"PAGOINTE\" - \"PAGODECU\" ) +  (select coalesce(\n"
                    + "(SELECT (\"REPEVALOR\") \n"
                    + "FROM tfinancial.\"RECAUDO_AP_PERKONS\" \n"
                    + "WHERE  \"REPEFERE\" >='"+fechaInicio+"'::date \n"
                    + "And  \"REPEFERE\" <='"+fechaFin+"'::date ),0)))as recaudo_total\n"
                    + "\n"
                    + "FROM tfinancial.\"COMPARENDOS_PAGOS\" c\n"
                    + "WHERE \"PAGOFEPA\" >='"+fechaInicio+"'::date AND  \"PAGOFEPA\" <='"+fechaFin+"'::date AND   c.\"PAGOESTA\" != 2\n"
                    + ") T,\n"
                    + "dtraffic.\"ORGTRANSITO\" O\n"
                    + "WHERE O.\"ORTRIDEN\"="+d.getOrganismo();
            //String query = "select * from tfinancial.\"CALCULA_RECAUDO_FUENTES\"('" + fechaInicio + "','" + fechaFin + "') ";
            System.out.print(query);

            /*String query="SELECT * FROM dblink('hostaddr='"+d.getDbServer()+"' port=5432 user='"+d.getDbUser()+"' password='"+d.getDbPassword()+"' dbname='"+d.getDbName()+"'',\n" +
             "'SELECT round(SUM(\"PAGOVATO\" + \"PAGOINTE\" - \"PAGODECU\" ) +  (select coalesce((SELECT (\"REPEVALOR\") FROM tfinancial.\"RECAUDO_AP_PERKONS\" WHERE  \"REPEFERE\" >='"+fechaInicio+"'::date And  \"REPEFERE\" <='"+fechaFin+"'::date ),0))\n" +
             ")as recaudo_total\n" +
             "FROM tfinancial.\"COMPARENDOS_PAGOS\" c\n" +
             "WHERE \"PAGOFEPA\" >='"+fechaInicio+"'::date AND  \"PAGOFEPA\" <='"+fechaFin+"'::date AND   c.\"PAGOESTA\" != 2'\n" +
             ")\n" +
             "AS MODULOS(\n" +
             "\"RECAUDO\" integer\n" +
             ")";*/
            /*String query="SELECT round(SUM(\"PAGOVATO\" + \"PAGOINTE\" - \"PAGODECU\" ) +  (select coalesce(\n" +
             "(SELECT (\"REPEVALOR\") \n" +
             " FROM tfinancial.\"RECAUDO_AP_PERKONS\" \n" +
             " WHERE  \"REPEFERE\" >="+fechaInicio +"::date\n" +
             " And  \"REPEFERE\" <="+fechaFin+"::date ),0)))as recaudo_total\n" +
             " FROM tfinancial.\"COMPARENDOS_PAGOS\" c\n" +
             " WHERE \"PAGOFEPA\" >="+fechaInicio+"::date AND  \"PAGOFEPA\" <="+fechaFin +"::date AND   c.\"PAGOESTA\" != 2;";*/
            try {
                
                CallableStatement cstmt = conn.prepareCall(query);
                ResultSet rs = cstmt.executeQuery();
                
                HashMap<String, Object> hm;
                ResultSetMetaData rsmd = rs.getMetaData();
                
                while (rs.next()) {
                    hm = new HashMap<String, Object>();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        hm.put(rsmd.getColumnLabel(i), rs.getObject(i));
                    }
                    result.add(hm);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        hmResult.put("lista", result);
        return hmResult;
    }

    /*Cargar Json y Recorrerlo*/
    public List<DBEntity> lecturaJson() throws IOException, ParseException {
        String path = System.getProperty("MOVILIDAD_FOLDER_DBS_CONFIG");
        JSONParser jsparse = new JSONParser();
        List<DBEntity> list = new LinkedList<DBEntity>();

        JSONArray jsonarray = (JSONArray) jsparse.parse(new FileReader(path + "configDBs.json"));
        for (Object obj : jsonarray) {
            JSONObject jsonobject = (JSONObject) obj;

            DBEntity entity = new DBEntity();
            entity.setDbServer(jsonobject.get("host").toString());
            entity.setDbName(jsonobject.get("dbname").toString());
            entity.setDbUser(jsonobject.get("username").toString());
            entity.setDbPassword(jsonobject.get("password").toString());
            entity.setOrganismo(jsonobject.get("organismo").toString());

            list.add(entity);
        }
        return list;
    }

}
