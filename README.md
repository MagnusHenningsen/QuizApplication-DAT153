NB! Burde brukes i darkmode, ellers ser man ikkje tekst under bilder i galleriet. Gidder ikkje fikse dette nå

# Tests
### Main Activity Test
> 1. Clicks the gallery button and checks that the display is indeed "Gallery" afterwards
![MainActivity Test Result](MainActivityTestResult.png)
### Quiz Test
> 1. Initializes the viewModel in the application context to avoid errors down the line as this is normally done in the main activity
> 2. Launches the quiz activity and gets the current "answer"
> 3. Collects rounds played / won from the shared preferences
> 4. Selects the correct option based on the name of the current answer
> 5. Collects rounds played / won from the shared preferences again
> 6. Compares the score before and after and returns the result
>> 6.1 this should in most cases work unless the name is somehow altered when put on the button.
![Quiz Test Result](QuizTestResult.png)
### Gallery Test
> 1. Initializes the viewModel in the application context to avoid errors down the line as this is normally done in the main activity
> 2. Launches the Gallery activity
> 3. Gets the size of the image list before a new entry
> 4. Creates a mock URI
> 5. Types "Mock Name" into the name field
> 6. Sets the mock URI as result data from the next ACTION_OPEN_DOCUMENT intent
> 7. Clicks the button that will use this intent to collect an image
> 8. Clicks the submit button
> 9. Compares the current size of the image list with the previous size
>> 9.1 if the image didn't exist before, it should return true
![Gallery Test Result](GalleryTestResult.png)
# APKs
> The main apk being used seems to be app-debug.apk
> The output seems to give me all details about the application, including the manifest file, resource files and similar.
> The command also produced alot of 'smali' folders, which I don't know what are.
>> in short: The decode command seems to return the reversed build version of the app.
>> Meaning android studio builds it to something android will understand, debug recovers the files to some degree, making it readable I assume.
