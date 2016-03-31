package wmp;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class Main {
	
	public static void main(String[] args) {
		Server server = new Server();
		server.setCpu("0.23");
		server.setMemory("0.65");
		server.setIpaddress("192.168.1.1");
		
		List<DiskInfo> list = new ArrayList<DiskInfo>();
		DiskInfo disk = new DiskInfo();
		disk.setPath("/dev");
		disk.setTotal("125");
		disk.setUsed("25");
		list.add(disk);
		DiskInfo disk1 = new DiskInfo();
		disk1.setPath("/home");
		disk1.setTotal("225");
		disk1.setUsed("20");
		list.add(disk1);
		server.setDisk(list);
		
		String obj = JSON.toJSONString(server);
		System.out.println(obj);
		
		
		
		System.out.println("-------------------------");
		List<Network> netWorkList = new ArrayList<Network>();
		Network a = new Network();
		a.setFromIp("192.168.1.1");
		a.setToIp("10.136.20.11");
		a.setStatus(0);
		netWorkList.add(a);
		a = new Network();
		a.setFromIp("192.168.1.1");
		a.setToIp("10.136.20.12");
		a.setStatus(0);
		netWorkList.add(a);
		a = new Network();
		a.setFromIp("192.168.1.1");
		a.setToIp("10.136.20.13");
		a.setStatus(0);
		netWorkList.add(a);
		System.out.println(JSON.toJSONString(netWorkList));
		
		
	}

}
