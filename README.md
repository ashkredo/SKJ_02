# Network of Counters UDP (SKJ)

Utworzenie sieci agentów:

Żeby odpalić kilka agentów na jednym port-cie na jednym komputerze skorzystałem z MulticastSocket.

Rozpoczynamy program z utworzenia kilku agentów podając początkową wartość licznika dla tego agenta i kwant czasu (w sekundach), co który agent przeprowadza operację synchronizacji swojego licznika z licznikami pozostałych agentów w sieci. 

Odtworzy się interfejs, gdzie pierwsza wartość – to wartość licznika w milisekundach, a druga – to kwant czasu.
![alt text](https://github.com/s15444/NetworkOfCountersUDP-SKJ/blob/master/project-info/1.png)
