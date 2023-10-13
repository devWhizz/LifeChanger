## LifeChanger | Donation App

This app allows users to discover, support, and donate to various causes and charitable organizations as well as creating their own donations.

### Table of Contents
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Key Files](#key-files)
- [Adding a Donation](#adding-a-donation)
- [Like Function](#like-function)
- [Payment](#payment)
- [Contributing](#contributing)


### Getting Started

To run the LifeChanger app on your local environment, follow these steps:

1. Clone the repository from [GitHub](https://github.com/devWhizz/LifeChanger).
2. Open the project in Android Studio.
3. Build and run the app on an emulator or physical device.


### Project Structure

The project follows a standard Android app structure using the MVVM method. Here's an overview of the project structure:

- **app**: This module contains the main app code and resources.
- **data**: This package includes data repositories and models.
- **db**: Contains Room database related classes and the DAO.
- **ui**: Includes fragments for the app's user interface.
- **adapter**: Includes adapters for recyclerviews used in Fragments
- **api**: Contains classes for making API requests.
- **res**: Resources including layout files, strings, and drawables.


### Key Files

Here are some of the key files of the project:

1. **SharedViewModel.kt**: This ViewModel manages data retrieval and manipulation for the app. It interacts with the API, the Room database, Google Firestore and Google Cloud Storage and provides data to the UI. It handles operations such as loading donations, managing liked donations, and retrieving data for various fragments.
2. **CategoryAdapter.kt**: An adapter class for the RecyclerView in the Category- and FavoritesFragment. It binds data to the ViewHolder and handles item clicks. The adapter is responsible for displaying a list of donations with the ability to like or dislike them.
3. **QuotesDatabase.kt**: This class defines the Room database and provides access to the DAO (Data Access Object). It allows the app to store and retrieve quotes, providing a local source of inspiration for users.
4. **MainActivity.kt**: The main activity of the app. It hosts the navigation components and handles the toolbar and bottom navigation bar. The activity is responsible for navigating between different fragments and managing the app's overall user interface.
5. **AddDonationFragment.kt**: This fragment is used for adding new donations to the app. Users can input details such as the donation title, description, category and image. After inputting the required information, users can submit the donation details to add it to the app's database.
6. **FavoritesFragment.kt**: This fragment displays a list of liked donations. Users can like or dislike donations and the fragment updates the list of liked donations based on user interactions. It also provides a click action to navigate to the DonationDetailFragment for further information about a liked donation.
7. **DonationDetailFragment.kt**: This fragment displays detailed information about a specific donation. It is used to view donation details such as the title, creator, image and description. Users can also like the donation from this fragment or proceed to the PaymentFragment to make a donation.
8. **PaymentFragment.kt**: This fragment guides users through the payment process for making a donation. Users can choose the donation amount and complete the donation transaction. The PaymentFragment is responsible for handling the donation process and confirming the payment.


### Adding a Donation

Users can add a new donation to the app by following these steps:

1. Open the app and navigate to the "Add Donation" section by clicking the FAB button (+).
2. Fill in the required information, such as the category, donation title, description, creator, image and PayPal address.
3. Submit the donation details to add it to the app's database (Google Firestore).


### Like Function

The "Like" function allows users to express their support for specific donations. Here's how it works:

1. Browse through the list of donations.
2. When you find a donation that you'd like to support, click the heart-shaped "Like" button associated with that donation.
3. The donation will be added to your list of liked donations for easy access and reference.


### Payment

Users can make a donation to support a cause they care about:

1. Click on a donation from the list to view its details.
2. Click the "Donate Now" FAB button (credit card icon).
3. You will be guided through a payment process to complete your donation using PayPal as payment method.


### Contributing

Please report any issues or bugs through our [Issue Tracker](https://github.com/devWhizz/LifeChanger/issues).

