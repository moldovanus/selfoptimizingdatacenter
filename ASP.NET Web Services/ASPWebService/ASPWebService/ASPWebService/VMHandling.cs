using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Management;
using System.IO;
using System.Runtime.InteropServices;
using System.Windows.Forms;

using ServerMonitorUtils;

namespace ServerManagement
{
    class VMHandling
    {
        [DllImport("PowrProf.dll")]
        public static extern bool SetSuspendState(bool Hibernate, bool ForceCritical, bool DisableWakeEvent);
        static ManagementObject GetLastVirtualSystemSnapshot(ManagementObject vm)
        {
            ManagementObjectCollection settings = vm.GetRelated(
                "Msvm_VirtualSystemsettingData",
                "Msvm_PreviousSettingData",
                null,
                null,
                "SettingData",
                "ManagedElement",
                false,
                null);
            ManagementObject virtualSystemsetting = null;
            foreach (ManagementObject setting in settings)
            {
                Console.WriteLine(setting.Path.Path);
                Console.WriteLine(setting["ElementName"]);
                virtualSystemsetting = setting;
            }
            return virtualSystemsetting;
        }


        static string GetVirtualSystemExportSettingDataInstance(ManagementScope scope, ManagementObject snapshotSettingData)
        {
            ManagementPath settingPath = new ManagementPath("Msvm_VirtualSystemExportSettingData");

            ManagementClass exportSettingDataClass = new ManagementClass(scope, settingPath, null);
            ManagementObject exportSettingData = exportSettingDataClass.CreateInstance();

            exportSettingData["CopySnapshotConfiguration"] = 2;
            exportSettingData["CreateVmExportSubdirectory"] = true;
            exportSettingData["SnapshotVirtualSystem"] = snapshotSettingData.Path.Path;

            string settingData = exportSettingData.GetText(TextFormat.CimDtd20);

            exportSettingData.Dispose();
            exportSettingDataClass.Dispose();

            return settingData;
        }


        public static void ExportVirtualSystemExSnapshots(string vmName, string exportDirectory)
        {

            ManagementScope scope = new ManagementScope(@"\root\virtualization", null);
            ManagementObject virtualSystemService = Utility.GetServiceObject(scope, "Msvm_VirtualSystemManagementService");

            ManagementBaseObject inParams = virtualSystemService.GetMethodParameters("ExportVirtualSystemEx");

            ManagementObject vm = Utility.GetTargetComputer(vmName, scope);
            CreateVirtualSystemSnapshot(vmName);
            ManagementObject snapshotSettingData = GetLastVirtualSystemSnapshot(vm);
            if (snapshotSettingData == null)
            {
                Console.WriteLine("The snapshot wasn't created");
                return;
            }
            inParams["ComputerSystem"] = vm.Path.Path;

            if (!Directory.Exists(exportDirectory))
            {
                Directory.CreateDirectory(exportDirectory);
            }
            inParams["ExportDirectory"] = exportDirectory;
            inParams["ExportSettingData"] = GetVirtualSystemExportSettingDataInstance(scope, snapshotSettingData);

            ManagementBaseObject outParams = virtualSystemService.InvokeMethod("ExportVirtualSystemEx", inParams, null);

            if ((UInt32)outParams["ReturnValue"] == ReturnCode.Started)
            {
                if (Utility.JobCompleted(outParams, scope))
                {
                    Console.WriteLine("VM '{0}' were exported successfully.", vm["ElementName"]);
                }
                else
                {
                    Console.WriteLine("Failed to export VM");
                }
            }
            else if ((UInt32)outParams["ReturnValue"] == ReturnCode.Completed)
            {
                Console.WriteLine("VM '{0}' were exported successfully.", vm["ElementName"]);
            }
            else
            {
                Console.WriteLine("Export virtual system failed with error:{0}", outParams["ReturnValue"]);
            }

            inParams.Dispose();
            outParams.Dispose();
            vm.Dispose();
            virtualSystemService.Dispose();
        }
        /**************************Config only*****************************************************/
        static string GetConfigOnlyVirtualSystemExportSettingDataInstance(ManagementScope scope)
        {
            ManagementPath settingPath = new ManagementPath("Msvm_VirtualSystemExportSettingData");

            ManagementClass exportSettingDataClass = new ManagementClass(scope, settingPath, null);
            ManagementObject exportSettingData = exportSettingDataClass.CreateInstance();

            // Do not copy VHDs and AVHDs but copy the Snapshot configuration and Saved State information (Runtime information) if present
            exportSettingData["CopySnapshotConfiguration"] = 0;
            exportSettingData["CopyVmRuntimeInformation"] = true;
            exportSettingData["CopyVmStorage"] = false;
            exportSettingData["CreateVmExportSubdirectory"] = true;

            string settingData = exportSettingData.GetText(TextFormat.CimDtd20);

            exportSettingData.Dispose();
            exportSettingDataClass.Dispose();

            return settingData;
        }


