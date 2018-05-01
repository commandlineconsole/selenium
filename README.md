# Test_DVLA

# PROJECT

Part 1:

Write a Service layer bean to do the following:

Scan configured directory in file system which will return this information --> filename, file mime type, file size, file extension

Use a directory containing a reasonably large number of files, minimum 10.

Provide a way to retrieve certain supported mime type files: configure excel and csv are supported currently

Part 2:

Write a selenium/cucumber framework to do the following:

Use the above service layer bean to get supported files (excel or csv are supported, from input directory)

Go through the file and read vehicle registration details in the file.

Open webpage : https://www.gov.uk/get-vehicle-information-from-dvla and go through all vehicles from excel/csv file.

On the Vehicle details page assert the details (Make/Color) match with expected output in excel/csv file.


# EXECUTION

Install Maven 3

Run chmod +x utilities/chromedriver

Execute 'mvn compile test' in the project directory.

See the captured screenshots in 'screenshots' directory.
"# selenium" 
