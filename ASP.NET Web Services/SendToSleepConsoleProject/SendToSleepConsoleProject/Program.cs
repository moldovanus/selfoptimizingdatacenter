using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Threading;

namespace SendToSleepConsoleProject
{
    class Program
    {
        static void Main(string[] args)
        {
            Thread.Sleep(60000);
            Application.SetSuspendState(PowerState.Suspend, true, true);
        }
    }
}
