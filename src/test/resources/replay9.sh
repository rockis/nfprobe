#!/bin/bash
sudo ~/temp/tcpreplay -i wlan0 -4 9999:9995 -e 192.168.1.111:192.168.1.14 -k 00:13:e8:92:22:05 -I  00:13:e8:92:22:05 v9all.pcap -p 1500 -l 1000000

