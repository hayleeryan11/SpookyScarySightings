Spooky Scary Sightings

App made for TCSS 450 at University of Washington Tacoma  
Made by Group 4  
Haylee Ryan, Matthew Frazier, and Kai Stansfield

* Test with Pixel XL (1440 x 2560)


Features


* Account creation with proper backend checks
* Ability to sign in using a username and password in the database
* Ability to access homepage upon logging in (opens to feed of sightings)
* Ability to view a list of sightings that are in database (View Sightings)
* Menu up top that gives an About dialog and takes you to preferences
* Navigation drawer for accessing different parts of the app
* Ability to report a sighting and put it in the database 
* The ability to see details about the monsters in the database and go to a link providing more information
* Ability to search through list of sightings by multiple fields
* Ability to search through list of monsters by name
* Ability to search through users by multiple fields
* User can view other people's profiles and their list of sightings
* User can alter the bio and favorite monster in their profile view
* Profile and sighting pictures are able to be uploaded and downloaded from the database

* Ability to share your sighting with other's via email, text message, ect.  
**This is implemented in our SignedInActivity for our ReportFragment. One a user wants to post a sighting, a dialog will ask if they want to share it**

* Data will remain on device even when no internet connection  
**This is implemented for the list of Sightings and Monsters. There is a database created for each (SightingsDB, MonsterDB).**

User Stories Implemented

* As a Monster Hunter, I want to be able to sign in so I can report and view monster sightings on the app.
* As a potential Monster Hunter, I want to be able to create a profile so that I can start tracking monsters. 
* As a Monster Hunter, I want to be able to see my previous monster reportings to show my friends.
* As a Monster Hunter, I want to filter the sightings by monster type so I can easily track only the monsters I want to find.
* As a Monster Hunter, I want to filter the sightings by location so I can find only the monsters close enough to me to travel to.
* As a Monster Hunter, I want to filter the sightings by Monster Hunters so I can see where specific users have had sightings.
* As a Monster Hunter, I want to review other monster hunters’ encounter data, stories, and photos so I can learn more about the monsters that other people are seeing.
* As a Monster Hunter, I want to share my discoveries to others using social media, email, or text so I can brag to my friends.
* As a Monster Hunter, I want to see basic information, photos, and resources to learn more about a certain monster.
* As a Monster Hunter, I want to be able to update my app profile in case I have information that changes.

Bug Fixes 
 
* Creating and account works. The database was altered before the code, so it did not complete registration for a day.
* Date and time picker added in for reporting a sighting, making it more intuitive.
* Naming conventions and javadoc fixed that were irrelevant to the app.
* Toasts are more detailed to the error of success that triggers them.
* Unique titles for activities/fragments
* Sightings, profile, and monster lists (and their details) more user friendly and readable.
* Pictures are fully implemented




