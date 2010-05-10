using System;
using System.Collections.Generic;

namespace ServerManagement{
    public class ServerInfo
    {
        public int CoreCount{get;set;}
        public int TotalCPU;
        public List<int> FreeCPU { get; set; }
        public List<Storage> Storage { get; set; }
        public int TotalMemory { get; set; }
        public int FreeMemory { get; set; }

    }

    public class Storage
    {
        public String Name { get; set; }
        public int Size{get;set;}
        public int FreeSpace { get; set; }

        public Storage(String name, int size, int freeSpace){
            Name = name;
            Size = size;
            FreeSpace = freeSpace;
        }
        public Storage()
        {
        }

    }
}