package com.txy.web.common.util;

import java.io.IOException;

import ch.ethz.ssh2.Connection;


public class SSHUtil {

	
	public static void main(String agrs []){
//		readFile("10.216.72.18","wlklog","wlklog","/var/log/","vsftp*.log");
		//构造链接
		Connection con = new Connection("10.216.72.18");
		try {
			con.connect();
			//验证账号密码
			if(!con.authenticateWithPassword("wlklog","wlklog")){
				System.out.println("Linux密码账号不对！ip:fsdsfd  userName:vsfsd  ");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取liunx里的文件
	 */
	public static void readFile(String ip,String username,String password,String pathurl,String filematches){
//		SshClient client=new SshClient();
//        try{
//            client.connect("10.216.72.18",22);
//            //设置用户名和密码
//            PasswordAuthenticationClient pwd = new PasswordAuthenticationClient();
//            pwd.setUsername("root");
//            pwd.setPassword("de1qing2qu3zhen4");
//            int result=client.authenticate(pwd);
//            if(result==AuthenticationProtocolState.COMPLETE){//如果连接完成
//                System.out.println("==============="+result);
//                String pathName = SSHUtil.class.getResource("/").getPath();
//                
//                Pattern pattern = Pattern.compile(filematches);
//				List<SftpFile> list = client.openSftpClient().ls(pathurl); //文件地址
//                for (SftpFile f : list){
//                    System.out.println(f.getFilename());
//                    System.out.println(f.getAbsolutePath());
//                    if(pattern.matcher(f.getFilename()).matches()){
//                        OutputStream os = new FileOutputStream(pathName+f.getFilename());
//                        client.openSftpClient().get(f.getAbsolutePath(), os);
//                        //以行为单位读取文件start
//                        File file = new File(pathName+f.getFilename());
//                        BufferedReader reader = null;
//                        try {
//                            System.out.println("以行为单位读取文件内容，一次读一整行：");
//                            reader = new BufferedReader(new FileReader(file));
//                            String tempString = null;
//                            int line = 1;//行号
//                            //一次读入一行，直到读入null为文件结束
//                            while ((tempString = reader.readLine()) != null){
//                                //显示行号
//                                System.out.println("line " + line + ": " + tempString);
//                                line++;
//                            }
//                            reader.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } finally {
//                            if (reader != null) {
//                                try {
//                                    reader.close();
//                                } catch (IOException e1) {
//                                }
//                            }
//                        }
//                        //以行为单位读取文件end
//                    }
//                }
//            }
//        }catch(IOException e){
//            e.printStackTrace();
//        }
	}
}
