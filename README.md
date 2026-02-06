<img src="/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png">

# Type Matchups

An application that will give the the effectiveness of Pokémon move types attacking the specified type of Pokémon.

## Overview
On the main screen, the user can select the type or types of the target Pokémon. The app will then display the effectiveness of each type of move against that Pokémon.

## Architecture
The app is written in Kotlin and uses Jetpack Compose for creating views. It follows the MVVM pattern for separation of concerns.
Different functionality is split into separate modules.
- app: View and view models
- core: Houses all functionality outside of Android-dependent code
- core:data: Includes repository for fetching data and model classes to represent that data
- core:localstorage: Includes code for reading the history workout data from the given txt file
- core:network: Includes code for fetching the type matchup data from the [GraphQL Pokémon API](https://graphqlPokémon.favware.tech/v8)

## Running the app
The app should run as-is after cloning the repository.
