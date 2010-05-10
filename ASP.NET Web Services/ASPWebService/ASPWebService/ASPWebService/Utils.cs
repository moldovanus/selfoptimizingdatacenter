using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Management;

namespace ServerMonitorUtils
{

    static class ResourceType
    {
        public const UInt16 Other = 1;
        public const UInt16 ComputerSystem = 2;
        public const UInt16 Processor = 3;
        public const UInt16 Memory = 4;
        public const UInt16 IDEController = 5;
        public const UInt16 ParallelSCSIHBA = 6;
        public const UInt16 FCHBA = 7;
        public const UInt16 iSCSIHBA = 8;
        public const UInt16 IBHCA = 9;
        public const UInt16 EthernetAdapter = 10;
        public const UInt16 OtherNetworkAdapter = 11;
        public const UInt16 IOSlot = 12;
        public const UInt16 IODevice = 13;
        public const UInt16 FloppyDrive = 14;
        public const UInt16 CDDrive = 15;
        public const UInt16 DVDdrive = 16;
        public const UInt16 Serialport = 17;
        public const UInt16 Parallelport = 18;
        public const UInt16 USBController = 19;
        public const UInt16 GraphicsController = 20;
        public const UInt16 VirtualStorage = 21; //modified
        public const UInt16 Disk = 22;
        public const UInt16 Tape = 23;
        public const UInt16 OtherStorageDevice = 24;
        public const UInt16 FirewireController = 25;
        public const UInt16 PartitionableUnit = 26;
        public const UInt16 BasePartitionableUnit = 27;
        public const UInt16 PowerSupply = 28;
        public const UInt16 CoolingDevice = 29;


        public const UInt16 DisketteController = 1;
    }

    static class ResourceSubType
    {
        public const string DisketteController = null;
        public const string DisketteDrive = "Microsoft Synthetic Diskette Drive";
        public const string ParallelSCSIHBA = "Microsoft Synthetic SCSI Controller";
        public const string IDEController = "Microsoft Emulated IDE Controller";
        public const string DiskSynthetic = "Microsoft Synthetic Disk Drive";
        public const string DiskPhysical = "Microsoft Physical Disk Drive";
        public const string DVDPhysical = "Microsoft Physical DVD Drive";
        public const string DVDSynthetic = "Microsoft Synthetic DVD Drive";
        public const string CDROMPhysical = "Microsoft Physical CD Drive";
        public const string CDROMSynthetic = "Microsoft Synthetic CD Drive";
        public const string EthernetSynthetic = "Microsoft Synthetic Ethernet Port";
        public const string Processor = "Microsoft Processor"; //added

        //logical drive
        public const string DVDLogical = "Microsoft Virtual CD/DVD Disk";
        public const string ISOImage = "Microsoft ISO Image";
        public const string VHD = "Microsoft Virtual Hard Disk";
        public const string DVD = "Microsoft Virtual DVD Disk";
        public const string VFD = "Microsoft Virtual Floppy Disk";
        public const string videoSynthetic = "Microsoft Synthetic Display Controller";
    }

    static class OtherResourceType
    {
        public const string DisketteController = "Microsoft Virtual Diskette Controller";

    }
    static class ReturnCode
    {
        public const UInt32 Completed = 0;
        public const UInt32 Started = 4096;
        public const UInt32 Failed = 32768;
        public const UInt32 AccessDenied = 32769;
        public const UInt32 NotSupported = 32770;
        public const UInt32 Unknown = 32771;
        public const UInt32 Timeout = 32772;
        public const UInt32 InvalidParameter = 32773;
        public const UInt32 SystemInUser = 32774;
        public const UInt32 InvalidState = 32775;
        public const UInt32 IncorrectDataType = 32776;
        public const UInt32 SystemNotAvailable = 32777;
        public const UInt32 OutofMemory = 32778;
    }

    class Utility
    {
        static class JobState
        {
            public const UInt16 New = 2;
            public const UInt16 Starting = 3;
            public const UInt16 Running = 4;
            public const UInt16 Suspended = 5;
            public const UInt16 ShuttingDown = 6;
            public const UInt16 Completed = 7;
            public const UInt16 Terminated = 8;
            public const UInt16 Killed = 9;
            public const UInt16 Exception = 10;
            public const UInt16 Service = 11;
        }
        /// <summary>
        /// Common utility function to get a service object
        /// </summary>
        /// <param name="scope"></param>
        /// <param name="serviceName"></param>
        /// <returns></returns>
        public static ManagementObject GetServiceObject(ManagementScope scope, string serviceName)
        {

            scope.Connect();
            ManagementPath wmiPath = new ManagementPath(serviceName);
            ManagementClass serviceClass = new ManagementClass(scope, wmiPath, null);
            ManagementObjectCollection services = serviceClass.GetInstances();

            ManagementObject serviceObject = null;

            foreach (ManagementObject service in services)
            {
                serviceObject = service;
            }
            return serviceObject;
        }