        static void ExportVirtualSystemExConfigOnly(string vmName, string exportDirectory)
        {
            ManagementScope scope = new ManagementScope(@"root\virtualization", null);
            ManagementObject virtualSystemService = Utility.GetServiceObject(scope, "Msvm_VirtualSystemManagementService");

            ManagementBaseObject inParams = virtualSystemService.GetMethodParameters("ExportVirtualSystemEx");

            ManagementObject vm = Utility.GetTargetComputer(vmName, scope);
            inParams["ComputerSystem"] = vm.Path.Path;

            if (!Directory.Exists(exportDirectory))
            {
                Directory.CreateDirectory(exportDirectory);
            }
            inParams["ExportDirectory"] = exportDirectory;
            inParams["ExportSettingData"] = GetConfigOnlyVirtualSystemExportSettingDataInstance(scope);

            ManagementBaseObject outParams = virtualSystemService.InvokeMethod("ExportVirtualSystemEx", inParams, null);

            if ((UInt32)outParams["ReturnValue"] == ReturnCode.Started)
            {
                if (Utility.JobCompleted(outParams, scope))
                {
                    Console.WriteLine("VM '{0}' were exported successfully.", vm["ElementName"]);

                }
                else
                {
                    Console.WriteLine("Failed to export VM");
                }
            }
            else if ((UInt32)outParams["ReturnValue"] == ReturnCode.Completed)
            {
                Console.WriteLine("VM '{0}' were exported successfully.", vm["ElementName"]);
            }
            else
            {
                Console.WriteLine("Export virtual system failed with error:{0}", outParams["ReturnValue"]);
            }

            inParams.Dispose();
            outParams.Dispose();
            vm.Dispose();
            virtualSystemService.Dispose();
        }
        /**********************************Create Snapshot *************/
        static void CreateVirtualSystemSnapshot(string vmName)
        {
            ManagementScope scope = new ManagementScope(@"root\virtualization", null);
            ManagementObject virtualSystemService = Utility.GetServiceObject(scope, "Msvm_VirtualSystemManagementService");

            ManagementObject vm = Utility.GetTargetComputer(vmName, scope);

            ManagementBaseObject inParams = virtualSystemService.GetMethodParameters("CreateVirtualSystemSnapshot");
            inParams["SourceSystem"] = vm.Path.Path;

            ManagementBaseObject outParams = virtualSystemService.InvokeMethod("CreateVirtualSystemSnapshot", inParams, null);

            if ((UInt32)outParams["ReturnValue"] == ReturnCode.Started)
            {
                if (Utility.JobCompleted(outParams, scope))
                {
                    Console.WriteLine("Snapshot was created successfully.");

                }
                else
                {
                    Console.WriteLine("Failed to create snapshot VM");
                }
            }
            else if ((UInt32)outParams["ReturnValue"] == ReturnCode.Completed)
            {
                Console.WriteLine("Snapshot was created successfully.");
            }
            else
            {
                Console.WriteLine("Create virtual system snapshot failed with error {0}", outParams["ReturnValue"]);
            }

            inParams.Dispose();
            outParams.Dispose();
            vm.Dispose();
            virtualSystemService.Dispose();
        }





