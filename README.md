# <img src="./img/logo.png" width="40"> WiFind (nwHacks 2018): Android Application

## By Luminescence

[![Build Status](https://travis-ci.org/nwHacks2018/Android-App.svg?branch=master)](https://travis-ci.org/nwHacks2018/Android-App)
[![License](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/nwHacks2018/Android-App/blob/master/LICENSE)

Android 8.0 (API level 26) application to automanage and autoconnect to crowdsourced public Wi-Fi networks.

## Setup Instructions

### Codebase

Clone the repository and navigate into the directory:
```shell
git clone https://URL.git
cd REPO/
```

### Android

Install [Android Studio](https://developer.android.com/studio/) and import the project.

#### Google Maps API Key

Generate a [Google Maps SDK API key (step 1)](https://developers.google.com/maps/documentation/android-sdk/get-api-key).

Duplicate the Google Maps API key template file to hold the API key:
```shell
cp Rebuild/app/src/debug/res/values/google_maps_api_template.xml Rebuild/app/src/debug/res/values/google_maps_api.xml

cp Rebuild/app/src/release/res/values/google_maps_api_template.xml Rebuild/app/src/release/res/values/google_maps_api.xml
```

Choose the `debug/` folder for development, or the `release/` folder for release.

In the new file `google_maps_api.xml`, uncomment the line with the key and replace the value of the placeholder with your API key. This new file will not be tracked by Git.

Build and run the Android application on a physical or virtual device.

#### Creating a Production Release

##### Step 1: Building a Release Variant

Create a new file in the root directory to hold the secure keystore details:
```shell
cp keystore.properties.template keystore.properties
```

This file will be ignored by Git. Set your keystore information in this file.

Under _Build_, click _Select Build Variant_.

In the _Build Variants_ view which appears, change the **Active Build Variant** from _debug_ to _release_.

Build the application normally to your phone. The keystore credentials will be verified and applied.

##### Step 2: Creating the Signed App

Under _Build_, click _Generate Signed Bundle / APK_.

Follow the instructions, inputting your keystore path and credentials, to create a signed app which can be uploaded to the app store.


## Contributing

The application mainly communicates with the Firebase database by asynchronously receiving a set of previously saved Wifi networks.

The base URL of the Firebase database is https://wifinder-294dd.firebaseio.com/.

The application is structured internally as in the following diagram:

[![App architecture diagram](readme-img/app-architecture.png)](https://drive.google.com/file/d/1PlWda-9x2nInU085GL3i_-j5ovSJSfQ5)

The API to retrieve all data at root-level is as follows:

### Request

```
GET https://wifinder-294dd.firebaseio.com/.json
```

### Response
```
HTTP 200: OK

{
  "networks": [
    network-name: {
      "ssid": string,
      "password": string,
      "coordinate": {
        "latitude": number,
        "longitude": number
      }
    },
    ...
  ]
}
```
