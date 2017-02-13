/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.tecnosystems.facade;

import co.tecnosystems.bean.DBEntity;
import co.tecnosystems.manager.DBManager;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.ejb.Stateless;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import co.tecnosystems.util.GenericJSONResponse;
import co.tecnosystems.util.Mail;
import co.tecnosystems.util.Utils;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 *
 *
 */
@Stateless
public class FacadePeticiones {

    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public FacadePeticiones() {
    }

    public HashMap<String, Object> listar(int organismo) throws SQLException, IOException, ParseException {

        DBManager dbManager = new DBManager(organismo);
        Connection conn = dbManager.getConnection();
        HashMap<String, Object> hmResult;
        hmResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> result = new ArrayList();

        String query = "select * from dtraffic.\"TIPO_SOLICITUD_PETICION\" ";
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

        hmResult.put("lista", result);

        return hmResult;
    }

    public String aprobar(String peticion, int organismo, Integer idresponsable) throws SQLException, IOException, ParseException {

        DBManager dbManager = new DBManager(organismo);
        Connection conn = dbManager.getConnection();
        CallableStatement cstmt;

        String consultaPeticion = "SELECT \"ID\",\"IDESTPETICION\",\"IDPETICION\" FROM dtraffic.\"REGISTRO_ESTADOS_PETICIONES\"  WHERE \"IDPETICION\" = " + peticion + " AND \"ESTADOCAMBIOESTADO\"= TRUE ";
        cstmt = conn.prepareCall(consultaPeticion);
        ResultSet rs0 = cstmt.executeQuery();

        Integer id = 0;
        Integer idestado = 0;
        Integer idpeticion = 0;
        Integer idUsuario = 0;
        String observacion = "APROBADO";
        String respuesta = "guapeo";

        if (rs0 != null) {
            while (rs0.next()) {
                id = rs0.getInt(1);
                idestado = rs0.getInt(2);
                idpeticion = rs0.getInt(3);

            }

            String consultaUsuario = "SELECT \"ID\" FROM dtraffic.\"USUARIOS_PETICIONES\"  WHERE \"IDUSUA\" = " + idresponsable + " AND \"ESTADO\"= TRUE ";
            cstmt = conn.prepareCall(consultaUsuario);
            ResultSet rs1 = cstmt.executeQuery();
            if (rs1 != null) {
                while (rs1.next()) {
                    idUsuario = rs1.getInt(1);

                }
            }

            String actualizar = "UPDATE dtraffic.\"REGISTRO_ESTADOS_PETICIONES\" SET \"ESTADOCAMBIOESTADO\" = false  WHERE \"ID\" = " + id + " ";
            cstmt = conn.prepareCall(actualizar);
            cstmt.execute();

            int fase = idestado + 1;

            String insert = "INSERT INTO dtraffic.\"REGISTRO_ESTADOS_PETICIONES\" (\"ID\", \"IDPETICION\", \"FECREGISTRO\", \"IDESTPETICION\", \"IDUSUARIOCAMBIOESTADO\",\"FECCAMBIOESTADO\",\"ACTIVO\",\"OBSERVACION\",\"ESTADOCAMBIOESTADO\") "
                    + "values (nextval('dtraffic.\"REGISTRO_ESTADOS_PETICIONES_ID_seq\"'::regclass)," + idpeticion + ",now()," + fase + ", " + idUsuario + ", now(),true,'" + observacion + "'  ,true)";

            cstmt = conn.prepareCall(insert);
            cstmt.execute();

        }

        return respuesta;
    }

    public HashMap<String, Object> listarEstados(int organismo) throws SQLException, IOException, ParseException {

        DBManager dbManager = new DBManager(organismo);
        Connection conn = dbManager.getConnection();
        HashMap<String, Object> hmResult;
        hmResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> result = new ArrayList();

        String query = "select * from dtraffic.\"ESTADOS_PETICION\" ";
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

        hmResult.put("lista", result);

        return hmResult;
    }

