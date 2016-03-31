package com.txy.capture.task;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.txy.common.orm.query.impl.DynamicBean;
import com.txy.tools.ExcelUtil;
import com.txy.web.common.util.ConfigUtil;
import com.txy.web.common.util.FTPUtil;
import com.txy.web.main.services.SystemBackInfoServie;

public class DataExportTask extends TimerTask
{
    
    private SystemBackInfoServie systemBackInfoServie;
    
    private DynamicBean dynamicBean;
    
    public DataExportTask(DynamicBean dynamicBean, SystemBackInfoServie systemBackInfoServie)
    {
        this.dynamicBean = dynamicBean;
        this.systemBackInfoServie = systemBackInfoServie;
    }
    
    @Override
    public void run()
    {
        String model = dynamicBean.getValue("model_type");
        String filePath = dynamicBean.getValue("filepath");
        String formatFile = dynamicBean.getValue("format_file");
        String name = dynamicBean.getValue("name");
        
        String username = dynamicBean.getValue("username");
        String pwd = dynamicBean.getValue("pwd");
        String ip = dynamicBean.getValue("ip");
        String storeType = dynamicBean.getValue("store_type");
        String rootPath = dynamicBean.getValue("filepath");
        
        DynamicBean bean = systemBackInfoServie.queryWmpCustomByModelType(model);
        List<Map<String, Object>> data = systemBackInfoServie.querySaveDatas(model, bean);
        if (data != null && data.size() > 0)
        {
            Map<String, String> columName = ConfigUtil.getColumName(model, bean);
            
            String fileName = name + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                    + "." + formatFile;
            
            if (filePath != null && !filePath.endsWith("/"))
            {
                filePath = filePath + "/";
            }
            filePath += fileName;
            // 本地盘
            if ("1".equals(storeType))
            {
                File path = new File(rootPath);
                if (!path.exists())
                {
                    path.mkdirs();
                }
                
                File file = new File(filePath);
                OutputStream out = null;
                OutputStreamWriter osw = null;
                BufferedWriter bw = null;
                try
                {
                    if (file.createNewFile())
                    {
                        out = new FileOutputStream(file);
                        if ("txt".equals(formatFile))
                        {
                            osw = new OutputStreamWriter(out, "UTF-8");
                            bw = new BufferedWriter(osw);
                            saveTxt(data, columName, bw);
                        }
                        else
                        {
                            ExcelUtil.toTimerExcel(out, data, columName);
                        }
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    this.close(bw, osw, out);
                }
            }
            // 共享盘
            else if ("2".equals(storeType))
            {
                SmbFile remoteFile = null;
                OutputStream out = null;
                OutputStreamWriter osw = null;
                BufferedWriter bw = null;
                try
                {
                    String remoteFilePath = "smb://" + username + ":" + pwd + "@" + ip;
                    if (!rootPath.startsWith("/"))
                    {
                        remoteFilePath += "/";
                    }
                    remoteFile = new SmbFile(remoteFilePath + rootPath);
                    if (!remoteFile.exists())
                    {
                        remoteFile.mkdirs();
                    }
                    remoteFile.connect(); // 尝试连接
                    
                    remoteFile = new SmbFile(remoteFilePath + filePath);
                    if (!remoteFile.exists())
                    {
                        remoteFile.createNewFile();
                    }
                    remoteFile.connect(); // 尝试连接
                    
                    out = new BufferedOutputStream(new SmbFileOutputStream(remoteFile));
                    
                    if ("txt".equals(formatFile))
                    {
                        osw = new OutputStreamWriter(out, "UTF-8");
                        bw = new BufferedWriter(osw);
                        saveTxt(data, columName, bw);
                    }
                    else
                    {
                        ExcelUtil.toTimerExcel(out, data, columName);
                    }
                }
                catch (MalformedURLException e)
                {
                }
                catch (IOException e)
                {
                }
                finally
                {
                    this.close(bw, osw, out);
                }
            }
            // ftp盘
            else
            {
                FTPClient ftp = new FTPClient();
                OutputStream out = null;
                OutputStreamWriter osw = null;
                BufferedWriter bw = null;
                try
                {
                    ftp = FTPUtil.getFtpClient(ip, username, pwd);
                    ftp.setFileType(FTP.BINARY_FILE_TYPE);//设置文件类型
                     ftp.enterLocalPassiveMode();
                     ftp.setControlEncoding("UTF-8");
                    ftp.makeDirectory(rootPath);
                    ftp.changeWorkingDirectory(rootPath);
                    
                    out = ftp.storeFileStream(fileName);
                    
                    if ("txt".equals(formatFile))
                    {
                        osw = new OutputStreamWriter(out, "UTF-8");
                        bw = new BufferedWriter(osw);
                        saveTxt(data, columName, bw);
                    }
                    else
                    {
                        ExcelUtil.toTimerExcel(out, data, columName);
                    }
                }
                catch (IOException e)
                {
                }
                finally
                {
                    this.close(bw, osw, out);
                    if (ftp != null && ftp.isConnected())
                    {
                        try
                        {
                            ftp.disconnect();
                        }
                        catch (IOException e)
                        {
                        }
                    }
                }
            }
            
        }
    }
    
    private void saveTxt(List<Map<String, Object>> data, Map<String, String> columName,
            BufferedWriter bw) throws IOException
    {
        Set<String> namesets = columName.keySet();
        String nameStr = "";
        for (String namekey : namesets)
        {
            nameStr += columName.get(namekey) + "    ";
        }
        bw.write(nameStr + "\t\n");
        
        for (Map<String, Object> map : data)
        {
            String str = "";
            Set<String> cs = columName.keySet();
            for (String namekey : cs)
            {
                str += map.get(namekey) + "    ";
            }
            bw.write(str + "\t\n");
        }
    }
    
    private void close(BufferedWriter bw, OutputStreamWriter osw, OutputStream out)
    {
        if (bw != null)
        {
            try
            {
                bw.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        if (osw != null)
        {
            try
            {
                osw.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        if (out != null)
        {
            try
            {
                out.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
}