        /*************************************************************************************************************************************************/
        public static ManagementObject CreateSwitch(ManagementScope scope, string name, string friendlyName, int learnableAddress)
        {
            ManagementObject switchService = Utility.GetServiceObject(scope, "Msvm_VirtualSwitchManagementService");
            ManagementObject createdSwitch = null;

            ManagementBaseObject inParams = switchService.GetMethodParameters("CreateSwitch");
            inParams["FriendlyName"] = friendlyName;
            inParams["Name"] = name;
            inParams["NumLearnableAddresses"] = learnableAddress;
            inParams["ScopeofResidence"] = null;
            ManagementBaseObject outParams = switchService.InvokeMethod("CreateSwitch", inParams, null);
            if ((UInt32)outParams["ReturnValue"] == ReturnCode.Completed)
            {
                Console.WriteLine("{0} was created successfully", inParams["Name"]);
                createdSwitch = new ManagementObject(outParams["CreatedVirtualSwitch"].ToString());
            }
            else
            {
                Console.WriteLine("Failed to create {0} switch.", inParams["Name"]);
            }
            return createdSwitch;
        }
       public static void ModifyVirtualSystemName(string vmName, string vmNewName)
        {
            ManagementScope scope = new ManagementScope(@"root\virtualization", null);
            ManagementObject virtualSystemService = Utility.GetServiceObject(scope, "Msvm_VirtualSystemManagementService");

            ManagementBaseObject inParams = virtualSystemService.GetMethodParameters("ModifyVirtualSystem");

            ManagementObject vm = Utility.GetTargetComputer(vmName, scope);
            inParams["ComputerSystem"] = vm.Path.Path;

            ManagementObject settingData = null;
            ManagementObjectCollection settingsData = vm.GetRelated("Msvm_VirtualSystemSettingData",
                                                                     "Msvm_SettingsDefineState",
                                                                     null,
                                                                     null,
                                                                     "SettingData",
                                                                     "ManagedElement",
                                                                     false,
                                                                     null);

            foreach (ManagementObject data in settingsData)
            {
                settingData = data;
            }

            settingData["ElementName"] = vmNewName;

            inParams["SystemsettingData"] = settingData.GetText(TextFormat.CimDtd20);

            ManagementBaseObject outParams = virtualSystemService.InvokeMethod("ModifyVirtualSystem", inParams, null);

            if ((UInt32)outParams["ReturnValue"] == ReturnCode.Started)
            {
                if (Utility.JobCompleted(outParams, scope))
                {
                    Console.WriteLine("VM '{0}' was renamed successfully.", vm["ElementName"]);

                }
                else
                {
                    Console.WriteLine("Failed to Modify VM");
                }
            }
            else if ((UInt32)outParams["ReturnValue"] == ReturnCode.Completed)
            {
                Console.WriteLine("VM '{0}' was renamed successfully.", vm["ElementName"]);
            }
            else
            {
                Console.WriteLine("Failed to modify VM. Error {0}", outParams["ReturnValue"]);
            }

            inParams.Dispose();
            outParams.Dispose();
            vm.Dispose();
            virtualSystemService.Dispose();
        }


        public static void ImportVirtualSystem(string importDirectory)
        {
            ManagementScope scope = new ManagementScope(@"root\virtualization", null);
            ManagementObject virtualSystemService = Utility.GetServiceObject(scope, "Msvm_VirtualSystemManagementService");

            ManagementBaseObject inParams = virtualSystemService.GetMethodParameters("ImportVirtualSystem");
            inParams["GenerateNewID"] = true;
            inParams["ImportDirectory"] = importDirectory;

            ManagementBaseObject outParams = virtualSystemService.InvokeMethod("ImportVirtualSystem", inParams, null);

            if ((UInt32)outParams["ReturnValue"] == ReturnCode.Started)
            {
                if (Utility.JobCompleted(outParams, scope))
                {
                    Console.WriteLine("VM were imported successfully.");

                }
                else
                {
                    Console.WriteLine("Failed to import VM");
                }
            }
            else if ((UInt32)outParams["ReturnValue"] == ReturnCode.Completed)
            {
                Console.WriteLine("VM were imported successfully.");
            }
            else
            {
                Console.WriteLine("Import virtual system failed with error {0}", outParams["ReturnValue"]);
            }

            inParams.Dispose();
            outParams.Dispose();
            virtualSystemService.Dispose();
        }

