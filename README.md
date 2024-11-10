#Problem Statement
Develop a mobile application for booking turf time slots. The application should allow users to sign up, log in using email or Google Sign-In, select a turf venue, pick a date and time slot, and book a time for usage. It should ensure that users are authenticated, prevent double booking of slots, and manage user sessions efficiently. The app should also display a waiting room feature, where users can view other users booked for the same slot, and a confirmation page summarizing booking details.
#Solution
A Turf Booking System mobile app that includes the following main features and components:
User Authentication: The app uses Firebase Authentication to support email-based and Google-based sign-ins. It manages login and registration through a Firebase backend.
Turf Booking: Users select a venue, date, and time slot to book. Firebase Realtime Database is used to store booking details, and a function checks the availability of selected slots to prevent double bookings.
Waiting Room: Users can view a waiting room that shows the number of other users booked in the same slot. Once a sufficient number of users are in the waiting room, the slot is confirmed as booked, and the booking status is updated.
Confirmation: After successful booking, users see a confirmation screen with their booking details.
User Interface: XML files define the UI for login, sign-up, booking, waiting room, and confirmation pages.
gn-In
Rebuild and run your app on a physical device or emulator.
Click the Sign in with Google button in your app.
You should see the Google Sign-In dialog, allowing you to select an account and complete the sign-in process.
