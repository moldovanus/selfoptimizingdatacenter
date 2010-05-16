using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using System.Diagnostics;
using System.Management;
using System.Threading;
using ServerMonitorUtils;

namespace ServerManagement
{
    public class ServerMonitor
    {
        public static ServerInfo CollectServerInfo()
        {

            ServerInfo serverInfo = new ServerInfo();
            List<Storage> storageInfo = new List<Storage>();
            List<int> freeCPU = new List<int>();

            ////get host computer core count using WMI
            ManagementObjectSearcher mgmtObjects = new ManagementObjectSearcher("Select * from Win32_ComputerSystem");

            foreach (var item in mgmtObjects.Get())
            {
                serverInfo.CoreCount = Convert.ToInt32(item["NumberOfLogicalProcessors"]);

                break;
            }


            //get free and total clock speed for each core
            mgmtObjects = new ManagementObjectSearcher("Select * from Win32_Processor");

            foreach (var item in mgmtObjects.Get())
            {
                serverInfo.TotalCPU = Convert.ToInt32(item["MaxClockSpeed"]);
                break;
            }

            mgmtObjects = new ManagementObjectSearcher("Select * from Win32_PerfFormattedData_PerfOS_Processor where NOT Name = '_Total' ");

            foreach (ManagementObject item in mgmtObjects.Get())
            {
                freeCPU.Add((int)((100 - Convert.ToDouble(item["PercentProcessorTime"])) / 100 * serverInfo.TotalCPU ));
            }

            serverInfo.FreeCPU = freeCPU;

            //get host computer RAM info
            mgmtObjects = new ManagementObjectSearcher("Select * from Win32_OperatingSystem");

            foreach (ManagementObject item in mgmtObjects.Get())
            {

                serverInfo.TotalMemory = Convert.ToInt32(item["TotalVisibleMemorySize"]) / 1024; // to store in MG
                serverInfo.FreeMemory = Convert.ToInt32(item["FreePhysicalMemory"]) / 1024;

                break;

            }


            //find host computer logical drives size and free space in Kb
            mgmtObjects = new ManagementObjectSearcher("Select * from Win32_LogicalDisk     ");

            foreach (ManagementObject item in mgmtObjects.Get())
            {

                int size = (int)(Convert.ToInt64(item["Size"]) / (1024 * 1024 * 1024));
                if (size == 0)
                {
                    continue;
                }
                Storage storage =
                    new Storage(item["Name"].ToString(),
                         size,
                        (int)(Convert.ToInt64(item["FreeSpace"]) / (1024 * 1024 * 1024)));

                storageInfo.Add(storage);

            }

            serverInfo.Storage = storageInfo;

            return serverInfo;
        }

