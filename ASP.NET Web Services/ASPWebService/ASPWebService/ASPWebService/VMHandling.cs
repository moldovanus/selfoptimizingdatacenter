﻿using System;
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