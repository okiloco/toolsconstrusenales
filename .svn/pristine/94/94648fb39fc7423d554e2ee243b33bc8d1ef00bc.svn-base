/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.tecnosystems.facade;

import co.tecnosystems.bean.DBEntity;
import co.tecnosystems.manager.DBManager;
import com.sun.media.sound.InvalidFormatException;
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
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import co.tecnosystems.util.DateUtil;
import co.tecnosystems.util.GenericJSONResponse;
import co.tecnosystems.util.HttpResponse;
import java.io.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import static java.net.Proxy.Type.HTTP;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.ws.rs.core.Cookie;
import org.apache.poi.ss.usermodel.DataFormat;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
//import jcifs.smb.SmbFileOutputStream;
//import jcifs.smb.SmbFileInputStream;

/**
 *
 *
 */
@Stateless
public class Facade {

    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public Facade() {
    }

    public HashMap<String, Object> listar() throws SQLException, IOException, ParseException {

        DBManager dbManager = new DBManager(226);
        Connection conn = dbManager.getConnection();
        HashMap<String, Object> hmResult;
        hmResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> result = new ArrayList();
        String query = "select *from dtraffic.id_tipo_documento_validator_view";
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

    public HashMap<String, Object> ConsultaComparendos(String organismos, String orden, String tdocumento, String documento,
            String placa) throws SQLException, IOException, ParseException {
        HashMap<String, Object> hmResult;

        hmResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> result = new ArrayList();

        if (!StringUtils.isBlank(organismos)) {
            Integer orga = Integer.parseInt(organismos);
            DBManager dbManager = new DBManager(orga);
            Connection conn = dbManager.getConnection();
            String query = "select * from dtraffic.\"FUNWEBCONSULTARCOMPARENDOS\"('" + orden + "','" + tdocumento + "','" + documento + "','" + placa + "','','')";
            System.out.println("q " + query);
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
            conn.close();
        } else {
            List<DBEntity> lecturaJson = lecturaJson();
            for (DBEntity d : lecturaJson) {
                DBManager dbManager = new DBManager(d.getDbServer(), d.getDbName(), d.getDbUser(), d.getDbPassword(), d.getOrganismo());
                Connection conn = dbManager.getConnection();
                try {

                    String query = "select * from dtraffic.\"FUNWEBCONSULTARCOMPARENDOS\"('" + orden + "','" + tdocumento + "','" + documento + "','" + placa + "','','')";
                    System.out.println("qwerty " + query);
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
                conn.close();
            }
        }

        hmResult.put("lista", result);

        return hmResult;
    }

    public HashMap<String, Object> consultarecaudoDiario(Date fechai, Date fechaf) throws SQLException, IOException, ParseException {
        HashMap<String, Object> hmResult;

        hmResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> result = new ArrayList();

        DBManager dbManager = new DBManager(226);
        Connection conn = dbManager.getConnection();
        String query = "select * from tfinancial.\"FUN_WEB_LISTA_RECAUDO_TIPO_O_USUARIO_UNIFICADO\"('" + fechai + "','" + fechaf + "',  1  ,'N')";
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

    public File generarExcel(List lista) throws IOException, InvalidFormatException, SQLException {

        File file = File.createTempFile("excel_file", ".xlsx");
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("org");
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);

        CellStyle currencyFormat = wb.createCellStyle();
        DataFormat df = wb.createDataFormat();
        currencyFormat.setDataFormat(df.getFormat("#,##0.00"));

        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 7500);
        sheet.setColumnWidth(2, 7500);
        sheet.setColumnWidth(3, 7500);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 4000);
        sheet.setColumnWidth(7, 4000);
        sheet.setColumnWidth(8, 5000);

        Row row;

        Row titleRow = sheet.createRow(0);

        row = sheet.createRow(0);
        Cell value0 = row.createCell(0);
        value0.setCellValue("id_organismo_transito");
        value0.setCellStyle(style);

        Cell value01 = row.createCell(1);
        value01.setCellValue("des_organismo_transito");
        value01.setCellStyle(style);

        Cell value02 = row.createCell(2);
        value02.setCellValue("nro_comparendo");
        value02.setCellStyle(style);

        Cell value03 = row.createCell(3);
        value03.setCellValue("nro_comparendo_sensys");
        value03.setCellStyle(style);

        Cell value04 = row.createCell(4);
        value04.setCellValue("lugar_infraccion");
        value04.setCellStyle(style);

        Cell value05 = row.createCell(5);
        value05.setCellValue("fecha_comparendo");
        value05.setCellStyle(style);

        Cell value06 = row.createCell(6);
        value06.setCellValue("hora_comparendo");
        value06.setCellStyle(style);

        Cell value07 = row.createCell(7);
        value07.setCellValue("fecha_entrega_comparendo");
        value07.setCellStyle(style);

        Cell value08 = row.createCell(8);//revisar este formato
        value08.setCellValue("valor_comparendo");
        value08.setCellStyle(currencyFormat);

        Cell value09 = row.createCell(9);
        value09.setCellValue("estado_comparendo");
        value09.setCellStyle(style);

        Cell value010 = row.createCell(10);
        value010.setCellValue("desc_codigo_transito");
        value010.setCellStyle(style);

        Cell value011 = row.createCell(11);
        value011.setCellValue("detalle_codigo_transito");
        value011.setCellStyle(style);

        Cell value012 = row.createCell(12);
        value012.setCellValue("desc_estado_comparendo");
        value012.setCellStyle(style);

        Cell value013 = row.createCell(13);
        value013.setCellValue("estado_envio_mensajeria");
        value013.setCellStyle(style);

        Cell value014 = row.createCell(14);
        value014.setCellValue("nombre_municipio_infraccion");
        value014.setCellStyle(style);

        Cell value015 = row.createCell(15);
        value015.setCellValue("apellidos_infractor");
        value015.setCellStyle(style);

        Cell value016 = row.createCell(16);
        value016.setCellValue("nombres_infractor");
        value016.setCellStyle(style);

        Cell value017 = row.createCell(17);
        value017.setCellValue("tipo_documento_infractor");
        value017.setCellStyle(style);

        Cell value018 = row.createCell(18);
        value018.setCellValue("nro_documento_infractor");
        value018.setCellStyle(style);

        Cell value019 = row.createCell(19);
        value019.setCellValue("direccion_infractor");
        value019.setCellStyle(style);

        Cell value020 = row.createCell(20);
        value020.setCellValue("municipio_infractor");
        value020.setCellStyle(style);

        Cell value021 = row.createCell(21);
        value021.setCellValue("municipio_infractor");
        value021.setCellStyle(style);

        Cell value022 = row.createCell(22);
        value022.setCellValue("apellido_propietario");
        value022.setCellStyle(style);

        Cell value023 = row.createCell(23);
        value023.setCellValue("nombre_propietario");
        value023.setCellStyle(style);

        Cell value024 = row.createCell(24);
        value024.setCellValue("nro_documento_propietario");
        value024.setCellStyle(style);

        Cell value025 = row.createCell(25);
        value025.setCellValue("direccion_propietario");
        value025.setCellStyle(style);

        Cell value026 = row.createCell(26);
        value026.setCellValue("placa_vehiculo");
        value026.setCellStyle(style);

        Cell value027 = row.createCell(27);
        value027.setCellValue("desc_lugar_matricula");
        value027.setCellStyle(style);

        for (int j = 0; j < lista.size(); j++) {
            row = sheet.createRow(j + 1);

            HashMap<String, Object> record = (HashMap<String, Object>) lista.get(j);
            Cell value = row.createCell(0);
            value.setCellValue(record.get("id_organismo_transito").toString());

            String var = value.toString();

            if (var.equals("20")) {
                Cell value1 = row.createCell(1);
                value1.setCellValue(("BARRANQUILLA").toString());
            }

            if (var.equals("11")) {
                Cell value1 = row.createCell(1);
                value1.setCellValue(("MAGDALENA").toString());
            }

            if (var.equals("14")) {
                Cell value1 = row.createCell(1);
                value1.setCellValue(("ARJONA").toString());
            }

            if (var.equals("47")) {
                Cell value1 = row.createCell(1);
                value1.setCellValue(("CHINCHINA").toString());
            }

            if (var.equals("57")) {
                Cell value1 = row.createCell(1);
                value1.setCellValue(("COROZAL - SUCRE").toString());
            }

            if (var.equals("81")) {
                Cell value1 = row.createCell(1);
                value1.setCellValue(("GALAPA").toString());
            }

            if (var.equals("106")) {
                Cell value1 = row.createCell(1);
                value1.setCellValue(("LA DORADA CALDAS").toString());
            }

            if (var.equals("159")) {
                Cell value1 = row.createCell(1);
                value1.setCellValue(("PUERTO COLOMBIA").toString());
            }

            if (var.equals("199")) {
                Cell value1 = row.createCell(1);
                value1.setCellValue(("SOLEDAD").toString());
            }

            if (var.equals("208")) {
                Cell value1 = row.createCell(1);
                value1.setCellValue(("TURBACO").toString());
            }

            if (var.equals("226")) {
                Cell value1 = row.createCell(1);
                value1.setCellValue(("ATLANTICO ITA").toString());
            }

            if (var.equals("216")) {
                Cell value1 = row.createCell(1);
                value1.setCellValue(("VILLAROSARIO").toString());
            }
            if (var.equals("137")) {
                Cell value1 = row.createCell(1);
                value1.setCellValue(("OCANA").toString());
            }

            Cell value2 = row.createCell(2);
            value2.setCellValue(record.get("nro_comparendo").toString());

            Cell value3 = row.createCell(3);
            value3.setCellValue(record.get("nro_comparendo_sensys").toString());

            Cell value4 = row.createCell(4);
            if (record.get("lugar_infraccion") != null) {
                value4.setCellValue(record.get("lugar_infraccion").toString());
            } else {
                value4.setCellValue("");
            }

            Cell value5 = row.createCell(5);
            value5.setCellValue(record.get("fecha_comparendo").toString());

            Cell value6 = row.createCell(6);
            value6.setCellValue(record.get("hora_comparendo").toString());

            Cell value7 = row.createCell(7);
            if (record.get("fecha_entrega_comparendo") != null) {
                value7.setCellValue(record.get("fecha_entrega_comparendo").toString());
            } else {
                value7.setCellValue("");
            }

            Cell value8 = row.createCell(8);
            if (record.get("valor_comparendo") != null) {
                value8.setCellValue(record.get("valor_comparendo").toString());
                value8.setCellStyle(currencyFormat);
            } else {
                value8.setCellValue("");
            }

            Cell value9 = row.createCell(9);
            value9.setCellValue(record.get("estado_comparendo").toString());

            Cell value10 = row.createCell(10);
            value10.setCellValue(record.get("desc_codigo_transito").toString());

            Cell value11 = row.createCell(11);
            value11.setCellValue(record.get("detalle_codigo_transito").toString());

            Cell value12 = row.createCell(12);
            value12.setCellValue(record.get("desc_estado_comparendo").toString());

            Cell value13 = row.createCell(13);
            if (record.get("estado_envio_mensajeria") != null) {
                value13.setCellValue(record.get("estado_envio_mensajeria").toString());
            } else {
                value13.setCellValue("");
            }

            Cell value14 = row.createCell(14);
            if (record.get("nombre_municipio_infraccion") != null) {
                value14.setCellValue(record.get("nombre_municipio_infraccion").toString());
            } else {
                value14.setCellValue("");
            }

            Cell value15 = row.createCell(15);
            if (record.get("apellidos_infractor") != null) {
                value14.setCellValue(record.get("apellidos_infractor").toString());
            } else {
                value14.setCellValue("");
            }

            Cell value16 = row.createCell(16);
            value16.setCellValue(record.get("nombres_infractor").toString());

            Cell value17 = row.createCell(17);
            value17.setCellValue("tipo_documento_infractor");

            Cell value18 = row.createCell(18);
            value18.setCellValue(record.get("nro_documento_infractor").toString());

            Cell value19 = row.createCell(19);
            value19.setCellValue(record.get("direccion_infractor").toString());

            Cell value20 = row.createCell(20);
            value20.setCellValue(record.get("municipio_infractor").toString());

            Cell value21 = row.createCell(21);
            value21.setCellValue(record.get("municipio_infractor").toString());

            Cell value22 = row.createCell(22);
            value22.setCellValue(record.get("apellido_propietario").toString());

            Cell value23 = row.createCell(23);
            value23.setCellValue(record.get("nombre_propietario").toString());

            Cell value24 = row.createCell(24);
            value24.setCellValue(record.get("nro_documento_propietario").toString());

            Cell value25 = row.createCell(25);
            value25.setCellValue(record.get("direccion_propietario").toString());

            Cell value26 = row.createCell(26);
            value26.setCellValue(record.get("placa_vehiculo").toString());

            Cell value27 = row.createCell(27);
            value27.setCellValue(record.get("desc_lugar_matricula").toString());

        }

        FileOutputStream fileOut = new FileOutputStream(file);
        wb.write(fileOut);
        fileOut.close();
        return file;
    }