    public HashMap<String, Object> listarUsuario(int organismo, String filtro, int start, int limit) throws SQLException, IOException, ParseException {

        DBManager dbManager = new DBManager(organismo);
        Connection conn = dbManager.getConnection();
        HashMap<String, Object> hmResult;
        hmResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> result = new ArrayList();

        String consulta = "SELECT T.*,T1.*,T2.* FROM (SELECT  u.\"USCODI\",u.\"USNOMB\", u.\"USAPEL\", u.\"USUSUA\", CONCAT(u.\"USNOMB\",' ',u.\"USAPEL\") \"NOMBRE\",u.\"USACTI\", up.\"IDUSUARIODEVOLUCION\",up.\"IDUSUARIOAPROBACIONES\",  "
                + "(CASE WHEN up.\"ESTADO\" IS TRUE THEN 1 ELSE 0 END) CHECKED FROM dtraffic.\"USUARIOS\" u "
                + "LEFT JOIN dtraffic.\"USUARIOS_PETICIONES\" up ON u.\"USCODI\"=up.\"IDUSUA\") T "
                + "LEFT JOIN  (SELECT CONCAT(usd.\"USNOMB\",' ',usd.\"USAPEL\")as \"USUARIODEVOLUCION\" ,ups.\"ID\" as \"IDUSUARIOD\" "
                + "from dtraffic.\"USUARIOS_PETICIONES\" ups "
                + "LEFT join dtraffic.\"USUARIOS\" usd on usd.\"USCODI\"= ups.\"IDUSUARIODEVOLUCION\" ) T1 "
                + "On T.\"IDUSUARIODEVOLUCION\"=T1.\"IDUSUARIOD\" "
                + "LEFT JOIN (SELECT CONCAT(usd.\"USNOMB\",' ',usd.\"USAPEL\")as \"USUARIODEVOLUCION\" ,ups.\"ID\" as \"IDUSUARIOD\" "
                + "from dtraffic.\"USUARIOS_PETICIONES\" ups "
                + "LEFT join dtraffic.\"USUARIOS\" usd on usd.\"USCODI\"= ups.\"IDUSUARIOAPROBACIONES\" ) T2 "
                + "On T.\"IDUSUARIOAPROBACIONES\"=T2.\"IDUSUARIOD\" "
                + "WHERE  T.\"USACTI\" = true AND (length(cast (T.\"USCODI\" as text))>=5) ";

        String where = "";

        if (filtro != null) {
            where += (StringUtils.isBlank(where) ? " AND " : " AND ") + "  T.\"NOMBRE\" LIKE '%" + filtro.toUpperCase() + "%' ";
        }
        String startLimit = "order by T.CHECKED desc limit " + limit + " offset " + start + " ";

        String query = (consulta + where + startLimit);
        CallableStatement cstmt = conn.prepareCall(query);
        ResultSet rs = cstmt.executeQuery();

        HashMap<String, Object> hm;
        ResultSetMetaData rsmd = rs.getMetaData();

        while (rs.next()) {
            hm = new HashMap<String, Object>();
            hm.put("ID", rs.getBigDecimal(1));
            hm.put("NOMBRE", rs.getString(2));
            hm.put("APELLIDO", rs.getString(3));
            hm.put("USUARIO", rs.getString(4));
            hm.put("NOMBRECOMPLETO", rs.getString(5));
            hm.put("CHECKED", rs.getInt(7));
            hm.put("USUARIODEVOLUCION", rs.getString(10));
            hm.put("IDUSUARIOAPROBACIONES", rs.getString(12));

            result.add(hm);
        }

        hmResult.put("lista", result);

        return hmResult;
    }

    public HashMap<String, Object> listarUsuarioPeticiones(int organismo) throws SQLException, IOException, ParseException {

        DBManager dbManager = new DBManager(organismo);
        Connection conn = dbManager.getConnection();
        HashMap<String, Object> hmResult;
        hmResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> result = new ArrayList();

        String query = "select up.\"ID\",up.\"IDUSUA\",up.\"ESTADO\",up.\"IDUSUARIOREGISTRO\" , CONCAT(u.\"USNOMB\",' ',u.\"USAPEL\")as \"NOMUSUA\","
                + " r.\"NOMBRE\" from dtraffic.\"USUARIOS_PETICIONES\" up left join dtraffic.\"USUARIOS\" u on u.\"USCODI\"= up.\"IDUSUA\""
                + " left join tauthentication.\"USUARIO_ROL\" ur on u.\"USCODI\"= ur.id_usuario left join tauthentication.\"ROLES\" r on ur.id_rol=r.id"
                + " WHERE \"ESTADO\" = TRUE order by up.\"ID\" ";
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

        hmResult.put("lista", result);

        return hmResult;
    }

