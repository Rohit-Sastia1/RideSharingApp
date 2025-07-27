# RideSharingApp

*COMPANY*: CODTECH IT SOLUTIONS

*NAME*: ROHIT SASTIA

*INTERN ID*: CT04DH928

*DOMAIN*: ANDROID DEVELOPMENT

*DURATION*: 4 WEEKS

*MENTOR*: NEELA SANTHOSH

# Ride Sharing App

RideSharingApp is a prototype Android application that simulates a basic ride-sharing experience. It integrates **Google Maps SDK** to show the user's live location and allows them to request a ride, which is recorded in **Firebase Realtime Database**. The app demonstrates key features of real-time location tracking, ride request logging, and simple backend communication.

---

## 📱 Features

- Real-time user location tracking using Google Maps  
- Ride request functionality with one tap  
- Ride details stored in Firebase Realtime Database  
- Displays current location on map with camera zoom  
- Stores ride metadata including GPS coordinates, timestamp, and status  

---

## Architecture

- Activity: `MainActivity.java` manages map display and Firebase integration  
- Layout: XML includes a `SupportMapFragment` and a `Button` for ride request  
- Firebase: Used for real-time database updates to store and retrieve ride data  
- Location Services: Fused Location Provider is used for fetching user GPS data  


---

## 🧠 How it Works

1. The app asks for location permissions and loads a map centered on the user's current location.  
2. The user taps the "Request Ride" button to create a new ride request.  
3. The app captures the GPS coordinates and saves them to Firebase along with a status and timestamp.  
4. This structure can be extended to support driver assignment, tracking, and notifications.

---

## Minimum Requirements

- Android Studio Giraffe or newer  
- Minimum SDK 27 (Android 8.1 Oreo)  
- Google Maps API Key with Maps SDK enabled  
- Firebase project with Realtime Database configured  

---

## Future Enhancements

- Add driver app and simulate ride acceptance  
- Real-time driver tracking and route display  
- In-app messaging between rider and driver  
- Push notifications for ride status updates  

---


## Author

Developed as part of Task 01 – Android Expense Tracker Project for educational purposes.

---

## Output

![Image](https://github.com/user-attachments/assets/a3b0ab9a-b778-4cde-b04f-7d4606185c3e)


