# NoteApp

## Architecture and Modules

The app is modularized and the architecture is inspired by Clean Architecture.

- `app`: wrapper that generates apk, does the DI wiring and combines each feature.
- `features`: parent directory for the features.
    - `note-list`: parent directory for the list of notes.
        - `ui`: contains the UI of the feature and exposes its routing config.
        - `use-case`: contains the Use Cases with the logic of the feature.
    - `note-detail`
        - `ui`
        - `use-case`
    - `ui-commons`: contains common ui elements and utilities related to the features.
    - `use-case-commons`: contains the base classes for the use cases.
- `domain`: domain entities for the whole app.
- `store`
    - `specification`: public abstractions of the local store datasource, used by the use cases
    - `entity`: contains the database entities, only available to `implementation`
    - `implementation`: the Room implementation of `specification`, only used in `app`.

### The `app` module

The entry point is the `:app` module, which generates the APK, configures the dependency graph and wraps all the features.
It has a single activity that hosts and routes all the composables exposed by each feature.

### The `features` modules

One level bellow `:app`, we have the features. The idea is to add modules here for each new feature of the app.

### The `:ui` module of the feature

The entry point of each feature are the `:ui` modules. They expose the screens (services, or other components) of the feature.

In teory, all classes of these modules could be private, we just need to provide the routing or the entry point to `:app`.

In this project, all classes from `:ui` are public, but only the `NavGraphConfiguration` is used from `:app`.

It would be recommended to instead have a module providing the entry point of the feature to `:app` and hide the rest of the implementation (or mark all non-public classes as internal).

### The `:use-case` module of the feature

The `:ui` depends on `use-case`. For this app, I decided to implement the use cases directly on each feature, but it would be possible to implement shared use cases for all the features.

The use cases depend on the data sources which for this are globally available (outside the features modules).

The data sources are abstractions that only know the domain objects, so use cases are totally independent of platforms, frameworks, libraries, or all other non-domain components.

Except for the use cases, which are concrete classes directly used from the ViewModes, all other classes have abstractions used to respect the dependency inversion principle.

This approach has some drawbacks, like making it difficult to mock the UseCases to test the view models, but I think it is simple and flexible.

### Architecture of the `features`

Each feature module can have its own dependencies and architecture, hiding its own implementation details.

For the current features (`note-list` and `note-detail`), I decided to use an MVVM with Unidirectional Data Flow, very similar to MVI.

The Views are implemented with Jetpack Compose, and they are re-build when the State is updated.

The ViewModel exposes only one State to the View. The view model of `note-list` exposes a Flow and the view model of `note-list` exposes an immutable state. But each feature could have the architecture that best fits it, like MVP or whatever.

The Model are the Use Cases.

### The `store` modules

Given the small complexity of this app, I decided to only use local data sources which I named `Store`s.

The `:store` module consists of the `specification`, the `entities`, and the `implementation`.

`:specification` acts as the public API of the store, they are abstractions that only know the domain objects (from the `:domain` module).

The `entities` and `implementation` are totally framework-dependent, so they are in the outer onion circle of our architecture diagram.

The current implementations use Room.

### The `domain` module

This module contains plain old dataclasses for the domain objects of this simple app.

## Build

The app was built with `Android Studio Android Studio Bumblebee | 2021.1.1 Patch 2`.

The generated debug apk should be ready to install and use.

## TODO and Fixes

- Improve `note-detail` to be more performant when saving a note.
- Logger is missing the line number
- Create a wiring module for DI, so not all modules are needed in `:android:app`
- Release configuration for apk singing, etc
- Implement tests for the view models.
- Implement tests for the ui.
- Implement espresso tests.
- After writing the first character, the note is shown as edited instead of created 
- Note screen has a problem with the scrolling and the enter transition animation
- Maybe create a base class for ViewModels.

### Deadline

We'll be waiting for your solution within 4 days. Do not create your own private/public repository. All the developments must be done in this repository.

### Goal ###

Develop a simple note app that allows the user to save/edit/delete any kind of note and display them in a list.

### Functional Requirements ###

* Kotlin is preferred but not a must.
* JetPack Compose Projects are not preferred
* Users must be able to create notes with input fields such as title, description, image url (input can be optional) and store it locally on their phones.
* Image url should be displayed as an Image in the listing screen.
* Created note must contain a created date.
* There must be a way to display all saved notes in the list. An item on the list must contain the created date (dd/mm/yyyy), the image if url is available, title and max. 2 lines of description.
* There must be a way to edit/delete previously created notes. But edited notes must contain an (edited) tag somewhere while being displayed on the list.
* All data should be persisted locally.

### UI Suggestions ###

It doesn't need to be super pretty, but it shouldn't be broken as well. The design is mostly up to you as long as creating, listing and editing/deleting features are available to use.

Nice to have:
* Animations/Transitions
* At least one custom view

### Expectations ###

Consider this as a showcase of your skills.
Approach it as if you are going to make a pull request on our main/master branch.

We are expecting at least:
* Clear, defined architecture.
* Apply the Material Design Guidelines as much as possible.
* Meaningful tests (You do not need to have 100% coverage, but we will be looking for tests).
* Good and lint verified syntax.
* We expect a clear history in the repo. We don't mind your choice of git strategy as long as it has a track of your progress.
* The code must compile.
* The code must be production ready. Unit tests are expected.