        public static ManagementObject GetHostSystemDevice(string deviceClassName, string deviceObjectElementName, ManagementScope scope)
        {
            string hostName = System.Environment.MachineName;
            ManagementObject systemDevice = GetSystemDevice(deviceClassName, deviceObjectElementName, hostName, scope);
            return systemDevice;
        }


        public static ManagementObject GetSystemDevice
        (
            string deviceClassName,
            string deviceObjectElementName,
            string vmName,
            ManagementScope scope)
        {
            ManagementObject systemDevice = null;
            ManagementObject computerSystem = Utility.GetTargetComputer(vmName, scope);

            ManagementObjectCollection systemDevices = computerSystem.GetRelated
            (
                deviceClassName,
                "Msvm_SystemDevice",
                null,
                null,
                "PartComponent",
                "GroupComponent",
                false,
                null
            );

            foreach (ManagementObject device in systemDevices)
            {
                if (device["ElementName"].ToString().ToLower() == deviceObjectElementName.ToLower())
                {
                    systemDevice = device;
                    break;
                }
            }

            return systemDevice;
        }



        public static bool JobCompleted(ManagementBaseObject outParams, ManagementScope scope)
        {
            bool jobCompleted = true;

            //Retrieve msvc_StorageJob path. This is a full wmi path
            string JobPath = (string)outParams["Job"];
            ManagementObject Job = new ManagementObject(scope, new ManagementPath(JobPath), null);
            //Try to get storage job information
            Job.Get();
            while ((UInt16)Job["JobState"] == JobState.Starting
                || (UInt16)Job["JobState"] == JobState.Running)
            {
                Console.WriteLine("In progress... {0}% completed.", Job["PercentComplete"]);
                System.Threading.Thread.Sleep(1000);
                Job.Get();
            }

            //Figure out if job failed
            UInt16 jobState = (UInt16)Job["JobState"];
            if (jobState != JobState.Completed)
            {
                UInt16 jobErrorCode = (UInt16)Job["ErrorCode"];
                Console.WriteLine("Error Code:{0}", jobErrorCode);
                Console.WriteLine("ErrorDescription: {0}", (string)Job["ErrorDescription"]);
                jobCompleted = false;
            }
            return jobCompleted;
        }


        public static ManagementObject GetTargetComputer(string vmElementName, ManagementScope scope)
        {
            string query = string.Format("select * from Msvm_ComputerSystem Where ElementName = '{0}'", vmElementName);

            ManagementObjectSearcher searcher = new ManagementObjectSearcher(scope, new ObjectQuery(query));

            ManagementObjectCollection computers = searcher.Get();

            ManagementObject computer = null;

            foreach (ManagementObject instance in computers)
            {
                computer = instance;
                break;
            }
            return computer;
        }

        public static ManagementObject GetVirtualSystemSettingData(ManagementObject vm)
        {
            ManagementObject vmSetting = null;
            ManagementObjectCollection vmSettings = vm.GetRelated
            (
                "Msvm_VirtualSystemSettingData",
                "Msvm_SettingsDefineState",
                null,
                null,
                "SettingData",
                "ManagedElement",
                false,
                null
            );

            if (vmSettings.Count != 1)
            {
                throw new Exception(String.Format("{0} instance of Msvm_VirtualSystemSettingData was found", vmSettings.Count));
            }

            foreach (ManagementObject instance in vmSettings)
            {
                vmSetting = instance;
                break;
            }

            return vmSetting;
        }


        enum ValueRole
        {
            Default = 0,
            Minimum = 1,
            Maximum = 2,
            Increment = 3
        }
        enum ValueRange
        {
            Default = 0,
            Minimum = 1,
            Maximum = 2,
            Increment = 3
        }


        //
        // Get RASD definitions
        //
        public static ManagementObject GetResourceAllocationsettingDataDefault
        (
            ManagementScope scope,
            UInt16 resourceType,
            string resourceSubType,
            string otherResourceType
            )
        {
            ManagementObject RASD = null;

            string query = String.Format("select * from Msvm_ResourcePool where ResourceType = '{0}' and ResourceSubType ='{1}' and OtherResourceType = '{2}'",
                             resourceType, resourceSubType, otherResourceType);

            if (resourceType == ResourceType.Other)
            {
                query = String.Format("select * from Msvm_ResourcePool where ResourceType = '{0}' and ResourceSubType = null and OtherResourceType = {1}",
                                             resourceType, otherResourceType);
            }
            else
            {
                query = String.Format("select * from Msvm_ResourcePool where ResourceType = '{0}' and ResourceSubType ='{1}' and OtherResourceType = null",
                                             resourceType, resourceSubType);
            }

            ManagementObjectSearcher searcher = new ManagementObjectSearcher(scope, new ObjectQuery(query));

            ManagementObjectCollection poolResources = searcher.Get();

            //Get pool resource allocation ability
            if (poolResources.Count == 1)
            {
                foreach (ManagementObject poolResource in poolResources)
                {
                    ManagementObjectCollection allocationCapabilities = poolResource.GetRelated("Msvm_AllocationCapabilities");
                    foreach (ManagementObject allocationCapability in allocationCapabilities)
                    {
                        ManagementObjectCollection settingDatas = allocationCapability.GetRelationships("Msvm_SettingsDefineCapabilities");
                        foreach (ManagementObject settingData in settingDatas)
                        {

                            if (Convert.ToInt16(settingData["ValueRole"]) == (UInt16)ValueRole.Default)
                            {
                                RASD = new ManagementObject(settingData["PartComponent"].ToString());
                                break;
                            }
                        }
                    }
                }
            }

            return RASD;
        }

