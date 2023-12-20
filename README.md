# CIS-18_NetworkScan This branch is now Depricated, forking to three new projects
1. A fork continuing the java dev path - mainy a reference point 
2. a fork porting the code to python - main dev branch, forking to python because java sourced some bad beans.
3. a fork porting the code to rust - this one will be experiental and more of a way to learn rust, not a ful fork of the application, at least for a while.

Development is of this application framework for our College of The Redwoods CIS-18 class is complete(ish) great job team!

Program Overview
Model-View-Controller (MVC) Pattern:

  NetworkScannerModel: Acts as the data model, holding the application's state, such as IP addresses and subnet masks.
  NetworkScannerView: Manages the graphical user interface, displaying data to the user and capturing user input.
  NetworkScannerController: Serves as the central controller, coordinating actions between the model and view, handling user inputs, and invoking scanning processes.
Scanner Components:

  portScanner: Responsible for scanning network ports.
  PingDecorator: Enhances portScanner with ping functionality, demonstrating the Decorator Pattern.
  hostDisco: Handles network host discovery, sending TCP packets and processing responses.
Supporting Classes:

  setNet: Manages network settings and configurations.
  hostDisco: Discovers active hosts on the network.
  hostImpl and hostFactory: Manage host objects, with hostFactory creating instances of hostImpl.
  Program Flow
Initialization:

  The Main class initializes the MVC components (model, view, controller).
  The controller is provided with instances of portScanner, PingDecorator, and other necessary components.
User Interaction:

  The user interacts with the NetworkScannerView (GUI), selecting scan types and entering network data.
  Actions in the view trigger event handlers in the NetworkScannerController.
Scanning Process:

  Based on user input, the controller invokes the appropriate scanning method (e.g., port scan, host discovery).
  Scanning classes (portScanner, PingDecorator, hostDisco) perform network scanning tasks.
  Results are sent back to the view for display to the user.
Model-View-Controller (MVC) Pattern
How it Works:
  NetworkScannerModel (Model): This class holds the application's data, such as IP addresses and subnet masks. It's responsible for managing the state and logic of the application data.
  NetworkScannerView (View): Manages the UI, displaying information to the user, and capturing user inputs. It's decoupled from the model and controller, focusing solely on the presentation layer.
  NetworkScannerController (Controller): Acts as an intermediary between the model and the view. It responds to user inputs captured by the view, manipulates data in the model, and updates the view accordingly.
Design Pattern Utilization:
  The MVC pattern separates concerns, making the application easier to manage and extend. Changes in the presentation layer do not affect data handling, and vice versa. This separation enhances testability and maintenance.
  Strategy Pattern
  Usage in setSubnet and getip:
  These classes provide different strategies for obtaining network settings (IP addresses and subnet masks). This allows for flexible substitution of various methods for retrieving these settings.
  Design Pattern Utilization:
  By encapsulating the subnet and IP address retrieval strategies, these classes make the components interchangeable. This flexibility is beneficial in scenarios where the method of obtaining network settings might vary.
Decorator Pattern
  Usage in PingDecorator:
  Enhances a portScanner instance with additional functionality (ping capability) without altering its core structure.
  Design Pattern Utilization:
  The Decorator pattern provides a flexible alternative to subclassing for extending functionality. It allows adding responsibilities to objects dynamically.
Factory Pattern
  Usage in hostFactory:
  Centralizes the creation of hostImpl objects, managing their instantiation and ensuring the appropriate setup.
  Design Pattern Utilization:
  The Factory pattern encapsulates object creation, leading to a more modular codebase where object creation logic is localized and can be easily modified without affecting the rest of the system.
