**Master of Renaissance Digital Twin**

This is the "Master of Renaissance" Java application. This project implements, tests, and documents a Java application inspired by the popular board game "Master of Renaissance." The primary objectives of this project were to evaluate students' proficiency in applying Object-Oriented paradigms, specifically the Model-View-Controller (MVC) design pattern, and their understanding of Socket/RMI architecture.


>About "Master of Renaissance" Board Game:

"Master of Renaissance" is a captivating board game known for its strategic gameplay. In the game, players compete to become influential and prosperous citizens during the Renaissance era. They make decisions related to resource management, investments, and technological advancements to outsmart their opponents.

>Project Objectives:

The key objectives of this project were as follows:

-Implement the Game Logic: Create the core game logic that simulates the "Master of Renaissance" board game, allowing players to interact with the digital version.

-Apply MVC Design Pattern: Demonstrate a strong understanding of the Model-View-Controller (MVC) architectural pattern by organizing the codebase accordingly.

-Utilize Socket/RMI Architecture: Implement networking functionality using either Sockets or Remote Method Invocation (RMI) to enable multiplayer gameplay.

-Testing and Documentation: Ensure that the application is thoroughly tested for correctness and reliability. Create comprehensive documentation to aid users and future developers.

>Project Structure:

The project is structured as follows:

client/: Contains the Java source code for the application client.
server/: Contains the Java source code for the application server.
exceptions/: contains Java source to handle special cases that could arise during the game.
network/:Contains the Java code to handle the game connection.
derivables/: Includes documentation, uml and jars of the clint and the server.
tests/: Houses unit tests and test cases to validate the functionality of the application.



>Group components:

Alessia Conca Roncari, Anastasia Favero, Iacopo Roberto Ferrario.


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
