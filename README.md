# Task Management Application

This is a Task Management application built for Android using Kotlin, Firebase Realtime Database, and following the MVVM architecture. The application allows users to manage their tasks, including creating, updating, and deleting tasks, with visual indicators for task deadlines.

## Features

- Create, update, and delete tasks.
- Display task details in a popup fragment.
- Tasks are color-coded based on the deadline:
  - Green: More than 5 days remaining
  - Yellow: 3-5 days remaining
  - Red: Less than 3 days remaining
  - Blue: Task is completed
- User-friendly interface with support for task categories, priorities, and statuses.
- Integrates Firebase Realtime Database for persistent data storage.


## Technologies Used

- **Kotlin**: Programming language
- **Firebase Realtime Database**: For storing task data
- **MVVM Architecture**: For managing UI-related data in a lifecycle-conscious way
- **Android Studio**: IDE used for development

## Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/yourusername/task-management-app.git
