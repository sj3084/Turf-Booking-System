Problem Statement
Develop a mobile application for booking turf time slots. The application should allow users to sign up, log in using email or Google Sign-In, select a turf venue, pick a date and time slot, and book a time for usage. It should ensure that users are authenticated, prevent double booking of slots, and manage user sessions efficiently. The app should also display a waiting room feature, where users can view other users booked for the same slot, and a confirmation page summarizing booking details.
Solution
A Turf Booking System mobile app that includes the following main features and components:
User Authentication: The app uses Firebase Authentication to support email-based and Google-based sign-ins. It manages login and registration through a Firebase backend.
Turf Booking: Users select a venue, date, and time slot to book. Firebase Realtime Database is used to store booking details, and a function checks the availability of selected slots to prevent double bookings.
Waiting Room: Users can view a waiting room that shows the number of other users booked in the same slot. Once a sufficient number of users are in the waiting room, the slot is confirmed as booked, and the booking status is updated.
Confirmation: After successful booking, users see a confirmation screen with their booking details.
User Interface: XML files define the UI for login, sign-up, booking, waiting room, and confirmation pages.
Google Sign-IN Implementation
1. Set Up Firebase Project and Enable Authentication
Go to the Firebase Console.
Select your project, or create a new one if you haven’t done so.
In your Firebase project, go to Authentication in the sidebar.
Under the Sign-in method tab, enable Google as a sign-in provider.
Configure any necessary settings (you may be prompted to add support email).
2. Configure Android Project in Firebase
In the Firebase Console, click on Project Settings (gear icon next to Project Overview).
Scroll down to Your apps and select Add app if you haven’t already added your Android app.
Register your app by providing your Android package name.
Follow the on-screen instructions to download the google-services.json file.
Place the downloaded google-services.json file in the app/ directory of your Android project.
3. Add Firebase SDK to Project
Open your build.gradle files to add the Firebase SDK:
Project-level build.gradle file:
gradleCopy codebuildscript {
    dependencies {
        // Add this line
        classpath 'com.google.gms:google-services:4.3.15'
    }
}
App-level build.gradle file:
gradleCopy codedependencies {
    // Add Firebase Authentication and Google Sign-In SDKs
    implementation 'com.google.firebase:firebase-auth:22.1.1'
    implementation 'com.google.android.gms:play-services-auth:20.5.0'
}

// Add this line at the bottom of the file
apply plugin: 'com.google.gms.google-services'
Sync your project to download the required dependencies.
4. Set Up Google Sign-In Configuration
Back in the Firebase Console, go to Project Settings and scroll down to Your apps.
Here, you’ll find the Web client ID for Google Sign-In. Copy it.
In your Android project, open res/values/strings.xml and add the default_web_client_id:

Replace YOUR_WEB_CLIENT_ID_HERE with the actual Web client ID from the Firebase Console.
Web Client ID taken from Google Cloud>APIs & Services>Credentials>OAuth 2.0 Client IDs

5. Enable Google Sign-In API in Google Cloud Console
Go to the Google Cloud Console.
Ensure you’re in the same project that’s linked to your Firebase project.
Navigate to APIs & Services > Library.
Search for and enable the Google Identity Toolkit API and Google Sign-In API.

Add SHA-1 Certificate Fingerprint

6. Update AndroidManifest.xml
Add the Internet permission to your AndroidManifest.xml:
Ensure your app’s AndroidManifest.xml file has the necessary configuration to use Firebase.

7. Run the App and Test Google Sign-In
Rebuild and run your app on a physical device or emulator.
Click the Sign in with Google button in your app.
You should see the Google Sign-In dialog, allowing you to select an account and complete the sign-in process.