        public static void Test()
        {

            //ManagementObjectSearcher mgmtObjects = new ManagementObjectSearcher("Select * from Win32_Processor");

            //foreach (var item in mgmtObjects.Get())
            //{

            //   Console.WriteLine(item["LoadPercentage"]);


            //}





            ////get host computer CPU count
            ////using Enviroment
            // Console.WriteLine("CPU Count: {0}", Environment.ProcessorCount);

            ////using WMI
            //ManagementObjectSearcher mgmtObjects =  new ManagementObjectSearcher("Select * from Win32_ComputerSystem");

            //foreach (var item in mgmtObjects.Get())
            //{
            //    Console.WriteLine("Number Of Processors - " +
            //                       item["NumberOfProcessors"]);
            //    Console.WriteLine("Number Of Logical Processors - " +
            //                       item["NumberOfLogicalProcessors"]);
            //}


            ////get host computer RAM info
            //ManagementObjectSearcher mgmtObjects = new ManagementObjectSearcher("Select * from Win32_OperatingSystem      ");

            //foreach (ManagementObject item in mgmtObjects.Get())
            //{

            //     ManagementObject c =(ManagementObject) item["PartComponent"];   
            //    Console.WriteLine("Total memory " +
            //                       item["TotalVisibleMemorySize"]);
            //    Console.WriteLine("Free memory" +
            //                     item["FreePhysicalMemory"]);

            //}

            ////find host computer logical drives size and free space in Kb
            //ManagementObjectSearcher mgmtObjects = new ManagementObjectSearcher("Select * from Win32_LogicalDisk     ");

            //foreach (ManagementObject item in mgmtObjects.Get())
            //{

            //    // ManagementObject c =(ManagementObject) item["PartComponent"];   
            //    Console.WriteLine("Size " +
            //                       item["Size"]);
            //    Console.WriteLine("Free " +
            //                     item["FreeSpace"]);

            //}


            ////list all Performance Counters from that category
            //PerformanceCounterCategory pcc = new PerformanceCounterCategory("Server");
            //{

            //    Console.WriteLine("\n Category : {0}  \n", pcc.CategoryName);
            //    string[] counters = pcc.GetInstanceNames();
            //    foreach (string counter in counters)
            //    {
            //        if (pcc.InstanceExists(counter))
            //        {
            //            PerformanceCounter[] performanceCounters = pcc.GetCounters(counter);

            //            for (int j = 0; j < performanceCounters.Length; j++)
            //            {
            //                Console.WriteLine("InstanceName {0} : CounterName {1}", counter, performanceCounters[j].CounterName);
            //            }
            //        }
            //    }
            //}

            ////placed for testing counters
            //PerformanceCounter guestCpuCounter = new PerformanceCounter
            //   ("Processor Information", "Processor Frequency", "0,1");
            //Console.WriteLine("{0}", guestCpuCounter.NextValue());

            ////list all performance counters from the Windows Server
            //PerformanceCounterCategory[] perfCounters = PerformanceCounterCategory.GetCategories();
            //for (int i = 0; i < perfCounters.Length; i++)
            //{
            //    Console.WriteLine("{0}",perfCounters[i].CategoryName);
            //}




            ////monitor CPU usage for virtual machine TestMachine
            //PerformanceCounter guestCpuCounter = new PerformanceCounter
            //   ("Hyper-V Hypervisor Virtual Processor", "% Guest Run Time", "xp:Hv VP 0");
            //PerformanceCounter totalCpuCounter = new PerformanceCounter
            //  ("Hyper-V Hypervisor Virtual Processor", "% Total Run Time", "xp:Hv VP 0");
            //PerformanceCounter idleCpuCounter = new PerformanceCounter
            //  ("Hyper-V Hypervisor Virtual Processor", "% Hypervisor Run Time", "xp:Hv VP 0");

            //// while (true) because the first call returns 0. the next calls compute the diference between the previous and now and report % value. Also to monitor live the usage
            //while (true)
            //{

            //    Console.WriteLine(""//\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b"
            //        + " TotalTime: {0}  GuestTime: {1} HypervisorTime:{2}",
            //        String.Format("{0:##0} %", totalCpuCounter.NextValue()),
            //        String.Format("{0:##0} %", guestCpuCounter.NextValue()),
            //        String.Format("{0:##0} %", idleCpuCounter.NextValue())
            //        );

            //    Thread.Sleep(1000);
            //}


            ////monitor Memory for virtual machine JeOsMachine
            // PerformanceCounter guestCpuCounter = new PerformanceCounter
            //("Hyper-V Hypervisor Partition", "1G GPA pages", "JeOsMachine:HvPt");
            // PerformanceCounter totalCpuCounter = new PerformanceCounter
            //   ("Hyper-V Hypervisor Partition", "2M GPA pages", "JeOsMachine:HvPt");
            // PerformanceCounter idleCpuCounter = new PerformanceCounter
            //   ("Hyper-V Hypervisor Partition", "4K GPA pages", "JeOsMachine:HvPt"); //aici se vede cat o primit masina virtuala. Cel putin XP si JeOs primesc pagini de 4 k da mai au din cand in cand si pe 2M

            // // while (true) because the first call returns 0( sau poate numa la CPU face asa). the next calls compute the diference between the previous and now and report % value
            // while (true)
            // {

            //     Console.WriteLine(""//\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b"
            //         + " 1G pages memory: {0}  2M pages memory: {1} 4k pages memory:{2}",
            //         String.Format("{0:##0} MB", totalCpuCounter.NextValue() * 1024),
            //         String.Format("{0:##0} MB", guestCpuCounter.NextValue() * 2),
            //         String.Format("{0:##0} MB", idleCpuCounter.NextValue() * 4 / 1024)
            //         );
            //     Thread.Sleep(1000);
            // }


            // //monitor Memory for virtual machine JeOsMachine
            // PerformanceCounter readCounter = new PerformanceCounter
            //("Hyper-V Virtual IDE Controller", "Read Bytes/sec", "TestMachine:Ide Controller");
            // PerformanceCounter writeCounter = new PerformanceCounter
            //   ("Hyper-V Virtual IDE Controller", "Write Bytes/sec", "TestMachine:Ide Controller");


            // // while (true) because the first call returns 0( sau poate numa la CPU face asa). the next calls compute the diference between the previous and now and report % value
            // while (true)
            // {

            //     Console.WriteLine(""//\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b"
            //         + " Read: {0}  Write: {1}  ",
            //         String.Format("{0:##0} MB", readCounter.NextValue() ),
            //         String.Format("{0:##0} MB", writeCounter.NextValue() )
            //         );

            //     Thread.Sleep(1000);
            // }


            ////get Hard Drive Info
            ///*
            // * Placed here  for easy reference :D
            // * class Msvm_ResourceAllocationSettingData : CIM_ResourceAllocationSettingData
            //    {
            //      string  Caption;
            //      string  Description;
            //      string  InstanceID;
            //      string  ElementName;
            //      uint16  ResourceType;
            //      string  OtherResourceType;
            //      string  ResourceSubType;
            //      string  PoolID;
            //      uint16  ConsumerVisibility;
            //      string  HostResource[];
            //      string  AllocationUnits;
            //      uint64  VirtualQuantity;
            //      uint64  Reservation;
            //      uint64  Limit;
            //      uint32  Weight;
            //      boolean AutomaticAllocation;
            //      boolean AutomaticDeallocation;
            //      string  Parent;
            //      string  Connection[]; //the path of the virtual resource
            //      string  Address;
            //      uint16  MappingBehavior;
            //      string  VirtualSystemIdentifiers[] = { "GUID" };
            //    };
            // * */
            ////get Resource Allocation Data
            //ManagementScope manScope = new ManagementScope(@"\\.\root\virtualization");
            //ManagementObject targetMachine = Utility.GetTargetComputer("xp", manScope);
            ////get machine hard drive : ResourceSubType needed because under VirtualStorage are  Virtual CD/DVD and Virtual Hard Drive as subtypes
            //ManagementObject RASD = Utility.GetResourceAllocationsettingData(targetMachine, ResourceType.VirtualStorage, ResourceSubType.VHD);


            //if (RASD != null)
            //{
            //    Console.WriteLine("{0}", RASD["VirtualQuantity"].ToString());
            //    //at connection[0] is the virtual hard drive file :D
            //    string[] connection = (string[])RASD["Connection"];
            //    Console.WriteLine("{0}", Utility.GetVirtualHardDiskInfo(connection[0]));

            //}
            //else
            //{
            //    Console.WriteLine("Is null");
            //}


            ////get Memory data
            //ManagementScope manScope = new ManagementScope(@"\\.\root\virtualization");
            //ManagementObject targetMachine = Utility.GetTargetComputer("TestMachine", manScope);
            ////get machine hard drive : ResourceSubType needed because under VirtualStorage are  Virtual CD/DVD and Virtual Hard Drive as subtypes
            //ManagementObject RASD = Utility.GetMemorySettingData(targetMachine);


            //if (RASD != null)
            //{
            //    Console.WriteLine("{0}", RASD["VirtualQuantity"].ToString());// Reservation and Limit give same result. The amount of memory assigned to virtual machine

            //}
            //else
            //{
            //    Console.WriteLine("Is null");
            //}

            ////get assigned CPU
            //ManagementScope manScope = new ManagementScope(@"\\.\root\virtualization");
            //ManagementObject targetMachine = Utility.GetTargetComputer("TestMachine", manScope);
            ////get machine hard drive : ResourceSubType needed because under VirtualStorage are  Virtual CD/DVD and Virtual Hard Drive as subtypes
            //ManagementObjectCollection processors = Utility.GetProcessorsData(targetMachine);
            //foreach (ManagementObject processor in processors)
            //{

            //    Console.WriteLine("{0}", processor["MaxClockSpeed"].ToString());
            //    Console.WriteLine("{0}", processor["CurrentClockSpeed"].ToString());

            //}
            // ImportVirtualSystemEx("C:/xp/TestMachine");
            Console.ReadLine();

        }


    }
}