# <img src="./img/logo.png" width="40"> WiFind (nwHacks 2018): Android Application

## By Luminescence

[![Build Status](https://travis-ci.org/nwHacks2018/Android-App.svg?branch=master)](https://travis-ci.org/nwHacks2018/Android-App)
[![License](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/nwHacks2018/Android-App/blob/master/LICENSE)

Android 8.0 (API level 26) application to automanage and autoconnect to crowdsourced public Wi-Fi networks.

## Contributing

The application mainly communicates with the Firebase database by asynchronously receiving a set of previously saved Wifi networks.

The base URL of the Firebase database is https://wifinder-294dd.firebaseio.com/.

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
