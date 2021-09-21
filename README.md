# QuizApp
An android application which uses mock quiz API for getting questions.

## Prerequisites

#### 1. Check API

If the app cannot get questions, check the mock quiz API.

	https://run.mocky.io/v3/b534c346-b389-41d9-bf25-4123a08d171c
	https://run.mocky.io/v3/b1d8dfc4-3d4c-4ed4-aaf5-adf8c3fb2b65
	https://run.mocky.io/v3/e19ac854-d2aa-491e-9972-7acd62316a0a
	https://run.mocky.io/v3/b1a34bad-e2c2-429e-b4b1-bd6972d9e540
	https://run.mocky.io/v3/3c794462-2d17-497c-b752-c0b3f2703bce

#### 2. Ready to run.

## Features
- Quiz Features (Listing, Deleting, Time)
- Caching Questions (Offline Capability)
- Unit Tests

## Tech Stack
- **Kotlin** - Officially supported programming language for Android development by Google
- **Kotlin DSL** - Alternative syntax to the Groovy DSL
- **Coroutines** - Perform asynchronous operations
- **Flow** - Handle the stream of data asynchronously
- **Android Architecture Components**
  - **LiveData** - Notify views about data changes
  - **Room** - Persistence library
  - **ViewModel** - UI related data holder
  - **ViewBinding** - Allows to more easily write code that interacts with views
- **Hilt** - Dependency Injection framework
- **Retrofit** - Networking library
- **Moshi** - A modern JSON library for Kotlin and Java
- **Coil** - Image loading library
 
 ## Local Unit Tests
- The project uses MockWebServer (scriptable web server) to test Quiz API interactions.

## Screenshots
![quiz](https://user-images.githubusercontent.com/25778714/134246835-f5452be7-f14b-4212-97ca-6f798845edd5.png)

## Architecture
![arch500](https://user-images.githubusercontent.com/25778714/113482640-3801f100-94a8-11eb-98d6-e15cb21a905b.png)