        /*
         * Modified by us
         * */
        public static ManagementObject GetResourceAllocationsettingData
        (
            ManagementObject vm,
            UInt16 resourceType,
            string resourceSubType
            )
        {
            //vm->vmsettings->RASD for IDE controller
            ManagementObject RASD = null;
            ManagementObjectCollection settingDatas = vm.GetRelated("Msvm_VirtualSystemsettingData");
            foreach (ManagementObject settingData in settingDatas)
            {
                //retrieve the rasd
                ManagementObjectCollection RASDs = settingData.GetRelated("Msvm_ResourceAllocationsettingData");
                foreach (ManagementObject rasdInstance in RASDs)
                {


                    if (Convert.ToUInt16(rasdInstance["ResourceType"]) == resourceType)
                    {
                        //Console.WriteLine("{0} {1}", rasdInstance["ResourceSubType"], Convert.ToUInt16(rasdInstance["ResourceType"]));
                        //found the matching type

                        if (rasdInstance["ResourceSubType"].ToString() == resourceSubType)
                        {
                            RASD = rasdInstance;
                            break;
                        }


                    }
                }

            }
            return RASD;
        }

        /**
        * Create by us
        */
        public static ManagementObjectCollection GetProcessorsData(ManagementObject vm)
        {
            //vm->vmsettings->RASD for IDE controller
            ManagementObjectCollection settingDatas = vm.GetRelated("Msvm_VirtualSystemsettingData");
            foreach (ManagementObject settingData in settingDatas)
            {
                //retrieve the rasd
                ManagementObjectCollection RASDs = settingData.GetRelated("Msvm_ProcessorSettingData");

                //only one Msvm_ProcessorSettingData element is in the collection 
                foreach (ManagementObject o in RASDs)
                {
                    ManagementObjectCollection p = o.GetRelated("Msvm_Processor");
                    if (p.Count != 0)
                    {
                        return p;
                    }

                }

            }
            return null;
        }
        /**
         * Create by us
         */
        public static ManagementObject GetMemorySettingData(ManagementObject vm)
        {
            //vm->vmsettings->RASD for IDE controller
            ManagementObject RASD = null;
            ManagementObjectCollection settingDatas = vm.GetRelated("Msvm_VirtualSystemsettingData");
            foreach (ManagementObject settingData in settingDatas)
            {
                //retrieve the rasd
                ManagementObjectCollection RASDs = settingData.GetRelated("Msvm_MemorySettingData");
                foreach (ManagementObject rasdInstance in RASDs)
                {
                    if (Convert.ToUInt16(rasdInstance["ResourceType"]) == ResourceType.Memory)
                    {
                        RASD = rasdInstance;
                        break;
                    }
                }

            }
            return RASD;
        }

        /***
         *created by us
         **/
        public static string GetVirtualHardDiskInfo(string path)
        {
            ManagementScope scope = new ManagementScope(@"root\virtualization", null);
            ManagementObject imageService = Utility.GetServiceObject(scope, "Msvm_ImageManagementService");
            ManagementBaseObject inParams = imageService.GetMethodParameters("GetVirtualHardDiskInfo");
            inParams["Path"] = path;

            ManagementBaseObject outParams = imageService.InvokeMethod("GetVirtualHardDiskInfo", inParams, null);
            string diskInfo = null;
            if (outParams != null)
            {
                if ((UInt32)outParams["ReturnValue"] == ReturnCode.Completed)
                {
                    //no error
                    diskInfo = (string)outParams["Info"];

                }
                else
                {   // error occurred
                    Console.WriteLine("GetVirtualHardDiskInfo failed. ReturnValue:{0}", (UInt32)outParams["ReturnValue"]);
                }
            }
            else
            {
                Console.WriteLine("WMI failed");
            }

            inParams.Dispose();
            outParams.Dispose();
            imageService.Dispose();
            return diskInfo;
        }

    }

}