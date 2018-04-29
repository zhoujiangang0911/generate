package com.zhoujg77.generate;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhoujg77
 * Time: 2018/4/28 13:27
 * Email: zhoqjg77@163.com
 */
@Controller
public class Index {



    @RequestMapping(path = {"/"})
    public String HelloSpring (ModelMap  model,String msg){
        return "index";
    }


    @RequestMapping("/generator")
    public String generator(ModelMap model, DbHelper dbHelper, HttpServletResponse response,
                            HttpServletRequest request, HttpSession session) throws FileNotFoundException {
        Map<String, Object> params = new HashMap<String, Object>();
        String conlog = "";
        String driveFile = "";
        String driveClass = "";
        String conn = "";
        String rootpath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        String oraclepath = rootpath + "drive"+ File.separator+"ojdbc14.jar";
        String mySqlpath =  rootpath + "drive"+File.separator+"mysql.jar";
        if("mysql".equals(dbHelper.getDbType())){
            driveFile = mySqlpath;
            driveClass = "com.mysql.jdbc.Driver";
            conn = "jdbc:mysql://"+dbHelper.getDbIP()+":"+dbHelper.getDbPort()+"/"+
                    dbHelper.getDbName();
        }else{
            driveFile = oraclepath;
            driveClass = "oracle.jdbc.driver.OracleDriver";
            conn = "jdbc:oracle:thin:@"+dbHelper.getDbIP()+":"+dbHelper.getDbPort()+":"+
                    dbHelper.getDbName();
        }
        params.put("driveFile",driveFile);
        params.put("driverClass", driveClass);
        params.put("connectionURL", conn);
        params.put("userId", dbHelper.getDbUser());
        params.put("password", dbHelper.getDbPass());
        params.put("filePath", rootpath);
        params.put("modelPackage",dbHelper.getModelPackage());
        params.put("xmlPackage", dbHelper.getXmlPackage());
        params.put("mapPackage", dbHelper.getMapPackage());
        params.put("tableName", dbHelper.getTableName());
        InputStream is = new FileInputStream(new File(rootpath+"generatorConfigTmp.xml"));
        String requestXml = parseString(is, params, true);
        String genpath = rootpath+"/mvnproject/src/main/resources/generatorConfig.xml";
        Process p;
        try {
            Files.write(Paths.get(genpath), requestXml.getBytes());
            //执行命令
            String a="#!/bin/sh  \n" +
                    "  \n" +
                  //  "unzip generate-0.0.1-SNAPSHOT.jar  -d generate"+
                    "cd "+rootpath+"/mvnproject/"+"\n" +
                    "mvn mybatis-generator:generate";
            String sh = rootpath+"mvn.sh";
            Files.write(Paths.get(sh), a.getBytes());
            setFilePermission(sh);
            p = Runtime.getRuntime().exec(sh);
            //取得命令结果的输出流
            InputStream fis=p.getInputStream();
            //用一个读输出流类去读
            InputStreamReader isr=new InputStreamReader(fis);
            //用缓冲器读行
            BufferedReader br=new BufferedReader(isr);
            String line=null;
            //直到读完为止
            while((line=br.readLine())!=null)
            {
                System.out.println(line);
                conlog+=line+"\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("conlog",conlog);
        String tableName = getCamelCaseString(dbHelper.getTableName(),true);
        String downFilename = rootpath+tableName+".zip";
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" +tableName+".zip");

        File[] files = {
                new File(rootpath+packageToPath(dbHelper.getModelPackage())+
                        tableName+".java"),
                new File(rootpath+packageToPath(dbHelper.getXmlPackage())+
                        tableName+"Mapper.xml"),
                new File(rootpath+packageToPath(dbHelper.getMapPackage())+
                        tableName+"Mapper.java")
        };

        zipFiles(files,new File(downFilename));

        try {
            OutputStream os = response.getOutputStream();
            os.write(FileUtils.readFileToByteArray(new File(downFilename)));
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return HelloSpring(model,conlog);
    }

    @Bean
    public ErrorPageFilter errorPageFilter() {
        return new ErrorPageFilter();
    }

    @Bean
    public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter filter) {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.setEnabled(false);
        return filterRegistrationBean;
    }

    public void setFilePermission(String path) throws IOException {
        Set<PosixFilePermission> perms = new HashSet<>();
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);

        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);

        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.add(PosixFilePermission.GROUP_EXECUTE);

        Files.setPosixFilePermissions(new File(path).toPath(), perms);
    }


    public static String packageToPath(String packagepath) {
        if("".equals(packagepath)|| packagepath==null){
            return "";
        }
        String path = "";
        String[] paths = packagepath.split("\\.");
        for (String pathPart:paths) {
            path+=pathPart+File.separator;
        }

        return path;
    }

    public static String getSetterMethodName(String property)
    {
        StringBuilder sb = new StringBuilder();

        sb.append(property);
        if ((Character.isLowerCase(sb.charAt(0))) &&
                ((sb.length() == 1) || (!Character.isUpperCase(sb.charAt(1))))) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }
        sb.insert(0, "set");

        return sb.toString();
    }

 

    public static String getCamelCaseString(String inputString, boolean firstCharacterUppercase)
    {
        StringBuilder sb = new StringBuilder();

        boolean nextUpperCase = false;
        for (int i = 0; i < inputString.length(); i++)
        {
            char c = inputString.charAt(i);
            switch (c)
            {
                case ' ':
                case '#':
                case '$':
                case '&':
                case '-':
                case '/':
                case '@':
                case '_':
                    if (sb.length() > 0) {
                        nextUpperCase = true;
                    }
                    break;
                default:
                    if (nextUpperCase)
                    {
                        sb.append(Character.toUpperCase(c));
                        nextUpperCase = false;
                    }
                    else
                    {
                        sb.append(Character.toLowerCase(c));
                    }
                    break;
            }
        }
        if (firstCharacterUppercase) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }
        return sb.toString();
    }

    public static String parseString(InputStream is, Map<String, Object> args, boolean dVal) {
        try {
            if(is != null){
                return parseString(IOUtils.toString(is,"UTF-8"), args, dVal);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String parseString(String str, Map<String, Object> args, boolean dVal) {
        if (!StringUtils.isEmpty(str)) {
            String temp = str.replaceAll("<!--.[^-]*(?=-->)-->", "").replace("\r\n", "");
            for (String key : args.keySet()) {
                if (args.get(key) != null) {
                    temp = temp.replace("${" + key + "}", args.get(key).toString());
                } else {
                    if (dVal) {
                        temp = temp.replace("${" + key + "}", "");
                    } else {
                        temp = temp.replaceAll("<" + key + ">(.*?)</" + key + ">", "");
                    }
                }
            }
            return temp;
        }
        return "";
    }

    /**
     * 功能:压缩多个文件成一个zip文件
     * @param srcfile：源文件列表
     * @param zipfile：压缩后的文件
     */
    public static void zipFiles(File[] srcfile, File zipfile){
        byte[] buf=new byte[1024];
        try {
            //ZipOutputStream类：完成文件或文件夹的压缩
            ZipOutputStream out=new ZipOutputStream(new FileOutputStream(zipfile));
            for(int i=0;i<srcfile.length;i++){
                FileInputStream in=new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                int len;
                while((len=in.read(buf))>0){
                    out.write(buf,0,len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
            System.out.println("压缩完成.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