        public static ManagementBaseObject GetVirtualSystemImportSettingData(ManagementScope scope, string importDirectory,string vmName, string rootDirectoryToCopy)
        {

            string targetVhdResourcePath = rootDirectoryToCopy; //Directories specified should exist
            

            ManagementObject virtualSystemService = Utility.GetServiceObject(scope, "Msvm_VirtualSystemManagementService");
            ManagementBaseObject importSettingData = null;
            ManagementBaseObject inParams = virtualSystemService.GetMethodParameters("GetVirtualSystemImportSettingData");
            inParams["ImportDirectory"] = importDirectory;

            ManagementBaseObject outParams = virtualSystemService.InvokeMethod("GetVirtualSystemImportSettingData", inParams, null);

            if ((UInt32)outParams["ReturnValue"] == ReturnCode.Started)
            {
                if (Utility.JobCompleted(outParams, scope))
                {
                    importSettingData = (ManagementBaseObject)outParams["ImportSettingData"];
                    Console.WriteLine("Import Setting Data for the ImportDirectory '{0}' was retrieved successfully.", importDirectory);
                }
                else
                {
                    Console.WriteLine("Failed to get the Import Setting Data");
                }
            }
            else if ((UInt32)outParams["ReturnValue"] == ReturnCode.Completed)
            {
                importSettingData = (ManagementBaseObject)outParams["ImportSettingData"];
                Console.WriteLine("Import Setting Data for the ImportDirectory '{0}' was retrieved successfully.", importDirectory);
            }
            else
            {
                Console.WriteLine("Failed to get the Import Setting Data for the Import Directory :{0}", (UInt32)outParams["ReturnValue"]);
            }

            inParams.Dispose();
            outParams.Dispose();
            virtualSystemService.Dispose();

            importSettingData["GenerateNewId"] = true;
            importSettingData["CreateCopy"] = true;
            importSettingData["Name"] = vmName;
            importSettingData["TargetVmDataRoot"] = rootDirectoryToCopy;
            importSettingData["TargetSnapshotDataRoot"] = rootDirectoryToCopy;
            importSettingData["TargetVhdDataRoot"] = rootDirectoryToCopy;

            ManagementObject newSwitch = CreateSwitch(scope, Guid.NewGuid().ToString(), "Switch", 1024);
            importSettingData["TargetNetworkConnections"] = new string[] { (newSwitch.GetPropertyValue("Name").ToString()) };
            //Console.WriteLine("", ((string[])importSettingData["TargetResourcePaths"])[0]);
            return importSettingData;
        }

