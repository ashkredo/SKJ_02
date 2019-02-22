# Network of Counters UDP (SKJ)

Utworzenie sieci agentów:

Żeby odpalić kilka agentów na jednym port-cie na jednym komputerze skorzystałem z MulticastSocket.

Rozpoczynamy program z utworzenia kilku agentów podając początkową wartość licznika dla tego agenta i kwant czasu (w sekundach), co który agent przeprowadza operację synchronizacji swojego licznika z licznikami pozostałych agentów w sieci. 

Odtworzy się interfejs, gdzie pierwsza wartość – to wartość licznika w milisekundach, a druga – to kwant czasu.

![alt text](https://github.com/s15444/NetworkOfCountersUDP-SKJ/blob/master/project-info/1.png)

Kontroler:

Jedyny problem testowanie tego programu polega na tym że działamy na localhost i jeżeli włączymy kilku agentów na localhost to nie możemy odczytać albo zmienić paramenty pojedynczego agenta. 

Ale to nie jest problemem zgodnie do wymagań. 

Możemy wysyłać polecenie za poleceniem w tym graficznym interfejsie o zobaczyć odpowiedz na ‘get’ w konsole naszego kompilatora.

![alt text](https://github.com/s15444/NetworkOfCountersUDP-SKJ/blob/master/project-info/2.png)

Licznik i synchronizacja:

Licznik i synchronizacja licznika działają w różnych wątkach. Co  1 milisekundę w jednym wątku licznik zwiększa się o 1. 
Synchronizacja - Po utworzeniu agenta, socket agenta łączy się do grupy i co kwant czasu odsyła polecenie SYN do wszystkich w grupie, przyjmuję wartości liczników i zmienia swój. 
