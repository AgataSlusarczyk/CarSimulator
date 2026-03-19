# Car Simulator

A vehicle behavior simulation application built with **JavaFX**. The project allows for interactive management of multiple vehicles and their key components, such as the engine, gearbox, and clutch.

## Features

* **Fleet management:** Add and remove multiple cars within a single session.
* **Engine control:** Start/stop the engine and smoothly regulate RPM.
* **Manual gearbox:** Shift gears (up/down) taking vehicle mechanics into account.
* **Clutch handling:** Realistic control of the clutch state (pressed/released).
* **Movement simulation:** Real-time visualization of the car's movement on a map.
* **Detailed data:** View technical parameters of each component (weight, price, name, operational parameters).

##  Project Structure

The project has been divided into packages following clean architecture principles:

```text
car/
├── app/          # JavaFX controllers and main application class
├── model/        # Business logic (Car, Engine, GearBox, Clutch)
├── exception/    # Custom exceptions (e.g., gear shifting errors)
├── listener/     # Listener interfaces (Observer pattern)
└── test/         # JUnit unit tests