    public GenericJSONResponse crearUsuarioPeticiones(int organismo, Integer idusuario, String nombre, Integer usuarioRegistro, Integer idusuarioDevolucion, Integer idusuarioAprobacion, Integer checked, Integer idrol) throws SQLException, IOException, ParseException {

        DBManager dbManager = new DBManager(organismo);
        Connection conn = dbManager.getConnection();
        CallableStatement cstmt;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Integer id = 0;
        try {
            if (checked == 1) {

                String consultaUsuario = "SELECT * FROM dtraffic.\"USUARIOS_PETICIONES\"  WHERE \"IDUSUA\" = " + idusuario + " AND \"ESTADO\"= FALSE ";
                cstmt = conn.prepareCall(consultaUsuario);
                ResultSet rs0 = cstmt.executeQuery();

                if (rs0 != null) {
                    while (rs0.next()) {
                        id = rs0.getInt(1);

                    }
                }

                if (id != 0) {
                    String actualizar = "UPDATE dtraffic.\"USUARIOS_PETICIONES\" SET \"ESTADO\" = true, \"IDUSUARIODEVOLUCION\"= " + idusuarioDevolucion + ","
                            + " \"IDUSUARIOAPROBACIONES\" = " + idusuarioAprobacion + " WHERE \"ID\" = " + id + " ";
                    cstmt = conn.prepareCall(actualizar);
                    cstmt.execute();

                    String rolusuario = "UPDATE tauthentication.\"USUARIO_ROL\" SET id_rol= " + idrol + " WHERE id_usuario= " + idusuario + "";
                    cstmt = conn.prepareCall(rolusuario);
                    cstmt.execute();

                    return new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "datos guardados correctamente");
                } else {

                    String query = "INSERT INTO dtraffic.\"USUARIOS_PETICIONES\"(\"ID\", \"IDUSUA\", \"NOMUSUA\",\"ESTADO\", \"FECREG\", \"IDUSUARIOREGISTRO\",\"IDUSUARIODEVOLUCION\",\"IDUSUARIOAPROBACIONES\") VALUES "
                            + "(nextval('dtraffic.\"USUARIOS_PETICIONES_ID_seq\"'::regclass)," + idusuario + ",'" + nombre + "'," + true + ",'" + df.format(new Date()) + "', " + usuarioRegistro + ", " + idusuarioDevolucion + ", " + idusuarioAprobacion + ")  ";

                    cstmt = conn.prepareCall(query);
                    cstmt.execute();

                    String rolusuario = "insert into tauthentication.\"USUARIO_ROL\" (id,id_rol,id_usuario) values (nextval('tauthentication.\"USUARIOS_ROLES_SEQ\"'::regclass)," + idrol + "," + idusuario + ")";

                    cstmt = conn.prepareCall(rolusuario);
                    cstmt.execute();
                    return new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "datos guardados correctamente");
                }
            } else {
                String query = "UPDATE dtraffic.\"USUARIOS_PETICIONES\" SET \"ESTADO\" = false  WHERE \"IDUSUA\" = " + idusuario + " ";

                cstmt = conn.prepareCall(query);
                cstmt.execute();
                return new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "datos guardados correctamente");
            }

        } catch (Exception e) {
            return new GenericJSONResponse(false, GenericJSONResponse.INTERNAL_ERROR_STATUS, e.getMessage());

        }

    }

    public GenericJSONResponse registarPeticion(Integer organismo, Integer nroradicado, Date fechradicado, String nompeticionario, Integer idsolicitud, Integer idresponsable, String direccion, String cedula,
            String observacion) throws SQLException, ParseException, IOException {

        Integer id = 0;
        String fechaVencimiento = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DBManager dbManager = new DBManager(organismo);
        Connection conn = dbManager.getConnection();
        CallableStatement cstmt;

        try {

            String diasHabile = "SELECT * From tfinancial.\"FUNWEBADICCIONARDIASHABILESAFECHA\"('" + df.format(fechradicado) + "', 15)";
            cstmt = conn.prepareCall(diasHabile);
            ResultSet rs = cstmt.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    fechaVencimiento = rs.getString(1);

                }

            }

            String sql = "SELECT * from dtraffic.\"FUN_WEB_REGISTRA_PETICION\"('" + nroradicado + "','" + df.format(fechradicado) + "','" + nompeticionario + "'," + idsolicitud + "," + idresponsable + ","
                    + "'" + direccion + "','" + cedula + "','" + fechaVencimiento + "')";
            cstmt = conn.prepareCall(sql);
            ResultSet rs0 = cstmt.executeQuery();

            if (rs0 != null) {
                while (rs0.next()) {
                    id = rs0.getInt(1);

                }

            }

            if (id == 0) {
                return new GenericJSONResponse(false, GenericJSONResponse.GENERAL_BUSINESS_LOGIC_ERROR, "Error, verifique que el numero de radicado no este creado");
            } else {

                String query = "INSERT INTO dtraffic.\"REGISTRO_ESTADOS_PETICIONES\" (\"ID\", \"IDPETICION\", \"FECREGISTRO\", \"IDESTPETICION\", \"IDUSUARIOCAMBIOESTADO\",\"FECCAMBIOESTADO\",\"ACTIVO\",\"OBSERVACION\",\"ESTADOCAMBIOESTADO\") "
                        + "values (nextval('dtraffic.\"REGISTRO_ESTADOS_PETICIONES_ID_seq\"'::regclass)," + id + ",now(),1," + idresponsable + ",now(),true,'" + observacion + "',true)";

                cstmt = conn.prepareCall(query);
                cstmt.execute();
                return new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Petición Generada Correctamente");
            }
        } catch (Exception e) {
            return new GenericJSONResponse(false, GenericJSONResponse.INTERNAL_ERROR_STATUS, e.getMessage());
        }

    }

    public HashMap<String, Object> listarPeticiones(int organismo, Integer numeroRadicado, Integer responsable, Date fechradicado, int estadoPeticion, int start, int limit) throws SQLException, IOException, ParseException {

        DBManager dbManager = new DBManager(organismo);
        Connection conn = dbManager.getConnection();
        HashMap<String, Object> hmResult;
        hmResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> result = new ArrayList();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String consulta = "SELECT * FROM "
                + " (select pte.\"OBSERVACION\",pt.\"ID\",pt.\"NRORADICADO\",pt.\"FECREGRADICADO\",pt.\"NOMPETICIONARIO\", ts.\"TISODESCRIP\",pte.\"ID\" as \"IDREGISTRO_ESTADOS_PETICIONES\","
                + " CONCAT(u.\"USNOMB\",' ',u.\"USAPEL\")as \"NOMUSUA\",ts.\"TISOID\" as \"IDTIPOSOLICITUD\",up.\"ID\" AS \"IDUSUARIOP\",ep.\"ESTID\" AS \"IDESTADOPETICIO\", "
                + " pt.\"DIRECCION\",pt.\"CEDULA\",pt.\"FECVENCIMIENTO\",ep.\"DESCRIPCION\",case when now()::date > pt.\"FECVENCIMIENTO\" then 'SI' else 'NO' end AS \"RADICADOVENDICO\","
                + " pte.\"ACTIVO\",pte.\"ESTADOCAMBIOESTADO\",pt.\"IDFUNCRESPONSABLE\""
                + " ,case when (EXTRACT(DAY FROM age(timestamp 'now()',date(pt.\"FECVENCIMIENTO\"))))  > 0 then (EXTRACT(DAY FROM age(timestamp 'now()',date(pt.\"FECVENCIMIENTO\"))))"
                + " else 0 end as \"DIAS_VENCID\""
                + " from dtraffic.\"PETICION\" pt"
                + " left join dtraffic.\"REGISTRO_ESTADOS_PETICIONES\" pte on pte.\"IDPETICION\" = pt.\"ID\" "
                + " left join dtraffic.\"TIPO_SOLICITUD_PETICION\" ts on ts.\"TISOID\" =pt.\"IDTIPSOLICITUD\""
                + " left join dtraffic.\"USUARIOS_PETICIONES\" up on up.\"ID\" = pt.\"IDFUNCRESPONSABLE\" "
                + " left join dtraffic.\"USUARIOS\" u on u.\"USCODI\"= up.\"IDUSUA\" "
                + " left join dtraffic.\"ESTADOS_PETICION\" ep on ep.\"ESTID\"= pte.\"IDESTPETICION\"  "
                + " ) T  "
                + " INNER JOIN  (SELECT DISTINCT ON (pt.\"ID\") pt.\"ID\", "
                + " (CASE WHEN fl.\"ESTADO\" IS TRUE THEN 'SI' ELSE 'NO' END) \"IMAGENE\" "
                + " FROM dtraffic.\"PETICION\"  pt "
                + " LEFT JOIN dtraffic.\"FOLIO_PETICION\" fl"
                + " ON fl.\"IDPETICION\"= pt.\"ID\""
                // + " GROUP BY fl.\"ESTADO\", pt.\"ID\""
                + " ) T2"
                + " ON T2.\"ID\"=T.\"ID\""
                + " INNER JOIN  (SELECT CONCAT(usd.\"USNOMB\",' ',usd.\"USAPEL\")as \"USUARIODEVOLUCION\" ,ups.\"ID\" as \"IDUSUARIOD\""
                + " from dtraffic.\"USUARIOS_PETICIONES\" ups"
                + " LEFT join dtraffic.\"USUARIOS\" usd on usd.\"USCODI\"= ups.\"IDUSUARIODEVOLUCION\" ) T3"
                + " ON T.\"IDUSUARIOP\"=T3.\"IDUSUARIOD\""
                + " INNER JOIN (SELECT CONCAT(usd.\"USNOMB\",' ',usd.\"USAPEL\")as \"USUARIOAPROBACION\" ,ups.\"ID\" as \"IDUSUARIOP\""
                + " from dtraffic.\"USUARIOS_PETICIONES\" ups"
                + " LEFT join dtraffic.\"USUARIOS\" usd on usd.\"USCODI\"= ups.\"IDUSUARIOAPROBACIONES\" ) T4"
                + " ON T.\"IDUSUARIOP\"=T4.\"IDUSUARIOP\""
                + " LEFT JOIN (select DISTINCT ON (pts.\"ID\") pts.\"ID\",count(flp.\"ID\")as \"NROEVIDENCIAS\" from dtraffic.\"PETICION\" pts"
                + " LEFT JOIN dtraffic.\"FOLIO_PETICION\" flp ON flp.\"IDPETICION\"= pts.\"ID\"  group by pts.\"ID\")T5"
                + " ON T5.\"ID\"=T.\"ID\""
                + " where T.\"ESTADOCAMBIOESTADO\" = true and T.\"ACTIVO\" = true ";

        String where = "";

        if (numeroRadicado != 0) {
            where += (StringUtils.isBlank(where) ? " AND " : " AND ") + "  T.\"NRORADICADO\" = " + numeroRadicado + " ";
        }

        if (responsable != 0) {
            where += (StringUtils.isBlank(where) ? " AND " : " AND ") + "  T.\"IDFUNCRESPONSABLE\" = " + responsable + " ";
        }

        if (fechradicado != null) {
            where += (StringUtils.isBlank(where) ? " AND " : " AND ") + "  T.\"FECREGRADICADO\" BETWEEN '" + df.format(fechradicado) + "' and '" + df.format(fechradicado) + "' ";
        }

        if (estadoPeticion != 0) {
            where += (StringUtils.isBlank(where) ? " AND " : " AND ") + "  T.\"IDESTADOPETICIO\" = " + estadoPeticion + " ";
        } else {
            where += (StringUtils.isBlank(where) ? " AND " : " AND ") + "  T.\"IDESTADOPETICIO\" <> 14 ";
        }
        String startLimit = " order by T2.\"IMAGENE\" desc limit " + limit + " offset " + start + "";

        String query = (consulta + where + startLimit);
        

        // System.out.println("qwe " + query);
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

        hmResult.put("lista", result);

        return hmResult;
    }

    public HashMap<String, Object> listarPeticionesUsuario(int organismo, Integer numeroRadicado, Integer responsable, Date fechradicado, int start, int limit) throws SQLException, IOException, ParseException {

        DBManager dbManager = new DBManager(organismo);
        Connection conn = dbManager.getConnection();
        HashMap<String, Object> hmResult;
        hmResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> result = new ArrayList();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String consulta = "SELECT * FROM "
                + " (select pte.\"OBSERVACION\",pt.\"ID\",pt.\"NRORADICADO\",pt.\"FECREGRADICADO\",pt.\"NOMPETICIONARIO\", ts.\"TISODESCRIP\",pte.\"ID\" as \"IDREGISTRO_ESTADOS_PETICIONES\","
                + " CONCAT(u.\"USNOMB\",' ',u.\"USAPEL\")as \"NOMUSUA\",ts.\"TISOID\" as \"IDTIPOSOLICITUD\",up.\"ID\" AS \"IDUSUARIOP\",ep.\"ESTID\" AS \"IDESTADOPETICIO\", "
                + " pt.\"DIRECCION\",pt.\"CEDULA\",pt.\"FECVENCIMIENTO\",ep.\"DESCRIPCION\",case when now()::date > pt.\"FECVENCIMIENTO\" then 'SI' else 'NO' end AS \"RADICADOVENDICO\","
                + " pte.\"ACTIVO\",pte.\"ESTADOCAMBIOESTADO\",pt.\"IDFUNCRESPONSABLE\""
                + " ,case when (EXTRACT(DAY FROM age(timestamp 'now()',date(pt.\"FECVENCIMIENTO\"))))  > 0 then (EXTRACT(DAY FROM age(timestamp 'now()',date(pt.\"FECVENCIMIENTO\"))))"
                + " else 0 end as \"DIAS_VENCID\",up.\"IDUSUA\" "
                + " from dtraffic.\"PETICION\" pt"
                + " left join dtraffic.\"REGISTRO_ESTADOS_PETICIONES\" pte on pte.\"IDPETICION\" = pt.\"ID\" "
                + " left join dtraffic.\"TIPO_SOLICITUD_PETICION\" ts on ts.\"TISOID\" =pt.\"IDTIPSOLICITUD\""
                + " left join dtraffic.\"USUARIOS_PETICIONES\" up on up.\"ID\" = pt.\"IDFUNCRESPONSABLE\" "
                + " left join dtraffic.\"USUARIOS\" u on u.\"USCODI\"= up.\"IDUSUA\" "
                + " left join dtraffic.\"ESTADOS_PETICION\" ep on ep.\"ESTID\"= pte.\"IDESTPETICION\"  "
                + " ) T  "
                + " INNER JOIN  (SELECT DISTINCT ON (pt.\"ID\") pt.\"ID\", "
                + " (CASE WHEN fl.\"ESTADO\" IS TRUE THEN 'SI' ELSE 'NO' END) \"IMAGENE\" "
                + " FROM dtraffic.\"PETICION\"  pt "
                + " LEFT JOIN dtraffic.\"FOLIO_PETICION\" fl"
                + " ON fl.\"IDPETICION\"= pt.\"ID\""
                //+ " GROUP BY fl.\"ESTADO\", pt.\"ID\""
                + " ) T2"
                + " ON T2.\"ID\"=T.\"ID\""
                + " INNER JOIN  (SELECT CONCAT(usd.\"USNOMB\",' ',usd.\"USAPEL\")as \"USUARIODEVOLUCION\" ,ups.\"ID\" as \"IDUSUARIOD\""
                + " from dtraffic.\"USUARIOS_PETICIONES\" ups"
                + " LEFT join dtraffic.\"USUARIOS\" usd on usd.\"USCODI\"= ups.\"IDUSUARIODEVOLUCION\" ) T3"
                + " ON T.\"IDUSUARIOP\"=T3.\"IDUSUARIOD\""
                + " INNER JOIN (SELECT CONCAT(usd.\"USNOMB\",' ',usd.\"USAPEL\")as \"USUARIOAPROBACION\" ,ups.\"ID\" as \"IDUSUARIOP\""
                + " from dtraffic.\"USUARIOS_PETICIONES\" ups"
                + " LEFT join dtraffic.\"USUARIOS\" usd on usd.\"USCODI\"= ups.\"IDUSUARIOAPROBACIONES\" ) T4"
                + " ON T.\"IDUSUARIOP\"=T4.\"IDUSUARIOP\""
                + " LEFT JOIN (select DISTINCT ON (pts.\"ID\") pts.\"ID\",count(flp.\"ID\")as \"NROEVIDENCIAS\" from dtraffic.\"PETICION\" pts"
                + " LEFT JOIN dtraffic.\"FOLIO_PETICION\" flp ON flp.\"IDPETICION\"= pts.\"ID\"  group by pts.\"ID\")T5"
                + " ON T5.\"ID\"=T.\"ID\""
                + " where T.\"ESTADOCAMBIOESTADO\" = true and T.\"ACTIVO\" = true and T.\"IDUSUA\"= " + responsable + " ";

        String where = "";

        if (numeroRadicado != 0) {
            where += (StringUtils.isBlank(where) ? " AND " : " AND ") + "  pt.\"NRORADICADO\" = " + numeroRadicado + " ";
        }

        if (fechradicado != null) {
            where += (StringUtils.isBlank(where) ? " AND " : " AND ") + "  pt.\"FECREGRADICADO\" BETWEEN '" + df.format(fechradicado) + "' and '" + df.format(fechradicado) + "' ";
        }

        String startLimit = " order by T.\"ID\" asc limit " + limit + " offset " + start + "";

        String query = (consulta + where + startLimit);
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

        hmResult.put("lista", result);

        return hmResult;
    }

    public GenericJSONResponse peticionCambiarEstado(Integer organismo, String IdPeticion, String IdRegistroEst, Integer estadoPeticion, Integer usuario, Integer idtiposolicitud, Integer usuariosession) throws SQLException, ParseException, IOException {
        DBManager dbManager = new DBManager(organismo);
        Connection conn = dbManager.getConnection();
        CallableStatement cstmt;

        Integer idUsuariosesion = 0;
        Integer idEstadoPeticion = 0;
        String observaciones = null;
        String correo = null;
        String nombre = null;
        Integer numeroradicado = 0;

        try {
            String consultaUsuario = "SELECT \"ID\" FROM dtraffic.\"USUARIOS_PETICIONES\"  WHERE \"IDUSUA\" = " + usuariosession + " ";
            cstmt = conn.prepareCall(consultaUsuario);
            ResultSet rs1 = cstmt.executeQuery();
            if (rs1 != null) {
                while (rs1.next()) {
                    idUsuariosesion = rs1.getInt(1);

                }
            }

            String consultaCorreo = "SELECT u.\"USEMAI\" FROM dtraffic.\"USUARIOS_PETICIONES\" up left join dtraffic.\"USUARIOS\" u on u.\"USCODI\"=\"IDUSUA\" WHERE up.\"ID\"= " + usuario + " ";
            cstmt = conn.prepareCall(consultaCorreo);
            ResultSet rsc = cstmt.executeQuery();
            if (rsc != null) {
                while (rsc.next()) {
                    correo = rsc.getString(1);

                }
            }

            String consultaNombe = "select CONCAT(u.\"USNOMB\",' ',u.\"USAPEL\") from  dtraffic.\"USUARIOS\" u where u.\"USCODI\"= " + usuariosession + " ";
            cstmt = conn.prepareCall(consultaNombe);
            ResultSet rsn = cstmt.executeQuery();
            if (rsn != null) {
                while (rsn.next()) {
                    nombre = rsn.getString(1);

                }
            }

            String consultaRegistroEstadoPeticion = "select rs.\"IDESTPETICION\", rs.\"OBSERVACION\",p.\"NRORADICADO\"  from dtraffic.\"REGISTRO_ESTADOS_PETICIONES\"  rs left join dtraffic.\"PETICION\" p on rs.\"IDPETICION\"= p.\"ID\" where rs.\"ID\"= " + IdRegistroEst + "";

            cstmt = conn.prepareCall(consultaRegistroEstadoPeticion);
            ResultSet rs2 = cstmt.executeQuery();
            if (rs2 != null) {
                while (rs2.next()) {
                    idEstadoPeticion = rs2.getInt(1);
                    observaciones = rs2.getString(2);
                    numeroradicado = rs2.getInt(3);

                }
            }

            if (!StringUtils.isBlank(IdRegistroEst)) {
                String sql = "UPDATE dtraffic.\"REGISTRO_ESTADOS_PETICIONES\" SET \"ESTADOCAMBIOESTADO\" = false  WHERE \"ID\" = " + IdRegistroEst + "";
                cstmt = conn.prepareCall(sql);
                cstmt.execute();

                if (idtiposolicitud != 0) {
                    String sql2 = "UPDATE dtraffic.\"PETICION\" SET \"IDTIPSOLICITUD\" = " + idtiposolicitud + "  WHERE \"ID\" = " + IdPeticion + "";
                    cstmt = conn.prepareCall(sql2);
                    cstmt.execute();
                } else {
                    String sql2 = "UPDATE dtraffic.\"PETICION\" SET  \"IDFUNCRESPONSABLE\" = " + usuario + "  WHERE \"ID\" = " + IdPeticion + "";
                    cstmt = conn.prepareCall(sql2);
                    cstmt.execute();

                    Mail.sendMail(correo, "Peticion Asignada - (" + nombre + ")", "Las siguientes peticiones, con número de radicados: " + numeroradicado + "");

                }

            } else {
                return new GenericJSONResponse(false, GenericJSONResponse.GENERAL_BUSINESS_LOGIC_ERROR, "Esta Petición no tiene un estado");
            }

            if (estadoPeticion != 0) {
                String query = "INSERT INTO dtraffic.\"REGISTRO_ESTADOS_PETICIONES\" (\"ID\", \"IDPETICION\", \"FECREGISTRO\", \"IDESTPETICION\", \"IDUSUARIOCAMBIOESTADO\",\"FECCAMBIOESTADO\",\"ACTIVO\",\"OBSERVACION\",\"ESTADOCAMBIOESTADO\") "
                        + "values (nextval('dtraffic.\"REGISTRO_ESTADOS_PETICIONES_ID_seq\"'::regclass)," + IdPeticion + ",now()," + estadoPeticion + "," + idUsuariosesion + ",now(),true,'" + observaciones + "',true)";

                cstmt = conn.prepareCall(query);
                cstmt.execute();
                return new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Petición actualizada Correctamente");
            } else {
                String query = "INSERT INTO dtraffic.\"REGISTRO_ESTADOS_PETICIONES\" (\"ID\", \"IDPETICION\", \"FECREGISTRO\", \"IDESTPETICION\", \"IDUSUARIOCAMBIOESTADO\",\"FECCAMBIOESTADO\",\"ACTIVO\",\"OBSERVACION\",\"ESTADOCAMBIOESTADO\") "
                        + "values (nextval('dtraffic.\"REGISTRO_ESTADOS_PETICIONES_ID_seq\"'::regclass)," + IdPeticion + ",now()," + idEstadoPeticion + "," + idUsuariosesion + ",now(),true,'" + observaciones + "',true)";

                cstmt = conn.prepareCall(query);
                cstmt.execute();
                return new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Petición actualizada Correctamente");
            }

        } catch (Exception e) {
            return new GenericJSONResponse(false, GenericJSONResponse.INTERNAL_ERROR_STATUS, e.getMessage());
        }

    }

    public GenericJSONResponse add(String basePath, String filename, Integer usuario_id, Integer idPeticiones, Integer organismo, byte[] data) {
        GenericJSONResponse response = null;
        try {

            if (usuario_id != null) {
                Date now = new Date();
                String ext = FilenameUtils.getExtension(filename);
                String rut = "PETICIONES";
                String folder = basePath + File.separator
                        + rut;

                String nombreArchivo = UUID.randomUUID().toString() + "." + ext;
                String filePath = folder + File.separator + nombreArchivo;

                File folderFile = new File(folder);
                if (!folderFile.exists()) {
                    folderFile.mkdirs();
                }
                File file = new File(filePath);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(data);
                fos.close();

                DBManager dbManager = new DBManager(organismo);
                Connection conn = dbManager.getConnection();
                CallableStatement cstmt;

                String query = "INSERT INTO dtraffic.\"FOLIO_PETICION\"(\"IDPETICION\", \"RUTAARCH\", \"FECREGISTRO\", \"ID\", \"NOMBREARCH\", \"PETUSUA\", \"ESTADO\")VALUES  "
                        + "(" + idPeticiones + ",'" + filePath + "' ,now(),nextval('dtraffic.\"FOLIO_PETICION_ID_seq\"'::regclass) ,'" + nombreArchivo + "'," + usuario_id + ",true)";

                cstmt = conn.prepareCall(query);
                cstmt.execute();

                response = new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Evidencia adjuntada con exito");
            }

        } catch (IOException ex) {
            response = new GenericJSONResponse(false, GenericJSONResponse.INTERNAL_ERROR_STATUS, "Error al adjuntar la evidencia en el sistema");
            ex.printStackTrace();
        } finally {
            return response;
        }
    }

    public GenericJSONResponse subirArchivo(String basePath, String filename, Integer usuario_id, Integer idPeticiones, Integer organismo, byte[] bytes) {
        GenericJSONResponse response = null;
        try {
            if (usuario_id != null) {
                Date now = new Date();
                String ext = FilenameUtils.getExtension(filename);
                String rut = "PETICIONES";
                String folder = basePath + File.separator
                        + rut;

                String nombreArchivo = UUID.randomUUID().toString() + "." + ext;
                String filePath = folder + File.separator + nombreArchivo;

                File folderFile = new File(folder);
                if (!folderFile.exists()) {
                    folderFile.mkdirs();
                }

                boolean ftp = subirarchivo(bytes, "prueba.jpg");
                if (ftp == true) {
                    DBManager dbManager = new DBManager(organismo);
                    Connection conn = dbManager.getConnection();
                    CallableStatement cstmt;

                    String query = "INSERT INTO dtraffic.\"FOLIO_PETICION\"(\"IDPETICION\", \"RUTAARCH\", \"FECREGISTRO\", \"ID\", \"NOMBREARCH\", \"PETUSUA\", \"ESTADO\")VALUES  "
                            + "(" + idPeticiones + ",'" + filePath + "' ,now(),nextval('dtraffic.\"FOLIO_PETICION_ID_seq\"'::regclass) ,'" + nombreArchivo + "'," + usuario_id + ",true)";

                    cstmt = conn.prepareCall(query);
                    cstmt.execute();

                    response = new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Evidencia adjuntada con exito");
                } else {
                    response = new GenericJSONResponse(false, GenericJSONResponse.INTERNAL_ERROR_STATUS, "Error al adjuntar la evidencia en el sistema");
                }

            }

        } catch (IOException ex) {
            response = new GenericJSONResponse(false, GenericJSONResponse.INTERNAL_ERROR_STATUS, "Error al adjuntar la evidencia en el sistema");
            ex.printStackTrace();
        } finally {
            return response;
        }
    }

    public HashMap<String, Object> listarEvidencias(int organismo, int IDPETICION) throws SQLException, IOException, ParseException {

        DBManager dbManager = new DBManager(organismo);
        Connection conn = dbManager.getConnection();
        HashMap<String, Object> hmResult;
        hmResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> result = new ArrayList();

        String query = "SELECT \"ID\",\"NOMBREARCH\" src, (CASE WHEN (substr(\"NOMBREARCH\",(length(\"NOMBREARCH\") - (2)) ,length(\"NOMBREARCH\"))) = 'pdf' THEN 'pdf' ELSE 'image' END) AS type FROM dtraffic.\"FOLIO_PETICION\" where \"IDPETICION\" = " + IDPETICION + " AND \"ESTADO\"=TRUE order by \"ID\"  ";
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

        hmResult.put("lista", result);

        return hmResult;
    }

    public GenericJSONResponse aprobarPeticiones(String idpeticiones, int idusuario, int organismo) throws SQLException, ParseException, IOException {
        DBManager dbM = new DBManager(organismo);
        Connection conn = dbM.getConnection();
        CallableStatement cstmt;
        int usuario = 0;
        String observacion = "";
        String asunto = "";
        String correo = "";
        String nombrecompleto = "";

        try {
            String sql = "SELECT * FROM dtraffic.\"FUNPETICIONESFASESIGUIENTE\"('"
                    + idpeticiones + "'," + idusuario + ");";

            cstmt = conn.prepareCall(sql);
            ResultSet rs = cstmt.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    usuario = rs.getInt(1);
                    correo = rs.getString(2);
                    observacion = rs.getString(3);
                    asunto = rs.getString(4);

                }
            }

            Mail.sendMail(correo, asunto, observacion);

            return new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Petición Aprobada Correctamente");
        } catch (Exception e) {
            return new GenericJSONResponse(false, GenericJSONResponse.INTERNAL_ERROR_STATUS, e.getMessage());
        }
    }

    public GenericJSONResponse devolverPeticiones(String idpeticiones, int idusuario, int organismo) throws SQLException, ParseException, IOException {
        DBManager dbM = new DBManager(organismo);
        Connection conn = dbM.getConnection();
        CallableStatement cstmt;
        int usuario = 0;
        String observacion = "";
        String asunto = "";
        String correo = "";
        String nombrecompleto = "";
        try {
            String sql = "SELECT * FROM dtraffic.\"FUN_PETICIONES_DEVOLVER\"('"
                    + idpeticiones + "'," + idusuario + ");";

            cstmt = conn.prepareCall(sql);

            ResultSet rs = cstmt.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    usuario = rs.getInt(1);
                    correo = rs.getString(2);
                    observacion = rs.getString(3);
                    asunto = rs.getString(4);

                }
            }

            Mail.sendMail(correo, asunto, observacion);
            return new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Petición Devuelta Correctamente");
        } catch (Exception e) {
            return new GenericJSONResponse(false, GenericJSONResponse.INTERNAL_ERROR_STATUS, e.getMessage());
        }
    }

    public GenericJSONResponse eliminarEvidenciaPeticiones(int organismo, Integer idevidencia) throws SQLException, IOException, ParseException {

        DBManager dbManager = new DBManager(organismo);
        Connection conn = dbManager.getConnection();
        CallableStatement cstmt;

        try {

            String query = "UPDATE dtraffic.\"FOLIO_PETICION\" SET \"ESTADO\" = false  WHERE \"ID\" = " + idevidencia + " ";
            cstmt = conn.prepareCall(query);
            cstmt.execute();
            return new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "evidencia eliminada correctamente ");

        } catch (Exception e) {
            return new GenericJSONResponse(false, GenericJSONResponse.INTERNAL_ERROR_STATUS, e.getMessage());

        }

    }

    public boolean subirarchivo(byte[] bytes, String nombreArchivo) {
        try {

            FTPClient ftpClient = new FTPClient();
//            ftpClient.setDefaultPort(Integer.parseInt(System.getProperty("FTP_PORT")));
//            ftpClient.connect(System.getProperty("FTP_HOST"));
//            ftpClient.login(System.getProperty("FTP_USER"), System.getProperty("FTP_PSD"));
//            ftpClient.changeWorkingDirectory(System.getProperty("FTP_CARPETA") + "/");

            ftpClient.connect(InetAddress.getByName("10.1.2.201"));
            ftpClient.login("sistra", "tosf52la");

            //Verificar conexión con el servidor.
            int reply = ftpClient.getReplyCode();

            System.out.println("Respuesta recibida de conexión FTP:" + reply);

            if (FTPReply.isPositiveCompletion(reply)) {
                System.out.println("Conectado Satisfactoriamente");
            } else {
                System.out.println("Imposible conectarse al servidor");
            }

            ftpClient.changeWorkingDirectory("/home/ftp/sistra/PETICIONES/");//Cambiar directorio de trabajo
            System.out.println("Se cambió satisfactoriamente el directorio");

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            System.out.println("iniciando transferencia " + bytes);
            InputStream document = new ByteArrayInputStream(bytes);
            BufferedInputStream buffIn = new BufferedInputStream(document);
            ftpClient.enterLocalPassiveMode();
            ftpClient.storeFile(nombreArchivo, buffIn);
            buffIn.close();
            document.close();
            return true;

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public HashMap<String, Object> listarRoles(int organismo) throws SQLException, IOException, ParseException {

        DBManager dbManager = new DBManager(organismo);
        Connection conn = dbManager.getConnection();
        HashMap<String, Object> hmResult;
        hmResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> result = new ArrayList();

        String query = "select id as idrol, \"NOMBRE\" as nombrerol  from tauthentication.\"ROLES\" where id>=29 and id<=31 ";
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

        hmResult.put("lista", result);

        return hmResult;
    }

    public GenericJSONResponse cambiarObservacion(int organismo, Integer idestadopeticion, String observacion) throws SQLException, IOException, ParseException {

        DBManager dbManager = new DBManager(organismo);
        Connection conn = dbManager.getConnection();
        CallableStatement cstmt;

        try {

            String query = "UPDATE dtraffic.\"REGISTRO_ESTADOS_PETICIONES\" SET \"OBSERVACION\" = '" + observacion + "'  WHERE \"ID\" = " + idestadopeticion + " ";

            cstmt = conn.prepareCall(query);
            cstmt.execute();
            return new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "observación actualizada correctamente ");

        } catch (Exception e) {
            return new GenericJSONResponse(false, GenericJSONResponse.INTERNAL_ERROR_STATUS, e.getMessage());

        }

    }

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