        public static void ImportVirtualSystemEx(string importDirectory, string vmName , string importCopyDirectory)
        {
            try
            {
                if (Directory.Exists(importCopyDirectory))
                {
                    Directory.Delete(importCopyDirectory, true);
                }
            }
            catch(Exception ex)
            {
                Console.WriteLine(ex.ToString());
            }
            Directory.CreateDirectory(importCopyDirectory);
            ManagementScope scope = new ManagementScope(@"root\virtualization", null);
            ManagementObject virtualSystemService = Utility.GetServiceObject(scope, "Msvm_VirtualSystemManagementService");

            ManagementBaseObject importSettingData = GetVirtualSystemImportSettingData(scope, importDirectory, vmName, importCopyDirectory);

            ManagementBaseObject inParams = virtualSystemService.GetMethodParameters("ImportVirtualSystemEx");
            inParams["ImportDirectory"] = importDirectory;
              inParams["ImportSettingData"] = importSettingData.GetText(TextFormat.CimDtd20);

            ManagementBaseObject outParams = virtualSystemService.InvokeMethod("ImportVirtualSystemEx", inParams, null);

            if ((UInt32)outParams["ReturnValue"] == ReturnCode.Started)
            {
                if (Utility.JobCompleted(outParams, scope))
                {
                    Console.WriteLine("VM were Imported successfully.");

                }
                else
                {
                    Console.WriteLine("Failed to Imported VM");
                }
            }
            else if ((UInt32)outParams["ReturnValue"] == ReturnCode.Completed)
            {
                Console.WriteLine("VM were Imported successfully.");
            }
            else
            {
                Console.WriteLine("Import virtual system failed with error:{0}", outParams["ReturnValue"]);
            }

            inParams.Dispose();
            outParams.Dispose();
            virtualSystemService.Dispose();
        }
        /*copy the content of a directory into another directory*/
        public static void CopyAll(DirectoryInfo source, DirectoryInfo target)
        {
            // Check if the target directory exists, if not, create it.
            if (Directory.Exists(target.FullName) == false)
            {
                Directory.CreateDirectory(target.FullName);
            }
            FileInfo fileInfo;
            // Copy each file into it’s new directory.
            foreach (FileInfo fi in source.GetFiles())
            {
                Console.WriteLine(@"Copying {0}\{1}", target.FullName, fi.Name);
                fileInfo=fi.CopyTo(Path.Combine(target.ToString(), fi.Name), true);
            }

            // Copy each subdirectory using recursion.
            foreach (DirectoryInfo diSourceSubDir in source.GetDirectories())
            {
                DirectoryInfo nextTargetSubDir =
                    target.CreateSubdirectory(diSourceSubDir.Name);
                CopyAll(diSourceSubDir, nextTargetSubDir);
            }
        }
        /********************* Modify virtual machine *********************/
       public static void RequestStateChange(string vmName, string action)
        {
            ManagementScope scope = new ManagementScope(@"root\virtualization", null);
            ManagementObject vm = Utility.GetTargetComputer(vmName, scope);

            ManagementBaseObject inParams = vm.GetMethodParameters("RequestStateChange");

            const int Enabled = 2;
            const int Disabled = 3;

            if (action.ToLower() == "start")
            {
                inParams["RequestedState"] = Enabled;
            }
            else if (action.ToLower() == "stop")
            {
                inParams["RequestedState"] = Disabled;
            }
            else
            {
                throw new Exception("Wrong action is specified");
            }

            ManagementBaseObject outParams = vm.InvokeMethod("RequestStateChange", inParams, null);

            if ((UInt32)outParams["ReturnValue"] == ReturnCode.Started)
            {
                if (Utility.JobCompleted(outParams, scope))
                {
                    Console.WriteLine("{0} state was changed successfully.", vmName);
                }
                else
                {
                    Console.WriteLine("Failed to change virtual system state");
                }
            }
            else if ((UInt32)outParams["ReturnValue"] == ReturnCode.Completed)
            {
                Console.WriteLine("{0} state was changed successfully.", vmName);
            }
            else
            {
                Console.WriteLine("Change virtual system state failed with error {0}", outParams["ReturnValue"]);
            }

        }

