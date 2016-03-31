package wmp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class Server {
	
	public static void main(String[] args) {
		BufferedReader br = null;  
        try {  
            Process p = Runtime.getRuntime().exec("ping 127.0.0.1");  
            br = new BufferedReader(new InputStreamReader(p.getInputStream(),"gbk"));  
            String line = null;  
            StringBuilder sb = new StringBuilder();  
            while ((line = br.readLine()) != null) {  
                sb.append(line + "\n");  
            }  
            System.out.println(sb.toString());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }   
        finally  
        {  
            if (br != null)  
            {  
                try {  
                    br.close();  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
	}
	
	
	private String ipaddress;
	
	private String cpu;
	
	private String memory;
	
	private List<DiskInfo>  disk;
	
	public String getIpaddress() {
		return ipaddress;
	}


	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}




	public String getCpu() {
		return cpu;
	}




	public void setCpu(String cpu) {
		this.cpu = cpu;
	}




	public String getMemory() {
		return memory;
	}




	public void setMemory(String memory) {
		this.memory = memory;
	}




	public List<DiskInfo> getDisk() {
		return disk;
	}




	public void setDisk(List<DiskInfo> disk) {
		this.disk = disk;
	}




}


class DiskInfo{
	private String path;
	private String total;
	private String used;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getUsed() {
		return used;
	}
	public void setUsed(String used) {
		this.used = used;
	}
	
	
}


