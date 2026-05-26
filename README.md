# Lost and Found Map Mobile App

This is an Android Lost and Found mobile application developed in Android Studio using Java. The app allows users to create lost or found adverts, save them locally, upload images, get current location, display items on Google Maps, and search nearby items using radius-based filtering.

## Project Overview

The purpose of this app is to help users report lost or found items and make it easier to locate them using map-based features. Users can create an advert by entering item details, selecting a category, uploading an image, and adding the current GPS location. The saved adverts are stored locally using SQLite database.

This project extends the basic Lost and Found app by adding Android geo features such as current location, Google Maps markers, and radius-based searching.

## Features

- Create lost or found adverts
- Enter item details such as name, phone, description, location, and category
- Upload an image for each advert
- Save advert data using SQLite database
- Store date and time stamp for each post
- View all saved lost/found adverts
- View full advert details
- Delete adverts after the item is found
- Get current GPS location
- Store latitude and longitude with each advert
- Display saved adverts as markers on Google Maps
- Search adverts within a selected radius in kilometres

## Technologies Used

- Java
- Android Studio
- XML Layouts
- SQLite Database
- Google Maps API
- Android Location Services
- FusedLocationProviderClient
- Android Emulator
- ADB commands for emulator GPS testing

## Main Screens

### Home Screen

The home screen provides three main options:

- Create New Advert
- Show Lost & Found Items
- Show On Map

### Create Advert Screen

This screen allows the user to create a new lost or found advert. The user can enter item details, select a category, upload an image, and use the Get Current Location button to retrieve GPS coordinates.

### Lost and Found List Screen

This screen displays all saved adverts from the SQLite database. Each advert shows basic information such as image, type, category, description, and timestamp.

### Advert Detail Screen

This screen shows full information about a selected advert. The user can also remove the advert from the database.

### Map Screen

This screen displays saved adverts on Google Maps using markers. Each marker represents one lost or found item based on its stored latitude and longitude.

## Location and Map Functionality

The app uses Android location services to retrieve the current device location. When the user clicks the Get Current Location button, the app asks for location permission and then gets latitude and longitude coordinates using FusedLocationProviderClient.

These coordinates are stored in the SQLite database with the advert details. Later, the map screen reads the saved coordinates and places markers on Google Maps.

## Radius-Based Search

The radius search feature allows users to search for lost or found items within a selected distance. The user enters a radius value in kilometres, and the app compares the user’s current location with each advert location.

Only adverts within the selected radius are displayed on the map. This helps users find nearby lost or found items more easily.

## SQLite Database

SQLite is used for local data storage. The database stores:

- Advert ID
- Lost or found type
- User name
- Phone number
- Item description
- Location text
- Category
- Image URI
- Timestamp
- Latitude
- Longitude

This allows the app to keep data even after it is closed and reopened.

## Emulator GPS Testing

The app was tested using Android Emulator. Since the emulator does not have real GPS movement like a physical phone, GPS locations were simulated using ADB commands.

Example command:

```bash
adb emu geo fix 145.1246 -37.9150
