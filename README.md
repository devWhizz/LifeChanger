# LifeChanger | Donation App

<br>

### This app allows users to discover, support and donate to various causes and charitable organizations as well as creating their own fundraisers.

<br>
 
![](https://mediadesign.solutions/wp-content/uploads/2023/11/github-home.png)
![](https://mediadesign.solutions/wp-content/uploads/2023/11/github-category.png)
![](https://mediadesign.solutions/wp-content/uploads/2023/11/github-detail.png)
![](https://mediadesign.solutions/wp-content/uploads/2023/11/github-search.png)

<br>

### Table of Contents
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Key Files](#key-files)
- [Key Features](#key-features)
- [Contributing](#contributing)

<br>

### Getting Started

To run the LifeChanger app on your local environment, follow these steps:

1. Clone the repository from [GitHub](https://github.com/devWhizz/LifeChanger).
2. Open the project in Android Studio.
3. Build and run the app on an emulator or physical device.

<br>

---

### Project Structure


The project follows a standard Android app structure using the MVVM architecture. Here's an overview of the project structure:

<br>

![Static Badge](https://img.shields.io/badge/Package-app-orange)

This module contains the main app code and resources.

<br>

![Static Badge](https://img.shields.io/badge/Package-data-orange)

This package includes data repositories and models.

<br>

![Static Badge](https://img.shields.io/badge/Package-db-orange)

Contains Room database related classes and the DAO.

<br>

![Static Badge](https://img.shields.io/badge/Package-ui-orange)

Includes fragments for the app's user interface.

<br>

![Static Badge](https://img.shields.io/badge/Package-adapter-orange)

Includes adapters for recyclerviews used in Fragments

<br>

![Static Badge](https://img.shields.io/badge/Package-api-orange)

Contains classes for making API requests.

<br>

![Static Badge](https://img.shields.io/badge/Package-res-orange)

Resources including layout files, strings, and drawables.

<br>

---

### Key Files

Here are some of the projects key files:

<br>

![Static Badge](https://img.shields.io/badge/Key_File-SharedViewModel.kt-orange)

The ViewModel manages data retrieval and manipulation for the app. It interacts with the API, the Room database, Google Firestore and Google Cloud Storage and provides data to the UI. It handles operations such as loading donations, managing liked donations, and retrieving data for various fragments.

<br>

![Static Badge](https://img.shields.io/badge/Key_File-CategoryAdapter.kt-orange)

An adapter class for the RecyclerView in the Category- and FavoritesFragment. It binds data to the ViewHolder and handles item clicks. The adapter is responsible for displaying a list of donations with the ability to like or dislike them.

<br>

![Static Badge](https://img.shields.io/badge/Key_File-QuotesDatabase.kt-orange)

This class defines the Room database and provides access to the DAO (Data Access Object). It allows the app to store and retrieve quotes, providing a local source of inspiration for users.

<br>

![Static Badge](https://img.shields.io/badge/Key_File-MainActivity.kt-orange)

The main activity of the app. It hosts the navigation components and handles the toolbar and bottom navigation bar. The activity is responsible for navigating between different fragments and managing the app's overall user interface.

<br>

![Static Badge](https://img.shields.io/badge/Key_File-AddDonationFragment.kt-orange)

This fragment is used for adding new donations to the app. Users can input details such as the donation title, description, category and image. After inputting the required information, users can submit the donation details to add it to the app's database.

<br>

![Static Badge](https://img.shields.io/badge/Key_File-FavoritesFragment.kt-orange)

This fragment displays a list of liked donations. Users can like or dislike donations and the fragment updates the list of liked donations based on user interactions. It also provides a click action to navigate to the DonationDetailFragment for further information about a liked donation.

<br>

![Static Badge](https://img.shields.io/badge/Key_File-SearchFragment.kt-orange)

This fragment allows the user to browse all stored donations by typing keywords into the search field.

<br>

![Static Badge](https://img.shields.io/badge/Key_File-DonationDetailFragment.kt-orange)

This fragment displays detailed information about a specific donation. It is used to view donation details such as the title, creator, image and description. Users can also like the donation from this fragment or proceed to the PaymentFragment to make a donation.

<br>

![Static Badge](https://img.shields.io/badge/Key_File-PaymentFragment.kt-orange)

This fragment guides users through the payment process for making a donation. Users can choose the donation amount and complete the donation transaction. The PaymentFragment is responsible for handling the donation process and confirming the payment.

<br>

![Static Badge](https://img.shields.io/badge/Key_File-SettingsFragment.kt-orange)

This fragment provides users with app settings and preferences. It includes options to toggle Dark Mode, change the app language (English or German), and access the "About" section.

<br>

---

### Key Features

<br>

![Static Badge](https://img.shields.io/badge/Key_Feature-Adding_a_Donation-orange)

Users can add a new donation to the app by following these steps:

1. Open the app and navigate to the "Add Donation" section by clicking the FAB button (+).
2. Fill in the required information, such as the category, donation title, description, creator, image and PayPal address.
3. Submit the donation details to add it to the app's database (Google Firestore).

<br>

![Static Badge](https://img.shields.io/badge/Key_Feature-Like_Function-orange)

The like feature allows users to express their support for specific donations. Here's how it works:

1. Browse through the list of donations.
2. When you find a donation that you'd like to support, click the heart-shaped "Like" button associated with that donation.
3. The donation will be added to your list of liked donations for easy access and reference.
4. You can always get to them by navigating to the FavoritesFragment from the bottom navigation bar (heart icon)

<br>

![Static Badge](https://img.shields.io/badge/Key_Feature-Search-orange)

The app includes a search feature that allows users to find specific donations by using keywords. Here's how you can use the search function:

1. Open the Search Fragment by navigating to it from the bottom navigation bar (magnifier icon).
2. Enter a search query into the input field. Enter keywords related to the donations you're looking for.
3. As you type in your search query, the app dynamically filters the donations based on your input. The recyclerview displays the search results in real-time, updating as you type.
4. You can click on a donation from the search results to view more details about it.

<br>

![Static Badge](https://img.shields.io/badge/Key_Feature-Payment_via_PayPal-orange)

Users can make a donation to support a cause they care about:

1. Click on a donation from the list to view its details.
2. Click the "Donate Now" FAB button (credit card icon).
3. You will be guided through a payment process to complete your donation using PayPal as payment method.

<br>

---

### Contributing

Please report any issues or bugs through the [Issue Tracker](https://github.com/devWhizz/LifeChanger/issues).

<br>
<br>