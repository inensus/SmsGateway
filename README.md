## About
Gateway Android Application is a service provider which handles incoming SMS and forwards them to a pre-defined server (e.g. http://your-domain.com/), or receives a Push Notification from a server via Firebase, parses phone number and message from the notification and sends an SMS with the message to the related phone number.

## Download
To configure the app, download the latest version from master branch and open with Android Studio.

## Configuration
- The Application has Firebase Cloud Messaging integration. To establish your own environment; you need to create a Firebase Project, setup Cloud Messaging and put google-services.json into the source code.
- To specify the server url, `DEFAULT_BASE_URL` in `SharedPreferencesWrapper.kt` should be configured. Server url can can also be specified by tapping on `Settings-menu` on main page and restarting the application.
- The App can either be used in Debug or in Release version. 

## Project Info
- minSdkVersion: 21
- compileSdkVersion: 29
- Programming Language: Kotlin
- Permissions: INTERNET, SEND_SMS, RECEIVE_SMS, READ_SMS, READ_PHONE_STATE