    public HashMap<String, Object> buscarInfoComparendo(String comparendo, String cedula, String placa) throws SQLException, IOException, ParseException {

        HashMap<String, Object> hmResult;

        hmResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> result = new ArrayList();

        List<DBEntity> lecturaJson = lecturaJson();
        for (DBEntity d : lecturaJson) {
            DBManager dbManager = new DBManager(d.getDbServer(), d.getDbName(), d.getDbUser(), d.getDbPassword(), d.getOrganismo());
            Connection conn = dbManager.getConnection();
            try {

                String query = " select * from dtraffic.\"FUNWEBLISTADOEFECTY\"('" + comparendo + "',1,'" + cedula + "','" + placa + "')";
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

    public String liquidarYPago(String Comparendo, int usuario) throws SQLException, IOException, ParseException {

        int nroLiq = 0, anoLiq = 0, idcomp = 0;
        String estado = "", descripcion = "", numerocompa = "";
        String estado2 = "", descripcion2 = "";
        java.sql.Date fechaValidez = null;

        List<DBEntity> lecturaJson = lecturaJson();
        for (DBEntity d : lecturaJson) {
            DBManager dbManager = new DBManager(d.getDbServer(), d.getDbName(), d.getDbUser(), d.getDbPassword(), d.getOrganismo());
            System.out.println("db " + d.getDbName());
            Connection conn = dbManager.getConnection();
            CallableStatement cstmt;

            String sql = " select \"COMPIDEN\",\"COMPORDEN\" from dtraffic.\"COMPARENDOS\" WHERE \"COMPORDEN\" = '" + Comparendo + "'";
            cstmt = conn.prepareCall(sql);
            ResultSet rs0 = cstmt.executeQuery();
            if (rs0 != null) {
                while (rs0.next()) {
                    idcomp = rs0.getInt(1);
                    numerocompa = rs0.getString(2);

                }

            }

            String query = "SELECT * FROM tfinancial.\"FUN_WEB_LIQUIDA_COMPARENDO\"(" + idcomp + "," + usuario + ",false,'N')";
            cstmt = conn.prepareCall(query);
            ResultSet rs = cstmt.executeQuery();

            if (rs != null) {
                while (rs.next()) {

                    nroLiq = rs.getInt(1);
                    fechaValidez = rs.getDate(2);
                    anoLiq = rs.getInt(3);
                    estado = rs.getString(4);
                    descripcion = rs.getString(5);
                }

            }

            GregorianCalendar c = new GregorianCalendar();
            c.setTime(new java.util.Date());
            java.util.Date time = c.getTime();
            String timeFormatted = DateUtil.dateToString(time, DateUtil.FORMAT_SQL);

            String query2 = " SELECT * FROM tfinancial.\"FUN_WEB_REGISTRA_PAGO_COMPARENDO\"(" + nroLiq + "," + anoLiq + ", '" + timeFormatted + "', 1, 1, -1, 1, '-')";
            cstmt = conn.prepareCall(query);
            ResultSet rs2 = cstmt.executeQuery();

            if (rs2 != null) {
                while (rs2.next()) {
                    estado2 = rs2.getString(1);
                    descripcion2 = rs2.getString(2);

                }
            }
        }
        if (estado2 != null) {
            if (!estado2.equalsIgnoreCase("OK")) {
                return "Ha ocurrido un error: " + descripcion2;
            }
            return descripcion2;
        } else {
            return descripcion;
        }

    }

    public HashMap<String, Object> guardaLogLLmada(Integer idorganismo, Integer Comparendo, String telefono, String observacion,
            String nombre, Integer tipoLlamada, Integer codificacion, Integer usuario, String direccion, String correo, int deparCiudad, String cedula)
            throws SQLException, IOException, ParseException {
        String comillas = "'";
        HashMap<String, Object> hmResult;
        hmResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> result = new ArrayList();

        DBManager dbManager = new DBManager(idorganismo);
        Connection conn = dbManager.getConnection();
        Integer ciudad;

        if (deparCiudad != 0) {
            ciudad = new Integer(deparCiudad);
        } else {
            ciudad = null;
        }

        if (telefono.equals("")) {
            telefono = null;
        } else {

            telefono = comillas + telefono + comillas;

        }

        if (observacion.equals("")) {
            observacion = null;
        } else {
            observacion = comillas + observacion + comillas;
        }

        if (direccion.equals("")) {
            direccion = null;
        } else {
            direccion = comillas + direccion + comillas;
        }

        if (correo.equals("")) {
            correo = null;
        } else {
            correo = comillas + correo + comillas;
        }

        if (cedula.equals("")) {
            cedula = null;
        } else {
            cedula = comillas + cedula + comillas;
        }

        String query = " SELECT * FROM dtraffic.\"REGISTRA_LOG_CALL_CENTER\"(" + Comparendo + "," + telefono + ", " + observacion + ""
                + " , '" + nombre + "'," + tipoLlamada + " ," + codificacion + ", " + usuario + ", " + direccion + ", " + correo + ", " + ciudad + ", " + cedula + ")";

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
        conn.close();
        return hmResult;

    }

    public HashMap<String, Object> consultarRegistroLlamada(int organismo, int idcomparendo) throws SQLException, IOException, ParseException {
        HashMap<String, Object> hmResult;

        hmResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> result = new ArrayList();

        DBManager dbManager = new DBManager(organismo);
        Connection conn = dbManager.getConnection();
        String query = "select l.\"CALLIDEN\",l.\"CALLIDCO\",l.\"CALLTEL\",l.\"CALLOBSERV\",l.\"CALLFERE\",l.\"CALLNOMB\",l.\"CALLUSUA\",l.\"CALLTIPO\",l.\"CALLCODIF\", "
                + " c.\"COMPORDEN\", coalesce(u.\"USNOMB\"||' '||u.\"USAPEL\")as \"NOMBRE\", u.\"USUSUA\",co.\"CODDESC\" as \"CODIFLLAMADA\","
                + " tl.\"TIPODESC\" as \"TIPOLLAMADA\",l.\"CALLCORREO\" as email, l.\"CALLDIREC\" as direccion, l.\"CALLCEDULA\" as cedula,l.\"CALLDEPARCIUD\" as idciudad,"
                + " coalesce(m.\"MUNOMB\"||' - '||d.\"DENOMB\")as ciudad "
                + " from dtraffic.\"COMPARENDOS_LOG_CALL_CENTER\" l  "
                + " left join dtraffic.\"COMPARENDOS\" c on l.\"CALLIDCO\"= c.\"COMPIDEN\" "
                + " left join dtraffic.\"USUARIOS\" u on  l.\"CALLUSUA\" = u.\"USCODI\" "
                + " left join dtraffic.\"CODIFICACION_PERSUASIVO\" co on l.\"CALLCODIF\" = co.\"CODPERSUID\" "
                + " left join dtraffic.\"TIPO_LLAMADA\" tl on l.\"CALLTIPO\"= tl.\"TIPOID\""
                + " left join dtraffic.\"MUNICIPIOS\" m on m.\"MUIDEN\"= l.\"CALLDEPARCIUD\""
                + " left join dtraffic.\"DEPARTAMENTOS\" d on d.\"DEIDEN\"=m.\"MUDEID\" "
                + "WHERE l.\"CALLIDCO\" = " + idcomparendo + " and l.\"CALLESTADO\"= true  order by l.\"CALLIDEN\" desc ";

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
        conn.close();
        return hmResult;
    }

    public HashMap<String, Object> consultarTipoLlamada(int organismo) throws SQLException, IOException, ParseException {
        HashMap<String, Object> hmResult;

        hmResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> result = new ArrayList();

        DBManager dbManager = new DBManager(organismo);
        Connection conn = dbManager.getConnection();
        String query = " select * from dtraffic.\"TIPO_LLAMADA\"";

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
        conn.close();
        return hmResult;
    }

    public HashMap<String, Object> consultarCodificacionLlamada(int organismo, int tipo) throws SQLException, IOException, ParseException {
        HashMap<String, Object> hmResult;

        hmResult = new HashMap<String, Object>();
        List<HashMap<String, Object>> result = new ArrayList();

        DBManager dbManager = new DBManager(organismo);
        Connection conn = dbManager.getConnection();
        String query = " select * from dtraffic.\"CODIFICACION_PERSUASIVO\"";

        String where = "";

        if (tipo == 1) {
            where += (StringUtils.isBlank(where) ? " WHERE " : " WHERE ") + "  \"CODTIPO\"= 'E'  ";
        } else {
            where += (StringUtils.isBlank(where) ? " WHERE " : " WHERE ") + "  \"CODTIPO\"= 'S'  ";
        }
        where+=" ORDER BY \"CODPERSUID\" DESC";
        String consulta = (query + where);
        CallableStatement cstmt = conn.prepareCall(consulta);

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
        conn.close();
        return hmResult;
    }

    public void buscarGrabacion(String llamdada, String fecha) throws Exception {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("192.168.220.130", "root", "s3rv3rL1nux");
        SmbFile f;

        String grabacion = "20150730-165433-1438293273.34.wav";

        try {

            String path = "smb://192.168.220.130/var/spool/asterisk/monitor" + grabacion + "";
            f = new SmbFile(path, auth);
            f.connect();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("Error:  " + e.getMessage());
        }

    }

    public GenericJSONResponse editarLogLLmada(Integer organismo, Integer idLLamada, Integer usuario, Integer tipoLlamada, Integer codificacion,
            String observacion, String nombre, String telefono, String direccion, String correo, int ciudad, String cedula) throws SQLException, ParseException, IOException {

        Integer id = 0;
        DBManager dbManager = new DBManager(organismo);
        Connection conn = dbManager.getConnection();
        CallableStatement cstmt;

        Integer ciudads;
        String comillas = "'";

        if (ciudad != 0) {
            ciudads = new Integer(ciudad);
        } else {
            ciudads = null;
        }

        if (telefono.equals("")) {
            telefono = null;
        } else {

            telefono = comillas + telefono + comillas;

        }

        if (observacion.equals("")) {
            observacion = null;
        } else {
            observacion = comillas + observacion + comillas;
        }

        if (direccion.equals("")) {
            direccion = null;
        } else {
            direccion = comillas + direccion + comillas;
        }

        if (correo.equals("")) {
            correo = null;
        } else {
            correo = comillas + correo + comillas;
        }

        if (cedula.equals("")) {
            cedula = null;
        } else {
            cedula = comillas + cedula + comillas;
        }

        if (nombre.equals("")) {
            nombre = null;
        } else {
            nombre = comillas + nombre + comillas;
        }

        try {

            String sql = "select l.\"CALLIDEN\" from dtraffic.\"COMPARENDOS_LOG_CALL_CENTER\" l where l.\"CALLIDEN\" = " + idLLamada + " and l.\"CALLUSUA\" = " + usuario + " ";
            cstmt = conn.prepareCall(sql);
            ResultSet rs0 = cstmt.executeQuery();

            if (rs0 != null) {
                while (rs0.next()) {
                    id = rs0.getInt(1);

                }

            }

            if (id == 0) {
                return new GenericJSONResponse(false, GenericJSONResponse.GENERAL_BUSINESS_LOGIC_ERROR, "Error, llamada no puede ser editada por este usuario");
            } else {

                String query = "UPDATE dtraffic.\"COMPARENDOS_LOG_CALL_CENTER\" SET \"CALLTIPO\" = " + tipoLlamada + ",\"CALLCODIF\"=" + codificacion + " ,"
                        + " \"CALLOBSERV\"=" + observacion + ", \"CALLNOMB\" = " + nombre + ", \"CALLTEL\" =" + telefono + ", \"CALLDIREC\"=" + direccion + ", "
                        + " \"CALLCORREO\"= " + correo + ",\"CALLDEPARCIUD\"= " + ciudads + ", \"CALLCEDULA\"=  " + cedula + "  "
                        + " WHERE \"CALLIDEN\" = " + idLLamada + " ";

                cstmt = conn.prepareCall(query);
                cstmt.execute();
                return new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Registro actualizado  Correctamente");
            }
        } catch (Exception e) {
            return new GenericJSONResponse(false, GenericJSONResponse.INTERNAL_ERROR_STATUS, e.getMessage());
        }

    }

    public String conseguirMAC() throws UnknownHostException, IOException {
        InetAddress ip;

        ip = InetAddress.getLocalHost();
        System.out.println("Current IP address : " + ip.getHostAddress());

        NetworkInterface network = NetworkInterface.getByInetAddress(ip);

        byte[] mac = network.getHardwareAddress();

        System.out.print("Current MAC address : ");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }

        System.out.println(sb.toString());
        return sb.toString();

    }

    public GenericJSONResponse eliminarObservacionLLmada(Integer organismo, Integer idLLamada, Integer usuario) throws SQLException, ParseException, IOException {

        Integer id = 0;
        DBManager dbManager = new DBManager(organismo);
        Connection conn = dbManager.getConnection();
        CallableStatement cstmt;

        try {

            String sql = "select l.\"CALLIDEN\" from dtraffic.\"COMPARENDOS_LOG_CALL_CENTER\" l where l.\"CALLIDEN\" = " + idLLamada + " and l.\"CALLUSUA\" = " + usuario + " ";
            cstmt = conn.prepareCall(sql);
            ResultSet rs0 = cstmt.executeQuery();

            if (rs0 != null) {
                while (rs0.next()) {
                    id = rs0.getInt(1);

                }

            }

            if (id == 0) {
                return new GenericJSONResponse(false, GenericJSONResponse.GENERAL_BUSINESS_LOGIC_ERROR, "Error, observación no puede ser eliminada por este usuario");
            } else {

                String query = "UPDATE dtraffic.\"COMPARENDOS_LOG_CALL_CENTER\" SET "
                        + " \"CALLESTADO\"=  false  "
                        + " WHERE \"CALLIDEN\" = " + idLLamada + " ";

                cstmt = conn.prepareCall(query);
                cstmt.execute();
                return new GenericJSONResponse(true, GenericJSONResponse.SUCCESFUL_PROCESSED_STATUS, "Observación eliminada  Correctamente");
            }
        } catch (Exception e) {
            return new GenericJSONResponse(false, GenericJSONResponse.INTERNAL_ERROR_STATUS, e.getMessage());
        }

    }

    public byte[] generarCsv() throws SQLException, IOException, ParseException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(os);
        List<DBEntity> lecturaJson = lecturaJson();
        for (DBEntity d : lecturaJson) {
            DBManager dbManager = new DBManager(d.getDbServer(), d.getDbName(), d.getDbUser(), d.getDbPassword(), d.getOrganismo());
            Connection conn = dbManager.getConnection();
            try {
                String query = "SELECT * FROM dtraffic.\"FUN_WEB_UPDATE_PAGOS_SENSYS\"('2016-08-01','2016-09-30') LIMIT 20";
                //System.out.println("qwerty " + query);
                CallableStatement cstmt = conn.prepareCall(query);
                ResultSet rs = cstmt.executeQuery();
                if (rs != null) {
                    while (rs.next()) {
                        writer.append(rs.getString(1) + "\t");
                        writer.append(rs.getString(2) + "\t");
                        writer.append(rs.getString(3) + "\t");
                        writer.append(rs.getString(4) + "\t");
                        writer.append(rs.getString(5) + " \r\n");
                    }
                }
                writer.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        writer.close();
        return os.toByteArray();
    }
    
    public byte[] generarInformLlamadasCsv(int organismo ,String idusuario,String fechaInicio,String fechaFin) throws IOException, ParseException, SQLException{
        
        Connection conn;
        DBManager dBManager;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(os);
        
        Boolean encabezado=false;
        String where="";
      
        String query = "SELECT o.\"ORTRNOMB\" \"ORGANISMO\",t.* FROM\n"
              + "(select \n"
              + "CASE WHEN call.\"CALLTIPO\" = 1 THEN 'ENTRANTE'\n"
              + " ELSE 'SALIENTE'\n"
              + "END AS \"TIPO LLAMADA\",\n"
              + "co.\"CODDESC\" \"CODIFICACION\",\n"
              + "trim(call.\"CALLOBSERV\") \"OBSERVACION\",\n"
              + "call.\"CALLTEL\" \"TELEFONO\",\n"
              + "call.\"CALLFERE\" \"FECHA HORA\",  trim(c.\"COMPORDEN\")::text \"COMPARENDO\", CONCAT(u.\"USNOMB\",u.\"USAPEL\") \"NOMBRE\" \n"
              + "FROM dtraffic.\"COMPARENDOS_LOG_CALL_CENTER\" call\n"
              + "INNER JOIN dtraffic.\"CODIFICACION_PERSUASIVO\"  co\n"
              + "ON call.\"CALLCODIF\"=co.\"CODPERSUID\"\n"
              + "INNER JOIN dtraffic.\"COMPARENDOS\" c\n"
              + "ON call.\"CALLIDCO\" = c.\"COMPIDEN\"\n"
              + "INNER JOIN dtraffic.\"USUARIOS\" u\n"
              + "ON u.\"USCODI\"=call.\"CALLUSUA\" \n"
              + "WHERE \"CALLUSUA\"=%s\n"
              + "%s\n"
              + ") t,\n"
              + "dtraffic.\"ORGTRANSITO\" o\n"
              + "WHERE \"ORTRIDEN\"=%s";
        
        if(!(fechaInicio.equals("") && fechaFin.equals(""))){
            if(fechaInicio.equals(fechaFin)){
                fechaInicio=fechaInicio+" 00:00:00";
                fechaFin=fechaFin+" 24:00:00";
            }
            where="AND (\"CALLFERE\">='"+fechaInicio+"' AND \"CALLFERE\"<='"+fechaFin+"')\n";
        }
        
        if(organismo!=0){
            dBManager = new DBManager(organismo);
            conn = dBManager.getConnection();
            
            try{
                //Preparo la consulta
                 CallableStatement cstmt = conn.prepareCall(String.format(query, idusuario, where, organismo));
                 ResultSet rs = cstmt.executeQuery();
                 //Generar Csv
                encabezado=this.csvWriter(writer, encabezado, rs);
                  
             }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            
            List<DBEntity> lecturaJson = lecturaJson();
            
            for(DBEntity d:lecturaJson){            
                
                try {
                     
                    dBManager= new DBManager(d.getDbServer(),d.getDbName(),d.getDbUser(),d.getDbPassword(),d.getOrganismo());
                    conn = dBManager.getConnection();
                    String sql=String.format(query, idusuario, where, d.getOrganismo());
                    //Preparo la consulta
                    CallableStatement cstmt = conn.prepareCall(sql);
                    ResultSet rs = cstmt.executeQuery();
                    //Generar Csv
                    encabezado=this.csvWriter(writer, encabezado, rs); 
                    
                } catch (Exception e) {
                  e.printStackTrace();
                }
            }
        }
        writer.close();        
        return os.toByteArray();      
    }
    public Boolean csvWriter(PrintWriter writer,Boolean encabezado,ResultSet rs) throws SQLException{
     
        try{            
            if (rs != null) {
                ResultSetMetaData rsmd = rs.getMetaData();
                String cellData="";
                while (rs.next()) {
                    //Recorrer encabezado de columnas
                    if(!encabezado){
                       for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                          writer.append(rsmd.getColumnLabel(i).toUpperCase()+"\t");//                     
                       }
                       writer.append("\r\n");
                       encabezado=true;
                    }
                    //Generar las filas
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        if(rs.getObject(i)!=null){                
                            cellData=rs.getObject(i).toString().trim();
                            cellData=cellData.replaceAll("[\n\r]", " ");                                
                            cellData="\""+cellData+"\"";
                            writer.append(cellData+"\t");
                        }else{
                            writer.append("\t");
                        }
                    }
                    writer.append("\r\n");
                }
                
             }
             writer.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
        return encabezado;
    }

}