       public static void ModifyVirtualSystemProperties(string vmName,int memory, int procSpeed, int nrCores)
       {
           string virtualMachineName = vmName;
           string[] resourceSettingData = new string[2];

           ManagementPath path = new ManagementPath(@"root\virtualization");
           ManagementScope scope = new ManagementScope(path, new ConnectionOptions());
           scope.Connect();

           ManagementObject objMachine = Utility.GetTargetComputer(virtualMachineName, scope);

           // prepare settings datas.
           ManagementObject memorySetting = null;
           ManagementObject processorSetting = null;
           ManagementObject storageSetting = null;

           ManagementObjectCollection settingDatas = objMachine.GetRelated("Msvm_VirtualSystemsettingData");
           foreach (ManagementObject settingData in settingDatas)
           {
               foreach (ManagementObject memorySettingData in settingData.GetRelated("Msvm_MemorySettingData"))
               {
                   memorySetting = memorySettingData;
                   break;
               }
               foreach (ManagementObject proccessSettingData in settingData.GetRelated("Msvm_ProcessorSettingData"))
               {
                   processorSetting = proccessSettingData;
                   break;
               }
             /*  foreach (ManagementObject storageSettingData in settingData.GetRelated("Msvm_VirtualHardDiskInfo"))
               {
                   storageSetting = storageSettingData;
                   break;
               }*/
           }

           memorySetting["Limit"] = memory;
           memorySetting["Reservation"] = memory;
           memorySetting["VirtualQuantity"] = memory;
           resourceSettingData[0] = memorySetting.GetText(TextFormat.CimDtd20);

           processorSetting["Limit"] = procSpeed *1000;
           processorSetting["Reservation"] = procSpeed * 1000;
           processorSetting["VirtualQuantity"] = nrCores;
           resourceSettingData[1] = processorSetting.GetText(TextFormat.CimDtd20);

           processorSetting["Weight"] = 5000;

           /*
               storageSetting["FileSize"] = 2;
               storageSetting["MaxInternalSize"] = 2;
               resourceSettingData[2] = storageSetting.GetText(TextFormat.CimDtd20);
            */
           // get Msvm_VirtualSystemManagementService instance.
           ManagementObject virtualSystem = null;
           ManagementClass systemManagementServiceClass = new ManagementClass(
               scope,
               new ManagementPath("Msvm_VirtualSystemManagementService"),
               null);

           ManagementObjectCollection systemManagementServices = systemManagementServiceClass.GetInstances();
           foreach (ManagementObject service in systemManagementServices)
           {
               virtualSystem = service;
           }

           ManagementBaseObject inParams = virtualSystem.GetMethodParameters("ModifyVirtualSystemResources");
           inParams["ComputerSystem"] = objMachine.Path.Path;
           inParams["ResourcesettingData"] = resourceSettingData;
           ManagementBaseObject outParams = virtualSystem.InvokeMethod("ModifyVirtualSystemResources", inParams, null);
           if ((UInt32)outParams["ReturnValue"] == ReturnCode.Started)
           {
               if (Utility.JobCompleted(outParams, scope))
               {
                   Console.WriteLine("Virtual machine was modified successfully.");
               }
               else
               {
                   Console.WriteLine("Failed to modify virtual machine.");
               }
           }
           else if ((UInt32)outParams["ReturnValue"] == ReturnCode.Completed)
           {
               Console.WriteLine("Virtual machine was modified successfully.");
           }
           else
           {
               Console.WriteLine("Modify virtual machine failed with error : {0}", outParams["ReturnValue"]);
           }

           inParams.Dispose();
           outParams.Dispose();
           memorySetting.Dispose();
           processorSetting.Dispose();
           objMachine.Dispose();
       }

        /************************* Delete virtual machine *************************/
       public static void DestroyVirtualSystem(string vmName)
        {
            ManagementScope scope = new ManagementScope(@"root\virtualization", null);
            ManagementObject virtualSystemService = Utility.GetServiceObject(scope, "Msvm_VirtualSystemManagementService");

            ManagementBaseObject inParams = virtualSystemService.GetMethodParameters("DestroyVirtualSystem");

            ManagementObject vm = Utility.GetTargetComputer(vmName, scope);

            inParams["ComputerSystem"] = vm.Path.Path;

            ManagementBaseObject outParams = virtualSystemService.InvokeMethod("DestroyVirtualSystem", inParams, null);

            if ((UInt32)outParams["ReturnValue"] == ReturnCode.Started)
            {
                if (Utility.JobCompleted(outParams, scope))
                {
                    Console.WriteLine("VM '{0}' were deleted successfully.", vm["ElementName"]);

                }
                else
                {
                    Console.WriteLine("Failed to delete VM");
                }
            }
            else if ((UInt32)outParams["ReturnValue"] == ReturnCode.Completed)
            {
                Console.WriteLine("VM '{0}' were deleted successfully.", vm["ElementName"]);
            }
            else
            {
                Console.WriteLine("Destroy virtual system failed with error {0}", outParams["ReturnValue"]);
            }

            inParams.Dispose();
            outParams.Dispose();
            vm.Dispose();
            virtualSystemService.Dispose();
        }



    }
}
