package com.chengxin.common;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.web.util.HtmlUtils;

import com.chengxin.bfip.Constants;

public class BinaryFormUtil extends BaseController {

    DiskFileUpload upload;
    private String savePath;
    private String tempPath;
    private String separator;
    private Hashtable<String, BinaryFormParameter> binaryFormParameterTable;
    private List<BinaryFormParameter> binaryFormParameterList;

    public void setSavePath(String value) {this.savePath = value;}
    public void setTempPath(String value) {this.tempPath = value;}
    public void setSeparatotPath(String value) {this.separator = value;}

    public BinaryFormUtil(String savePath, String tempPath, String separator) throws Exception{
        this.upload = new DiskFileUpload();
        this.binaryFormParameterTable = new Hashtable<String, BinaryFormParameter>();
        this.binaryFormParameterList = new ArrayList<BinaryFormParameter>();
        this.savePath = savePath;
        this.tempPath = tempPath;
        this.separator = separator;
        
        upload.setHeaderEncoding("UTF-8");
        upload.setSizeMax( 524288000 );
        upload.setSizeThreshold( 4096 );
        upload.setRepositoryPath( this.tempPath );
    }

    public String getString(String parameter, String defaultValue) {
        if(this.binaryFormParameterTable.containsKey(parameter)) {
            BinaryFormParameter formParameter = (BinaryFormParameter)binaryFormParameterTable.get(parameter);
            if(formParameter.getIsTextField()) {
                String value = SecureUtil.XSSFilter((String)formParameter.getValue());
                if(value.equals(CommonType.C_STRING_NULL)) {
                    return defaultValue;
                } else {
                    return HtmlUtils.htmlUnescape(SecureUtil.XSSFilter((String)formParameter.getValue()));
                }
            }
        }

        return defaultValue;
    }

    public File getFile(String parameter) {
        if(this.binaryFormParameterTable.containsKey(parameter)) {
            BinaryFormParameter formParameter = (BinaryFormParameter)binaryFormParameterTable.get(parameter);

            if(!formParameter.getIsTextField()) {
                return (File)formParameter.getValue();
            }
        }

        return null;
    }

    public List<File> getFileList() {
        List<File> fileList = new ArrayList<File>();

        for(BinaryFormParameter binaryFormParameter : binaryFormParameterList) {
            if(!binaryFormParameter.getIsTextField()) {
                fileList.add((File)binaryFormParameter.getValue());
            }
        }

        return fileList;
    }

    public static void createDirectory(String savePath, String separator, String subPath) {
        boolean type = false;

         java.io.File file = new java.io.File(savePath);

         java.io.File[] directoryList = file.listFiles();

         if ( directoryList != null ) {
             for (int i = 0; i < directoryList.length; i++) {
                 if( directoryList[i].getName().equals( subPath ) ) {
                     type = true;
                     break;
                 }
            }
         }

         file = new java.io.File (savePath + separator + subPath );

         if ( type == false ) {
             file.mkdirs();
         }
    }
    
    public void initForm(HttpServletRequest request, HttpServletResponse response, String encoding)
            throws FileUploadException, UnsupportedEncodingException, Exception {
        try {

            List items = upload.parseRequest(request);

            _createDirectory();

            Iterator iterator = items.iterator();
            int fileIdx = 1;

            while(iterator.hasNext()) {
                FileItem fileItem = (FileItem)iterator.next();

                if(fileItem.isFormField()) {
                    binaryFormParameterTable.put(fileItem.getFieldName(),
                            new BinaryFormParameter(fileItem.getFieldName(),
                            true,
                            fileItem.getString(encoding)));
                } else {
                    if ( fileItem.getName() != null && !fileItem.getName().equals(CommonType.C_STRING_NULL) ) {
                        String fileName = fileItem.getName();
                        fileName = fileName.substring(fileName.lastIndexOf(Constants.C_COMMON_FILE_PATH_SEP) + 1);

                        String fileExtension = _getFileExtension(fileName);
                        String saveFileName = _getUniqueFileName(fileIdx, fileExtension);

                        long size = fileItem.getSize();
                        String dateString = DateTimeUtil.getDate().replace("-", "");

                        File _file = new File();

                        _file.setOriginName(fileName);
                        _file.setPhysicalName(saveFileName);
                        _file.setPhysicalPath(dateString);
                        _file.setExtension(fileExtension);
                        _file.setSize((int)size);

                        java.io.File file = new java.io.File(this.savePath + this.separator + dateString, saveFileName);
                        fileItem.write (file);

                        if(binaryFormParameterTable.containsKey(fileItem.getFieldName())) {
                            fileItem.setFieldName(fileItem.getFieldName() + "_" + fileIdx);
                        }

                        BinaryFormParameter formParameter = new BinaryFormParameter(
                                fileItem.getFieldName(),
                                false,
                                _file);

                        binaryFormParameterTable.put(fileItem.getFieldName(),
                                formParameter);

                        binaryFormParameterList.add(formParameter);

                        fileIdx ++;
                    }
                }
            }
        } catch (org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException e) {
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(),"KSC5601"));
            out.println("<script>alert('file capacity exceeded.');history.back();</script>");
            out.close();
        } catch (Exception e) {
            throw e;
        }
    }


    private void _createDirectory( ) throws Exception{

        boolean type = false;
        String dateString = DateTimeUtil.getDate().replace("-", "");

         java.io.File file = new java.io.File( this.savePath );

         java.io.File[] directoryList = file.listFiles();

         if ( directoryList != null ) {
             for (int i = 0; i < directoryList.length; i++) {
                 if( directoryList[i].getName().equals( DateTimeUtil.dateFormat(dateString) ) ) {
                     type = true;
                     break;
                 }
            }
         }

         file = new java.io.File ( this.savePath + this.separator + DateTimeUtil.dateFormat(dateString) );

         if ( type == false ) {
             file.mkdirs();
         }
    }

    private String _getUniqueFileName(int sequence, String extension) throws Exception {
        int i = 0;

        while (true) {
            String fileName = DateTimeUtil.getUniqueTime() + sequence + "_" + i;

            if (!extension.equals(CommonType.C_STRING_NULL)) {
                fileName += "." + extension;
            }

            java.io.File file = new java.io.File(this.savePath + this.separator + fileName);

            if (!file.exists()) {
                return fileName;
            }

            i++;
        }
    }

    public String _getFileExtension(String fileName) {
        String extension = CommonType.C_STRING_NULL;

        if (fileName.lastIndexOf(".") > -1) {
            extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        }

        return extension;
    }
    
    private void _deleteFile(String name) throws Exception {
        String fileName =  this.savePath + this.separator + name;

        deleteFile(fileName);
    }

    public static void deleteFile(String fileName) throws Exception {
        try {
            java.io.File file = new java.io.File(fileName);
            file.delete();
        } catch (Exception e) {

        }
    }
}
