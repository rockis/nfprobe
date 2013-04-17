#!/bin/bash
sudo /tmp/tcpreplay -i lo -4 9996:9995 -e 10.24.2.11:10.28.2.1 -k 00:23:ae:a2:5f:c5 -I 00:23:ae:a2:5f:c5 $1 
