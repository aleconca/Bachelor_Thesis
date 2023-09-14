**Master of Renaissance Digital Twin**

This is the "Master of Renaissance" Java application. This software brings the classic board game "Master of Renaissance" to the digital realm.


>Introduction_

"Master of Renaissance" is a popular board game known for its engaging gameplay, where players compete to become the most influential and prosperous citizens of the Renaissance era. In this digital twin application, we aim to recreate the essence of the board game.


>Group components:

Iacopo Roberto Ferrario , Alessia Conca Roncari , Anastasia Favero


>Personal note on how to start the jars:

Il jar del server verrà eseguito da cmd su sistema operativo Windows 10

Il jar del client (CLI) verrà eseguito su WSL Ubuntu

Funzionalità implementate: regole complete + CLI



-----from: \OneDrive\Desktop\ing-sw-2021-Ferrario-Favero-Conca\deliverables\final\jar>


CLIENT on Windows 10:
java -jar server-PSP21-1.0-SNAPSHOT.jar
LAPTOP-N8T657MS/192.168.56.1

SERVER on Ubuntu:
java -jar client-PSP21-1.0-SNAPSHOT.jar
Welcome to MASTER OF RENAISSANCE Game!
To create a game or connect with your friends, please fill the following fields ^_^
Type username: ape
Type ip server: 192.168.56.1
Type port number: 


---from windows cmd: C:\Users\User> netstat -ano
  TCP    192.168.56.1:139 //this didn't work
  UDP    192.168.56.1:5353      *:*                                 
  UDP    192.168.56.1:5353      *:*                                  
  UDP    192.168.56.1:51791     *:*                                  
  UDP    [::]:5353
