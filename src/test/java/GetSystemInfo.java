import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;


public class GetSystemInfo {

	public static void main(String[] args) throws SigarException {
		Sigar sigar = new Sigar();
		for(CpuInfo cpuInfo: sigar.getCpuInfoList()){
			String info = "[Total Cores="+cpuInfo.getTotalCores();
			info+=",Vendor="+cpuInfo.getVendor();
			info+=",Mhz="+cpuInfo.getMhz();
			info+=",Model="+cpuInfo.getModel();
			info+="]";
			System.out.println("CPU Info="+info);
		}
		System.out.println("PID="+sigar.getPid());
		System.out.println("FQDN="+sigar.getFQDN());
		System.out.println("Uptime="+sigar.getUptime().getUptime()+" secs");
		System.out.println("Memory="+sigar.getMem().toMap());
		System.out.println("CPU="+sigar.getCpu().toMap());
		System.out.println("NetInfo="+sigar.getNetInfo().toMap());
		System.out.println("TCP="+sigar.getTcp().toMap());
		System.out.println("FileSysMap="+sigar.getFileSystemMap());
		
	}
}
