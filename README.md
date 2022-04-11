# NoteApp

## Architecture and Modules

The app is modularized and the architecture is inspired by Clean Architecture.

The entry point is the `:app` module, which generates the APK and wraps all the features.
It is a single activity app that hosts and routes all the composables exposed by the features.

Bellow the app, we have the features modules. The idea is to add modules here for each new feature of the app.

These module can have their own dependencies and architecture, hiding their own implementation details.

The entry point of each feature are the `:ui` modules, They expose the screens (if applicable) of the feature.

The UI depends on use cases. For this app, I decided to implement the use cases directly on each feature.

And finally, the use cases depend on the data sources which are globally available (outside the features modules).

Given the small complexity of this app, I decide to only use local datasource which I named `Store`s.``

Except from the use cases, which are concrete classes directly used from the ViewModes, all other classes have abstractions used to respect the dependency inversion principle.
This approach has some draw backs, like making it difficult to mock the UseCases to test the view models, but I think it is simply and flexible.

### Modules
- `app`
- `features`
    - `todo-list`
        - `ui`
        - `use-case`
    - `todo-detail`
        - `ui`
        - `use-case`
    - `ui-commons`
    - `use-case-commons`
- `domain`
- `store`
    - `entity`
    - `specification`
    - `implementation`

## Build

The app was built with `Android Studio Android Studio Bumblebee | 2021.1.1 Patch 2`.
The generated debug apk should be ready to install and use.

## TODO and Fixes

- Logger is missing the line number
- Create a wiring module for DI, so not all modules are needed in `:android:app`
- Release configuration for apk singing, etc
- Implement more testing, increase coverage
- Create a base class for ViewModels

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
