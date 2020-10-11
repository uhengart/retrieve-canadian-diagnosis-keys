# retrieve-canadian-diagnosis-keys
retrieveDiagnosisKeys.pl is a Perl script for retrieving the diagnosis keys (aka temporary exposure keys) uploaded by [COVID Alert](https://github.com/cds-snc/covid-alert-app), Canada's exposure notifications app, from Health Canada's server.

Run the script and it will create a directory whose name is the current time (in UTC) with the following files:
* *config.json* containing the current configuration for COVID Alert.
* *all.zip* containing all currently available diagnosis keys.
* 14 files containing all currently available diagnosis keys with the name of the file indicating the day when the keys in the file were uploaded. These files may not contain all keys uploaded on a particular day since keys valid on a day before the upload day may no longer be available. Of course, today's file will contain only the keys uploaded so far today.

You can use the [diagnosis-keys tools](https://github.com/mh-/diagnosis-keys) for analyzing the downloaded diagnosis keys. 

DetermineDiagnosisKeysURL.java is a Java program that outputs the URL to download all available diagnosis keys (if the program is given no argument) or the URL to download the diagnosis keys uploaded n days ago (where 0 <= n < 14 is given as an argument to the program)
