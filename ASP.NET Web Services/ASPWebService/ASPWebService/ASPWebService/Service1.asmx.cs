﻿using System;
using System.Collections;
using System.ComponentModel;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Web.Services.Protocols;
using System.Xml.Linq;
using ServerManagement;
using System.Net;
using System.Net.Sockets;
using System.Windows.Forms;
using System.Diagnostics;
using Microsoft.Win32;

namespace ASPWebService
{
    /// <summary>
    /// Summary description for Service1
    /// </summary>
    [WebService(Namespace = "http://www.SelfOptimizingDatacenter.edu/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]
    public class Service1 : System.Web.Services.WebService
    {
        private static bool operationCompleted = false;

        [WebMethod]
        public ServerInfo GetServerInfo()
        {
            return ServerMonitor.CollectServerInfo();
        }
        [WebMethod]
        public void MoveDestinationActions(String path1, String path2, String vmName)
        {
            operationCompleted = false;
            VMHandling.ImportVirtualSystemEx(path1 + "/" + vmName, vmName, path2);
            VMHandling.RequestStateChange(vmName, "start");
            operationCompleted = true;
        }
        [WebMethod]
        public void MoveSourceActions(String path, String vmName)
        {
            operationCompleted = false;
            VMHandling.RequestStateChange(vmName, "stop");
            VMHandling.ExportVirtualSystemExSnapshots(vmName, path); // export existing snapshot of a virtual machine
            VMHandling.DestroyVirtualSystem(vmName);
            operationCompleted = true;
        }
        [WebMethod]
        public void DeployVirtualMachine(String from, String to, String vmName)
        {
            //just to make sure it is running
            Process.Start("net start hvboot");
            operationCompleted = false;
            VMHandling.ImportVirtualSystemEx(from + "\\" + vmName, vmName, to);
            operationCompleted = true;
        }
        [WebMethod]
        public void StartVirtualMachine(String vmName)
        {
            //just to make sure it is running
            Process.Start("net start hvboot");
            operationCompleted = false;
            VMHandling.RequestStateChange(vmName, "start");
            operationCompleted = true;
        }
        [WebMethod]
        public void StopVirtualMachine(String vmName)
        {
            operationCompleted = false;
            VMHandling.RequestStateChange(vmName, "stop");
            operationCompleted = true;
        }
        [WebMethod]
        public void DeleteVirtualMachine(String vmName)
        {
            VMHandling.DestroyVirtualSystem(vmName);
        }

        //must be "02 17 31 65 C3 5F"
        [WebMethod]
        public void WakeUpServer(String mac, String ipAddress, int port)
        {
            operationCompleted = false;
            string[] values = mac.Split('-');

            UdpClient client = new UdpClient();
            client.Connect(IPAddress.Parse(ipAddress), port);

            //
            // WOL packet contains a 6-bytes trailer and 16 times a 6-bytes sequence containing the MAC address.
            //
            byte[] packet = new byte[17 * 6];

            //
            // Trailer of 6 times 0xFF.
            //
            for (int i = 0; i < 6; i++)
                packet[i] = 0xFF;

            //
            // Body of magic packet contains 16 times the MAC address.
            //
            for (int i = 1; i <= 16; i++)
                for (int j = 0; j < 6; j++)
                    packet[i * 6 + j] = (byte)Convert.ToInt32(values[j], 16);

            //
            // Submit WOL packet.
            //
            client.Send(
                packet, packet.Length);
        }
        [WebMethod]
        public void SendServerToSleep()
        {
            RegistryKey key = Registry.LocalMachine.CreateSubKey("SYSTEM\\CurrentControlSet\\Services\\hvboot");
            key.SetValue("Start", 3);

            RegistryKey runOnceKey = Registry.LocalMachine.CreateSubKey("SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\RunOnce");
            runOnceKey.SetValue("SendToSleepAutomatically", "C:\\SendToSleep.exe");

            Process.Start("shutdown.exe", " -r -f -t 0");
            //System.Windows.Forms.Application.SetSuspendState(PowerState.Suspend, false, false);
        }

        [WebMethod]
        public bool IsOperationCompleted()
        {
            return operationCompleted;
        }
    }
}